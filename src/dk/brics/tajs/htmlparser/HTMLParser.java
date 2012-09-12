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

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;

public class HTMLParser {

	private static Logger logger = Logger.getLogger(HTMLParser.class); 

	private List<JavaScriptSource> jsList;
    private List<JavaScriptSource> jsEventList;
    private List<HtmlSource> htmlElementList;

    public HTMLParser() { }

    public Document build(String inputFileName) throws IOException {

        String outputFileName;
        if (inputFileName.endsWith(".htm")) {
            outputFileName = inputFileName.substring(0, inputFileName.indexOf(".htm")) + ".tidy.htm";
        } else if (inputFileName.endsWith(".html")) {
            outputFileName = inputFileName.substring(0, inputFileName.indexOf(".html")) + ".tidy.html";
        } else {
            outputFileName = inputFileName + ".tidy";
        }

        {
            Tidy tidy = newTidy();
            org.w3c.dom.Document document = tidy.parseDOM(new FileInputStream(inputFileName), null); // TODO: potential resource leak?
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputByteStream(new FileOutputStream(outputFileName)); // TODO: potential resource leak?
            FileOutputStream outputStream = new FileOutputStream(outputFileName);
            tidy.pprint(document, outputStream);
            outputStream.close();
        }

        SAXBuilder builder = new LineNumberSAXBuilder();
        Document document;
        try {
            document = builder.build(outputFileName);
        } catch (JDOMException e) {
            throw new IOException(e);
        }

        JavaScriptVisitor visitor = new JavaScriptVisitor(document, outputFileName);
        visitor.visitDocument();
        jsList = visitor.getJavaScript();

        if (!Options.isIgnoreHTMLContent()) {
            EventVisitor eventVisitor = new EventVisitor(document, outputFileName);
            eventVisitor.visitDocument();
            jsEventList = eventVisitor.getJsEventList();
            htmlElementList = eventVisitor.getHtmlElementList();
        }

        return document;
    }

    public List<JavaScriptSource> getJsEventList() {
        return jsEventList;
    }

    public List<JavaScriptSource> getJsList() {
        return jsList;
    }

    public List<HtmlSource> getHtmlSourceList() {
        return htmlElementList;
    }

    private static class EventVisitor extends HTMLVisitorImpl {
        private List<JavaScriptSource> jsEventList;
        private List<HtmlSource> htmlElementList;
        private final String filename;

        public EventVisitor(Document document, String filename) {
            super(document);
            this.jsEventList = newList();
            this.htmlElementList = newList();
            this.filename = filename;
        }

        public List<JavaScriptSource> getJsEventList() {
            return jsEventList;
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
                String value = attribute.getValue();
                if (DOMEventHelpers.isEventAttribute(name)) {
                    jsEventList.add(new JavaScriptSource(filename, name, value, getElementLinenumber(element)));
                }
            }
            
            if (Options.isDSLEnabled()) {
                String tag = element.getName();
                Map<String, String> attributes = new HashMap<>();
                for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
                    attributes.put(attribute.getName(), attribute.getValue());    
                }
                htmlElementList.add(new HtmlSource(tag, attributes, filename, getElementLinenumber(element)));
            }

        }

        private static int getElementLinenumber(Element element) {
            return ((LineNumberElement) element).getStartLine() - 1;
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
                jsEventList.add(new JavaScriptSource(filename, "onclick", jsCode, getElementLinenumber(element)));
            }
        }

        @SuppressWarnings("unchecked")
		@Override
        public void visitBody(Element element) {
            for (Attribute attribute : (List<Attribute>) element.getAttributes()) {
                String name = attribute.getName();
                String value = attribute.getValue();

                if (DOMEventHelpers.isLoadEventAttribute(name) || DOMEventHelpers.isUnloadEventAttribute(name)) {
                    jsEventList.add(new JavaScriptSource(filename, name, value, getElementLinenumber(element)));
                }
            }
        }

    }

    /**
     * Configures a new JTidy instance.
     *
     * @return Returns a new configured JTidy instance.
     */
    private static Tidy newTidy() {
        Tidy tidy = new Tidy();
        tidy.setMessageListener(new TidyMessageListener() {

            @Override
            public void messageReceived(TidyMessage msg) {
    			if (logger.isDebugEnabled()) 
    				logger.debug(String.format("HTML warning at %s:%s : %s", new Object[]{
    						msg.getLine(), msg.getColumn(), msg.getMessage()}));
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

    private static class JavaScriptVisitor extends HTMLVisitorImpl {

        private List<JavaScriptSource> fileToJS = Collections.newList();

        private final String htmlFileName;
        private final File file;

        private JavaScriptVisitor(Document document, String htmlFileName) {
            super(document);
            this.htmlFileName = htmlFileName;
            this.file = new File(htmlFileName);
        }

        public List<JavaScriptSource> getJavaScript() {
            return fileToJS;
        }

        @Override
        public void visitScript(Element element) {
            LineNumberElement elm = (LineNumberElement) element;
            String src = element.getAttributeValue("src");
            
            if (src == null) {
                // Embedded script
                fileToJS.add(new JavaScriptSource(htmlFileName, element.getText() + "\n", elm.getStartLine()));
            } else {
                // External script

                if (Options.isDSLEnabled()) {
                    // Libraries for which we have models
                    if ("phonegap.js".equals(src)) {
                        return;
                    }
                }

                // Script in file?
                String pathname = file.getParent() + File.separator + src;
                File srcFile = new File(pathname);
                if (srcFile.exists()) {
                    String s = readScriptFile(srcFile);
                    if (s != null) {
                        fileToJS.add(new JavaScriptSource(src, s, elm.getStartLine()));
                    }
                    return;
                }

                // Script in url?
                URL url;
                try {
                    url = new URL(src);
                    String s = readScriptURL(url);
                    if (s != null) {
                        fileToJS.add(new JavaScriptSource(src, s, elm.getStartLine()));
                    }
                } catch (MalformedURLException e) {
                    throw new AnalysisException(e);
                }

            }
        }

        private static String readScriptFile(File file) {
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file)); // TODO: resource leak?
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
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(url.openStream()));
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
