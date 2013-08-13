/*
 * Copyright 2009-2013 Aarhus University
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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

/**
 * Each Storage object provides access to a list of key/value pairs,
 * which are sometimes called items. Keys and values are strings.
 * Any string (including the empty string) is a valid key.
 */
public class StorageElement {

	public static ObjectLabel CONSTRUCTOR;
	public static ObjectLabel PROTOTYPE;
    public static ObjectLabel INSTANCES;
	
	public static void build(State s) {
		CONSTRUCTOR = new ObjectLabel(DOMObjects.STORAGE_CONSTRUCTOR, ObjectLabel.Kind.FUNCTION);
		PROTOTYPE = new ObjectLabel(DOMObjects.STORAGE_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = new ObjectLabel(DOMObjects.STORAGE_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        s.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        s.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeProperty(DOMWindow.WINDOW, "Storage", Value.makeObject(CONSTRUCTOR));

		// Prototype object
		s.newObject(PROTOTYPE);
		s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
		
		// Multiplied object
		s.newObject(INSTANCES);
		s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));
		
		/*
		 * Properties.
		 */
		createDOMProperty(s, INSTANCES, "length", Value.makeAnyNumUInt().setReadOnly());
		
		s.multiplyObject(INSTANCES);
		INSTANCES = INSTANCES.makeSingleton().makeSummary();
		
		/*
		 * Functions.
		 */
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_KEY, "key", 1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_GET_ITEM, "getItem", 1);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_SET_ITEM, "setItem", 2);
		createDOMFunction(s, PROTOTYPE, DOMObjects.STORAGE_REMOVE_ITEM, "removeItem", 1);
	}
	
	 public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
		 switch (nativeObject) {
		 case STORAGE_KEY: {
			 NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			 /* Value index =*/ Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
			 return Value.makeAnyStr();
		 }
		 case STORAGE_GET_ITEM: {
			 NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			 /* Value key =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
			 return Value.makeAnyStr().joinNull();
		 }
		 case STORAGE_SET_ITEM: {
			 NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
			 /* Value key =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
			 /* Value data =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
			 return Value.makeUndef();
		 }
		 case STORAGE_REMOVE_ITEM: {
			 NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
			 /* Value key =*/ Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
			 return Value.makeUndef();
		 }
		 default: {
			 throw new AnalysisException("Unsupported Native Object: " + nativeObject);
		 }
		 }
	 }
	
}
