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

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree.Kind;
import com.google.javascript.jscomp.parsing.parser.trees.ParseTree;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode.Type;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.ParseResult;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.SyntaxMesssage;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.ParseError;
import org.apache.log4j.Logger;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.addNodeToBlock;
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
public class FlowGraphBuilder {

    private static final Logger log = getLogger(FlowGraphBuilder.class);

    private static final boolean showParserWarnings = false;

    private final Mode mode = Mode.ES5; // TODO: (#3) currently ES5 mode

    private final JavaScriptParser parser;

    private final FunctionAndBlockManager functionAndBlocksManager;

    private final AstEnv initialEnv;

    private boolean closed = false;

    private TranslationResult processed;

    private ASTInfo astInfo;

    private SyntacticHintsCollector syntacticHintsCollector;

    /**
     * Constructs a new flow graph builder using a fresh environment.
     */
    public FlowGraphBuilder(URL location, String prettyFileName) {
        this(null, null, location, prettyFileName);
    }

    /**
     * Constructs a new flow graph builder.
     *  @param env traversal environment, or null if fresh
     * @param fab function/block manager, or null if fresh
     */
    public FlowGraphBuilder(AstEnv env, FunctionAndBlockManager fab, URL location, String prettyFileName) {
        if (fab == null) {
            fab = new FunctionAndBlockManager();
        }
        if (env == null) {
            // prepare an initial AstEnv with a dummy main function
            env = AstEnv.makeInitial();
            Function main = new Function(null, null, null, new SourceLocation(0, 0, prettyFileName, location));
            env = setupFunction(main, env, fab);
        }
        functionAndBlocksManager = fab;
        astInfo = new ASTInfo();
        initialEnv = env;
        parser = new JavaScriptParser(mode);
        processed = TranslationResult.makeAppendBlock(initialEnv.getAppendBlock());
        syntacticHintsCollector = new SyntacticHintsCollector();
    }

    /**
     * Transforms the given stand-alone JavaScript source code and appends it to the main function.
     */
    public Function transformStandAloneCode(JavaScriptSource source) {
        return transformCode(source, 0, 0);
    }

    /**
     * Transforms the given JavaScript source code and appends it to the main function, with location offsets.
     * The location offsets are used for setting the source locations in the flow graph.
     *
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     */
    Function transformCode(JavaScriptSource source, int lineOffset, int columnOffset) {
        final AstEnv env = initialEnv.makeAppendBlock(processed.getAppendBlock());
        ProgramTree t = makeAST(source.getLocation(), source.getPrettyFileName(), source.getCode(), lineOffset, columnOffset);
        processed = new FunctionBuilder(astInfo, functionAndBlocksManager, source.getLocation(), syntacticHintsCollector).process(t, env);
        return processed.getAppendBlock().getFunction();
    }

    /**
     * Parses the given JavaScript code.
     */
    private ProgramTree makeAST(URL location, String prettyFileName, String sourceContent, int lineOffset, int columnOffset) {
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

        ParseResult parseResult = parser.parse(location, prettyFileName, s.toString());
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
    public Function transformWebAppCode(JavaScriptSource s) {
        switch (s.getKind()) {

            case FILE: { // TODO: (#119) processing order of external JavaScript files (sync/async loading...)
                // TODO: (#119) should be added as a load event, but that does not work currently...
                // Function function = processFunctionBody(Collections.<String>newList(), s.getFileName(), s.getJavaScript(), 0, initialEnv);
                // eventHandlers.add(Pair.make(function, EventHandlerKind.DOM_LOAD));
                return transformStandAloneCode(s);
            }

            case EMBEDDED: { // TODO: (#119) currently ignoring events during page load (unsound)
                return transformCode(s, s.getLineOffset(), s.getColumnOffset());
            }

            case EVENTHANDLER: {
                Function function = transformFunctionBody(s.getLocation(), s.getPrettyFileName(), s.getCode(), s.getLineOffset(), s.getColumnOffset(), initialEnv);
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
    private Function transformFunctionBody(URL location, String prettyFileName, String sourceContent, int lineOffset, int columnOffset, AstEnv env) {
        ProgramTree tree = makeAST(location, prettyFileName, sourceContent, lineOffset, columnOffset);
        FormalParameterListTree params = new FormalParameterListTree(tree.location, ImmutableList.of());
        return new FunctionBuilder(astInfo, functionAndBlocksManager, location, syntacticHintsCollector).processFunctionDeclaration(Kind.DECLARATION, null, params, tree, env, makeSourceLocation(tree, location), null);
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
            // assume old flowgraph already has these.

            // TODO: (#129) but this means that there could be added a path to the main-exit that does not go through the event handlers.
            // but that is only a problem if dynamically added blocks are added to (end of) main, instead of as load-events (which is supposed to be fixed elsewhere)!
            addEventDispatchers(initialEnv.getFunction().getEntry().getSourceLocation());
        }

        if (flowGraph == null) {
            flowGraph = new FlowGraph(initialEnv.getFunction());
            flowGraph.setSyntacticHints(syntacticHintsCollector.getSyntacticHints());
        }
        int origBlockCount = flowGraph.getNumberOfBlocks();
        int origNodeCount = flowGraph.getNumberOfNodes();

        // wire the last processed block to the exit
        processed.getAppendBlock().addSuccessor(exitBlock);

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
                        ((BeginForInNode) n).setEndNodes(Collections.<EndForInNode>newSet());
                    }
                }
            }
        }
        for (EndForInNode end : ends) {
            end.getBeginNode().getEndNodes().add(end);
        }

