package dk.brics.tajs.js2flowgraph;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.parsing.parser.Parser.Config.Mode;
import com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree;
import com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree.Kind;
import com.google.javascript.jscomp.parsing.parser.trees.ProgramTree;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.EventHandlerKind;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.BeginForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EndForInNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode.Type;
import dk.brics.tajs.flowgraph.jsnodes.IfNode;
import dk.brics.tajs.flowgraph.jsnodes.NopNode;
import dk.brics.tajs.htmlparser.JavaScriptSource;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.ParseResult;
import dk.brics.tajs.js2flowgraph.JavaScriptParser.SyntaxMesssage;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSourceLocation;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.makeSuccessorBasicBlock;
import static dk.brics.tajs.js2flowgraph.FunctionBuilderHelper.setupFunction;
import static dk.brics.tajs.util.Collections.newList;
import static org.apache.log4j.Logger.getLogger;

/**
 * Converter from JavaScript source code to flow graphs.
 * The order the sources are provided is significant.
 */
public class FlowGraphBuilder {

    private static final Logger log = getLogger(FlowGraphBuilder.class);

    private static final boolean showParserWarnings = false;

    private final Mode mode = Mode.ES3; // TODO: (#3) currently ES3 mode

    private final JavaScriptParser parser;

    private final FunctionBuilder functionBuilder;

    private final FunctionAndBlockManager functionAndBlocksManager;

    private final AstEnv initialEnv;

    private final List<Pair<Function, EventHandlerKind>> eventHandlers;

    private boolean closed = false;

    private TranslationResult processed;

    /**
     * Constructs a new flow graph builder using a fresh environment.
     */
    public FlowGraphBuilder() {
        this(null, null);
    }

    /**
     * Constructs a new flow graph builder.
     *
     * @param env traversal environment, or null if fresh
     * @param fab function/block manager, or null if fresh
     */
    public FlowGraphBuilder(AstEnv env, FunctionAndBlockManager fab) {
        if (fab == null) {
            fab = new FunctionAndBlockManager();
        }
        if (env == null) {
            // prepare an initial AstEnv with a dummy main function
            env = AstEnv.makeInitial();
            Function main = new Function(null, null, null, null);
            env = setupFunction(main, env, fab);
        }
        functionAndBlocksManager = fab;
        initialEnv = env;
        parser = new JavaScriptParser(mode);
        functionBuilder = new FunctionBuilder(new ClosureASTUtil(mode), fab);
        processed = TranslationResult.makeAppendBlock(initialEnv.getAppendBlock());
        eventHandlers = newList();
    }

    /**
     * Transforms the given stand-alone JavaScript source code and appends it to the main function.
     */
    public void transformStandAloneCode(JavaScriptSource source) {
        transformCode(source, 0, 0);
    }

    /**
     * Transforms the given JavaScript source code and appends it to the main function, with location offsets.
     * The location offsets are used for setting the source locations in the flow graph.
     *
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     */
    void transformCode(JavaScriptSource source, int lineOffset, int columnOffset) {
        final AstEnv env = initialEnv.makeAppendBlock(processed.getAppendBlock());
        ProgramTree t = makeAST(source.getFileName(), source.getCode(), lineOffset, columnOffset, env);
        processed = functionBuilder.process(t, env);
    }

    /**
     * Parses the given JavaScript code.
     */
    private ProgramTree makeAST(String sourceName, String sourceContent, int lineOffset, int columnOffset, AstEnv env) {
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

        ParseResult parseResult = parser.parse(sourceName, s.toString());
        reportParseMessages(parseResult);
        patchSourceLocations(sourceName, env.getFunction(), lineOffset, columnOffset);
        return parseResult.getProgramAST();
    }

