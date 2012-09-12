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

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.ajax.AjaxBuilder;
import dk.brics.tajs.analysis.dom.core.CoreBuilder;
import dk.brics.tajs.analysis.dom.event.EventBuilder;
import dk.brics.tajs.analysis.dom.html.HTMLBuilder;
import dk.brics.tajs.analysis.dom.html5.HTML5Builder;
import dk.brics.tajs.analysis.dom.style.StyleBuilder;
import dk.brics.tajs.analysis.dom.view.ViewBuilder;

/**
 * Setup the DOM browser model.
 * <p/>
 * An overview is available at:
 * <p/>
 * http://dsmith77.files.wordpress.com/2008/07/the-document-object-model-dom.gif
 * <p/>
 * DOM Spec:
 * http://www.w3.org/DOM/DOMTR
 * <p/>
 * DOM LEVEL 1:
 * http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/ecma-script-language-binding.html
 * <p/>
 * DOM Level 2 Core:
 * http://www.w3.org/TR/DOM-Level-2-Core/core.html
 * http://www.w3.org/TR/DOM-Level-2-Core/ecma-script-binding.html
 * <p/>
 * DOM LEVEL 2 HTML:
 * http://www.w3.org/TR/DOM-Level-2-HTML/ecma-script-binding.html
 * <p/>
 * DOM LEVEL 2: Traversal
 * http://www.w3.org/TR/DOM-Level-2-Traversal-Range/Overview.html
 */
public class DOMBuilder {
    /**
     * Construct the initial DOM objects.
     * Its assumed that WINDOW is added to the state somewhere else before this function is invoked since its the
     * global objects when running in DOM mode.
     */
    public static void addInitialState(State s) {
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
    }
}
