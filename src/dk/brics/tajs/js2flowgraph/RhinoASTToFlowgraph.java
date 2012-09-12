/*
 * Copyright 2012 Aarhus University
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

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.CallbackKind;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.FlowGraphFragment;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.ICallNode;
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
import dk.brics.tajs.flowgraph.jsnodes.EnterWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventEntryNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.LeaveWithNode;
import dk.brics.tajs.flowgraph.jsnodes.LoadNode;
import dk.brics.tajs.flowgraph.jsnodes.NewObjectNode;
import dk.brics.tajs.flowgraph.jsnodes.NextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.ReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.UnaryOperatorNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.htmlparser.DOMEventHelpers;
import dk.brics.tajs.htmlparser.HtmlSource;
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;

public class RhinoASTToFlowgraph { // TODO: javadoc

	private static Logger logger = Logger.getLogger(RhinoASTToFlowgraph.class);

	private static final String TAJS_DIVIDER = "<TAJS_DIVIDER>"; // TODO: fix this weird hack?

	/**
	 * The graph being constructed.
	 */
	private final FlowGraph graph;

	/**
	 * The environment for the compiler; always the same once it is initialized.
	 */
	private final CompilerOptions compilerOptions;

	/**
	 * The maximum value that is used for a temporary variable.
	 */
	private int maxTmp = AbstractNode.FIRST_ORDINARY_REG;

	/**
	 * Event Handlers
	 */
	private Collection<JavaScriptSource> eventHandlers = newSet();

	private Collection<HtmlSource> htmlElements = new HashSet<>();

	/**
	 * The constructor. Translates a Rhino AST to a TAJS flow graph.
	 */
	public RhinoASTToFlowgraph() {
		graph = new FlowGraph();
		compilerOptions = new CompilerOptions();
		compilerOptions.setIdeMode(false);
		CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(compilerOptions);
	}

	/**
	 * Builds a flow graph for a list of source files.
	 * 
	 * @throws IOException
	 */
	public void buildFromFiles(List<String> files) {
		if (files.isEmpty())
			return;
		Pair<Node, Map<String, Integer>> p = parseFiles(files);
		buildTopLevel(p.getFirst(), p.getSecond());
	}

	/**
	 * Builds a flow graph for a list of sources gathered from an HTML file.
	 */
	public void buildFromSources(List<HtmlSource> elements, List<JavaScriptSource> newEventHandlers, List<JavaScriptSource> sources) { // FIXME: should have stripped "javascript:" from the source code before calling this method
		htmlElements.addAll(elements);
		eventHandlers.addAll(newEventHandlers);
		build(sources);
	}

	private void build(String sourceString, String sourceFile, int lineNo) {
		Node root = parse(sourceString, lineNo + TAJS_DIVIDER + sourceFile);
		Map<String, Integer> map = newMap();
		map.put(lineNo + TAJS_DIVIDER + sourceFile, lineNo);
		buildTopLevel(root, map);
	}

	private void build(List<JavaScriptSource> sources) {
		if (sources.isEmpty()) {
			build("", "dummy_placeholder.js", 0);
			return;
		}
		Pair<Node, Map<String, Integer>> p = parseSources(sources);
		buildTopLevel(p.getFirst(), p.getSecond());
	}

	/**
	 * Finishes the flow graph being constructed by this builder and returns the
	 * flow graph. Call this method when all JS code has been parsed.
	 */
	public FlowGraph close() {
		FlowGraph fg = graph.complete();
		if (Options.isTestFlowGraphBuilderEnabled())
			System.out.println("fg2: " + fg.toString());
		fg.check();
		return fg;
	}

	/**
	 * Adds and event loop to the flow graph. Does not make sense if DOM is
	 * not enabled.
	 */
	private BasicBlock addEventLoop(FlowGraphEnv env, BasicBlock bb, String filename) {
		if (!(Options.isDOMEnabled() || Options.isDSLEnabled()))
			return bb;

		BasicBlock currBB = newBasicBlock(env, bb);

		int eventVar = nextTemporary(env);
		for (JavaScriptSource src : eventHandlers) {
			Function f = translateEventHandler(env, currBB, src, eventVar);
			String name = src.getEventName();
			CallbackKind k;
			if (DOMEventHelpers.isLoadEventAttribute(name))
				k = CallbackKind.LOAD;
			else if (DOMEventHelpers.isUnloadEventAttribute(name))
				k = CallbackKind.UNLOAD;
			else if (DOMEventHelpers.isKeyboardEventAttribute(name))
				k = CallbackKind.KEYBOARD;
			else if (DOMEventHelpers.isMouseEventAttribute(name))
				k = CallbackKind.MOUSE;
			else
				k = CallbackKind.UNKNOWN;
			env.fg.addCallback(f, k);
		}

		SourceLocation srcLoc = newSourceLocation(0, -1, filename);

		if (Options.isSingleEventHandlerLoop()) { // TODO: remove Options.isSingleEventHandlerLoop()?
			/*
			 * // *ALL* Event Handlers
			 * BasicBlock singleBB = newBasicBlock(env, currBB);
			 * singleBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.SINGLE, newSourceLocation(0, -1, filename)));
			 * newBasicBlock();
			 * BasicBlock nopPostSingle = newBasicBlock(env, singleBB);
			 * nopPostSingle.addNode(new NopNode(srcLoc));
			 * nopPostSingle.addSuccessor(normal_exitBB);
			 * return XXXXFIXME;
			 */
		}

		// Event nodes need to be in their own block, and they can only have a single successor (like call nodes),
		// but different events might happen in any order so there needs to be some other blocks.
		// The other blocks can't be empty because of flow graph invariants so there's nop nodes in there.

		// Event Entry Block
		BasicBlock entryBB = newBasicBlock(env, currBB);
		entryBB.addNode(new EventEntryNode(srcLoc));

		// Load Event Handlers
		BasicBlock loadBB = newBasicBlock(env, entryBB);
		loadBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.LOAD, srcLoc));

		BasicBlock nopPostLoad = newBasicBlock(env, loadBB);
		nopPostLoad.addNode(new NopNode(srcLoc));
		nopPostLoad.addSuccessor(loadBB);

		// Other Event Handlers
		BasicBlock otherBB = newBasicBlock(env, nopPostLoad);
		otherBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.OTHER, srcLoc));

		BasicBlock nopPostOther = newBasicBlock(env, otherBB);
		nopPostOther.addNode(new NopNode(srcLoc));
		nopPostOther.addSuccessor(otherBB);

		// Unload Event Handlers
		BasicBlock unloadBB = newBasicBlock(env, nopPostOther);
		unloadBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.UNLOAD, srcLoc));

		BasicBlock nopPostUnload = newBasicBlock(env, unloadBB);
		nopPostUnload.addNode(new NopNode(srcLoc));
		nopPostUnload.addSuccessor(unloadBB);

		entryBB.addSuccessor(nopPostLoad);
		nopPostLoad.addSuccessor(nopPostOther);
		nopPostOther.addSuccessor(nopPostUnload);

		return nopPostUnload;
	}

	private Function translateEventHandler(FlowGraphEnv env, BasicBlock bb, JavaScriptSource src, int resultVar) {
		// XXX: Should the function be added to env.declsBB?
		String params = src.getEventName().equals("TIMEOUT") ? "" : "event";
		String wrappedJS = "(function (" + params + ") {" + src.getJavaScript() + "})";
		String mapName = src.getLineNumber() + TAJS_DIVIDER + src.getFileName();
		Node evRoot = parse(wrappedJS, mapName).getLastChild().getLastChild().getLastChild();
		env.lineOffsets.put(mapName, src.getLineNumber());
		SourceLocation srcLoc = newSourceLocation(getLine(evRoot, env.lineOffsets), evRoot.getCharno(), src.getFileName());
		return translateFunction(env, bb, evRoot, srcLoc, resultVar);
	}

	private Pair<Node, Map<String, Integer>> parseSources(List<JavaScriptSource> sources) {
		Compiler compiler = new Compiler();
		JSSourceFile[] fs = new JSSourceFile[sources.size()];
		Map<String, Integer> map = newMap();
		for (int i = 0; i < fs.length; i++) {
			JavaScriptSource ss = sources.get(i);
			fs[i] = JSSourceFile.fromCode(ss.getLineNumber() + TAJS_DIVIDER + ss.getFileName(), ss.getJavaScript());
			int lineCompensation = ss.getFileName().endsWith(".htm") || ss.getFileName().endsWith(".html") ? -1 : 0;
			map.put(ss.getLineNumber() + TAJS_DIVIDER + ss.getFileName(), ss.getLineNumber() + lineCompensation);
		}
		SourceFile dummy = SourceFile.fromCode("dummy.js", "");

		compiler.compile(dummy, fs, compilerOptions);

		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error in file:");

		return Pair.make(getParentOfFirstInterestingNode(compiler), map);
	}

	private Node parse(String sourceString, String sourceFile) {
		Compiler compiler = new Compiler();

		compiler.compile(JSSourceFile.fromCode("dummy.js", ""), JSSourceFile.fromCode(sourceFile, sourceString), compilerOptions);

		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error on code:" + sourceString);

		return getParentOfFirstInterestingNode(compiler);
	}

	private Pair<Node, Map<String, Integer>> parseFiles(List<String> sourceFiles) {
		Compiler compiler = new Compiler();
		JSSourceFile[] fs = new JSSourceFile[sourceFiles.size()];
		Map<String, Integer> map = newMap();
		for (int i = 0; i < fs.length; i++) {
			if ("-".equals(sourceFiles.get(i))) { // Read from stdin
				try {
					fs[i] = JSSourceFile.fromInputStream("<stdin>", System.in);
				} catch (Exception e) {
					throw new AnalysisException("Unable to parse stdin");
				}
			} else
				fs[i] = JSSourceFile.fromFile(sourceFiles.get(i));
			map.put(sourceFiles.get(i), 0);
		}

		SourceFile dummy = SourceFile.fromCode("dummy.js", "");

		compiler.compile(dummy, fs, compilerOptions);

		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error in file: " + sourceFiles);

		return Pair.make(getParentOfFirstInterestingNode(compiler), map);
	}

	/**
	 * The tree we get from Rhino has a few layers of indirection. Get rid of a couple of them.
	 */
	private static Node getParentOfFirstInterestingNode(Compiler comp) {
		return comp.getRoot().getLastChild();
	}

	private static int getLine(Node n, Map<String, Integer> map) {
		Integer offset = map.get(n.getSourceFileName());
		return n.getLineno() + (offset == null ? 0 : offset);
	}

	private static String getFileName(Node n) {
		String fname = n.getSourceFileName();
		int dividerIndex = fname.indexOf(TAJS_DIVIDER);
		return dividerIndex < 0 ? fname : fname.substring(dividerIndex + TAJS_DIVIDER.length());
	}

	/**
	 * Build the top level of a javascript file.
	 */
	private void buildTopLevel(Node root, Map<String, Integer> map) { // TODO: javadoc (this 'map' stuff looks like a messy hack...)
		// The HTML parser is wrong about the line numbers. Try to correct it with some magic constants.
		boolean fromHtmlParser = root.getLastChild().getSourceFileName().endsWith("html") || root.getLastChild().getSourceFileName().endsWith("htm");

		// The line information might be garbled and give us negative line numbers. If that happens the block order
		// gets wrong, so pick a sub node to avoid crashing.
		Node subRoot = getLine(root.getLastChild(), map) < 0 ? root.getLastChild().getFirstChild() : root.getLastChild();

		SourceLocation srcLoc = newSourceLocation(getLine(subRoot, map) + (fromHtmlParser ? 2 : 0), subRoot.getCharno(), getFileName(subRoot));

		Function fun = new Function(null, null, null, srcLoc);

		FlowGraphEnv env = new FlowGraphEnv(graph, dk.brics.tajs.util.Collections.<List<BasicBlock>> newList(), newBBList(), fun, map);

		BasicBlock bb = setupFunction(env, fun, srcLoc);
		BasicBlock retBlock = translateChildren(env, bb, root, AbstractNode.RETURN_REG);

		BasicBlock retBlockPostEvents = addEventLoop(env, retBlock, srcLoc.getFileName());
		retBlockPostEvents.addSuccessor(env.retBB);
		fun.setMaxRegister(maxTmp);

		env.pendingBBs.add(env.retBB);
		env.pendingBBs.add(env.panicBB);

		for (List<BasicBlock> funBBs : env.pendingFuns)
			env.pendingBBs.addAll(funBBs);

		Map<BasicBlock, Integer> pointedTo = newMap();
		removeEmptyBlocks(env, pointedTo);

		for (BasicBlock pending : env.pendingBBs)
			if ((pointedTo.containsKey(pending) && (pointedTo.get(pending) > 0)) || !pending.isEmpty())
				env.fg.addBlock(pending);
	}

	/**
	 * Removes empty blocks from the pending flow graph.
	 */
	private void removeEmptyBlocks(FlowGraphEnv env, Map<BasicBlock, Integer> pointedTo) {
		boolean changed;
		for (Function f : env.fg.getFunctions()) {
			increaseOrAdd(pointedTo, f.getEntry());
			increaseOrAdd(pointedTo, f.getOrdinaryExit());
			increaseOrAdd(pointedTo, f.getExceptionalExit());
		}

		for (BasicBlock pending : env.pendingBBs) {
			increaseOrAdd(pointedTo, pending.getSuccessors());
			increaseOrAdd(pointedTo, pending.getExceptionHandler());
		}

		do {
			changed = false;
			for (BasicBlock pending : env.pendingBBs) {
				for (BasicBlock child : newList(pending.getSuccessors())) {
					if (child.isEmpty()) {
						changed = true;
						pending.removeSuccessor(child);
						decreaseOrRemove(pointedTo, child);
						for (BasicBlock tmp : child.getSuccessors()) {
							pending.addSuccessor(tmp);
							increaseOrAdd(pointedTo, tmp);
							updateIfNodes(pending, child, tmp, pointedTo);
						}
					}
				}
				BasicBlock panicBB = pending.getExceptionHandler();
				if (panicBB != null && panicBB.isEmpty()) {
					if (panicBB.isEmpty()) {
						changed = true;
						decreaseOrRemove(pointedTo, panicBB);
						pending.setExceptionHandler(panicBB.getSingleSuccessor());
						increaseOrAdd(pointedTo, panicBB.getSingleSuccessor());
					}
				}
			}
		} while (changed);
	}

	/**
	 * We might need to update the targets of the if nodes if we remove empty blocks, so do that.
	 */
	private void updateIfNodes(BasicBlock bb, BasicBlock oldBlock, BasicBlock newBlock, Map<BasicBlock, Integer> pointedTo) {
		if (!bb.isEmpty() && bb.getLastNode() instanceof IfNode) {
			IfNode ifn = (IfNode) bb.getLastNode();
			BasicBlock succTrue = ifn.getSuccTrue();
			BasicBlock succFalse = ifn.getSuccFalse();
			if (oldBlock == succTrue && oldBlock == succFalse) {
				ifn.setSuccessors(newBlock, newBlock);
				decreaseOrRemove(pointedTo, oldBlock);
				increaseOrAdd(pointedTo, newBlock);
			} else if (oldBlock == succTrue)
				ifn.setSuccessors(newBlock, succFalse);
			else if (oldBlock == succFalse)
				ifn.setSuccessors(succTrue, newBlock);
			else
				throw new NullPointerException("ASDF" + bb.toString());
		}
	}

	/**
	 * Increases the counter for bb in the map m if it exists, otherwise adds it with default value 1.
	 */
	private static void increaseOrAdd(Map<BasicBlock, Integer> m, BasicBlock bb) {
		if (bb == null)
			return;
		Integer n = 0;
		if (m.containsKey(bb))
			n = m.get(bb);
		m.put(bb, n + 1);
	}

	/**
	 * Increases the counter for all the blocks in bbs in the map m, or adds them with the default 1 if they don't exist.
	 */
	private static void increaseOrAdd(Map<BasicBlock, Integer> m, Iterable<BasicBlock> bbs) {
		for (BasicBlock bb : bbs)
			increaseOrAdd(m, bb);
	}

	/**
	 * Decreases or removes the counter for the basic block bb in m.
	 */
	private boolean decreaseOrRemove(Map<BasicBlock, Integer> m, BasicBlock bb) {
		Integer n = -1;
		if (m.containsKey(bb))
			n = m.get(bb);
		if (n > 1)
			m.put(bb, n - 1);
		else if (n > -1) {
			m.remove(bb);
			decreaseOrRemove(m, bb.getExceptionHandler());
		}
		return (n > 1) || n == -1;
	}

	/**
	 * Convenience function that translates all the children of a node.
	 * Only for statement-level nodes.
	 */
	private BasicBlock translateChildren(FlowGraphEnv env, BasicBlock bb, Node root, int resultVar) {
		BasicBlock currBB = bb;
		for (Node n : root.children())
			currBB = translateNode(env, currBB, n, resultVar, true);
		return currBB;
	}

	/**
	 * Helper function that translates a function.
	 */
	private Function translateFunction(FlowGraphEnv env, BasicBlock bb, Node sn, SourceLocation srcLoc, int resultVar) {
		Node left = sn.getFirstChild();
		Node params = left.getNext();
		Node body = params.getNext();
		List<String> args = params.hasChildren() ? Collections.<String> newList() : java.util.Collections.<String> emptyList();

		// If we don't have a name we're a function expression. But we're also a function expression if
		// we're in a RHS position, so if our resultVar is different from the constant node in the entry block
		// that's it. BUT we can also be a target for the unevalizer, so that might also make us a function expression.
		boolean exprContext = (left.isName() && left.getString().isEmpty()) || !left.isName() ||
				!(((ConstantNode) env.declsBB.getFirstNode()).getResultRegister() == resultVar ||
				(env.evalResultMap != null && env.evalResultMap.getSecond() == resultVar));

		String name = left.isName() && !left.getString().isEmpty() ? left.getString() : null;

		for (Node c : params.children())
			args.add(c.getString());

		// Move f into FlowGraphEnv?
		Function f = new Function(name, args, env.fun, srcLoc);

		int oldMaxTmp = maxTmp;
		maxTmp = AbstractNode.FIRST_ORDINARY_REG;

		FlowGraphEnv envTmp = new FlowGraphEnv(env.fg, env.pendingFuns, newBBList(), f, env.lineOffsets);
		int newWithScope = exprContext && name != null ? nextTemporary(envTmp) : AbstractNode.NO_VALUE;
		FlowGraphEnv env2 = envTmp.copyAndUpdateBaseObj(newWithScope);
		BasicBlock bb2 = setupFunction(env2, f, srcLoc);

		// Function declarations and function expressions are different. It's even worse with named function expressions.
		if (exprContext)
			bb.addNode(new DeclareFunctionNode(f, true, resultVar, srcLoc));
		else
			env.declsBB.addNode(new DeclareFunctionNode(f, false, AbstractNode.NO_VALUE, srcLoc));

		BasicBlock ret = translateNode(env2, bb2, body, AbstractNode.RETURN_REG, true);
		ret.addSuccessor(env2.retBB);

		env2.pendingBBs.add(env2.retBB);
		env2.pendingBBs.add(env2.panicBB);
		env.pendingFuns.add(env2.pendingBBs);

		f.setMaxRegister(maxTmp);
		maxTmp = oldMaxTmp;

		return f;
	}

	/**
	 * The main worker. Pattern matches on the Rhino node type and calls itself recursively. Returns the last basic
	 * block for the entire translation.
	 */
	@SuppressWarnings("null")
	private BasicBlock translateNode(FlowGraphEnv env, BasicBlock bb, Node sn, int resultVar, boolean statement) {
		SourceLocation srcLoc = newSourceLocation(getLine(sn, env.lineOffsets), sn.getCharno(), getFileName(sn));
		int snType = sn.getType();
		if (logger.isDebugEnabled())
			logger.debug("translating node " + sn.toString(true, true, true));
		switch (snType) {
		case Token.FUNCTION: // function [id]([args]...) {}
			translateFunction(env, bb, sn, srcLoc, resultVar);
			return bb;
		case Token.CONST:
			throw new AnalysisException("Const keyword currently not handled.");
		case Token.VAR: { // var ([id] = [exp]?)+
			Node grandchild;
			BasicBlock currBB = bb;
			for (Node child : sn.children()) {
				String id = child.getString();
				env.declsBB.addNode(new DeclareVariableNode(id, srcLoc));
				env.fun.addVariableName(id);
				// Handle variable initializations in the declaration.
				if ((grandchild = child.getFirstChild()) != null) {
					int writeVar = nextTemporary(env);
					currBB = translateNode(env, currBB, grandchild, writeVar, false);
					addNodeToBlock(env, currBB, new WriteVariableNode(writeVar, id, srcLoc), writeVar, true);
				}
			}
			return currBB;
		}
		case Token.EXPR_RESULT: // [expr]; Expression statements.
		case Token.SCRIPT:
		case Token.BLOCK:
			return translateChildren(env, bb, sn, snType == Token.EXPR_RESULT ? AbstractNode.NO_VALUE : resultVar);
		case Token.NEW:
		case Token.CALL: { // [new] [fn-name]([arg1],...,[argn])
			Node firstChild = sn.getFirstChild();
			boolean complexChildType = firstChild.isGetElem() || firstChild.isGetProp();
			int objVal = snType == Token.NEW || !firstChild.isName() ? AbstractNode.NO_VALUE : nextTemporary(env);
			int lhsVal = nextTemporary(env);
			FlowGraphEnv env2 = env.copyAndUpdateBaseObj(objVal);
			BasicBlock currBB = translateNode(env2, bb, firstChild, lhsVal, false);
			env.nextTmp = env2.nextTmp; // Do not overwrite used registers.
			if (complexChildType) {
				AbstractNode an = env.propertyNodes.get(lhsVal);
				if (an instanceof IPropertyNode)
					objVal = ((IPropertyNode) an).getBaseRegister();
				else if (an instanceof ReadVariableNode)
					objVal = ((ReadVariableNode) an).getResultRegister();
			}
			List<Integer> rhsVals = newList();
			for (Node child : sn.children()) {
				if (child == firstChild)
					continue;
				currBB = translateNode(env, currBB, child, nextTemporary(env, rhsVals), false);
			}
			BasicBlock bb2 = newBasicBlock(env, currBB);
			addNodeToBlock(env, bb2, new CallNode(snType == Token.NEW, false, resultVar, objVal, lhsVal, rhsVals, srcLoc), objVal, statement);
			return newBasicBlock(env, bb2);
		}
		case Token.NAME: // var reference
			addNodeToBlock(env, bb, new ReadVariableNode(sn.getString(), resultVar, env.baseVal, srcLoc), resultVar, statement);
			return bb;
		case Token.STRING: // [string]
			addNodeToBlock(bb, ConstantNode.makeString(sn.getString(), resultVar, srcLoc), statement);
			return bb;
		case Token.NUMBER: // [number]
			addNodeToBlock(bb, ConstantNode.makeNumber(sn.getDouble(), resultVar, srcLoc), statement);
			return bb;
		case Token.TYPEOF: {
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			TypeofNode tp;
			// There's two typeof. One is called with a register, and one with a plain text name. The old Rhino
			// had different tokens for these.
			if (firstChild.isName())
				tp = new TypeofNode(firstChild.getString(), resultVar, srcLoc);
			else {
				int argVal = nextTemporary(env);
				currBB = translateNode(env, bb, firstChild, argVal, false);
				tp = new TypeofNode(argVal, resultVar, srcLoc);
			}
			addNodeToBlock(env, currBB, tp, resultVar, statement);
			return currBB;
		}
		case Token.BITNOT:
		case Token.NOT:
		case Token.POS:
		case Token.NEG: { // [op] [operand]
			int argVal = nextTemporary(env);
			BasicBlock childBlock = translateNode(env, bb, sn.getFirstChild(), argVal, false);
			addNodeToBlock(childBlock, new UnaryOperatorNode(getFlowGraphUnaryOp(snType), argVal, resultVar, srcLoc), statement);
			return childBlock;
		}
		case Token.IN:
		case Token.DIV:
		case Token.LSH:
		case Token.MOD:
		case Token.MUL:
		case Token.RSH:
		case Token.SUB:
		case Token.URSH:
		case Token.BITAND:
		case Token.BITOR:
		case Token.BITXOR:
		case Token.GE:
		case Token.GT:
		case Token.LE:
		case Token.LT:
		case Token.NE:
		case Token.SHNE:
		case Token.SHEQ:
		case Token.EQ:
		case Token.INSTANCEOF:
		case Token.ADD: { // [left-operand] [op] [right-operand]
			int arg1Val = nextTemporary(env), arg2Val = nextTemporary(env);
			BasicBlock currBB = translateNode(env, bb, sn.getFirstChild(), arg1Val, false);
			currBB = translateNode(env, currBB, sn.getLastChild(), arg2Val, false);
			addNodeToBlock(currBB, new BinaryOperatorNode(getFlowGraphBinaryOp(snType), arg1Val, arg2Val, resultVar, srcLoc), statement);
			return currBB;
		}
		case Token.ASSIGN_ADD:
		case Token.ASSIGN_BITAND:
		case Token.ASSIGN_BITOR:
		case Token.ASSIGN_BITXOR:
		case Token.ASSIGN_DIV:
		case Token.ASSIGN_LSH:
		case Token.ASSIGN_MOD:
		case Token.ASSIGN_MUL:
		case Token.ASSIGN_RSH:
		case Token.ASSIGN_SUB:
		case Token.ASSIGN_URSH:
		case Token.ASSIGN: {
			// This is somewhat messy. A plain assignment is lhs = rhs. Other assignments such as lhs += rhs
			// are not plain.
			boolean isPlainAssignment = snType == Token.ASSIGN;
			Node firstChild = sn.getFirstChild(); // lhs
			Node grandChild = firstChild.getLastChild(); // property name of lhs, null if not a property write
			boolean inUnevalMappingMode = env.evalResultMap != null && Options.isUnevalEnabled() && isPlainAssignment
					&& firstChild.isName() && firstChild.getString().equals(env.evalResultMap.getFirst());
			int rhsVal = resultVar == AbstractNode.NO_VALUE ? nextTemporary(env) : resultVar;
			int lhsVal = isPlainAssignment ? AbstractNode.NO_VALUE : nextTemporary(env);
			int baseFirstChildVal = AbstractNode.NO_VALUE;
			int propFirstChildVal = AbstractNode.NO_VALUE;
			int oldRhsVal = AbstractNode.NO_VALUE;
			BasicBlock currBB;
			if (firstChild.isName()) // lhs is a variable?
				currBB = bb;
			else { // lhs is a property write
				baseFirstChildVal = nextTemporary(env);
				propFirstChildVal = grandChild == null || grandChild.isString() ? AbstractNode.NO_VALUE : nextTemporary(env);
				currBB = translateNode(env, bb, firstChild.getFirstChild(), baseFirstChildVal, false); // translating first part of lhs
				if (propFirstChildVal != AbstractNode.NO_VALUE)
					currBB = translateNode(env, currBB, grandChild, propFirstChildVal, false);
			}

			if (!isPlainAssignment) {
				AbstractNode rn;
				oldRhsVal = rhsVal;
				rhsVal = nextTemporary(env);
				if (firstChild.isName())
					rn = new ReadVariableNode(firstChild.getString(), lhsVal, env.baseVal, srcLoc);
				else {
					if (firstChild.getLastChild().isString())
						rn = new ReadPropertyNode(baseFirstChildVal, firstChild.getLastChild().getString(), lhsVal, srcLoc);
					else
						rn = new ReadPropertyNode(baseFirstChildVal, propFirstChildVal, lhsVal, srcLoc);
				}
				addNodeToBlock(env, currBB, rn, baseFirstChildVal, false);
			}
			currBB = translateNode(env, currBB, sn.getLastChild(), inUnevalMappingMode ? env.evalResultMap.getSecond() : rhsVal, false); // translating rhs
			if (!isPlainAssignment) {
				addNodeToBlock(env, currBB, new BinaryOperatorNode(getFlowGraphBinaryOp(sn.getType()), lhsVal, rhsVal, oldRhsVal, srcLoc), rhsVal, false);
				rhsVal = oldRhsVal;
			}
			if (inUnevalMappingMode)
				return currBB;
			if (firstChild.isName()) // lhs is a variable?
				addNodeToBlock(env, currBB, new WriteVariableNode(rhsVal, firstChild.getString(), srcLoc), rhsVal, statement);
			else if (firstChild.isGetElem() || firstChild.isGetProp()) { // lhs is a property write
				AbstractNode an = env.propertyNodes.get(baseFirstChildVal);
				WritePropertyNode wp;
				if (an instanceof ReadPropertyNode) {
					if (grandChild.isString())
						wp = new WritePropertyNode(((LoadNode) an).getResultRegister(), grandChild.getString(), rhsVal, srcLoc);
					else
						wp = new WritePropertyNode(((LoadNode) an).getResultRegister(), propFirstChildVal, rhsVal, srcLoc);
				} else if (an instanceof ReadVariableNode) {
					if (grandChild.isString())
						wp = new WritePropertyNode(((ReadVariableNode) an).getResultRegister(), grandChild.getString(), rhsVal, srcLoc);
					else
						wp = new WritePropertyNode(((ReadVariableNode) an).getResultRegister(), propFirstChildVal, rhsVal, srcLoc);
				} else {
					if (grandChild.isString())
						wp = new WritePropertyNode(baseFirstChildVal, grandChild.getString(), rhsVal, srcLoc);
					else
						wp = new WritePropertyNode(baseFirstChildVal, propFirstChildVal, rhsVal, srcLoc);
				}
				addNodeToBlock(env, currBB, wp, wp.getBaseRegister(), statement);
			}
			return currBB;
		}
		case Token.INC:
		case Token.DEC: {
			Node firstChild = sn.getFirstChild();
			boolean isPost = sn.getIntProp(Node.INCRDECR_PROP) == 1;
			boolean resultUsed = resultVar != AbstractNode.NO_VALUE;
			BinaryOperatorNode.Op op = Token.INC == snType ? BinaryOperatorNode.Op.ADD : BinaryOperatorNode.Op.SUB;
			int oneVar = nextTemporary(env), res = nextTemporary(env);
			bb.addNode(ConstantNode.makeNumber(1, oneVar, srcLoc));
			BasicBlock currBB = translateNode(env, bb, firstChild, res, false);
			int toNumberVar = isPost && resultUsed ? resultVar : nextTemporary(env);
			int binopVar = isPost ? nextTemporary(env) : (resultUsed ? resultVar : nextTemporary(env));
			currBB.addNode(new UnaryOperatorNode(UnaryOperatorNode.Op.PLUS, res, toNumberVar, srcLoc));
			currBB.addNode(new BinaryOperatorNode(op, toNumberVar, oneVar, binopVar, srcLoc));
			if (firstChild.isName())
				addNodeToBlock(env, currBB, new WriteVariableNode(binopVar, firstChild.getString(), srcLoc), binopVar, statement);
			else if (firstChild.isGetElem() || firstChild.isGetProp()) {
				ReadPropertyNode rn = (ReadPropertyNode) env.propertyNodes.get(res);
				WritePropertyNode wp;
				if (rn.isPropertyFixed())
					wp = new WritePropertyNode(rn.getBaseRegister(), rn.getPropertyString(), binopVar, srcLoc);
				else
					wp = new WritePropertyNode(rn.getBaseRegister(), rn.getPropertyRegister(), binopVar, srcLoc);
				addNodeToBlock(env, currBB, wp, rn.getBaseRegister(), statement);
			}
			return currBB;
		}
		case Token.HOOK:
		case Token.IF: {
			// XXX: Insert assume nodes for comparisons with null
			Node condNode = sn.getFirstChild();
			Node trueNode = condNode.getNext();
			Node falseNode = trueNode.getNext();
			int condVar = nextTemporary(env);
			BasicBlock condBlock = translateNode(env, bb, condNode, condVar, false);

			BasicBlock trueBranch = newBasicBlock(env, condBlock);
			BasicBlock falseBranch = newBasicBlock(env, condBlock);

			IfNode ifn = new IfNode(condVar, srcLoc);
			ifn.setSuccessors(trueBranch, falseBranch);
			FlowGraphEnv env2 = env.copyAndGiveUniquePnodes(AbstractNode.NO_VALUE);
			addNodeToBlock(env2, condBlock, ifn, condVar, false); // can't set end-of-statement, condVar may be used at the edges

			BasicBlock trueBlock = translateNode(env2.copyAndGiveUniquePnodes(AbstractNode.NO_VALUE), trueBranch, trueNode, resultVar, false);
			BasicBlock falseBlock = falseNode == null ? falseBranch : translateNode(env.copyAndGiveUniquePnodes(condVar), falseBranch, falseNode, resultVar, false);
			return newJoinBasicBlock(env, trueBlock, falseBlock);
		}
		case Token.TRUE:
		case Token.FALSE:
			addNodeToBlock(bb, ConstantNode.makeBoolean(snType == Token.TRUE, resultVar, srcLoc), statement);
			return bb;
		case Token.NULL:
			addNodeToBlock(bb, ConstantNode.makeNull(resultVar, srcLoc), statement);
			return bb;
		case Token.GET:
		case Token.GETELEM:
		case Token.GETPROP: {
			Node obj = sn.getFirstChild();
			ReadPropertyNode rp;
			int baseVar = nextTemporary(env);
			BasicBlock currBB = translateNode(env, bb, obj, baseVar, false);
			Node prop = obj.getNext();

			if (prop.isString())
				rp = new ReadPropertyNode(baseVar, prop.getString(), resultVar, srcLoc);
			else {
				int rpVal = nextTemporary(env);
				currBB = translateNode(env, currBB, prop, rpVal, false);
				rp = new ReadPropertyNode(baseVar, rpVal, resultVar, srcLoc);
			}
			addNodeToBlock(env, currBB, rp, baseVar, statement);
			return currBB;
		}
		case Token.SET: {
			throw new AnalysisException("Not implemented" + Token.name(sn.getType()));
			// AssumeNode.makePropertyNonNullUndef(baseVar, propNode.getString(), wn, srcLoc);
		}
		case Token.THIS:
			addNodeToBlock(env, bb, new ReadVariableNode("this", resultVar, AbstractNode.NO_VALUE, srcLoc), resultVar, statement);
			return bb;
		case Token.BREAK:
		case Token.CONTINUE: {
			bb.addSuccessor(snType == Token.BREAK ? env.breakBlock : env.continueBlock);
			return newStandaloneBasicBlock(env, bb);
		}
		case Token.EMPTY:
		case Token.LABEL_NAME:
			return bb;
		case Token.LABEL:
			return translateChildren(env, bb, sn, resultVar);
		case Token.RETURN: {
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			if (firstChild != null)
				currBB = translateNode(env, bb, firstChild, resultVar, false);
			currBB.addSuccessor(env.retBB);
			return newStandaloneBasicBlock(env, currBB);
		}
		case Token.TRY: {
			Node tryNode = sn.getFirstChild();
			Node catchNode = tryNode.getNext();
			Node finallyNode = catchNode != null ? catchNode.getNext() : null;
			// Don't bother with translating catch-nodes and finally-nodes without children.
			finallyNode = finallyNode != null && finallyNode.getFirstChild() == null ? null : finallyNode;
			catchNode = catchNode != null && catchNode.getFirstChild() == null ? null : catchNode;
			BasicBlock bbPanicBB = bb.getExceptionHandler();

			BasicBlock tryBlock = newBasicBlock(env, bb);
			BasicBlock finallyBlock = null, catchBlock = null;

			// We need to allocate the catch block early so that all children gets the right exception handler.
			if (catchNode != null)
				catchBlock = newCatchBasicBlock(env, tryBlock);

			BasicBlock retBlock = newStandaloneBasicBlock(env, bb);

			if (finallyNode != null) {
				finallyBlock = newStandaloneBasicBlock(env, bb);
				if (catchNode != null)
					catchBlock.setExceptionHandler(finallyBlock);
				else
					tryBlock.setExceptionHandler(finallyBlock);
			}

			FlowGraphEnv env2 = env.specialCopy(env.continueBlock, retBlock).copyAndGiveUniquePnodes(AbstractNode.NO_VALUE);
			if (finallyNode != null)
				env2.retBB = retBlock; // see wala/try.js, crazy.
			BasicBlock tryBB = translateNode(env2, tryBlock, tryNode, resultVar, false);

			bumpToFrontOfPending(env, retBlock);
			tryBB.addSuccessor(retBlock);

			if (catchNode != null) {
				bumpToFrontOfPending(env, catchBlock);
				BasicBlock currBB = translateNode(env, catchBlock, catchNode, resultVar, false);
				currBB.addSuccessor(retBlock);
				if (finallyNode != null)
					currBB.getExceptionHandler().addSuccessor(finallyBlock);
			}

			// Translate the finally block for the case when an exception is thrown.
			if (finallyNode != null) {
				bumpToFrontOfPending(env, finallyBlock);
				int tryVal = nextTemporary(env);
				// Catch nodes are used for finally blocks as well.
				CatchNode cn = new CatchNode(tryVal, srcLoc);
				cn.setArtificial();
				finallyBlock.addNode(cn);
				BasicBlock tmpBB = translateNode(env, finallyBlock, finallyNode, resultVar, false);
				ThrowNode tn = new ThrowNode(tryVal, srcLoc);
				tn.setArtificial();
				tmpBB.addNode(tn);
				tmpBB.addSuccessor(bbPanicBB);
				BasicBlock tmpRet = retBlock;
				FlowGraphEnv env3 = env.copyAndInitializeForCopy(tmpRet);
				// Insert a copy of the finally block with a regular return point.
				retBlock = translateNode(env3, retBlock, finallyNode, resultVar, false);
				setDuplicateBlocks(env3.copyBlocks, Collections.<BasicBlock> newList(), tmpRet, finallyBlock);
			}
			return retBlock;
		}
		case Token.CATCH: {
			int catchVal = resultVar;
			Node firstChild = sn.getFirstChild();
			if (firstChild.isName()) {
				catchVal = nextTemporary(env);
				bb.addNode(new CatchNode(firstChild.getString(), catchVal, srcLoc));
			}
			bb.addNode(new EnterWithNode(catchVal, srcLoc));
			BasicBlock tempBB = newBasicBlock(env, bb);
			BasicBlock panicBB = newCatchBasicBlock(env, tempBB);
			panicBB.addNode(new LeaveWithNode(srcLoc));
			panicBB.setExceptionHandler(null);
			panicBB.addSuccessor(bb.getExceptionHandler());
			int childVal = nextTemporary(env);
			BasicBlock childBB = translateNode(env.copyAndUpdateBaseObj(childVal), tempBB, sn.getLastChild(), resultVar, false);
			childBB.addNode(new LeaveWithNode(srcLoc));
			return childBB;
		}
		case Token.THROW: {
			int throwVal = nextTemporary(env);
			BasicBlock throwBB = translateNode(env, bb, sn.getFirstChild(), throwVal, false);
			addNodeToBlock(throwBB, new ThrowNode(throwVal, srcLoc), statement);
			return throwBB;
		}
		case Token.WITH: {
			int withVal = nextTemporary(env);
			BasicBlock currBB = translateNode(env, bb, sn.getFirstChild(), withVal, false);
			addNodeToBlock(currBB, new EnterWithNode(withVal, srcLoc), statement);
			currBB = newBasicBlock(env, currBB);
			BasicBlock panicBB = newCatchBasicBlock(env, currBB);
			addNodeToBlock(panicBB, new LeaveWithNode(srcLoc), statement);
			panicBB.addSuccessor(bb.getExceptionHandler());
			panicBB.setExceptionHandler(null);
			int baseVal = nextTemporary(env);
			currBB = translateNode(env.copyAndUpdateBaseObj(baseVal), currBB, sn.getLastChild(), resultVar, false);
			AbstractNode lastNode = currBB.isEmpty() ? null : currBB.getLastNode();
			currBB = requiresNewBlock(lastNode) ? newBasicBlock(env, currBB) : currBB;
			addNodeToBlock(currBB, new LeaveWithNode(srcLoc), statement);
			currBB.setExceptionHandler(bb.getExceptionHandler());
			return currBB;
		}
		case Token.ARRAYLIT: {
			List<Integer> argVars = newList();
			BasicBlock currBB = bb;
			int arrVal = nextTemporary(env);
			addNodeToBlock(env, currBB, new ReadVariableNode("Array", arrVal, AbstractNode.NO_VALUE, srcLoc), arrVal, false);
			for (Node child : sn.children()) {
				if (child.isEmpty())
					currBB.addNode(ConstantNode.makeUndefined(nextTemporary(env, argVars), srcLoc));
				else
					currBB = translateNode(env, currBB, child, nextTemporary(env, argVars), false);
			}

			currBB = newBasicBlock(env, currBB);
			addNodeToBlock(env, currBB, new CallNode(true, true, resultVar, AbstractNode.NO_VALUE, arrVal, argVars, srcLoc), resultVar, statement);
			return newBasicBlock(env, currBB);
		}
		case Token.OBJECTLIT: { // {(prop: [exp])*}
			BasicBlock currBB = bb;
			addNodeToBlock(env, currBB, new NewObjectNode(resultVar, srcLoc), resultVar, false);

			// So the children of object literals have their own children, even if they are of a simple
			// data type. Special case for them here.

			// SPEC: 11.1.5 A property name can be either a string or number literal.
			for (Node child : sn.children()) {
				WritePropertyNode wn;
				int propValueVar = nextTemporary(env);
				currBB = translateNode(env, currBB, child.getFirstChild(), propValueVar, false);
				if (child.isString())
					wn = new WritePropertyNode(resultVar, child.getString(), propValueVar, srcLoc);
				else {
					int propNameVar = nextTemporary(env);
					currBB = translateNode(env, currBB, child, propNameVar, false);
					wn = new WritePropertyNode(resultVar, propNameVar, propValueVar, srcLoc);
				}
				addNodeToBlock(env, currBB, wn, resultVar, false);
			}
			return currBB;
		}
		case Token.DELPROP: { // delete [exp] or delete [identifier]
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			int firstChildType = firstChild.getType();
			int delVal = resultVar == AbstractNode.NO_VALUE ? nextTemporary(env) : resultVar;
			if (firstChildType == Token.GETELEM || firstChildType == Token.GETPROP) {
				int objVal = nextTemporary(env), propVal = nextTemporary(env);
				currBB = translateNode(env, bb, firstChild.getFirstChild(), objVal, false);
				currBB = translateNode(env, currBB, firstChild.getLastChild(), propVal, false);
				addNodeToBlock(currBB, new DeletePropertyNode(objVal, propVal, delVal, srcLoc), statement);
			} else
				addNodeToBlock(currBB, new DeletePropertyNode(firstChild.getString(), delVal, srcLoc), statement);
			return currBB;
		}
		case Token.OR:
		case Token.AND: { // [exp] AND/OR [exp]
			int arg1Val = resultVar == AbstractNode.NO_VALUE ? nextTemporary(env) : resultVar;
			IfNode ifn = new IfNode(arg1Val, srcLoc);
			FlowGraphEnv env2 = snType == Token.AND ? env : env.copyAndGiveUniquePnodes(AbstractNode.NO_VALUE);

			BasicBlock arg1Block = translateNode(env2, bb, sn.getFirstChild(), arg1Val, false);

			BasicBlock trueBranch = newBasicBlock(env, arg1Block);
			BasicBlock falseBranch = newBasicBlock(env, arg1Block);

			if (snType == Token.AND)
				ifn.setSuccessors(falseBranch, trueBranch);
			else {
				env2 = env;
				ifn.setSuccessors(trueBranch, falseBranch);
			}

			addNodeToBlock(env2, arg1Block, ifn, arg1Val, false);

			BasicBlock falseBlock = translateNode(env2.copyAndGiveUniquePnodes(arg1Val), falseBranch, sn.getLastChild(), resultVar, false);

			return newJoinBasicBlock(env, trueBranch, falseBlock);
		}
		case Token.COMMA: {
			Node lastChild = sn.getLastChild();
			BasicBlock currBB = bb;
			for (Node n : sn.children())
				currBB = translateNode(env, currBB, n, n == lastChild ? resultVar : AbstractNode.NO_VALUE, false);
			return currBB;
		}
		case Token.VOID: {
			BasicBlock voidBB = translateNode(env, bb, sn.getFirstChild(), AbstractNode.NO_VALUE, false);
			addNodeToBlock(voidBB, ConstantNode.makeUndefined(resultVar, srcLoc), statement);
			return voidBB;
		}
		case Token.SWITCH: {
			Node firstChild = sn.getFirstChild();
			Node defaultNode = null, lastChild = null;
			int condVal = nextTemporary(env);
			BasicBlock currBB = translateNode(env, bb, firstChild, condVal, false);
			BasicBlock trueBranch = null, falseBranch = null, oldTrueBranch;
			int caseVal = sn.hasMoreThanOneChild() ? nextTemporary(env) : AbstractNode.NO_VALUE;
			int compVal = sn.hasMoreThanOneChild() ? nextTemporary(env) : AbstractNode.NO_VALUE;
			BasicBlock retBlock = newStandaloneBasicBlock(env, currBB);
			FlowGraphEnv env2 = env.specialCopy(currBB, retBlock);
			for (Node child : sn.children()) {
				if (child == firstChild)
					continue;

				if (!child.isCase() && !child.isDefaultCase())
					throw new AnalysisException("Unknown child type of switch:" + child.toStringTree());

				oldTrueBranch = trueBranch;
				lastChild = child.getLastChild();

				// Nodes are not nicely ordered; we need to delay the translation of default until the end of the
				// switch statement.
				if (child.isDefaultCase()) {
					defaultNode = child.getLastChild();
					continue;
				}

				trueBranch = newBasicBlock(env2, currBB);
				falseBranch = newBasicBlock(env2, currBB);

				if (oldTrueBranch != null && !oldTrueBranch.getSuccessors().contains(retBlock))
					oldTrueBranch.addSuccessor(trueBranch);

				currBB = translateNode(env2, currBB, child.getFirstChild(), caseVal, false);
				currBB.addNode(new BinaryOperatorNode(BinaryOperatorNode.Op.EQ, condVal, caseVal, compVal, srcLoc));
				IfNode ifn = new IfNode(compVal, srcLoc);
				currBB.addNode(ifn);
				ifn.setSuccessors(trueBranch, falseBranch);

				trueBranch = translateNode(env2, trueBranch, child.getLastChild(), resultVar, false);
				currBB = falseBranch;
			}
			if (trueBranch != null && defaultNode != null && lastChild != defaultNode)
				trueBranch.addSuccessor(retBlock);

			if (defaultNode != null) {
				BasicBlock defaultBlock = newBasicBlock(env2, currBB);
				if (falseBranch != null && !falseBranch.getSuccessors().contains(retBlock))
					falseBranch.addSuccessor(defaultBlock);
				if (trueBranch != null && !trueBranch.getSuccessors().contains(retBlock))
					trueBranch.addSuccessor(defaultBlock);
				currBB = translateNode(env2, defaultBlock, defaultNode, resultVar, false);
			} else {
				if (trueBranch != null && !trueBranch.getSuccessors().contains(retBlock))
					trueBranch.addSuccessor(currBB);
			}
			currBB.addSuccessor(retBlock);
			return retBlock;
		}
		case Token.CASE: {
			throw new AnalysisException("Top level case detected, weird.");
		}
		case Token.REGEXP: { // /[regexp]/[flags]
			// The parameters are interpreted as strings, so no need to traverse the children.
			// Translate the construction into new RegExp(regexp,flags).
			List<Integer> args = newList();
			int reVal = nextTemporary(env), expVal = nextTemporary(env, args);
			int flagVal = sn.getChildCount() > 1 ? nextTemporary(env, args) : AbstractNode.NO_VALUE;
			bb.addNode(new ReadVariableNode("RegExp", reVal, AbstractNode.NO_VALUE, srcLoc));
			bb.addNode(ConstantNode.makeString(sn.getFirstChild().getString(), expVal, srcLoc));
			if (flagVal != AbstractNode.NO_VALUE)
				bb.addNode(ConstantNode.makeString(sn.getLastChild().getString(), flagVal, srcLoc));
			BasicBlock callBlock = newBasicBlock(env, bb);
			addNodeToBlock(callBlock, new CallNode(true, false, resultVar, AbstractNode.NO_VALUE, reVal, args, srcLoc), statement);
			return newBasicBlock(env, callBlock);
		}
		case Token.FOR:
		case Token.WHILE: {
			BasicBlock currBB = bb, condBlock;
			Node condNode = snType == Token.FOR ? sn.getFirstChild().getNext() : sn.getFirstChild();
			int condVal = nextTemporary(env);

			// There are two kinds of for-nodes in the closure compiler AST. One is the regular
			// one and the second one is for (x in obj) {}, and the difference is the number of
			// children.

			// Special treatment of for-in loop, it's really a different kind of beast.
			if (sn.getChildCount() == 3)
				return translateForIn(env, bb, sn, resultVar, srcLoc);

			// Handle the regular for-loop initialization.
			if (snType == Token.FOR)
				currBB = translateNode(env, currBB, sn.getFirstChild(), AbstractNode.NO_VALUE, false);

			BasicBlock condBB = newBasicBlock(env, currBB);
			currBB = condBB;

			// Insert a synthetic node "true" in case the condition block in the for-loop is empty.
			if (snType == Token.FOR && condNode.isEmpty()) {
				ConstantNode bn = ConstantNode.makeBoolean(true, condVal, srcLoc);
				bn.setArtificial();
				condBB.addNode(bn);
				currBB = newBasicBlock(env, currBB);
			}

			condBlock = translateNode(env, currBB, condNode, condVal, false);

			BasicBlock trueBranch = newBasicBlock(env, condBlock);
			BasicBlock falseBranch = newBasicBlock(env, condBlock);

			IfNode ifn = new IfNode(condVal, srcLoc);
			condBlock.addNode(ifn);
			ifn.setSuccessors(trueBranch, falseBranch);

			FlowGraphEnv freshEnv = env.specialCopy(condBB, falseBranch).copyAndGiveUniquePnodes(AbstractNode.NO_VALUE);

			BasicBlock trueBlock = translateNode(freshEnv, trueBranch, sn.getLastChild(), resultVar, false);
			if (snType == Token.FOR) {
				trueBlock = translateNode(freshEnv, trueBlock, sn.getFirstChild().getNext().getNext(), AbstractNode.NO_VALUE, false);
				if (Options.isUnrollOneAndAHalfEnabled()) {
					BasicBlock tmpRet = newBasicBlock(freshEnv, trueBlock);
					FlowGraphEnv env3 = freshEnv.copyAndInitializeForCopy(tmpRet);
					// Insert a copy of the condition block with a quick bail out.
					trueBlock = translateNode(freshEnv, tmpRet, condNode, condVal, false);
					ifn = new IfNode(condVal, srcLoc);
					ifn.setSuccessors(trueBranch, falseBranch);
					trueBlock.addNode(ifn);
					trueBlock.addSuccessor(trueBranch);
					trueBlock.addSuccessor(falseBranch);
					setDuplicateBlocks(env3.copyBlocks, Collections.<BasicBlock> newList(), tmpRet, condBlock);
					return falseBranch;
				}
			}

			trueBlock.addSuccessor(condBB);
			return falseBranch;
		}
		case Token.DO: {
			BasicBlock entryBB = newBasicBlock(env, bb);

			int condVal = nextTemporary(env);
			BasicBlock falseBranch = newStandaloneBasicBlock(env, bb);
			FlowGraphEnv freshEnv = env.specialCopy(entryBB, falseBranch);

			BasicBlock trueBlock = translateNode(freshEnv, entryBB, sn.getFirstChild(), resultVar, false);

			BasicBlock condBB = translateNode(env, newBasicBlock(freshEnv, trueBlock), sn.getLastChild(), condVal, false);

			IfNode ifn = new IfNode(condVal, srcLoc);
			ifn.setSuccessors(entryBB, falseBranch);
			condBB.addNode(ifn);
			condBB.addSuccessor(entryBB);
			condBB.addSuccessor(falseBranch);

			return falseBranch;
		}
		default: {
			throw new AnalysisException("Unknown node type: " + Token.name(sn.getType()) + " at " + srcLoc + ":" + sn.toStringTree());
		}
		}
	}

	private static void addNodeToBlock(BasicBlock bb, AbstractNode node, boolean endOfStatement) {
		bb.addNode(node);
		if (endOfStatement)
			node.setRegistersDone(true);
	}

	private static void addNodeToBlock(FlowGraphEnv env, BasicBlock bb, AbstractNode node, int baseVar, boolean endOfStatement) {
		BasicBlock workingBB = bb;
		workingBB.addNode(node);
		AbstractNode prevNode = env.propertyNodes.get(baseVar);
		AbstractNode lastNode = node;

		// Figure out if we need to add this node to our property node map, and how to index it.
		Integer mapReg = AbstractNode.NO_VALUE;
		if (node instanceof WritePropertyNode)
			mapReg = ((WritePropertyNode) node).getBaseRegister();
		else if (node instanceof ReadVariableNode || node instanceof ReadPropertyNode)
			mapReg = ((LoadNode) node).getResultRegister();
		if (mapReg != AbstractNode.NO_VALUE)
			env.propertyNodes.put(mapReg, node);

		if (prevNode != null) {
			AbstractNode prevPrevNode = null;
			
			// We have a case for placing an assume node. Figure out what to place it.
			if (node instanceof IfNode)
				workingBB = ((IfNode) node).getSuccTrue();

			if (prevNode instanceof ReadVariableNode && node instanceof IPropertyNode) {
				if (!(((ReadVariableNode) prevNode).getVariableName().equals("this"))) {
					workingBB.addNode(AssumeNode.makeVariableNonNullUndef(((ReadVariableNode) prevNode).getVariableName(), node.getSourceLocation()));
					lastNode = workingBB.getLastNode();
				}
			} else if (prevNode instanceof ReadPropertyNode && (node instanceof IPropertyNode || node instanceof IfNode)) {
				prevPrevNode = prevNode; // env.propertyNodes.get(((IPropertyNode) prevNode).getBaseRegister());
			}

			if (prevPrevNode != null) {
				workingBB.addNode(AssumeNode.makePropertyNonNullUndef(prevNode));
				lastNode = workingBB.getLastNode();
			}
		}
		
		if (endOfStatement)
			lastNode.setRegistersDone(true);
	}

	/**
	 * Dynamically splits a basic block above node n and inserts the code.
	 */
	public int extendFlowgraphWithCallAtNode(FlowGraph fg, AbstractNode n, int baseReg) {
		boolean add = false;
		BasicBlock bb = n.getBlock();
		SourceLocation srcLoc = bb.getSourceLocation();
		Function f = bb.getFunction();
		maxTmp = f.getMaxRegister();
		int resultReg = maxTmp++;
		// XXX: Ponder copyOf.
		List<AbstractNode> newnodes = newList();
		BasicBlock lowerBlock = new BasicBlock(f);
		BasicBlock callBlock1 = new BasicBlock(f);
		callBlock1.addNode(new CallConversionNode(CallConversionNode.Kind.VALUEOF, CallConversionNode.Sequence.FIRST, baseReg, resultReg, srcLoc));
		BasicBlock callBlock2 = new BasicBlock(f);
		callBlock2.addNode(new CallConversionNode(CallConversionNode.Kind.VALUEOF, CallConversionNode.Sequence.SECOND, baseReg, resultReg, srcLoc));
		Collection<BasicBlock> succs = bb.getSuccessors();
		List<AbstractNode> remainingNodes = newList();
		// Split the nodes in bb into an upper half (reusing bb as block) and a lower half that uses lowerBlock as block.
		for (AbstractNode node : bb.getNodes()) {
			if (node == n)
				add = true;
			else if (!add)
				newnodes.add(node);
			if (add)
				remainingNodes.add(node);
		}
		bb.replaceNodes(newnodes); // Avoid concurrent modification exception.
		if (remainingNodes.isEmpty())
			remainingNodes.add(new NopNode(srcLoc));
		for (AbstractNode node : remainingNodes)
			lowerBlock.addNode(node);
		// Rewire successors so that bb points to lowerBlock and the successors of lowerBlock are the former successors of bb.
		for (BasicBlock b1 : succs) {
			bb.removeSuccessor(b1);
			lowerBlock.addSuccessor(b1);
		}
		bb.addSuccessor(callBlock1);
		callBlock1.addSuccessor(callBlock2);
		callBlock2.addSuccessor(lowerBlock);
		// Make sure to update book-keeping things.
		f.setMaxRegister(maxTmp);
		fg.addBlock(callBlock1);
		fg.addBlock(callBlock2);
		fg.addBlock(lowerBlock);
		// TODO: Fix flow graph fragments deletion.
		fg.complete();

		if (Options.isTestFlowGraphBuilderEnabled())
			System.out.println("fg2: " + fg.toString());
		return resultReg;
	}

	/**
	 * Dynamically adds a piece of code to the flow graph at the call node n and returns the entry block for the code.
	 */
	public FlowGraphFragment extendFlowgraph(FlowGraph fg, String sourceString, String normalizedSourceString, CallNode n, FlowGraphFragment previousExt, String varName) {
		SourceLocation s = n.getSourceLocation();
		BasicBlock callBlock = n.getBlock();
		Function fun = callBlock.getFunction();
		int resultRegister = n.getResultRegister();

		FlowGraphEnv env = new FlowGraphEnv(fg, new LinkedList<List<BasicBlock>>(), newBBList(), fun, Collections.<String, Integer> newMap());

		BasicBlock bb = setupEnv(callBlock, env, previousExt);
		env.evalResultMap = varName == null ? null : Pair.make(varName, resultRegister);

		Node root = parse(sourceString, s.getFileName());
		int resultVar = nextTemporary(env);
		// Insert a dummy node to prevent empty basic blocks. We need a constant node to get uniform treatment
		// with other "functions".
		env.declsBB.addNode(ConstantNode.makeUndefined(resultVar, env.fun.getSourceLocation()));

		BasicBlock retBlock = translateChildren(env, bb, root.getLastChild(), resultVar);

		return postTranslationFixup(env, normalizedSourceString, retBlock);
	}

	private BasicBlock setupEnv(BasicBlock callBlock, FlowGraphEnv env, FlowGraphFragment previousExt) {
		nukeExtension(env.fg, env.fun, previousExt);
		env.declsBB = newStandaloneBasicBlock(env, callBlock);
		env.retBB = callBlock.getSingleSuccessor();
		env.panicBB = callBlock.getExceptionHandler();
		// Start allocating registers one more than the previous max.
		maxTmp = env.fun.getMaxRegister() + 1;
		env.nextTmp = maxTmp;
		return env.declsBB;
	}

	/**
	 * Dynamically adds a piece of code to the flow graph at the call node n and returns the entry block for the code.
	 */
	public FlowGraphFragment extendFlowGraphWithEventHandler(FlowGraph fg, String sourceString, FlowGraphFragment previousExt, LoadNode n) {
		BasicBlock callBlock = n.getBlock();
		Function fun = n.getBlock().getFunction();
		FlowGraphEnv env = new FlowGraphEnv(fg, new LinkedList<List<BasicBlock>>(), newBBList(), fun, Collections.<String, Integer> newMap());

		BasicBlock bb = setupEnv(callBlock, env, previousExt);

		int resultVar = n.getResultRegister() == AbstractNode.NO_VALUE ? nextTemporary(env) : n.getResultRegister();
		// Insert a dummy node to prevent empty basic blocks. We need a constant node to get uniform treatment
		// with other "functions".
		env.declsBB.addNode(ConstantNode.makeUndefined(resultVar, env.fun.getSourceLocation()));

		// XXX: Should we add the event variable as argument here as well?
		translateEventHandler(env, bb, new JavaScriptSource(fun.getSourceLocation().getFileName(), "TIMEOUT", sourceString, 0), resultVar);

		return postTranslationFixup(env, sourceString, bb);
	}

	private FlowGraphFragment postTranslationFixup(FlowGraphEnv env, String sourceString, BasicBlock retBlock) {
		Collection<Function> fs = newSet();
		Function firstFun = env.pendingFuns.isEmpty() ? null : env.pendingFuns.get(0).get(0).getFunction();
		for (List<BasicBlock> funBBs : env.pendingFuns)
			fs.add(funBBs.get(0).getFunction());

		Collection<BasicBlock> bbs = newSet();
		bbs.addAll(env.pendingBBs);

		Collection<AbstractNode> ns = newSet();
		// Dig out the newly inserted declare function/declare variable nodes from the declaration block.
		for (AbstractNode nss : env.declsBB.getNodes()) {
			// TODO: Dig through fun.getEntry() rather than env.declsBB. Does not matter which with the current code.
			if (nss instanceof DeclareFunctionNode) {
				if (fs.contains(((DeclareFunctionNode) nss).getFunction()))
					; // ns.add(nss);
			}
			if (nss instanceof DeclareVariableNode) {
				; // Similar to above.
			}
		}

		retBlock.addSuccessor(env.retBB);
		env.fun.setMaxRegister(maxTmp);

		for (List<BasicBlock> funBBs : env.pendingFuns)
			env.pendingBBs.addAll(funBBs);

		Map<BasicBlock, Integer> pointedTo = newMap();
		increaseOrAdd(pointedTo, env.declsBB);
		removeEmptyBlocks(env, pointedTo);

		for (BasicBlock pending : env.pendingBBs)
			if ((pointedTo.containsKey(pending) && (pointedTo.get(pending) > 0)) || !pending.isEmpty())
				env.fg.addBlock(pending);

		env.fg.complete();

		if (Options.isTestFlowGraphBuilderEnabled())
			System.out.println("fg2: " + env.fg.toString());

		return new FlowGraphFragment(sourceString, env.declsBB, firstFun, fs, bbs, ns);
	}

	/**
	 * Removes a flow graph extension from a flow graph.
	 */
	private static void nukeExtension(FlowGraph fg, Function fun, FlowGraphFragment e) {
		if (e == null)
			return;

		fg.removeFunctions(e.getFunction());
		fun.removeBlocks(e.getBlocks());
		fun.getEntry().removeNodes(e.getNodes());
	}

	private static boolean requiresNewBlock(AbstractNode n) {
		return (n instanceof ThrowNode
				|| n instanceof ICallNode
				|| n instanceof ExceptionalReturnNode
				|| n instanceof ReturnNode
				|| n instanceof EnterWithNode
				|| n instanceof LeaveWithNode);
	}

	/**
	 * This function translates the for (x in obj) for, which is smashed into the same token type as regular for
	 * in Rhino. The parse trees look different though.
	 */
	private BasicBlock translateForIn(FlowGraphEnv env, BasicBlock bb, Node sn, int resultVar, SourceLocation srcLoc) {
		int baseVal = nextTemporary(env), propListVal = nextTemporary(env), condVal = nextTemporary(env);
		int propertyVal = nextTemporary(env);
		Node firstChild = sn.getFirstChild();
		BasicBlock currBB = bb;
		String loopVar;
		if (firstChild.isName())
			loopVar = firstChild.getString();
		else {
			loopVar = firstChild.getFirstChild().getString(); // FIXME: this doesn't work for general LeftHandSideExpression, e.g. 'for (a.b.c[7] in x)'
			currBB = translateNode(env, currBB, firstChild, AbstractNode.NO_VALUE, false);
		}

		currBB = translateNode(env, currBB, firstChild.getNext(), baseVal, false);
		currBB = newBasicBlock(env, currBB);
		BeginForInNode bf = new BeginForInNode(baseVal, propListVal, srcLoc);
		currBB.addNode(bf);

		BasicBlock condBlock = newBasicBlock(env, currBB);

		condBlock.addNode(new HasNextPropertyNode(propListVal, condVal, srcLoc));

		BasicBlock trueBranch = newBasicBlock(env, condBlock);
		BasicBlock falseBranch = newBasicBlock(env, condBlock);
		BasicBlock trueEndBlock = newStandaloneBasicBlock(env, condBlock);

		IfNode ifn = new IfNode(condVal, srcLoc);
		condBlock.addNode(ifn);
		ifn.setSuccessors(trueBranch, falseBranch);
		ifn.setArtificial();

		trueBranch.addNode(new NextPropertyNode(propListVal, propertyVal, srcLoc));
		trueBranch.addNode(new WriteVariableNode(propertyVal, loopVar, srcLoc));
		FlowGraphEnv env2 = env.specialCopy(trueEndBlock, falseBranch).copyAndGiveUniquePnodes(AbstractNode.NO_VALUE);
		BasicBlock trueBlock = translateNode(env2, trueBranch, sn.getLastChild(), resultVar, false);

		trueBlock.addSuccessor(trueEndBlock);
		bumpToFrontOfPending(env, trueEndBlock);
		bumpToFrontOfPending(env, falseBranch);
		trueEndBlock.addNode(new EndForInNode(bf, srcLoc));
		trueEndBlock.addSuccessor(condBlock);

		falseBranch.setExceptionHandler(bb.getExceptionHandler());

		return falseBranch;
	}

	/**
	 * Creates a new basic block that joins trueBlock and falseBlock; typically used for if and similar constructs.
	 */
	private static BasicBlock newJoinBasicBlock(FlowGraphEnv env, BasicBlock trueBlock, BasicBlock falseBlock) {
		BasicBlock joinBlock = newStandaloneBasicBlock(env, trueBlock);

		if (!hasSpecialSuccessors(env, trueBlock))
			trueBlock.addSuccessor(joinBlock);
		if (!hasSpecialSuccessors(env, falseBlock))
			falseBlock.addSuccessor(joinBlock);
		return joinBlock;
	}

	/**
	 * Helper function that iterates over the successors of the block and see if they are the continue or break
	 * blocks of env.
	 */
	private static boolean hasSpecialSuccessors(FlowGraphEnv env, BasicBlock bb) {
		for (BasicBlock b : bb.getSuccessors())
			if (b == env.continueBlock || b == env.breakBlock)
				return true;
		return false;
	}

	/**
	 * Traverses the children of the blocks and marks the duplicates.
	 */
	private void setDuplicateBlocks(List<BasicBlock> copyBlocks, List<BasicBlock> seenBlocks, BasicBlock copyBlock, BasicBlock origBlock) {
		if (!copyBlocks.contains(copyBlock) || seenBlocks.contains(copyBlock))
			return;
		setDuplicateNodes(copyBlock, origBlock);
		seenBlocks.add(copyBlock);
		Iterator<BasicBlock> oi = origBlock.getSuccessors().iterator();
		Iterator<BasicBlock> ci = copyBlock.getSuccessors().iterator();
		while (ci.hasNext()) {
			setDuplicateBlocks(copyBlocks, seenBlocks, ci.next(), oi.next());
		}
	}

	/**
	 * Traverses the nodes of the blocks and marks the duplicates.
	 */
	private static void setDuplicateNodes(BasicBlock copyBlock, BasicBlock origBlock) {
		Iterator<AbstractNode> ci = copyBlock.getNodes().iterator();
		Iterator<AbstractNode> oi = origBlock.getNodes().iterator();
		while (oi.hasNext()) {
			AbstractNode on = oi.next();
			if (!ci.hasNext())
				break;
			AbstractNode cn = ci.next();
			if (!on.getSourceLocation().equals(cn.getSourceLocation()) && oi.hasNext())
				on = oi.next();
			cn.setDuplicateOf(on);
		}

	}

	/**
	 * Create a new source location.
	 */
	private static SourceLocation newSourceLocation(int line, int column, String filename) {
		// The test suite does plain text string comparisons of the output. Since
		// the separator is different between Windows and Unix that will cause
		// test failures; normalize the output so that it is always "/" to
		// avoid this problem.
		String normalized_filename = filename.replace("\\", "/");
		// The closure compiler uses 0-indexed column numbers and this seems unlikely to change in the nearby future.
		// Add one to all column numbers so that the user gets the left-adjusted syntactic construct starting
		// in column one.
		return new SourceLocation(line, column + 1, normalized_filename);
	}

	/**
	 * Create a new basic block that becomes a successor to the incoming basic block.
	 */
	private static BasicBlock newBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = newStandaloneBasicBlock(env, bb);
		bb.addSuccessor(bb2);
		if (env.copyBlocks != null)
			env.copyBlocks.add(bb2);
		return bb2;
	}

	/**
	 * Create a new basic block that becomes the exception handler for the incoming basic block.
	 */
	private static BasicBlock newCatchBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = newStandaloneBasicBlock(env, bb);
		bb.setExceptionHandler(bb2);
		if (env.copyBlocks != null)
			env.copyBlocks.add(bb2);
		return bb2;
	}

	/**
	 * Create a stand alone basic block.
	 */
	private static BasicBlock newStandaloneBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = new BasicBlock(env.fun);
		bb2.setExceptionHandler(bb.getExceptionHandler());
		env.pendingBBs.add(bb2);
		if (env.copyBlocks != null)
			env.copyBlocks.add(bb2);
		return bb2;
	}

	/**
	 * Allocate a new list of a particular type.
	 */
	private static List<BasicBlock> newBBList() {
		return newList();
	}

	/**
	 * Rhino binary operator -> TAJS flow graph operator.
	 */
	private static BinaryOperatorNode.Op getFlowGraphBinaryOp(int rhinoType) {
		switch (rhinoType) {
		case Token.ASSIGN_DIV:
		case Token.DIV:
			return BinaryOperatorNode.Op.DIV;
		case Token.ASSIGN_LSH:
		case Token.LSH:
			return BinaryOperatorNode.Op.SHL;
		case Token.ASSIGN_MOD:
		case Token.MOD:
			return BinaryOperatorNode.Op.REM;
		case Token.ASSIGN_MUL:
		case Token.MUL:
			return BinaryOperatorNode.Op.MUL;
		case Token.ASSIGN_RSH:
		case Token.RSH:
			return BinaryOperatorNode.Op.SHR;
		case Token.ASSIGN_SUB:
		case Token.SUB:
			return BinaryOperatorNode.Op.SUB;
		case Token.ASSIGN_URSH:
		case Token.URSH:
			return BinaryOperatorNode.Op.USHR;
		case Token.GE:
			return BinaryOperatorNode.Op.GE;
		case Token.GT:
			return BinaryOperatorNode.Op.GT;
		case Token.LE:
			return BinaryOperatorNode.Op.LE;
		case Token.LT:
			return BinaryOperatorNode.Op.LT;
		case Token.NE:
			return BinaryOperatorNode.Op.NE;
		case Token.SHNE:
			return BinaryOperatorNode.Op.SNE;
		case Token.ASSIGN_ADD:
		case Token.ADD:
			return BinaryOperatorNode.Op.ADD;
		case Token.SHEQ:
			return BinaryOperatorNode.Op.SEQ;
		case Token.EQ:
			return BinaryOperatorNode.Op.EQ;
		case Token.ASSIGN_BITOR:
		case Token.BITOR:
			return BinaryOperatorNode.Op.OR;
		case Token.ASSIGN_BITAND:
		case Token.BITAND:
			return BinaryOperatorNode.Op.AND;
		case Token.ASSIGN_BITXOR:
		case Token.BITXOR:
			return BinaryOperatorNode.Op.XOR;
		case Token.INSTANCEOF:
			return BinaryOperatorNode.Op.INSTANCEOF;
		case Token.IN:
			return BinaryOperatorNode.Op.IN;
		default:
			throw new AnalysisException("Unknown binary operator: " + Token.name(rhinoType));
		}
	}

	/**
	 * Rhino unary operator -> TAJS flow graph operator.
	 */
	private static UnaryOperatorNode.Op getFlowGraphUnaryOp(int rhinoType) {
		switch (rhinoType) {
		case Token.BITNOT:
			return UnaryOperatorNode.Op.COMPLEMENT;
		case Token.NOT:
			return UnaryOperatorNode.Op.NOT;
		case Token.POS:
			return UnaryOperatorNode.Op.PLUS;
		case Token.NEG:
			return UnaryOperatorNode.Op.MINUS;
		default:
			throw new AnalysisException("Unknown unary operator: " + Token.name(rhinoType));
		}
	}

	private int nextTemporary(FlowGraphEnv env) {
		maxTmp += env.nextTmp > maxTmp ? 1 : 0;
		return env.nextTmp++;
	}

	private int nextTemporary(FlowGraphEnv env, Collection<Integer> l) {
		int tmpVal = nextTemporary(env);
		l.add(tmpVal);
		return tmpVal;
	}

	/**
	 * Moves bb from wherever it is in the pending queue to the front of the queue.
	 */
	private static void bumpToFrontOfPending(FlowGraphEnv env, BasicBlock bb) {
		env.pendingBBs.remove(bb);
		env.pendingBBs.add(bb);
	}

	/**
	 * Helper function that does a lot of the work for setting up a function.
	 */
	private static BasicBlock setupFunction(FlowGraphEnv env, Function fun, SourceLocation srcLoc) {
		BasicBlock entryBB = new BasicBlock(fun);
		entryBB.addNode(ConstantNode.makeUndefined(AbstractNode.RETURN_REG, srcLoc));

		// Declarations basic block
		env.declsBB = entryBB; // TODO: could merge the declsBB and its successor after the function has been completed?
		env.pendingBBs.add(entryBB);
		if (env.copyBlocks != null)
			env.copyBlocks.add(entryBB);

		// Exceptional exit (singleton BB)
		BasicBlock panicBB = newCatchBasicBlock(env, entryBB);
		// We like to read our functions in a linear fashion, so remove the block from the pending queue and add it
		// in the caller of setupFunction instead.
		env.pendingBBs.remove(panicBB);
		env.panicBB = panicBB;
		panicBB.addNode(new ExceptionalReturnNode(srcLoc));

		BasicBlock bb = newBasicBlock(env, entryBB);

		// Normal exit node (in a singleton BB)
		BasicBlock exitBB = newStandaloneBasicBlock(env, bb);
		// We like to read our functions from top to bottom, so add the exit block second to last in the caller instead.
		env.pendingBBs.remove(exitBB);
		env.retBB = exitBB;
		exitBB.addNode(new ReturnNode(AbstractNode.RETURN_REG, srcLoc));

		fun.setEntry(entryBB);
		fun.setExceptionalExit(panicBB);
		fun.setOrdinaryExit(exitBB);
		env.fg.addFunction(fun);
		return bb;
	}
}

