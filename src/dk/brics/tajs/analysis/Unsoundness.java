/*
 * Copyright 2009-2019 Aarhus University
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

package dk.brics.tajs.analysis;

import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.EventDispatcherNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ObjectProperty;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.Str;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.options.UnsoundnessOptionValues;
import dk.brics.tajs.solver.Message.Severity;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Controls intentional unsoundness choices.
 * <p/>
 * This class is supposed to contain all implementations of unsound (micro-)transfers in TAJS.
 * The core analysis implementation should focus on being sound, and dispatch to this class at locations where unsoundness could be used.
 * <p/>
 * Implementation notes:
 * <ul>
 * <li>This class decides whether an unsound (micro-)transfer function is used or not. (Preferably all controlled by {@link UnsoundnessOptionValues}, but that is not always the case currently (see TODOs))</li>
 * <li>{@link #addMessage(AbstractNode, String)} should be used whenever unsoundness is applied. {@link UnsoundnessOptionValues#showUnsoundnessUsage} can be used to show where unsound transfer functions have been applied.</li>
 * </ul>
 */
public class Unsoundness {

    private static Logger log = Logger.getLogger(Unsoundness.class);

    private final UnsoundnessOptionValues options;

    private final MessageCollector messageCollector;

    /**
     * Constructor with a configuration and a collector for the messages about unsoundness.
     */
    public Unsoundness(UnsoundnessOptionValues options, MessageCollector messageCollector) {
        this.options = options;
        this.messageCollector = messageCollector;
    }

    /**
     * Convenience method for adding a message if an unsound choice was made.
     */
    private boolean addMessageIfUnsound(AbstractNode node, boolean unsound, String msg) {
        if (!unsound) {
            return false;
        }
        addMessage(node, msg);
        return true;
    }

    /**
     * Records that unsoundness was used.
     */
    public void addMessage(AbstractNode node, String msg) {
        if (options.isShowUnsoundnessUsage()) {
            log.debug(msg + " at " + node.getClass().getSimpleName() + " " + node.getSourceLocation());
            messageCollector.accept(node, Severity.TAJS_UNSOUNDNESS, msg);
        }
    }

    /**
     * Decides if a dynamic property read should consider a particular property name.
     */
    public boolean maySkipSpecificDynamicPropertyRead(AbstractNode node, PKey concretePropertyName) {
        if (!options.isIgnoreUnlikelyPropertyReads()) {
            return false;
        }
        Set<PKey> skippable = newSet(Arrays.asList(StringPKey.__PROTO__, StringPKey.make("constructor")));
        return addMessageIfUnsound(
                node,
                skippable.contains(concretePropertyName),
                "Skipping read of property '" + concretePropertyName + "'");
    }

    /**
     * Decides if a dynamic property write should consider a particular property name.
     */
    public boolean maySkipPropertyWrite(AbstractNode node, ObjectProperty property) {
        // TODO: enable by option?
        return false; /*addMessageIfUnsound(
                node,
                false,
                "Skipping write of property '" + property + "'");*/
    }

    /**
     * Decides if a write to a completely free variable should result in a write on the global object.
     */
    public boolean maySkipDeclaringGlobalVariablesImplicitly(AbstractNode node, String variableName) {
        return addMessageIfUnsound(
                node,
                options.isNoImplicitGlobalVarDeclarations(),
                "Skipping implicit declaration of global variable '" + variableName + "'");
    }

    /**
     * Decides if a an imprecise call to the Function-constructor can be treated as allocating an empty function.
     */
    public boolean maySimplifyImpreciseFunctionConstructor(CallNode node) {
        return addMessageIfUnsound(
                node,
                options.isIgnoreImpreciseFunctionConstructor(),
                "Simplifying result of imprecise constructor call to Function to a noop-function");
    }

