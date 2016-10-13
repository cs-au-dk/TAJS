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

package dk.brics.tajs.analysis.uneval;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Decorator { // TODO: needs review

    private Map<BasicBlock, Set<BasicBlock>> predecessorBlocks = Collections.newMap();

    private Map<AbstractNode, Set<AbstractNode>> predecessorNodes = Collections.newMap();

    private Map<AbstractNode, Set<AbstractNode>> successorNodes = Collections.newMap();

    private Map<Function, Set<Function>> innerFunctions = Collections.newMap();

    private Map<Function, Set<String>> innerVariablesWritten = Collections.newMap();

    public Decorator(FlowGraph flowGraph) {

        // Build a map of predecessor basic blocks
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                predecessorBlocks.put(basicBlock, Collections.<BasicBlock>newSet());
            }
            for (BasicBlock predecessor : function.getBlocks()) {
                for (BasicBlock successor : predecessor.getSuccessors()) {
                    /* OLD IMPLEMENTATION, by ???:
                    predecessorBlocks.get(successor).add(predecessor);
                     */
                    /* NEW IMPLEMENTATION, by @esbena:
                        @esbena: the unevalizer is treated as a black box due to its code quality,
                        This fix makes some sense, but I am not sure exactly why it is needed.
                         (the fix prevents a StackOverflowError)
                     */
                    // the flowgraph block order forms a topological ordering: loop back edges can be identified easily
                    boolean isLoopBackEdge = predecessor.getOrder() > successor.getOrder();
                    // the unevalizer mechanisms does not need to consider loop back edges
                    if (!isLoopBackEdge) {
                        predecessorBlocks.get(successor).add(predecessor);
                    }
                }
            }
        }

        // Build map of predecessor nodes
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                AbstractNode previous = null;
                for (AbstractNode node : basicBlock.getNodes()) {
                    Set<AbstractNode> result = Collections.newSet();
                    if (node == basicBlock.getFirstNode()) {
                        for (BasicBlock predecessorBlock : predecessorBlocks.get(basicBlock)) {
                            result.add(predecessorBlock.getLastNode());
                        }
                    } else {
                        result.add(previous);
                    }
                    predecessorNodes.put(node, result);
                    previous = node;
                }
            }
        }

        // Build map of successor nodes
        for (Function function : flowGraph.getFunctions()) {
            for (BasicBlock basicBlock : function.getBlocks()) {
                List<AbstractNode> nodes = basicBlock.getNodes();
                // Case 1: The node has an immediate successor
                // Note: The loops goes to size() - 1,
                // because the last nodes must be handled seperately.
                for (int i = 0; i < nodes.size() - 1; i++) {
                    Set<AbstractNode> result = Collections.newSet();
                    result.add(nodes.get(i + 1));
                    successorNodes.put(nodes.get(i), result);
                }
                // Case 2: The node has successors in the next basic blocks
                Set<AbstractNode> result = Collections.newSet();
                for (BasicBlock successorBlock : basicBlock.getSuccessors()) {
                    result.add(successorBlock.getFirstNode());
                }
                if (basicBlock.canThrowExceptions() && basicBlock.getExceptionHandler() != null) {
                    result.add(basicBlock.getExceptionHandler().getFirstNode());
                }
                successorNodes.put(basicBlock.getLastNode(), result);
            }
        }

        // Build map of inner functions.
        for (Function function : flowGraph.getFunctions()) {
            innerFunctions.put(function, Collections.<Function>newSet());
        }
        for (Function function : flowGraph.getFunctions()) {
            if (function.hasOuterFunction()) {
                innerFunctions.get(function.getOuterFunction()).add(function);
            }
        }

        // Build map of inner variables (potentially) written
        for (Function function : flowGraph.getFunctions()) {
            Set<String> variables = Collections.<String>newSet();
            if (function.hasOuterFunction()) {
                variables.addAll(getVariablesWritten(function));
            }
            innerVariablesWritten.put(function, variables);
        }
    }

    /**
     * Recursively visit each function.
     */
    private Set<String> getVariablesWritten(Function function) {
        Set<String> result = Collections.newSet();
        for (Function innerFunction : innerFunctions.get(function)) {
            for (BasicBlock block : innerFunction.getBlocks()) {
                for (AbstractNode node : block.getNodes()) {
                    if (node instanceof WriteVariableNode) {
                        result.add(((WriteVariableNode) node).getVariableName());
                    }
                }
            }
            result.addAll(getVariablesWritten(innerFunction));
        }
        return result;
    }

    /**
     * Returns the set of basic blocks occurring immediately before the given basic block.
     */
    public Set<BasicBlock> getPredecessorBlocks(BasicBlock basicBlock) {
        return predecessorBlocks.get(basicBlock);
    }
}
