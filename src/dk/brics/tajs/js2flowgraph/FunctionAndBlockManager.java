package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Registers creation of functions and blocks.
 */
class FunctionAndBlockManager {

    private final Map<SessionKey, Collection<BasicBlock>> sessionMap;

    private final Set<SessionKey> activeSessions;

    private final List<BasicBlock> blocks;

    private final List<Function> functions;

    private boolean closed;

    /**
     * Constructs a new function/block manager.
     */
    FunctionAndBlockManager() {
        closed = false;
        blocks = newList();
        functions = newList();
        sessionMap = newMap();
        activeSessions = newSet();
    }

    /**
     * Registers the given function.
     */
    void add(Function function) {
        if (closed) {
            throw new IllegalStateException("Already closed, cannot add more functions.");
        }
        functions.add(function);
    }

    /**
     * Registers the given blocks.
     * This includes adding them to each active session.
     */
    void add(BasicBlock... bs) {
        add(Arrays.asList(bs));
    }

    /**
     * Registers the given blocks.
     * This includes adding them to each active session.
     */
    void add(List<BasicBlock> bs) {
        if (closed) {
            throw new IllegalStateException("Already closed, cannot add more blocks.");
        }
        for (SessionKey activeKey : activeSessions) {
            sessionMap.get(activeKey).addAll(bs);
        }
        blocks.addAll(bs);
    }

    /**
     * Closes this collection of functions and blocks.
     */
    Pair<List<Function>, List<BasicBlock>> close() {
        closed = true;
        return Pair.make(functions, blocks);
    }

    /**
     * Marks the start of a session.
     * Blocks created in this session can be extracted later with
     * {@link #getSessionBlocks(FunctionAndBlockManager.SessionKey)}.
     * Sessions do not have to nest properly.
     *
     * @return a unique key identifying the session.
     */
    SessionKey startSession() {
        SessionKey key = new SessionKey();
        activeSessions.add(key);
        Collection<BasicBlock> list = newList();
        sessionMap.put(key, list);
        return key;
    }

    /**
     * Ends a session.
     */
    void endSession(SessionKey key) {
        activeSessions.remove(key);
    }

    /**
     * Returns the blocks produced between a call to {@link #startSession()} and
     * {@link #endSession(FunctionAndBlockManager.SessionKey)}
     */
    Collection<BasicBlock> getSessionBlocks(SessionKey key) {
        if (activeSessions.contains(key)) {
            throw new IllegalArgumentException("Session still in progress: end it before querying");
        }
        return sessionMap.get(key);
    }

    /**
     * Session key object.
     */
    static class SessionKey {

        private SessionKey() {
        }
    }
}