        int blockCount = origBlockCount;
        int nodeCount = origNodeCount;

        // set block orders
        flowGraph.complete();

        // Avoid changes to block- & node-indexes due to a change in a hostenv-source.
        // (dynamically added code from eval et. al will still change)
        List<Function> sortedFunctions = newList(flowGraph.getFunctions());
        java.util.Collections.sort(sortedFunctions, (f1, f2) -> {
            boolean f1host = HostEnvSources.isHostEnvSource(f1.getSourceLocation());
            boolean f2host = HostEnvSources.isHostEnvSource(f2.getSourceLocation());
            if (f1host != f2host) {
                return f1host ? 1 : -1;
            }
            return 0;
        });
        for (Function function : sortedFunctions) {
            List<BasicBlock> blocks = newList(function.getBlocks());
            java.util.Collections.sort(blocks, (o1, o2) -> o1.getOrder() - o2.getOrder());

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

        if (Options.get().isTestFlowGraphBuilderEnabled())
            log.info("fg2: " + flowGraph);
        return flowGraph;
    }

    /**
     * Sets the entry block of every block in a function.
     */
    private static void setEntryBlocks(Function f, FunctionAndBlockManager functionAndBlocksManager) {
        Stack<BasicBlock> entryStack = new Stack<>();
        entryStack.push(f.getEntry());

        setEntryBlocks(null, f.getEntry(), entryStack, newSet(), functionAndBlocksManager);

        // needed if the blocks are unreachable
        f.getOrdinaryExit().setEntryBlock(f.getEntry());
        f.getExceptionalExit().setEntryBlock(f.getEntry());
    }

    /**
     * Recursively sets BasicBlock.entry_block
     * All blocks between "Begin" and "End" nodes form a region with a changed entry block see {@link BasicBlock#entry_block}
     */
    public static void setEntryBlocks(BasicBlock predecessor, BasicBlock target, Stack<BasicBlock> entryStack, Set<BasicBlock> visited, FunctionAndBlockManager functionAndBlocksManager) {
        visited.add(target);
        if (visited.size() > 1000) {
            throw new AnalysisLimitationException.SyntacticSupportNotImplemented(target.getFunction().getSourceLocation() + ": Recursive algorithm cannot handle large function body");
        }
        Stack<BasicBlock> successorEntryStack = new Stack<>();
        successorEntryStack.addAll(entryStack);
        Stack<BasicBlock> exceptionEntryStack = new Stack<>();
        exceptionEntryStack.addAll(entryStack);

        target.setEntryBlock(successorEntryStack.peek());
        if (target == target.getEntryBlock()) {
            target.setEntryPredecessorBlock(predecessor);
        }

        if (!target.isEmpty()) {
            AbstractNode lastNode = target.getLastNode();
            if(!Options.get().isForInSpecializationDisabled()) {
                if (lastNode instanceof BeginForInNode) {
                    successorEntryStack.push(target.getSingleSuccessor());
                }
                if (lastNode instanceof EndForInNode) {
                    successorEntryStack.pop();
                    exceptionEntryStack.pop();
                }
            }
        }

        if (successorEntryStack.isEmpty()) {
            throw new AnalysisException("Empty entry_block stack due to " + target);
        }

        Set<BasicBlock> unvisitedSuccessors = newSet();
        unvisitedSuccessors.addAll(target.getSuccessors());
        unvisitedSuccessors.addAll(functionAndBlocksManager.getUnreachableSyntacticSuccessors(target));
        unvisitedSuccessors.removeAll(visited);
        unvisitedSuccessors.remove(null);
        unvisitedSuccessors.forEach(s -> setEntryBlocks(target, s, successorEntryStack, visited, functionAndBlocksManager));
        BasicBlock exceptionHandler = target.getExceptionHandler();
        if (exceptionHandler != null && !visited.contains(exceptionHandler)) {
            setEntryBlocks(target, exceptionHandler, exceptionEntryStack, visited, functionAndBlocksManager);
        }
    }

