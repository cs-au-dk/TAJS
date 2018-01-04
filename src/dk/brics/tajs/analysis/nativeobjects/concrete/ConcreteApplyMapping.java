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

package dk.brics.tajs.analysis.nativeobjects.concrete;

import dk.brics.tajs.util.Collectors;

import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Mapping for the receiver, arguments and results of a call using Function.prototype.apply.
 */
public class ConcreteApplyMapping {

    public static final String BASE = "base";

    public static final String ARGUMENTS_LIST = "argumentsList";

    public static final String RESULT = "result";

    public static final String MAGIC_IDENTIFIER = "MAPPING_OBJECT";

    private final ConcreteValue base;

    private final ConcreteArray arguments;

    private final ConcreteValue result;

    public ConcreteApplyMapping(ConcreteValue base, ConcreteArray arguments, ConcreteValue result) {
        this.base = base;
        this.arguments = arguments;
        this.result = result;
    }

    /**
     * Formats the call to Function.prototype.apply as an expression that produces the mapping for this class.
     */
    public static String formatMappedValuesScript(String functionName, ConcreteValue base, List<ConcreteValue> arguments) {
        return format(functionName, base, arguments, String.format("({%s: %s, %s: %s, %s: %s, %s: %s});", BASE, BASE, ARGUMENTS_LIST, ARGUMENTS_LIST, RESULT, RESULT, MAGIC_IDENTIFIER, true));
    }

    /**
     * Formats the call to Function.prototype.apply as an expression that is true if the result is null.
     */
    public static String formatNullResultCheckScript(String functionName, ConcreteValue base, List<ConcreteValue> arguments){
        return format(functionName, base, arguments, String.format("%s === null;", RESULT));
    }

    private static String format(String functionName, ConcreteValue base, List<ConcreteValue> arguments, String resultExpression) {
        List<String> lines = newList();
        String argumentsList = String.join(",", arguments.stream().map(ConcreteValue::toSourceCode).collect(Collectors.toList()));
        lines.add(String.format("var %s = %s;", BASE, base.toSourceCode()));
        lines.add(String.format("var %s = [%s];", ARGUMENTS_LIST, argumentsList));
        lines.add(String.format("var %s = %s.apply(%s, %s);", RESULT, functionName, BASE, ARGUMENTS_LIST));
        lines.add(resultExpression);
        return String.join(" ", lines);
    }

    public ConcreteValue getResult() {
        return result;
    }

    public ConcreteValue getBase() {
        return base;
    }

    public ConcreteArray getArguments() {
        return arguments;
    }
}
