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

package dk.brics.tajs.analysis.dom.html5;

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMFunctions;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;
import dk.brics.tajs.util.Collections;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;

public class CanvasRenderingContext2D {

    public static ObjectLabel CONTEXT2D;

    public static ObjectLabel CONTEXT2D_PROTOTYPE;

    public static ObjectLabel GRADIENT;

    public static ObjectLabel PATTERN;

    public static ObjectLabel PIXEL_ARRAY;

    public static ObjectLabel IMAGE_DATA;

    public static ObjectLabel TEXT_METRICS;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();
        CONTEXT2D = ObjectLabel.make(DOMObjects.CANVASRENDERINGCONTEXT2D, ObjectLabel.Kind.OBJECT);
        CONTEXT2D_PROTOTYPE = ObjectLabel.make(DOMObjects.CANVASRENDERINGCONTEXT2D_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        GRADIENT = ObjectLabel.make(DOMObjects.CANVASGRADIENT, ObjectLabel.Kind.OBJECT);
        PATTERN = ObjectLabel.make(DOMObjects.CANVASPATTERN, ObjectLabel.Kind.OBJECT);
        PIXEL_ARRAY = ObjectLabel.make(DOMObjects.CANVASPIXELARRAY, ObjectLabel.Kind.OBJECT);
        IMAGE_DATA = ObjectLabel.make(DOMObjects.IMAGEDATA, ObjectLabel.Kind.OBJECT);
        TEXT_METRICS = ObjectLabel.make(DOMObjects.TEXTMETRICS, ObjectLabel.Kind.OBJECT);

        // Prototype Object
        s.newObject(CONTEXT2D_PROTOTYPE);
        s.writeInternalPrototype(CONTEXT2D_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Canvas Context Object
        s.newObject(CONTEXT2D);
        s.writeInternalPrototype(CONTEXT2D, Value.makeObject(CONTEXT2D_PROTOTYPE));
        pv.writeProperty(CONTEXT2D, "prototype", Value.makeObject(CONTEXT2D_PROTOTYPE));   // TODO: Verify
        pv.writeProperty(DOMWindow.WINDOW, "CanvasRenderingContext2D", Value.makeObject(CONTEXT2D));

        // Misc Objects
        s.newObject(PATTERN);
        s.newObject(PIXEL_ARRAY);
        s.newObject(IMAGE_DATA);
        s.newObject(TEXT_METRICS);
        s.newObject(GRADIENT);
        s.writeInternalPrototype(PATTERN, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeInternalPrototype(PIXEL_ARRAY, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeInternalPrototype(IMAGE_DATA, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeInternalPrototype(TEXT_METRICS, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));
        s.writeInternalPrototype(GRADIENT, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        /*
         * Properties.
         */

        // compositing
        createDOMProperty(CONTEXT2D_PROTOTYPE, "globalAlpha", Value.makeNum(1.0), c);
        createDOMProperty(CONTEXT2D_PROTOTYPE, "globalCompositeOperation", Value.makeStr("source-over"), c);

        // colours and styles
        createDOMProperty(CONTEXT2D, "strokeStyle", Value.makeAnyBool().join(Value.makeAnyNum()).join(Value.makeAnyStr()), c);
        createDOMProperty(CONTEXT2D, "fillStyle", Value.makeAnyBool().join(Value.makeAnyNum()).join(Value.makeAnyStr()), c);

        // line caps/joins
        createDOMProperty(CONTEXT2D, "lineWidth", Value.makeNum(1), c);
        createDOMProperty(CONTEXT2D, "lineCap", Value.makeStr("butt"), c);
        createDOMProperty(CONTEXT2D, "lineJoin", Value.makeStr("miter"), c);
        createDOMProperty(CONTEXT2D, "miterLimit", Value.makeNum(10), c);

        // shadows
        createDOMProperty(CONTEXT2D, "shadowOffsetX", Value.makeNum(0), c);
        createDOMProperty(CONTEXT2D, "shadowOffsetY", Value.makeNum(0), c);
        createDOMProperty(CONTEXT2D, "shadowBlur", Value.makeNum(0), c);
        createDOMProperty(CONTEXT2D, "shadowColor", Value.makeStr("transparent black"), c);

        // text
        createDOMProperty(CONTEXT2D, "font", Value.makeStr("10px sans-serif"), c);
        createDOMProperty(CONTEXT2D, "textAlign", Value.makeStr("start"), c);
        createDOMProperty(CONTEXT2D, "textBaseline", Value.makeStr("alphabetic"), c);

        // ImageData
        createDOMProperty(IMAGE_DATA, "data", Value.makeObject(PIXEL_ARRAY).setReadOnly(), c);
        createDOMProperty(IMAGE_DATA, "height", Value.makeAnyNumUInt().setReadOnly(), c);
        createDOMProperty(IMAGE_DATA, "width", Value.makeAnyNumUInt().setReadOnly(), c);

        // CanvasPixelArray
        createDOMProperty(PIXEL_ARRAY, "length", Value.makeAnyNumUInt().setReadOnly(), c);
        pv.writeProperty(Collections.singleton(PIXEL_ARRAY), Value.makeAnyStrUInt(), Value.makeAnyNumUInt(), false, true);

        /*
         * Canvas Functions.
         */

        // State
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SAVE, "save", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RESTORE, "restore", 0, c);

        // Transformations
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SCALE, "scale", 2, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ROTATE, "rotate", 1, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSLATE, "translate", 2, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSFORM, "transform", 6, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SETTRANSFORM, "setTransform", 6, c);

        // Colors & Style
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT, "createLinearGradient", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT, "createRadialGradient", 6, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_PATTERN, "createPattern", 2, c);

        // Rects
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLEAR_RECT, "clearRect", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_RECT, "fillRect", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_RECT, "strokeRect", 4, c);

        // Paths
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEGIN_PATH, "beginPath", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLOSE_PATH, "closePath", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MOVE_TO, "moveTo", 2, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_LINE_TO, "lineTo", 2, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO, "quadraticCurveTo", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO, "bezierCurveTo", 6, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC_TO, "arcTo", 5, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RECT, "rect", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC, "arc", 5, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL, "fill", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE, "stroke", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLIP, "clip", 0, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH, "isPointInPath", 2, c);

