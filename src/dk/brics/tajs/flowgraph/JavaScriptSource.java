/*
 * Copyright 2009-2015 Aarhus University
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

    private final String fileName;

    private final String code;

    private final int lineOffset;

    private final int columnOffset;

    private final String eventName;

    private JavaScriptSource(Kind kind, String eventName, String fileName, String code, int lineOffset, int columnOffset) {
        this.kind = kind;
        this.eventName = eventName;
        this.fileName = fileName;
        this.code = code;
        this.lineOffset = lineOffset;
        this.columnOffset = columnOffset;
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code in a separate file.
     */
    public static JavaScriptSource makeFileCode(String fileName, String code) {
        return new JavaScriptSource(Kind.FILE, null, fileName, code, 0, 0);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in a 'script' tag in an HTML file.
     *
     * @param fileName     file nam or URL of the code
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEmbeddedCode(String fileName, String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EMBEDDED, null, fileName, code, lineOffset, columnOffset);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in an event handler attribute in an HTML file.
     *
     * @param eventName    event kind, e.g. "click" or "submit"
     * @param fileName     file nam or URL of the code
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEventHandlerCode(String eventName, String fileName, String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EVENTHANDLER, eventName, fileName, code, lineOffset, columnOffset);
    }

    /**
     * Returns the kind.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * Returns the file name associated with the code.
     */
    public String getFileName() {
        return fileName;
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
     * Returns the event name, or null if not event handler code.
     */
    public String getEventName() {
        return eventName;
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
