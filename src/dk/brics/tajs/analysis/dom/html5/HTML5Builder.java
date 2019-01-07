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

package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class HTML5Builder {

    public static final Set<ObjectLabel> HTML5_OBJECT_LABELS = Collections.newSet();

    public static void build(Solver.SolverInterface c) {
        CanvasRenderingContext2D.build(c);
        HTMLCanvasElement.build(c);
        TimeRanges.build(c);
        HTMLMediaElement.build(c);
        HTMLAudioElement.build(c);
        StorageElement.build(c);
        WebGLRenderingContext.build(c);
        AudioParam.build(c);
        AudioNode.build(c);
        AudioDestinationNode.build(c);
        OscillatorNode.build(c);
        ScriptProcessorNode.build(c);
        AudioContext.build(c);
        MediaQueryList.build(c);
        OfflineResourceList.build(c);
        MutationObserver.build(c);
        Worker.build(c);

        // HTML5 properties on Window
        createDOMProperty(DOMWindow.WINDOW, "localStorage", Value.makeObject(StorageElement.INSTANCES), c);
        createDOMProperty(DOMWindow.WINDOW, "sessionStorage", Value.makeObject(StorageElement.INSTANCES), c);
        createDOMProperty(DOMWindow.WINDOW, "applicationCache", Value.makeObject(OfflineResourceList.INSTANCES), c);


        HTML5_OBJECT_LABELS.add(HTMLCanvasElement.INSTANCES);
        HTML5_OBJECT_LABELS.add(HTMLAudioElement.INSTANCES);
    }

}
