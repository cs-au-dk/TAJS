/*
 * Copyright 2009-2020 Aarhus University
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

package dk.brics.tajs.analysis.uneval;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.unevalizer.AnalyzerCallback;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Strings;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Utility routines for the unevalizer parts.
 *
 * @author simonhj
 */
public class UnevalTools {

    private static final String TEMPORARY = "TAJS_TEMPORARY";

    public static String VAR_PLACEHOLDER = "<!>notsyntax<!>(";

    /**
     * Rebuilds the expression used to create the value in a register from the flow graph.
     */
    public static String rebuildFullExpression(FlowGraph fg, AbstractNode n, int register) {
        Decorator dr = new Decorator(n.getBlock().getFunction()); //TODO Cache the decorator.

        return p_build_full(register, n, dr);
    }

    /**
     * Returns the normal form expression used to create the first argument to the CallNode.
     */
    public static NormalForm rebuildNormalForm(FlowGraph fg, CallNode n, State s, Solver.SolverInterface c) {
        int register = n.getNumberOfArgs() == 0? AbstractNode.NO_VALUE: n.getArgRegister(0);
        Decorator dr = new Decorator(n.getBlock().getFunction()); //TODO Cache the decorator.

        Map<String, Integer> mapping = Collections.newMap();
        Set<String> args = newSet();
        String nf = p_build_nf(register, n, dr, mapping, args, Collections.newSet(), s, c, false);
        return new NormalForm(nf, mapping, args);
    }

    /**
     * Rebuild normal form by backwards traversal in the flow graph.
     */
    private static String p_build_nf(int var, AbstractNode node, Decorator dr, Map<String, Integer> mapping, Set<String> arguments, Set<Integer> seen_blocks, State s, Solver.SolverInterface c, boolean effects_only) {
        BasicBlock bb = node.getBlock();
        List<AbstractNode> ns = bb.getNodes();
        int idx = ns.indexOf(node);

        while (idx >= 0) {
            AbstractNode n = ns.get(idx);

            if (!effects_only && n instanceof LoadNode && ((LoadNode) n).getResultRegister() == var) {
                if (s.isRegisterDefined(var)) {
                    Value v = Conversion.toString(UnknownValueResolver.getRealValue(s.readRegister(var), s), c);
                    if (v.isMaybeSingleStr()) {
                        // We know the value of the register but we still need to traverse the rest of the flow graph to get
                        // the proper context sensitivity set up.  We don't care about the return value though; throw it away.
                        p_build_nf(var, node, dr, mapping, arguments, seen_blocks, s, c, true);
                        return "\"" + Strings.escapeSource(v.getStr()) + "\"";
                    }
                }
            }

            if (n instanceof BinaryOperatorNode) {
                BinaryOperatorNode bop = (BinaryOperatorNode) n;
                if (bop.getResultRegister() == var) {
                    String left = p_build_nf(bop.getArg1Register(), n, dr, mapping, arguments, seen_blocks, s, c, effects_only);
                    String right = p_build_nf(bop.getArg2Register(), n, dr, mapping, arguments, seen_blocks, s, c, effects_only);
                    if (left.endsWith("\"") && right.startsWith("\""))
                        return left.substring(0, left.length() - 1) + right.substring(1);
                    return left + " " + bop.operatorToString() + " " + right;
                }
            } else if (n instanceof UnaryOperatorNode) {
                UnaryOperatorNode un = (UnaryOperatorNode) n;
                if (un.getArgRegister() == var)
                    return paren(un.getOperator() + paren(p_build_nf(un.getArgRegister(), node, dr, mapping, arguments, seen_blocks, s, c, effects_only)));
            } else if (n instanceof ConstantNode) {
                ConstantNode cons = (ConstantNode) n;
                if (cons.getResultRegister() == var) {
                    switch (cons.getType()) {
                        case BOOLEAN:
                            return String.valueOf(cons.getBoolean());
                        case NULL:
                            return "null";
                        case NUMBER:
                            return String.valueOf(cons.getNumber());
                        case STRING:
                            return "\"" + cons.getString() + "\"";
                        case UNDEFINED:
                            return "undefined";
                    }
                }
            } else if (n instanceof WriteVariableNode && arguments.contains(((WriteVariableNode) n).getVariableName()) && !seen_blocks.contains(n.getBlock().getIndex())) {
                int valueReg = ((WriteVariableNode) n).getValueRegister();
                // Remember that we have traversed this block already, in case of cyclic flow graphs.
                seen_blocks.add(n.getBlock().getIndex());
                if (idx > 0)
                    p_build_nf(valueReg, ns.get(idx - 1), dr, mapping, arguments, seen_blocks, s, c, true);
                else {
                    for (BasicBlock p_bb : dr.getPredecessorBlocks(bb))
                        if (!seen_blocks.contains(p_bb.getIndex()))
                            p_build_nf(valueReg, p_bb.getLastNode(), dr, mapping, arguments, seen_blocks, s, c, true);
                }
            } else if (n instanceof LoadNode && ((LoadNode) n).getResultRegister() == var) {
                if (n instanceof ReadVariableNode) {
                    String varname = ((ReadVariableNode) n).getVariableName();
                    if (!arguments.contains(varname) && !"this".equals(varname)) {
                        arguments.add(varname);
                        if (idx > 0)
                            p_build_nf(var, ns.get(idx - 1), dr, mapping, arguments, seen_blocks, s, c, true);
                        else {
                            for (BasicBlock p_bb : dr.getPredecessorBlocks(bb))
                                if (!seen_blocks.contains(p_bb.getIndex()))
                                    p_build_nf(var, p_bb.getLastNode(), dr, mapping, arguments, seen_blocks, s, c, true);
                        }
                    }
                }
                String f_var = fresh_var(var);
                mapping.put(f_var, var);
                return f_var;
            }
            idx--;
        }

        StringBuilder sb = new StringBuilder();
        for (BasicBlock predecessor : dr.getPredecessorBlocks(bb))
            if (!seen_blocks.contains(predecessor.getIndex())) {
                AbstractNode lastNode = predecessor.getLastNode();
                if (!(lastNode instanceof EventDispatcherNode)) {
                    sb.append(p_build_nf(var, lastNode, dr, mapping, arguments, seen_blocks, s, c, effects_only));
                }
                seen_blocks.add(predecessor.getIndex());
            }
        return effects_only ? "" : sb.toString();
    }

