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

import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.syntaticinfo.SyntacticReference;

/**
 * Information about the result of translating a program fragment.
 */
class TranslationResult {

    /**
     * Holds the basic block where new blocks can be appended.
     */
    private BasicBlock appendBlock;

    /**
     * Holds the result reference, or null if the result if not a reference.
     */
    private SyntacticReference resultReference;

    private TranslationResult() {
    }

    /**
     * Returns the append block.
     */
    BasicBlock getAppendBlock() {
        return appendBlock;
    }

    /**
     * Returns the result reference, or null if none.
     */
    SyntacticReference getResultReference() {
        return resultReference;
    }

    /**
     * Creates a translation result that stores the last basic block that been generated.
     */
    static TranslationResult makeAppendBlock(BasicBlock appendBlock) {
        TranslationResult res = new TranslationResult();
        res.appendBlock = appendBlock;
        return res;
    }

    /**
     * Creates a translation result that stores the result of an expression and the last basic block that been generated.
     */
    static TranslationResult makeResultReference(SyntacticReference resultReference, BasicBlock appendBlock) {
        TranslationResult res = new TranslationResult();
        res.resultReference = resultReference;
        res.appendBlock = appendBlock;
        return res;
    }
}
