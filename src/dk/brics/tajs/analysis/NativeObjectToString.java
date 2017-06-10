package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Implementation of Object.prototype.toString, which depends on the host environment.
 */
// TODO refactor DOM/ECMA/XYZ host functionality to respective sub-packages to get proper dependencies
// TODO support GitHub #373
public class NativeObjectToString {

    private static final Set<DOMObjects> DOMElementInstanceHostObjects = newSet(
            Arrays.asList(DOMObjects.values()).stream()
                    .filter(e -> e == DOMObjects.HTMLDOCUMENT_INSTANCES || e.toString().endsWith("Element instances")) // NB: relying on enum.toString
                    .collect(Collectors.toList()));

    public static Value evaluate(Value v, Solver.SolverInterface c) {
        if (v.isPolymorphicOrUnknown()) {
            throw new AnalysisException("Cannot do toString on Unknown/Polymorphic values");
        }

        if (v.isNone()) {
            return Value.makeNone();
        }

        Separator<String> separator = new Separator<>();

        // specials
        separator.choose(v.isMaybeUndef(), "Undefined");
        separator.choose(v.isMaybeNull(), "Null");

        // objects
        Value vObject = Conversion.toObject(c.getNode(), v.restrictToNotNullNotUndef(), c);
        Set<ObjectLabel> nonDOMElementInstanceObjects;
        if (Options.get().isDOMEnabled()) {
            Map<Boolean, List<ObjectLabel>> domElementInstanceGrouped = vObject.getObjectLabels().stream()
                    .collect(Collectors.groupingBy(l -> {
                        boolean isDOMElementInstance = l.isHostObject()
                                && l.getHostObject().getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL
                                && DOMElementInstanceHostObjects.contains(l.getHostObject());
                        return isDOMElementInstance;
                    }));

            Set<DOMObjects> DOMElementInstanceObjects = domElementInstanceGrouped.getOrDefault(true, newList()).stream()
                    .map(l -> (DOMObjects) l.getHostObject())
                    .collect(Collectors.toSet());

            // DOMElements have special treatment in Object.prototype.toString, e.g. [object HTMLDocument], evaluate them now
            DOMElementInstanceHostObjects.forEach(e -> {
                        String elementName = e.toString().replace(" instances", ""); // NB: relying on enum.toString
                        separator.choose(DOMElementInstanceObjects.contains(e), elementName);
                    }
            );

            nonDOMElementInstanceObjects = newSet(domElementInstanceGrouped.getOrDefault(false, newList()));
        } else {
            nonDOMElementInstanceObjects = vObject.getObjectLabels();
        }

        Set<ObjectLabel.Kind> nonDomElementKinds = nonDOMElementInstanceObjects.stream()
                .map(ObjectLabel::getKind).collect(Collectors.toSet());
        Arrays.stream(ObjectLabel.Kind.values()).forEach(k ->
                separator.choose(nonDomElementKinds.contains(k), k.toString())); // NB: relying on enum.toString

        if (separator.positive.isEmpty()) {
            throw new AnalysisException("No case for toString on " + v + "???");
        }

        Function<String, String> stringWrapper = s -> "[object " + (s.charAt(0) + "").toUpperCase() + s.substring(1) + "]";

        Set<Value> positives = separator.positive.stream().map(stringWrapper).map(Value::makeStr).collect(Collectors.toSet());
        Set<String> negatives = separator.negative.stream().map(stringWrapper).collect(Collectors.toSet());
        return Value.join(positives); // TODO add .removeStrings once complement-strings are supported
    }

    private static class Separator<T> {

        private Set<T> positive = newSet();

        private Set<T> negative = newSet();

        public void choose(boolean choice, T result) {
            (choice ? positive : negative).add(result);
        }
    }
}
