package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.BasicBlock;

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
    private Reference resultReference;

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
    Reference getResultReference() {
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
    static TranslationResult makeResultReference(Reference resultReference, BasicBlock appendBlock) {
        TranslationResult res = new TranslationResult();
        res.resultReference = resultReference;
        res.appendBlock = appendBlock;
        return res;
    }
}