    /**
     * Reports parse errors and warnings to the log.
     *
     * @throws AnalysisException if the parse result contains a parse error
     */
    private static void reportParseMessages(ParseResult parseResult) {
        if (!parseResult.getErrors().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (SyntaxMesssage error : parseResult.getErrors()) {
                sb.append(String.format("%n%s: Syntax error: %s", error.getSourceLocation().toString(), error.getMessage()));
            }
            throw new AnalysisException(String.format("Parse error:%s", sb.toString()));
        }
        if (showParserWarnings) {
            for (SyntaxMesssage warning : parseResult.getWarnings()) {
                log.warn(String.format("%s: Parser warning: %s", warning.getSourceLocation().toString(), warning.getMessage()));
            }
        }
    }

    /**
     * Patches the source location of the function entry/exit nodes.
     */
    private static void patchSourceLocations(String sourceName, Function function, int lineOffset, int columnOffset) {
        if (function.getSourceLocation() == null) {
            SourceLocation loc = new SourceLocation(1 + lineOffset, 1 + columnOffset, sourceName);
            function.setSourceLocation(loc);
            function.getEntry().getFirstNode().setSourceLocation(loc);
            function.getOrdinaryExit().getFirstNode().setSourceLocation(loc);
            function.getExceptionalExit().getFirstNode().setSourceLocation(loc);
        }
    }

    /**
     * Transforms the given web application JavaScript source code.
     */
    public void transformWebAppCode(JavaScriptSource s) {
        switch (s.getKind()) {

            case FILE: { // TODO: (#119) processing order of external JavaScript files (sync/async loading...)
                // TODO: (#119) should be added as a load event, but that does not work currently...
                // Function function = processFunctionBody(Collections.<String>newList(), s.getFileName(), s.getJavaScript(), 0, initialEnv);
                // eventHandlers.add(Pair.make(function, EventHandlerKind.LOAD));
                transformStandAloneCode(s);
                break;
            }

            case EMBEDDED: { // TODO: (#119) currently ignoring events during page load (unsound)
                transformCode(s, s.getLineOffset(), s.getColumnOffset());
                break;
            }

            case EVENTHANDLER: {
                Function function = transformFunctionBody(s.getFileName(), s.getCode(), s.getLineOffset(), s.getColumnOffset(), initialEnv);
                String name = s.getEventName();
                final EventHandlerKind k;
                switch (name) { // TODO: (#118) not all HTML5 events are covered, see https://html.spec.whatwg.org/
                    case "load":
                        k = EventHandlerKind.LOAD;
                        break;
                    case "unload":
                        k = EventHandlerKind.UNLOAD;
                        break;
                    case "keypress":
                    case "keydown":
                    case "keyup":
                        k = EventHandlerKind.KEYBOARD;
                        break;
                    case "click":
                    case "dblclick":
                    case "mousedown":
                    case "mouseup":
                    case "mouseover":
                    case "mousemove":
                    case "mouseout":
                        k = EventHandlerKind.MOUSE;
                        break;
                    default:
                        k = EventHandlerKind.UNKNOWN;
                        break;
                }
                eventHandlers.add(Pair.make(function, k));
                break;
            }
        }
    }

    /**
     * Transforms the given code as a function body.
     *
     * @return the new function
     */
    private Function transformFunctionBody(String sourceName, String sourceContent, int lineOffset, int columnOffset, AstEnv env) {
        ProgramTree tree = makeAST(sourceName, sourceContent, lineOffset, columnOffset, env);
        FormalParameterListTree params = new FormalParameterListTree(tree.location, ImmutableList.of());
        return functionBuilder.processFunctionDeclaration(Kind.DECLARATION, null, params, tree, env, makeSourceLocation(tree));
    }

