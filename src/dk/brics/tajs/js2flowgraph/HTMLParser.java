/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.js2flowgraph;

import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.flowgraph.JavaScriptSource;
import dk.brics.tajs.flowgraph.SourceLocation.SourceLocationMaker;
import dk.brics.tajs.flowgraph.SourceLocation.StaticLocationMaker;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException.AnalysisModelLimitationException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.PathAndURLUtils;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.RowColumnVector;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * HTML parser based on Jericho.
 */
public class HTMLParser {

    private static final Logger log = Logger.getLogger(HTMLParser.class);

    private final Source doc;

    private List<Pair<URL, JavaScriptSource>> code;

    /**
     * Parses the given HTML file.
     */
    public HTMLParser(URL location) throws IOException {
        this(Loader.getString(location, Charset.forName("UTF-8")), location, new StaticLocationMaker(location));
    }

    /**
     * Parses the given HTML source.
     */
    public HTMLParser(String source, URL baseURL, SourceLocationMaker maker) {
        this.doc = new Source(source);
        this.code = parseCode(doc, baseURL, maker);
    }

    private static List<Pair<URL, JavaScriptSource>> parseCode(Source doc, URL baseURL, SourceLocationMaker sourceLocationMaker) {
        Set<String> standardJavaScriptScriptTypeNames = newSet(Arrays.asList("text/javascript", "text/ecmascript", "application/javascript", "application/ecmascript"));
        Set<String> allJavaScriptScriptTypeNames = newSet();
        allJavaScriptScriptTypeNames.addAll(standardJavaScriptScriptTypeNames);
        // add some extra names to cater for a common typo
        allJavaScriptScriptTypeNames.addAll(Arrays.asList("javascript", "ecmascript", ""));
        Pattern pattern = Pattern.compile("^(.*) at \\(r(\\d+),c(\\d+),p\\d+\\) (.*)$"); // pattern for extracting source positions from the error messages. Example: "StartTag at (r21,c11,p721) missing required end tag"
        doc.setLogger(new HTMLParserLogger(msg -> {
            if (!Options.get().isNoMessages()) {
                Matcher matcher = pattern.matcher(msg);
                if (matcher.matches()) {
                    String messageWithoutRawPosition = String.format("%s %s", matcher.group(1), matcher.group(4));
                    int row = Integer.parseInt(matcher.group(2));
                    int column = Integer.parseInt(matcher.group(3));
                    log.info(String.format("%s: HTML %s", sourceLocationMaker.make(row, column, row, column), messageWithoutRawPosition)); // TODO: use Monitoring instead (github #535)
                } else {
                    log.info(String.format("%s: HTML %s", sourceLocationMaker.makeUnspecifiedPosition(), msg)); // TODO: use Monitoring instead (github #535)
                }
            }
        }));
        List<Pair<URL, JavaScriptSource>> code = newList();
        for (Element e : doc.getAllElements()) {
            String name = e.getName();
            if ("script".equals(name) && (e.getAttributeValue("type") == null || allJavaScriptScriptTypeNames.contains(e.getAttributeValue("type")))) {
                String src = e.getAttributeValue("src");
                if (src != null) {
                    // external script
                    try {
                        URL resolved = new URL(baseURL, src);
                        try {
                            code.add(Pair.make(resolved, JavaScriptSource.makeFileCode(Loader.getString(resolved, Charset.forName("UTF-8")))));
                        } catch (IOException e1) {
                            throw new AnalysisException("Could not load source code for " + resolved, e1);
                        }
                    } catch (MalformedURLException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    // embedded script
                    RowColumnVector pos = doc.getRowColumnVector(e.getStartTag().getEnd());
                    code.add(Pair.make(baseURL, JavaScriptSource.makeEmbeddedCode(e.getContent().toString(), pos.getRow() - 1, pos.getColumn() - 1)));
                }
            } else if ("a".equals(name) || "form".equals(name)) {
                Attributes as = e.getAttributes();
                if (as != null) {
                    Attribute a = as.get("a".equals(name) ? "href" : "action");
                    if (a != null) {
                        String val = a.getValue();
                        if (val != null) {
                            final String JAVASCRIPT = "javascript:";
                            if (val.length() > JAVASCRIPT.length() && val.substring(0, JAVASCRIPT.length()).equalsIgnoreCase(JAVASCRIPT)) {
                                // embedded 'javascript:' event handler
                                String js = val.substring(JAVASCRIPT.length());
                                RowColumnVector pos = doc.getRowColumnVector(a.getValueSegment().getBegin() + JAVASCRIPT.length());
                                EventType eventType = EventType.getEventHandlerTypeFromString("a".equals(name) ? "click" : "submit");
                                code.add(Pair.make(baseURL, JavaScriptSource.makeEventHandlerCode(eventType, js, pos.getRow() - 1, pos.getColumn() - 1)));
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
                            code.add(Pair.make(baseURL, JavaScriptSource.makeEventHandlerCode(eventKind, val, pos.getRow() - 1, pos.getColumn() - 1)));
                        }
                    }
                    if (eventKind == EventType.UNKNOWN && aname.startsWith("on")) {
                        throw new AnalysisModelLimitationException("Likely missing support for event-attribute: " + aname);
                    }
                }
            }
        }
        return code;
    }

    /**
     * Utility function for extracting all the script-paths in a HTML file.
     */
    public static Set<Path> getScriptsInHTMLFile(Path html) {
        if (!html.getFileName().toString().endsWith(".html") && !html.getFileName().toString().endsWith(".htm")) {
            throw new IllegalArgumentException("Not a HTML file: " + html);
        }
        try {
            return new HTMLParser(PathAndURLUtils.toURL(html)).getJavaScript().stream()
                    .filter(pair -> pair.getSecond().getKind() == JavaScriptSource.Kind.FILE)
                    .map(Pair::getFirst)
                    .map(PathAndURLUtils::toPath)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public List<Pair<URL, JavaScriptSource>> getJavaScript() {
        return code;
    }

    private static class HTMLParserLogger implements net.htmlparser.jericho.Logger {

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
