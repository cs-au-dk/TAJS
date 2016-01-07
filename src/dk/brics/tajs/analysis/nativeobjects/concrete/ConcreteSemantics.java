package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.util.None;
import dk.brics.tajs.util.Optional;
import dk.brics.tajs.util.Some;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.Undefined;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Concrete semantics implementation.
 * Will perform concrete evaluation of concrete values using the Java native implementation of JavaScript.
 */
public class ConcreteSemantics {

    private static final Logger log = Logger.getLogger(ConcreteSemantics.class);

    private static ConcreteSemantics instance;

    private final ScriptEngineManager factory;

    private ConcreteSemantics() {
        factory = new ScriptEngineManager();
    }

    public static ConcreteSemantics get() {
        if (instance == null) {
            instance = new ConcreteSemantics();
        }
        return instance;
    }

//    public static void main(String[] args) throws ScriptException {
//        System.out.println(get().toConcreteValue((get().makeEngine().eval("[null, undefined]"))));
//    }

    public Optional<ConcreteValue> apply(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
        String script = String.format("%s.apply(%s, [%s]);", functionName, base.toSourceCode(), makeList(arguments));
        return eval(script);
    }

    public Optional<ConcreteValue> eval(String script) {
        ScriptEngine engine = makeEngine();
        // System.out.println(script);
        try {
            Object resultObject = engine.eval(script);
            // System.out.println("   ==> " + result);
            //System.out.println(scripts.getResults().size());
            return Some.make(toConcreteValue(resultObject));
        } catch (Throwable t) {
            log.error(t);
            return None.make();
        }
    }

    private ScriptEngine makeEngine() {
        return factory.getEngineByName("JavaScript");
    }

    private static String makeList(List<ConcreteValue> arguments) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ConcreteValue argument : arguments) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(argument.toSourceCode());
        }

        return sb.toString();
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
