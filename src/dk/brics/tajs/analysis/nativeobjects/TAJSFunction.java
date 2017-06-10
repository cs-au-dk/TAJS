/*
 * Copyright 2009-2017 Aarhus University
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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.lattice.HostObject;

/**
 * TAJS_-functions that serve as utility functions for the analysis.
 */
public enum TAJSFunction implements HostObject {

    TAJS_DUMPVALUE("TAJS_dumpValue"),
    TAJS_DUMPPROTOTYPE("TAJS_dumpPrototype"),
    TAJS_DUMPOBJECT("TAJS_dumpObject"),
    TAJS_DUMPSTATE("TAJS_dumpState"),
    TAJS_DUMPMODIFIEDSTATE("TAJS_dumpModifiedState"),
    TAJS_DUMPATTRIBUTES("TAJS_dumpAttributes"),
    TAJS_DUMPEXPRESSION("TAJS_dumpExp"),
    TAJS_DUMPNF("TAJS_dumpNF"),
    TAJS_CONVERSION_TO_PRIMITIVE("TAJS_conversionToPrimitive"),
    TAJS_GET_UI_EVENT("TAJS_getUIEvent"),
    TAJS_GET_MOUSE_EVENT("TAJS_getMouseEvent"),
    TAJS_GET_KEYBOARD_EVENT("TAJS_getKeyboardEvent"),
    TAJS_GET_EVENT_LISTENER("TAJS_getEventListener"),
    TAJS_GET_WHEEL_EVENT("TAJS_getWheelEvent"),
    TAJS_GET_AJAX_EVENT("TAJS_getAjaxEvent"),
    TAJS_ADD_CONTEXT_SENSITIVITY("TAJS_addContextSensitivity"),
    TAJS_MAKE_CONTEXT_SENSITIVE("TAJS_makeContextSensitive"),
    TAJS_ASSERT("TAJS_assert"),
    TAJS_NEW_OBJECT("TAJS_newObject"),
    TAJS_NEW_ARRAY("TAJS_newArray"),
    TAJS_ASYNC_LISTEN("TAJS_asyncListen"),
    TAJS_MAKE("TAJS_make"),
    TAJS_MAKE_PARTIAL("TAJS_makePartial"),
    TAJS_JOIN("TAJS_join"),
    TAJS_ASSERT_EQUALS("TAJS_assertEquals"),
    TAJS_LOAD("TAJS_load"),
    TAJS_FIRST_ORDER_STRING_REPLACE("TAJS_firstOrderStringReplace"),
    ;

    private final HostAPIs api;

    private String string;

    TAJSFunction(String str) {
        api = HostAPIs.TAJS;
        string = str;
    }

    public HostAPIs getAPI() {
        return api;
    }

    @Override
    public String toString() {
        return string;
    }
}
