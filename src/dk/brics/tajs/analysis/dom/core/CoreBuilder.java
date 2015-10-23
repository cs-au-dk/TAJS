/*
 * Copyright 2009-2015 Aarhus University
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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class CoreBuilder {

    public static void build(State s) {
        DOMNodeList.build(s);
        DOMNode.build(s);
        DOMAttr.build(s);
        DOMNamedNodeMap.build(s);
        DOMDocumentType.build(s);
        DOMException.build(s);
        DOMElement.build(s);
        DOMCharacterData.build(s);
        DOMText.build(s);
        DOMConfiguration.build(s);
        DOMNotation.build(s);
        DOMCDataSection.build(s);
        DOMComment.build(s);
        DOMEntity.build(s);
        DOMEntityReference.build(s);
        DOMProcessingInstruction.build(s);
        DOMStringList.build(s);
        DOMDocumentFragment.build(s);

        // Document
        DOMDocument.build(s);
        DOMImplementation.build(s);
    }
}