        // Focus Management
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING, "drawFocusRing", 4, c);

        // Text
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_TEXT, "fillText", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_TEXT, "strokeText", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MEASURE_TEXT, "measureText", 1, c);

        // Drawing Images
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_IMAGE, "drawImage", 9, c);

        // Pixel Manipulation
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA, "createImageData", 2, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA, "getImageData", 4, c);
        createDOMFunction(CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA, "putImageData", 7, c);

        /*
         * Gradient Functions
         */
        createDOMFunction(GRADIENT, DOMObjects.CANVASGRADIENT_ADD_COLOR_STOP, "addColorStop", 2, c);

        /*
         * Multiply objects
         */
        // Canvas
        s.multiplyObject(CONTEXT2D);
        CONTEXT2D = CONTEXT2D.makeSingleton().makeSummary();

        // Image Data
        s.multiplyObject(IMAGE_DATA);
        IMAGE_DATA = IMAGE_DATA.makeSingleton().makeSummary();

        // Pattern
        s.multiplyObject(PATTERN);
        PATTERN = PATTERN.makeSingleton().makeSummary();

        // Gradient
        s.multiplyObject(GRADIENT);
        GRADIENT = GRADIENT.makeSingleton().makeSummary();

        // Pixel Array
        s.multiplyObject(PIXEL_ARRAY);
        PIXEL_ARRAY = PIXEL_ARRAY.makeSingleton().makeSummary();

        // Text Metrics
        s.multiplyObject(TEXT_METRICS);
        TEXT_METRICS = TEXT_METRICS.makeSingleton().makeSummary();
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            // State
            case CANVASRENDERINGCONTEXT2D_SAVE:
            case CANVASRENDERINGCONTEXT2D_RESTORE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }

            // Transformations
            case CANVASRENDERINGCONTEXT2D_SCALE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ROTATE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value angle =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_TRANSLATE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_TRANSFORM:
            case CANVASRENDERINGCONTEXT2D_SETTRANSFORM: {
                DOMFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value m11 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value m12 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value m21 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value m22 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                /* Value dx =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                /* Value dy =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 5), c);
                return Value.makeUndef();
            }

            // Colors & Styles
            case CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x0 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y0 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value x1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value y1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                return Value.makeObject(GRADIENT);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value x0 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y0 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value r0 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value x1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                /* Value y1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                /* Value r1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 5), c);
                return Value.makeObject(GRADIENT);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_PATTERN: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                // TODO: Check the arguments
                /* Value image = */
                FunctionCalls.readParameter(call, s, 0);
                /* Value repetitionString =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeObject(PATTERN);
            }

            // Rects
            case CANVASRENDERINGCONTEXT2D_CLEAR_RECT:
            case CANVASRENDERINGCONTEXT2D_FILL_RECT:
            case CANVASRENDERINGCONTEXT2D_STROKE_RECT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value width =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value height =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }

            // Paths
            case CANVASRENDERINGCONTEXT2D_BEGIN_PATH: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_CLOSE_PATH: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_MOVE_TO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_LINE_TO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value cpx =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value cpy =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value cp1x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value cp1y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value cp2x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value cp2y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 5), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_RECT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value w =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value h =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ARC: {
                DOMFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value radius =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value startAngle =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                /* Value endAngle =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                /* Value anticlockwise =*/
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 5));
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ARC_TO: {
                DOMFunctions.expectParameters(nativeObject, call, c, 5, 5);
                /* Value x1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y1 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value x2 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value y2 =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                /* Value radius =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 4), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_CLIP: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case CANVASRENDERINGCONTEXT2D_FILL:
            case CANVASRENDERINGCONTEXT2D_STROKE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_DRAW_IMAGE: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, -1);

                boolean bad = FunctionCalls.readParameter(call, s, 0).isMaybePrimitive();
                for (ObjectLabel l : FunctionCalls.readParameter(call, s, 0).getObjectLabels()) {
                    if (!l.isHostObject() || (l.getHostObject() != DOMObjects.HTMLIMAGEELEMENT_INSTANCES && l.getHostObject() != DOMObjects.HTMLCANVASELEMENT_INSTANCES)) {
                        bad = true;
                    }
                }
                if (bad) {
                    String msg = "TypeError, non-HTMLImageElement or non-HTMLCanvasElement as argument to drawImage";
                    c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, msg);
                }

                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA: {
                DOMFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value sx =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value sy =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value sw =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value sh =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                return Value.makeObject(IMAGE_DATA);
            }
            case CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, -1);
                /* Value imageData =*/
                DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, FunctionCalls.readParameter(call, s, 0), false, c);
                Value dx = Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                Value dy = Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);

                if (dx.isNaN() || dy.isNaN() || dx.isMaybeInf() || dy.isMaybeInf()) {
                    final String message = "TypeError, inf or NaN as arguments to CanvasRenderingContext2D.putImageData";
                    c.getMonitoring().addMessage(c.getNode(), Message.Severity.HIGH, message);
                }

                return Value.makeUndef();
            }
            case CANVASGRADIENT_ADD_COLOR_STOP: {
                DOMFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value offset =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                /* Value color =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_STROKE_TEXT:
            case CANVASRENDERINGCONTEXT2D_FILL_TEXT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, 4);
                /* Value text =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                /* Value x =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value y =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                if (FunctionCalls.readParameter(call, s, 3) != Value.makeUndef()) {
                    /* Value maxWidth =*/
                    Conversion.toNumber(FunctionCalls.readParameter(call, s, 3), c);
                }
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_MEASURE_TEXT: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value text =*/
                Conversion.toString(FunctionCalls.readParameter(call, s, 0), c);
                return Value.makeObject(TEXT_METRICS);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA: {
                DOMFunctions.expectParameters(nativeObject, call, c, 1, 2);
                if (FunctionCalls.readParameter(call, s, 1) == Value.makeUndef()) {
                    // Version with only one argument
                    /* Value imageData =*/
                    DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, FunctionCalls.readParameter(call, s, 0), false, c);
                } else {
                    // Version with two arguments
                    /* Value sw =*/
                    Conversion.toNumber(FunctionCalls.readParameter(call, s, 0), c);
                    /* Value sh =*/
                    Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                }
                return Value.makeObject(IMAGE_DATA);
            }
            // Focus Management
            case CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING: {
                DOMFunctions.expectParameters(nativeObject, call, c, 3, 4);
                /* Value element = */
                DOMConversion.toHTMLElement(FunctionCalls.readParameter(call, s, 0), c);
                /* Value xCaret = */
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 1), c);
                /* Value yCaret =*/
                Conversion.toNumber(FunctionCalls.readParameter(call, s, 2), c);
                /* Value canDrawCustom (optional) = */
                Conversion.toBoolean(FunctionCalls.readParameter(call, s, 3));
                return Value.makeAnyBool();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
