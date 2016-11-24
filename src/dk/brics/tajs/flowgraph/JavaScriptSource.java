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

package dk.brics.tajs.flowgraph;

import java.net.URL;

/**
 * JavaScript code snippet with meta-information.
 */
public class JavaScriptSource {

    private final Kind kind;

    private final String prettyFileName;

    private final String code;

    private final int lineOffset;

    private final int columnOffset;

    private final EventType eventkind;

    private final URL location;

    private JavaScriptSource(Kind kind, EventType eventkind, String prettyFileName, String code, int lineOffset, int columnOffset, URL location) {
        this.kind = kind;
        this.eventkind = eventkind;
        this.prettyFileName = prettyFileName;
        this.code = code;
        this.lineOffset = lineOffset;
        this.columnOffset = columnOffset;
        this.location = location;
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code in a separate file.
     */
    public static JavaScriptSource makeFileCode(URL location, String prettyFileName, String code) {
        return new JavaScriptSource(Kind.FILE, null, prettyFileName, code, 0, 0, location);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in a 'script' tag in an HTML file.
     *
     * @param prettyFileName file nam or URL of the code
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEmbeddedCode(URL location, String prettyFileName, String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EMBEDDED, null, prettyFileName, code, lineOffset, columnOffset, location);
    }

    /**
     * Constructs a new code snippet descriptor for JavaScript code embedded in an event handler attribute in an HTML file.
     *
     * @param kind    event kind, e.g. "click" or "submit"
     * @param fileName     file nam or URL of the code
     * @param code         the JavaScript code
     * @param lineOffset   number of lines preceding the code
     * @param columnOffset number of columns preceding the first line of the code
     * @return new JavaScriptSource object
     */
    public static JavaScriptSource makeEventHandlerCode(EventType kind, URL location, String fileName, String code, int lineOffset, int columnOffset) {
        return new JavaScriptSource(Kind.EVENTHANDLER, kind, fileName, code, lineOffset, columnOffset, location);
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
    public String getPrettyFileName() {
        return prettyFileName;
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

    public URL getLocation() {
        return location;
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
