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

import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.NativeFunctions;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.analysis.dom.DOMConversion;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.solver.Message;

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

    public static void build(State s) {
        CONTEXT2D = new ObjectLabel(DOMObjects.CANVASRENDERINGCONTEXT2D, ObjectLabel.Kind.OBJECT);
        CONTEXT2D_PROTOTYPE = new ObjectLabel(DOMObjects.CANVASRENDERINGCONTEXT2D_PROTOTYPE, ObjectLabel.Kind.OBJECT);

        GRADIENT = new ObjectLabel(DOMObjects.CANVASGRADIENT, ObjectLabel.Kind.OBJECT);
        PATTERN = new ObjectLabel(DOMObjects.CANVASPATTERN, ObjectLabel.Kind.OBJECT);
        PIXEL_ARRAY = new ObjectLabel(DOMObjects.CANVASPIXELARRAY, ObjectLabel.Kind.OBJECT);
        IMAGE_DATA = new ObjectLabel(DOMObjects.IMAGEDATA, ObjectLabel.Kind.OBJECT);
        TEXT_METRICS = new ObjectLabel(DOMObjects.TEXTMETRICS, ObjectLabel.Kind.OBJECT);

        // Prototype Object
        s.newObject(CONTEXT2D_PROTOTYPE);
        s.writeInternalPrototype(CONTEXT2D_PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Canvas Context Object
        s.newObject(CONTEXT2D);
        s.writeInternalPrototype(CONTEXT2D, Value.makeObject(CONTEXT2D_PROTOTYPE));
        s.writeProperty(CONTEXT2D, "prototype", Value.makeObject(CONTEXT2D_PROTOTYPE));   // TODO: Verify
        s.writeProperty(DOMWindow.WINDOW, "CanvasRenderingContext2D", Value.makeObject(CONTEXT2D));

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
        createDOMProperty(s, CONTEXT2D_PROTOTYPE, "globalAlpha", Value.makeNum(1.0));
        createDOMProperty(s, CONTEXT2D_PROTOTYPE, "globalCompositeOperation", Value.makeStr("source-over"));

        // colours and styles
        createDOMProperty(s, CONTEXT2D, "strokeStyle", Value.makeAnyBool().join(Value.makeAnyNum()).join(Value.makeAnyStr()));
        createDOMProperty(s, CONTEXT2D, "fillStyle", Value.makeAnyBool().join(Value.makeAnyNum()).join(Value.makeAnyStr()));

        // line caps/joins
        createDOMProperty(s, CONTEXT2D, "lineWidth", Value.makeNum(1));
        createDOMProperty(s, CONTEXT2D, "lineCap", Value.makeStr("butt"));
        createDOMProperty(s, CONTEXT2D, "lineJoin", Value.makeStr("miter"));
        createDOMProperty(s, CONTEXT2D, "miterLimit", Value.makeNum(10));

        // shadows
        createDOMProperty(s, CONTEXT2D, "shadowOffsetX", Value.makeNum(0));
        createDOMProperty(s, CONTEXT2D, "shadowOffsetY", Value.makeNum(0));
        createDOMProperty(s, CONTEXT2D, "shadowBlur", Value.makeNum(0));
        createDOMProperty(s, CONTEXT2D, "shadowColor", Value.makeStr("transparent black"));

        // text
        createDOMProperty(s, CONTEXT2D, "font", Value.makeStr("10px sans-serif"));
        createDOMProperty(s, CONTEXT2D, "textAlign", Value.makeStr("start"));
        createDOMProperty(s, CONTEXT2D, "textBaseline", Value.makeStr("alphabetic"));

        // ImageData
        createDOMProperty(s, IMAGE_DATA, "data", Value.makeObject(PIXEL_ARRAY).setReadOnly());
        createDOMProperty(s, IMAGE_DATA, "height", Value.makeAnyNumUInt().setReadOnly());
        createDOMProperty(s, IMAGE_DATA, "width", Value.makeAnyNumUInt().setReadOnly());

        // CanvasPixelArray
        createDOMProperty(s, PIXEL_ARRAY, "length", Value.makeAnyNumUInt().setReadOnly());

        /*
         * Canvas Functions.
         */

        // State
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SAVE, "save", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RESTORE, "restore", 0);

        // Transformations
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SCALE, "scale", 2);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ROTATE, "rotate", 1);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSLATE, "translate", 2);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_TRANSFORM, "transform", 6);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_SETTRANSFORM, "setTransform", 6);

        // Colors & Style
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT, "createLinearGradient", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT, "createRadialGradient", 6);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_PATTERN, "createPattern", 2);

        // Rects
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLEAR_RECT, "clearRect", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_RECT, "fillRect", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_RECT, "strokeRect", 4);

        // Paths
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEGIN_PATH, "beginPath", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLOSE_PATH, "closePath", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MOVE_TO, "moveTo", 2);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_LINE_TO, "lineTo", 2);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO, "quadraticCurveTo", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO, "bezierCurveTo", 6);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC_TO, "arcTo", 5);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_RECT, "rect", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_ARC, "arc", 5);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL, "fill", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE, "stroke", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CLIP, "clip", 0);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH, "isPointInPath", 2);

        // Focus Management
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING, "drawFocusRing", 4);

        // Text
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_FILL_TEXT, "fillText", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_STROKE_TEXT, "strokeText", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_MEASURE_TEXT, "measureText", 1);

        // Drawing Images
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_DRAW_IMAGE, "drawImage", 9);

        // Pixel Manipulation
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA, "createImageData", 2);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA, "getImageData", 4);
        createDOMFunction(s, CONTEXT2D_PROTOTYPE, DOMObjects.CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA, "putImageData", 7);

        /*
         * Gradient Functions
         */
        createDOMFunction(s, GRADIENT, DOMObjects.CANVASGRADIENT_ADD_COLOR_STOP, "addColorStop", 2);

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

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, State s, Solver.SolverInterface c) {
        switch (nativeObject) {
            // State
            case CANVASRENDERINGCONTEXT2D_SAVE:
            case CANVASRENDERINGCONTEXT2D_RESTORE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }

            // Transformations
            case CANVASRENDERINGCONTEXT2D_SCALE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ROTATE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value angle =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_TRANSLATE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_TRANSFORM:
            case CANVASRENDERINGCONTEXT2D_SETTRANSFORM: {
                NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value m11 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value m12 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value m21 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value m22 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                /* Value dx =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                /* Value dy =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 5), c);
                return Value.makeUndef();
            }

            // Colors & Styles
            case CANVASRENDERINGCONTEXT2D_CREATE_LINEAR_GRADIENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x0 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y0 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value x1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value y1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                return Value.makeObject(GRADIENT);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_RADIAL_GRADIENT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value x0 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y0 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value r0 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value x1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                /* Value y1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                /* Value r1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 5), c);
                return Value.makeObject(GRADIENT);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_PATTERN: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                // TODO: Check the arguments
                /* Value image = */
                NativeFunctions.readParameter(call, s, 0);
                /* Value repetitionString =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeObject(PATTERN);
            }

            // Rects
            case CANVASRENDERINGCONTEXT2D_CLEAR_RECT:
            case CANVASRENDERINGCONTEXT2D_FILL_RECT:
            case CANVASRENDERINGCONTEXT2D_STROKE_RECT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value width =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value height =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }

            // Paths
            case CANVASRENDERINGCONTEXT2D_BEGIN_PATH: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_CLOSE_PATH: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_MOVE_TO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_LINE_TO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_QUADRATIC_CURVE_TO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value cpx =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value cpy =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_BEZIER_CURVE_TO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value cp1x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value cp1y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value cp2x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value cp2y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 5), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_RECT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value w =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value h =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ARC: {
                NativeFunctions.expectParameters(nativeObject, call, c, 6, 6);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value radius =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value startAngle =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                /* Value endAngle =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                /* Value anticlockwise =*/
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 5));
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_ARC_TO: {
                NativeFunctions.expectParameters(nativeObject, call, c, 5, 5);
                /* Value x1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y1 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value x2 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value y2 =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                /* Value radius =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 4), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_CLIP: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_IS_POINT_IN_PATH: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeAnyBool();
            }
            case CANVASRENDERINGCONTEXT2D_FILL:
            case CANVASRENDERINGCONTEXT2D_STROKE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 0, 0);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_DRAW_IMAGE: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, -1);

                boolean good = false;
                boolean bad = NativeFunctions.readParameter(call, s, 0).isMaybePrimitive();
                for (ObjectLabel l : NativeFunctions.readParameter(call, s, 0).getObjectLabels()) {
                    if (l.isHostObject()
                            && (l.getHostObject() == DOMObjects.HTMLIMAGEELEMENT_INSTANCES || l.getHostObject() == DOMObjects.HTMLCANVASELEMENT_INSTANCES)) {
                        good = true;
                    } else {
                        bad = true;
                    }
                }
                if (bad) {
                    String msg = "TypeError, non-HTMLImageElement or non-HTMLCanvasElement as argument to drawImage";
                    c.getMonitoring().addMessage(c.getCurrentNode(), Message.Severity.HIGH, msg);
                }

                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_GET_IMAGE_DATA: {
                NativeFunctions.expectParameters(nativeObject, call, c, 4, 4);
                /* Value sx =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value sy =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value sw =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value sh =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                return Value.makeObject(IMAGE_DATA);
            }
            case CANVASRENDERINGCONTEXT2D_PUT_IMAGE_DATA: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, -1);
                /* Value imageData =*/
                DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, NativeFunctions.readParameter(call, s, 0), false, c);
                Value dx = Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                Value dy = Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);

                if (dx.isNaN() || dy.isNaN() || dx.isMaybeInf() || dy.isMaybeInf()) {
                    final String message = "TypeError, inf or NaN as arguments to CanvasRenderingContext2D.putImageData";
                    c.getMonitoring().addMessage(c.getCurrentNode(), Message.Severity.HIGH, message);
                }

                return Value.makeUndef();
            }
            case CANVASGRADIENT_ADD_COLOR_STOP: {
                NativeFunctions.expectParameters(nativeObject, call, c, 2, 2);
                /* Value offset =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                /* Value color =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 1), c);
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_STROKE_TEXT:
            case CANVASRENDERINGCONTEXT2D_FILL_TEXT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 4);
                /* Value text =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                /* Value x =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value y =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                if (NativeFunctions.readParameter(call, s, 3) != Value.makeUndef()) {
                    /* Value maxWidth =*/
                    Conversion.toNumber(NativeFunctions.readParameter(call, s, 3), c);
                }
                return Value.makeUndef();
            }
            case CANVASRENDERINGCONTEXT2D_MEASURE_TEXT: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 1);
                /* Value text =*/
                Conversion.toString(NativeFunctions.readParameter(call, s, 0), c);
                return Value.makeObject(TEXT_METRICS);
            }
            case CANVASRENDERINGCONTEXT2D_CREATE_IMAGE_DATA: {
                NativeFunctions.expectParameters(nativeObject, call, c, 1, 2);
                if (NativeFunctions.readParameter(call, s, 1) == Value.makeUndef()) {
                    // Version with only one argument
                    /* Value imageData =*/
                    DOMConversion.toNativeObject(DOMObjects.IMAGEDATA, NativeFunctions.readParameter(call, s, 0), false, c);
                } else {
                    // Version with two arguments
                    /* Value sw =*/
                    Conversion.toNumber(NativeFunctions.readParameter(call, s, 0), c);
                    /* Value sh =*/
                    Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                }
                return Value.makeObject(IMAGE_DATA);
            }
            // Focus Management
            case CANVASRENDERINGCONTEXT2D_DRAW_FOCUS_RING: {
                NativeFunctions.expectParameters(nativeObject, call, c, 3, 4);
                /* Value element = */
                DOMConversion.toHTMLElement(NativeFunctions.readParameter(call, s, 0), c);
                /* Value xCaret = */
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 1), c);
                /* Value yCaret =*/
                Conversion.toNumber(NativeFunctions.readParameter(call, s, 2), c);
                /* Value canDrawCustom (optional) = */
                Conversion.toBoolean(NativeFunctions.readParameter(call, s, 3));
                return Value.makeAnyBool();
            }
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
