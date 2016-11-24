/*
 * Copyright 2009-2016 Aarhus University
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

package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Operations for extending an existing flow graph.
 */
public class FlowGraphMutator {

    private final static String dynamicSourceCodePrefix = "TAJS-dynamic-code";

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
        Set<BasicBlock> oldBlocks = newSet(function.getBlocks());
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
        BasicBlock firstBodyBlock = makeBasicBlock(env.getFunction(), extenderBlock.getExceptionHandler(), functionAndBlocksManager);
        declarationBlock.addSuccessor(firstBodyBlock);
        env = env.makeAppendBlock(firstBodyBlock);

        if (resultVariableName != null) {
            env = env.makeUnevalExpressionResult(new UnevalExpressionResult(resultVariableName, extenderNode.getResultRegister()));
        }

        // (a bit hacky way to indicate this, but it supports nested dynamic code, and simplifies the SourceLocation type)
        String fileName = formatDynamicSourceCodeFileName(extenderNode.getSourceLocation().toString());
        FlowGraphBuilder flowGraphBuilder = new FlowGraphBuilder(env, functionAndBlocksManager, null, fileName);
        Function entryFunction;
        BasicBlock entryBlock;
        if (asTimeOutEvent) {
            // transform the code as event handler code
            JavaScriptSource script = JavaScriptSource.makeEventHandlerCode(EventType.TIMEOUT, null, fileName, newSourceCode, 0, 0); // using dummy event name
            entryFunction = flowGraphBuilder.transformWebAppCode(script);

            // insert the new code right after the extendedNode (to model function reachability and preserve the number of successors of extendedNode)
            extenderBlock.getSuccessors().clear();
            extenderBlock.addSuccessor(declarationBlock); // the edge to the successor of extendedNode is set via close(..) below

            entryBlock = null; // this variant is a (pseudo) function, not just a collection of basic blocks
        } else {
            // transform the code as embedded code
            JavaScriptSource script = JavaScriptSource.makeEmbeddedCode(null, fileName, newSourceCode, 0, 0);
            flowGraphBuilder.transformCode(script, 0, 0);

            if (declarationBlock.getNodes().isEmpty()) {
                // insert a dummy node to prevent empty basic blocks
                final ConstantNode undef = ConstantNode.makeUndefined(AbstractNode.NO_VALUE, extenderNode.getSourceLocation());
                undef.setArtificial();
                declarationBlock.addNode(undef);
            }

            entryFunction = null; // this variant is not a (pseudo) function but a collection of basic blocks
            entryBlock = declarationBlock;
            functionAndBlocksManager.registerUnreachableSyntacticSuccessor(extenderBlock, declarationBlock);

            Stack<BasicBlock> entryStack = new Stack<>();
            entryStack.push(extenderBlock.getEntryBlock());
            FlowGraphBuilder.setEntryBlocks(extenderBlock, entryBlock, entryStack, newSet(oldBlocks), functionAndBlocksManager);
        }

        // FIXME: (#124) does not include surrounding break/continue targets or finally blocks - so exceptions and jumps are not handled soundly

        FlowGraph closed = flowGraphBuilder.close(existingFlowGraph, extenderSuccessor);
        closed.check();

        Pair<List<Function>, List<BasicBlock>> blocksAndFunctions = functionAndBlocksManager.close();
        return new FlowGraphFragment(sourceCodeIdentifier, entryBlock, entryFunction, blocksAndFunctions.getFirst(), blocksAndFunctions.getSecond());
    }

    private static String formatDynamicSourceCodeFileName(String fileName) {
        return String.format("%s(%s)", dynamicSourceCodePrefix, fileName);
    }

    public static boolean isDynamicSourceCode(SourceLocation sourceLocation) {
        return sourceLocation.getPrettyFileName().startsWith(dynamicSourceCodePrefix);
    }

}
