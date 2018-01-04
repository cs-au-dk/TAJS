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

package dk.brics.tajs.analysis.dom.core;

import dk.brics.tajs.analysis.Solver;

public class CoreBuilder {

    public static void build(Solver.SolverInterface c) {
        DOMNodeList.build(c);
        DOMTokenList.build(c);
        DOMNode.build(c);
        DOMAttr.build(c);
        DOMNamedNodeMap.build(c);
        DOMDocumentType.build(c);
        DOMException.build(c);
        DOMElement.build(c);
        DOMCharacterData.build(c);
        DOMText.build(c);
        DOMConfiguration.build(c);
        DOMNotation.build(c);
        DOMCDataSection.build(c);
        DOMComment.build(c);
        DOMEntity.build(c);
        DOMEntityReference.build(c);
        DOMProcessingInstruction.build(c);
        DOMStringList.build(c);
        DOMStringMap.build(c);
        DOMDocumentFragment.build(c);
        // Document
        DOMDocument.build(c);
        DOMImplementation.build(c);
        //Touch
        DOMTouchList.build(c);
        DOMTouch.build(c);
    }
}
