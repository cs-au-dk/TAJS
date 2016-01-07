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

/**
 * Auxiliary methods for recognizing HTML attribute names related to event handlers.
 */
public class DOMEventHelpers {

    /**
     * Returns true iff the specified attribute is an event attribute.
     */
    public static boolean isEventAttribute(String attribute) {
        return isKeyboardEventAttribute(attribute)
                || isMouseEventAttribute(attribute)
                || isOtherEventAttribute(attribute);
    }

    /**
     * Returns true iff the specified property is an event property.
     */
    public static boolean isEventProperty(String property) {
        return isKeyboardEventProperty(property)
                || isMouseEventProperty(property)
                || isOtherEventProperty(property);
    }

    /**
     * Returns true iff the specified attribute is an load event attribute.
     */
    public static boolean isLoadEventAttribute(String attribute) {
        return attribute.equalsIgnoreCase("load") || attribute.equalsIgnoreCase("onload") ||
                attribute.equalsIgnoreCase("DOMContentLoaded") || attribute.equalsIgnoreCase("onDOMContentLoaded");
    }

    /**
     * Returns true iff the specified attribute is an unload event attribute.
     */
    public static boolean isUnloadEventAttribute(String attribute) {
        return attribute.equalsIgnoreCase("unload") || attribute.equalsIgnoreCase("onunload");
    }

    /**
     * Returns true iff the specified attribute is a keyboard event attribute.
     */
    public static boolean isKeyboardEventAttribute(String attribute) {
        return attribute.equalsIgnoreCase("onkeypress")
                || attribute.equalsIgnoreCase("onkeydown")
                || attribute.equalsIgnoreCase("onkeyup");
    }

    /**
     * Returns true iff the specified property is a keyboard event property.
     */
    public static boolean isKeyboardEventProperty(String property) {
        return property.equalsIgnoreCase("keypress")
                || property.equalsIgnoreCase("keydown")
                || property.equalsIgnoreCase("keyup");
    }

    /**
     * Returns true iff the specified attribute is a mouse event attribute.
     */
    public static boolean isMouseEventAttribute(String attribute) {
        return attribute.equalsIgnoreCase("onclick")
                || attribute.equalsIgnoreCase("ondblclick")
                || attribute.equalsIgnoreCase("onmousedown")
                || attribute.equalsIgnoreCase("onmouseup")
                || attribute.equalsIgnoreCase("onmouseover")
                || attribute.equalsIgnoreCase("onmousemove")
                || attribute.equalsIgnoreCase("onmouseout");
    }

    /**
     * Returns true iff the specified property is a mouse event property.
     */
    public static boolean isMouseEventProperty(String property) {
        return property.equalsIgnoreCase("click")
                || property.equalsIgnoreCase("dblclick")
                || property.equalsIgnoreCase("mousedown")
                || property.equalsIgnoreCase("mouseup")
                || property.equalsIgnoreCase("mouseover")
                || property.equalsIgnoreCase("mousemove")
                || property.equalsIgnoreCase("mouseout");
    }

    /**
     * Returns true iff the specified property is an AJAX event property.
     */
    public static boolean isAjaxEventProperty(String property) {
        return property.equalsIgnoreCase("onreadystatechange");
    }

    /**
     * Returns true iff the specified attribute is an other event attribute.
     */
    public static boolean isOtherEventAttribute(String attribute) {
        return attribute.equalsIgnoreCase("onfocus")
                || attribute.equalsIgnoreCase("onblur")
                || attribute.equalsIgnoreCase("onsubmit")
                || attribute.equalsIgnoreCase("onreset")
                || attribute.equalsIgnoreCase("onselect")
                || attribute.equalsIgnoreCase("onchange")
                || attribute.equalsIgnoreCase("onresize")
                || attribute.equalsIgnoreCase("onselectstart")
                || attribute.equalsIgnoreCase("ontouchstart")
                || attribute.equalsIgnoreCase("ontouchend")
                || attribute.equalsIgnoreCase("ontouchmove")
                || attribute.equalsIgnoreCase("ontouchcancel");
    }

    /**
     * Returns true iff the specified property is an other event property.
     */
    public static boolean isOtherEventProperty(String property) {
        return property.equalsIgnoreCase("focus")
                || property.equalsIgnoreCase("blur")
                || property.equalsIgnoreCase("submit")
                || property.equalsIgnoreCase("reset")
                || property.equalsIgnoreCase("select")
                || property.equalsIgnoreCase("change")
                || property.equalsIgnoreCase("resize")
                || property.equalsIgnoreCase("touchstart")
                || property.equalsIgnoreCase("touchend")
                || property.equalsIgnoreCase("touchmove")
                || property.equalsIgnoreCase("touchcancel");
    }
}
