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

package dk.brics.tajs.util;

/**
 * Exception for analysis limitations.
 * <p>
 * Unlike {@link AnalysisException} this exception does not indicate a bug but critical lack of precision or missing modelling of a native function.
 */
public class AnalysisLimitationException extends RuntimeException { // TODO: use this exception in more places

    private static final long serialVersionUID = 1L;

    private AnalysisLimitationException(String msg) {
        super(msg);
    }

    /**
     * To be used when encountering situations where the state is too imprecise, e.g. eval(TOP_STRING).
     */
    public static class AnalysisPrecisionLimitationException extends AnalysisLimitationException {

        /**
         * Constructs a new exception.
         */
        public AnalysisPrecisionLimitationException(String msg) {
            super(msg);
        }
    }

    public static class AnalysisTimeException extends AnalysisLimitationException {

        /**
         * Constructs a new exception.
         */
        public AnalysisTimeException(String msg) {
            super(msg);
        }
    }

    /**
     * To be used when encountering unmodeled native functions.
     */
    public static class AnalysisModelLimitationException extends AnalysisLimitationException {

        /**
         * Constructs a new exception.
         */
        public AnalysisModelLimitationException(String msg) {
            super(msg);
        }
    }

    /**
     * Exception for syntactic features that are not yet implemented.
     */
    public static class SyntacticSupportNotImplemented extends AnalysisLimitationException {

        /**
         * Constructs a new exception.
         */
        public SyntacticSupportNotImplemented(String msg) {
            super(msg);
        }
    }
}