    /**
     * Generate a JavaScript variable for a TAJS register.
     */
    private static String fresh_var(int temp) {
        return TEMPORARY + "_" + temp;
    }

    /**
     * Rebuild the full expression for a register by backwards traversal of the flow graph.
     */
    private static String p_build_full(int var, AbstractNode start_node, Decorator dr) {

        Set<BasicBlock> p_bbs = newSet();
        BasicBlock bb = start_node.getBlock();
        p_bbs.addAll(dr.getPredecessorBlocks(bb));
        List<AbstractNode> ns = bb.getNodes();
        int idx = ns.indexOf(start_node);

        while (idx >= 0) {
            AbstractNode n = ns.get(idx);
            if (n instanceof BinaryOperatorNode) {
                BinaryOperatorNode bop = (BinaryOperatorNode) n;
                if (bop.getResultRegister() == var) {
                    return paren(p_build_full(bop.getArg1Register(), n, dr)
                            + " " + bop.operatorToString() + " " + p_build_full(bop.getArg2Register(), n, dr));
                }
            } else if (n instanceof UnaryOperatorNode) {
                UnaryOperatorNode un = (UnaryOperatorNode) n;
                if (un.getResultRegister() == var) {
                    return paren(un.getOperator() + paren(p_build_full(un.getArgRegister(), n, dr)));
                }
            } else if (n instanceof CallNode) {
                CallNode call = (CallNode) n;
                if (call.getResultRegister() == var) {
                    StringBuilder b = new StringBuilder();
                    b.append("(");
                    if (call.getFunctionRegister() != AbstractNode.NO_VALUE) {
                        b.append(p_build_full(call.getFunctionRegister(), n, dr));
                    } else {
                        b.append(paren(p_build_full(call.getBaseRegister(), n, dr)));
                        if (call.getPropertyString() != null)
                            b.append(".").append(call.getPropertyString());
                        else
                            b.append("[").append(p_build_full(call.getPropertyRegister(), n, dr)).append("]");
                    }
                    b.append(")");
                    b.append("(");
                    for (int i = 0; i < call.getNumberOfArgs(); i++) {
                        b.append(paren(p_build_full(call.getArgRegister(i), n, dr)));
                        if (!(i == call.getNumberOfArgs() - 1))
                            b.append(",");
                    }
                    b.append(")");
                    return b.toString();
                }
            } else if (n instanceof ConstantNode) {
                ConstantNode cons = (ConstantNode) n;
                if (cons.getResultRegister() == var) {
                    switch (cons.getType()) {
                        case BOOLEAN:
                            return String.valueOf(cons.getBoolean());
                        case NULL:
                            return "null";
                        case NUMBER:
                            return String.valueOf(cons.getNumber());
                        case STRING:
                            return "\"" + cons.getString() + "\"";
                        case UNDEFINED:
                            return "undefined";
                    }
                }
            } else if (n instanceof ReadVariableNode) {
                ReadVariableNode rn = (ReadVariableNode) n;
                if (rn.getResultRegister() == var)
                    return rn.getVariableName();
            } else if (n instanceof ReadPropertyNode) {
                ReadPropertyNode rn = (ReadPropertyNode) n;
                if (rn.getResultRegister() == var) {
                    if (rn.isPropertyFixed()) {
                        return paren(p_build_full(rn.getBaseRegister(), n, dr)) + "." + rn.getPropertyString();
                    } else {
                        return paren(p_build_full(rn.getBaseRegister(), n, dr)) + "[" + paren(p_build_full(rn.getPropertyRegister(), n, dr)) + "]";
                    }
                }
            } else if (n instanceof NewObjectNode) {
                NewObjectNode no = (NewObjectNode) n;
                if (no.getResultRegister() == var) {
                    return "{}";
                }
            } else if (n instanceof WriteVariableNode || n instanceof WritePropertyNode) {
                return "";
            }
            idx--;
        }
        StringBuilder sb = new StringBuilder();
        for (BasicBlock p_bb : p_bbs) {
            AbstractNode lastNode = p_bb.getLastNode();
            if (!(lastNode instanceof EventDispatcherNode)) {
                sb.append(p_build_full(var, lastNode, dr));
            }
        }
        return sb.toString();
    }