    /**
     * Decides if a call to 'eval' at a non-call node (likely spurious) can be treated as a no-op.
     */
    public boolean mayIgnoreEvalCallAtNonCallNode(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isIgnoreAsyncEvals() && node instanceof EventDispatcherNode,
                "Ignoring call to eval at non-call node");
    }

    /**
     * Decides if 'Object.keys' produces a deterministically ordered array.
     */
    public boolean mayUseSortedObjectKeys(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isUseOrderedObjectKeys(),
                "Assuming Object.keys is sorted");
    }

    /**
     * Decides if 'Math.random' produces a fixed value.
     */
    public boolean mayUseFixedMathRandom(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isUseFixedRandom(),
                "Assuming Math.random returns fixed value");
    }

    /**
     * Decides if 'Date.now' produces a fixed value.
     */
    public boolean mayUseFixedDateNow(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isUseFixedRandom(),
                "Assuming Date.now returns fixed value");
    }

    /**
     * Decides if an imprecise call to 'eval' can be treated as a no-op.
     */
    public boolean mayIgnoreImpreciseEval(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isIgnoreImpreciseEvals(),
                "Ignoring imprecise call to eval");
    }

    /**
     * Maybe produces an unsound value for 'Function.prototype.toString.call(..)'.
     */
    public Optional<String> evaluate_FunctionToString(AbstractNode node, ObjectLabel functionLabel) {
        if (!options.isUsePreciseFunctionToString()) {
            return Optional.empty();
        }
        addMessage(node, "Assuming Function.prototype.toString is deterministic");
        return Optional.of(TAJSConcreteSemantics.convertFunctionToString(functionLabel));
    }

    /**
     * Decides if at call to a native function without an associated transfer function can be treated as a no-op.
     */
    public boolean maySkipMissingModelOfNativeFunction(AbstractNode node, HostObject nativObject) {
        return addMessageIfUnsound(
                node,
                options.isIgnoreMissingNativeModels(),
                "Ignoring missing model for native function '" + nativObject + "'");
    }

    /**
     * Decides if a call to 'String.prototype.toLocaleString' (and similar methods) can be treated like 'String.prototype.toString'.
     */
    public boolean mayAssumeFixedLocale(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                options.isIgnoreLocale(),
                "Assuming locale is fixed");
    }

    /**
     * Decides if the result of 'n + m', where 'n' and 'm' are unsigned integers, is also an unsigned integer.
     */
    public boolean mayAssumeClosedUIntAddition(AbstractNode node) {
        // TODO: enable by option? (GitHub #357)

        // TODO: see github #295

        // NB: Number.MAX_VALUE + Number.MAX_VALUE == Infinity, but Number.MAX_VALUE + (Number.MAX_VALUE / 100000000000000000) == Number.MAX_VALUE
        // So it is actually sound to treat UInt as closed under "small" additions.
        return addMessageIfUnsound(
                node,
                true,
                "Assuming UInt-addition is closed");
    }

    /**
     * Decides if a specific property name can be assumed not to be in the prototypes during a property read.
     */
    public boolean maySkipPrototypesForPropertyRead(AbstractNode node, Str propertyName, Value currentPropertyValue) {
        if (!options.isIgnoreSomePrototypesDuringDynamicPropertyReads()) {
            return false;
        }
        boolean isFuzzy = propertyName.isMaybeFuzzyStr();
        if (!isFuzzy) {
            return false;
        }
        boolean isOnlyFuzzyUInt = !propertyName.isMaybeStrSomeNonUInt()
                && propertyName.isMaybeStrSomeUInt();
        if (isOnlyFuzzyUInt) {
            addMessage(node, "Assuming array-like reads do not use prototypes");
            return true;
        }

        boolean skip = currentPropertyValue.isMaybePresent(); // if the property is definitely absent, then we probably ought to use the prototypes
        return addMessageIfUnsound(
                node,
                skip,
                "Assuming dynamic property read does not need to use prototypes");
    }

    /**
     * Decides if an imprecise write to outerHTML can be treated as a no-op.
     */
    public boolean mayIgnoreImpreciseInnerOuterHTML(AbstractNode node, String propertyName, Str rhs) {
        return addMessageIfUnsound(
                node,
                true, // TODO: enable by option? (GitHub #357)
                String.format("Ignoring imprecise write to .%s with value %s", propertyName, rhs));
    }

    /**
     * Decides if an property write should update the internal prototype.
     */
    public boolean maySkipInternalProtoPropertyWrite(AbstractNode node) {
        return addMessageIfUnsound(
                node,
                (node instanceof WritePropertyNode) && !((WritePropertyNode) node).isPropertyFixed(), // TODO: enable by option? (GitHub #357)
                "Skipping write to property '__proto__'");
    }

    /**
     * Decides if undefined should be ignored as the first argument to addition
     */
    public boolean mayIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(AbstractNode node, Value v) {
        boolean allowUnsoundness = options.isIgnoreUnlikelyUndefinedAsFirstArgumentToAddition()
                && v.isMaybeUndef() && v.isMaybeOtherThanUndef();
        return addMessageIfUnsound(
                node,
                allowUnsoundness,
                String.format("Ignoring unlikely undefined as first argument to addition for value: %s", v));
    }

    /**
     * Decides if the 'in' operator should be definitely true, when value to test 'v' is only numeric and 'v' is
     * maybe in the object.
     */
    public boolean mayAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(AbstractNode node, Value v, Value res) {
        boolean allowUnsoundness = options.isAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber()
                && v.restrictToNotNum().isNone() && res.isMaybeTrue();
        return addMessageIfUnsound(
                node,
                allowUnsoundness,
                "Ignoring result of 'in' operator is maybe false, because value to test is numeric and is maybe in object");
    }

    public void ignoringException(AbstractNode node, String exceptionKind) {
        addMessage(node, "Ignoring potential " + exceptionKind + " exception");
    }

    @FunctionalInterface
    public interface MessageCollector {

        void accept(AbstractNode n, Severity severity, String msg);
    }
}
