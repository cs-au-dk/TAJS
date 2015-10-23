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

package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTML5Builder {

    public static final Set<ObjectLabel> HTML5_OBJECT_LABELS = Collections.newSet();

    public static void build(State s) {
        CanvasRenderingContext2D.build(s);
        HTMLCanvasElement.build(s);
        TimeRanges.build(s);
        HTMLMediaElement.build(s);
        HTMLAudioElement.build(s);
        StorageElement.build(s);
        WebGLRenderingContext.build(s);
        AudioParam.build(s);
        AudioNode.build(s);
        AudioDestinationNode.build(s);
        OscillatorNode.build(s);
        ScriptProcessorNode.build(s);
        AudioContext.build(s);

        // HTML5 properties on Window
        createDOMProperty(s, DOMWindow.WINDOW, "localStorage", Value.makeObject(StorageElement.INSTANCES));
        createDOMProperty(s, DOMWindow.WINDOW, "sessionStorage", Value.makeObject(StorageElement.INSTANCES));

        HTML5_OBJECT_LABELS.add(HTMLCanvasElement.INSTANCES);
        HTML5_OBJECT_LABELS.add(HTMLAudioElement.INSTANCES);
    }

}