/**
 * The environment of the flow graph builder which is internal to the module.
 * Flows *downwards*. It's really just a record, so all the fields are
 * public. That does not mean you should mutate the fields though, copy them!
 */
class FlowGraphEnv { // TODO: javadoc for the FlowGraphEnv fields

	public FlowGraph fg;
	public List<List<BasicBlock>> pendingFuns;
	public List<BasicBlock> pendingBBs;
	public Map<Integer, AbstractNode> propertyNodes;
	public Function fun;
	public BasicBlock breakBlock;
	public BasicBlock continueBlock;
	public BasicBlock declsBB; // variable/function declarations are added here
	public BasicBlock retBB;
	public BasicBlock panicBB;
	public List<BasicBlock> copyBlocks;
	public int nextTmp;
	public Pair<String, Integer> evalResultMap;
	public int baseVal;
	public Map<String, Integer> lineOffsets;

	public FlowGraphEnv(FlowGraph fg, List<List<BasicBlock>> pendingFuns, List<BasicBlock> pendingBBs, Function fun, Map<String, Integer> lineOffsets) {
		this.fg = fg;
		this.pendingFuns = pendingFuns;
		this.pendingBBs = pendingBBs;
		this.propertyNodes = newMap();
		this.fun = fun;
		this.breakBlock = null;
		this.continueBlock = null;
		this.declsBB = null;
		this.retBB = null;
		this.panicBB = null;
		this.copyBlocks = null;
		this.nextTmp = AbstractNode.FIRST_ORDINARY_REG;
		this.evalResultMap = null;
		this.baseVal = AbstractNode.NO_VALUE;
		this.lineOffsets = lineOffsets;
	}

