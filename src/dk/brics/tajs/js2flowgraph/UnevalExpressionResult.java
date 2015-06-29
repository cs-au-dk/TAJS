package dk.brics.tajs.js2flowgraph;

/**
 * Used by the unevalizer to get results from unevalized expressions.
 */
class UnevalExpressionResult {

    /**
     * The special variable name used for the result of the expression.
     */
    final String specialVariableName;

    /**
     * The register where the result is redirected to.
     */
    final int resultRegister;

    UnevalExpressionResult(String specialVariableName, int resultRegister) {
        this.specialVariableName = specialVariableName;
        this.resultRegister = resultRegister;
    }
}