    /**
     * Returns the event handlers collected by {@link #transformWebAppCode(JavaScriptSource)}.
     */
    public List<Pair<Function, EventHandlerKind>> getEventHandlers() {
        return eventHandlers;
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
        flowGraph.setMain(flowGraph.getFunctions().iterator().next()); // FIXME: (#129) ugly hack, set main function in a more clean way
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

        if (Options.get().isDOMEnabled() && flowGraph == null) {
            // assume old flowgraph already has these.

            // TODO: (#129) but this means that there could be added a path to the main-exit that does not go through the event handlers.
            // but that is only a problem if dynamically added blocks are added to (end of) main, instead of as load-events (which is supposed to be fixed elsewhere)!
            addEventDispatchers(initialEnv.getFunction().getEntry().getSourceLocation());
        }

        if (flowGraph == null) {
            flowGraph = new FlowGraph();
        }
        int origBlockCount = flowGraph.getNumberOfBlocks();
        int origNodeCount = flowGraph.getNumberOfNodes();

        // wire the last processed block to the exit
        processed.getAppendBlock().addSuccessor(exitBlock);

        Pair<List<Function>, List<BasicBlock>> blocksAndFunctions = functionAndBlocksManager.close();

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

        // add each function and non-empty basic block to the flow graph
        for (Function f : blocksAndFunctions.getFirst()) {
            flowGraph.addFunction(f);
        }
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

        // add event handers
        for (Pair<Function, EventHandlerKind> callbackKindPair : eventHandlers) {
            flowGraph.addEventHandler(callbackKindPair.getFirst(), callbackKindPair.getSecond());
        }

        int blockCount = origBlockCount;
        int nodeCount = origNodeCount;

        // set block orders
        flowGraph.complete();

        for (Function function : flowGraph.getFunctions()) {
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
     * Creates the nodes responsible for execution of registered event-handlers.
     */
    private void addEventDispatchers(SourceLocation location) {
        Function main = initialEnv.getFunction();

        // event entry block (last node may require a single successor)
        BasicBlock entryBB = makeSuccessorBasicBlock(main, processed.getAppendBlock(), functionAndBlocksManager);
        entryBB.addNode(new NopNode(location)); // blocks cannot be empty

        // load event handlers
        BasicBlock loadBB = makeSuccessorBasicBlock(main, entryBB, functionAndBlocksManager);
        loadBB.addNode(new EventDispatcherNode(Type.LOAD, location));

        BasicBlock nopPostLoad = makeSuccessorBasicBlock(main, loadBB, functionAndBlocksManager); // loadBB muat have single successor
        NopNode nopPostNode = new NopNode(location);
        nopPostNode.setArtificial();
        nopPostLoad.addNode(nopPostNode); // blocks cannot be empty
        nopPostLoad.addSuccessor(loadBB); // back loop

        // other event handlers
        BasicBlock otherBB = makeSuccessorBasicBlock(main, nopPostLoad, functionAndBlocksManager);
        otherBB.addNode(new EventDispatcherNode(Type.OTHER, location));

        BasicBlock nopPostOther = makeSuccessorBasicBlock(main, otherBB, functionAndBlocksManager); // otherBB must have single successor
        AbstractNode nopPostOtherNode = new NopNode(location);
        nopPostOtherNode.setArtificial();
        nopPostOther.addNode(nopPostOtherNode); // blocks cannot be empty
        nopPostOther.addSuccessor(otherBB); // back loop

        // unload event handlers
        BasicBlock unloadBB = makeSuccessorBasicBlock(main, nopPostOther, functionAndBlocksManager);
        unloadBB.addNode(new EventDispatcherNode(Type.UNLOAD, location));

        BasicBlock nopPostUnload = makeSuccessorBasicBlock(main, unloadBB, functionAndBlocksManager); // unloadBB must have single successor
        AbstractNode nopPostUnloadNode = new NopNode(location);
        nopPostUnloadNode.setArtificial();
        nopPostUnload.addNode(nopPostUnloadNode); // blocks cannot be empty
        nopPostUnload.addSuccessor(unloadBB); // back loop

        entryBB.addSuccessor(nopPostLoad); // may skip load
        nopPostLoad.addSuccessor(nopPostOther); // may skip other
        nopPostOther.addSuccessor(nopPostUnload); // may skip unload

        processed = TranslationResult.makeAppendBlock(nopPostUnload);
    }
}
