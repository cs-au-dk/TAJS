/*
 * Copyright 2009-2017 Aarhus University
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
import dk.brics.tajs.flowgraph.SourceLocation.DynamicLocationMaker;
import dk.brics.tajs.flowgraph.SourceLocation.SourceLocationMaker;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder.TripleForSetEntryBlocksWorklist;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Operations for extending an existing flow graph.
 */
public class FlowGraphMutator {

    private static FlowGraphMutator instance;

    private final Map<Pair<URL, List<String>>, Function> fileCache;

    private final Map<Pair<String, Pair<String, List<String>>>, Function> sourceCache;

    private Set<URL> hostEnvironmentLocations;

    private Set<Function> functionsWithSyntheticParameters;

    private FlowGraphMutator() {
        fileCache = newMap();
        sourceCache = newMap();
        hostEnvironmentLocations = newSet();
        functionsWithSyntheticParameters = newSet();
    }

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
        BasicBlock declarationBlock = makeBasicBlock(extenderBlock.getExceptionHandler(), functionAndBlocksManager);
        env = env.makeDeclarationBlock(declarationBlock);
        BasicBlock firstBodyBlock = makeBasicBlock(extenderBlock.getExceptionHandler(), functionAndBlocksManager);
        declarationBlock.addSuccessor(firstBodyBlock);
        env = env.makeAppendBlock(firstBodyBlock);

        if (resultVariableName != null) {
            env = env.makeUnevalExpressionResult(new UnevalExpressionResult(resultVariableName, extenderNode.getResultRegister()));
        }

        FlowGraphBuilder flowGraphBuilder = new FlowGraphBuilder(env, functionAndBlocksManager);
        Function entryFunction;
        BasicBlock entryBlock;
        DynamicLocationMaker sourceLocationMaker = new DynamicLocationMaker(extenderNode.getSourceLocation());
        if (asTimeOutEvent) {
            // transform the code as event handler code
            JavaScriptSource script = JavaScriptSource.makeEventHandlerCode(EventType.TIMEOUT, newSourceCode, 0, 0); // using dummy event name
            entryFunction = flowGraphBuilder.transformWebAppCode(script, sourceLocationMaker);

            // insert the new code right after the extendedNode (to model function reachability and preserve the number of successors of extendedNode)
            extenderBlock.getSuccessors().clear();
            extenderBlock.addSuccessor(declarationBlock); // the edge to the successor of extendedNode is set via close(..) below

            entryBlock = null; // this variant is a (pseudo) function, not just a collection of basic blocks
        } else {
            // transform the code as embedded code
            flowGraphBuilder.transformCode(newSourceCode, 0, 0, sourceLocationMaker);

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
            FlowGraphBuilder.setEntryBlocks(new TripleForSetEntryBlocksWorklist(extenderBlock, entryBlock, entryStack), newSet(oldBlocks), functionAndBlocksManager);
        }

        // FIXME: (#124) does not include surrounding break/continue targets or finally blocks - so exceptions and jumps are not handled soundly

        FlowGraph closed = flowGraphBuilder.close(existingFlowGraph, extenderSuccessor);
        closed.check();

        Pair<List<Function>, List<BasicBlock>> blocksAndFunctions = functionAndBlocksManager.close();
        return new FlowGraphFragment(sourceCodeIdentifier, entryBlock, entryFunction, blocksAndFunctions.getFirst(), blocksAndFunctions.getSecond());
    }

    public static FlowGraphMutator get() {
        if (instance == null) {
            instance = new FlowGraphMutator();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    /**
     * Adds a top-level function to the current flowgraph.
     *
     * @param file              source code of the function body
     * @param parameterNames    pararmeter names of the function
     * @param isHostEnvironment true if the function is part of the host environment
     * @param existingFlowgraph flowgraph to extend
     * @return newly created function
     */
    public Function extendFlowGraphWithTopLevelFunction(URL file, List<String> parameterNames, boolean isHostEnvironment, FlowGraph existingFlowgraph, SourceLocationMaker sourceLocationMaker) {
        Pair<URL, List<String>> key = Pair.make(file, parameterNames);
        if (!fileCache.containsKey(key)) {
            try {
                String source = Loader.getString(file, Charset.forName("UTF-8"));
                Function function = extendFlowGraphWithTopLevelFunction(file.toExternalForm(), source, parameterNames, existingFlowgraph, sourceLocationMaker);
                fileCache.put(key, function);
                functionsWithSyntheticParameters.add(function);
                if (isHostEnvironment) {
                    hostEnvironmentLocations.add(file);
                }
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
        }
        return fileCache.get(key);
    }

    public Function extendFlowGraphWithTopLevelFunction(String uniqueIdentifier, String source, List<String> parameterNames, FlowGraph existingFlowgraph, SourceLocationMaker sourceLocationMaker) {
        Pair<String, Pair<String, List<String>>> key = Pair.make(uniqueIdentifier, Pair.make(source, parameterNames)); // TODO make use of something more robust than a string for identifiers? (GitHub #363)
        if (!sourceCache.containsKey(key)) {
            BasicBlock standaloneBlock = new BasicBlock(existingFlowgraph.getMain());
            AstEnv env = AstEnv.makeInitial().makeEnclosingFunction(existingFlowgraph.getMain()).makeAppendBlock(standaloneBlock);
            FlowGraphBuilder builder = new FlowGraphBuilder(env, new FunctionAndBlockManager());
            Function function = builder.transformFunctionBody(source, parameterNames, sourceLocationMaker);
            builder.close(existingFlowgraph, null);
            sourceCache.put(key, function);
        }
        return sourceCache.get(key);
    }

    public boolean isHostEnvironmentSource(SourceLocation location) {
        return hostEnvironmentLocations.contains(location.getLocation());
    }

    public boolean hasSyntheticParameters(Function function) {
        return functionsWithSyntheticParameters.contains(function);
    }
}
