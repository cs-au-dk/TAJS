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

import dk.brics.tajs.analysis.Exceptions;
import dk.brics.tajs.analysis.FunctionCalls;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.dom.DOMWindow;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;

import java.util.Arrays;
import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.util.Collections.newSet;

public class WebGLRenderingContext {

    public static ObjectLabel CONSTRUCTOR;

    public static ObjectLabel PROTOTYPE;

    public static ObjectLabel INSTANCES;

    public static void build(Solver.SolverInterface c) {
        State s = c.getState();
        PropVarOperations pv = c.getAnalysis().getPropVarOperations();

        // Scraped from google chrome:
        Set<String> scrapedNumericPropertyNames = newSet(Arrays.asList("DEPTH_BUFFER_BIT", "STENCIL_BUFFER_BIT", "COLOR_BUFFER_BIT", "POINTS", "LINES", "LINE_LOOP", "LINE_STRIP", "TRIANGLES", "TRIANGLE_STRIP", "TRIANGLE_FAN", "ZERO", "ONE", "SRC_COLOR", "ONE_MINUS_SRC_COLOR", "SRC_ALPHA", "ONE_MINUS_SRC_ALPHA", "DST_ALPHA", "ONE_MINUS_DST_ALPHA", "DST_COLOR", "ONE_MINUS_DST_COLOR", "SRC_ALPHA_SATURATE", "FUNC_ADD", "BLEND_EQUATION", "BLEND_EQUATION_RGB", "BLEND_EQUATION_ALPHA", "FUNC_SUBTRACT", "FUNC_REVERSE_SUBTRACT", "BLEND_DST_RGB", "BLEND_SRC_RGB", "BLEND_DST_ALPHA", "BLEND_SRC_ALPHA", "CONSTANT_COLOR", "ONE_MINUS_CONSTANT_COLOR", "CONSTANT_ALPHA", "ONE_MINUS_CONSTANT_ALPHA", "BLEND_COLOR", "ARRAY_BUFFER", "ELEMENT_ARRAY_BUFFER", "ARRAY_BUFFER_BINDING", "ELEMENT_ARRAY_BUFFER_BINDING", "STREAM_DRAW", "STATIC_DRAW", "DYNAMIC_DRAW", "BUFFER_SIZE", "BUFFER_USAGE", "CURRENT_VERTEX_ATTRIB", "FRONT", "BACK", "FRONT_AND_BACK", "TEXTURE_2D", "CULL_FACE", "BLEND", "DITHER", "STENCIL_TEST", "DEPTH_TEST", "SCISSOR_TEST", "POLYGON_OFFSET_FILL", "SAMPLE_ALPHA_TO_COVERAGE", "SAMPLE_COVERAGE", "NO_ERROR", "INVALID_ENUM", "INVALID_VALUE", "INVALID_OPERATION", "OUT_OF_MEMORY", "CW", "CCW", "LINE_WIDTH", "ALIASED_POINT_SIZE_RANGE", "ALIASED_LINE_WIDTH_RANGE", "CULL_FACE_MODE", "FRONT_FACE", "DEPTH_RANGE", "DEPTH_WRITEMASK", "DEPTH_CLEAR_VALUE", "DEPTH_FUNC", "STENCIL_CLEAR_VALUE", "STENCIL_FUNC", "STENCIL_FAIL", "STENCIL_PASS_DEPTH_FAIL", "STENCIL_PASS_DEPTH_PASS", "STENCIL_REF", "STENCIL_VALUE_MASK", "STENCIL_WRITEMASK", "STENCIL_BACK_FUNC", "STENCIL_BACK_FAIL", "STENCIL_BACK_PASS_DEPTH_FAIL", "STENCIL_BACK_PASS_DEPTH_PASS", "STENCIL_BACK_REF", "STENCIL_BACK_VALUE_MASK", "STENCIL_BACK_WRITEMASK", "VIEWPORT", "SCISSOR_BOX", "COLOR_CLEAR_VALUE", "COLOR_WRITEMASK", "UNPACK_ALIGNMENT", "PACK_ALIGNMENT", "MAX_TEXTURE_SIZE", "MAX_VIEWPORT_DIMS", "SUBPIXEL_BITS", "RED_BITS", "GREEN_BITS", "BLUE_BITS", "ALPHA_BITS", "DEPTH_BITS", "STENCIL_BITS", "POLYGON_OFFSET_UNITS", "POLYGON_OFFSET_FACTOR", "TEXTURE_BINDING_2D", "SAMPLE_BUFFERS", "SAMPLES", "SAMPLE_COVERAGE_VALUE", "SAMPLE_COVERAGE_INVERT", "COMPRESSED_TEXTURE_FORMATS", "DONT_CARE", "FASTEST", "NICEST", "GENERATE_MIPMAP_HINT", "BYTE", "UNSIGNED_BYTE", "SHORT", "UNSIGNED_SHORT", "INT", "UNSIGNED_INT", "FLOAT", "DEPTH_COMPONENT", "ALPHA", "RGB", "RGBA", "LUMINANCE", "LUMINANCE_ALPHA", "UNSIGNED_SHORT_4_4_4_4", "UNSIGNED_SHORT_5_5_5_1", "UNSIGNED_SHORT_5_6_5", "FRAGMENT_SHADER", "VERTEX_SHADER", "MAX_VERTEX_ATTRIBS", "MAX_VERTEX_UNIFORM_VECTORS", "MAX_VARYING_VECTORS", "MAX_COMBINED_TEXTURE_IMAGE_UNITS", "MAX_VERTEX_TEXTURE_IMAGE_UNITS", "MAX_TEXTURE_IMAGE_UNITS", "MAX_FRAGMENT_UNIFORM_VECTORS", "SHADER_TYPE", "DELETE_STATUS", "LINK_STATUS", "VALIDATE_STATUS", "ATTACHED_SHADERS", "ACTIVE_UNIFORMS", "ACTIVE_ATTRIBUTES", "SHADING_LANGUAGE_VERSION", "CURRENT_PROGRAM", "NEVER", "LESS", "EQUAL", "LEQUAL", "GREATER", "NOTEQUAL", "GEQUAL", "ALWAYS", "KEEP", "REPLACE", "INCR", "DECR", "INVERT", "INCR_WRAP", "DECR_WRAP", "VENDOR", "RENDERER", "VERSION", "NEAREST", "LINEAR", "NEAREST_MIPMAP_NEAREST", "LINEAR_MIPMAP_NEAREST", "NEAREST_MIPMAP_LINEAR", "LINEAR_MIPMAP_LINEAR", "TEXTURE_MAG_FILTER", "TEXTURE_MIN_FILTER", "TEXTURE_WRAP_S", "TEXTURE_WRAP_T", "TEXTURE", "TEXTURE_CUBE_MAP", "TEXTURE_BINDING_CUBE_MAP", "TEXTURE_CUBE_MAP_POSITIVE_X", "TEXTURE_CUBE_MAP_NEGATIVE_X", "TEXTURE_CUBE_MAP_POSITIVE_Y", "TEXTURE_CUBE_MAP_NEGATIVE_Y", "TEXTURE_CUBE_MAP_POSITIVE_Z", "TEXTURE_CUBE_MAP_NEGATIVE_Z", "MAX_CUBE_MAP_TEXTURE_SIZE", "TEXTURE0", "TEXTURE1", "TEXTURE2", "TEXTURE3", "TEXTURE4", "TEXTURE5", "TEXTURE6", "TEXTURE7", "TEXTURE8", "TEXTURE9", "TEXTURE10", "TEXTURE11", "TEXTURE12", "TEXTURE13", "TEXTURE14", "TEXTURE15", "TEXTURE16", "TEXTURE17", "TEXTURE18", "TEXTURE19", "TEXTURE20", "TEXTURE21", "TEXTURE22", "TEXTURE23", "TEXTURE24", "TEXTURE25", "TEXTURE26", "TEXTURE27", "TEXTURE28", "TEXTURE29", "TEXTURE30", "TEXTURE31", "ACTIVE_TEXTURE", "REPEAT", "CLAMP_TO_EDGE", "MIRRORED_REPEAT", "FLOAT_VEC2", "FLOAT_VEC3", "FLOAT_VEC4", "INT_VEC2", "INT_VEC3", "INT_VEC4", "BOOL", "BOOL_VEC2", "BOOL_VEC3", "BOOL_VEC4", "FLOAT_MAT2", "FLOAT_MAT3", "FLOAT_MAT4", "SAMPLER_2D", "SAMPLER_CUBE", "VERTEX_ATTRIB_ARRAY_ENABLED", "VERTEX_ATTRIB_ARRAY_SIZE", "VERTEX_ATTRIB_ARRAY_STRIDE", "VERTEX_ATTRIB_ARRAY_TYPE", "VERTEX_ATTRIB_ARRAY_NORMALIZED", "VERTEX_ATTRIB_ARRAY_POINTER", "VERTEX_ATTRIB_ARRAY_BUFFER_BINDING", "IMPLEMENTATION_COLOR_READ_TYPE", "IMPLEMENTATION_COLOR_READ_FORMAT", "COMPILE_STATUS", "LOW_FLOAT", "MEDIUM_FLOAT", "HIGH_FLOAT", "LOW_INT", "MEDIUM_INT", "HIGH_INT", "FRAMEBUFFER", "RENDERBUFFER", "RGBA4", "RGB5_A1", "RGB565", "DEPTH_COMPONENT16", "STENCIL_INDEX", "STENCIL_INDEX8", "DEPTH_STENCIL", "RENDERBUFFER_WIDTH", "RENDERBUFFER_HEIGHT", "RENDERBUFFER_INTERNAL_FORMAT", "RENDERBUFFER_RED_SIZE", "RENDERBUFFER_GREEN_SIZE", "RENDERBUFFER_BLUE_SIZE", "RENDERBUFFER_ALPHA_SIZE", "RENDERBUFFER_DEPTH_SIZE", "RENDERBUFFER_STENCIL_SIZE", "FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE", "FRAMEBUFFER_ATTACHMENT_OBJECT_NAME", "FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL", "FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE", "COLOR_ATTACHMENT0", "DEPTH_ATTACHMENT", "STENCIL_ATTACHMENT", "DEPTH_STENCIL_ATTACHMENT", "NONE", "FRAMEBUFFER_COMPLETE", "FRAMEBUFFER_INCOMPLETE_ATTACHMENT", "FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT", "FRAMEBUFFER_INCOMPLETE_DIMENSIONS", "FRAMEBUFFER_UNSUPPORTED", "FRAMEBUFFER_BINDING", "RENDERBUFFER_BINDING", "MAX_RENDERBUFFER_SIZE", "INVALID_FRAMEBUFFER_OPERATION", "UNPACK_FLIP_Y_WEBGL", "UNPACK_PREMULTIPLY_ALPHA_WEBGL", "CONTEXT_LOST_WEBGL", "UNPACK_COLORSPACE_CONVERSION_WEBGL", "BROWSER_DEFAULT_WEBGL"));
        Set<String> scrapedFunctionPropertyNames = newSet(Arrays.asList("activeTexture", "attachShader", "bindAttribLocation", "bindBuffer", "bindFramebuffer", "bindRenderbuffer", "bindTexture", "blendColor", "blendEquation", "blendEquationSeparate", "blendFunc", "blendFuncSeparate", "bufferData", "bufferSubData", "checkFramebufferStatus", "clear", "clearColor", "clearDepth", "clearStencil", "colorMask", "compileShader", "compressedTexImage2D", "compressedTexSubImage2D", "copyTexImage2D", "copyTexSubImage2D", "createBuffer", "createFramebuffer", "createProgram", "createRenderbuffer", "createShader", "createTexture", "cullFace", "deleteBuffer", "deleteFramebuffer", "deleteProgram", "deleteRenderbuffer", "deleteShader", "deleteTexture", "depthFunc", "depthMask", "depthRange", "detachShader", "disable", "disableVertexAttribArray", "drawArrays", "drawElements", "enable", "enableVertexAttribArray", "finish", "flush", "framebufferRenderbuffer", "framebufferTexture2D", "frontFace", "generateMipmap", "getActiveAttrib", "getActiveUniform", "getAttachedShaders", "getAttribLocation", "getBufferParameter", "getContextAttributes", "getError", "getExtension", "getFramebufferAttachmentParameter", "getParameter", "getProgramParameter", "getProgramInfoLog", "getRenderbufferParameter", "getShaderParameter", "getShaderInfoLog", "getShaderPrecisionFormat", "getShaderSource", "getSupportedExtensions", "getTexParameter", "getUniform", "getUniformLocation", "getVertexAttrib", "getVertexAttribOffset", "hint", "isBuffer", "isContextLost", "isEnabled", "isFramebuffer", "isProgram", "isRenderbuffer", "isShader", "isTexture", "lineWidth", "linkProgram", "pixelStorei", "polygonOffset", "readPixels", "renderbufferStorage", "sampleCoverage", "scissor", "shaderSource", "stencilFunc", "stencilFuncSeparate", "stencilMask", "stencilMaskSeparate", "stencilOp", "stencilOpSeparate", "texParameterf", "texParameteri", "texImage2D", "texSubImage2D", "uniform1f", "uniform1fv", "uniform1i", "uniform1iv", "uniform2f", "uniform2fv", "uniform2i", "uniform2iv", "uniform3f", "uniform3fv", "uniform3i", "uniform3iv", "uniform4f", "uniform4fv", "uniform4i", "uniform4iv", "uniformMatrix2fv", "uniformMatrix3fv", "uniformMatrix4fv", "useProgram", "validateProgram", "vertexAttrib1f", "vertexAttrib1fv", "vertexAttrib2f", "vertexAttrib2fv", "vertexAttrib3f", "vertexAttrib3fv", "vertexAttrib4f", "vertexAttrib4fv", "vertexAttribPointer", "viewport"));

        CONSTRUCTOR = ObjectLabel.make(DOMObjects.WEBGLRENDERINGCONTEXT_CONSTRCUTOR, ObjectLabel.Kind.FUNCTION);
        PROTOTYPE = ObjectLabel.make(DOMObjects.WEBGLRENDERINGCONTEXT_PROTOTYPE, ObjectLabel.Kind.OBJECT);
        INSTANCES = ObjectLabel.make(DOMObjects.WEBGLRENDERINGCONTEXT_INSTANCES, ObjectLabel.Kind.OBJECT);

        // Constructor Object
        s.newObject(CONSTRUCTOR);
        pv.writePropertyWithAttributes(CONSTRUCTOR, "length", Value.makeNum(0).setAttributes(true, true, true));
        pv.writePropertyWithAttributes(CONSTRUCTOR, "prototype", Value.makeObject(PROTOTYPE).setAttributes(true, true, true));
        s.writeInternalPrototype(CONSTRUCTOR, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        pv.writeProperty(DOMWindow.WINDOW, "WebGLRenderingContext", Value.makeObject(CONSTRUCTOR));

        // Prototype Object
        s.newObject(PROTOTYPE);
        s.writeInternalPrototype(PROTOTYPE, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE));

        // Multiplied Object
        s.newObject(INSTANCES);
        s.writeInternalPrototype(INSTANCES, Value.makeObject(PROTOTYPE));

        /*
         * Properties.
         */
        for (String propertyName : scrapedNumericPropertyNames) {
            createDOMProperty(INSTANCES, propertyName, Value.makeAnyNum(), c);
        }

        s.multiplyObject(INSTANCES);
        INSTANCES = INSTANCES.makeSingleton().makeSummary();

        /*
         * Functions.
         */
        ObjectLabel dummyFunction = ObjectLabel.make(DOMObjects.WEBGLRENDERINGCONTEXT_TAJS_UNSUPPORTED_FUNCTION, ObjectLabel.Kind.FUNCTION);
        for (String propertyName : scrapedFunctionPropertyNames) {
            // explicit implementation of: createDOMFunction(PROTOTYPE, DOMObjects.WEBGLRENDERINGCONTEXT_SOME_FUNCTION, propertyName, ***Value.makeAnyNum()***);
            s.newObject(dummyFunction);
            pv.writePropertyWithAttributes(PROTOTYPE, propertyName, Value.makeObject(dummyFunction).setAttributes(true, false, false));
            pv.writePropertyWithAttributes(dummyFunction, "length", Value.makeAnyNum().setAttributes(true, true, true));
            s.writeInternalPrototype(dummyFunction, Value.makeObject(InitialStateBuilder.FUNCTION_PROTOTYPE));
        }
    }

    public static Value evaluate(DOMObjects nativeObject, FunctionCalls.CallInfo call, Solver.SolverInterface c) {
        State s = c.getState();
        switch (nativeObject) {
            case WEBGLRENDERINGCONTEXT_CONSTRCUTOR:
                Exceptions.throwTypeError(c);
                s.setToBottom();
            case WEBGLRENDERINGCONTEXT_TAJS_UNSUPPORTED_FUNCTION:
                throw new AnalysisException("This function from WebGLRenderingContext is not yet supported: " + call.getJSSourceNode().getSourceLocation());
            default: {
                throw new UnsupportedOperationException("Unsupported Native Object: " + nativeObject);
            }
        }
    }
}
