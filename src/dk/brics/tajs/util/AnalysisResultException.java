/*
 * Copyright 2009-2019 Aarhus University
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
 * Exception for analysis errors that appear as unexpected analysis output.
 */
public class AnalysisResultException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception.
     */
    public AnalysisResultException() {
    }

    /**
     * Constructs a new exception.
     */
    public AnalysisResultException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception.
     */
    public AnalysisResultException(Throwable t) {
        super(t);
    }

    /**
     * Constructs a new exception.
     */
    public AnalysisResultException(String msg, Throwable t) {
        super(msg, t);
    }
}
