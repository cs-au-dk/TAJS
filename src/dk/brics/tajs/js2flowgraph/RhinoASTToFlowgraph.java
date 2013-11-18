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

package dk.brics.tajs.js2flowgraph;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
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
import dk.brics.tajs.flowgraph.jsnodes.BeginWithNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventEntryNode;
import dk.brics.tajs.flowgraph.jsnodes.ExceptionalReturnNode;
import dk.brics.tajs.flowgraph.jsnodes.HasNextPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.EndWithNode;
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
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.EmbeddedJavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.EventHandlerJavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.ExternalJavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.JavaScriptSourceVisitor;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;

public class RhinoASTToFlowgraph { // TODO: improve javadoc

    public enum Directive { // spelling is significant, it is used for matching with the source code directives
        UNREACHABLE("TAJS_unreachable");

        private final String name;

        private Directive(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    }

	private static Logger logger = Logger.getLogger(RhinoASTToFlowgraph.class);

	/**
	 * When multiple JavaScript sources from HTML are parsed, their host filenames need to be modified in order to distinguish them.
	 * The divider string is used for encoding the offset into the filename of the parsed nodes in {@link JSSourceFile} objects.
	 * @see #getRealLineNumber
	 */
	private static class EncodedFileName {

		private final String fileName;

		private final int offset;

		private static final String TAJS_DIVIDER = "<TAJS_DIVIDER>";

		public EncodedFileName(String fileName) {
			this.fileName = fileName;
			this.offset = 0;
		}

		public EncodedFileName(String fileName, int offset) {
			this.fileName = fileName;
			this.offset = offset;
		}

		@Override
		public String toString() {
			return offset + TAJS_DIVIDER + fileName;
		}
		
		/**
		 * Gets the line number in the file containing the given Rhino node.
		 */
		public static int getRealLineNumber(Node n) {
			String fname = n.getSourceFileName();
			int dividerIndex = fname.indexOf(TAJS_DIVIDER);
			if (dividerIndex == -1) {
				throw new IllegalStateException("No TAJS_DIVIDER in sourceFileName: " + fname + " for " + n.toString());
			}
			final int offset = Integer.parseInt(fname.substring(0, dividerIndex));
			return n.getLineno() + offset;
		}

		/**
		 * Gets the name of the file containing the given Rhino node.
		 */
		public static String getDecodedFileName(Node n) {
			String fname = n.getSourceFileName();
			int dividerIndex = fname.indexOf(TAJS_DIVIDER);
			if (dividerIndex == -1) {
				throw new IllegalStateException("No TAJS_DIVIDER in sourceFileName: " + fname + " for " + n.toString());
			}
			return fname.substring(dividerIndex + TAJS_DIVIDER.length());
		}
	}

	/**
	 * The flow graph being constructed.
	 */
	private final FlowGraph graph = new FlowGraph();

	/**
	 * The environment for the compiler; always the same once it is initialized.
	 */
	private final CompilerOptions compilerOptions = new CompilerOptions();

	/**
	 * The maximum register number in use for the current function.
	 */
	private int maxReg = AbstractNode.FIRST_ORDINARY_REG;

	/**
	 * Event handlers.
	 */
	private Collection<EventHandlerJavaScriptSource> eventHandlers = newSet();

	/**
	 * Counter to ensure unique file names for dummy nodes.
	 */
	private int dummyCount = 0;
	
	private SourceLocation lastSrcLoc;

	/**
	 * Constructs a new translator from a Rhino AST to a TAJS flow graph.
	 */
	public RhinoASTToFlowgraph() {
		compilerOptions.setIdeMode(false);
		CompilationLevel.WHITESPACE_ONLY.setOptionsForCompilationLevel(compilerOptions);
	}

	/**
	 * Builds a flow graph for a list of source files.
	 * @throws IOException if unable to read the files
	 */
	public void buildFromJavaScriptFiles(List<String> files) throws IOException {
		buildTopLevel(parseJavaScriptFiles(files));
	}

	/**
	 * Builds a flow graph for a list of sources gathered from an HTML file.
	 */
	public void buildFromHTMLFile(List<JavaScriptSource> scriptList, List<EventHandlerJavaScriptSource> eventList) { // FIXME: should have stripped "javascript:" from the source code before calling this method
		eventHandlers.addAll(eventList);
		if (scriptList.isEmpty()) {
			// TODO: no script elements, handle this elsewhere?
			buildTopLevel(parse("", "dummy" + dummyCount++ + ".js", 0));
			return;
		}
		buildTopLevel(parseSources(scriptList));
	}

	/**
	 * Finishes the flow graph being constructed by this builder and returns the
	 * flow graph. Call this method when all JS code has been parsed.
	 */
	public FlowGraph close() { // TODO: move this to the end of buildTopLevel?
		FlowGraph fg = graph.complete();
		if (Options.isTestFlowGraphBuilderEnabled())
			System.out.println("fg2: " + fg.toString());
		fg.check();
		return fg;
	}

	/**
	 * Adds an event loop to the flow graph. Does not make sense if DOM is not enabled.
	 */
	private BasicBlock addEventLoop(FlowGraphEnv env, BasicBlock bb, String filename) {
		if (!Options.isDOMEnabled())
			return bb;

		BasicBlock currBB = newSuccessorBasicBlock(env, bb);

		int eventReg = nextRegister(env);
		for (EventHandlerJavaScriptSource src : eventHandlers) {
			Function f = translateEventHandler(env, currBB, src, eventReg);
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

		SourceLocation srcLoc = SourceLocationMaker.makeUnknown(filename);
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
		BasicBlock entryBB = newSuccessorBasicBlock(env, currBB);
		entryBB.addNode(new EventEntryNode(srcLoc));

		// Load Event Handlers
		BasicBlock loadBB = newSuccessorBasicBlock(env, entryBB);
		loadBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.LOAD, srcLoc));

		BasicBlock nopPostLoad = newSuccessorBasicBlock(env, loadBB);
		NopNode nopPostNode = new NopNode(srcLoc);
		nopPostNode.setArtificial();
		nopPostLoad.addNode(nopPostNode);
		nopPostLoad.addSuccessor(loadBB);

