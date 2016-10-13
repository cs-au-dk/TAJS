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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTMLBuilder {

    public static final Set<ObjectLabel> HTML4_OBJECT_LABELS = Collections.newSet();

    /**
     * Build HTML objects
     */
    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        HTMLCollection.build(c);
        HTMLOptionsCollection.build(c);
        HTMLDocument.build(c);
        HTMLElement.build(c);
        HTMLFormElement.build(c);

        HTMLAnchorElement.build(c);
        HTMLAppletElement.build(c);
        HTMLAreaElement.build(c);
        HTMLBaseElement.build(c);
        HTMLBaseFontElement.build(c);
        HTMLBodyElement.build(c);
        HTMLBRElement.build(c);
        HTMLButtonElement.build(c);
        HTMLDirectoryElement.build(c);
        HTMLDivElement.build(c);
        HTMLDListElement.build(c);
        HTMLFieldSetElement.build(c);
        HTMLFontElement.build(c);
        HTMLFrameElement.build(c);
        HTMLFrameSetElement.build(c);
        HTMLHeadElement.build(c);
        HTMLHeadingElement.build(c);
        HTMLHRElement.build(c);
        HTMLHtmlElement.build(c);
        HTMLIFrameElement.build(c);
        HTMLImageElement.build(c);
        HTMLInputElement.build(c);
        HTMLIsIndexElement.build(c);
        HTMLLabelElement.build(c);
        HTMLLegendElement.build(c);
        HTMLLIElement.build(c);
        HTMLLinkElement.build(c);
        HTMLMapElement.build(c);
        HTMLMenuElement.build(c);
        HTMLMetaElement.build(c);
        HTMLModElement.build(c);
        HTMLObjectElement.build(c);
        HTMLOListElement.build(c);
        HTMLOptGroupElement.build(c);
        HTMLOptionElement.build(c);
        HTMLParagraphElement.build(c);
        HTMLParamElement.build(c);
        HTMLPreElement.build(c);
        HTMLQuoteElement.build(c);
        HTMLScriptElement.build(c);
        HTMLSelectElement.build(c);
        HTMLStyleElement.build(c);
        HTMLTableCaptionElement.build(c);
        HTMLTableCellElement.build(c);
        HTMLTableColElement.build(c);
        HTMLTableRowElement.build(c);
        HTMLTableSectionElement.build(c);
        HTMLTableElement.build(c);
        HTMLTextAreaElement.build(c);
        HTMLTitleElement.build(c);
        HTMLUListElement.build(c);

        HTML4_OBJECT_LABELS.add(HTMLAnchorElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLAppletElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLAreaElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLBaseElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLBaseFontElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLBodyElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLBRElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLButtonElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLDirectoryElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLDivElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLDListElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLDocument.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLElement.ELEMENT);
        HTML4_OBJECT_LABELS.add(HTMLFieldSetElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLFontElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLFormElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLFrameElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLFrameSetElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLHeadElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLHeadingElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLHRElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLHtmlElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLIFrameElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLImageElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLInputElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLIsIndexElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLLabelElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLLegendElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLLIElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLLinkElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLMapElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLMenuElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLMetaElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLModElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLObjectElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLOListElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLOptGroupElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLOptionElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLParagraphElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLParamElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLPreElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLQuoteElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLScriptElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLSelectElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLStyleElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableCaptionElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableCellElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableColElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableRowElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTableSectionElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTextAreaElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLTitleElement.INSTANCES);
        HTML4_OBJECT_LABELS.add(HTMLUListElement.INSTANCES);

        // Write documentElement (due to cyclic dependency) and summarize DOMDocument.
        createDOMProperty(DOMDocument.INSTANCES, "documentElement", Value.makeObject(HTMLHtmlElement.INSTANCES).setReadOnly(), c);
        s.multiplyObject(DOMDocument.INSTANCES);
        DOMDocument.INSTANCES = DOMDocument.INSTANCES.makeSingleton().makeSummary();
    }
}