    /**
     * Creates the nodes responsible for execution of registered event-handlers.
     */
    private void addEventDispatchers(SourceLocation location) {
        BasicBlock appendBlock = processed.getAppendBlock();

        boolean needsEventLoop = Options.get().isDOMEnabled() || Options.get().isAsyncEventsEnabled();
        if (needsEventLoop) {
            appendBlock = makeSuccessorBasicBlock(initialEnv.getFunction(), processed.getAppendBlock(), functionAndBlocksManager);
            NopNode nopEntryNode = new NopNode("eventDispatchers: entry", location);
            nopEntryNode.setArtificial();
            appendBlock.addNode(nopEntryNode);

            if (Options.get().isDOMEnabled()) {
                BasicBlock lastLoadEventLoopBlock = addEventHandlerLoop(appendBlock, Type.DOM_LOAD, "Load", location);
                BasicBlock lastOtherEventLoopBlock = addEventHandlerLoop(lastLoadEventLoopBlock, Type.DOM_OTHER, "Other", location);
                BasicBlock lastUnloadEventLoopBlock = addEventHandlerLoop(lastOtherEventLoopBlock, Type.DOM_UNLOAD, "Unload", location);
                appendBlock = lastUnloadEventLoopBlock;
            } else if (Options.get().isAsyncEventsEnabled()) {
                appendBlock = addEventHandlerLoop(appendBlock, Type.ASYNC, "Async", location);
            }
        }
        processed = TranslationResult.makeAppendBlock(appendBlock);
    }

    /**
     * Creates a zero-or-more loop in the flowgraph.
     * The loop contains an event dispatcher for the chosen event type.
     */
    private BasicBlock addEventHandlerLoop(BasicBlock appendBlock, Type eventType, String prettyEventName, SourceLocation location) {
        BasicBlock firstEventLoopBlock = makeSuccessorBasicBlock(initialEnv.getFunction(), appendBlock, functionAndBlocksManager);
        EventDispatcherNode eventDispatcherNode = new EventDispatcherNode(eventType, location);
        firstEventLoopBlock.addNode(eventDispatcherNode);

        BasicBlock lastEventLoopBlock = makeSuccessorBasicBlock(initialEnv.getFunction(), firstEventLoopBlock, functionAndBlocksManager);
        NopNode nopPostNode = new NopNode("eventDispatchers: post" + prettyEventName, location);
        nopPostNode.setArtificial();
        lastEventLoopBlock.addNode(nopPostNode);

        appendBlock.addSuccessor(lastEventLoopBlock); // may skip loop entirely
        lastEventLoopBlock.addSuccessor(firstEventLoopBlock); // back loop
        return lastEventLoopBlock;
    }

    public ASTInfo getAstInfo() {
        return astInfo;
    }

    /**
     * Creates a call to a function that defines and calls functions containing the host function sources.
     *
     * @see HostEnvSources
     */
    public void transformHostFunctionSources(List<JavaScriptSource> sources) {
        // make loader
        AstEnv mainEnv = this.initialEnv;
        SourceLocation loaderDummySourceLocation = HostEnvSources.getLoaderDummySourceLocation();

        final BasicBlock[] lastLoaderBlockBox = {mainEnv.getFunction().getEntry().getSingleSuccessor()};

        sources.stream().map(source -> {
            // make a function for each source ...
            ProgramTree tree = makeAST(source.getLocation(), source.getPrettyFileName(), source.getCode(), source.getLineOffset(), source.getColumnOffset());

            FormalParameterListTree params = new FormalParameterListTree(tree.location, ImmutableList.<ParseTree>of());

            int register = mainEnv.getRegisterManager().nextRegister();
            AstEnv declarationEnv = mainEnv.makeResultRegister(register).makeStatementLevel(false).makeAppendBlock(lastLoaderBlockBox[0]);
            SourceLocation location = new SourceLocation(0, 0, source.getPrettyFileName(), source.getLocation());
            new FunctionBuilder(astInfo, functionAndBlocksManager, source.getLocation(), syntacticHintsCollector).processFunctionDeclaration(Kind.EXPRESSION, "load:" + Paths.get(source.getPrettyFileName()).getFileName(), params, tree, declarationEnv, location, null);
            return register;
        }).collect(Collectors.toList() /* order of block side effects is important. sync here */).
                forEach(functionRegister -> {
                    // ... call each function
                    lastLoaderBlockBox[0] = makeSuccessorBasicBlock(mainEnv.getFunction(), lastLoaderBlockBox[0], functionAndBlocksManager);
                    CallNode callNode = new CallNode(false, AbstractNode.NO_VALUE, AbstractNode.NO_VALUE, functionRegister, newList(), loaderDummySourceLocation);
                    addNodeToBlock(callNode, lastLoaderBlockBox[0], mainEnv.makeStatementLevel(false));
                });

        BasicBlock postCallLoaderBlock = makeSuccessorBasicBlock(mainEnv.getFunction(), lastLoaderBlockBox[0], functionAndBlocksManager);
        processed = TranslationResult.makeAppendBlock(postCallLoaderBlock);
    }
}
