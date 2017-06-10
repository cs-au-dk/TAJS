package dk.brics.tajs.test;

import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree;
import com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.js2flowgraph.ASTInfo;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

public class TestASTInfo {

    @SafeVarargs
    @SuppressWarnings("varargs")
    private static <T> Set<T> set(T... vs) {
        return newSet(Arrays.asList(vs));
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

    @Test
    public void variableNamedPropertyAccesses() {
        ASTInfo astInfo = makeInfo("o[r] = o[w]; o[r] = 42; o[w];");
        Map<FunctionDeclarationTree, Set<MemberLookupExpressionTree>> map = astInfo.getFunctionsWithVariablesAsPropertyAccessName();
        assertEquals(1, map.size());
        Set<MemberLookupExpressionTree> accesses = map.values().iterator().next();
        assertEquals(4, accesses.size());
    }

    @Test
    public void correlatedDynamicPropertyAccesses() {
        ASTInfo astInfo = makeInfo("function f(){o[x] = o[x]; o[y] = 42; o[z];} function g(){}");
        Map<FunctionDeclarationTree, Set<String>> map = astInfo.getFunctionsWithVariableCorrelatedPropertyAccesses();
        assertEquals(1, map.size());
        assertEquals(1, map.values().iterator().next().size());
        assertEquals("x", map.values().iterator().next().iterator().next());
    }

    @Test
    public void conditionVariableReads1() {
        ASTInfo astInfo = makeInfo("if(x){}");
        assertEquals(set("x"), singleton(astInfo.getConditionsWithVariableReadsInTheCondition().values()));
    }

    @Test
    public void conditionVariableReads2() {
        ASTInfo astInfo = makeInfo("if(x){}if(y){}");
        assertEquals(set(set("x"), set("y")), newSet(astInfo.getConditionsWithVariableReadsInTheCondition().values()));
    }

    @Test
    public void conditionVariableReads3() {
        ASTInfo astInfo = makeInfo("if(x){if(y){}}");
        assertEquals(set(set("x"), set("y")), newSet(astInfo.getConditionsWithVariableReadsInTheCondition().values()));
    }

    @Test
    public void conditionBodyVariableReads1() {
        ASTInfo astInfo = makeInfo("if(_){x}");
        assertEquals(set("x"), singleton(astInfo.getConditionsWithVariableReadsInTheBodies().values()));
    }

    @Test
    public void conditionBodyVariableReads2() {
        ASTInfo astInfo = makeInfo("if(_){x}if(_){y}");
        assertEquals(set(set("x"), set("y")), newSet(astInfo.getConditionsWithVariableReadsInTheBodies().values()));
    }

    @Test
    public void conditionBodyVariableReads3() {
        ASTInfo astInfo = makeInfo("if(_){if(_){x}y}");
        assertEquals(set(set("x"), set("_", "x", "y")), newSet(astInfo.getConditionsWithVariableReadsInTheBodies().values()));
    }

    @Test
    public void conditionBodyVariableReads4() {
        ASTInfo astInfo = makeInfo("if(_){}else{x}");
        assertEquals(set("x"), singleton(astInfo.getConditionsWithVariableReadsInTheBodies().values()));
    }

    @Test
    public void conditionBodyVariableReads5() {
        ASTInfo astInfo = makeInfo("if(_){}else if(_){x}");
        assertEquals(set(set("_", "x"), set("x")), newSet(astInfo.getConditionsWithVariableReadsInTheBodies().values()));
    }

    @Test
    public void conditionVariableArgumentReads1() {
        ASTInfo astInfo = makeInfo("if(f(x)){}");
        assertEquals(set(Arrays.asList("x")), singleton(astInfo.getConditionsWithVariableReadsAsArgumentsInTheCondition().values()));
    }

    @Test
    public void conditionVariableArgumentReads2() {
        ASTInfo astInfo = makeInfo("if(f(x)){}if(f(y)){}");
        assertEquals(set(set(Arrays.asList("x")), set(Arrays.asList("y"))), newSet(astInfo.getConditionsWithVariableReadsAsArgumentsInTheCondition().values()));
    }

    @Test
    public void conditionVariableArgumentReads3() {
        ASTInfo astInfo = makeInfo("if(f(x, y)){if(f(z)){}}");
        assertEquals(set(set(Arrays.asList("x", "y")), set(Arrays.asList("z"))), newSet(astInfo.getConditionsWithVariableReadsAsArgumentsInTheCondition().values()));
    }

    @Test
    public void conditionVariableArgumentReads4() {
        ASTInfo astInfo = makeInfo("if(f(x, y) || f(z)){}");
        assertEquals(set(set(Arrays.asList("x", "y"), Arrays.asList("z"))), newSet(astInfo.getConditionsWithVariableReadsAsArgumentsInTheCondition().values()));
    }

    @Test
    public void conditionRefinedVariables1() {
        ASTInfo astInfo = makeInfo("if(x){x}");
        assertEquals(set("x"), singleton(astInfo.getConditionRefinedVariables().values()));
    }

    @Test
    public void conditionRefinedVariables2() {
        ASTInfo astInfo = makeInfo("if(x){x}if(y){}if(_){z}");
        assertEquals(set("x"), singleton(astInfo.getConditionRefinedVariables().values()));
    }

    @Test
    public void conditionRefinedArgumentVariables1() {
        ASTInfo astInfo = makeInfo("if(f(x)){x}");
        assertEquals(set(Arrays.asList("x")), singleton(astInfo.getConditionRefinedArgumentVariables().values()));
    }

    @Test
    public void conditionRefinedArgumentVariables2() {
        ASTInfo astInfo = makeInfo("if(f(x, y, z)){x; z}");
        assertEquals(set(Arrays.asList("x", null, "z")), singleton(astInfo.getConditionRefinedArgumentVariables().values()));
    }

    @Test
    public void conditionRefinedArgumentVariables3() {
        ASTInfo astInfo = makeInfo("if(f(x, y) && g(z)){x; y; z;}");
        assertEquals(set(Arrays.asList("x", "y"), Arrays.asList("z")), singleton(astInfo.getConditionRefinedArgumentVariables().values()));
    }

    @Test
    public void nonStackVariables0() {
        ASTInfo astInfo = makeInfo("function f(){}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables1() {
        ASTInfo astInfo = makeInfo("function f(){v}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables2() {
        ASTInfo astInfo = makeInfo("function f(v){}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables3() {
        ASTInfo astInfo = makeInfo("function f(v){v}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables4() {
        ASTInfo astInfo = makeInfo("function f(){ function g(v){v}}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables5() {
        ASTInfo astInfo = makeInfo("function f(){ function g(){v}}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables6() {
        ASTInfo astInfo = makeInfo("function f(v){ function g(v){v}}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables7() {
        ASTInfo astInfo = makeInfo("function f(v){ v; function g(v){v}}");
        assertEquals(newMap(), astInfo.getNonStackVariables());
    }

    @Test
    public void nonStackVariables8() {
        ASTInfo astInfo = makeInfo("function f(v){ function g(){v}}");
        assertEquals(set("v"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables9() {
        ASTInfo astInfo = makeInfo("function f(v1){ function g(){v1} function h(v2){v2}}");
        assertEquals(set("v1"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables10() {
        ASTInfo astInfo = makeInfo("function f(v1, v2){ function g(){v1} function h(){v2}}");
        assertEquals(set("v1", "v2"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables11() {
        ASTInfo astInfo = makeInfo("function f(){ var v; function g(){v}}");
        assertEquals(set("v"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables12() {
        ASTInfo astInfo = makeInfo("function f(v1, v2){ function g(){v1; function h(){v2}}}");
        assertEquals(set("v1", "v2"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables13() {
        ASTInfo astInfo = makeInfo("function f(v1){ function g(v2){v1; function h(){v2}}}");
        assertEquals(set("v1", "v2"), extractNonStackVariables(astInfo));
    }

    @Test
    public void nonStackVariables14() {
        ASTInfo astInfo = makeInfo("function f(){ for(var v in o){} function g(){v;}}");
        assertEquals(set("v"), extractNonStackVariables(astInfo));
    }

    private Set<String> extractNonStackVariables(ASTInfo astInfo) {
        return astInfo.getNonStackVariables().values().stream().flatMap(Set::stream).collect(Collectors.toSet());
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
        SourceLocation.SyntheticLocationMaker sourceLocationMaker = new SourceLocation.SyntheticLocationMaker("synthetic");
        FlowGraphBuilder builder = FlowGraphBuilder.makeForMain(sourceLocationMaker);
        builder.transformStandAloneCode(source, sourceLocationMaker);
        builder.close();
        return builder.getAstInfo();
    }
}
