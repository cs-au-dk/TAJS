/*
 * Copyright 2012 Aarhus University
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

import dk.brics.tajs.lattice.ObjectLabel;

public class DOMRegistry {

    public static enum MaySets {
        LOAD_EVENT_HANDLER,
        UNLOAD_EVENT_HANDLERS,

        KEYBOARD_EVENT_HANDLER,
        MOUSE_EVENT_HANDLER,
        AJAX_EVENT_HANDLER,
        TIMEOUT_EVENT_HANDLERS,
        UNKNOWN_EVENT_HANDLERS
    }

    public static enum MustSets {
        LOAD_EVENT_HANDLER
    }

    public static enum MayMaps {
        ELEMENTS_BY_ID,
        ELEMENTS_BY_NAME,
        ELEMENTS_BY_TAGNAME
    }

    private static ObjectLabel keyboardEvent;
    private static ObjectLabel mouseEvent;
    private static ObjectLabel ajaxEvent;
    private static ObjectLabel mutationEvent;
    private static ObjectLabel wheelEvent;

    public static void reset() {
        keyboardEvent = null;
        mouseEvent = null;
        ajaxEvent = null;
        mutationEvent = null;
        wheelEvent = null;
    }

    public static void registerKeyboardEventLabel(ObjectLabel l) {
        keyboardEvent = l;
    }

    public static void registerMouseEventLabel(ObjectLabel l) {
        mouseEvent = l;
    }

    public static void registerAjaxEventLabel(ObjectLabel l) {
        ajaxEvent = l;
    }

    public static void registerMutationEventLabel(ObjectLabel l) {
        mutationEvent = l;
    }

    public static void registerWheelEventLabel(ObjectLabel l) {
        wheelEvent = l;
    }

    public static ObjectLabel getKeyboardEventLabel() {
        if (keyboardEvent == null) {
            throw new IllegalStateException("No keyboard event object labels registered");
        }
        return keyboardEvent;
    }

    public static ObjectLabel getMouseEventLabel() {
        if (mouseEvent == null) {
            throw new IllegalStateException("No mouse event object labels registered");
        }
        return mouseEvent;
    }

    public static ObjectLabel getAjaxEventLabel() {
        if (ajaxEvent == null) {
            throw new IllegalStateException("No ajax event object labels registered");
        }
        return ajaxEvent;
    }

    public static ObjectLabel getMutationEventLabel() {
        if (mutationEvent == null) {
            throw new IllegalStateException("No mutation event object labels registered");
        }
        return mutationEvent;
    }

    public static ObjectLabel getWheelEventLabel() {
        if (wheelEvent == null) {
            throw new IllegalStateException("No wheel event object labels registered");
        }
        return wheelEvent;
    }

}
