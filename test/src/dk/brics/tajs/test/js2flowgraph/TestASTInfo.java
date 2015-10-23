package dk.brics.tajs.test.js2flowgraph;

import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.js2flowgraph.ASTInfo;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import dk.brics.tajs.util.Collections;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestASTInfo {

    @SafeVarargs
    private static <T> Set<T> set(T... vs) {
        return Collections.newSet(Arrays.asList(vs));
    }

    private static <T> T singleton(Collection<T> vs) {
        if (vs.size() != 1) {
            throw new AssertionError("Collection is not a singleton.");
        }
        return vs.iterator().next();
    }

    @Test
    public void closureVariable() {
        ASTInfo astInfo = makeInfo("var x; function f(){ x }");
        assertEquals(set("x"), singleton(astInfo.getFunctionClosureVariables().values()));
    }

    @Test
    public void closureVariables() {
        ASTInfo astInfo = makeInfo("var x, y; function f(){ x; y }");
        assertEquals(set("x", "y"), singleton(astInfo.getFunctionClosureVariables().values()));
    }

    @Test
    public void closureVariablesForParameters() {
        ASTInfo astInfo = makeInfo("function g(y){ var x; function f(){ x; y }}");
        assertEquals(set("x", "y"), getFunctionEntry("f", astInfo.getFunctionClosureVariables()));
    }

    @Test
    public void closureVariablesOuter() {
        ASTInfo astInfo = makeInfo("function h(){ var y; function g(){ var x; function f(){ x; y }}}");
        assertEquals(set("x", "y"), getFunctionEntry("f", astInfo.getFunctionClosureVariables()));
    }

    @Test
    public void dprLoop() {
        ASTInfo astInfo = makeInfo("while(true){o[i];}");
        assertEquals(1, astInfo.getFunctionsOrLoopsWithDynamicPropertyReads().size());
    }

    @Test
    public void dprLoopNested() {
        ASTInfo astInfo = makeInfo("while(true){while(true){o[i];}}");
        assertEquals(1, astInfo.getFunctionsOrLoopsWithDynamicPropertyReads().size());
    }

    @Test
    public void dprFunction() {
        ASTInfo astInfo = makeInfo("function f(){o[i];}");
        assertEquals(1, astInfo.getFunctionsOrLoopsWithDynamicPropertyReads().size());
    }

    @Test
    public void dprFunctionNested() {
        ASTInfo astInfo = makeInfo("function f(){function g(){o[i];}}");
        assertEquals(1, astInfo.getFunctionsOrLoopsWithDynamicPropertyReads().size());
    }

    @Test
    public void dprFunctionTopLevel() {
        ASTInfo astInfo = makeInfo("o[i]");
        assertEquals(1, astInfo.getFunctionsOrLoopsWithDynamicPropertyReads().size());
    }

    @Test
    public void forInLiteralObject() {
        ASTInfo astInfo = makeInfo("for(p in o){({})}");
        assertEquals(1, astInfo.getLiteralsInForIn().size());
    }

    @Test
    public void forInLiteralObjectInFunction() {
        ASTInfo astInfo = makeInfo("function f(){for(p in o){({})}}");
        assertEquals(1, astInfo.getLiteralsInForIn().size());
    }

    @Test
    public void forInLiteralArray() {
        ASTInfo astInfo = makeInfo("for(p in o){([])}");
        assertEquals(1, astInfo.getLiteralsInForIn().size());
    }

    @Test
    public void forInLiteralObjects() {
        ASTInfo astInfo = makeInfo("for(p in o){({});({})}");
        assertEquals(2, astInfo.getLiteralsInForIn().size());
    }

    @Test
    public void loopConditionVariable() {
        ASTInfo astInfo = makeInfo("for(;x;){}");
        assertEquals(set("x"), singleton(astInfo.getLoopConditionVariableReads().values()));
    }

    @Test
    public void loopConditionVariables() {
        ASTInfo astInfo = makeInfo("for(;x && y;){}");
        assertEquals(set("x", "y"), singleton(astInfo.getLoopConditionVariableReads().values()));
    }

    @Test
    public void loopDPRBody() {
        ASTInfo astInfo = makeInfo("for(;;){o[x]}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopDPRsBody() {
        ASTInfo astInfo = makeInfo("for(;;){o[x]; o[y]}");
        assertEquals(set("x", "y"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopDPRsBodyNonStandard() {
        ASTInfo astInfo = makeInfo("for(;;){o[x[y]]}");
        assertEquals(set("x", "y"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopDPRInc() {
        ASTInfo astInfo = makeInfo("for(;;){o[x++]}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopDPRWhile() {
        ASTInfo astInfo = makeInfo("while(true){o[x]}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopDPRsUpdate() {
        ASTInfo astInfo = makeInfo("for(;;o[x]){}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerDynamicPropertyVariableReads().values()));
    }

    @Test
    public void loopVarWriteBody() {
        ASTInfo astInfo = makeInfo("for(;;){x = 42}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerVariableWrites().values()));
    }

    @Test
    public void loopVarWriteBodyInc() {
        ASTInfo astInfo = makeInfo("for(;;){x++}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerVariableWrites().values()));
    }

    @Test
    public void loopVarWriteUpdate() {
        ASTInfo astInfo = makeInfo("for(;;x++){}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerVariableWrites().values()));
    }

    @Test
    public void loopVarWriteBodyWhile() {
        ASTInfo astInfo = makeInfo("while(true){x = 42}");
        assertEquals(set("x"), singleton(astInfo.getLoopNonInitializerVariableWrites().values()));
    }

    @Test
    public void loopNestingNone() {
        ASTInfo astInfo = makeInfo("");
        assertEquals(0, astInfo.getNestedLoops().size());
    }

    @Test
    public void loopNesting0() {
        ASTInfo astInfo = makeInfo("for(;;){}");
        assertEquals(0, astInfo.getNestedLoops().size());
    }

    @Test
    public void loopNesting1() {
        ASTInfo astInfo = makeInfo("for(;;){for(;;){}}");
        assertEquals(1, astInfo.getNestedLoops().size());
    }

    @Test
    public void loopNesting2() {
        ASTInfo astInfo = makeInfo("for(;;){for(;;){}}for(;;){for(;;){}}");
        assertEquals(2, astInfo.getNestedLoops().size());
    }

    @Test
    public void loopNesting2nested() {
        ASTInfo astInfo = makeInfo("for(;;){for(;;){for(;;){}}}");
        assertEquals(2, astInfo.getNestedLoops().size());
    }

    @Test
    public void literalObjectVariable() {
        ASTInfo astInfo = makeInfo("({p: x})");
        assertEquals(set("x"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    @Test
    public void literalObjectVariables() {
        ASTInfo astInfo = makeInfo("({p1: x, p2: y})");
        assertEquals(set("x", "y"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    @Test
    public void literalArrayVariable() {
        ASTInfo astInfo = makeInfo("({p: x})");
        assertEquals(set("x"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    @Test
    public void literalObjectVariableIndirect0() {
        ASTInfo astInfo = makeInfo("({p: (x)})");
        assertEquals(set("x"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    @Test
    public void literalObjectVariableIndirect1() {
        ASTInfo astInfo = makeInfo("({p: (x++)})");
        assertEquals(set("x"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    @Test
    public void literalObjectVariableIndirect2() {
        ASTInfo astInfo = makeInfo("({p: (42? x: 12)})");
        assertEquals(set("x"), singleton(astInfo.getVariableReadsInLiterals().values()));
    }

    private <T> T getFunctionEntry(String name, Map<FunctionDeclarationTree, T> map) {
        for (Map.Entry<FunctionDeclarationTree, T> functionDeclarationTreeTEntry : map.entrySet()) {
            FunctionDeclarationTree declaration = functionDeclarationTreeTEntry.getKey();
            if (declaration.name != null && name.equals(declaration.name.toString())) {
                return functionDeclarationTreeTEntry.getValue();
            }
        }
        throw new AssertionError("No such function name: " + name);
    }

    private ASTInfo makeInfo(String source) {
        FlowGraphBuilder builder = new FlowGraphBuilder();
        builder.transformStandAloneCode(JavaScriptSource.makeFileCode("dummy.js", source));
        builder.close();
        return builder.getAstInfo();
    }
}