	private FlowGraphEnv(FlowGraph fg, List<List<BasicBlock>> pendingFuns, List<BasicBlock> pendingBBs, Map<Integer, AbstractNode> propertyNodes, Function fun,
			BasicBlock breakBlock, BasicBlock continueBlock, BasicBlock declsBB, BasicBlock retBB, BasicBlock panicBB, List<BasicBlock> copyBlocks,
			int nextTmp, int baseVal, Map<String, Integer> lineOffsets, Pair<String, Integer> evalResultMap) {
		this(fg, pendingFuns, pendingBBs, fun, lineOffsets);
		this.propertyNodes = propertyNodes;
		this.breakBlock = breakBlock;
		this.continueBlock = continueBlock;
		this.declsBB = declsBB;
		this.retBB = retBB;
		this.panicBB = panicBB;
		this.copyBlocks = copyBlocks;
		this.nextTmp = nextTmp;
		this.baseVal = baseVal;
		this.evalResultMap = evalResultMap;
	}

	public FlowGraphEnv specialCopy(BasicBlock contBlock, BasicBlock escBlock) {
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, fun, escBlock, contBlock, declsBB, retBB, panicBB, copyBlocks, nextTmp, baseVal, lineOffsets, evalResultMap);
	}

	public FlowGraphEnv copyAndUpdateBaseObj(int newBaseVal) {
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, fun, breakBlock, continueBlock, declsBB, retBB, panicBB, copyBlocks, nextTmp, newBaseVal, lineOffsets, evalResultMap);
	}

	public FlowGraphEnv copyAndGiveUniquePnodes(int disqualifiedNode) {
		Map<Integer, AbstractNode> pnodes = newMap(propertyNodes);
		if (disqualifiedNode != AbstractNode.NO_VALUE)
			pnodes.remove(disqualifiedNode);
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, pnodes, fun, breakBlock, continueBlock, declsBB, retBB, panicBB, copyBlocks, nextTmp, baseVal, lineOffsets, evalResultMap);

	}

	public FlowGraphEnv copyAndInitializeForCopy(BasicBlock initBlock) {
		List<BasicBlock> newCopyBlocks = newList();
		newCopyBlocks.add(initBlock);
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, fun, breakBlock, continueBlock, declsBB, retBB, panicBB, newCopyBlocks, nextTmp, baseVal, lineOffsets, evalResultMap);
	}
}
