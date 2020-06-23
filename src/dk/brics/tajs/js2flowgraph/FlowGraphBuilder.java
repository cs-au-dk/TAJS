/*
 * Copyright 2009-2020 Aarhus University
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

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.parsing.parser.IdentifierToken;
import com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree.Kind;
import com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.SourceLocation.SourceLocationMaker;
import dk.brics.tajs.flowgraph.SourceLocation.SyntheticLocationMaker;
import dk.brics.tajs.flowgraph.TAJSFunctionName;
import dk.brics.tajs.flowgraph.ValueLogLocationInformation;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.CatchNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode.Type;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.flowgraph.jsnodes.ThrowNode;
import dk.brics.tajs.flowgraph.syntaticinfo.RawSyntacticInformation;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.ParseResult;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.SyntaxMesssage;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.ParseError;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.addNodeToBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeCatchBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSourceLocation;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSuccessorBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setupFunction;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static org.apache.log4j.Logger.getLogger;

/**
 * Converter from JavaScript source code to flow graphs.
 * The order the sources are provided is significant.
 */
@SuppressWarnings("FieldCanBeLocal")
public class FlowGraphBuilder {

    private static final Logger log = getLogger(FlowGraphBuilder.class);

    private static final boolean showParserWarnings = false;

    private final Mode mode = Mode.ES5; // TODO: (#3) currently ES5 mode

    private final boolean strict = false;

    private final JavaScriptParser parser;

    private final FunctionAndBlockManager functionAndBlocksManager;

    private final AstEnv initialEnv;

    private boolean closed = false;

    private TranslationResult processed;

    private ASTInfo astInfo;

    private final RawSyntacticInformation syntacticInformation;

    private final ValueLogLocationInformation valueLogMappingInformation;

    /**
     * Constructs a flow graph builder.
     * @param env traversal environment
     * @param fab function/block manager
     */
    public FlowGraphBuilder(AstEnv env, FunctionAndBlockManager fab) {
        assert env != null;
        assert fab != null;
        functionAndBlocksManager = fab;
        astInfo = new ASTInfo();
        initialEnv = env;
        parser = new JavaScriptParser(mode, strict);
        processed = TranslationResult.makeAppendBlock(initialEnv.getAppendBlock());
        syntacticInformation = new RawSyntacticInformation();
        valueLogMappingInformation = new ValueLogLocationInformation();
    }

    /**
     * Transforms the given stand-alone JavaScript source code and appends it to the main function.
     */
    public Function transformStandAloneCode(String source, SourceLocationMaker sourceLocationMaker) {
        return transformCode(source, 0, 0, sourceLocationMaker);
    }

    /**
     * Transforms the given JavaScript source code and appends it to the main function, with location offsets.
     * The location offsets are used for setting the source locations in the flow graph.
     *
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     */
    Function transformCode(String source, int lineOffset, int columnOffset, SourceLocationMaker sourceLocationMaker) {
        final AstEnv env = initialEnv.makeAppendBlock(processed.getAppendBlock());
        ProgramTree t = makeAST(source, lineOffset, columnOffset, sourceLocationMaker);
        processed = new FunctionBuilder(astInfo, functionAndBlocksManager, sourceLocationMaker, makeSyntacticAnalysis()).process(t, env);
        return processed.getAppendBlock().getFunction();
    }

    /**
     * Parses the given JavaScript code.
     */
    private ProgramTree makeAST(String sourceContent, int lineOffset, int columnOffset, SourceLocationMaker sourceLocationMaker) {
        if (closed) {
            throw new RuntimeException("Already closed.");
        }

        // add line/column offsets (a bit hacky - but it avoids other silly encodings or extra fields)
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < lineOffset; i++) {
            s.append("\n");
        }
        for (int i = 0; i < columnOffset; i++) {
            s.append(" ");
        }
        s.append(sourceContent);

