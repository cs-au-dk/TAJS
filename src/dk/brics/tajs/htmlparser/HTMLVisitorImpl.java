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

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Pre-order traversal of JDOM HTML document.
 */
public abstract class HTMLVisitorImpl implements HTMLVisitor {

	private static Logger logger = Logger.getLogger(HTMLVisitorImpl.class); 

	private final Document document;

	/**
	 * Constructs a new visitor for the given document.
	 */
    protected HTMLVisitorImpl(Document document) {
        if (document == null)
            throw new NullPointerException();
        this.document = document;
    }

    /**
     * Traverses the document.
     * The element visitor methods do nothing by default.
     */
    @Override
    public void visitDocument() {
  		visit(document.getRootElement());
    }

    /**
     * Dispatches to the visitor method of the name of the given element.
     * The element visitor methods do nothing by default.
     */
    @Override
    public void visit(Element element) {
        String tagName = element.getName();
        if ("a".equalsIgnoreCase(tagName)) {
            visitA(element);
        } else if ("applet".equalsIgnoreCase(tagName)) {
            visitApplet(element);
        } else if ("area".equalsIgnoreCase(tagName)) {
            visitArea(element);
        } else if ("base".equalsIgnoreCase(tagName)) {
            visitBase(element);
        } else if ("basefont".equalsIgnoreCase(tagName)) {
            visitBaseFont(element);
        } else if ("body".equalsIgnoreCase(tagName)) {
            visitBody(element);
        } else if ("br".equalsIgnoreCase(tagName)) {
            visitBR(element);
        } else if ("button".equalsIgnoreCase(tagName)) {
            visitButton(element);
        } else if ("caption".equalsIgnoreCase(tagName)) {
            visitCaption(element);
        } else if ("col".equalsIgnoreCase(tagName)) {
            visitCol(element);
        } else if ("dir".equalsIgnoreCase(tagName)) {
            visitDir(element);
        } else if ("div".equalsIgnoreCase(tagName)) {
            visitDiv(element);
        } else if ("dl".equalsIgnoreCase(tagName)) {
            visitDl(element);
        } else if ("fieldset".equalsIgnoreCase(tagName)) {
            visitFieldSet(element);
        } else if ("font".equalsIgnoreCase(tagName)) {
            visitFont(element);
        } else if ("form".equalsIgnoreCase(tagName)) {
            visitForm(element);
        } else if ("frame".equalsIgnoreCase(tagName)) {
            visitFrame(element);
        } else if ("frameset".equalsIgnoreCase(tagName)) {
            visitFrameSet(element);
        } else if ("h1".equalsIgnoreCase(tagName)) {
            visitH1(element);
        } else if ("h2".equalsIgnoreCase(tagName)) {
            visitH2(element);
        } else if ("h3".equalsIgnoreCase(tagName)) {
            visitH3(element);
        } else if ("h4".equalsIgnoreCase(tagName)) {
            visitH4(element);
        } else if ("h5".equalsIgnoreCase(tagName)) {
            visitH5(element);
        } else if ("h6".equalsIgnoreCase(tagName)) {
            visitH6(element);
        } else if ("head".equalsIgnoreCase(tagName)) {
            visitHead(element);
        } else if ("hr".equalsIgnoreCase(tagName)) {
            visitHR(element);
        } else if ("html".equalsIgnoreCase(tagName)) {
            visitHTML(element);
        } else if ("iframe".equalsIgnoreCase(tagName)) {
            visitIFrame(element);
        } else if ("img".equalsIgnoreCase(tagName)) {
            visitImg(element);
        } else if ("input".equalsIgnoreCase(tagName)) {
            visitInput(element);
        } else if ("label".equalsIgnoreCase(tagName)) {
            visitLabel(element);
        } else if ("legend".equalsIgnoreCase(tagName)) {
            visitLegend(element);
        } else if ("li".equalsIgnoreCase(tagName)) {
            visitLI(element);
        } else if ("link".equalsIgnoreCase(tagName)) {
            visitLink(element);
        } else if ("map".equalsIgnoreCase(tagName)) {
            visitMap(element);
        } else if ("menu".equalsIgnoreCase(tagName)) {
            visitMenu(element);
        } else if ("meta".equalsIgnoreCase(tagName)) {
            visitMeta(element);
        } else if ("object".equalsIgnoreCase(tagName)) {
            visitObject(element);
        } else if ("ol".equalsIgnoreCase(tagName)) {
            visitOL(element);
        } else if ("optgroup".equalsIgnoreCase(tagName)) {
            visitOptGroup(element);
        } else if ("option".equalsIgnoreCase(tagName)) {
            visitOption(element);
        } else if ("p".equalsIgnoreCase(tagName)) {
            visitP(element);
        } else if ("param".equalsIgnoreCase(tagName)) {
            visitParam(element);
        } else if ("pre".equalsIgnoreCase(tagName)) {
            visitPre(element);
        } else if ("script".equalsIgnoreCase(tagName)) {
            visitScript(element);
        } else if ("select".equalsIgnoreCase(tagName)) {
            visitSelect(element);
        } else if ("style".equalsIgnoreCase(tagName)) {
            visitStyle(element);
        } else if ("table".equalsIgnoreCase(tagName)) {
            visitTable(element);
        } else if ("tbody".equalsIgnoreCase(tagName)) {
            visitTBody(element);
        } else if ("td".equalsIgnoreCase(tagName)) {
            visitTD(element);
        } else if ("textarea".equalsIgnoreCase(tagName)) {
            visitTextArea(element);
        } else if ("tfoot".equalsIgnoreCase(tagName)) {
            visitTFoot(element);
        } else if ("th".equalsIgnoreCase(tagName)) {
            visitTH(element);
        } else if ("thead".equalsIgnoreCase(tagName)) {
            visitTHead(element);
        } else if ("title".equalsIgnoreCase(tagName)) {
            visitTitle(element);
        } else if ("tr".equalsIgnoreCase(tagName)) {
            visitTR(element);
        } else if ("ul".equalsIgnoreCase(tagName)) {
            visitUL(element);
        } else {
        	logger.debug("Unknown Element Tag: " + tagName);
        }

        for (Object o : element.getChildren()) {
            if (o instanceof Element)
                visit((Element) o);
        }
    }

