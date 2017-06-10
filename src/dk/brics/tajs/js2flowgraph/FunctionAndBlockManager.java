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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
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
public class FunctionAndBlockManager {

    private final Map<SessionKey, Collection<BasicBlock>> sessionMap;

    private final Set<SessionKey> activeSessions;

    private final List<BasicBlock> blocks;

    private final List<Function> functions;

    private boolean closed;

    /**
     * Some blocks are not reachable after flowgraph construction due to (weird) source code.
     * But throwing the blocks away as an optimization would make error reporting (and dynamic code) harder.
     * Unfortunately, these unreachable blocks still need to be processed by various flowgraph post-processings, as if they were reachable.
     * These maps can be used to make BasicBlock#getSuccessors() return some extra blocks.
     */
    // TODO it would be safer to put this information in the blocks, such that it is maintained automatically on cloning
    private final Map<BasicBlock, Set<BasicBlock>> unreachableSyntacticSuccessors;

    private final Map<BasicBlock, BasicBlock> unreachableSyntacticSuccessorPredecessors;

    /**
     * Constructs a new function/block manager.
     */
    FunctionAndBlockManager() {
        closed = false;
        blocks = newList();
        functions = newList();
        unreachableSyntacticSuccessors = newMap();
        unreachableSyntacticSuccessorPredecessors = newMap();
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
     * {@link #getSessionBlocks(SessionKey)}.
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
     * {@link #endSession(SessionKey)}
     */
    Collection<BasicBlock> getSessionBlocks(SessionKey key) {
        if (activeSessions.contains(key)) {
            throw new IllegalArgumentException("Session still in progress: end it before querying");
        }
        return sessionMap.get(key);
    }

    public void registerUnreachableSyntacticSuccessor(BasicBlock predecessor, BasicBlock unreachableBlock) {
        if (unreachableSyntacticSuccessorPredecessors.containsKey(unreachableBlock)) {
            throw new AnalysisException("Registering unreachable block twice. That should not really be needed");
        }
        Collections.addToMapSet(unreachableSyntacticSuccessors, predecessor, unreachableBlock);
        unreachableSyntacticSuccessorPredecessors.put(unreachableBlock, predecessor);
    }

    public Set<BasicBlock> getUnreachableSyntacticSuccessors(BasicBlock predecessor) {
        if (!unreachableSyntacticSuccessors.containsKey(predecessor)) {
            return newSet();
        }
        return unreachableSyntacticSuccessors.get(predecessor);
    }

    public boolean isUnreachable(BasicBlock block) {
        return unreachableSyntacticSuccessorPredecessors.containsKey(block);
    }

    public BasicBlock getUnreachableSyntacticSuccessorPredecessor(BasicBlock unreachable) {
        if (!unreachableSyntacticSuccessorPredecessors.containsKey(unreachable)) {
            throw new AnalysisException("Block is not unreachable: " + unreachable);
        }
        return unreachableSyntacticSuccessorPredecessors.get(unreachable);
    }

    /**
     * Session key object.
     */
    static class SessionKey {

        private SessionKey() {
        }
    }
}