    /**
     * Generate balanced parentheses around s.
     */
    private static String paren(String s) {
        return "(" + s + ")";
    }

    /**
     * Rebuild a full expression from a normal form and a mapping.
     */
    public static String rebuildFullFromMapping(FlowGraph fg, String unevaled,
                                                Map<String, Integer> mapping, AbstractNode n) {
        String res = unevaled;
        for (String s : mapping.keySet())
            res = res.replace(s, paren(rebuildFullExpression(fg, n, mapping.get(s))));
        return res;
    }

    /**
     * Returns a callback object for the transformation component.
     * In the uneval paper the input to the transformation component is an 8-tuple (E,V,D_G,D_L,r,p,n).
     * This callback allows the transformer to access most of these components:
     * E: This is passed directly to the transformer (see Unevalizer.uneval)
     * E is a String that contains a JavaScript expression on normal form.  It is constructed by TAJS and is a concatenation
     * of either constants or identifiers. For example: eval("x = " + (f() + "yy")) will have the normal form "\"x = \" + TAJS_ID42"
     * where TAJS_ID42 is a place holder identifier representing the value of the subexpression.
     * V: The transformer can query information about the abstract value of the place holders using the CallBack functions.
     * D_G,D_L: The functions anyGlobalVariable and getNonGlobalIdentifiers allow access to this
     * D_M: TODO: Not implemented.
     * r: Direct parameter in the call to uneval() in JSGlobal and others.
     * p: TODO: Not implemented.
     * n: TODO: Not implemented.
     */
    public static AnalyzerCallback unevalizerCallback(final FlowGraph fg, final Solver.SolverInterface c, final AbstractNode evalCall, final NormalForm input, boolean isEvalCall) {
        final State state = c.getState();

        return new AnalyzerCallback() {

            /**
             * Returns true of there is a name clash between declared variables and the argument set.
             */
            @Override
            public boolean anyDeclared(Set<String> vars) {
                if (!isEvalCall)
                    return false;
                for (String ss : vars) {
                    if (c.getAnalysis().getPropVarOperations().readVariable(ss, null).isMaybePresent())
                        return true;
                }
                return false;
            }

            /**
             * Returns true if placeholder is JSON data.
             */
            @Override
            public boolean isDefinitelyJSONData(String placeholder) {
                Value v = state.readRegister(input.getMapping().get(placeholder));
                v = UnknownValueResolver.getRealValue(v, state);
                v = v.restrictToStr();
                return v.isStrJSON();
            }

            /**
             * Returns true if placeholder is an IdentiferFragment.
             */
            @Override
            public boolean isDefinitelyIdentifierFragment(String placeholder) {
                Value v = state.readRegister(input.getMapping().get(placeholder));
                v = UnknownValueResolver.getRealValue(v, state);
                v = v.restrictToStr();
                return v.isStrIdentifierParts();
            }

            /**
             * Returns true if placeholder is an IdentiferString.
             */
            @Override
            public boolean isDefinitelyIdentifier(String placeholder) {
                Value v = state.readRegister(input.getMapping().get(placeholder));
                v = UnknownValueResolver.getRealValue(v, state);
                v = v.restrictToStr();
                return v.isStrIdentifier();
            }

            /**
             * Return the set of bound Identifiers in the current scope, excluding ones that are bound in the global.
             * The set D_L.
             */
            @Override
            public Set<String> getNonGlobalIdentifiers() {
                Set<String> res = newSet();
                for (Set<ObjectLabel> ls : ScopeChain.iterable(state.getScopeChain()))
                    for (ObjectLabel l : ls)
                        if (!l.equals(InitialStateBuilder.GLOBAL)) {
                            if (UnknownValueResolver.getDefaultOtherProperty(l, state).isNotAbsent()) // TODO: javadoc
                                return null;
                            for (PKey propertyname : UnknownValueResolver.getProperties(l, state).keySet())
                                if (UnknownValueResolver.getProperty(l, propertyname, state, true).isMaybePresent())
                                    if (propertyname instanceof StringPKey)
                                        res.add(((StringPKey)propertyname).getStr());
                        }
                return res;
            }

            /**
             * Returns true if placeholder is a Boolean.
             */
            @Override
            public boolean isDefinitelyBoolean(String placeholder) {
                Value v = state.readRegister(input.getMapping().get(placeholder));
                v = UnknownValueResolver.getRealValue(v, state);
                return !v.isMaybeOtherThanBool();
            }

            /**
             * Returns true if placeholder is an Integer.
             */
            @Override
            public boolean isDefinitelyInteger(String placeholder) {
                Value v = state.readRegister(input.getMapping().get(placeholder));
                v = UnknownValueResolver.getRealValue(v, state);
                return !v.isMaybeOtherThanNum();
            }

            /**
             * Return the full expression (without any placeholders). Used for generating sensible error messages.
             */
            @Override
            public String getFullExpression(String ph) {
                return rebuildFullExpression(fg, evalCall, input.getMapping().get(ph));
            }
        };
    }

