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

package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.dom.ajax.AjaxBuilder;
import dk.brics.tajs.analysis.dom.core.CoreBuilder;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.core.DOMNamedNodeMap;
import dk.brics.tajs.analysis.dom.event.Event;
import dk.brics.tajs.analysis.dom.event.EventBuilder;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.dom.html.HTMLCollection;
import dk.brics.tajs.analysis.dom.html5.AudioContext;
import dk.brics.tajs.analysis.dom.html5.AudioDestinationNode;
import dk.brics.tajs.analysis.dom.html5.HTML5Builder;
import dk.brics.tajs.analysis.dom.html5.OscillatorNode;
import dk.brics.tajs.analysis.dom.html5.ScriptProcessorNode;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.analysis.dom.style.StyleBuilder;
import dk.brics.tajs.analysis.dom.view.ViewBuilder;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.Arrays;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Setup the DOM browser model.
 * <p>
 * An overview is available at:
 * <p>
 * http://dsmith77.files.wordpress.com/2008/07/the-document-object-model-dom.gif
 * <p>
 * DOM Spec:
 * http://www.w3.org/DOM/DOMTR
 * <p>
 * DOM LEVEL 1:
 * http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/ecma-script-language-binding.html
 * <p>
 * DOM Level 2 Core:
 * http://www.w3.org/TR/DOM-Level-2-Core/core.html
 * http://www.w3.org/TR/DOM-Level-2-Core/ecma-script-binding.html
 * <p>
 * DOM LEVEL 2 HTML:
 * http://www.w3.org/TR/DOM-Level-2-HTML/ecma-script-binding.html
 * <p>
 * DOM LEVEL 2: Traversal
 * http://www.w3.org/TR/DOM-Level-2-Traversal-Range/Overview.html
 */
public class DOMBuilder {

    private static final Set<ObjectLabel> ALL_HTML_OBJECT_LABELS = Collections.newSet();

    private static boolean isDoneBuildingHTMLObjectLabels = false;

    /**
     * Construct the initial DOM objects.
     * Its assumed that WINDOW is added to the state somewhere else before this function is invoked since its the
     * global objects when running in DOM mode.
     */
    public static void addInitialState(State s, Source document) {
        isDoneBuildingHTMLObjectLabels = false;
        // Reset DOM Registry
        DOMRegistry.reset();

        // Build window object
        DOMWindow.WINDOW = InitialStateBuilder.GLOBAL;
        DOMWindow.build(s);

        // Build initial core dom state
        CoreBuilder.build(s);

        // Build initial style state
        StyleBuilder.build(s);

        // Build initial html state
        HTMLBuilder.build(s);

        // Build initial html5 state
        HTML5Builder.build(s);

        // Build initial event state
        EventBuilder.build(s);

        // Build initial views state
        ViewBuilder.build(s);

        // Build initial AJAX state
        AjaxBuilder.build(s);

        ALL_HTML_OBJECT_LABELS.addAll(HTML5Builder.HTML5_OBJECT_LABELS);
        ALL_HTML_OBJECT_LABELS.addAll(HTMLBuilder.HTML4_OBJECT_LABELS);

        isDoneBuildingHTMLObjectLabels = true;

        // Set some shared properties on DOM elements due to circularity, and convenience
        s.writeProperty(singleton(HTMLCollection.INSTANCES), Value.makeAnyStrUInt(), Value.makeObject(ALL_HTML_OBJECT_LABELS), false, false);

        Value cssProperty = Value.makeObject(CSSStyleDeclaration.STYLEDECLARATION).setReadOnly();
        Value htmlElementsProperty = Value.makeObject(ALL_HTML_OBJECT_LABELS).joinNull().setReadOnly();
        Value uintProperty = Value.makeAnyNumUInt().setReadOnly();

        for (ObjectLabel element : ALL_HTML_OBJECT_LABELS) {
            createDOMProperty(s, element, "clientWidth", uintProperty);
            createDOMProperty(s, element, "clientHeight", uintProperty);
            createDOMProperty(s, element, "scrollWidth", uintProperty);
            createDOMProperty(s, element, "scrollHeight", uintProperty);
            createDOMProperty(s, element, "style", cssProperty);
            createDOMProperty(s, element, "firstChild", htmlElementsProperty);
            createDOMProperty(s, element, "parentNode", htmlElementsProperty);
            createDOMProperty(s, element, "lastChild", htmlElementsProperty);
            createDOMProperty(s, element, "previousSibling", htmlElementsProperty);
            createDOMProperty(s, element, "nextSibling", htmlElementsProperty);
            createDOMProperty(s, element, "children", Value.makeObject(HTMLCollection.INSTANCES));
            createDOMProperty(s, element, "attributes", Value.makeObject(DOMNamedNodeMap.INSTANCES));
            createDOMProperty(s, element, "ownerDocument", Value.makeObject(DOMDocument.INSTANCES).joinNull().setReadOnly());
        }

        createDOMProperty(s, Event.PROTOTYPE, "target", Value.makeObject(getAllDOMEventTargets()));
        createDOMProperty(s, Event.PROTOTYPE, "currentTarget", htmlElementsProperty);
        createDOMProperty(s, MouseEvent.INSTANCES, "relatedTarget", htmlElementsProperty);

        Set<ObjectLabel> htmlElementsAndWindow = newSet(ALL_HTML_OBJECT_LABELS);
        htmlElementsAndWindow.add(DOMWindow.WINDOW);
        for (ObjectLabel element : htmlElementsAndWindow) {
            createDOMProperty(s, element, "onsubmit", Value.makeNull());
            createDOMProperty(s, element, "onchange", Value.makeNull());
        }

        createDOMProperty(s, DOMDocument.INSTANCES, "defaultView", Value.makeObject(DOMWindow.WINDOW).joinNull().setReadOnly());

        for (ObjectLabel instance : Arrays.asList(AudioDestinationNode.INSTANCES, ScriptProcessorNode.INSTANCES, OscillatorNode.INSTANCES) /* + other instances of AudioNode ... */) {
            createDOMProperty(s, instance, "context", Value.makeObject(AudioContext.INSTANCES).setReadOnly());
            createDOMProperty(s, instance, "numberOfInputs", Value.makeAnyNum().setReadOnly());
            createDOMProperty(s, instance, "numberOfOutputs", Value.makeAnyNum().setReadOnly());
            createDOMProperty(s, instance, "channelCount", Value.makeAnyNum());
            createDOMProperty(s, instance, "channelCountMode", Value.makeAnyStr());
            createDOMProperty(s, instance, "channelInterpretation", Value.makeAnyStr());
        }

        if (document != null) {
            buildHTML(s, document);
        }else{
            DOMFunctions.makeAnyHTMLNodeList(s);
        }
    }


    /**
     * Build model of the HTML page.
     */
    private static void buildHTML(State s, Source document) { // TODO: (#118) more precise models of the HTML DOM?
        // Ignore HTML content?
        if (Options.get().isIgnoreHTMLContent()) {
            return;
        }

        for (Element element : document.getAllElements()) {

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

    public static Set<ObjectLabel> getAllDOMEventTargets() {
        Set<ObjectLabel> targets = newSet();
        targets.addAll(getAllHtmlObjectLabels());
        targets.add(DOMWindow.WINDOW);
        return targets;
    }

    public static Set<ObjectLabel> getAllHtmlObjectLabels() {
        if (!isDoneBuildingHTMLObjectLabels) {
            throw new AnalysisException("DOM is not done building, can not request object labels yet.");
        }
        return ALL_HTML_OBJECT_LABELS;
    }
}
