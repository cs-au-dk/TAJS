/*
 * Copyright 2009-2019 Aarhus University
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
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.ajax.AjaxBuilder;
import dk.brics.tajs.analysis.dom.ajax.ReadystateEvent;
import dk.brics.tajs.analysis.dom.core.CoreBuilder;
import dk.brics.tajs.analysis.dom.core.DOMAttr;
import dk.brics.tajs.analysis.dom.core.DOMCDataSection;
import dk.brics.tajs.analysis.dom.core.DOMCharacterData;
import dk.brics.tajs.analysis.dom.core.DOMComment;
import dk.brics.tajs.analysis.dom.core.DOMDocument;
import dk.brics.tajs.analysis.dom.core.DOMDocumentFragment;
import dk.brics.tajs.analysis.dom.core.DOMDocumentType;
import dk.brics.tajs.analysis.dom.core.DOMElement;
import dk.brics.tajs.analysis.dom.core.DOMEntity;
import dk.brics.tajs.analysis.dom.core.DOMEntityReference;
import dk.brics.tajs.analysis.dom.core.DOMNamedNodeMap;
import dk.brics.tajs.analysis.dom.core.DOMNode;
import dk.brics.tajs.analysis.dom.core.DOMNodeList;
import dk.brics.tajs.analysis.dom.core.DOMNotation;
import dk.brics.tajs.analysis.dom.core.DOMProcessingInstruction;
import dk.brics.tajs.analysis.dom.core.DOMText;
import dk.brics.tajs.analysis.dom.core.DOMTouch;
import dk.brics.tajs.analysis.dom.core.DOMTouchList;
import dk.brics.tajs.analysis.dom.event.EventBuilder;
import dk.brics.tajs.analysis.dom.event.HashChangeEvent;
import dk.brics.tajs.analysis.dom.event.KeyboardEvent;
import dk.brics.tajs.analysis.dom.event.LoadEvent;
import dk.brics.tajs.analysis.dom.event.MouseEvent;
import dk.brics.tajs.analysis.dom.event.MutationEvent;
import dk.brics.tajs.analysis.dom.event.TouchEvent;
import dk.brics.tajs.analysis.dom.event.WheelEvent;
import dk.brics.tajs.analysis.dom.html.HTMLBodyElement;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.dom.html.HTMLCollection;
import dk.brics.tajs.analysis.dom.html.HTMLDocument;
import dk.brics.tajs.analysis.dom.html.HTMLElement;
import dk.brics.tajs.analysis.dom.html.HTMLHeadElement;
import dk.brics.tajs.analysis.dom.html.HTMLOptionElement;
import dk.brics.tajs.analysis.dom.html.HTMLOptionsCollection;
import dk.brics.tajs.analysis.dom.html.HTMLScriptElement;
import dk.brics.tajs.analysis.dom.html5.AudioContext;
import dk.brics.tajs.analysis.dom.html5.AudioDestinationNode;
import dk.brics.tajs.analysis.dom.html5.HTML5Builder;
import dk.brics.tajs.analysis.dom.html5.HTMLCanvasElement;
import dk.brics.tajs.analysis.dom.html5.OscillatorNode;
import dk.brics.tajs.analysis.dom.html5.ScriptProcessorNode;
import dk.brics.tajs.analysis.dom.style.CSSStyleDeclaration;
import dk.brics.tajs.analysis.dom.style.StyleBuilder;
import dk.brics.tajs.analysis.dom.view.ViewBuilder;
import dk.brics.tajs.analysis.dom.xpath.XPathResult;
import dk.brics.tajs.flowgraph.EventType;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collections;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.html5.CanvasRenderingContext2D.CONTEXT2D;
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
    private static final Set<ObjectLabel> ALL_NODE_OBJECT_LABELS = Collections.newSet();

    private static boolean isDoneBuildingHTMLObjectLabels = false;

    /**
     * Construct the initial DOM objects.
     * Its assumed that WINDOW is added to the state somewhere else before this function is invoked since its the
     * global objects when running in DOM mode.
     */
    public static void build(Source document, Solver.SolverInterface c) {
        isDoneBuildingHTMLObjectLabels = false;
        // Reset DOM Registry
        DOMRegistry.reset();

        // Build window object
        DOMWindow.WINDOW = InitialStateBuilder.GLOBAL;
        DOMWindow.build(c);

        // Build initial core dom state
        CoreBuilder.build(c);

        // Build initial style state
        StyleBuilder.build(c);

        // Build initial html state
        HTMLBuilder.build(c);

        // Build initial html5 state
        HTML5Builder.build(c);

        // Build initial event state
        EventBuilder.build(c);

        // Build initial views state
        ViewBuilder.build(c);

        // Build initial views state
        XPathResult.build(c);

        // Build initial AJAX state
        AjaxBuilder.build(c);

        ALL_HTML_OBJECT_LABELS.addAll(HTML5Builder.HTML5_OBJECT_LABELS);
        ALL_HTML_OBJECT_LABELS.addAll(HTMLBuilder.HTML4_OBJECT_LABELS);

        ALL_NODE_OBJECT_LABELS.addAll(ALL_HTML_OBJECT_LABELS);
        ALL_NODE_OBJECT_LABELS.add(DOMDocumentType.INSTANCES);
        ALL_NODE_OBJECT_LABELS.add(DOMText.INSTANCES);

        isDoneBuildingHTMLObjectLabels = true;

        // Set some shared properties on DOM elements due to circularity, and convenience
        c.getAnalysis().getPropVarOperations().writeProperty(singleton(HTMLCollection.INSTANCES), Value.makeAnyStrUInt(), Value.makeObject(ALL_HTML_OBJECT_LABELS), false, true);

        Value cssProperty = Value.makeObject(CSSStyleDeclaration.INSTANCES).setReadOnly();
        Value nodesProperty = Value.makeObject(ALL_NODE_OBJECT_LABELS).joinNull().setReadOnly();
        Value htmlElementsProperty = Value.makeObject(ALL_HTML_OBJECT_LABELS).joinNull().setReadOnly();
        Value uintProperty = Value.makeAnyNumUInt().setReadOnly();

        createDOMProperty(TouchEvent.INSTANCES, "changedTouches", Value.makeObject(DOMTouchList.INSTANCES).setReadOnly(), c);
        createDOMProperty(TouchEvent.INSTANCES, "targetTouches", Value.makeObject(DOMTouchList.INSTANCES).setReadOnly(), c);
        createDOMProperty(TouchEvent.INSTANCES, "touches", Value.makeObject(DOMTouchList.INSTANCES).setReadOnly(), c);
        createDOMProperty(DOMTouch.PROTOTYPE, "target", DOMFunctions.makeAnyHTMLElement().setReadOnly(), c);

        Value nodes = Value.makeObject(getAllDOMNodes()).setReadOnly();
        c.getAnalysis().getPropVarOperations().writeProperty(singleton(DOMNamedNodeMap.INSTANCES), Value.makeAnyStrNotUInt(), nodes);

        c.getAnalysis().getPropVarOperations().writeProperty(singleton(HTMLOptionsCollection.INSTANCES), Value.makeAnyStrUInt(), Value.makeObject(HTMLOptionElement.INSTANCES));

        Set<ObjectLabel> htmlAndDOMObjects = newSet(ALL_HTML_OBJECT_LABELS);
        htmlAndDOMObjects.addAll(getAllDOMNodes());
        for (ObjectLabel element : htmlAndDOMObjects) {
            createDOMProperty(element, "parentNode", htmlElementsProperty, c);
            createDOMProperty(element, "firstChild", nodesProperty, c);
            createDOMProperty(element, "firstElementChild", htmlElementsProperty, c);
            createDOMProperty(element, "lastChild", htmlElementsProperty, c);
            createDOMProperty(element, "lastElementChild", htmlElementsProperty, c);
            createDOMProperty(element, "previousSibling", htmlElementsProperty, c);
            createDOMProperty(element, "nextSibling", htmlElementsProperty, c);
            createDOMProperty(element, "children", Value.makeObject(HTMLCollection.INSTANCES), c);
            createDOMProperty(element, "ownerDocument", Value.makeObject(HTMLDocument.INSTANCES).joinNull().setReadOnly(), c);
            createDOMProperty(element, "textContent", Value.makeAnyStr(), c);
        }

        for (ObjectLabel element : ALL_HTML_OBJECT_LABELS) {
            createDOMProperty(element, "clientWidth", uintProperty, c);
            createDOMProperty(element, "clientHeight", uintProperty, c);
            createDOMProperty(element, "scrollWidth", uintProperty, c);
            createDOMProperty(element, "scrollHeight", uintProperty, c);
            createDOMProperty(element, "scrollLeft", uintProperty, c);
            createDOMProperty(element, "scrollLeftMax", uintProperty, c);
            createDOMProperty(element, "scrollTop", uintProperty, c);
            createDOMProperty(element, "scrollTopMax", uintProperty, c);
            createDOMProperty(element, "style", cssProperty, c);
            createDOMProperty(element, "attributes", Value.makeObject(DOMNamedNodeMap.INSTANCES), c);
            createDOMProperty(element, "offsetParent", htmlElementsProperty, c);
            createDOMProperty(element, "innerHTML", Value.makeAnyStr(), c);
            createDOMProperty(element, "outerHTML", Value.makeAnyStr(), c);
            createDOMProperty(element, "id", Value.makeAnyStr(), c);
            createDOMProperty(element, "name", Value.makeAnyStr(), c);
        }

        // Properties ownerDocument, parentNode also exists on DOMText
        createDOMProperty(DOMNode.PROTOTYPE, "ownerDocument", Value.makeObject(DOMDocument.INSTANCES).joinNull().setReadOnly(), c);
        createDOMProperty(DOMNode.PROTOTYPE, "parentNode", htmlElementsProperty, c);

        createDOMProperty(DOMDocument.INSTANCES, "activeElement", htmlElementsProperty, c);
        createDOMProperty(DOMDocument.INSTANCES, "currentScript", Value.makeObject(HTMLScriptElement.INSTANCES).joinNull(), c);

        createDOMProperty(DOMDocumentFragment.INSTANCES, "firstChild", htmlElementsProperty, c);
        createDOMProperty(DOMDocumentFragment.INSTANCES, "lastChild", htmlElementsProperty, c);
        createDOMProperty(DOMDocumentFragment.INSTANCES, "previousSibling", htmlElementsProperty, c);
        createDOMProperty(DOMDocumentFragment.INSTANCES, "nextSibling", htmlElementsProperty, c);
        createDOMProperty(DOMDocumentFragment.INSTANCES, "children", Value.makeObject(HTMLCollection.INSTANCES), c);

        Stream.of(HashChangeEvent.INSTANCES,
                KeyboardEvent.INSTANCES,
                MouseEvent.INSTANCES,
                ReadystateEvent.INSTANCES,
                LoadEvent.INSTANCES,
                MutationEvent.INSTANCES,
                WheelEvent.INSTANCES,
                TouchEvent.INSTANCES).forEach(eventInstance -> {
            createDOMProperty(eventInstance, "target", Value.makeObject(getAllDOMEventTargets()), c);
            createDOMProperty(eventInstance, "srcElement", Value.makeObject(getAllDOMEventTargets()), c);
            createDOMProperty(eventInstance, "fromElement", htmlElementsProperty.joinAbsent(), c);
            createDOMProperty(eventInstance, "currentTarget", htmlElementsProperty, c);
            createDOMProperty(eventInstance, "toElement", htmlElementsProperty.joinAbsent(), c);
            createDOMProperty(eventInstance, "defaultPrevented", Value.makeAnyBool(), c);
            createDOMProperty(eventInstance, "returnValue", Value.makeAnyBool(), c);
        });
        createDOMProperty(MouseEvent.INSTANCES, "relatedTarget", htmlElementsProperty, c);

        Set<ObjectLabel> htmlElementsAndWindow = newSet(ALL_HTML_OBJECT_LABELS);
        htmlElementsAndWindow.add(DOMWindow.WINDOW);
        for (ObjectLabel element : htmlElementsAndWindow) {
            createDOMProperty(element, "onsubmit", Value.makeNull(), c);
            createDOMProperty(element, "onchange", Value.makeNull(), c);
        }

        createDOMProperty(DOMDocument.INSTANCES, "defaultView", Value.makeObject(DOMWindow.WINDOW).joinNull().setReadOnly(), c);
        createDOMProperty(DOMDocument.INSTANCES, "body", Value.makeObject(HTMLBodyElement.INSTANCES).joinNull().setReadOnly(), c);
        createDOMProperty(DOMDocument.INSTANCES, "head", Value.makeObject(HTMLHeadElement.INSTANCES).setReadOnly(), c);

        for (ObjectLabel instance : Arrays.asList(AudioDestinationNode.INSTANCES, ScriptProcessorNode.INSTANCES, OscillatorNode.INSTANCES) /* + other instances of AudioNode ... */) {
            createDOMProperty(instance, "context", Value.makeObject(AudioContext.INSTANCES).setReadOnly(), c);
            createDOMProperty(instance, "numberOfInputs", Value.makeAnyNum().setReadOnly(), c);
            createDOMProperty(instance, "numberOfOutputs", Value.makeAnyNum().setReadOnly(), c);
            createDOMProperty(instance, "channelCount", Value.makeAnyNum(), c);
            createDOMProperty(instance, "channelCountMode", Value.makeAnyStr(), c);
            createDOMProperty(instance, "channelInterpretation", Value.makeAnyStr(), c);
        }

        createDOMProperty(CONTEXT2D, "canvas", Value.makeObject(HTMLCanvasElement.INSTANCES).setReadOnly(), c);

        Collection<ObjectLabel> eventNameContainers = Arrays.asList(HTMLElement.ELEMENT_PROTOTYPE, DOMNamedNodeMap.INSTANCES, DOMWindow.WINDOW);
        writeEventListenerProperties(eventNameContainers, c.getAnalysis().getPropVarOperations());

        c.getAnalysis().getPropVarOperations().writeProperty(singleton(DOMNodeList.INSTANCES), Value.makeAnyStrUInt(), nodesProperty);
        if (document != null) {
            registerHTML(document, c);
        }
    }

    private static Set<ObjectLabel> getAllDOMNodes() {
        Set<ObjectLabel> domNodes = newSet();
        domNodes.add(DOMAttr.INSTANCES);
        domNodes.add(DOMCharacterData.INSTANCES);
        domNodes.add(DOMComment.INSTANCES);
        domNodes.add(DOMText.INSTANCES);
        domNodes.add(DOMCDataSection.INSTANCES);
        domNodes.add(DOMDocument.INSTANCES);
        domNodes.add(DOMDocumentFragment.INSTANCES);
        domNodes.add(DOMDocumentType.INSTANCES);
        domNodes.add(DOMElement.INSTANCES);
        domNodes.add(DOMEntity.INSTANCES);
        domNodes.add(DOMEntityReference.INSTANCES);
        domNodes.add(DOMNode.INSTANCES);
        domNodes.add(DOMNotation.INSTANCES);
        domNodes.add(DOMProcessingInstruction.INSTANCES);
        return domNodes;
    }


    /**
     * Build model of the HTML page, as it looks when loaded, before scripts are invoked.
     */
    public static void registerHTML(Source document, Solver.SolverInterface c) { // TODO: (#118) more precise models of the HTML DOM?
        State s = c.getState();
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

                    // NB: technically, the property resides in a scope *outside* window!
                    c.getAnalysis().getPropVarOperations().writeProperty(DOMWindow.WINDOW, id, Value.makeObject(label));
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

    public static void writeEventListenerProperties(Collection<ObjectLabel> targets, PropVarOperations pv) {
        Arrays.stream(EventType.values()).forEach(type ->
                EventType.getEventHandlerAttributeNames(type).forEach(attributeName ->
                        targets.forEach(target -> {
                                    Value event = DOMEvents.getEvent(type);
                                    boolean maybePresent = type == EventType.OTHER; // support for some of these event types is spotty
                                    pv.writeProperty(singleton(target), Value.makeTemporaryStr(attributeName), Value.makeNull().join(event).setAttributes(false, false, false), maybePresent, true);
                                }
                        )
                )
        );
    }
}
