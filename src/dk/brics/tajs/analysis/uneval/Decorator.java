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

package dk.brics.tajs.analysis.uneval;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.*;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Decorator { // TODO: copied from optimizer, needs review

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
                    predecessorBlocks.get(successor).add(predecessor);
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
     * Returns the set of variables declared in a given function.
     */
    public static Set<String> getDeclaredVariables(Function function) {
        Set<String> result = Collections.newSet();
        for (BasicBlock basicBlock : function.getBlocks()) {
            for (AbstractNode node : basicBlock.getNodes()) {
                if (node instanceof DeclareVariableNode) {
                    DeclareVariableNode declareVariableNode = (DeclareVariableNode) node;
                    result.add(declareVariableNode.getVariableName());
                }
            }
        }
        result.addAll(function.getParameterNames());
        return result;
    }

    /**
     * Returns the set of basic blocks occurring immediately before the given basic block.
     */
    public Set<BasicBlock> getPredecessorBlocks(BasicBlock basicBlock) {
        return predecessorBlocks.get(basicBlock);
    }

    /**
     * Returns the node occurring immediately before the given node.
     */
    public AbstractNode getPredecessorNode(AbstractNode node) {
        Set<AbstractNode> nodes = predecessorNodes.get(node);
        if (nodes.size() != 1) {
            throw new AnalysisException("Node does not have a single predecessor node");
        }
        return nodes.iterator().next();
    }

    /**
     * Returns the set of predecessor nodes, either the node occurring immediately before the given node,
     * or the last node occurring in each predecessor basic block
     */
    public Set<AbstractNode> getAllPredecessorNodes(AbstractNode node) {
        return predecessorNodes.get(node);
    }

    /**
     * Returns the node occuring immediate after the given node.
     */
    public AbstractNode getSuccessorNode(AbstractNode node) {
        Set<AbstractNode> nodes = successorNodes.get(node);
        if (nodes.size() != 1) {
            throw new AnalysisException("Node does not have a single successor node");
        }
        return nodes.iterator().next();
    }

    /**
     * Returns the set of successor nodes, either the node occurring immediately after the given node,
     * or the set of nodes occurring in the following basic blocks
     */
    public Set<AbstractNode> getAllSuccessorNodes(AbstractNode node) {
        return successorNodes.get(node);
    }

    /**
     * Returns the set of variables potentially (over)written by inner functions for the given function.
     */
    public Set<String> getInnerVariablesWritten(Function function) {
        return innerVariablesWritten.get(function);
    }

    /**
     * Replace targetVar by replacementVar in the BasicBlock and all successors.
     */
    public static void replaceVariable(BasicBlock basicBlock, int targetVar, int replacementVar) {
        Set<BasicBlock> visited = Collections.newSet();
        Queue<BasicBlock> queue = Collections.newQueue();
        queue.add(basicBlock);
        while (!queue.isEmpty()) {
            BasicBlock bs = queue.poll();
            // Updates Nodes
            for (AbstractNode node : bs.getNodes()) {
                replaceInNode(node, targetVar, replacementVar);
            }
            // Add successors to the queue?
            for (BasicBlock successor : bs.getSuccessors()) {
                if (!visited.contains(successor)) {
                    queue.add(successor);
                }
            }
            visited.add(bs);
        }
    }

    /**
     * Replace all occurrences of targetVar with replacementVar.
     */
    private static void replaceInNode(AbstractNode node, int targetVar, int replacementVar) {

        if (node instanceof AssumeNode) {
            // TODO: Implement new type of assume node
            AssumeNode assumeNode = (AssumeNode) node;
            if (assumeNode.getBaseRegister() == targetVar) {
                assumeNode.setBaseRegister(replacementVar);
            }
            if (assumeNode.getPropertyRegister() == targetVar) {
                assumeNode.setPropertyRegister(replacementVar);
            }
        } else if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode) node;
            if (binaryOperatorNode.getArg1Register() == targetVar) {
                binaryOperatorNode.setArg1Register(replacementVar);
            }
            if (binaryOperatorNode.getArg2Register() == targetVar) {
                binaryOperatorNode.setArg2Register(replacementVar);
            }
        } else if (node instanceof CallNode) {
            CallNode callNode = (CallNode) node;
            if (callNode.getBaseRegister() == targetVar) {
                callNode.setBaseRegister(replacementVar);
            }
            if (callNode.getFunctionRegister() == targetVar) {
                callNode.setFunctionRegister(replacementVar);
            }
            for (int i = 0; i < callNode.getNumberOfArgs(); i++) {
                if (callNode.getArgRegister(i) == targetVar) {
                    callNode.setArgRegister(i, replacementVar);
                }
            }
        } else if (node instanceof CatchNode) {
            // do nothing
        } else if (node instanceof ConstantNode) {
            // do nothing
        } else if (node instanceof DeclareFunctionNode) {
            // do nothing
        } else if (node instanceof DeclareVariableNode) {
            // do nothing
        } else if (node instanceof DeletePropertyNode) {
            DeletePropertyNode deletePropertyNode = (DeletePropertyNode) node;
            if (deletePropertyNode.getBaseRegister() == targetVar) {
                deletePropertyNode.setBaseRegister(replacementVar);
            }
            if (deletePropertyNode.getPropertyRegister() == targetVar) {
                deletePropertyNode.setPropertyRegister(replacementVar);
            }
        } else if (node instanceof BeginWithNode) {
            BeginWithNode enterWithNode = (BeginWithNode) node;
            if (enterWithNode.getObjectRegister() == targetVar) {
                enterWithNode.setObjectRegister(replacementVar);
            }
        } else if (node instanceof EventEntryNode) {
            // do nothing
        } else if (node instanceof EventDispatcherNode) {
            // do nothing
        } else if (node instanceof ExceptionalReturnNode) {
            // do nothing
        } else if (node instanceof BeginForInNode) {
            BeginForInNode beginForInNode = (BeginForInNode) node;
            if (beginForInNode.getObjectRegister() == targetVar) {
                beginForInNode.setObjectRegister(replacementVar);
            }
        } else if (node instanceof HasNextPropertyNode) {
            // do nothing
        } else if (node instanceof IfNode) {
            IfNode ifNode = (IfNode) node;
            if (ifNode.getConditionRegister() == targetVar) {
                ifNode.setConditionRegister(replacementVar);
            }
        } else if (node instanceof EndWithNode) {
            // do nothing
        } else if (node instanceof NewObjectNode) {
            // do nothing
        } else if (node instanceof NextPropertyNode) {
            NextPropertyNode nextPropertyNode = (NextPropertyNode) node;
            if (nextPropertyNode.getPropertyRegister() == targetVar) {
                nextPropertyNode.setPropertyRegister(replacementVar);
            }
        } else if (node instanceof NopNode) {
            // do nothing
        } else if (node instanceof ReadPropertyNode) {
            ReadPropertyNode readPropertyNode = (ReadPropertyNode) node;
            if (readPropertyNode.getBaseRegister() == targetVar) {
                readPropertyNode.setBaseRegister(replacementVar);
            }
            if (readPropertyNode.getPropertyRegister() == targetVar) {
                readPropertyNode.setPropertyRegister(replacementVar);
            }
        } else if (node instanceof ReadVariableNode) {
            // do nothing
        } else if (node instanceof ReturnNode) {
            ReturnNode returnNode = (ReturnNode) node;
            if (returnNode.getReturnValueRegister() == targetVar) {
                returnNode.setReturnValueRegister(replacementVar);
            }
        } else if (node instanceof ThrowNode) {
            ThrowNode throwNode = (ThrowNode) node;
            if (throwNode.getValueRegister() == targetVar) {
                throwNode.setValueRegister(replacementVar);
            }
        } else if (node instanceof TypeofNode) {
            TypeofNode typeofNode = (TypeofNode) node;
            if (typeofNode.getArgRegister() == targetVar) {
                typeofNode.setArgRegister(replacementVar);
            }
        } else if (node instanceof UnaryOperatorNode) {
            UnaryOperatorNode unaryOperatorNode = (UnaryOperatorNode) node;
            if (unaryOperatorNode.getArgRegister() == targetVar) {
                unaryOperatorNode.setArgRegister(replacementVar);
            }
        } else if (node instanceof WritePropertyNode) {
            WritePropertyNode writePropertyNode = (WritePropertyNode) node;
            if (writePropertyNode.getBaseRegister() == targetVar) {
                writePropertyNode.setBaseRegister(replacementVar);
            }
            if (writePropertyNode.getPropertyRegister() == targetVar) {
                writePropertyNode.setPropertyRegister(replacementVar);
            }
            if (writePropertyNode.getValueRegister() == targetVar) {
                writePropertyNode.setValueRegister(replacementVar);
            }
        } else if (node instanceof WriteVariableNode) {
            WriteVariableNode writeVariableNode = (WriteVariableNode) node;
            if (writeVariableNode.getValueRegister() == targetVar) {
                writeVariableNode.setRegister(replacementVar);
            }
        } else {
            throw new AnalysisException("Unknown Node Type: " + node.getClass());
        }
    }

}
