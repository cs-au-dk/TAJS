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

import static dk.brics.tajs.util.Collections.newList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.contrib.input.LineNumberElement;
import org.jdom.contrib.input.LineNumberSAXBuilder;
import org.jdom.input.SAXBuilder;
import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyMessage;
import org.w3c.tidy.TidyMessageListener;

// import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import dk.brics.tajs.htmlparser.JavaScriptSource.EmbeddedJavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.EventHandlerJavaScriptSource;
import dk.brics.tajs.htmlparser.JavaScriptSource.ExternalJavaScriptSource;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

/**
 * HTML parser using JTidy and JDOM to extract JavaScript code and HTML elements from HTML files.
 */
public class HTMLParser {

	private static Logger logger = Logger.getLogger(HTMLParser.class); 

	private List<JavaScriptSource> scriptList;

	private List<EventHandlerJavaScriptSource> eventList;
    
	private List<HtmlSource> htmlElementList;

    public HTMLParser() { }

    /**
     * Parses the given HTML file.
     * @param inputFileName
     * @return the JDOM document of the HTML file
     * @throws IOException if unable to read the file
     */
    public Document build(String inputFileName) throws IOException {
        String outputFileName;
        if (inputFileName.endsWith(".htm")) {
            outputFileName = inputFileName.substring(0, inputFileName.indexOf(".htm")) + ".tidy.htm";
        } else if (inputFileName.endsWith(".html")) {
            outputFileName = inputFileName.substring(0, inputFileName.indexOf(".html")) + ".tidy.html";
        } else {
            outputFileName = inputFileName + ".tidy";
        }
        try (FileInputStream in = new FileInputStream(inputFileName); 
        		FileOutputStream outputStream = new FileOutputStream(outputFileName)) {
            Tidy tidy = newTidy();
        	tidy.pprint(tidy.parseDOM(in, null), outputStream);
        }
        SAXBuilder builder = new LineNumberSAXBuilder();
        Document document;
        try {
            document = builder.build(outputFileName);
        } catch (JDOMException e) {
            throw new IOException(e);
        }
        ScriptVisitor visitor = new ScriptVisitor(document, outputFileName);
        visitor.visitDocument();
        scriptList = visitor.getJavaScript();
        if (!Options.isIgnoreHTMLContent()) {
            EventVisitor eventVisitor = new EventVisitor(document, outputFileName);
            eventVisitor.visitDocument();
            eventList = eventVisitor.getEventHandlerAttributeList();
            htmlElementList = eventVisitor.getHtmlElementList();
        }
        return document;
    }

    /**
     * Returns the JavaScript code from event handler attributes with "javascript:".
     */
    public List<EventHandlerJavaScriptSource> getEventHandlerAttributeList() {
        return eventList;
    }

    /**
     * Returns the JavaScript code from 'script' elements, both embedded and external.
     */
    public List<JavaScriptSource> getScriptList() {
        return scriptList;
    }

    /**
     * Returns the HTML elements (only if DSL is enabled).
     */
    public List<HtmlSource> getHtmlSourceList() { // FIXME: really need this for DSL?
        return htmlElementList;
    }

    private static int getElementLineNumber(Element element) {
        return ((LineNumberElement) element).getStartLine() - 1;
    }

    private static class EventVisitor extends HTMLVisitorImpl {

    	private List<EventHandlerJavaScriptSource> eventList;
        
    	private List<HtmlSource> htmlElementList;
        
    	private final String filename;

        public EventVisitor(Document document, String filename) {
            super(document);
            this.eventList = newList();
            this.htmlElementList = newList();
            this.filename = filename;
        }

        public List<EventHandlerJavaScriptSource> getEventHandlerAttributeList() {
            return eventList;
        }
        
        public List<HtmlSource> getHtmlElementList() {
            return htmlElementList;     
        }
        
        @SuppressWarnings("unchecked")
		@Override
        public void visit(Element element) {
            super.visit(element);

            // Pick up event handlers
            for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
                String name = attribute.getName();
                String javaScriptSource = attribute.getValue();
                if (DOMEventHelpers.isEventAttribute(name)) {
                    eventList.add(new EventHandlerJavaScriptSource(filename, javaScriptSource, getElementLineNumber(element), name));
                }
            }
            