		// Other Event Handlers
		BasicBlock otherBB = newSuccessorBasicBlock(env, nopPostLoad);
		otherBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.OTHER, srcLoc));

		BasicBlock nopPostOther = newSuccessorBasicBlock(env, otherBB);
		AbstractNode nopPostOtherNode = new NopNode(srcLoc);
		nopPostOtherNode.setArtificial();
		nopPostOther.addNode(nopPostOtherNode);
		nopPostOther.addSuccessor(otherBB);

		// Unload Event Handlers
		BasicBlock unloadBB = newSuccessorBasicBlock(env, nopPostOther);
		unloadBB.addNode(new EventDispatcherNode(EventDispatcherNode.Type.UNLOAD, srcLoc));

		BasicBlock nopPostUnload = newSuccessorBasicBlock(env, unloadBB);
		AbstractNode nopPostUnloadNode = new NopNode(srcLoc);
		nopPostUnloadNode.setArtificial();
		nopPostUnload.addNode(nopPostUnloadNode );
		nopPostUnload.addSuccessor(unloadBB);

		entryBB.addSuccessor(nopPostLoad);
		nopPostLoad.addSuccessor(nopPostOther);
		nopPostOther.addSuccessor(nopPostUnload);

		return nopPostUnload;
	}

	/**
	 * Encapsulates the construction of SourceLocations.
	 */
	public static class SourceLocationMaker {
		
		public static SourceLocation makeUnknown(String filename) {
			return make(0, 0, filename);
		}

		public static SourceLocation makeFromNode(Node node) {
			int line = EncodedFileName.getRealLineNumber(node);
			int column = node.getCharno();
			String filename = EncodedFileName.getDecodedFileName(node);
			// The closure compiler uses 0-indexed column numbers and this seems unlikely to change in the nearby future.
			// Add one to all column numbers so that the user gets the left-adjusted syntactic construct starting in column one.
			return make(line, column + 1, filename);
		}

		private static SourceLocation make(int line, int column, String filename) {
			// The test suite does plain text string comparisons of the output. Since
			// the separator is different between Windows and Unix that will cause
			// test failures; normalize the output so that it is always "/" to
			// avoid this problem.
			return new SourceLocation(line, column, filename.replace("\\", "/"));
		}
	}

	private Function translateEventHandler(FlowGraphEnv env, BasicBlock bb, EventHandlerJavaScriptSource src, int resultReg) {
		// TODO: Should the function be added to env.declsBB?
		String params = src.getEventName().equals("TIMEOUT") ? "" : "event";
		String wrappedJS = "(function (" + params + ") {" + src.getJavaScript() + "})";
		final int lineNumberOffset = src.getLineNumberOffset();
		final String fileName = src.getFileName();
		Node evRoot = parse(wrappedJS, fileName, lineNumberOffset).getLastChild().getLastChild().getLastChild();
		SourceLocation srcLoc = SourceLocationMaker.makeFromNode(evRoot);
		lastSrcLoc = srcLoc;
		return translateFunction(env, bb, evRoot, srcLoc, resultReg);
	}

	/**
	 * Runs the Rhino parser on the given JavaScript snippets.
	 * @return Rhino AST node (last child of the root)
	 */
	private Node parseSources(List<JavaScriptSource> sources) {
		Compiler compiler = new Compiler();
		List<JSSourceFile> fs = new ArrayList<>();
		for (JavaScriptSource ss : sources) {
			Integer offset = ss.apply(new JavaScriptSourceVisitor<Integer>() {
				@Override
				public Integer visit(EmbeddedJavaScriptSource s) {
					return s.getLineNumberOffset();
				}

				@Override
				public Integer visit(EventHandlerJavaScriptSource s) {
					return s.getLineNumberOffset();
				}

				@Override
				public Integer visit(ExternalJavaScriptSource s) {
					return 0;
				}
			});
			fs.add(fromCode(ss.getJavaScript(), new EncodedFileName(ss.getFileName(), offset)));
		}
		// FIXME: externs vs modules as parameters??
		compiler.compile(newList(Collections.singleton(fromEmptyCode())), fs, compilerOptions);
		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error in file:");
		return getParentOfFirstInterestingNode(compiler);
	}

	/**
	 * Runs the Rhino parser on the given JavaScript snippet.
	 * @return Rhino AST node (last child of the root)
	 */
	private Node parse(String sourceString, String sourceFile, int lineNumberOffset) {
		Compiler compiler = new Compiler();
		compiler.compile(fromEmptyCode(), fromCode(sourceString, new EncodedFileName(sourceFile, lineNumberOffset)), compilerOptions);
		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error on code: " + sourceString);
		return getParentOfFirstInterestingNode(compiler);
	}

	/**
	 * Runs the Rhino parser on the given JavaScript snippets.
	 * @return Rhino AST node (last child of the root)
	 */
	private Node parseJavaScriptFiles(List<String> sourceFiles) throws IOException {
		Compiler compiler = new Compiler();
		JSSourceFile[] fs = new JSSourceFile[sourceFiles.size()];
		for (int i = 0; i < fs.length; i++) {
			final File file;
			final String sourceFileName = sourceFiles.get(i);
			if ("-".equals(sourceFileName)) { // Read from stdin
				try {
					file = File.createTempFile("TAJS_<stdin>", ".js");
					try (FileOutputStream out = new FileOutputStream(file)) {
						byte[] buffer = new byte[1024];
						int len;
						while ((len = System.in.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
					}
				} catch (Exception e) {
					throw new AnalysisException("Unable to parse stdin");
				}
			} else {
				file = new File(sourceFileName);
			}
			fs[i] = fromFile(file);
		}
		compiler.compile(fromEmptyCode(), fs, compilerOptions);
		if (compiler.getErrorCount() > 0)
			throw new AnalysisException("Parse error in file: " + sourceFiles);
		return getParentOfFirstInterestingNode(compiler);
	}

	/**
	 * Constructs a JSSourceFile from a JavaScript file.
	 * @throws IOException if unable to read the file
	 */
	static JSSourceFile fromFile(File file) throws IOException {
		try (FileInputStream stream = new FileInputStream(file)) {
			try (FileChannel fc = stream.getChannel()) {
				MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
				final Charset charset = Charset.forName("UTF-8");
				return fromCode(charset.decode(bb).toString(), new EncodedFileName(file.getPath()));
			}
		}
	}

	/**
	 * Constructs a JSSourceFile from a JavaScript snippet.
	 */
	static JSSourceFile fromCode(String source, EncodedFileName fileName) {
		return JSSourceFile.fromCode(fileName.toString(), source);
	}

	/**
	 * Empty JavaScript code snippet; used by the Rhino parser.
	 */
	static JSSourceFile fromEmptyCode() { // TODO: why is this method needed??
		return fromCode("", new EncodedFileName("dummy.js"));
	}

	/**
	 * The tree we get from Rhino has a few layers of indirection. Get rid of a couple of them.
	 */
	private static Node getParentOfFirstInterestingNode(Compiler comp) {
		return comp.getRoot().getLastChild();
	}

	/**
	 * Builds the flow graph for the top level of a JavaScript file.
	 */
	private void buildTopLevel(Node root) {
		final Node lastChild = root.getLastChild();

		// The line information might be garbled and give us negative line numbers. 
		// If that happens the block order gets wrong, so pick a sub node to avoid crashing.
		// TODO: how are we guaranteed that the subnode has a better line number?
		final boolean garbledLineNumbers = EncodedFileName.getRealLineNumber(lastChild) < 0;
		Node subRoot = garbledLineNumbers ? lastChild.getFirstChild() : lastChild;
		if (EncodedFileName.getRealLineNumber(subRoot) < 0)
			throw new IllegalStateException("Line numbers too garbled at: " + subRoot + " --> " + EncodedFileName.getRealLineNumber(subRoot));
		SourceLocation srcLoc = SourceLocationMaker.makeFromNode(subRoot);
		lastSrcLoc = srcLoc;

		Function fun = new Function(null, null, null, srcLoc);

		FlowGraphEnv env = new FlowGraphEnv(graph, dk.brics.tajs.util.Collections.<List<BasicBlock>> newList(), 
				dk.brics.tajs.util.Collections.<BasicBlock> newList(), fun);

		BasicBlock bb = setupFunction(env, fun);
		BasicBlock retBlock = translateChildren(env, bb, root, AbstractNode.RETURN_REG);

		BasicBlock retBlockPostEvents = addEventLoop(env, retBlock, srcLoc.getFileName());
		retBlockPostEvents.addSuccessor(env.retBB);
		fun.setMaxRegister(maxReg);

		env.pendingBBs.add(env.retBB);
		env.pendingBBs.add(env.exceptionretBB);

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
	private BasicBlock translateChildren(FlowGraphEnv env, BasicBlock bb, Node root, int resultReg) {
		BasicBlock currBB = bb;
		for (Node n : root.children())
			currBB = translateNode(env, currBB, n, resultReg, true);
		return currBB;
	}

	/**
	 * Helper function that translates a function.
	 */
	private Function translateFunction(FlowGraphEnv env, BasicBlock bb, Node sn, SourceLocation srcLoc, int resultReg) {
		Node left = sn.getFirstChild();
		Node params = left.getNext();
		Node body = params.getNext();
		List<String> args = params.hasChildren() ? Collections.<String> newList() : java.util.Collections.<String> emptyList();

		// If we don't have a name we're a function expression. But we're also a function expression if
		// we're in a RHS position, so if our resultReg is different from the constant node in the entry block
		// that's it. BUT we can also be a target for the unevalizer, so that might also make us a function expression.
		boolean exprContext = (left.isName() && left.getString().isEmpty()) || !left.isName() ||
				!(((ConstantNode) env.declsBB.getFirstNode()).getResultRegister() == resultReg ||
				(env.evalResultMap != null && env.evalResultMap.getSecond() == resultReg));

		String name = left.isName() && !left.getString().isEmpty() ? left.getString() : null;

		for (Node c : params.children())
			args.add(c.getString());

		// Move f into FlowGraphEnv?
		Function f = new Function(name, args, env.fun, srcLoc);

		int oldMaxTmp = maxReg;
		maxReg = AbstractNode.FIRST_ORDINARY_REG;

		FlowGraphEnv envTmp = new FlowGraphEnv(env.fg, env.pendingFuns, 
				dk.brics.tajs.util.Collections.<BasicBlock> newList(), f);
		int newWithScope = exprContext && name != null ? nextRegister(envTmp) : AbstractNode.NO_VALUE;
		FlowGraphEnv env2 = envTmp.copyAndUpdateBaseReg(newWithScope);
		BasicBlock bb2 = setupFunction(env2, f);

		// Function declarations and function expressions are different. It's even worse with named function expressions.
		if (exprContext)
			bb.addNode(new DeclareFunctionNode(f, true, resultReg, srcLoc));
		else
			env.declsBB.addNode(new DeclareFunctionNode(f, false, AbstractNode.NO_VALUE, srcLoc));

		BasicBlock ret = translateNode(env2, bb2, body, AbstractNode.RETURN_REG, true);
		ret.addSuccessor(env2.retBB);

		env2.pendingBBs.add(env2.retBB);
		env2.pendingBBs.add(env2.exceptionretBB);
		env.pendingFuns.add(env2.pendingBBs);

		f.getOrdinaryExit().getFirstNode().setSourceLocation(lastSrcLoc);
		f.getExceptionalExit().getFirstNode().setSourceLocation(lastSrcLoc);
		
		f.setMaxRegister(maxReg);
		maxReg = oldMaxTmp;

		return f;
	}

	/**
	 * The main worker. Pattern matches on the Rhino node type and calls itself recursively. Returns the last basic
	 * block for the entire translation.
	 * 
	 * @param env environment
	 * @param bb current basic block
	 * @param sn node to translate
	 * @param resultReg register where the result should be stored
	 */
	@SuppressWarnings("null")
	private BasicBlock translateNode(FlowGraphEnv env, BasicBlock bb, Node sn, int resultReg, boolean statement) {
		SourceLocation srcLoc = SourceLocationMaker.makeFromNode(sn);
		lastSrcLoc = srcLoc;
		int snType = sn.getType();
		if (logger.isDebugEnabled())
			logger.debug("translating node " + sn.toString(true, true, true));
		switch (snType) {
		case Token.FUNCTION: // function [id]([args]...) {}
			translateFunction(env, bb, sn, srcLoc, resultReg);
			return bb;
		case Token.CONST:
			throw new AnalysisException("Const keyword currently not handled.");
		case Token.VAR: { // var ([id] = [exp]?)+
			Node grandchild;
			BasicBlock currBB = bb;
			for (Node child : sn.children()) {
				String id = child.getString();
				// env.declsBB.addNode(new DeclareVariableNode(id, srcLoc));
				final SourceLocation mySourceLoc = SourceLocationMaker.makeFromNode(child);
				env.declsBB.addNode(new DeclareVariableNode(id, mySourceLoc));
				env.fun.addVariableName(id);
				// Handle variable initializations in the declaration.
				if ((grandchild = child.getFirstChild()) != null) {
					final SourceLocation initializerLocation = SourceLocationMaker.makeFromNode(grandchild);
					int writeReg = nextRegister(env);
					currBB = translateNode(env, currBB, grandchild, writeReg, false);
					addNodeToBlock(env, currBB, new WriteVariableNode(writeReg, id, initializerLocation), writeReg, true);
				}
			}
			return currBB;
		}
		case Token.EXPR_RESULT: { // [expr]; Expression statements.
			Directive directive = getDirective(sn);
			if (directive != null) {
				switch (directive) {
				case UNREACHABLE: {
					addNodeToBlock(bb, AssumeNode.makeUnreachable(srcLoc), statement);
					return bb;
				}
				default:
					throw new UnsupportedOperationException("Unhandled enum: " + directive);
				}
			}
		}
		//$FALL-THROUGH$		
		case Token.SCRIPT:
		case Token.BLOCK:
			return translateChildren(env, bb, sn, snType == Token.EXPR_RESULT ? AbstractNode.NO_VALUE : resultReg);
		case Token.NEW:
		case Token.CALL: { // [new] [fn-name]([arg1],...,[argn])
			Node firstChild = sn.getFirstChild();
			boolean complexChildType = firstChild.isGetElem() || firstChild.isGetProp();
			
			int objReg = snType == Token.NEW || !firstChild.isName() ? AbstractNode.NO_VALUE : nextRegister(env);
			int lhsReg = nextRegister(env);
			FlowGraphEnv env2 = env.copyAndUpdateBaseReg(objReg);
			BasicBlock currBB = translateNode(env2, bb, firstChild, lhsReg, false); // Must ensure right order of execution
			env.nextReg = env2.nextReg; // Do not overwrite used registers.
			
			int propReg = AbstractNode.NO_VALUE;
			String propStr = null;
			if (complexChildType) {
				AbstractNode an = env.propertyNodes.get(lhsReg);
				if (an instanceof IPropertyNode) {
					objReg = ((IPropertyNode) an).getBaseRegister();
					if (an instanceof ReadPropertyNode) {
						((ReadPropertyNode) an).setResultRegister(AbstractNode.NO_VALUE); // Don't need the result anymore.
					}
				} else if (an instanceof ReadVariableNode) {
					objReg = ((ReadVariableNode) an).getResultRegister();
				}
				Node prop = firstChild.getFirstChild().getNext();
				if (prop.isString())
					propStr = prop.getString();
				else {
					propReg = ((IPropertyNode) an).getPropertyRegister();
				}
			}
			List<Integer> rhsRegs = newList();
			for (Node child : sn.children()) {
				if (child == firstChild)
					continue;
				currBB = translateNode(env, currBB, child, nextRegister(env, rhsRegs), false);
			}
			BasicBlock bb2 = newSuccessorBasicBlock(env, currBB);
			CallNode cn;
			if (complexChildType)
				cn = new CallNode(snType == Token.NEW, resultReg, objReg, propReg, propStr, rhsRegs, srcLoc);
			else
				cn = new CallNode(snType == Token.NEW, false, resultReg, objReg, lhsReg, rhsRegs, srcLoc);
			addNodeToBlock(env, bb2, cn, objReg, statement);
			return newSuccessorBasicBlock(env, bb2);
		}
		case Token.NAME: // var reference
			addNodeToBlock(env, bb, new ReadVariableNode(sn.getString(), resultReg, env.baseReg, srcLoc), resultReg, statement);
			return bb;
		case Token.STRING: // [string]
			addNodeToBlock(bb, ConstantNode.makeString(sn.getString(), resultReg, srcLoc), statement);
			return bb;
		case Token.NUMBER: // [number]
			addNodeToBlock(bb, ConstantNode.makeNumber(sn.getDouble(), resultReg, srcLoc), statement);
			return bb;
		case Token.TYPEOF: {
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			TypeofNode tp;
			// There's two typeof. One is called with a register, and one with a plain text name. The old Rhino
			// had different tokens for these.
			if (firstChild.isName())
				tp = new TypeofNode(firstChild.getString(), resultReg, srcLoc);
			else {
				int argReg = nextRegister(env);
				currBB = translateNode(env, bb, firstChild, argReg, false);
				tp = new TypeofNode(argReg, resultReg, srcLoc);
			}
			addNodeToBlock(env, currBB, tp, resultReg, statement);
			return currBB;
		}
		case Token.BITNOT:
		case Token.NOT:
		case Token.POS:
		case Token.NEG: { // [op] [operand]
			int argReg = nextRegister(env);
			BasicBlock childBlock = translateNode(env, bb, sn.getFirstChild(), argReg, false);
			addNodeToBlock(childBlock, new UnaryOperatorNode(getFlowGraphUnaryOp(snType), argReg, resultReg, srcLoc), statement);
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
			int arg1Reg = nextRegister(env), arg2Reg = nextRegister(env);
			BasicBlock currBB = translateNode(env, bb, sn.getFirstChild(), arg1Reg, false);
			currBB = translateNode(env, currBB, sn.getLastChild(), arg2Reg, false);
			addNodeToBlock(currBB, new BinaryOperatorNode(getFlowGraphBinaryOp(snType), arg1Reg, arg2Reg, resultReg, srcLoc), statement);
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
			// This is somewhat messy. A plain assignment is lhs = rhs. Other assignments such as lhs += rhs are not plain.
			boolean isPlainAssignment = snType == Token.ASSIGN;
			Node firstChild = sn.getFirstChild(); // lhs
			Node grandChild = firstChild.getLastChild(); // property name of lhs, null if not a property write
			boolean inUnevalMappingMode = env.evalResultMap != null && Options.isUnevalizerEnabled() && isPlainAssignment
					&& firstChild.isName() && firstChild.getString().equals(env.evalResultMap.getFirst());
			int rhsReg = resultReg == AbstractNode.NO_VALUE ? nextRegister(env) : resultReg;
			int lhsReg = isPlainAssignment ? AbstractNode.NO_VALUE : nextRegister(env);
			int baseFirstChildReg = AbstractNode.NO_VALUE;
			int propFirstChildReg = AbstractNode.NO_VALUE;
			int oldRhsReg = AbstractNode.NO_VALUE;
			BasicBlock currBB;
			if (firstChild.isName()) // lhs is a variable?
				currBB = bb;
			else { // lhs is a property write
				baseFirstChildReg = nextRegister(env);
				propFirstChildReg = grandChild == null || grandChild.isString() ? AbstractNode.NO_VALUE : nextRegister(env);
				currBB = translateNode(env, bb, firstChild.getFirstChild(), baseFirstChildReg, false); // translating first part of lhs
				if (propFirstChildReg != AbstractNode.NO_VALUE)
					currBB = translateNode(env, currBB, grandChild, propFirstChildReg, false);
			}

			if (!isPlainAssignment) {
				AbstractNode rn;
				oldRhsReg = rhsReg;
				rhsReg = nextRegister(env);
				if (firstChild.isName())
					rn = new ReadVariableNode(firstChild.getString(), lhsReg, env.baseReg, srcLoc);
				else {
					if (firstChild.getLastChild().isString())
						rn = new ReadPropertyNode(baseFirstChildReg, firstChild.getLastChild().getString(), lhsReg, srcLoc);
					else
						rn = new ReadPropertyNode(baseFirstChildReg, propFirstChildReg, lhsReg, srcLoc);
				}
				addNodeToBlock(env, currBB, rn, baseFirstChildReg, false);
			}
			currBB = translateNode(env, currBB, sn.getLastChild(), inUnevalMappingMode ? env.evalResultMap.getSecond() : rhsReg, false); // translating rhs
			if (!isPlainAssignment) {
				addNodeToBlock(env, currBB, new BinaryOperatorNode(getFlowGraphBinaryOp(sn.getType()), lhsReg, rhsReg, oldRhsReg, srcLoc), rhsReg, false);
				rhsReg = oldRhsReg;
			}
			if (inUnevalMappingMode)
				return currBB;
			if (firstChild.isName()) // lhs is a variable?
				addNodeToBlock(env, currBB, new WriteVariableNode(rhsReg, firstChild.getString(), srcLoc), rhsReg, statement);
			else if (firstChild.isGetElem() || firstChild.isGetProp()) { // lhs is a property write
				AbstractNode an = env.propertyNodes.get(baseFirstChildReg);
				WritePropertyNode wp;
				if (an instanceof ReadPropertyNode) {
					if (grandChild.isString())
						wp = new WritePropertyNode(((LoadNode) an).getResultRegister(), grandChild.getString(), rhsReg, srcLoc);
					else
						wp = new WritePropertyNode(((LoadNode) an).getResultRegister(), propFirstChildReg, rhsReg, srcLoc);
				} else if (an instanceof ReadVariableNode) {
					if (grandChild.isString())
						wp = new WritePropertyNode(((ReadVariableNode) an).getResultRegister(), grandChild.getString(), rhsReg, srcLoc);
					else
						wp = new WritePropertyNode(((ReadVariableNode) an).getResultRegister(), propFirstChildReg, rhsReg, srcLoc);
				} else {
					if (grandChild.isString())
						wp = new WritePropertyNode(baseFirstChildReg, grandChild.getString(), rhsReg, srcLoc);
					else
						wp = new WritePropertyNode(baseFirstChildReg, propFirstChildReg, rhsReg, srcLoc);
				}
				addNodeToBlock(env, currBB, wp, wp.getBaseRegister(), statement);
			}
			return currBB;
		}
		case Token.INC:
		case Token.DEC: {
			Node firstChild = sn.getFirstChild();
			boolean isPost = sn.getIntProp(Node.INCRDECR_PROP) == 1;
			boolean resultUsed = resultReg != AbstractNode.NO_VALUE;
			BinaryOperatorNode.Op op = Token.INC == snType ? BinaryOperatorNode.Op.ADD : BinaryOperatorNode.Op.SUB;
			int oneReg = nextRegister(env), res = nextRegister(env);
			bb.addNode(ConstantNode.makeNumber(1, oneReg, srcLoc));
			BasicBlock currBB = translateNode(env, bb, firstChild, res, false);
			int toNumberReg = isPost && resultUsed ? resultReg : nextRegister(env);
			int binopReg = isPost ? nextRegister(env) : (resultUsed ? resultReg : nextRegister(env));
			currBB.addNode(new UnaryOperatorNode(UnaryOperatorNode.Op.PLUS, res, toNumberReg, srcLoc));
			currBB.addNode(new BinaryOperatorNode(op, toNumberReg, oneReg, binopReg, srcLoc));
			if (firstChild.isName())
				addNodeToBlock(env, currBB, new WriteVariableNode(binopReg, firstChild.getString(), srcLoc), binopReg, statement);
			else if (firstChild.isGetElem() || firstChild.isGetProp()) {
				ReadPropertyNode rn = (ReadPropertyNode) env.propertyNodes.get(res);
				WritePropertyNode wp;
				if (rn.isPropertyFixed())
					wp = new WritePropertyNode(rn.getBaseRegister(), rn.getPropertyString(), binopReg, srcLoc);
				else
					wp = new WritePropertyNode(rn.getBaseRegister(), rn.getPropertyRegister(), binopReg, srcLoc);
				addNodeToBlock(env, currBB, wp, rn.getBaseRegister(), statement);
			}
			return currBB;
		}
		case Token.HOOK:
		case Token.IF: {
			// TODO: Insert assume nodes for comparisons with null
			Node condNode = sn.getFirstChild();
			Node trueNode = condNode.getNext();
			Node falseNode = trueNode.getNext();
			int condReg = nextRegister(env);
			BasicBlock condBlock = translateNode(env, bb, condNode, condReg, false);

			BasicBlock trueBranch = newSuccessorBasicBlock(env, condBlock);
			BasicBlock falseBranch = newSuccessorBasicBlock(env, condBlock);

			IfNode ifn = new IfNode(condReg, srcLoc);
			ifn.setSuccessors(trueBranch, falseBranch);
			FlowGraphEnv env2 = env.copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE);
			addNodeToBlock(env2, condBlock, ifn, condReg, false); // can't set end-of-statement, condReg may be used at the edges

			BasicBlock trueBlock = translateNode(env2.copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE), trueBranch, trueNode, resultReg, false);
			BasicBlock falseBlock = falseNode == null ? falseBranch : translateNode(env.copyAndGiveUniquePropertyNodes(condReg), falseBranch, falseNode, resultReg, false);
			return newJoinBasicBlock(env, trueBlock, falseBlock);
		}
		case Token.TRUE:
		case Token.FALSE:
			addNodeToBlock(bb, ConstantNode.makeBoolean(snType == Token.TRUE, resultReg, srcLoc), statement);
			return bb;
		case Token.NULL:
			addNodeToBlock(bb, ConstantNode.makeNull(resultReg, srcLoc), statement);
			return bb;
		case Token.GET:
		case Token.GETELEM:
		case Token.GETPROP: {
			Node obj = sn.getFirstChild();
			ReadPropertyNode rp;
			int baseReg = nextRegister(env);
			BasicBlock currBB = translateNode(env, bb, obj, baseReg, false);
			Node prop = obj.getNext();

			if (prop.isString())
				rp = new ReadPropertyNode(baseReg, prop.getString(), resultReg, srcLoc);
			else {
				int rpReg = nextRegister(env);
				currBB = translateNode(env, currBB, prop, rpReg, false);
				rp = new ReadPropertyNode(baseReg, rpReg, resultReg, srcLoc);
			}
			addNodeToBlock(env, currBB, rp, baseReg, statement);
			return currBB;
		}
		case Token.SET: {
			throw new AnalysisException("Not implemented: " + Token.name(sn.getType()));
			// AssumeNode.makePropertyNonNullUndef(baseReg, propNode.getString(), wn, srcLoc);
		}
		case Token.THIS:
			bb.getFunction().setUsesThis(true);
			addNodeToBlock(env, bb, new ReadVariableNode("this", resultReg, AbstractNode.NO_VALUE, srcLoc), resultReg, statement);
			return bb;
		case Token.BREAK:
		case Token.CONTINUE: { // FIXME: support break/continue statements with label!
			bb.addSuccessor(snType == Token.BREAK ? env.breakBlock : env.continueBlock);
			return newBasicBlock(env, bb);
		}
		case Token.EMPTY:
		case Token.LABEL_NAME: 
			return bb;
		case Token.LABEL: // FIXME: collect labels for break/continue (see testFlowgraphBuilder0188.js)
			return translateChildren(env, bb, sn, resultReg);
		case Token.RETURN: {
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			if (firstChild != null) {
				currBB = translateNode(env, bb, firstChild, resultReg, false);
			} else {
				// add the implicit undefined return node, so line can be visited
				final ConstantNode undefined = ConstantNode.makeUndefined(resultReg, SourceLocationMaker.makeFromNode(sn));
				currBB.addNode(undefined);
			}
			currBB.addSuccessor(env.retBB);
			return newBasicBlock(env, currBB);
		}
		case Token.TRY: {
			Node tryNode = sn.getFirstChild();
			Node catchNode = tryNode.getNext();
			Node finallyNode = catchNode != null ? catchNode.getNext() : null;
			// Don't bother with translating catch-nodes and finally-nodes without children.
			finallyNode = finallyNode != null && finallyNode.getFirstChild() == null ? null : finallyNode;
			catchNode = catchNode != null && catchNode.getFirstChild() == null ? null : catchNode;
			BasicBlock bbPanicBB = bb.getExceptionHandler();

			BasicBlock tryBlock = newSuccessorBasicBlock(env, bb);
			BasicBlock finallyBlock = null, catchBlock = null;

			// We need to allocate the catch block early so that all children gets the right exception handler.
			if (catchNode != null)
				catchBlock = newCatchBasicBlock(env, tryBlock);

			BasicBlock retBlock = newBasicBlock(env, bb);

			if (finallyNode != null) {
				finallyBlock = newBasicBlock(env, bb);
				if (catchNode != null)
					catchBlock.setExceptionHandler(finallyBlock);
				else
					tryBlock.setExceptionHandler(finallyBlock);
			}

			FlowGraphEnv env2 = env.copyAndUpdateContinueBreak(env.continueBlock, retBlock).copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE);
			if (finallyNode != null)
				env2.retBB = retBlock; // see wala/try.js, crazy.
			BasicBlock tryBB = translateNode(env2, tryBlock, tryNode, resultReg, false);

			bumpToFrontOfPending(env, retBlock);
			tryBB.addSuccessor(retBlock);

			if (catchNode != null) {
				bumpToFrontOfPending(env, catchBlock);
				BasicBlock currBB = translateNode(env, catchBlock, catchNode, resultReg, false);
				currBB.addSuccessor(retBlock);
				if (finallyNode != null)
					currBB.getExceptionHandler().addSuccessor(finallyBlock);
			}

			// Translate the finally block for the case when an exception is thrown.
			if (finallyNode != null) {
				bumpToFrontOfPending(env, finallyBlock);
				int tryReg = nextRegister(env);
				// Catch nodes are used for finally blocks as well.
				CatchNode cn = new CatchNode(tryReg, srcLoc);
				cn.setArtificial();
				finallyBlock.addNode(cn);
				BasicBlock tmpBB = translateNode(env, finallyBlock, finallyNode, resultReg, false);
				ThrowNode tn = new ThrowNode(tryReg, srcLoc);
				tn.setArtificial();
				tmpBB.addNode(tn);
				tmpBB.addSuccessor(bbPanicBB);
				BasicBlock tmpRet = retBlock;
				FlowGraphEnv env3 = env.copyAndInitializeForCopy(tmpRet);
				// Insert a copy of the finally block with a regular return point.
				retBlock = translateNode(env3, retBlock, finallyNode, resultReg, false);
				setDuplicateBlocks(env3.copyBlocks, Collections.<BasicBlock> newList(), tmpRet, finallyBlock);
			}
			return retBlock;
		}
		case Token.CATCH: {
			int catchReg = resultReg;
			Node firstChild = sn.getFirstChild();
			if (firstChild.isName()) {
				catchReg = nextRegister(env);
				bb.addNode(new CatchNode(firstChild.getString(), catchReg, srcLoc));
			}
			BeginWithNode enter1 = new BeginWithNode(catchReg, srcLoc);
			enter1.setArtificial();
			bb.addNode(enter1);
			BasicBlock tempBB = newSuccessorBasicBlock(env, bb);
			BasicBlock panicBB = newCatchBasicBlock(env, tempBB);
			EndWithNode leave1 = new EndWithNode(srcLoc);
			leave1.setArtificial();
			panicBB.addNode(leave1);

			panicBB.setExceptionHandler(null);
			panicBB.addSuccessor(bb.getExceptionHandler());
			int childReg = nextRegister(env);
			BasicBlock childBB = translateNode(env.copyAndUpdateBaseReg(childReg), tempBB, sn.getLastChild(), resultReg, false);
			EndWithNode leave2 = new EndWithNode(srcLoc);
			leave2.setArtificial();
			childBB.addNode(leave2);

			return childBB;
		}
		case Token.THROW: {
			int throwReg = nextRegister(env);
			BasicBlock throwBB = translateNode(env, bb, sn.getFirstChild(), throwReg, false);
			addNodeToBlock(throwBB, new ThrowNode(throwReg, srcLoc), statement);
			return throwBB;
		}
		case Token.WITH: {
			int withReg = nextRegister(env);
			BasicBlock currBB = translateNode(env, bb, sn.getFirstChild(), withReg, false);
			addNodeToBlock(currBB, new BeginWithNode(withReg, srcLoc), statement);
			currBB = newSuccessorBasicBlock(env, currBB);
			BasicBlock panicBB = newCatchBasicBlock(env, currBB);
			addNodeToBlock(panicBB, new EndWithNode(srcLoc), statement);
			panicBB.addSuccessor(bb.getExceptionHandler());
			panicBB.setExceptionHandler(null);
			int baseReg = nextRegister(env);
			currBB = translateNode(env.copyAndUpdateBaseReg(baseReg), currBB, sn.getLastChild(), resultReg, false);
			AbstractNode lastNode = currBB.isEmpty() ? null : currBB.getLastNode();
			currBB = requiresNewBlock(lastNode) ? newSuccessorBasicBlock(env, currBB) : currBB;
			addNodeToBlock(currBB, new EndWithNode(srcLoc), statement);
			currBB.setExceptionHandler(bb.getExceptionHandler());
			return currBB;
		}
		case Token.ARRAYLIT: {
			List<Integer> argRegs = newList();
			BasicBlock currBB = bb;
			int arrReg = nextRegister(env);
			addNodeToBlock(env, currBB, new ReadVariableNode("Array", arrReg, AbstractNode.NO_VALUE, srcLoc), arrReg, false);
			for (Node child : sn.children()) {
				if (child.isEmpty())
					currBB.addNode(ConstantNode.makeUndefined(nextRegister(env, argRegs), srcLoc));
				else
					currBB = translateNode(env, currBB, child, nextRegister(env, argRegs), false);
			}

			currBB = newSuccessorBasicBlock(env, currBB);
			addNodeToBlock(env, currBB, new CallNode(true, true, resultReg, AbstractNode.NO_VALUE, arrReg, argRegs, srcLoc), resultReg, statement);
			return newSuccessorBasicBlock(env, currBB);
		}
		case Token.OBJECTLIT: { // {(prop: [exp])*}
			BasicBlock currBB = bb;
			if (resultReg == AbstractNode.NO_VALUE) {
				resultReg = nextRegister(env);
			}
			addNodeToBlock(env, currBB, new NewObjectNode(resultReg, srcLoc), resultReg, false);

			// So the children of object literals have their own children, even if they are of a simple
			// data type. Special case for them here.

			// SPEC: 11.1.5 A property name can be either a string or number literal.
			for (Node child : sn.children()) {
				WritePropertyNode wn;
				int propValueReg = nextRegister(env);
				currBB = translateNode(env, currBB, child.getFirstChild(), propValueReg, false);
				if (child.isString())
					wn = new WritePropertyNode(resultReg, child.getString(), propValueReg, srcLoc);
				else {
					int propNameReg = nextRegister(env);
					currBB = translateNode(env, currBB, child, propNameReg, false);
					wn = new WritePropertyNode(resultReg, propNameReg, propValueReg, srcLoc);
				}
				addNodeToBlock(env, currBB, wn, resultReg, false);
			}
			return currBB;
		}
		case Token.DELPROP: { // delete [exp] or delete [identifier]
			BasicBlock currBB = bb;
			Node firstChild = sn.getFirstChild();
			int firstChildType = firstChild.getType();
			int delReg = resultReg == AbstractNode.NO_VALUE ? nextRegister(env) : resultReg;
			if (firstChildType == Token.GETELEM || firstChildType == Token.GETPROP) {
				int objReg = nextRegister(env), propReg = nextRegister(env);
				currBB = translateNode(env, bb, firstChild.getFirstChild(), objReg, false);
				currBB = translateNode(env, currBB, firstChild.getLastChild(), propReg, false);
				addNodeToBlock(currBB, new DeletePropertyNode(objReg, propReg, delReg, srcLoc), statement);
			} else
				addNodeToBlock(currBB, new DeletePropertyNode(firstChild.getString(), delReg, srcLoc), statement);
			return currBB;
		}
		case Token.OR:
		case Token.AND: { // [exp] AND/OR [exp]
			int arg1Reg = resultReg == AbstractNode.NO_VALUE ? nextRegister(env) : resultReg;
			IfNode ifn = new IfNode(arg1Reg, srcLoc);
			FlowGraphEnv env2 = snType == Token.AND ? env : env.copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE);

			BasicBlock arg1Block = translateNode(env2, bb, sn.getFirstChild(), arg1Reg, false);

			BasicBlock trueBranch = newSuccessorBasicBlock(env, arg1Block);
			BasicBlock falseBranch = newSuccessorBasicBlock(env, arg1Block);

			if (snType == Token.AND)
				ifn.setSuccessors(falseBranch, trueBranch);
			else {
				env2 = env;
				ifn.setSuccessors(trueBranch, falseBranch);
			}

			addNodeToBlock(env2, arg1Block, ifn, arg1Reg, false);

			BasicBlock falseBlock = translateNode(env2.copyAndGiveUniquePropertyNodes(arg1Reg), falseBranch, sn.getLastChild(), resultReg, false);

			return newJoinBasicBlock(env, trueBranch, falseBlock);
		}
		case Token.COMMA: {
			Node lastChild = sn.getLastChild();
			BasicBlock currBB = bb;
			for (Node n : sn.children())
				currBB = translateNode(env, currBB, n, n == lastChild ? resultReg : AbstractNode.NO_VALUE, false);
			return currBB;
		}
		case Token.VOID: {
			BasicBlock voidBB = translateNode(env, bb, sn.getFirstChild(), AbstractNode.NO_VALUE, false);
			final ConstantNode undefined = ConstantNode.makeUndefined(resultReg, srcLoc);
			undefined.setArtificial();
			addNodeToBlock(voidBB, undefined, statement);
			return voidBB;
		}
		case Token.SWITCH: {
			Node firstChild = sn.getFirstChild();
			Node defaultNode = null, lastChild = null;
			int condReg = nextRegister(env);
			BasicBlock currBB = translateNode(env, bb, firstChild, condReg, false);
			BasicBlock trueBranch = null, falseBranch = null, oldTrueBranch;
			int caseReg = sn.hasMoreThanOneChild() ? nextRegister(env) : AbstractNode.NO_VALUE;
			int compReg = sn.hasMoreThanOneChild() ? nextRegister(env) : AbstractNode.NO_VALUE;
			BasicBlock retBlock = newBasicBlock(env, currBB);
			FlowGraphEnv env2 = env.copyAndUpdateContinueBreak(currBB, retBlock);
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

				trueBranch = newSuccessorBasicBlock(env2, currBB);
				falseBranch = newSuccessorBasicBlock(env2, currBB);

				if (oldTrueBranch != null && !oldTrueBranch.getSuccessors().contains(retBlock))
					oldTrueBranch.addSuccessor(trueBranch);

				currBB = translateNode(env2, currBB, child.getFirstChild(), caseReg, false);
				currBB.addNode(new BinaryOperatorNode(BinaryOperatorNode.Op.EQ, condReg, caseReg, compReg, srcLoc));
				IfNode ifn = new IfNode(compReg, srcLoc);
				currBB.addNode(ifn);
				ifn.setSuccessors(trueBranch, falseBranch);

				trueBranch = translateNode(env2, trueBranch, child.getLastChild(), resultReg, false);
				currBB = falseBranch;
			}
			if (trueBranch != null && defaultNode != null && lastChild != defaultNode)
				trueBranch.addSuccessor(retBlock);

			if (defaultNode != null) {
				BasicBlock defaultBlock = newSuccessorBasicBlock(env2, currBB);
				if (falseBranch != null && !falseBranch.getSuccessors().contains(retBlock))
					falseBranch.addSuccessor(defaultBlock);
				if (trueBranch != null && !trueBranch.getSuccessors().contains(retBlock))
					trueBranch.addSuccessor(defaultBlock);
				currBB = translateNode(env2, defaultBlock, defaultNode, resultReg, false);
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
			int reReg = nextRegister(env), expReg = nextRegister(env, args);
			int flagReg = sn.getChildCount() > 1 ? nextRegister(env, args) : AbstractNode.NO_VALUE;
			bb.addNode(new ReadVariableNode("RegExp", reReg, AbstractNode.NO_VALUE, srcLoc));
			bb.addNode(ConstantNode.makeString(sn.getFirstChild().getString(), expReg, srcLoc));
			if (flagReg != AbstractNode.NO_VALUE)
				bb.addNode(ConstantNode.makeString(sn.getLastChild().getString(), flagReg, srcLoc));
			BasicBlock callBlock = newSuccessorBasicBlock(env, bb);
			addNodeToBlock(callBlock, new CallNode(true, false, resultReg, AbstractNode.NO_VALUE, reReg, args, srcLoc), statement);
			return newSuccessorBasicBlock(env, callBlock);
		}
		case Token.FOR:
		case Token.WHILE: {
			BasicBlock currBB = bb, condBlock;
			Node condNode = snType == Token.FOR ? sn.getFirstChild().getNext() : sn.getFirstChild();
			int condReg = nextRegister(env);

			// There are two kinds of for-nodes in the closure compiler AST. One is the regular
			// one and the second one is for (x in obj) {}, and the difference is the number of
			// children.

			// Special treatment of for-in loop, it's really a different kind of beast.
			if (sn.getChildCount() == 3)
				return translateForIn(env, bb, sn, resultReg, srcLoc);

			// Handle the regular for-loop initialization.
			if (snType == Token.FOR)
				currBB = translateNode(env, currBB, sn.getFirstChild(), AbstractNode.NO_VALUE, false);

			BasicBlock condBB = newSuccessorBasicBlock(env, currBB);
			currBB = condBB;

			// Insert a synthetic node "true" in case the condition block in the for-loop is empty. (Copied below.)
			if (snType == Token.FOR && condNode.isEmpty()) {
				ConstantNode bn = ConstantNode.makeBoolean(true, condReg, srcLoc);
				bn.setArtificial();
				condBB.addNode(bn);
				currBB = newSuccessorBasicBlock(env, currBB);
			}

			condBlock = translateNode(env, currBB, condNode, condReg, false);

			BasicBlock trueBranch = newSuccessorBasicBlock(env, condBlock);
			BasicBlock falseBranch = newSuccessorBasicBlock(env, condBlock);

			IfNode ifn = new IfNode(condReg, srcLoc);
			condBlock.addNode(ifn);
			ifn.setSuccessors(trueBranch, falseBranch);

			FlowGraphEnv freshEnv = env.copyAndUpdateContinueBreak(condBB, falseBranch).copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE);

			BasicBlock trueBlock = translateNode(freshEnv, trueBranch, sn.getLastChild(), resultReg, false);
			if (snType == Token.FOR) {
				trueBlock = translateNode(freshEnv, trueBlock, sn.getFirstChild().getNext().getNext(), AbstractNode.NO_VALUE, false);
				if (Options.isUnrollOneAndAHalfEnabled()) {
					BasicBlock tmpRet = newSuccessorBasicBlock(freshEnv, trueBlock);
					FlowGraphEnv env3 = freshEnv.copyAndInitializeForCopy(tmpRet);
					// Insert a copy of the condition block with a quick bail out.
					if (snType == Token.FOR && condNode.isEmpty()) {
						ConstantNode bn = ConstantNode.makeBoolean(true, condReg, srcLoc);
						bn.setArtificial();
						tmpRet.addNode(bn);
						tmpRet = newSuccessorBasicBlock(freshEnv, tmpRet);
					}
					trueBlock = translateNode(freshEnv, tmpRet, condNode, condReg, false);
					ifn = new IfNode(condReg, srcLoc);
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
			BasicBlock entryBB = newSuccessorBasicBlock(env, bb);

			int condReg = nextRegister(env);
			BasicBlock falseBranch = newBasicBlock(env, bb);
			FlowGraphEnv freshEnv = env.copyAndUpdateContinueBreak(entryBB, falseBranch);

			BasicBlock trueBlock = translateNode(freshEnv, entryBB, sn.getFirstChild(), resultReg, false);

			BasicBlock condBB = translateNode(env, newSuccessorBasicBlock(freshEnv, trueBlock), sn.getLastChild(), condReg, false);

			IfNode ifn = new IfNode(condReg, srcLoc);
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

	/**
	 * Decides if a node is a directive, and if so what kind of directive it is.
	 * @see Directive for how the directives should be spelled
	 */
	private static Directive getDirective(Node n) {
		if (n != null) {
			int nType = n.getType();
			final boolean typeIsOk = nType == Token.EXPR_RESULT || nType == Token.VOID;
			final boolean valTypeIsOk = n.getFirstChild().getType() == Token.STRING;
			if (typeIsOk && valTypeIsOk) {
				final String text = n.getFirstChild().getString();
				for (Directive d : Directive.values()) {
					if (text.equalsIgnoreCase(d.getName())) {
						return d;
					}
				}
			}
		}
		return null;
	}

	private static void addNodeToBlock(BasicBlock bb, AbstractNode node, boolean endOfStatement) {
		bb.addNode(node);
		if (endOfStatement)
			node.setRegistersDone(true);
	}

	/**
	 * Adds the given node to the given basic block.
	 * 
	 * @param env
	 *            environment
	 * @param bb
	 *            basic block
	 * @param node
	 *            node to add
	 * @param baseReg
	 * @param endOfStatement
	 *            if true, mark the node as registers-done
	 */
	private static void addNodeToBlock(FlowGraphEnv env, BasicBlock bb, AbstractNode node, int baseReg, boolean endOfStatement) {
		bb.addNode(node);
		{
			// Figure out if we need to add this node to our property node map, and how to index it.
			if (node instanceof WritePropertyNode) { 
				env.registerPropertyWrite((WritePropertyNode) node);
			} else if(node instanceof LoadNode){
				env.registerRegisterLoad((LoadNode) node);
			}
		}
		Set<AssumeNode> assumeNodesToAdd = newSet();
		{
			// check if we can add assume nodes for property dereferences
			AbstractNode baseNode = env.loadNodes.get(baseReg);
			if (node instanceof IPropertyNode && baseNode != null) {
				// we have something of the form x.p, where x is either a variable or property (the base)
				// x will be non-null and non-undefined after the dereference, otherwise a crash would have occured
				if (baseNode instanceof ReadVariableNode) {
					// x.p, and x is a variable
					final ReadVariableNode readNode = (ReadVariableNode) baseNode;
					if (!(readNode.getVariableName().equals("this"))) {
						assumeNodesToAdd.add(AssumeNode.makeVariableNonNullUndef(readNode.getVariableName(), node.getSourceLocation()));
					}
				} else if (baseNode instanceof ReadPropertyNode) {
					// x.p, and x is a property (thus the full form is o.x.p)
					assumeNodesToAdd.add(AssumeNode.makePropertyNonNullUndef((ReadPropertyNode)baseNode));
				}
			}
		}
		if (!assumeNodesToAdd.isEmpty()) {
			Set<BasicBlock> blocksToAddAssumesTo = Collections.newSet();
			if (node instanceof IfNode) {
				// special case: IfNode must be the last node in its block, so add assume to successors
				final IfNode ifNode = (IfNode) node;
				blocksToAddAssumesTo.add(ifNode.getSuccTrue());
				blocksToAddAssumesTo.add(ifNode.getSuccFalse());
			} else {
				blocksToAddAssumesTo.add(bb);
			}
			for (BasicBlock block : blocksToAddAssumesTo) {
				for (AssumeNode assume: assumeNodesToAdd) {
					block.addNode(assume);
				}
			}
		}
		if (endOfStatement)
			bb.getLastNode().setRegistersDone(true);
	}

	/**
	 * Dynamically splits a basic block above node n and inserts the code.
	 */
	public int extendFlowgraphWithCallAtNode(FlowGraph fg, AbstractNode n, int baseReg) {
		boolean add = false;
		BasicBlock bb = n.getBlock();
		SourceLocation srcLoc = bb.getSourceLocation();
		Function f = bb.getFunction();
		maxReg = f.getMaxRegister();
		int resultReg = maxReg++;
		// TODO: Ponder copyOf.
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
		f.setMaxRegister(maxReg);
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

		FlowGraphEnv env = new FlowGraphEnv(fg, dk.brics.tajs.util.Collections.<List<BasicBlock>> newList(), 
				dk.brics.tajs.util.Collections.<BasicBlock> newList(), fun);

		BasicBlock bb = setupEnv(callBlock, env, previousExt);
		env.evalResultMap = varName == null ? null : Pair.make(varName, resultRegister);

		Node root = parse(sourceString, s.getFileName(), 0 /* will be overridden later */);

		// set the line and column numbers of the created nodes, to the location the parsing happens at!
		// TODO somehow encode original location of parsed code?
		setLineAndColumnNumbers(root, s.getLineNumber(), s.getColumnNumber() - 1);

		int resultReg = nextRegister(env);
		// Insert a dummy node to prevent empty basic blocks. We need a constant node to get uniform treatment
		// with other "functions".
		final ConstantNode undefined = ConstantNode.makeUndefined(resultReg, env.fun.getSourceLocation());
		undefined.setArtificial();
		env.declsBB.addNode(undefined);
		BasicBlock retBlock = translateChildren(env, bb, root.getLastChild(), resultReg);

		return postTranslationFixup(env, normalizedSourceString, retBlock);
	}

	/**
	 * Overwrites the line and column number for the given AST node and its successors and descendants.
	 */
	private static void setLineAndColumnNumbers(Node root, int lineNumber, int columnNumber) {
		LinkedList<Node> worklist = new LinkedList<>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			Node current = worklist.pop();
			if (current == null)
				continue;
			current.setLineno(lineNumber);
			current.setCharno(columnNumber);
			worklist.add(current.getNext());
			worklist.add(current.getFirstChild());
		}
	}

	/**
	 * Prepares env for a flow graph extension.
	 * Creates a new basic block,, sets retBB as the after-call block and exceptionretBB as the exception handler of the call node. 
	 * 
	 * @param callBlock call block where the extension is made
	 * @param env environment
	 * @param previousExt previous flow graph extension to be removed, or null if none
	 * @return the new basic block for declarations
	 */
	private BasicBlock setupEnv(BasicBlock callBlock, FlowGraphEnv env, FlowGraphFragment previousExt) {
		if (previousExt != null)
			nukeExtension(env.fg, env.fun, previousExt);
		env.declsBB = newBasicBlock(env, callBlock);
		env.retBB = callBlock.getSingleSuccessor();
		env.exceptionretBB = callBlock.getExceptionHandler();
		// Start allocating registers one more than the previous max.
		maxReg = env.fun.getMaxRegister() + 1;
		env.nextReg = maxReg;
		return env.declsBB;
	}

	/**
	 * Dynamically adds a piece of code to the flow graph at the call node n and returns the entry block for the code.
	 */
	public FlowGraphFragment extendFlowGraphWithEventHandler(FlowGraph fg, String sourceString, FlowGraphFragment previousExt, LoadNode n) {
		BasicBlock callBlock = n.getBlock();
		Function fun = n.getBlock().getFunction();
		FlowGraphEnv env = new FlowGraphEnv(fg, dk.brics.tajs.util.Collections.<List<BasicBlock>> newList(), 
				dk.brics.tajs.util.Collections.<BasicBlock> newList(), fun);

		BasicBlock bb = setupEnv(callBlock, env, previousExt);

		int resultReg = n.getResultRegister() == AbstractNode.NO_VALUE ? nextRegister(env) : n.getResultRegister();
		// Insert a dummy node to prevent empty basic blocks. We need a constant node to get uniform treatment
		// with other "functions".
		final ConstantNode undef = ConstantNode.makeUndefined(resultReg, env.fun.getSourceLocation());
		undef.setArtificial();		
		env.declsBB.addNode(undef);

		// TODO: Should we add the event variable as argument here as well?
		translateEventHandler(env, bb, new EventHandlerJavaScriptSource(fun.getSourceLocation().getFileName(), sourceString, 0, "TIMEOUT"), resultReg);

		return postTranslationFixup(env, sourceString, bb);
	}

	/**
	 * Fixes the flow graph after an extension has been made and returns a new FlowGraphFragment.
	 */
	private FlowGraphFragment postTranslationFixup(FlowGraphEnv env, String sourceString, BasicBlock retBlock) {
		Collection<Function> fs = newSet();
		Function firstFun = env.pendingFuns.isEmpty() ? null : env.pendingFuns.get(0).get(0).getFunction();
		for (List<BasicBlock> funBBs : env.pendingFuns)
			fs.add(funBBs.get(0).getFunction());

		Collection<BasicBlock> bbs = newSet();
		bbs.addAll(env.pendingBBs);

		Collection<AbstractNode> ns = newSet(); // FIXME: not used?? (intended for the unevalizer?)
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
		env.fun.setMaxRegister(maxReg);

		for (List<BasicBlock> funBBs : env.pendingFuns)
			env.pendingBBs.addAll(funBBs);

		Map<BasicBlock, Integer> pointedTo = newMap();
		increaseOrAdd(pointedTo, env.declsBB);
		removeEmptyBlocks(env, pointedTo);

		for (BasicBlock pending : env.pendingBBs)
			if ((pointedTo.containsKey(pending) && (pointedTo.get(pending) > 0)) || !pending.isEmpty()) // TODO: why this isEmpty check?
				env.fg.addBlock(pending);

		env.fg.complete();

		if (Options.isTestFlowGraphBuilderEnabled())
			System.out.println("fg2: " + env.fg.toString());

		return new FlowGraphFragment(sourceString, env.declsBB, firstFun, fs, bbs, ns);
	}

	/**
	 * Removes the given flow graph extension from the flow graph and function.
	 */
	private static void nukeExtension(FlowGraph fg, Function fun, FlowGraphFragment e) {
		fg.removeFunctions(e.getFunction());
		fun.removeBlocks(e.getBlocks());
		fun.getEntry().removeNodes(e.getNodes());
	}

	/**
	 * Checks whether the given node is of a kind that can only be at the end of its basic block.
	 */
	private static boolean requiresNewBlock(AbstractNode n) {
		return (n instanceof ThrowNode
				|| n instanceof ICallNode
				|| n instanceof ExceptionalReturnNode
				|| n instanceof ReturnNode
				|| n instanceof BeginWithNode
				|| n instanceof EndWithNode);
	}

	/**
	 * Translates for-in.
	 */
	private BasicBlock translateForIn(FlowGraphEnv env, BasicBlock bb, Node sn, int resultReg, SourceLocation srcLoc) {
		int objectReg = nextRegister(env), propListReg = nextRegister(env), condReg = nextRegister(env), propertyReg = nextRegister(env);
		Node firstChild = sn.getFirstChild();
		BasicBlock currBB = bb;
		
		String loopVar;
		if (firstChild.isName())
			loopVar = firstChild.getString();
		else {
			loopVar = firstChild.getFirstChild().getString(); // FIXME: this doesn't work for general LeftHandSideExpression, e.g. 'for (a.b.c[7] in x)'
			currBB = translateNode(env, currBB, firstChild, AbstractNode.NO_VALUE, false);
		}

		// translate the object expression and add a BeginForInNode
		currBB = translateNode(env, currBB, firstChild.getNext(), objectReg, false);
		currBB = newSuccessorBasicBlock(env, currBB);
		BeginForInNode bf = new BeginForInNode(objectReg, propListReg, srcLoc);
		currBB.addNode(bf);

		// start a new basic block with a HasNextPropertyNode and an IfNode
		BasicBlock condBlock = newSuccessorBasicBlock(env, currBB);
		condBlock.addNode(new HasNextPropertyNode(propListReg, condReg, srcLoc));
		BasicBlock trueBranch = newSuccessorBasicBlock(env, condBlock);
		BasicBlock falseBranch = newSuccessorBasicBlock(env, condBlock);
		BasicBlock trueEndBlock = newBasicBlock(env, condBlock);
		IfNode ifn = new IfNode(condReg, srcLoc);
		condBlock.addNode(ifn);
		ifn.setSuccessors(trueBranch, falseBranch);
		ifn.setArtificial();

		// add a NextPropertyNode and translate the loop body at the true branch
		trueBranch.addNode(new NextPropertyNode(propListReg, propertyReg, srcLoc));
		trueBranch.addNode(new WriteVariableNode(propertyReg, loopVar, srcLoc)); // FIXME: not necessarily a variable, could be a property reference
		FlowGraphEnv env2 = env.copyAndUpdateContinueBreak(trueEndBlock, falseBranch).copyAndGiveUniquePropertyNodes(AbstractNode.NO_VALUE);
		BasicBlock trueBlock = translateNode(env2, trueBranch, sn.getLastChild(), resultReg, false); // FIXME: exceptions (and labeled break/continue!) should flow through a separate EndForInNode (see flowgraph_builder0187.js)
		
		// connect the basic blocks
		trueBlock.addSuccessor(trueEndBlock);
		bumpToFrontOfPending(env, trueEndBlock);
		bumpToFrontOfPending(env, falseBranch);
		EndForInNode ef = new EndForInNode(bf, lastSrcLoc);
		bf.setEndNode(ef);
		trueEndBlock.addNode(ef);
		trueEndBlock.addSuccessor(condBlock);

		falseBranch.setExceptionHandler(bb.getExceptionHandler()); // TODO: is this step necessary?

		return falseBranch;
	}
	
	/**
	 * Creates a new basic block that joins trueBlock and falseBlock; typically used for if and similar constructs.
	 */
	private static BasicBlock newJoinBasicBlock(FlowGraphEnv env, BasicBlock trueBlock, BasicBlock falseBlock) {
		BasicBlock joinBlock = newBasicBlock(env, trueBlock);
		if (!hasSpecialSuccessors(env, trueBlock))
			trueBlock.addSuccessor(joinBlock);
		if (!hasSpecialSuccessors(env, falseBlock))
			falseBlock.addSuccessor(joinBlock);
		return joinBlock;
	}

	/**
	 * Helper function that iterates over the successors of the block and see if 
	 * they are the continue or break blocks of env.
	 * Used by {@see #newJoinBasicBlock(FlowGraphEnv, BasicBlock, BasicBlock)}.
	 */
	private static boolean hasSpecialSuccessors(FlowGraphEnv env, BasicBlock bb) {
		for (BasicBlock b : bb.getSuccessors())
			if (b == env.continueBlock || b == env.breakBlock)
				return true;
		return false;
	}

	/**
	 * Traverses the children of the blocks and marks the duplicates 
	 * by calling {@link dk.brics.tajs.flowgraph.AbstractNode#setDuplicateOf(AbstractNode)}.
	 * 
	 * @param copyBlocks collection of all the copy blocks
	 * @param seenBlocks blocks already visited in the traversal (initially empty)
	 * @param copyBlock head of the copy blocks
	 * @param origBlock head of the original blocks
	 */
	private void setDuplicateBlocks(List<BasicBlock> copyBlocks, List<BasicBlock> seenBlocks, BasicBlock copyBlock, BasicBlock origBlock) {
		if (!copyBlocks.contains(copyBlock) || seenBlocks.contains(copyBlock)) // TODO: use sets instead of lists
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
	 * Used by {@link #setDuplicateBlocks(List, List, BasicBlock, BasicBlock)}.
	 */
	private static void setDuplicateNodes(BasicBlock copyBlock, BasicBlock origBlock) {
		Iterator<AbstractNode> ci = copyBlock.getNodes().iterator();
		Iterator<AbstractNode> oi = origBlock.getNodes().iterator();
		while (oi.hasNext()) {
			AbstractNode on = oi.next();
			if (!ci.hasNext())
				break;
			AbstractNode cn = ci.next();
			if (!on.getSourceLocation().equals(cn.getSourceLocation()) && oi.hasNext()) // TODO: why is this check necessary?
				on = oi.next();
			cn.setDuplicateOf(on);
		}
	}

	/**
	 * Creates a new basic block as a successor of the given basic block.
	 */
	private static BasicBlock newSuccessorBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = newBasicBlock(env, bb);
		bb.addSuccessor(bb2);
		return bb2;
	}

	/**
	 * Creates a new basic block that becomes the exception handler for the given basic block.
	 */
	private static BasicBlock newCatchBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = newBasicBlock(env, bb);
		bb.setExceptionHandler(bb2);
		return bb2;
	}

	/**
	 * Creates a new basic block.
	 * The new basic block is added to the pending list.
	 * The exception handler is inherited from the given basic block.
	 * If using copy blocks, the new basic block is also added to the copy blocks.
	 */
	private static BasicBlock newBasicBlock(FlowGraphEnv env, BasicBlock bb) {
		BasicBlock bb2 = new BasicBlock(env.fun);
		bb2.setExceptionHandler(bb.getExceptionHandler());
		env.pendingBBs.add(bb2);
		if (env.copyBlocks != null)
			env.copyBlocks.add(bb2);
		return bb2;
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

	/**
	 * Returns the next available register number.
	 */
	private int nextRegister(FlowGraphEnv env) {
		maxReg += env.nextReg > maxReg ? 1 : 0;
		return env.nextReg++;
	}

	/**
	 * Returns the next available register number and adds it to the given collection.
	 */
	private int nextRegister(FlowGraphEnv env, Collection<Integer> l) {
		int tmpReg = nextRegister(env);
		l.add(tmpReg);
		return tmpReg;
	}

	/**
	 * Moves bb from wherever it is in the pending queue to the front of the queue.
	 */
	private static void bumpToFrontOfPending(FlowGraphEnv env, BasicBlock bb) { // TODO: why is this done?
		env.pendingBBs.remove(bb);
		env.pendingBBs.add(bb);
	}

	/**
	 * Initializes flow graph segment for a function.
	 * Creates the basic blocks for declarations, ordinary return, and exceptional return.
	 * The caller is responsible for adding retBB and exceptionretBB to the pending blocks after the function body is created.
	 * 
	 * @param env environment
	 * @param fun newly created function object
	 * @return new basic block for construction of the function body
	 */
	private static BasicBlock setupFunction(FlowGraphEnv env, Function fun) {
		BasicBlock entryBB = new BasicBlock(fun);
		final ConstantNode returnUndefinedByDefault = ConstantNode.makeUndefined(AbstractNode.RETURN_REG, fun.getSourceLocation());
		returnUndefinedByDefault.setArtificial();
		entryBB.addNode(returnUndefinedByDefault);

		// Declarations basic block
		env.declsBB = entryBB; // TODO: could merge the declsBB and its successor after the function has been completed?
		env.pendingBBs.add(entryBB);
		if (env.copyBlocks != null)
			env.copyBlocks.add(entryBB);

		// Exceptional exit (in a singleton basic block)
		BasicBlock exceptionretBB = newCatchBasicBlock(env, entryBB);
		// We like to read our functions in a linear fashion, so remove the block from the pending queue and add it
		// in the caller of setupFunction instead.
		env.pendingBBs.remove(exceptionretBB);
		env.exceptionretBB = exceptionretBB;
		final ExceptionalReturnNode expeptionalReturn = new ExceptionalReturnNode(fun.getSourceLocation());
		expeptionalReturn.setArtificial();
		exceptionretBB.addNode(expeptionalReturn);

		BasicBlock bb = newSuccessorBasicBlock(env, entryBB);

		// Normal exit node (in a singleton basic block)
		BasicBlock retBB = newBasicBlock(env, bb);
		// We like to read our functions from top to bottom, so add the exit block second to last in the caller instead.
		env.pendingBBs.remove(retBB);
		env.retBB = retBB;
		final ReturnNode returnNode = new ReturnNode(AbstractNode.RETURN_REG, fun.getSourceLocation());
		returnNode.setArtificial();
		retBB.addNode(returnNode);

		fun.setEntry(entryBB);
		fun.setExceptionalExit(exceptionretBB);
		fun.setOrdinaryExit(retBB);
		env.fg.addFunction(fun);
		return bb;
	}
}

/**
 * The environment of the flow graph builder which is internal to the module.
 * Flows *downwards*. It's really just a record, so all the fields are
 * public. That does not mean you should mutate the fields though, copy them!
 */
class FlowGraphEnv {
	
	/**
	 * Flow graph being constructed.
	 */
	public FlowGraph fg;

	/**
	 * Basic blocks of functions to be added to the flow graph (if not empty).
	 */
	public List<List<BasicBlock>> pendingFuns;
	
	/**
	 * Basic blocks to be added to the flow graph (if not empty).
	 */
	public List<BasicBlock> pendingBBs;
		
	/**
	 * Current flow graph function object.
	 */
	public Function fun;
	
	/**
	 * Block to go to in case of a 'break'.
	 */
	public BasicBlock breakBlock;
	
	/**
	 * Block to go to in case of a 'continue'.
	 */
	public BasicBlock continueBlock;
	
	/**
	 * Basic block containing variable/function declarations.
	 */
	public BasicBlock declsBB;
	
	/**
	 * Basic block containing the ordinary function return node.
	 */
	public BasicBlock retBB;
	
	/**
	 * Basic block containing the exceptional function return node.
	 */
	public BasicBlock exceptionretBB;
	
	/**
	 * Copy of constructed basic blocks, for 'finally' blocks and Options.isUnrollOneAndAHalfEnabled.
	 * If null, don't make a copy.
	 */
	public List<BasicBlock> copyBlocks;
	
	/**
	 * Next free register number.
	 */
	public int nextReg;
	
	public Pair<String, Integer> evalResultMap; // TODO: javadoc (for unevalizer)
	
	/**
	 * Register to use for result base value at ReadVariableNodes.
	 */
	public int baseReg;

	/**
	 * Map from register to last WritePropertyNode having that register as base register
	 * or to last ReadPropertyNode or ReadVariableNode having that register as result register.
	 * Used for AssumeNodes and CallNodes.
	 */
	public Map<Integer, AbstractNode> propertyNodes; // TODO: explain more about what this is used for

	/**
	 * Map from register to last LoadNode having that register as result register.
	 * Used for AssumeNodes. Is subsumed by the {@link #propertyNodes} map.
	 */
	public Map<Integer, LoadNode> loadNodes;
	
	private FlowGraphEnv(FlowGraph fg, List<List<BasicBlock>> pendingFuns, List<BasicBlock> pendingBBs, Map<Integer, AbstractNode> propertyNodes, Map<Integer, LoadNode> loadNodes, Function fun,
			BasicBlock breakBlock, BasicBlock continueBlock, BasicBlock declsBB, BasicBlock retBB, BasicBlock exceptionretBB, List<BasicBlock> copyBlocks,
			int nextReg, int baseReg, Pair<String, Integer> evalResultMap) {
		this(fg, pendingFuns, pendingBBs, fun);
		this.propertyNodes = propertyNodes;
		this.loadNodes = loadNodes;
		this.breakBlock = breakBlock;
		this.continueBlock = continueBlock;
		this.declsBB = declsBB;
		this.retBB = retBB;
		this.exceptionretBB = exceptionretBB;
		this.copyBlocks = copyBlocks;
		this.nextReg = nextReg;
		this.baseReg = baseReg;
		this.evalResultMap = evalResultMap;
	}	

	/**
	 * Constructs a new environment.
	 * 
	 * @param fg current flow graph
	 * @param pendingFuns current pending functions
	 * @param pendingBBs current pending basic blocks
	 * @param fun current function
	 */
	public FlowGraphEnv(FlowGraph fg, List<List<BasicBlock>> pendingFuns, List<BasicBlock> pendingBBs, Function fun) {
		this.fg = fg;
		this.pendingFuns = pendingFuns;
		this.pendingBBs = pendingBBs;
		this.propertyNodes = newMap();
		this.loadNodes = newMap();
		this.fun = fun;
		this.breakBlock = null;
		this.continueBlock = null;
		this.declsBB = null;
		this.retBB = null;
		this.exceptionretBB = null;
		this.copyBlocks = null;
		this.nextReg = AbstractNode.FIRST_ORDINARY_REG;
		this.evalResultMap = null;
		this.baseReg = AbstractNode.NO_VALUE;
	}

	/**
	 * Constructs a copy of this environment but updating the continue block and the break block.
	 */
	public FlowGraphEnv copyAndUpdateContinueBreak(BasicBlock newContinueBlock, BasicBlock newBreakBlock) {
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, loadNodes, fun, newBreakBlock, newContinueBlock, declsBB, retBB, exceptionretBB, copyBlocks, nextReg, baseReg, evalResultMap);
	}

	/**
	 * Constructs a copy of this environment but updating the base register.
	 */
	public FlowGraphEnv copyAndUpdateBaseReg(int newBaseReg) {
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, loadNodes, fun, breakBlock, continueBlock, declsBB, retBB, exceptionretBB, copyBlocks, nextReg, newBaseReg, evalResultMap);
	}

	/**
	 * Constructs a copy of this environment but with its own property node map.
	 * @param disqualifiedNode if no NO_VALUE, this register is removed from the new map
	 */
	public FlowGraphEnv copyAndGiveUniquePropertyNodes(int disqualifiedNode) {
		Map<Integer, AbstractNode> pnodes = newMap(propertyNodes);
		Map<Integer, LoadNode> lnodes = newMap(loadNodes);
		if (disqualifiedNode != AbstractNode.NO_VALUE){
			pnodes.remove(disqualifiedNode);
			lnodes.remove(disqualifiedNode);
		}
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, pnodes, lnodes, fun, breakBlock, continueBlock, declsBB, retBB, exceptionretBB, copyBlocks, nextReg, baseReg, evalResultMap);
	}

	/**
	 * Constructs a copy of this environment but with a new fresh list of copy blocks initially containing the given block.
	 */
	public FlowGraphEnv copyAndInitializeForCopy(BasicBlock initBlock) {
		List<BasicBlock> newCopyBlocks = newList();
		newCopyBlocks.add(initBlock);
		return new FlowGraphEnv(fg, pendingFuns, pendingBBs, propertyNodes, loadNodes, fun, breakBlock, continueBlock, declsBB, retBB, exceptionretBB, newCopyBlocks, nextReg, baseReg, evalResultMap);
	}
	
	public void registerPropertyWrite(WritePropertyNode node){
		this.propertyNodes.put(node.getBaseRegister(), node);
	}
	
	public void registerRegisterLoad(LoadNode node){
		this.propertyNodes.put(node.getResultRegister(), node);
		this.loadNodes.put(node.getResultRegister(), node);
	}
}
