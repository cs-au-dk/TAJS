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

package dk.brics.tajs.htmlparser;

import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Loader;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.RowColumnVector;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * HTML parser based on Jericho.
 */
public class HTMLParser {

    private static final Logger log = Logger.getLogger(HTMLParser.class);

    private Source doc;

    private List<JavaScriptSource> code;

    /**
     * Parses the given HTML file.
     */
    public HTMLParser(URL location, String prettyFileName) throws IOException {
        Set<String> standardJavaScriptScriptTypeNames = newSet(Arrays.asList("text/javascript", "text/ecmascript", "application/javascript", "application/ecmascript"));
        Set<String> allJavaScriptScriptTypeNames = newSet();
        allJavaScriptScriptTypeNames.addAll(standardJavaScriptScriptTypeNames);
        // add some extra names to cater for a common typo
        allJavaScriptScriptTypeNames.addAll(Arrays.asList("javascript", "ecmascript", ""));

        try {
            Path file = Paths.get(location.toURI());
            doc = new Source(file.toFile());
            doc.setLogger(new HTMLParserLogger(msg -> log.info(String.format("%s: %s", prettyFileName, msg)))); // squelch errors from the HTML
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        code = newList();
        for (Element e : doc.getAllElements()) {
            String name = e.getName();
            if (name.equals("script") && (e.getAttributeValue("type") == null || allJavaScriptScriptTypeNames.contains(e.getAttributeValue("type")))) {
                String src = e.getAttributeValue("src");
                if (src != null) {
                    // external script
                    try {
                        URL resolved = new URL(location, src);
                        code.add(JavaScriptSource.makeFileCode(resolved, src, Loader.getString(resolved, Charset.forName("UTF-8"))));
                    } catch (MalformedURLException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    // embedded script
                    RowColumnVector pos = doc.getRowColumnVector(e.getStartTag().getEnd());
                    code.add(JavaScriptSource.makeEmbeddedCode(location, prettyFileName, e.getContent().toString(), pos.getRow() - 1, pos.getColumn() - 1));
                }
            } else if (name.equals("a") || name.equals("form")) {
                Attributes as = e.getAttributes();
                if (as != null) {
                    Attribute a = name.equals("a") ? as.get("href") : as.get("action");
                    if (a != null) {
                        String val = a.getValue();
                        final String JAVASCRIPT = "javascript:";
                        if (val != null) {
                            if (val.length() > JAVASCRIPT.length() && val.substring(0, JAVASCRIPT.length()).equalsIgnoreCase(JAVASCRIPT)) {
                                // embedded 'javascript:' event handler
                                String js = val.substring(JAVASCRIPT.length());
                                RowColumnVector pos = doc.getRowColumnVector(a.getValueSegment().getBegin() + JAVASCRIPT.length());
                                EventType eventType = EventType.getEventHandlerTypeFromString(name.equals("a") ? "click" : "submit");
                                code.add(JavaScriptSource.makeEventHandlerCode(eventType, location, prettyFileName, js, pos.getRow() - 1, pos.getColumn() - 1));
                            }
                        }
                    }
                }
            }
            Attributes as = e.getAttributes();
            if (as != null) {
                for (Attribute a : as) {
                    String aname = a.getKey();
                    EventType eventKind = EventType.getEventHandlerTypeFromAttributeName(aname);
                    if (eventKind != EventType.UNKNOWN) { // may include too many attributes in case of bad HTML...
                        String val = a.getValue();
                        if (val != null) {
                            // embedded 'on...' event handler
                            RowColumnVector pos = doc.getRowColumnVector(a.getValueSegment().getBegin());
                            code.add(JavaScriptSource.makeEventHandlerCode(eventKind, location, prettyFileName, val, pos.getRow() - 1, pos.getColumn() - 1));
                        }
                    }
                    if (eventKind == EventType.UNKNOWN && aname.startsWith("on")) {
                        throw new AnalysisLimitationException.AnalysisModelLimitationException("Likely missing support for event-attribute: " + aname);
                    }
                }
            }
        }
    }

    /**
     * Returns the HTML.
     */
    public Source getHTML() {
        return doc;
    }

    /**
     * Returns all JavaScript code in the document, both from both embedded/external 'script' elements and event handlers.
     * The order of elements is that of a DFS traversal of the HTML document.
     */
    public List<JavaScriptSource> getJavaScript() {
        return code;
    }

    private class HTMLParserLogger implements net.htmlparser.jericho.Logger {

        Consumer<String> outerLogger;

        public HTMLParserLogger(Consumer<String> outerLogger) {
            this.outerLogger = outerLogger;
        }

        @Override
        public void error(String message) {
            outerLogger.accept(message);
        }

        @Override
        public void warn(String message) {
            outerLogger.accept(message);
        }

        @Override
        public void info(String message) {

        }

        @Override
        public void debug(String message) {

        }

        @Override
        public boolean isErrorEnabled() {
            return true;
        }

        @Override
        public boolean isWarnEnabled() {
            return true;
        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }
    }
}
