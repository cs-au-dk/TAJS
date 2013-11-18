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

package dk.brics.tajs.analysis.dom.html;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

import java.util.Set;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

public class HTMLBuilder {

    public static final Set<ObjectLabel> HTML_OBJECT_LABELS = Collections.newSet();

    /**
     * Build HTML objects
     */
    public static void build(State s) {
        HTMLCollection.build(s);
        HTMLOptionsCollection.build(s);
        HTMLDocument.build(s);
        HTMLElement.build(s);
        HTMLFormElement.build(s);

        HTMLAnchorElement.build(s);
        HTMLAppletElement.build(s);
        HTMLAreaElement.build(s);
        HTMLBaseElement.build(s);
        HTMLBaseFontElement.build(s);
        HTMLBodyElement.build(s);
        HTMLBRElement.build(s);
        HTMLButtonElement.build(s);
        HTMLDirectoryElement.build(s);
        HTMLDivElement.build(s);
        HTMLDListElement.build(s);
        HTMLFieldSetElement.build(s);
        HTMLFontElement.build(s);
        HTMLFrameElement.build(s);
        HTMLFrameSetElement.build(s);
        HTMLHeadElement.build(s);
        HTMLHeadingElement.build(s);
        HTMLHRElement.build(s);
        HTMLHtmlElement.build(s);
        HTMLIFrameElement.build(s);
        HTMLImageElement.build(s);
        HTMLInputElement.build(s);
        HTMLIsIndexElement.build(s);
        HTMLLabelElement.build(s);
        HTMLLegendElement.build(s);
        HTMLLIElement.build(s);
        HTMLLinkElement.build(s);
        HTMLMapElement.build(s);
        HTMLMenuElement.build(s);
        HTMLMetaElement.build(s);
        HTMLModElement.build(s);
        HTMLObjectElement.build(s);
        HTMLOListElement.build(s);
        HTMLOptGroupElement.build(s);
        HTMLOptionElement.build(s);
        HTMLParagraphElement.build(s);
        HTMLParamElement.build(s);
        HTMLPreElement.build(s);
        HTMLQuoteElement.build(s);
        HTMLScriptElement.build(s);
        HTMLSelectElement.build(s);
        HTMLStyleElement.build(s);
        HTMLTableCaptionElement.build(s);
        HTMLTableCellElement.build(s);
        HTMLTableColElement.build(s);
        HTMLTableRowElement.build(s);
        HTMLTableSectionElement.build(s);
        HTMLTableElement.build(s);
        HTMLTextAreaElement.build(s);
        HTMLTitleElement.build(s);
        HTMLUListElement.build(s);

        HTML_OBJECT_LABELS.add(HTMLAnchorElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLAppletElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLAreaElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLBaseElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLBaseFontElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLBodyElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLBRElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLButtonElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLCollection.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLDirectoryElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLDivElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLDListElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLDocument.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLElement.ELEMENT);
        HTML_OBJECT_LABELS.add(HTMLFieldSetElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLFontElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLFormElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLFrameElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLFrameSetElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLHeadElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLHeadingElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLHRElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLHtmlElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLIFrameElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLImageElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLInputElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLIsIndexElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLLabelElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLLegendElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLLIElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLLinkElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLMapElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLMenuElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLMetaElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLModElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLObjectElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLOListElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLOptGroupElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLOptionElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLOptionsCollection.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLParagraphElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLParamElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLPreElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLQuoteElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLScriptElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLSelectElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLStyleElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableCaptionElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableCellElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableColElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableRowElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTableSectionElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTextAreaElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLTitleElement.INSTANCES);
        HTML_OBJECT_LABELS.add(HTMLUListElement.INSTANCES);

        // Write documentElement (due to cyclic dependency) and summarize DOMDocument.
        createDOMProperty(s, DOMDocument.INSTANCES, "documentElement", Value.makeObject(HTMLHtmlElement.INSTANCES).setReadOnly());
        s.multiplyObject(DOMDocument.INSTANCES);
        DOMDocument.INSTANCES = DOMDocument.INSTANCES.makeSingleton().makeSummary();
        
        // Set the remaining properties on DOMNode, due to circularity
        createDOMProperty(s, DOMNode.PROTOTYPE, "firstChild", Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS).joinNull().setReadOnly());
        createDOMProperty(s, DOMNode.PROTOTYPE, "parentNode", Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS).joinNull().setReadOnly());
        createDOMProperty(s, DOMNode.PROTOTYPE, "lastChild", Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS).setReadOnly());
        createDOMProperty(s, DOMNode.PROTOTYPE, "previousSibling", Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS).joinNull().setReadOnly());
        createDOMProperty(s, DOMNode.PROTOTYPE, "nextSibling", Value.makeObject(HTMLBuilder.HTML_OBJECT_LABELS).joinNull().setReadOnly());

    }

}
