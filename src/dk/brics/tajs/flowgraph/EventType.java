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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Auxiliary methods for recognizing HTML attribute names related to event handlers.
 */
public enum EventType { // TODO: (#116) reconsider how to represent HTML event handlers in flowgraphs

    DOM_CONTENT_LOADED("DomContentLoaded"),
    LOAD("LoadEventHandler"),
    UNLOAD("UnloadEventHandler"),
    KEYBOARD("KeyboardEventHandler"),
    MOUSE("MouseEventHandler"),
    UNKNOWN("UnknownEventHandler"),
    OTHER("OtherEventHandler"),
    AJAX("AjaxEventHandler"),
    TIMEOUT("TimeoutEventHandler");

    private static final String attributePrefix = "on";

    private String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isUserEvent() {
        return this == EventType.KEYBOARD || this == EventType.MOUSE || this == EventType.OTHER;
    }

    /**
     * Auxiliary methods for recognizing HTML attribute names related to event handlers.
     */
    private static final Map<String, EventType> eventTypes = newMap();

    static {
        eventTypes.put("load", EventType.LOAD);
        eventTypes.put("DOMContentLoaded", EventType.LOAD);

        eventTypes.put("unload", EventType.UNLOAD);

        eventTypes.put("keypress", EventType.KEYBOARD);
        eventTypes.put("keydown", EventType.KEYBOARD);
        eventTypes.put("keyup", EventType.KEYBOARD);

        eventTypes.put("click", EventType.MOUSE);
        eventTypes.put("dblclick", EventType.MOUSE);
        eventTypes.put("mousedown", EventType.MOUSE);
        eventTypes.put("mouseup", EventType.MOUSE);
        eventTypes.put("mouseover", EventType.MOUSE);
        eventTypes.put("mousemove", EventType.MOUSE);
        eventTypes.put("mouseout", EventType.MOUSE);

        eventTypes.put("readystatechange", EventType.AJAX);

        eventTypes.put("focus", EventType.OTHER);
        eventTypes.put("focusin", EventType.OTHER);
        eventTypes.put("focusout", EventType.OTHER);
        eventTypes.put("hashchange", EventType.OTHER);
        eventTypes.put("blur", EventType.OTHER);
        eventTypes.put("submit", EventType.OTHER);
        eventTypes.put("reset", EventType.OTHER);
        eventTypes.put("select", EventType.OTHER);
        eventTypes.put("selectstart", EventType.OTHER);
        eventTypes.put("selectend", EventType.OTHER);
        eventTypes.put("change", EventType.OTHER);
        eventTypes.put("resize", EventType.OTHER);
        eventTypes.put("touchstart", EventType.OTHER);
        eventTypes.put("touchend", EventType.OTHER);
        eventTypes.put("touchmove", EventType.OTHER);
        eventTypes.put("touchcancel", EventType.OTHER);
        eventTypes.put("orientationchange", EventType.OTHER); // TODO: make event-object for this event type?
    }

    /**
     * All event type names, e.g. click, reset, submit.
     */
    public static Set<String> getAllEventTypeNames() {
        return eventTypes.keySet();
    }

    public static EventType getEventHandlerTypeFromString(String typeString) {
        return eventTypes.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(typeString))
                .map(Map.Entry::getValue)
                .findAny().orElse(EventType.UNKNOWN);
    }

    /**
     * The DOM element attribute names that can trigger the event type, e.g. MOUSE -> {onclick, ondblclick, ...}.
     */
    public static Collection<String> getEventHandlerAttributeNames(EventType type) {
        return eventTypes.entrySet().stream()
                .filter(e -> e.getValue() == type)
                .map(e -> attributePrefix + e.getKey())
                .collect(Collectors.toList());
    }

    /**
     * The event type that can be triggered by the DOM element attribute name, e.g. onclick -> MOUSE.
     */
    public static EventType getEventHandlerTypeFromAttributeName(String eventName) {
        if (!eventName.startsWith(attributePrefix)) {
            return EventType.UNKNOWN;
        }
        return getEventHandlerTypeFromString(convertEventAttributeToType(eventName));
    }

    private static String convertEventAttributeToType(String eventName) {
        if (!eventName.startsWith(attributePrefix)) {
            throw new AnalysisException("Inconsistent use of attribute names: " + eventName);
        }
        return eventName.substring(attributePrefix.length());
    }
}
