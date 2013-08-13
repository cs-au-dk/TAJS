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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.State;
import dk.brics.tajs.htmlparser.HTMLVisitorImpl;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.options.Options;
import org.jdom.Document;
import org.jdom.Element;

import java.util.Collections;

public class DOMVisitor extends HTMLVisitorImpl {

    private final State s;

    public DOMVisitor(Document document, State s) {
        super(document);
        this.s = s;
    }

    @Override
    public void visit(Element element) {
        super.visit(element);

        // Ignore HTML content?
        if (Options.isIgnoreHTMLContent()) {
            return;
        }

        // Pick up special properties
        ObjectLabel label = DOMFunctions.getHTMLObjectLabel(element.getName());
        if (label != null) {
            // Special Property: id
            String id = element.getAttributeValue("id");
            if (id != null) {
                s.getExtras().addToMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_ID.name(), id, Collections.singleton(label));

                // TODO An element with id FOO is available as FOO
                // s.writeProperty(DOMWindow.WINDOW, id, Value.makeObject(label));
            }

            // Special Property: name
            String name = element.getAttributeValue("name");
            if (name != null) {
                s.getExtras().addToMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_NAME.name(), name, dk.brics.tajs.util.Collections.singleton(label));
            }

            // Special Property: tagName
            String tagname = element.getName();
            if (tagname != null) {
                s.getExtras().addToMayMap(DOMRegistry.MayMaps.ELEMENTS_BY_TAGNAME.name(), tagname, Collections.singleton(label));
            }
        }

    }

}
