package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.util.Pair;

import java.util.List;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;

/**
 * Operations for extending an existing flow graph.
 */
public class FlowGraphMutator {

    /**
     * Extends the given flow graph.
     *
     * @param existingFlowGraph    the existing flow graph
     * @param newSourceCode        new JavaScript code
     * @param sourceCodeIdentifier key to use for the new flow graph fragment
     * @param previousExtension    previous extension that should be replaced by this one, or null if none
     * @param extenderNode         node in the existing flow graph where the new fragment should be placed
     * @param asTimeOutEvent       if true, add the code as a timeout/interval event handler; if false, add the code as ordinary embedded code
     * @param resultVariableName   variable name for the expression result, or null if not used
     * @return new flow graph fragment
     */
    public static FlowGraphFragment extendFlowGraph(FlowGraph existingFlowGraph, String newSourceCode, String sourceCodeIdentifier,
                                                    FlowGraphFragment previousExtension, LoadNode extenderNode, boolean asTimeOutEvent, String resultVariableName) {
        BasicBlock extenderBlock = extenderNode.getBlock();
        BasicBlock extenderSuccessor = extenderBlock.getSingleSuccessor();
        Function function = extenderNode.getBlock().getFunction();
        FunctionAndBlockManager functionAndBlocksManager = new FunctionAndBlockManager();

        if (previousExtension != null) {
            // remove old extension
            existingFlowGraph.removeFunctions(previousExtension.getFunction());
            function.removeBlocks(previousExtension.getBlocks());
        }

        // prepare AstEnv
        AstEnv env = AstEnv.makeInitial();
        env = env.makeEnclosingFunction(function);
        env = env.makeRegisterManager(new RegisterManager(env.getFunction().getMaxRegister() + 1));    // start allocating registers one more than the previous max
        BasicBlock declarationBlock = makeBasicBlock(env.getFunction(), extenderBlock.getExceptionHandler(), functionAndBlocksManager);
        env = env.makeDeclarationBlock(declarationBlock);
        env = env.makeAppendBlock(declarationBlock);

        if (resultVariableName != null) {
            env = env.makeUnevalExpressionResult(new UnevalExpressionResult(resultVariableName, extenderNode.getResultRegister()));
        }

        // (a bit hacky way to indicate this, but it supports nested dynamic code, and simplifies the SourceLocation type)
        String fileName = String.format("TAJS-dynamic-code(%s)", extenderNode.getSourceLocation().toString());
        FlowGraphBuilder flowGraphBuilder = new FlowGraphBuilder(env, functionAndBlocksManager);
        Function entryFunction;
        BasicBlock entryBlock;
        if (asTimeOutEvent) {
            // transform the code as event handler code
            JavaScriptSource script = JavaScriptSource.makeEventHandlerCode("TIMEOUT", fileName, newSourceCode, 0, 0); // using dummy event name
            flowGraphBuilder.transformWebAppCode(script);

            // insert the new code right after the extendedNode (to model function reachability and preserve the number of successors of extendedNode)
            extenderBlock.getSuccessors().clear();
            extenderBlock.addSuccessor(env.getAppendBlock()); // the edge to the successor of extendedNode is set via close(..) below

            // extract the created callback
            entryFunction = flowGraphBuilder.getEventHandlers().get(flowGraphBuilder.getEventHandlers().size() - 1).getFirst(); // TODO: (#129) a bit hacky to depend on the list order...
            entryBlock = null; // this variant is a (pseudo) function, not just a collection of basic blocks
        } else {
            // insert a dummy node to prevent empty basic blocks
            final ConstantNode undef = ConstantNode.makeUndefined(env.getResultRegister(), extenderNode.getSourceLocation()); // use a constant node to get uniform treatment with other "functions"
            undef.setArtificial();
            env.getDeclarationBlock().addNode(undef);

            // transform the code as embedded code
            JavaScriptSource script = JavaScriptSource.makeEmbeddedCode(fileName, newSourceCode, 0, 0);
            flowGraphBuilder.transformCode(script, 0, 0);

            entryFunction = null; // this variant is not a (pseudo) function but a collection of basic blocks
            entryBlock = env.getAppendBlock();
            functionAndBlocksManager.registerUnreachableSyntacticSuccessor(extenderBlock, declarationBlock);
        }

        // FIXME: (#124) does not include surrounding break/continue targets or finally blocks - so exceptions and jumps are not handled soundly

        FlowGraph closed = flowGraphBuilder.close(existingFlowGraph, extenderSuccessor);
        closed.check();

        Pair<List<Function>, List<BasicBlock>> blocksAndFunctions = functionAndBlocksManager.close();
        return new FlowGraphFragment(sourceCodeIdentifier, entryBlock, entryFunction, blocksAndFunctions.getFirst(), blocksAndFunctions.getSecond());
    }
}