        ParseResult parseResult = parser.parse(s.toString(), sourceLocationMaker);
        reportParseMessages(parseResult);
        astInfo.updateWith(parseResult.getProgramAST());
        return parseResult.getProgramAST();
    }

    /**
     * Reports parse errors and warnings to the log.
     *
     * @throws ParseError if the parse result contains a parse error
     */
    private static void reportParseMessages(ParseResult parseResult) {
        if (!parseResult.getErrors().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (SyntaxMesssage error : parseResult.getErrors()) {
                sb.append(String.format("%n%s: Syntax error: %s", error.getSourceLocation().toString(), error.getMessage()));
            }
            throw new ParseError(sb.toString());
        }
        if (showParserWarnings) {
            for (SyntaxMesssage warning : parseResult.getWarnings()) {
                log.warn(String.format("%s: Parser warning: %s", warning.getSourceLocation().toString(), warning.getMessage()));
            }
        }
    }

    /**
     * Transforms the given web application JavaScript source code.
     */
    public Function transformWebAppCode(JavaScriptSource s, SourceLocationMaker sourceLocationMaker) {
        switch (s.getKind()) {

            case FILE: { // TODO: (#119) processing order of external JavaScript files (sync/async loading...)
                // TODO: (#119) should be added as a load event, but that does not work currently...
                // Function function = processFunctionBody(Collections.<String>newList(), s.getFileName(), s.getJavaScript(), 0, initialEnv);
                // eventHandlers.add(Pair.make(function, EventHandlerKind.DOM_LOAD));
                return transformStandAloneCode(s.getCode(), sourceLocationMaker);
            }

            case EMBEDDED: { // TODO: (#119) currently ignoring events during page load (unsound)
                return transformCode(s.getCode(), s.getLineOffset(), s.getColumnOffset(), sourceLocationMaker);
            }

            case EVENTHANDLER: {
                Function function = transformFunctionBody(s.getCode(), s.getLineOffset(), s.getColumnOffset(), initialEnv, sourceLocationMaker);
                function.getNode().setDomEventType(s.getEventKind());
                return function;
            }
            default:
                throw new AnalysisException("Unhandled case: " + s.getKind());
        }
    }

    /**
     * Transforms the given code as a function body.
     *
     * @return the new function
     */
    private Function transformFunctionBody(String sourceContent, int lineOffset, int columnOffset, AstEnv env, SourceLocationMaker sourceLocationMaker) {
        ProgramTree tree = makeAST(sourceContent, lineOffset, columnOffset, sourceLocationMaker);
        FormalParameterListTree params = new FormalParameterListTree(tree.location, ImmutableList.of());
        return new FunctionBuilder(astInfo, functionAndBlocksManager, sourceLocationMaker, makeSyntacticAnalysis()).processFunctionDeclaration(Kind.DECLARATION, null, params, tree, env, makeSourceLocation(tree, sourceLocationMaker), null);
    }

    /**
     * Completes the flow graph construction.
     * This will:
     * <ul>
     * <li>Add the event dispatcher loop, if DOM mode enabled.
     * <li>Set the block order on each basic block (used by the analysis worklist).
     * <li>Set the index on each basic block and node.
     * <li>Return the resulting flow graph.
     * </ul>
     */
    public FlowGraph close() {
        FlowGraph flowGraph = close(null, initialEnv.getFunction().getOrdinaryExit());
        if (Options.get().isTestFlowGraphBuilderEnabled())
            log.info("fg2: " + flowGraph);
        flowGraph.check();
        return flowGraph;
    }

    /**
     * Completes the flow graph construction.
     *
     * @param flowGraph existing flow graph if extending, or null if creating a new
     * @param exitBlock the basic block where the last processed block will exit to
     * @see #close()
     */
    public FlowGraph close(FlowGraph flowGraph, BasicBlock exitBlock) {
        closed = true;

        if (flowGraph == null) {
            processed = addEventDispatchers(initialEnv.getFunction().getEntry().getSourceLocation(), initialEnv.makeAppendBlock(processed.getAppendBlock()));
        }

        if (flowGraph == null) {
            flowGraph = new FlowGraph(initialEnv.getFunction());
        }
        flowGraph.addSyntacticInformation(syntacticInformation, valueLogMappingInformation);

        int origBlockCount = flowGraph.getNumberOfBlocks();
        int origNodeCount = flowGraph.getNumberOfNodes();

        // wire the last processed block to the exit
        if (exitBlock != null) {
            processed.getAppendBlock().addSuccessor(exitBlock);
        }

        Pair<List<Function>, List<BasicBlock>> blocksAndFunctions = functionAndBlocksManager.close();

        for (Function f : blocksAndFunctions.getFirst()) {
            flowGraph.addFunction(f);
        }
        flowGraph.getFunctions().forEach(f -> setEntryBlocks(f, functionAndBlocksManager));

        // bypass empty basic blocks
        boolean changed;
        do {
            changed = false;
            for (BasicBlock b1 : blocksAndFunctions.getSecond()) {
                for (BasicBlock b2 : newList(b1.getSuccessors())) {
                    // b1 has an ordinary edge to b2
                    if (b2.isEmpty()) {
                        // b2 is empty, bypass it
                        b1.removeSuccessor(b2);
                        for (BasicBlock b3 : b2.getSuccessors()) {
                            b1.addSuccessor(b3);
                            if (!b1.isEmpty() && b1.getLastNode() instanceof IfNode) {
                                // 'if' nodes have their own successors
                                IfNode ifn = (IfNode) b1.getLastNode();
                                BasicBlock succTrue = ifn.getSuccTrue();
                                BasicBlock succFalse = ifn.getSuccFalse();
                                if (b2 == succTrue && b2 == succFalse) {
                                    ifn.setSuccessors(b3, b3);
                                } else if (b2 == succTrue)
                                    ifn.setSuccessors(b3, succFalse);
                                else if (b2 == succFalse)
                                    ifn.setSuccessors(succTrue, b3);
                            }
                        }
                        changed = true; // b3 may itself be empty, so repeat (naive but fast enough in practice)
                    }
                }
            }
        } while (changed);

        // add each non-empty basic block to the flow graph
        for (BasicBlock b : blocksAndFunctions.getSecond()) {
            if (!b.isEmpty()) {
                flowGraph.addBlock(b);
            }
        }

        // complete links from end-for-in nodes to begin-for-in nodes (cannot be done at constructor time due to later cloning)
        Collection<EndForInNode> ends = newList();
        for (Function f : flowGraph.getFunctions()) {
            for (BasicBlock b : f.getBlocks()) {
                for (AbstractNode n : b.getNodes()) {
                    if (n instanceof EndForInNode) {
                        ends.add((EndForInNode) n);
                    }
                    if (n instanceof BeginForInNode) {
                        ((BeginForInNode) n).setEndNodes(Collections.newSet());
                    }
                }
            }
        }
        for (EndForInNode end : ends) {
            end.getBeginNode().getEndNodes().add(end);
        }

        // set block orders
        flowGraph.complete();

        // Avoid changes to block- & node-indexes due to a change in a hostenv-source.
        // (dynamically added code from eval et. al will still change)
        List<Function> sortedFunctions = newList(flowGraph.getFunctions());
        FlowGraph finalFlowGraph = flowGraph;
        sortedFunctions.sort((f1, f2) -> {
            boolean f1host = finalFlowGraph.isHostEnvironmentSource(f1.getSourceLocation());
            boolean f2host = finalFlowGraph.isHostEnvironmentSource(f2.getSourceLocation());
            if (f1host != f2host) {
                return f1host ? 1 : -1;
            }
            return SourceLocation.Comparator.compareStatic(f1.getSourceLocation(), f2.getSourceLocation());
        });
        int nodeCount = origNodeCount;
        int blockCount = origBlockCount;
        for (Function function : sortedFunctions) {
            List<BasicBlock> blocks = newList(function.getBlocks());
            blocks.sort(Comparator.comparingInt(BasicBlock::getTopologicalOrder));

            for (BasicBlock block : blocks) {
                if (block.getIndex() == -1) {
                    block.setIndex(blockCount++);
                }
                for (AbstractNode n : block.getNodes())
                    if (n.getIndex() == -1) {
                        n.setIndex(nodeCount++);
                    }
            }
        }

        return flowGraph;
    }

    /**
     * Sets the entry block of every block in a function.
     */
    private static void setEntryBlocks(Function f, FunctionAndBlockManager functionAndBlocksManager) {
        Stack<BasicBlock> entryStack = new Stack<>();
        entryStack.push(f.getEntry());

        setEntryBlocks(new TripleForSetEntryBlocksWorklist(null, f.getEntry(), entryStack), newSet(), functionAndBlocksManager);

        // needed if the blocks are unreachable
        f.getOrdinaryExit().setEntryBlock(f.getEntry());
        f.getExceptionalExit().setEntryBlock(f.getEntry());
    }

    public static final class TripleForSetEntryBlocksWorklist {

        private final BasicBlock predecessor;

        private final BasicBlock target;

        private final Stack<BasicBlock> entryStack;

        public TripleForSetEntryBlocksWorklist(BasicBlock predecessor, BasicBlock target, Stack<BasicBlock> entryStack) {
            this.predecessor = predecessor;
            this.target = target;
            this.entryStack = entryStack;
        }
    }

    /**
     * Recursively sets BasicBlock.entry_block
     * All blocks between "Begin" and "End" nodes form a region with a changed entry block, see {@link BasicBlock#getEntryBlock()}.
     */
    public static void setEntryBlocks(TripleForSetEntryBlocksWorklist startingPoint, Set<BasicBlock> visited, FunctionAndBlockManager functionAndBlocksManager) {
        LinkedList<TripleForSetEntryBlocksWorklist> worklist = new LinkedList<>();
        worklist.add(startingPoint);
        while (!worklist.isEmpty()) {
            TripleForSetEntryBlocksWorklist current = worklist.removeFirst();
            if (visited.contains(current.target)) {
                continue;
            }
            visited.add(current.target);

            Stack<BasicBlock> successorEntryStack = new Stack<>();
            successorEntryStack.addAll(current.entryStack);
            Stack<BasicBlock> exceptionEntryStack = new Stack<>();
            exceptionEntryStack.addAll(current.entryStack);
            current.target.setEntryBlock(successorEntryStack.peek());
            if (current.target == current.target.getEntryBlock()) {
                current.target.setEntryPredecessorBlock(current.predecessor);
            }
            if (!current.target.isEmpty()) {
                AbstractNode lastNode = current.target.getLastNode();
                if (Options.get().isForInSpecializationEnabled()) {
                    if (lastNode instanceof BeginForInNode) {
                        successorEntryStack.push(current.target.getSingleSuccessor());
                    }
                    if (lastNode instanceof EndForInNode) {
                        successorEntryStack.pop();
                        exceptionEntryStack.pop();
                    }
                }
            }
            if (successorEntryStack.isEmpty()) {
                throw new AnalysisException("Empty entry_block stack due to " + current.target);
            }
            Set<BasicBlock> unvisitedSuccessors = newSet();
            unvisitedSuccessors.addAll(current.target.getSuccessors());
            unvisitedSuccessors.addAll(functionAndBlocksManager.getUnreachableSyntacticSuccessors(current.target));
            unvisitedSuccessors.removeAll(visited);
            unvisitedSuccessors.remove(null);
            worklist.addAll(unvisitedSuccessors.stream().map(b -> new TripleForSetEntryBlocksWorklist(current.target, b, successorEntryStack)).collect(Collectors.toList()));
            BasicBlock exceptionHandler = current.target.getExceptionHandler();
            if (exceptionHandler != null && !visited.contains(exceptionHandler)) {
                worklist.add(new TripleForSetEntryBlocksWorklist(current.target, exceptionHandler, exceptionEntryStack));
            }
        }
    }

    /**
     * Creates the nodes responsible for execution of registered event-handlers.
     */
    private TranslationResult addEventDispatchers(SourceLocation location, AstEnv env) {
        BasicBlock appendBlock = env.getAppendBlock();
        boolean needsEventLoop = Options.get().isDOMEnabled() || Options.get().isAsyncEventsEnabled();
        if (needsEventLoop) {
            appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);

            BasicBlock weakCatcher = injectCatcherForFunction(location, env);
            if (!Options.get().getUnsoundness().isIgnoreEventsAfterExceptions() &&
                    !Options.get().isNodeJS()) // in Node.js, uncaught exceptions during initialization prevent event processing
                weakCatcher.addSuccessor(appendBlock);

            NopNode nopEntryNode = new NopNode("eventDispatchers: entry", location);
            nopEntryNode.setArtificial();
            appendBlock.addNode(nopEntryNode);

            if (Options.get().isDOMEnabled()) {
                BasicBlock lastDOMContentLoadedEventLoopBlock = addAsyncBlocks(Type.DOM_CONTENT_LOADED, true, "DOMContentLoaded", location, env.makeAppendBlock(appendBlock));
                BasicBlock lastLoadEventLoopBlock = addAsyncBlocks(Type.DOM_LOAD, true, "Load", location, env.makeAppendBlock(lastDOMContentLoadedEventLoopBlock));
                BasicBlock lastOtherEventLoopBlock = addAsyncBlocks(Type.DOM_OTHER, !Options.get().isTypeCheckEnabled(), "Other", location, env.makeAppendBlock(lastLoadEventLoopBlock));
                if (Options.get().isTypeCheckEnabled()) {
                    lastOtherEventLoopBlock = addAsyncBlocks(Type.TYPE_TESTS, false, "TypeTests", location, env.makeAppendBlock(lastOtherEventLoopBlock));
                    lastOtherEventLoopBlock.addSuccessor(lastLoadEventLoopBlock);
                }
                BasicBlock lastUnloadEventLoopBlock = addAsyncBlocks(Type.DOM_UNLOAD, true, "Unload", location, env.makeAppendBlock(lastOtherEventLoopBlock));
                appendBlock = lastUnloadEventLoopBlock;
            } else if (Options.get().isAsyncEventsEnabled()) {
                appendBlock = addAsyncBlocks(Type.ASYNC, true, "Async", location, env.makeAppendBlock(appendBlock));
            }
        }
        return TranslationResult.makeAppendBlock(appendBlock);
    }

    /**
     * Injects an intermediary block before the exceptional exit of a function which can be used to maybe-ignore the exceptional data flow.
     *
     * @return block which maybe-ignores the exceptional dataflow of the function
     */
    private BasicBlock injectCatcherForFunction(SourceLocation location, AstEnv env) {
        // Implementation note: this replaces the current exceptional exit of the function.
        // Alternative strategies are:
        // (A) rewire all blocks with the current excepitonal exit as exception handler (this is a different hacky solution)
        // (B) use intermediary exception handler while constructing a function, and wire that handler and the exceptional exit together when done building the function (this is not hacky, but requires some refactorings)

        BasicBlock catchBlock = env.getFunction().getExceptionalExit();
        List<AbstractNode> exciptionalExitNodes = newList(catchBlock.getNodes());
        catchBlock.getNodes().clear();
        BasicBlock rethrowBlock = makeBasicBlock(env.getFunction(), functionAndBlocksManager);
        catchBlock.addSuccessor(rethrowBlock);
        BasicBlock newExceptionalExit = makeBasicBlock(env.getFunction(), functionAndBlocksManager);
        newExceptionalExit.getNodes().addAll(exciptionalExitNodes);
        rethrowBlock.setExceptionHandler(newExceptionalExit);
        env.getFunction().setExceptionalExit(newExceptionalExit);

        int catchRegister = env.getRegisterManager().nextRegister();
        CatchNode catchNode = new CatchNode(catchRegister, location);
        ThrowNode rethrowNode = new ThrowNode(catchRegister, location);
        catchNode.setArtificial();
        rethrowNode.setArtificial();
        addNodeToBlock(catchNode, catchBlock, env.makeStatementLevel(false));
        addNodeToBlock(rethrowNode, rethrowBlock, env);

        return catchBlock;
    }

    /**
     * Appends the required blocks for handling asyncronous dataflow in the flowgraph.
     * The blocks contains an event dispatcher for the chosen event type.
     */
    private BasicBlock addAsyncBlocks(Type eventType, boolean loop, String prettyEventName, SourceLocation location, AstEnv env) {
        Function fun = initialEnv.getFunction();
        BasicBlock appendBlock = env.getAppendBlock();
        // TODO support fire-once, but in unknown order handlers (e.g. load)

        // block structure
        BasicBlock first = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);
        BasicBlock ordinaryExit = makeSuccessorBasicBlock(first, functionAndBlocksManager);
        BasicBlock exceptionalExit = makeCatchBasicBlock(first, functionAndBlocksManager);
        BasicBlock exceptionalExitRethrow = makeSuccessorBasicBlock(exceptionalExit, functionAndBlocksManager);
        // TODO minor optimization: make ordinaryExit and last the same block (also skips a node)
        BasicBlock last = makeSuccessorBasicBlock(ordinaryExit, functionAndBlocksManager);

        // will continue execution ...
        exceptionalExit.addSuccessor(last);
        exceptionalExit.setExceptionHandler(null);
        // ... but the global exceptional exit also gets flow (for error reporting only)
        exceptionalExitRethrow.setExceptionHandler(fun.getExceptionalExit());

        appendBlock.addSuccessor(last); // may skip loop entirely
        if (loop) {
            last.addSuccessor(first); // back loop
        }

        // nodes
        EventDispatcherNode dispatcherNode = new EventDispatcherNode(eventType, location);
        NopNode nopOrdinaryExitNode = new NopNode("eventDispatchers: ordinary exit " + prettyEventName, location);
        NopNode nopExceptionExitNode = new NopNode("eventDispatchers: exceptional exit " + prettyEventName, location);
        int catchRegister = env.getRegisterManager().nextRegister();
        CatchNode catchNode = new CatchNode(catchRegister, location);
        ThrowNode rethrowNode = new ThrowNode(catchRegister, location);
        NopNode nopLastNode = new NopNode("eventDispatchers: post " + prettyEventName, location);
        nopOrdinaryExitNode.setArtificial();
        nopExceptionExitNode.setArtificial();
        catchNode.setArtificial();
        rethrowNode.setArtificial();
        nopLastNode.setArtificial();
        addNodeToBlock(dispatcherNode, first, env);
        addNodeToBlock(nopOrdinaryExitNode, ordinaryExit, env);
        addNodeToBlock(catchNode, exceptionalExit, env.makeStatementLevel(false));
        addNodeToBlock(nopExceptionExitNode, exceptionalExit, env.makeStatementLevel(false));
        addNodeToBlock(rethrowNode, exceptionalExitRethrow, env);
        addNodeToBlock(nopLastNode, last, env);

        return last;
    }

    public ASTInfo getAstInfo() {
        return astInfo;
    }

    /**
     * Creates a call to a function that defines and calls functions containing the host function sources.
     *
     * @see HostEnvSources
     */
    public void addLoadersForHostFunctionSources(List<URL> sources) {
        if (sources.isEmpty()) {
            return;
        }
        // make loader
        AstEnv mainEnv = this.initialEnv;
        SourceLocation loaderDummySourceLocation = new SyntheticLocationMaker("host-environment-sources-loader").makeUnspecifiedPosition();

        BasicBlock appendBlock = mainEnv.getFunction().getEntry().getSingleSuccessor();
        for (URL source : sources) {
            appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);
            int sourceRegister = mainEnv.getRegisterManager().nextRegister();
            int internalRegister = mainEnv.getRegisterManager().nextRegister();
            int loadedFunctionRegister = mainEnv.getRegisterManager().nextRegister();

            ConstantNode sourceStringNode = ConstantNode.makeString(source.toString(), sourceRegister, loaderDummySourceLocation);
            ConstantNode internalFlagNode = ConstantNode.makeBoolean(true, internalRegister, loaderDummySourceLocation);
            CallNode callLoadNode = new CallNode(loadedFunctionRegister, TAJSFunctionName.TAJS_LOAD, newList(Arrays.asList(sourceRegister, internalRegister)), loaderDummySourceLocation);
            CallNode callLoadedNode = new CallNode(false, AbstractNode.NO_VALUE, AbstractNode.NO_VALUE, loadedFunctionRegister, newList(), loaderDummySourceLocation);

            addNodeToBlock(sourceStringNode, appendBlock, mainEnv.makeStatementLevel(false));
            addNodeToBlock(internalFlagNode, appendBlock, mainEnv.makeStatementLevel(false));
            appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);
            addNodeToBlock(callLoadNode, appendBlock, mainEnv.makeStatementLevel(false));
            appendBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);
            addNodeToBlock(callLoadedNode, appendBlock, mainEnv.makeStatementLevel(false));
        }

        BasicBlock postCallLoaderBlock = makeSuccessorBasicBlock(appendBlock, functionAndBlocksManager);
        processed = TranslationResult.makeAppendBlock(postCallLoaderBlock);
    }

    /**
     * Creates a Function for the given source.
     * <p>
     * <pre>
     *  function (parameters[0], parameters[1], ...) {
     *      SOURCE
     *  }
     * </pre>
     */
    public Function transformFunctionBody(String source, List<String> parameterNames, SourceLocationMaker sourceLocationMaker) {
        final AstEnv env = initialEnv.makeAppendBlock(processed.getAppendBlock());

        // create a synthetic wrapper function with the source as body
        ProgramTree tree = makeAST(source, 0, 0, sourceLocationMaker);
        List<IdentifierExpressionTree> parameters =
                parameterNames.stream().map(
                        n -> new IdentifierExpressionTree(null, new IdentifierToken(null, n)))
                        .collect(Collectors.toList());

        FormalParameterListTree params = new FormalParameterListTree(tree.location, ImmutableList.copyOf(parameters));

        Function function = new FunctionBuilder(astInfo, functionAndBlocksManager, sourceLocationMaker, makeSyntacticAnalysis()).processFunctionDeclaration(Kind.EXPRESSION, null, params, tree, env.makeResultRegister(AbstractNode.NO_VALUE), makeSourceLocation(tree, sourceLocationMaker), source);

        return function;
    }

    public static FlowGraphBuilder makeForMain(SourceLocationMaker sourceLocationMaker) {
        AstEnv env = AstEnv.makeInitial();
        Function main = new Function(null, null, null, sourceLocationMaker.makeUnspecifiedPosition());
        FunctionAndBlockManager fab = new FunctionAndBlockManager();
        env = setupFunction(main, env, fab);
        return new FlowGraphBuilder(env, fab);
    }

    private SyntacticAnalysis makeSyntacticAnalysis() {
        return new SyntacticAnalysis(syntacticInformation, new ValueLogLocationRemapping(valueLogMappingInformation));
    }
}
