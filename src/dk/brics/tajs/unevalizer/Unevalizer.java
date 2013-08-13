/*
 * Copyright 2009-2013 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.unevalizer;
import static com.google.javascript.rhino.Node.newString;
import static dk.brics.tajs.util.Collections.newSet;

import java.util.Set;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ErrorManager;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class Unevalizer {
	
    /**
     * Morally this is function has the type:
     * AnalyzerCallback -> EvalString -> Bool -> Maybe ResultVar -> Maybe String
     */
    public String uneval(AnalyzerCallback callback, String source, boolean aliasedEval, String resVar) {
        log("Starting on: " + source);

        // TODO: Immediately fail on aliased calls. Not hard to handle, but no real use cases yet.
        if (aliasedEval)
            return null;

        Compiler comp = parseString(source);
        Node root = comp.getRoot();

        // Input was not syntactically valid (s \notin P)
        if (comp.getErrorCount() > 0)
            throw new IllegalArgumentException("Invalid abstract expression: " + source);

        // log(comp.toSource());
        String code = getConst(root);

        if (code != null) {
            // Strip the outermost quotes on our (string) input.
            if (code.startsWith("\"") && code.endsWith("\""))
                code = code.substring(1, code.length() - 1);

            // Someone might actually call eval(""), but that is fine. Calling x = eval("") is too, return undefined.
            if (code.isEmpty())
                return resVar == null ? "" : resVar + " = undefined";

            comp = parseString(code);
            // TODO: Check for shadowing.

            // We got a constant string that wasn't valid javascript. Weird, but legal, so return a syntax error.
            if (comp.getErrorCount() > 0)
                return "throw new SyntaxError()";

            log("Valid program");

            // Basic sanity checking complete; jump to the interesting work for the constant string case.
            return unevalConst(callback, comp, resVar);
        }

        log("Not a constant string");

        // So e = ".." + x1 + ".." + x2 + ..

        // A map of all the x_n's in the input (the caller guarantees they are fresh). Used for reconstructing the
        // AST once we have constant folded them away.
        Set<String> holeNames = newSet();

        // Treat the input as e = ".." + "x1" + ".." + "x2" + .. and constant fold.
        fillHolesAndConstantFold(comp, root, holeNames);

        code = getConst(root);

        if (code != null) {
            // We got a string after constant folding. Drop the quotes and parse it.
            if (code.startsWith("\"") && code.endsWith("\""))
                code = code.substring(1, code.length() - 1);

            comp = parseString(code);
            if (comp.getErrorCount() > 0) 
                throw new IllegalArgumentException("Invalid abstract expression");

            // Uneval the input by undoing the constant folding in a clever way.
            return contractEval(callback, comp, holeNames, resVar);
        }

        log("Failed to refactor, returning null");
        return null;
    }

    /**
     * Takes a source string and returns a google closure compiler. The caller must do the error checking and act
     * accordingly.
     */
    private static Compiler parseString(String sourceString) {
        Compiler compiler = new Compiler();
        ErrorManager em = new ErrorManager() {
            private int numErrs = 0;
            private JSError err;

            @Override
            public void report(CheckLevel checkLevel, JSError jsError) {
                if (checkLevel.compareTo(CheckLevel.ERROR) == 0) {
                    numErrs++;
                    err = jsError;
                }
            }

            @Override
            public void generateReport() { }

            @Override
            public int getErrorCount() { return numErrs; }

            @Override
            public int getWarningCount() { return 0; }

            @Override
            public JSError[] getErrors() {
                // Useful for debugging and doesn't cost anything to have in here.
                JSError[] tmp = new JSError[1];
                tmp[0] = err;
                return tmp;
            }

            @Override
            public JSError[] getWarnings() { return new JSError[0]; }

            @Override
            public void setTypedPercent(double v) { }

            @Override
            public double getTypedPercent() { return 0; }
        };
        compiler.setErrorManager(em);
        CompilerOptions options = new CompilerOptions();
        // Try very hard to interpret the meaning of our code string.
        options.setIdeMode(true);
        options.setOutputCharset("UTF-8");
        // We only care about the AST, and that is gotten through "compiling" whitespace mode only.
        CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(options);

        JSSourceFile dummy = JSSourceFile.fromCode("dummy.js", "");

        compiler.compile(dummy, JSSourceFile.fromCode("input.js", sourceString), options);
        return compiler;
    }

    /**
     * Helper function that gets the constant string child from a root node if it exists.
     */
    private static String getConst(Node root) {
        Node last = root.getLastChild().getLastChild().getLastChild().getLastChild();

        if (last.isString())
            return last.getString();

        return null;
    }

    /**
     * The main function for unevaling constant strings.
     */
    private String unevalConst(AnalyzerCallback callback, Compiler comp, String resVar) {
        Set<String> boundVariables = boundVariables(comp);
        // Fail if there's name capture (bv(s) \cap (D_G \cup D_L \cup D_M) \neq \emptyset).
        if (callback.anyDeclared(boundVariables)) {
            log("Failed due to name capture");
            return null;
        }

        log("No name capture");

        // We're done if nobody cares about the result value (r == false or \mathcal{C} = \epsilon).
        if (resVar == null)
            return comp.toSource();

        log("Return value of eval is used");

        // Our input is "s1; s2; ..; sn". Get sn and detach it from the parent for closure compiler reasons.
        Node sn = getLastStmt(getParentOfFirstInterestingNode(comp));
        sn.detachFromParent();
        comp.reportCodeChange();

        // If the last statement in our input doesn't have a value we need to abort (hv(sn))
        // TODO: isExpr needed here? Remove isExpr
        if (!hasValue(sn) || !isExpr(sn)) {
            log("Last expression is NOT the value yielding one");
            return null;
         }

        log("Last expression has value");

        // Getting the program back from the compiler. A play in two acts.
        // AST -> String for s1..s(n-1)
        Compiler.CodeBuilder cb1 = new Compiler.CodeBuilder();
        // AST -> String for s_n
        Compiler.CodeBuilder cb2 = new Compiler.CodeBuilder();
        comp.toSource(cb1, 1, comp.getRoot());
        comp.toSource(cb2, 1, sn);       

        return cb1.toString() + resVar + " = " + cb2.toString();
    }

    /**
     * Returns the last statement of a sequence of statements "s1; s2;.."
     */
    private static Node getLastStmt(Node root) {
        return root.getLastChild();
    }

    /**
     * Rhino puts in some extra layers of stuff. This gets the parent of the first interesting node.
     */
    private static Node getParentOfFirstInterestingNode(Compiler comp) {
        return comp.getRoot().getLastChild().getLastChild();
    }

    /**
     * Returns true if s might be a global identifier.
     */
    private static boolean isGlobalIdentifierPrefix(AnalyzerCallback callback, String s) {
        Set<String> nonGVars = callback.getNonGlobalIdentifiers();
        if (nonGVars == null)
            return false;
        // log(nonGVars.toString() + s);
        for (String var : nonGVars) {
            if (var.startsWith(s)) return false;
        }
        return true;
    }

    /**
     * Returns true if s might be a global identifier.
     */
    private static boolean isGlobalIdentifierSuffix(AnalyzerCallback callback, String s) {
        Set<String> nonGVars = callback.getNonGlobalIdentifiers();
        if (nonGVars == null)
            return false;
        // log(nonGVars.toString() + s);
        for (String var : nonGVars) {
            if (var.endsWith(s)) return false;
        }
        return true;
    }

    /**
     * Constant folds root, on the form ".." + x1 + ".." + .., into "..x1...."
     */
    private void fillHolesAndConstantFold(Compiler compiler, Node root, Set<String> map) {
        boolean b = true;
        
        while (b) {
            b = convertNamesToStrings(root.getLastChild().getLastChild().getLastChild().getLastChild(), map);
        }
        
        b = true;
        
        while (b) {
            b = constantFoldStrings(root.getLastChild().getLastChild().getLastChild().getLastChild());
        }
        compiler.reportCodeChange();
    }

    /**
     * Recursive worker function that converts name nodes to string nodes inplace.
     */
    private boolean convertNamesToStrings(Node root, Set<String> map) {
        if (root.isName()) {
            String name = root.getString();
            map.add(name);
            Node nn = Node.newString(name);
            root.getParent().replaceChild(root, nn);
            if (root.getParent() != null)
                root.detachFromParent();
            return true;
        }
        
        boolean res = false;
        for (Node n : root.children()) {
            res |= convertNamesToStrings(n, map);
        }

        return res;                
    }

    /**
     * Constant folds strings inplace.
     */
    private boolean constantFoldStrings(Node root) {
        if (root.isAdd()) {
            Node left = root.getFirstChild();
            Node right = root.getLastChild();
            if (left.isString() && right.isString()) {
                Node nn = Node.newString(left.getString() + right.getString());
                root.getParent().replaceChild(root, nn);
                return true;
            }
        }

        boolean res = false;
        for (Node n : root.children()) {
            res |= constantFoldStrings(n);
        }

        return res;
    }

    /**
     * Undoes the constant folding previously done.
     */
    private String contractEval(AnalyzerCallback callback, Compiler comp, Set<String> map, String resVar) {
        Node stringRoot = getParentOfFirstInterestingNode(comp).getLastChild();

        // LHS of the assignment to the result of the eval
        String res;
        if (resVar == null)
            res = "";
        else
            res = resVar + " = ";
        
        // log("Map of holes: " + map.toString());
        boolean unevalSucceed = contractEvalHelper(callback, comp, stringRoot, map);
        // The helper modifies the AST.
        comp.reportCodeChange();

        if (!unevalSucceed)
            return null;

        String result = res + comp.toSource();
        
        // log("Retval:" + result);
        return result;
    }

    /**
     * Helper function that does most of the actual work for contractEval.
     */
    private boolean contractEvalHelper(AnalyzerCallback callback, Compiler comp, Node root, Set<String> holes) {

        for (Node n : root.children()) {
            if (!contractEvalHelper(callback, comp, n, holes))
                return false;

            // log("Looping: " + n.toStringTree() + ":" +  n.getClass().getSimpleName());
            // Our names ended up in name or string nodes; check all such nodes for occurrences and transform accordingly.
            if (n.isName() || n.isString()) {
                String v = n.getString();               
                for (String hole : holes) {
                    int i = v.indexOf(hole);
                    // Hole exists somewhere in the name
                    if (i != -1) {
                        Node nam = newString(Token.NAME, hole);

                        Node tmpNode1, tmpRoot;
                        // The part of the name to the left of the variable. Might be empty.
                        String lsub = v.substring(0, i);
                        // The part to the right of the variable. Might be empty.
                        String rsub = v.substring(i + hole.length(), v.length());
                        if (lsub.length() > 0) {
                            if (!(callback.isDefinitelyIdentifierFragment(hole) || callback.isDefinitelyInteger(hole))) {
                                log("Failed due to non IdentifierFragment and non Integer");
                                return false;
                            }
                            // If there isn't a global identifier starting with lsub we're smoked.
                            if (!isGlobalIdentifierPrefix(callback, lsub))  {
                                log("Failed due to local shadowing");
                                return false;
                            }
                            tmpNode1 = new Node(Token.ADD, newString(lsub), nam);
                            // Copy AST properties to the new node.
                            tmpNode1.clonePropsFrom(nam);
                        } else {
                            if (!(callback.isDefinitelyIdentifier(hole) || callback.isDefinitelyBoolean(hole) || callback.isDefinitelyInteger(hole))) {
                                log("Not an identifier for sure: " + hole);
                                return false;
                            }
                            tmpNode1 = nam;
                        }

                        if (rsub.length() > 0) {
                            if (!isGlobalIdentifierSuffix(callback, rsub)) {
                                log("Failed due to local shadowing");
                                return false;
                            }
                            // Check if there are more than one hole in this node, and if so, give up.
                            for (String hole2 : holes) {
                                if (rsub.contains(hole2)) {
                                    log("Failed due to multiple holes in same node");
                                    return false;
                                }
                            }
                            tmpRoot = new Node(Token.ADD, tmpNode1, newString(rsub));
                        } else
                            tmpRoot = tmpNode1;

                        // Replace the old node with the newly constructed tree in the AST
                        Node p = n.getParent();                       
                        p.replaceChild(n, tmpRoot);
                        comp.reportCodeChange();
                        n = tmpRoot;
                        if (!fixupParent(callback, comp, p, n, hole, lsub.isEmpty() && rsub.isEmpty()))
                             return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Stitch up the AST (from below) during the process of constant folding backwards.
     */
    private static boolean fixupParent(AnalyzerCallback callback, Compiler comp, Node parent, Node child, String name, boolean noSiblings) {
        if (parent.isGetProp()) {
            Node lname = parent.getFirstChild();
            // TODO: This should be window[lname + ..]
            if (lname == child)
                return false;
            Node tmpNode = new Node(Token.GETELEM, newString(Token.NAME, lname.getQualifiedName()));
            parent.getParent().replaceChild(parent, tmpNode);
            parent = tmpNode;
            child.detachFromParent();
            tmpNode.addChildToBack(child);
        } else if (parent.isGetElem()) {
            child.detachFromParent();
            parent.addChildToBack(child);
        } else if (noSiblings && (callback.isDefinitelyBoolean(name) || callback.isDefinitelyInteger(name))) {
            // TODO: Do we need to coerce?
            // Do nothing.
            // log(parent.toStringTree());
        } else {
            // TODO: Use (function () {return this;}) instead of window.
            Node tmpNode = new Node(Token.GETELEM, newString(Token.NAME, "window"));
            parent.replaceChild(child, tmpNode);
            tmpNode.addChildToBack(child);
        }
        // The source is printed "(0, window[..])" instead of window[..] without the FREE_CALL modification.
        // I have no idea why, but we'll leave it there for now.
        parent.putIntProp(Node.FREE_CALL, 0);
        comp.reportCodeChange();
        return true;
    }

    /**
     * Returns the bound variables in the program.
     */
    private Set<String> boundVariables(Compiler comp) {
        Node root = getParentOfFirstInterestingNode(comp);
        Set<String> bvs = newSet();
        
        bvHelper(root, bvs);

        return bvs;
    }

    /**
     * Private helper that does the actual work for bv().
     */
    private void bvHelper(Node root, Set<String> bvs) {
        for (Node n : root.children()) {
            if (n.isVar()) {
                for (Node v : n.children()) {
                    bvs.add(v.getString());
                }
            }
            bvHelper(n, bvs);
        }
    }

    /**
     * Returns true if the statement has a value.
     */
    private static boolean hasValue(Node n) {
        if (n.isExprResult())
            return true;

        return false;
    }

    /**
     * Returns true if the parameter is an expression
     */
    private static boolean isExpr(Node n) {
        if (n.isVar())
            return false;
        return true;
    }

    /**
     * Log the string s.
     */
    private static void log(String s) { // FIXME: replace calls to this method with calls to log4j
        System.out.println("Unevalizer: " + s);
    }
}
