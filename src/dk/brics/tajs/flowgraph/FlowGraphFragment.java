/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.flowgraph;

import java.util.Collection;

/**
 * A fragment of a flow graph.
 */
public class FlowGraphFragment {

    /**
     * The fragment key.
     */
    private Object key;

    /**
     * The starting block for this fragment.
     */
    private BasicBlock entryBlock;

    /**
     * The outermost function block for this fragment (used to keep track of event handlers).
     */
    private Function entryFun;

    /**
     * The functions that belong to this fragment.
     */
    private Collection<Function> functions;

    /**
     * The extra blocks that belong to this fragment.
     */
    private Collection<BasicBlock> blocks;

    /**
     * Constructs a flow graph fragment.
     *
     * @param key        fragment key
     * @param entryBlock starting block for this fragment
     * @param entryFun   outermost function block for this fragment (used to keep track of event handlers)
     * @param functions  functions that belong to this fragment
     * @param blocks     blocks that belong to this fragment (excluding blocks in the given collection of functions)
     */
    public FlowGraphFragment(Object key, BasicBlock entryBlock, Function entryFun, Collection<Function> functions, Collection<BasicBlock> blocks) {
        this.key = key;
        this.entryBlock = entryBlock;
        this.entryFun = entryFun;
        this.functions = functions;
        this.blocks = blocks;
    }

    /**
     * Returns the key.
     */
    public Object getKey() {
        return key;
    }

    /**
     * Returns the entry block.
     */
    public BasicBlock getEntryBlock() {
        return entryBlock;
    }

    /**
     * Returns the entry function.
     */
    public Function getEntryFunction() {
        return entryFun;
    }

    /**
     * Returns the collection of functions.
     */
    public Collection<Function> getFunction() {
        return functions;
    }

    /**
     * Returns the collection of blocks.
     */
    public Collection<BasicBlock> getBlocks() {
        return blocks;
    }
}