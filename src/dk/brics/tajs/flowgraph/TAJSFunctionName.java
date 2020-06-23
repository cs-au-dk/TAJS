/*
 * Copyright 2009-2020 Aarhus University
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

/**
 * The complete list of names for TAJS_-functions.
 */
public enum TAJSFunctionName {

    TAJS_ADD_CONTEXT_SENSITIVITY("addContextSensitivity"),
    TAJS_ASSERT("assert"),
    TAJS_ASSERT_EQUALS("assertEquals"),
    TAJS_ASYNC_LISTEN("asyncListen"), // used only in common-async-polyfill.js

    TAJS_CONVERSION_TO_PRIMITIVE("conversionToPrimitive"),

    TAJS_DUMPATTRIBUTES("dumpAttributes"),
    TAJS_DUMPEXPRESSION("dumpExp"),
    TAJS_DUMPMODIFIEDSTATE("dumpModifiedState"),
    TAJS_DUMPNF("dumpNF"),
    TAJS_DUMPOBJECT("dumpObject"),
    TAJS_DUMPPROTOTYPE("dumpPrototype"),
    TAJS_DUMPSTATE("dumpState"),
    TAJS_DUMPVALUE("dumpValue"),

    TAJS_FIRST_ORDER_STRING_REPLACE("firstOrderStringReplace"), // used only in string-replace-model.js

    TAJS_GET_AJAX_EVENT("getAjaxEvent"), // only used for debugging
    TAJS_GET_EVENT_LISTENER("getEventListener"), // only used for debugging
    TAJS_GET_KEYBOARD_EVENT("getKeyboardEvent"), // only used for debugging
    TAJS_GET_MOUSE_EVENT("getMouseEvent"), // only used for debugging
    TAJS_GET_UI_EVENT("getUIEvent"), // only used for debugging
    TAJS_GET_WHEEL_EVENT("getWheelEvent"), // only used for debugging

    TAJS_JOIN("join"),

    TAJS_LOAD("load"),
    TAJS_ASSUMEMODULETYPE("assumeModuleType"), // used only in simple-require.js

    TAJS_MAKE("make"),
    TAJS_MAKE_EXCLUDED_STRINGS("makeExcludedStrings"),
    TAJS_MAKE_CONTEXT_SENSITIVE("makeContextSensitive"),
    TAJS_MAKE_PARTIAL("makePartial"), // used only in error-captureStackTrace-model.js

    TAJS_MODULE_EXPORTS_FILTERING("moduleExportsFiltering"), // used only in simple-require.js

    TAJS_NEW_ARRAY("newArray"),
    TAJS_NEW_OBJECT("newObject"),

    TAJS_GET_MAIN("getMain"), // used only in simple-bootstrap-node.js
    TAJS_NOT_IMPLEMENTED("NOT_IMPLEMENTED"), // used only in simple-require.js
    TAJS_LOAD_JSON("loadJSON"), // used only in simple-require.js
    TAJS_NODE_REQUIRE_RESOLVE("nodeRequireResolve"), // used only in simple-require.js
    TAJS_NODE_PARENT_DIR("parentDir"), // used only in simple-require.js
    TAJS_NODE_UNURL("unURL"); // used only in simple-require.js

    private String string;

    TAJSFunctionName(String suffix) {
        string = "TAJS_" + suffix;
    }

    @Override
    public String toString() {
        return string;
    }
}
