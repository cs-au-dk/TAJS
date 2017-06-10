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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Concrete semantics implementation.
 * Will perform concrete evaluation of concrete values using the Java native implementation of JavaScript.
 */
public class NashornConcreteSemantics implements NativeConcreteSemantics {

    private static final Logger log = Logger.getLogger(NashornConcreteSemantics.class);

    private final ScriptEngine engine;

    public NashornConcreteSemantics() {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public static void main(String[] args) throws ScriptException {
        Object o = new NashornConcreteSemantics()._eval("var base = \"abcdefg\"; var argumentsList = [1.00000000000000000000]; var result = String.prototype.substring.apply(base, argumentsList); ({base: base, argumentsList: argumentsList, result: result, MAPPING_OBJECT: true});");
        System.out.println(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MappedNativeResult<ConcreteValue> apply(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
        if ("String.prototype.startsWith".equals(functionName)) { // Nashorn does not implement this "standard" feature
            if (isConcreteString(base) && arguments.size() == 1 && isConcreteString(arguments.get(0))) {
                boolean startsWith = ((ConcreteString) base).getString().startsWith(((ConcreteString) arguments.get(0)).getString());
                return new MappedNativeResult<>(Optional.empty(), NativeResult.makeValue(new ConcreteBoolean(startsWith)));
            }
            return new MappedNativeResult<>(Optional.empty(), NativeResult.makeNonConcrete());
        }
        if ("String.prototype.endsWith".equals(functionName)) { // Nashorn does not implement this "standard" feature
            if (isConcreteString(base) && arguments.size() == 1 && isConcreteString(arguments.get(0))) {
                boolean endsWith = ((ConcreteString) base).getString().endsWith(((ConcreteString) arguments.get(0)).getString());
                return new MappedNativeResult<>(Optional.empty(), NativeResult.makeValue(new ConcreteBoolean(endsWith)));
            }
            return new MappedNativeResult<>(Optional.empty(), NativeResult.makeNonConcrete());
        }
        Object evalResult;
        try {
            String script = ConcreteApplyMapping.formatMappedValuesScript(functionName, base, arguments);
            evalResult = _eval(script);
        } catch (Throwable t) {
            return new MappedNativeResult<>(Optional.empty(), handleEvalThrowable(t));
        }
        if (evalResult instanceof ScriptObjectMirror && ((ScriptObjectMirror) evalResult).containsKey(ConcreteApplyMapping.MAGIC_IDENTIFIER)) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) evalResult;
            ConcreteApplyMapping mappedInvocation = new ConcreteApplyMapping(
                    toConcreteValue(mirror.get(ConcreteApplyMapping.BASE)),
                    toConcreteArray((ScriptObjectMirror) mirror.get(ConcreteApplyMapping.ARGUMENTS_LIST)),
                    toConcreteValue(mirror.get(ConcreteApplyMapping.RESULT))
            );
            return new MappedNativeResult<>(Optional.of(mappedInvocation), NativeResult.makeValue(mappedInvocation.getResult()));
        }
        throw new AnalysisException("Cound not read result (" + evalResult + ") of call to " + functionName);
    }

    private Boolean isConcreteString(ConcreteValue base) {
        return base.accept(new ConcreteValueVisitor<Boolean>() {
            @Override
            public Boolean visit(ConcreteNumber v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteString v) {
                return true;
            }

            @Override
            public Boolean visit(ConcreteArray v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteUndefined v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteRegularExpression v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteNull v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteNullOrUndefined v) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteBoolean v) {
                return false;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public NativeResult<ConcreteValue> eval(String script) {
        try {
            return NativeResult.makeValue(toConcreteValue(_eval(script)));
        } catch (Throwable t) {
            return handleEvalThrowable(t);
        }
    }

    private <T extends ConcreteValue> NativeResult<T> handleEvalThrowable(Throwable t) {
        if (t.getCause() instanceof ECMAException) {
            return NativeResult.makeException();
        }
        if (Options.get().isDebugEnabled()) {
            log.error(t);
        }
        return NativeResult.makeNonConcrete();
    }

    private Object _eval(String script) throws ScriptException {
        Object resultObject = engine.eval(script);
        // System.out.println("   ==> " + result);
        //System.out.println(scripts.getResults().size());

        return resultObject;
    }

    private ConcreteArray toConcreteArray(ScriptObjectMirror array) {
        Integer length = (Integer) ScriptUtils.convert(array.get("length"), Integer.class);
        final ConcreteValue[] concreteEntries = new ConcreteValue[length];
        for (int i = 0; i < length; i++) {
            concreteEntries[i] = toConcreteValue(array.get(Integer.toString(i)));
        }
        Map<String, ConcreteValue> extraProperties = newMap();
        array.keySet().forEach(k -> extraProperties.put(k, toConcreteValue(array.getMember(k))));
        return new ConcreteArray(Arrays.asList(concreteEntries), extraProperties);
    }

    private ConcreteValue toConcreteValue(Object value) {
        if (value == null) {
            return new ConcreteNullOrUndefined(); // Nashorn explicitly coerces `undefined` to `null` :(
        }
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) value;
            if (mirror.isArray()) {
                return toConcreteArray(mirror);
            }
            if ("RegExp".equals(mirror.getClassName())) {
                return toConcreteRegularExpression(mirror);
            }
            throw new IllegalArgumentException("Cannot convert object value to concrete values: " + value + " of inner type " + mirror.getClassName());
        }
        if (value instanceof Number) {
            return new ConcreteNumber(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            return new ConcreteString((String) value);
        }
        if (value instanceof Undefined) { // type is internal, and Nashorn explicitly converts this value to null, the branch is likely dead
            return new ConcreteUndefined();
        }
        if (value instanceof Boolean) {
            return new ConcreteBoolean((Boolean) value);
        }
        throw new IllegalArgumentException("Cannot convert value to concrete values: " + value + " of type " + value.getClass());
    }

    private ConcreteValue toConcreteRegularExpression(ScriptObjectMirror mirror) {
        ConcreteString source = new ConcreteString((String) mirror.get("source"));
        Object lastIndexObject = mirror.get("lastIndex");
        ConcreteNumber lastIndex = new ConcreteNumber(lastIndexObject instanceof Integer ? ((Integer) lastIndexObject).doubleValue() : (Double) lastIndexObject);
        ConcreteBoolean global = new ConcreteBoolean((Boolean) mirror.get("global"));
        ConcreteBoolean ignoreCase = new ConcreteBoolean((Boolean) mirror.get("ignoreCase"));
        ConcreteBoolean multiline = new ConcreteBoolean((Boolean) mirror.get("multiline"));
        return new ConcreteRegularExpression(source, global, ignoreCase, multiline, lastIndex);
    }
}
