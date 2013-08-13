/*
 * Copyright 2009-2013 Aarhus University
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

import java.util.Map;

/**
 * A compound structure that contains meta-information about a single HTML "source". Used for communicating
 * the results between the HTMLParser and Main.
 *
 * @see JavaScriptSource for its cousin.
 *
 */
public class HtmlSource {

    private final String tag;

    private final Map<String, String> attributes;
    
    private final String fileName;
    
    private final int lineNumber;

    /**
     * Constructs the meta-information about a single source.
     *
     * @param tag The HTML tag.
     * @param attributes The attributes of the tag.
     * @param fileName The filename.
     * @param lineNumber The line number in the source.
     */
    public HtmlSource(String tag, Map<String, String> attributes, String fileName, int lineNumber) {
        this.tag = tag;
        this.attributes = attributes;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    /**
     * Get the tag.
     *
     * @return The tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the attributes.
     *
     * @return A map of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Get the file name.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the line number.
     *
     * @return The line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }
}