    /**
     * Returns the function name that was used to call the function or empty string if unknown.
     */
    public static String get_call_name(FlowGraph fg, CallNode n) {
        if (n.getFunctionRegister() == AbstractNode.NO_VALUE)
            return null; // TODO: what if eval is accessed by a read-property operation instead of a read-variable?
        return get_read_variable_node(new Decorator(n.getBlock().getFunction()), n, n.getFunctionRegister(), Collections.newSet());
    }

    /**
     * Returns the name of the variable that is loaded into a register, or null.
     */
    private static String get_read_variable_node(Decorator dr, AbstractNode start_node, int register, Set<Integer> seen_blocks) {
        List<AbstractNode> ns = start_node.getBlock().getNodes();
        int idx = ns.indexOf(start_node);

        while (idx >= 0) {
            AbstractNode n = ns.get(idx);
            if (n instanceof ReadVariableNode && ((ReadVariableNode) n).getResultRegister() == register) {
                return ((ReadVariableNode) n).getVariableName();
            }
            idx--;
        }
        StringBuilder sb = new StringBuilder();
        for (BasicBlock bb : dr.getPredecessorBlocks(start_node.getBlock())) {
            if (seen_blocks.contains(bb.getIndex()))
                continue;
            seen_blocks.add(bb.getIndex());
            String ret = get_read_variable_node(dr, bb.getLastNode(), register, seen_blocks);
            sb.append(ret == null ? "" : ret);
        }
        String res = sb.toString();
        return res.isEmpty() ? null : res;
    }

    private static Set<String> usedGenSyms = newSet();

    public static String gensym() {
        String gensym;
        while (usedGenSyms.contains(gensym = "a" + Strings.randomString(10))) ;
        usedGenSyms.add(gensym);
        // technically unsound: a variable (or variable from eval!) could be this exact gensym, but it is very unlikely
        return gensym;
    }
}