            if (Options.isDSLEnabled()) {
                String tag = element.getName();
                Map<String, String> attributes = new HashMap<>();
                for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
                    attributes.put(attribute.getName(), attribute.getValue());    
                }
                htmlElementList.add(new HtmlSource(tag, attributes, filename, getElementLineNumber(element)));
            }
        }

        @Override
        public void visitA(Element element) {
            //check if the href attribute is there
            String href = element.getAttributeValue("href");

            if (href == null) {
                return;
            }

            if (href.startsWith("javascript:")) {
                String jsCode = href.substring("javascript:".length());
                eventList.add(new EventHandlerJavaScriptSource(filename, jsCode, getElementLineNumber(element), "onclick"));
            }
        }

        @SuppressWarnings("unchecked")
		@Override
        public void visitBody(Element element) {
            for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
                String name = attribute.getName();
                String value = attribute.getValue();

                if (DOMEventHelpers.isLoadEventAttribute(name) || DOMEventHelpers.isUnloadEventAttribute(name)) {
                    eventList.add(new EventHandlerJavaScriptSource(filename, value, getElementLineNumber(element), name));
                }
            }
        }
    }

    /**
     * Configures a new JTidy instance.
     */
    private static Tidy newTidy() {
        Tidy tidy = new Tidy();
        tidy.setMessageListener(new TidyMessageListener() {

            @Override
            public void messageReceived(TidyMessage msg) {
            	logger.warn(String.format("HTML warning at %s:%s: %s", msg.getLine(), msg.getColumn(), msg.getMessage()));
            }
        });
        tidy.setDropEmptyParas(false);
        tidy.setDropFontTags(false);
        tidy.setDropProprietaryAttributes(false);
        tidy.setTrimEmptyElements(false);
        tidy.setXHTML(true);
        tidy.setIndentAttributes(false);
        tidy.setIndentCdata(false);
        tidy.setIndentContent(false);
        tidy.setQuiet(true);
        tidy.setShowWarnings(!Options.isQuietEnabled());
        tidy.setShowErrors(0);
        tidy.setEncloseBlockText(false);
        tidy.setEscapeCdata(false);
        tidy.setDocType("omit");
        tidy.setInputEncoding("UTF-8");
        tidy.setRawOut(true);
        tidy.setOutputEncoding("UTF-8");
        tidy.setFixUri(false);
        Properties prop = new Properties();
        prop.put("new-blocklevel-tags", "canvas");
        tidy.getConfiguration().addProps(prop);
        return tidy;
    }

    /**
     * HTML document visitor that extracts JavaScript code from 'script' elements.
     */
    private static class ScriptVisitor extends HTMLVisitorImpl {

        private List<JavaScriptSource> fileToJS = Collections.newList();

        private final String htmlFileName;

        private final File file;

        /**
         * Constructs a new visitor.
         * @param document the HTML document to traverse
         * @param htmlFileName the name of the HTML file
         */
        private ScriptVisitor(Document document, String htmlFileName) {
            super(document);
            this.htmlFileName = htmlFileName;
            this.file = new File(htmlFileName);
        }

        /**
         * Returns the resulting list of JavaScript snippets.
         */
        public List<JavaScriptSource> getJavaScript() {
            return fileToJS;
        }

        @Override
        public void visitScript(Element element) {
            LineNumberElement elm = (LineNumberElement) element;
            String src = element.getAttributeValue("src");
            if (src == null) { // embedded script
                fileToJS.add(new EmbeddedJavaScriptSource(htmlFileName, element.getText(), getElementLineNumber(elm)));
            } else { // external script
            	// TODO: use Loader.resolveRelative and Loader.getString instead of readScriptFile and readScriptURL
                // script in file?
                String pathname = file.getParent() + File.separator + src;
                File srcFile = new File(pathname);
                if (srcFile.exists()) {
                    String s = readScriptFile(srcFile);
                    if (s != null) {
                        fileToJS.add(new ExternalJavaScriptSource(src, s));
                    }
                    return;
                }
                // script in url?
                URL url;
                try {
                    url = new URL(src);
                    String s = readScriptURL(url);
                    if (s != null) {
                        fileToJS.add(new ExternalJavaScriptSource(src, s));
                    }
                } catch (MalformedURLException e) {
                    throw new AnalysisException(e);
                }
            }
        }

        /**
         * Reads JavaScript code from a file.
         */
        private static String readScriptFile(File file) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                return sb.toString();
            } catch (IOException e1) {
            	logger.error("Unable to read src file: " + file.toString());
                return null;
            }
        }

        private static String readScriptURL(URL url) {
            try (BufferedReader bfr = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bfr.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                return sb.toString();
            } catch (MalformedURLException e1) {
                logger.warn("Malformed URL in src attribute: " + url);
                return null;
            } catch (IOException e2) {
                logger.warn("Could not fetch script from " + url);
                return null;
            }
        }
    }
}