    @Override
    public void visitA(Element element) {
    	// do nothing
    }

    @Override
    public void visitApplet(Element element) {
    	// do nothing
    }

    @Override
    public void visitArea(Element element) {
    	// do nothing
    }

    @Override
    public void visitBase(Element element) {
    	// do nothing
    }

    @Override
    public void visitBaseFont(Element element) {
    	// do nothing
    }

    @Override
    public void visitBR(Element element) {
    	// do nothing
    }

    @Override
    public void visitBody(Element element) {
    	// do nothing
    }

    @Override
    public void visitButton(Element element) {
    	// do nothing
    }

    @Override
    public void visitCaption(Element element) {
    	// do nothing
    }

    @Override
    public void visitCol(Element element) {
    	// do nothing
    }

    @Override
    public void visitDir(Element element) {
    	// do nothing
    }

    @Override
    public void visitDiv(Element element) {
    	// do nothing
    }

    @Override
    public void visitDl(Element element) {
    	// do nothing
    }

    @Override
    public void visitFieldSet(Element element) {
    	// do nothing
    }

    @Override
    public void visitFont(Element element) {
    	// do nothing
    }

    @Override
    public void visitForm(Element element) {
    	// do nothing
    }

    @Override
    public void visitFrame(Element element) {
    	// do nothing
    }

    @Override
    public void visitFrameSet(Element element) {
    	// do nothing
    }

    @Override
    public void visitH1(Element element) {
    	// do nothing
    }

    @Override
    public void visitH2(Element element) {
    	// do nothing
    }

    @Override
    public void visitH3(Element element) {
    	// do nothing
    }

    @Override
    public void visitH4(Element element) {
    	// do nothing
    }

    @Override
    public void visitH5(Element element) {
    	// do nothing
    }

    @Override
    public void visitH6(Element element) {
    	// do nothing
    }

    @Override
    public void visitHead(Element element) {
    	// do nothing
    }

    @Override
    public void visitHR(Element element) {
    	// do nothing
    }

    @Override
    public void visitHTML(Element element) {
    	// do nothing
    }

    @Override
    public void visitIFrame(Element element) {
    	// do nothing
    }

    @Override
    public void visitImg(Element element) {
    	// do nothing
    }

    @Override
    public void visitInput(Element element) {
    	// do nothing
    }

    @Override
    public void visitLabel(Element element) {
    	// do nothing
    }

    @Override
    public void visitLegend(Element element) {
    	// do nothing
    }

    @Override
    public void visitLI(Element element) {
    	// do nothing
    }

    @Override
    public void visitLink(Element element) {
    	// do nothing
    }

    @Override
    public void visitMap(Element element) {
    	// do nothing
    }

    @Override
    public void visitMenu(Element element) {
    	// do nothing
    }

    @Override
    public void visitMeta(Element element) {
    	// do nothing
    }

    @Override
    public void visitObject(Element element) {
    	// do nothing
    }

    @Override
    public void visitOL(Element element) {
    	// do nothing
    }

    @Override
    public void visitOptGroup(Element element) {
    	// do nothing
    }

    @Override
    public void visitOption(Element element) {
    	// do nothing
    }

    @Override
    public void visitP(Element element) {
    	// do nothing
    }

    @Override
    public void visitParam(Element element) {
    	// do nothing
    }

    @Override
    public void visitPre(Element element) {
    	// do nothing
    }

    @Override
    public void visitScript(Element element) {
    	// do nothing
    }

    @Override
    public void visitSelect(Element element) {
    	// do nothing
    }

    @Override
    public void visitStyle(Element element) {
    	// do nothing
    }

    @Override
    public void visitTable(Element element) {
    	// do nothing
    }

    @Override
    public void visitTBody(Element element) {
    	// do nothing
    }

    @Override
    public void visitTD(Element element) {
    	// do nothing
    }

    @Override
    public void visitTextArea(Element element) {
    	// do nothing
    }

    @Override
    public void visitTFoot(Element element) {
    	// do nothing
    }

    @Override
    public void visitTH(Element element) {
    	// do nothing
    }

    @Override
    public void visitTHead(Element element) {
    	// do nothing
    }

    @Override
    public void visitTitle(Element element) {
    	// do nothing
    }

    @Override
    public void visitTR(Element element) {
    	// do nothing
    }

    @Override
    public void visitUL(Element element) {
    	// do nothing
    }
}
