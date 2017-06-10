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

package dk.brics.tajs.analysis.dom.event;

import dk.brics.tajs.analysis.Solver;

/**
 * Initial State Builder for DOM Events.
 * <p>
 * More information at:
 * <p>
 * http://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113/
 * http://www.w3.org/TR/2003/NOTE-DOM-Level-3-Events-20031107/
 * <p>
 * http://www.w3.org/TR/REC-html40/interact/scripts.html#h-18.2.3
 */
public class EventBuilder {

    public static void build(Solver.SolverInterface c) {
        Event.build(c);
        EventTarget.build(c);
        EventListener.build(c);
        EventException.build(c);
        DocumentEvent.build(c);
        HashChangeEvent.build(c);
        MutationEvent.build(c);
        UIEvent.build(c);
        KeyboardEvent.build(c);
        MouseEvent.build(c);
        WheelEvent.build(c);
        LoadEvent.build(c);
        TouchEvent.build(c);
    }
}
