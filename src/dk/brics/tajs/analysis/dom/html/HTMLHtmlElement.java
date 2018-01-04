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

package dk.brics.tajs.analysis.dom.html;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * Root of an HTML document. See the HTML element definition in HTML 4.01.
 */
public class HTMLHtmlElement {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONSTRUCTOR = ObjectLabel.make(DOMObjects.HTMLHTMLELEMENT_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.HTMLHTMLELEMENT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.HTMLHTMLELEMENT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "HTMLHtmlElement", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(HTMLElement.ELEMENT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        // DOM Level 0
        createDOMProperty(INSTANCES, "clientWidth", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "clientHeight", Value.makeAnyStr(), c);
        createDOMProperty(INSTANCES, "nodeName", Value.makeStr("HTML"), c);

        // DOM Level 1
        createDOMProperty(INSTANCES, "version", Value.makeAnyStr(), c);

        // DOM LEVEL 2
        createDOMProperty(INSTANCES, "nodeName", Value.makeStr("HTML").setReadOnly(), c);

        // Event handler attributes
        createDOMProperty(INSTANCES, "onabort", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onauxclick", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onbeforecopy", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onbeforecut", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onbeforepaste", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onblur", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncancel", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncanplay", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncanplaythrough", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onchange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onclick", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onclose", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncontextmenu", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncopy", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncuechange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oncut", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondblclick", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondrag", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondragend", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondragenter", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondragleave", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondragover", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondragstart", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondrop", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ondurationchange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onemptied", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onended", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onerror", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onfocus", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ongotpointercapture", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oninput", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "oninvalid", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onkeydown", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onkeypress", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onkeyup", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onload", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onloadeddata", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onloadedmetadata", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onloadstart", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onlostpointercapture", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmousedown", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmouseenter", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmouseleave", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmousemove", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmouseout", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmouseover", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmouseup", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onmousewheel", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpaste", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpause", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onplay", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onplaying", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointercancel", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerdown", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerenter", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerleave", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointermove", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerout", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerover", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onpointerup", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onprogress", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onratechange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onreset", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onresize", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onscroll", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onsearch", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onseeked", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onseeking", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onselect", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onselectstart", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onshow", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onstalled", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onsubmit", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onsuspend", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ontimeupdate", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "ontoggle", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onvolumechange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onwaiting", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onwebkitfullscreenchange", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onwebkitfullscreenerror", Value.makeNull(), c);
        createDOMProperty(INSTANCES, "onwheel", Value.makeNull(), c);

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        // No functions.
    }
}
