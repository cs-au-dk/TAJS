/*
 * Copyright 2009-2016 Aarhus University
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
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Concrete semantics implementation.
 * Will perform concrete evaluation of concrete values using the Java native implementation of JavaScript.
 */
public class NashornConcreteSemantics { // XXX: singleton, but no reset method? just use static methods instead?

    private static final Logger log = Logger.getLogger(NashornConcreteSemantics.class);

    private static NashornConcreteSemantics instance;

    private final ScriptEngine engine;

    private NashornConcreteSemantics() {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public static NashornConcreteSemantics get() {
        if (instance == null) {
            instance = new NashornConcreteSemantics();
        }
        return instance;
    }

//    public static void main(String[] args) throws ScriptException {
//        System.out.println(get().toConcreteValue((get().makeEngine().eval("[null, undefined]"))));
//    }

    private static String makeList(List<ConcreteValue> arguments) {
        return String.join(",", arguments.stream().map(ConcreteValue::toSourceCode).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    public <T extends ConcreteValue> InvocationResult<T> apply(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
        if ("String.prototype.startsWith".equals(functionName)) { // Nashorn does not implement this "standard" feature
            if (isConcreteString(base) && arguments.size() == 1 && isConcreteString(arguments.get(0))) {
                boolean startsWith = ((ConcreteString) base).getString().startsWith(((ConcreteString) arguments.get(0)).getString());
                return InvocationResult.makeValue((T) new ConcreteBoolean(startsWith));
            }
            return InvocationResult.makeNonConcrete();
        }
        if ("String.prototype.endsWith".equals(functionName)) { // Nashorn does not implement this "standard" feature
            if (isConcreteString(base) && arguments.size() == 1 && isConcreteString(arguments.get(0))) {
                boolean endsWith = ((ConcreteString) base).getString().endsWith(((ConcreteString) arguments.get(0)).getString());
                return InvocationResult.makeValue((T) new ConcreteBoolean(endsWith));
            }
            return InvocationResult.makeNonConcrete();
        }
        String script = String.format("%s.apply(%s, [%s]);", functionName, base.toSourceCode(), makeList(arguments));
        return eval(script);
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
            public Boolean visit(ConcreteBoolean v) {
                return false;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends ConcreteValue> InvocationResult<T> eval(String script) {
        // System.out.println(script);
        try {
            Object resultObject = engine.eval(script);
            // System.out.println("   ==> " + result);
            //System.out.println(scripts.getResults().size());
            return InvocationResult.makeValue((T) toConcreteValue(resultObject));
        } catch (Throwable t) {
            if (t.getCause() instanceof ECMAException) {
                return InvocationResult.makeException();
            }
            if (Options.get().isDebugEnabled()) {
                log.error(t);
            }
            return InvocationResult.makeNonConcrete();
        }
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
            return new ConcreteNull();
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
        if (value instanceof Undefined) {
            return new ConcreteUndefined();
        }
        if (value instanceof Boolean) {
            return new ConcreteBoolean((Boolean) value);
        }
        throw new IllegalArgumentException("Cannot convert value to concrete values: " + value + " of type " + value.getClass());
    }

    private ConcreteValue toConcreteRegularExpression(ScriptObjectMirror mirror) {
        ConcreteString source = new ConcreteString((String) mirror.get("source"));
        ConcreteBoolean global = new ConcreteBoolean((Boolean) mirror.get("global"));
        ConcreteBoolean ignoreCase = new ConcreteBoolean((Boolean) mirror.get("ignoreCase"));
        ConcreteBoolean multiline = new ConcreteBoolean((Boolean) mirror.get("multiline"));
        return new ConcreteRegularExpression(source, global, ignoreCase, multiline);
    }
}
