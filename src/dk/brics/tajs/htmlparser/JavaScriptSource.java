/*
 * Copyright 2012 Aarhus University
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

package dk.brics.tajs.htmlparser;

/**
 * A compound structure that contains meta-information about a single source "file". Used for communicating
 * the results between the HTMLParser and Main.
 */
public class JavaScriptSource {
	
    private String fileName;
    private String javaScript;
    private String eventName;
    private int lineNumber;

    /**
     * Returns the file name.
     *
     * @return Returns the file name associated with the code.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the code.
     *
     * @return Returns the code.
     */
    public String getJavaScript() {
        return javaScript;
    }

    /**
     * Returns the starting line number.
     *
     * @return Returns the starting line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the name of the event.
     *
     * @return The name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Constructs the meta-information about a single file.
     *
     * @param fileName The file name.
     * @param javaScript The code string.
     * @param lineNumber The line number.
     */
    public JavaScriptSource(String fileName, String javaScript, int lineNumber) {
        this.fileName = fileName;
        this.javaScript = javaScript;
        this.lineNumber = lineNumber;
    }

    /**
     * Constructs the meta-information about an event handler.
     *
     * @param fileName The file name.
     * @param eventName The event name.
     * @param javaScript The code string.
     * @param lineNumber The line number.
     */
    public JavaScriptSource(String fileName, String eventName, String javaScript, int lineNumber) {
        this.fileName = fileName;
        this.eventName = eventName;
        this.javaScript = javaScript;
        this.lineNumber = lineNumber;
    }
}
