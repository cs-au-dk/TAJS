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

package dk.brics.tajs.flowgraph;

/**
 * JavaScript code snippet with meta-information.
 */
public class JavaScriptSource {

    private final Kind kind;

    private final String code;

    private final int lineOffset;

    private final int columnOffset;

    private final EventType eventkind;

    private JavaScriptSource(Kind kind, EventType eventkind, String code, int lineOffset, int columnOffset) {
        this.kind = kind;
        this.eventkind = eventkind;
        this.code = code;
        this.lineOffset = lineOffset;
        this.columnOffset = columnOffset;
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code in a separate file.
     */
    public static JavaScriptSource makeFileCode(String code) {
        return new JavaScriptSource(Kind.FILE, null, code, 0, 0);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in a 'script' tag in an HTML file.
     *
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEmbeddedCode(String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EMBEDDED, null, code, lineOffset, columnOffset);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in an event handler attribute in an HTML file.
     *
     * @param kind    event kind, e.g. "click" or "submit"
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEventHandlerCode(EventType kind, String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EVENTHANDLER, kind, code, lineOffset, columnOffset);
    }

    /**
     * Returns the kind.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the line offset for the first line.
     */
    public int getLineOffset() {
        return lineOffset;
    }

    /**
     * Returns the column offset for the first line.
     */
    public int getColumnOffset() {
        return columnOffset;
    }

    /**
     * Returns the event kind, or null if not event handler code.
     */
    public EventType getEventKind() {
        return eventkind;
    }

    public enum Kind {

        /**
         * JavaScript code in a separate file.
         */
        FILE,

        /**
         * JavaScript code embedded in a 'script' tag in an HTML file.
         */
        EMBEDDED,

        /**
         * JavaScript code embedded in an event handler attribute in an HTML file.
         */
        EVENTHANDLER
    }
}
