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

package dk.brics.tajs.monitoring;

import static dk.brics.tajs.util.Collections.addAllToMapSet;
import static dk.brics.tajs.util.Collections.addToMapList;
import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.AbstractNodeVisitor;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.AssumeNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BinaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.CallConversionNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareFunctionNode;
import dk.brics.tajs.flowgraph.jsnodes.DeclareVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.DeletePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventEntryNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.Node;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.BlockState;
import dk.brics.tajs.lattice.CallEdge;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.TypeCollector.VariableSummary;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.IBlockState;
import dk.brics.tajs.solver.ICallContext;
import dk.brics.tajs.solver.IMonitoring;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.solver.Message.Severity;
import dk.brics.tajs.solver.Message.Status;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Strings;

/**
 * Records various information during the scan phase of an analysis.
 */
public class Monitoring<
BlockStateType extends BlockState<BlockStateType,CallContextType,CallEdgeType>,
CallContextType extends ICallContext<CallContextType>,
CallEdgeType extends CallEdge<BlockStateType>> implements
                            IMonitoring<BlockStateType,CallContextType> {
	
	private static Logger logger = Logger.getLogger(Monitoring.class); 

	/**
	 * Set if in scan phase (at the fixpoint).
	 */
	private boolean scan_phase = false;
	
	/**
	 * Number of node transfers.
	 */
	private int node_transfers = 0;

	/**
	 * Number of block transfers.
	 */
	private int block_transfers = 0;

	/**
	 * Number of calls to the UnknownValueResolver where a value is recovered partially in non-scanning mode.
	 */
	private int unknown_value_resolve_analyzing_partial = 0;

	/**
	 * Number of calls to the UnknownValueResolver where a value is recovered fully in non-scanning mode.
	 */
	private int unknown_value_resolve_analyzing_full = 0;

	/**
	 * Number of calls to the UnknownValueResolver where a value is recovered partially in scanning mode.
	 */
	private int unknown_value_resolve_scanning_partial = 0;

	/**
	 * Number of calls to the UnknownValueResolver where a value is recovered fully in scanning mode.
	 */
	private int unknown_value_resolve_scanning_full = 0;

	/**
	 * Number of abstract state join operations.
	 */
	private int joins = 0;
	
	/**
	 * Number of functions.
	 */
	private int functions = 0;

	/**
	 * Unreachable functions.
	 */
	private Set<Function> unreachable_functions;

	/**
	 * Number of (non-this) variable read operations. 
	 */
	private int read_variable_nodes = 0;

	/**
	 * Number of property access operations.
	 */
	private int property_access_nodes = 0;

	/**
	 * Number of fixed-property read operations.
	 */
	private int read_fixed_property_nodes = 0;

	/**
	 * Number of call/construct nodes.
	 */
	private int call_nodes = 0;

	/**
	 * Variable read operations that may read an absent variable.
	 */
	private Set<ReadVariableNode> absent_variable_read;

	/**
	 * Fixed-property read operations that may access an absent property. 
	 */
	private Set<ReadPropertyNode> absent_fixed_property_read;

	/**
	 *  Property access operations that may dereference null or undefined.
	 */
	private Set<Node> null_undef_base;

	/**
	 * Call/construct nodes that may involve non-function values.
	 */
	private Set<AbstractNode> call_to_non_function;

	/**
	 * Store information about values read during analysis. 
	 */
	private Map<NodeAndContext<ICallContext<?>>, Value> value_reads;

	/**
	 * Values passed to eval.
	 */
	private Map<AbstractNode,Value> eval_calls; 

	/**
	 * Values written to innerHTML.
	 */
	private Map<AbstractNode,Value> inner_html_writes;

	/**
	 * Descriptions of new dataflow at function entry blocks.
	 */
	private Map<BasicBlock,Map<ICallContext<?>,List<String>>> newflows;

	/**
	 * Maximum memory usage measured.
	 */
	private long max_memory = 0;

	/**
	 * Map from line URL to set of guaranteed undefined lines in the source text.
	 */
	private Map<String,Set<Integer>> unreachable_lines; 

	/**
	 * Information about read/written properties in all abstract objects. 
	 * Singleton/summary information is stored for the singleton variant.
	 */
	private Map<ObjectLabel, ObjReadsWrites> obj_reads_writes; 

	/**
	 * Variables and parameters that are declared in each function and maybe read.
	 * Undeclared variables belong to the main function.
	 */
    private Map<Function,Set<String>> read_variables;
    
    /**
     * All reachable nodes.
     */
    private Set<AbstractNode> reachable_nodes;

    /**
     * Functions that may be called as constructors.
     */
    private Set<Function> called_as_constructor;
    
    private TypeCollector type_collector;
    
//    /**
//     * Counter for {@link #visitNewFlow(BasicBlock, ICallContext, IBlockState, String, String)}.
//     */
//    private int next_newflow_file;
    
    /**
     * Number of property recovery graphs of different sizes.
     */
    private Map<Integer,Integer> recovery_graph_sizes;
    
    /**
     * Collected messages.
     * Maps each message object to itself to make it possible to find existing messages that are "equal" to new ones.
     */
    private Map<Message, Message> messages;

	/**
	 * Constructs a new monitoring object.
	 */
	public Monitoring() {
		call_to_non_function = newSet();
		absent_variable_read = newSet();
		null_undef_base = newSet();
		absent_fixed_property_read = newSet();
		newflows = newMap();
		eval_calls = newMap();
		inner_html_writes = newMap();
		unreachable_lines = newMap();
		value_reads = newMap();
        obj_reads_writes = newMap();
        read_variables = newMap();
		unreachable_functions = newSet();
        reachable_nodes = newSet();
        called_as_constructor = newSet();
        type_collector = new TypeCollector();
        recovery_graph_sizes = newMap();
//        next_newflow_file = 1;
        messages = null;
	}

	@Override
	public void beginScanPhase(FlowGraph fg) {
		messages = newMap();
		scan_phase = true;
		for (Function f : fg.getFunctions())
			for (BasicBlock b : f.getBlocks())
				for (AbstractNode n : b.getNodes()) 
					n.visitBy(new AbstractNodeVisitor<Void>() {

						@SuppressWarnings("hiding")
						@Override
						public void visit(dk.brics.tajs.flowgraph.jsnodes.Node n, Void a) {
							n.visitBy(new dk.brics.tajs.flowgraph.jsnodes.NodeVisitor<Void>() {

								@Override
								public void visit(NopNode n, Void a) {}

								@Override
								public void visit(DeclareVariableNode n, Void a) {}

								@Override
								public void visit(ConstantNode n, Void a) {}

								@Override
								public void visit(NewObjectNode n, Void a) {}

								@Override
								public void visit(UnaryOperatorNode n, Void a) {}

								@Override
								public void visit(BinaryOperatorNode n, Void a) {}

								@Override
								public void visit(ReadVariableNode n, Void a) {
									if (!n.getVariableName().equals("this")) {
										read_variable_nodes++;
									}
								}

								@Override
								public void visit(WriteVariableNode n, Void a) {}

								@Override
								public void visit(ReadPropertyNode n, Void a) {
									property_access_nodes++;
									if (n.isPropertyFixed()) {
										read_fixed_property_nodes++;
									}
								}

								@Override
								public void visit(WritePropertyNode n, Void a) {
									property_access_nodes++;
								}

								@Override
								public void visit(DeletePropertyNode n, Void a) {
									if (!n.isVariable()) {
										property_access_nodes++;
									}
								}

								@Override
								public void visit(TypeofNode n, Void a) {}

								@Override
								public void visit(IfNode n, Void a) {}

								@Override
								public void visit(DeclareFunctionNode n, Void a) {}

								@Override
								public void visit(CallNode n, Void a) {
									call_nodes++;
								}

								@Override
								public void visit(CallConversionNode n, Void a) {
									call_nodes++; // TODO: should this count as a call node? 
								}

								@Override
								public void visit(ReturnNode n, Void a) {}

								@Override
								public void visit(ExceptionalReturnNode n, Void a) {}

								@Override
								public void visit(ThrowNode n, Void a) {}

								@Override
								public void visit(CatchNode n, Void a) {}

								@Override
								public void visit(BeginWithNode n, Void a) {}

								@Override
								public void visit(EndWithNode n, Void a) {}

								@Override
								public void visit(BeginForInNode n, Void a) {}

								@Override
								public void visit(NextPropertyNode n, Void a) {}

								@Override 
								public void visit(HasNextPropertyNode n, Void a) {}

								@Override
								public void visit(AssumeNode n, Void a) {}

								@Override
								public void visit(EventEntryNode n, Void a) {}

								@Override
								public void visit(EventDispatcherNode n, Void a) {}

								@Override
								public void visit(EndForInNode n, Void a) {}

							}, null);
						}
					}, null);
	}
	
	/**
	 * Finalizes messages by adding messages about unreachable code/functions, 
	 * unused variables and parameters, dead assignments, and shadowing declarations.
	 * @return The sorted list of messages produced during scanning.
	 */
    @Override
    public List<Message> endScanPhase(FlowGraph fg) {
    	reportUnreachableCode(fg);
    	reportUnusedVariableOrParameter(fg);
    	reportDeadAssignments();
        reportShadowing(fg);
        return getSortedMessages();
    }

	private void reportUnreachableCode(FlowGraph fg) {
		for (Function f : fg.getFunctions()) {
			if (unreachable_functions.contains(f)) // do not report blocks inside functions that are unreachable themselves.
				continue;
			Set<BasicBlock> successors = newSet();
			Set<BasicBlock> successors_of_blocks_where_last_node_is_reachable = newSet();
			for (BasicBlock b : f.getBlocks()) {
				successors.addAll(b.getSuccessors());
				if (b.getExceptionHandler() != null)
					successors.add(b.getExceptionHandler());
				if (reachable_nodes.contains(b.getLastNode()) &&
						!(b.getLastNode() instanceof IfNode)) // exclude if nodes, those are reported separately if always true/false
					successors_of_blocks_where_last_node_is_reachable.addAll(b.getSuccessors());
			}
			for (BasicBlock b : f.getBlocks()) {
				boolean prev_reachable = false;
				for (AbstractNode n : b.getNodes()) {
					if (n.getDuplicateOf() == null && !n.isArtificial() && !(n instanceof ReturnNode)) { // ignore duplicate nodes, artificial nodes, and return nodes
						if (!reachable_nodes.contains(n)) {
							if (n == b.getFirstNode()
									? (n instanceof CatchNode || // unreachable catch block
											successors_of_blocks_where_last_node_is_reachable.contains(b) || // unreachable first node but predecessor is reachable
											(!b.isEntry() && !successors.contains(b))) // block is neither function entry nor successor of another block
									: prev_reachable) { // previous node in the block killed the flow
								addMessage(n, Status.CERTAIN, Severity.LOW, "Unreachable code"); // see also "Unreachable function" and "The conditional expression is always true/false"
							}
							break; // no more messages for this block
						}
						prev_reachable = reachable_nodes.contains(n);
					}
				}
			}
		}
	}

    private void reportDeadAssignments() {
    	for (ObjReadsWrites rw : obj_reads_writes.values() ) {
    		for (String s : rw.getProperties()) {
    			// flag if definitely written and definitely not read (excluding 'length' and any-string properties)
    			if (rw.getWriteStatus(s) == ObjReadsWrites.W_Status.WRITTEN &&
    					rw.getReadStatus(s) == ObjReadsWrites.R_Status.NOT_READ) { 
    				for (AbstractNode n : rw.getDefiniteWriteLocations(s)) {
    					String m_s = "Dead assignment, property " + s + " is never read";
    					Message m = new Message(n, Status.CERTAIN, m_s, Severity.MEDIUM, true);
    					messages.put(m, m);
    				}
    			}
    		}
    		/* TODO: this needs more testing:
    		if (rw.isDefaultWritten() && !rw.isSomePropertyRead()) {
    			for (AbstractNode n : rw.getDefaultWriteLocations()) {
    				String m_s = "Dead assignment, property is never read";
    				Message m = new Message(n, Status.CERTAIN, m_s, Severity.MEDIUM, true);
    				messages.put(m, m);
				}
    		}
    		*/
    	}
	}
    
	private void reportUnusedVariableOrParameter(FlowGraph fg) {
		for (Function f : fg.getFunctions()) {
			// Skip reporting unused variables and parameters for dead code.
			if (unreachable_functions.contains(f))
				continue;
			Set<String> names = newSet(f.getVariableNames());
			names.addAll(f.getParameterNames());
			Set<String> rv = read_variables.get(f);
			if (rv != null)
				names.removeAll(rv);
			for (String n : names) {
				final AbstractNode declNode = getVarDeclNode(f, n);
				addMessage(declNode, Status.CERTAIN, Severity.LOW, "The variable " + Strings.escape(n) + " is never used");
			}
		}
	}

	private static AbstractNode getVarDeclNode(Function f, String n) {
		Collection<BasicBlock> blocks = f.getBlocks();
		if (f.getParameterNames().contains(n))
			return f.getEntry().getFirstNode();
		for (BasicBlock block : blocks) {
			List<AbstractNode> nodes = block.getNodes();
			for (AbstractNode node : nodes) {
				if (node instanceof DeclareVariableNode && ((DeclareVariableNode) node).getVariableName().equals(n)) {
					return node;
				}
			}
		}
		throw new IllegalArgumentException("Variable " + n + " is not declared in this function!?!");
	}	
    
    /**
     * Add messages about shadowing. Shadowing occurs when a declared variable clashes either local functions or parameters. 
     * This is a purely syntactic property, no analysis results are used. 
     */
    public void reportShadowing(FlowGraph fg) { // FIXME: variable declarations cannot shadow functions or parameters (see 10.1.3 and micro/test181.js) - but the programmer may think so
    	for (Function f : fg.getFunctions()) {
    		Map<String, DeclareFunctionNode> declared_functions = newMap();
    		Map<String, DeclareVariableNode> declared_variables = newMap();
    		for (AbstractNode n : f.getEntry().getNodes()) {
    			if (n instanceof DeclareFunctionNode) {
    				DeclareFunctionNode node = (DeclareFunctionNode) n;
    				if (node.getFunction().getName() != null) {
    					declared_functions.put(node.getFunction().getName(), node);
    				}
    			}
    			if (n instanceof DeclareVariableNode) {
    				DeclareVariableNode node = (DeclareVariableNode) n;
    				declared_variables.put(node.getVariableName(), node);
    			}
    		}
    		for (DeclareFunctionNode n : declared_functions.values()) {
    			String function_name = n.getFunction().getName();
    			if (declared_variables.containsKey(function_name)) {
    				DeclareVariableNode node = declared_variables.get(function_name);
    				addMessage(node, Message.Status.CERTAIN, Severity.LOW, "The variable declaration " + Strings.escape(node.getVariableName()) + " shadows a function");
    			}
    		}
    		for (DeclareVariableNode n : declared_variables.values()) {
    			String var_name = n.getVariableName();
    			if (f.getParameterNames().contains(var_name)) {
    				DeclareVariableNode node = declared_variables.get(var_name);
    				addMessage(node, Message.Status.CERTAIN, Severity.LOW, "The variable declaration " + Strings.escape(node.getVariableName()) + " shadows a parameter");
    			}
    		}
    	}
    }

	@Override
	public int getTotalNumberOfNodeTransfers() {
		return node_transfers;
	}

	@Override
	public void visitNodeTransfer(AbstractNode n) {
		node_transfers++;
	}
	
	/**
	 * Registers a block transfer occurrence.
	 * Also measures memory usage if enabled.
	 */
	@Override
	public void visitBlockTransfer() {
		block_transfers++;
		if (Options.isMemoryMeasurementEnabled()) {
			System.gc();
			long m = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
			if (m > max_memory) {
				max_memory = m;
			}
		}	
	}

	@Override
	public void visitUnknownValueResolve(boolean partial, boolean scanning) {
		if (scanning) {
			if (partial)
				unknown_value_resolve_scanning_partial++;
			else
				unknown_value_resolve_scanning_full++;
		} else {
			if (partial)
				unknown_value_resolve_analyzing_partial++;
			else
				unknown_value_resolve_analyzing_full++;
		}
	}

	@Override
	public void visitJoin() {
		joins++;
	}

	@Override
	public void visitNewFlow(BasicBlock b, ICallContext<?> c, IBlockState<?, ?, ?> s, String diff, String info) {
		if (Options.isNewFlowEnabled() && b.isEntry()) {
			if (diff != null) {
				Map<ICallContext<?>,List<String>> m = newflows.get(b);
				if (m == null) {
					m = newMap();
					newflows.put(b, m);
				}
				addToMapList(m, c, diff);
			}
//			if (info != null) {
//				try {
//					File outdir = new File("out" + File.separator + "newflows"); // TODO: separate files for separate contexts?
//					if (!outdir.exists()) {
//						outdir.mkdirs();
//					}
//					try (FileWriter fw = new FileWriter("out" + File.separator + "newflows" + File.separator + "line" + b.getFunction().getSourceLocation().getLineNumber() + "-" + 
//							(next_newflow_file++) + "-" + info + ".dot")) {
//						fw.write(s.toDot());
//					}
//				} catch (IOException e) {
//					throw new AnalysisException(e);
//				}
//			}

		}
//					try (FileWriter fw = new FileWriter("out" + File.separator + "newflows" + File.separator + "line" + b.getFunction().getSourceLocation().getLineNumber() + "-" + 
//							(next_newflow_file++) + "-" + info + ".dot")) {
//						fw.write(s.toDot());
//					}
//				} catch (IOException e) {
//					throw new AnalysisException(e);
//				}
//			}
//		}
	}

	/**
	 * Registers the function and checks whether it is unreachable.
	 */
	@Override
	public void visitFunction(Function f, Collection<BlockStateType> entry_states) {
		functions++;
		boolean reachable = false;
		for (BlockStateType s : entry_states) {
			if (!s.isNone()) {
				reachable = true;
				break;
			}
		}
		if (!reachable) {
			unreachable_functions.add(f); // see also "Unreachable code" and "The conditional expression is always true/false"
			addMessage(f.getEntry().getFirstNode(), Status.CERTAIN, Severity.LOW, "Unreachable function" + (f.getName() != null ? " " + Strings.escape(f.getName()) : ""));
		}
	}

    @Override
	public void visitReachableNode(AbstractNode n) {
    	AbstractNode d = n.getDuplicateOf();
    	if (d != null)
    		n = d;
    	reachable_nodes.add(n);
    }
    	
	/**
	 * Returns the set of maybe reachable nodes.
	 */
    public Set<AbstractNode> getReachableNodes() {
    	return java.util.Collections.unmodifiableSet(reachable_nodes);
    }

	/**
	 * Checks whether an absent variable is read.
	 * @param n (non-this) read variable operation
	 * @param v the value being read
	 */
	public void visitReadNonThisVariable(ReadVariableNode n, Value v) {
        if (!scan_phase) {
			return;
        }
        Status s;
        if (v.isMaybeAbsent() && !v.isMaybePresent()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeAbsent()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        if (s != Status.NONE) {
        	absent_variable_read.add(n);
        }
		addMessage(n, s, Severity.HIGH, "ReferenceError, reading absent variable " + Strings.escape(n.getVariableName()));
	}
	
	/**
	 * Checks whether the read of 'this' yields the global object.
     * @param n (this) read variable operation
     * @param v the value being read
     * @param state
     */
	public void visitReadThis(ReadVariableNode n, Value v, BlockStateType state, ObjectLabel global_obj) {
        if (!scan_phase) {
			return;
        }
        v = UnknownValueResolver.getRealValue(v, state);
        Status s;
        if (v.getObjectLabels().contains(global_obj)) {
            if (v.getObjectLabels().size() == 1 && !v.isMaybePrimitive()) {
                s = Status.CERTAIN;
            } else {
                s = Status.MAYBE;
            }
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.MEDIUM, "Reading 'this' yields the global object");
        if (v.getObjectLabels().size() > 1) {
        	addMessage(n, Status.INFO, Severity.LOW,
    				"'this' refers to multiple abstract objects, which may cause loss of precision"); // ...but still sound!
			if (logger.isDebugEnabled()) 
				logger.debug("this = " + v);
        }
        else if (v.getObjectLabels().size() == 1 && !v.getObjectLabels().iterator().next().isSingleton() && !Options.isRecencyDisabled()) {
        	addMessage(n, Status.INFO, Severity.LOW,
    				"'this' refers to a summary abstract object, which may cause loss of precision"); // ...but still sound!
			if (logger.isDebugEnabled()) 
				logger.debug("this = " + v);
        }
	}
	
	/**
	 * Checks whether the variable read yields null/undefined.
	 * Variables named 'undefined' are ignored.
	 * @param n read variable operation
	 * @param v the value being read
	 */
	public void visitReadVariable(ReadVariableNode n, Value v, BlockStateType state) {
        if (!scan_phase) {
			return;
        }
        String varname = n.getVariableName();
        if (varname.equals("undefined")) {
        	return;
        }
        v = UnknownValueResolver.getRealValue(v, state);
        Status s;
        if (v.isNullOrUndef()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeNull() || v.isMaybeUndef()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        addMessage(n, s, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Variable " + Strings.escape(varname) + " is null/undefined");
	}

	/**
	 * Checks whether the assignment has no effect.
	 * @param n write variable operation
	 * @param v value being written
	 * @param state current abstract state
	 */
	public void visitWriteVariable(WriteVariableNode n, Value v, BlockStateType state) {
        if (!scan_phase) {
			return;
        }
		Value current_value = state.readVariable(n.getVariableName(), null);
		boolean same_value = true;
		if (current_value.isMaybeAbsent()) {
			same_value = false;
		} else {
			current_value = UnknownValueResolver.getRealValue(current_value, state);
			if (current_value.isMaybeFuzzyNum() || current_value.isMaybeFuzzyStr() || current_value.getObjectLabels().size() > 1) {
				same_value = false;
			} else {
				int values = 0;
				if (current_value.isMaybeNull()) {
					values++;
				}
				if (current_value.isMaybeUndef()) {
					values++;
				}
				if (values <= 1 && current_value.isMaybeSingleStr()) {
					values++;
				}
				if (values <= 1 && current_value.isMaybeSingleNum()) {
					values++;
				}
				if (values <= 1 && current_value.isMaybeTrue()) {
					values++;
				}
				if (values <= 1 && current_value.isMaybeFalse()) {
					values++;
				}
				if (values <= 1 && current_value.getObjectLabels().size() == 1) {
					values++;
				}
				if (values > 1) {
					same_value = false;
				} else {
					v = UnknownValueResolver.getRealValue(v, state);
					same_value = current_value == v;
				}
			}
		}
		addMessage(n, 
				same_value ? Status.CERTAIN : Status.MAYBE, 
				Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, 
				"The assignment to " + Strings.escape(n.getVariableName()) + " has no effect");
	}
	
	/**
	 * Checks whether the branch condition is always true or always false.
     * @param n if node
     * @param v the boolean value
     */
    public void visitIf(IfNode n, Value v) { // see also "Unreachable code" and "Unreachable function"
        if (!scan_phase || n.isArtificial()) {
        	return;
        }
        boolean is_maybe_true = v.isMaybeTrue();
        boolean is_maybe_false = v.isMaybeFalse();
        addMessage(n, is_maybe_true ? (is_maybe_false ? Status.MAYBE : Status.CERTAIN) : Status.NONE, 
        		Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "The conditional expression is always true");
        addMessage(n, is_maybe_false ? (is_maybe_true ? Status.MAYBE : Status.CERTAIN) : Status.NONE, 
        		Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "The conditional expression is always false");
    }
    
    /**
     * Checks whether the 'instanceof' operation may fail.
     * @param n node performing the operation
     * @param maybe_v2_non_function set if the second parameter may be a non-function value
     * @param maybe_v2_function set if the second parameter may be a function value
     * @param maybe_v2_prototype_primitive set if the prototype property of the second parameter may be a primitive value
     * @param maybe_v2_prototype_nonprimitive set if the prototype property of the second parameter may be an object value
     */
    public void visitInstanceof(AbstractNode n, 
    		boolean maybe_v2_non_function, boolean maybe_v2_function, 
    		boolean maybe_v2_prototype_primitive, boolean maybe_v2_prototype_nonprimitive ) {
        if (!scan_phase) {
			return;
        }
    	Status s1;
    	if (maybe_v2_non_function) {
    		if (!maybe_v2_function) {
    			s1 = Status.CERTAIN;
    		} else {
    			s1 = Status.MAYBE;
    		}
    	} else { 
    		s1 = Status.NONE;
    	}
    	addMessage(n, s1, Severity.HIGH, "TypeError, non-function object at 'instanceof'");
    	Status s2;
    	if (maybe_v2_prototype_primitive) {
    		if (!maybe_v2_prototype_nonprimitive) {
    			s2 = Status.CERTAIN;
    		} else {
    			s2 = Status.MAYBE;
    		}
    	} else { 
    		s2 = Status.NONE;
    	}
    	addMessage(n, s2, Severity.HIGH, "TypeError, non-object prototype at 'instanceof'");
    }
    
    /**
     * Checks whether the 'in' operation may fail.
     * @param n node performing the operation
     * @param maybe_v2_object if the second parameter may be an object value
     * @param maybe_v2_nonobject if the second parameter may be a non-object value
     */
    public void visitIn(AbstractNode n, boolean maybe_v2_object, boolean maybe_v2_nonobject) {
        if (!scan_phase) {
			return;
        }
    	Status s;
    	if (maybe_v2_nonobject) {
    		if (!maybe_v2_object) {
    			s = Status.CERTAIN;
    		} else {
    			s = Status.MAYBE;
    		}
    	} else { 
    		s = Status.NONE;
    	}
    	addMessage(n, s, Severity.HIGH, "TypeError, non-object at 'in'");
    }
    
    /**
     * Checks whether the property access operation may dereference null or undefined.
     * @param n operation that accesses a property
     * @param baseval base value for the access
     */
    public void visitPropertyAccess(Node n, Value baseval) {
        if (!scan_phase) {
        	return;
        }
        Status s;
        if (baseval.isNullOrUndef()) {
            s = Status.CERTAIN;
        } else if (baseval.isMaybeNull() || baseval.isMaybeUndef()) {
            s = Status.MAYBE;
        } else {
            s = Status.NONE;
        }
        if (s != Status.NONE) {
        	null_undef_base.add(n);
        }
		addMessage(n, s, Severity.HIGH, "TypeError, accessing property of null/undefined");
    }

    /**
	 * Checks whether the property read operation accesses an absent property and whether the operation returns null/undefined.
	 * @param n read property operation
	 * @param objlabels objects being read from
	 * @param propertystr description of the property name
	 * @param maybe if there may be more than one value
	 * @param state current abstract state
	 */
	public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, Str propertystr, boolean maybe, BlockStateType state) {
        if (!scan_phase) {
        	return;
        }
        Value v = state.readPropertyWithAttributes(objlabels, propertystr);
        Status s;
        if (!maybe && v.isMaybeAbsent() && !v.isMaybePresent()) {
            s = Status.CERTAIN;
        } else if (v.isMaybeAbsent()) {
            s = Status.MAYBE;
        } else { 
            s = Status.NONE;
        }
        if (s != Status.NONE && n.isPropertyFixed()) {
        	absent_fixed_property_read.add(n);
        }
        if (n.isPropertyFixed()) {
        	addMessage(n, s, Severity.MEDIUM, "Reading absent property " + n.getPropertyString());
        } else {
        	addMessage(n, s, Severity.LOW, "Reading absent property (computed name)");
        }
        Status s2;
        v = UnknownValueResolver.getRealValue(v, state);
        if (v.isNullOrUndef() && s != Status.CERTAIN) {
        	s2 = Status.CERTAIN;
        } else if (v.isMaybeNull() || v.isMaybeUndef()) {
        	s2 = Status.MAYBE;
        } else {
        	s2 = Status.NONE;
        }
        addMessage(n, s2, Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE, "Property is null/undefined");
    }
	
	/**
	 * Warns about reads from unknown properties;
	 * also registers a read operation on abstract objects.
	 * Properties named 'length' on array objects are ignored.
	 * @param n the node responsible for the read
	 * @param objs the objects being read from
	 * @param propertystr description of the property name
	 * @param check_unknown if set, warn about reads from unknown properties
	 */
	public void visitPropertyRead(Node n, Set<ObjectLabel> objs, Str propertystr, BlockStateType state, boolean check_unknown) {
        if (!scan_phase) {
        	return;
        }
        // warn about potential loss of precision
        if (check_unknown && checkPropertyNameMayInterfereWithBuiltInProperties(propertystr)) {
        	addMessage(n, Status.INFO, Severity.LOW,
    				"Reading from unknown property that may cause loss of precision"); // ...but still sound!
        }
        // register read operation on abstract object
        String propertyname = propertystr.getStr();
        if (propertyname != null && propertyname.equals("length")) {
        	// Proceed if we find an object label that is *not* an array.
        	boolean only_array_length = true;
        	for (ObjectLabel ol : objs)
        		if (!ol.getKind().equals(ObjectLabel.Kind.ARRAY)) {
        			only_array_length = false;
        			break;
        		}
        	if (only_array_length)
        		return;
        }
        Set<ObjectLabel> os = newSet();
        // Only give warnings for objects created by the user (getNode != null) since reading and writing properties
        // related to the DOM might be desirable for side effects.
		for (ObjectLabel objlabel : objs) {
            // TODO: objlabel.isHostObject() exists, but does not do precisely the same thing. Figure out what the correct behavior is.
			if (objlabel.getNode() != null) {
				for (ObjectLabel oo : (propertyname == null ? state.getPrototypesUsedForUnknown(objlabel) : state.getPrototypeWithProperty(objlabel, propertyname))) { // TODO: this is also used for ReadVariableNode?
					if (oo.getNode() != null) { // TODO: Only give warnings for user objects, others maybe DOM or similar with side effects
						os.add(oo.makeSingleton());
					}
				}
			}
		}
		for (ObjectLabel objlabel : os) {
            // Record reading of arguments[propertystr] as reading of the actual function arguments as well.
            if (objlabel.getKind() == ObjectLabel.Kind.ARGUMENTS) {
                Function f = n.getBlock().getFunction();
                List<String> args = f.getParameterNames();
                // Fall through if there are no arguments to the function or reading something
                // other than an array index.
                if (args == null || propertyname != null && !Strings.isArrayIndex(propertyname))
                    continue;
                String arg = null;
                if (propertyname != null && Integer.valueOf(propertyname) < args.size())
                    arg = args.get(Integer.valueOf(propertyname));
                // Add all arguments if we aren't sure which we read.
                if (arg == null)
                	addAllToMapSet(read_variables, f, args);
                else
                	addToMapSet(read_variables, f, arg);
            }
			ObjReadsWrites i = obj_reads_writes.get(objlabel);
			if (i == null) {
				i = new ObjReadsWrites();
				obj_reads_writes.put(objlabel, i);
			}
			if (propertyname == null) {
				i.readAny();
			} else if (os.size() == 1) {
				i.readDefinite(propertyname);
			} else {
				i.readMaybe(propertyname);
			}
		}
	}
	
	private static boolean checkPropertyNameMayInterfereWithBuiltInProperties(Str propertystr) {
		return !propertystr.isMaybeSingleStr() && 
				(propertystr.isMaybeStrIdentifier() || propertystr.isMaybeStrIdentifierParts() || 
						propertystr.isMaybeStrPrefixedIdentifierParts() || propertystr.isMaybeStrJSON()); // TODO: more precise pattern of what may interfere?
	}

	/**
	 * Warns about writes to unknown properties; 
	 * also registers a write operation on abstract objects.
	 * Properties named 'length' on array objects are ignored.
     * Writes to the arguments object are also ignored.
	 * @param n the node responsible for the write
	 * @param objs the objects being written to
	 * @param propertystr description of the property name
	 */
	public void visitPropertyWrite(Node n, Set<ObjectLabel> objs, Str propertystr) {
        if (!scan_phase) {
        	return;
        }
        // warn about potential loss of precision
        if (checkPropertyNameMayInterfereWithBuiltInProperties(propertystr)) {
        	addMessage(n, 
        			Status.INFO,
    				Severity.MEDIUM,
    				"Writing to unknown property that may cause loss of precision" ); // ...but still sound!
        }
        // register write operation on abstract object
        String propertyname = propertystr.getStr();
		if (propertyname != null && propertyname.equals("length")) {
			// Proceed if we find an object label that is *not* an array.
        	boolean only_array_length = true;
        	for (ObjectLabel ol : objs)
        		if (!ol.getKind().equals(ObjectLabel.Kind.ARRAY)) {
        			only_array_length = false;
        			break;
        		}
        	if (only_array_length)
        		return;
		}
		Set<ObjectLabel> os = newSet();
		for (ObjectLabel o : objs) {
            // Local variables end up in activation record object labels.
            // We do not consider that a property write, so filter them out.
			if (o.getNode() != null && o.getKind() != ObjectLabel.Kind.ACTIVATION) {
				os.add(o.makeSingleton());
			}
		}
		for (ObjectLabel objlabel : os) {
			ObjReadsWrites i = obj_reads_writes.get(objlabel);
			if (i == null) {
				i = new ObjReadsWrites();
				obj_reads_writes.put(objlabel, i);
			}
			if (propertyname == null) {
				i.writeAny(n);
			} else if (objs.size() == 1) {
				i.writeDefinite(propertyname, n);
			} else {
				i.writeMaybe(propertyname);
			}
		}
	}
	
    /**
     * Checks whether the function is invoked both as a constructor (with 'new') and as a function/method (without 'new').
     * @param f function being called
     * @param call node responsible for the call
     * @param constructor if set, the call uses 'new'
     */
	public void visitUserFunctionCall(Function f, AbstractNode call, boolean constructor) { // TODO: avoid warning if the call is a super-call in a constructor
        Status s;
        if (!scan_phase) {
    		if (constructor) { // in non-scan phase, collect all constructor calls 
    			called_as_constructor.add(f);
    		}
            return;
        } else if (!constructor && called_as_constructor.contains(f) ) { // in scan phase, report non-constructor calls to functions that are also used in constructor calls
            s = Status.CERTAIN;
        } else
            s = Status.NONE;
   		addMessage(call, // use the location of the non-constructor call node
       				s,
       				Severity.MEDIUM,
       				"The function" + (f.getName() != null ? " " + Strings.escape(f.getName()) : "") + " is invoked both as constructor and function");
	}
	
	/**
	 * Registers a potential call/construct to a non-function value.
	 * @param n node responsible for the call
	 * @param maybe_non_function if set, this call may involve a non-function value
	 * @param maybe_function if set, this call may involve a function value
	 */
	public void visitCall(AbstractNode n, boolean maybe_non_function, boolean maybe_function) {
        if (!scan_phase) {
        	return;
        }
		Status s = maybe_non_function ? (maybe_function ? Status.MAYBE : Status.CERTAIN) : Status.NONE;
        if (s != Status.NONE) {
    		call_to_non_function.add(n);
        }
		addMessage(n, s, Severity.HIGH, "TypeError, call to non-function");
	}

	/**
	 * Registers a call to eval.
	 * @param n node that may call eval
	 * @param v value being eval'ed
	 */
	public void visitEvalCall(AbstractNode n, Value v) {
		if (!scan_phase || !Options.isEvalStatistics()) {
			return;
		}
		Value vv = eval_calls.get(n);
		if (vv != null) {
			v = v.join(vv);
		}
		eval_calls.put(n, v);
	}

	/**
	 * Registers a write to innerHTML.
	 * @param n node where the write occurs
	 * @param v value being written
	 */
	public void visitInnerHTMLWrite(Node n, Value v) {
		if (!scan_phase || !Options.isEvalStatistics()) {
			return;
		}
		Value vv = inner_html_writes.get(n);
		if (vv != null) {
			v = v.join(vv);
		}
		inner_html_writes.put(n, v);
	}

    /**
     * Checks the number of parameters for a call to a native function.
     * @param n node responsible for the call
     * @param hostobject the native function being called
     * @param num_actuals_unknown if set, the number of actuals is unknown
     * @param num_actuals number of actuals (if num_actuals_unknown is not set)
     * @param min minimum number of parameters expected
     * @param max maximum number of paramaters expected (-1 for any number)
     */
    public void visitNativeFunctionCall(AbstractNode n, HostObject hostobject, boolean num_actuals_unknown, int num_actuals, int min, int max) {
    	if (!scan_phase) {
    		return;
    	}
    	Status s1 = (!num_actuals_unknown && num_actuals < min) ? Status.CERTAIN : Status.NONE;
        addMessage(n, s1, Severity.MEDIUM, "Too few parameters to function " + hostobject);
        if (max != -1) {
			Status s2 = num_actuals_unknown ? Status.MAYBE : num_actuals > max ? Status.CERTAIN : Status.NONE;
			addMessage(n, s2, Severity.HIGH, "Too many parameters to function " + hostobject);
        }
    }

	/**
	 * Record type information about a var/prop read.
	 */
	public void visitRead(Node n, ICallContext<?> c, Value v, BlockStateType state) {
        if (!scan_phase) {
        	return;
        }
        v = UnknownValueResolver.getRealValue(v, state); // TODO: does this ruin the benefits of polymorphic values?
		value_reads.put(new NodeAndContext<ICallContext<?>>(n, c), v);
	}
	
	/**
	 * Registers that the given variable is read; also checks for suspicious type mixings.
	 * @param n (non-this) read variable operation 
	 * @param v value being read
	 */
	public void visitVariableAsRead(ReadVariableNode n, Value v, BlockStateType state) { 
        if (!scan_phase) {
        	return;
        }
        // record in read_variables
        String varname = n.getVariableName();
    	Function f = n.getBlock().getFunction();
        // The arguments object is special, but other than that walk upwards in scope to find where the variable belongs.
    	while (!"arguments".equals(varname) && !f.getVariableNames().contains(varname) && !f.getParameterNames().contains(varname)) {
            // Only main should be without an outer function.
            if (!f.hasOuterFunction())
                break;
            f = f.getOuterFunction(); // FIXME: what about 'with' and 'catch' blocks?
    	}
    	addToMapSet(read_variables, f, varname);
    	// report suspicious type mixings
        v = UnknownValueResolver.getRealValue(v, state);
        Status s; // TODO: join values across contexts when checking for suspicious type mixings?
        int i = 0;
        if (!v.isNotStr()) {
        	i++;
        }
        if (!v.isNotNum()) {
        	i++;
        }
        if (!v.isNotBool()) {
        	i++;
        }
        if (v.isMaybeObject() || v.isMaybeNull()) {
        	i++;
        }
        if (i > 1) {
        	s = Status.MAYBE;
        } else {
        	s = Status.NONE;
        }
		addMessage(n, s, Severity.LOW, "The variable " + Strings.escape(varname) + " has values with different types"); // TODO: also check property reads with different types?
    }
	
	@Override
	public void visitRecoveryGraph(int size) {
		Integer count = recovery_graph_sizes.get(size);
		if (count == null)
			count = 0;
		recovery_graph_sizes.put(size, count + 1);
	}

	/**
	 * Returns a string description of the results.
	 */
	@Override
	public String toString() {
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
		StringBuilder b = new StringBuilder();

		if (Options.isNewFlowEnabled()) {
			TreeMap<Integer,String> sorted = new TreeMap<>();
			b.append("\nNew flow at each function for each context:");
			for (Map.Entry<BasicBlock,Map<ICallContext<?>,List<String>>> me1 : newflows.entrySet()) {
				Function f = me1.getKey().getFunction();
				b.append("\n").append(f).append(" at ").append(f.getSourceLocation()).append(":");
				for (Map.Entry<ICallContext<?>,List<String>> me2 : me1.getValue().entrySet()) {
					b.append("\n  ").append(me2.getKey()).append(" state diffs: ").append(me2.getValue().size());
					for (String diff : me2.getValue()) {
						if (diff != null) {
							b.append("\n    state diff:").append(diff);
						}
					}
					sorted.put(me2.getValue().size(), me1.getKey().getFunction() + " " + me1.getKey().getFunction().getSourceLocation() + ", context " + me2.getKey());
				}
			}
			b.append("\nSorted new flow:");
			for (Map.Entry<Integer,String> me : sorted.entrySet())
				b.append("\n" + me.getKey() + " new flows at " + me.getValue());
		}

		b.append("\n\nCall/construct nodes with potential call to non-function:                     " + call_to_non_function.size());
		b.append("\nTotal number of call/construct nodes:                                         " + call_nodes);
		b.append("\n==> Call/construct nodes that are certain to never call non-functions:        " + 
				(call_nodes > 0 ? ((call_nodes - call_to_non_function.size()) * 1000 / call_nodes) / 10f + "%" : "-"));
		
		b.append("\n\nRead variable nodes with potential absent variable:                           " + absent_variable_read.size());
		b.append("\nTotal number of (non-this) read variable nodes:                               " + read_variable_nodes);
		b.append("\n==> Read variable nodes that are certain to never read absent variables:      " +
				(read_variable_nodes > 0 ? ((read_variable_nodes - absent_variable_read.size()) * 1000 / read_variable_nodes) / 10f + "%" : "-"));

		b.append("\n\nProperty access nodes with potential null/undef base:                         " + null_undef_base.size());
		b.append("\nTotal number of property access nodes:                                        " + property_access_nodes);
		b.append("\n==> Property access nodes that are certain to never have null/undef base:     " +
				(property_access_nodes > 0 ? ((property_access_nodes - null_undef_base.size()) * 1000 / property_access_nodes) / 10f + "%" : "-"));

		b.append("\n\nProperty reads resulting in singleton types:                                  " + getSingletonPropertyReads());
		b.append("\nVariable reads resulting in singleton types:                                  " + getSingletonVariableReads());
		// FIXME: only compare with reads with typeSize>0 ? (typeSize is 0 for polymorphic values...)
		float p_var = getVarReadsSize() > 0 ? (float)getSingletonVariableReads()  * 100 / getVarReadsSize() : -1;
		float p_prop = getPropReadsSize() > 0 ? (float)getSingletonPropertyReads()  * 100 / getPropReadsSize() : -1;
		float p_all = value_reads.size() > 0 ? (float)(getSingletonPropertyReads() + getSingletonVariableReads()) * 100 / value_reads.size() : -1;
		b.append("\n==> Variable reads with singleton results:                                    " + (p_var == -1 ? "-" : p_var + "%"));
		b.append("\n==> Property reads with singleton results:                                    " + (p_prop == -1 ? "-" : p_prop + "%"));
		b.append("\n==> Reads with singleton results:                                             " + (p_all == -1 ? "-" : p_all + "%"));

		b.append("\n==> Average type size in property reads:                                      " + formatter.format(getAveragePropertyTypeSize()));
		b.append("\n==> Average type size in variable reads:                                      " + formatter.format(getAverageVariableTypeSize()));
		b.append("\n==> Average type size in all reads:                                           " + formatter.format(geTotalAverageTypeSize()));
        b.append("\n==> Reads with at most one type:                                              " + formatter.format(getReadsWithAtMostOneType()));
        b.append("\n==> Reads with at least two types:                                            " + formatter.format(getReadsWithAtleastTwoTypes()));

		b.append("\n\nFixed-property read nodes with potential absent property:                     " + absent_fixed_property_read.size());
		b.append("\nTotal number of fixed-property read nodes:                                    " + read_fixed_property_nodes);
		b.append("\n==> Fixed-property read nodes that are certain to never have absent property: " +
				(read_fixed_property_nodes > 0 ? ((read_fixed_property_nodes - absent_fixed_property_read.size()) * 1000 / read_fixed_property_nodes) / 10f + "%" : "-"));

		b.append("\n\nTotal number of functions:                                                    " + functions);
		b.append("\nNumber of unreachable functions:                                              " + unreachable_functions.size());

		b.append("\n\nNode transfers: " + node_transfers);
		b.append("\nBlock transfers: " + block_transfers);
		b.append("\nUnknown-value recoveries: \n" + 
				" analysis: partial=" + unknown_value_resolve_analyzing_partial + ", full=" + unknown_value_resolve_analyzing_full + "\n" +
				" scanning: partial=" + unknown_value_resolve_scanning_partial + ", full=" + unknown_value_resolve_scanning_full);
		b.append("\nState joins: " + joins + "\n");

		if (Options.isMemoryMeasurementEnabled()) {
			formatter.setMaximumFractionDigits(2);
			b.append(" Max memory used: " + formatter.format((max_memory / (1024L * 1024L)))).append("M\n");
		}
		if (Options.isEvalStatistics()) {
			//TODO: extend eval statistics, e.g. to include Identifer and prefix information
			Set<String> eval_const_use = newSet();
			Set<String> inner_const_use = newSet();
			Map<AbstractNode,String> eval_anystr_use = newMap();
			Map<AbstractNode,String> inner_anystr_use = newMap();
			sortMustFromMaybe(eval_const_use, eval_anystr_use, eval_calls);
			sortMustFromMaybe(inner_const_use, inner_anystr_use, inner_html_writes);
			b.append("\nUse of eval/innerHTML\n");
			b.append(" Constant eval'ed strings:\n");
			for (String s : eval_const_use) {
				b.append("  ").append(s).append("\n").append("  ==\n");
			}
			b.append(" Constant innerHTML strings:\n");
			for (String s : inner_const_use) {
				b.append("  ").append(s).append("\n").append("  ==\n");
			}
			b.append(" Source locations with maybe values for eval:\n");
			for (Entry<AbstractNode, String> g : eval_anystr_use.entrySet()) {
				b.append("   ").append(g.getKey().getSourceLocation()).append(": ").append(g.getValue());
			}
			b.append("\n Source locations with maybe values for innerHTML:\n");
			for (Entry<AbstractNode, String> g : inner_anystr_use.entrySet()) {
				b.append("   ").append(g.getKey().getSourceLocation()).append(": ").append(g.getValue());
			}
			b.append("\n");
		}
		b.append("Recovery graph sizes: " + recovery_graph_sizes + "\n");
		return b.toString();
	}

	private int getVarReadsSize() {
		int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadVariableNode) { 
				res++;
			}
		}
		return res;
	}

	private int getPropReadsSize() {
		int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadPropertyNode) {
				res++;
			}
		}
		return res;
	}

	private double getAverageVariableTypeSize() {
		double res = 0;
		int div = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadVariableNode) {
				res += value_reads.get(n).typeSize();
				div ++;
			}
		}
		if (div == 0) {
			return 0;
		}
		res = (1000*res) / div;
		return res / 1000;
	}

	private double getAveragePropertyTypeSize() {
		double res = 0;
		int div = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadPropertyNode) {
				res += value_reads.get(n).typeSize();
				div ++;
			}
		}
		if (div == 0) {
			return 0;
		}
		res = (1000*res) / div;
		return res / 1000;
	}

	private double geTotalAverageTypeSize() {
		double res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			res += value_reads.get(n).typeSize();
		}
		res = (1000*res) / value_reads.size();
		return res / 1000;
	}

    private int getReadsWithAtMostOneType() {
        int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if(value_reads.get(n).typeSize() <= 1) {
                res++;
            }
		}
        return res;
    }

    private int getReadsWithAtleastTwoTypes() {
        int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if(value_reads.get(n).typeSize() > 1) {
                res++;
            }
		}
        return res;
    }

	private int getSingletonPropertyReads() {
		int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadPropertyNode) {
				if (value_reads.get(n).typeSize() == 1)
					res++;
			}
		}
		return res;
	}

	private int getSingletonVariableReads() {
		int res = 0;
		for (NodeAndContext<ICallContext<?>> n : value_reads.keySet()) {
			if (n.getNode() instanceof ReadVariableNode) {
				if (value_reads.get(n).typeSize() == 1)
					res++;
			}
		}
		return res;
	}

	private static void sortMustFromMaybe(
			Set<String> eval_const_use,
			Map<AbstractNode, String> eval_anystr_use, 
			Map<AbstractNode,Value> m) {
		for (Entry<AbstractNode, Value> e : m.entrySet()) {
			AbstractNode n = e.getKey();
			Value v = e.getValue();
			if (v.getStr() != null) {
				eval_const_use.add(v.getStr());
			} else {
				eval_anystr_use.put(n, v.toString());
			}
		}
	}

	/**
	 * Produces output showing the parts of the code that is determined unreachable by the analysis.
	 */
	public void generateUnreachableMap() {
		for (String fs : unreachable_lines.keySet()) {
			System.out.println("== unreachable info for " + fs + " ==");
			try (BufferedReader r =  new BufferedReader(new FileReader(fs))) {
				Set<Integer> ls = unreachable_lines.get(fs);
				if (ls == null) {
					ls = Collections.emptySet();
				}
				String line = "";
				int i = 1;
				while ((line = r.readLine()) != null) {
					String prefix;
					if (ls.contains(i++)) {
						prefix = "!!!";
					} else
						prefix = "   ";
					System.out.println(prefix + line);
				}
			} catch (IOException e) {
				throw new AnalysisException(e);
			}
		}
	}

	/**
     * Registers the name, location, and value of a variable being read or written.
     */
	public void visitVariableOrProperty(String var, SourceLocation loc, Value value) { 
        if (scan_phase && Options.isCollectVariableInfoEnabled()) {
        	type_collector.record(var, loc, value);
        }
	}
	
	/**
	 * Returns the collected type information.
	 */
	public Map<VariableSummary, Value> getTypeInformation() { // Used by the TAJS Eclipse plug-in
		return type_collector.getTypeInformation();
	}
	
	/**
	 * Presents the collected type information.
	 */
	public void presentTypeInformation() {
		type_collector.presentTypeInformation();
	}
	
    /**
     * Adds a message for the given node. If not in scan phase, nothing is done. 
     * Uses Status.INFO.
     * Uses the message as key (must be a fixed string).
     */
    public void addMessageInfo(AbstractNode n, Severity severity, String msg) {
    	addMessage(n, Status.INFO, severity, msg);
    }
    
    /**
     * Adds a message for the given node. If not in scan phase, nothing is done. 
     * Uses Status.MAYBE.
     * Uses the message as key (must be a fixed string).
     */
    public void addMessage(AbstractNode n, Severity severity, String msg) {
    	addMessage(n, Status.MAYBE, severity, msg);
    }
    
    /**
     * Adds a message for the given node. If not in scan phase, nothing is done. 
     * If the message already exists, the status is joined.
     * Uses the message as key (must be a fixed string).
     */
    private void addMessage(AbstractNode n, Status s, Severity severity, String msg) { // TODO: collect all message generation in Monitoring? (then make addMessage private?) 
    	addMessage(n, s, severity, msg, msg);
    }
    
    /**
     * Adds a message for the given node. If not in scan phase, nothing is done. 
     * Uses Status.MAYBE.
     * The key must be a fixed string.
     */
    public void addMessage(AbstractNode n, Severity severity, String key, String msg) {
    	addMessage(n, Status.MAYBE, severity, key, msg);
    }
    
    /**
     * Adds a message for the given node. If not in scan phase, nothing is done. 
     * If the message already exists, the status is joined.
     * The key must be a fixed string.
     */
    private void addMessage(AbstractNode n, Status s, Severity severity, String key, String msg) {
    	if (scan_phase) {
            boolean log;
            AbstractNode d = n.getDuplicateOf();
            if (d != null) // if n is a duplicate, use the original instead
            	n = d;
            Message m = new Message(n, s, key, msg, severity);
            Message mo = messages.get(m);
            if (mo != null) {
                Status old = mo.getStatus();
                mo.join(m);
                log = !old.equals(mo.getStatus());
            } else {
                messages.put(m, m);
                log = !s.equals(Status.NONE);
            }
            if (log && logger.isDebugEnabled()) {
    				logger.debug("addMessage: " + m.getStatus() + " " + m.getNode().getSourceLocation() + ": " + m.getMessage());
            }
        }
    }
    
    /**
     * Returns the collected messages.
     */
    public Set<Message> getMessages() {
    	return newSet(messages.values());
    }
    
    /**
     * Returns the sorted list of messages produced during scanning.
     * Ignores those messages that have Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE if not Status.CERTAIN
     * and those that have Severity.LOW if not the option low-severity is enabled.
     */
    private List<Message> getSortedMessages() {
        List<Message> es = newList();
        for (Message m : messages.values()) {
            if (m.getStatus() != Status.NONE
                    && !(m.getSeverity() == Severity.MEDIUM_IF_CERTAIN_NONE_OTHERWISE && m.getStatus() != Status.CERTAIN)
                    && !(m.getSeverity() == Severity.LOW && !Options.isLowSeverityEnabled())) {
                es.add(m);
            }
        }
        java.util.Collections.sort(es);
        return es;
    }
}
