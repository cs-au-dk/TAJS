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

package dk.brics.tajs.monitoring.soundness.testing;

import dk.au.cs.casa.jer.entries.AllocationSiteObjectDescription;
import dk.au.cs.casa.jer.entries.BuiltinObjectDescription;
import dk.au.cs.casa.jer.entries.ConcreteStringDescription;
import dk.au.cs.casa.jer.entries.ObjectDescription;
import dk.au.cs.casa.jer.entries.ObjectDescriptionVisitor;
import dk.au.cs.casa.jer.entries.OtherDescription;
import dk.au.cs.casa.jer.entries.OtherObjectDescription;
import dk.au.cs.casa.jer.entries.OtherSymbolDescription;
import dk.au.cs.casa.jer.entries.PrefixStringDescription;
import dk.au.cs.casa.jer.entries.ValueDescription;
import dk.au.cs.casa.jer.entries.ValueDescriptionVisitor;
import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteBoolean;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteNumber;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteRegularExpression;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteString;
import dk.brics.tajs.analysis.nativeobjects.concrete.ConcreteValue;
import dk.brics.tajs.analysis.nativeobjects.concrete.MappedNativeResult;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.ValueLogLocationInformation;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.soundness.ValueLogSourceLocationEqualityDecider;
import dk.brics.tajs.util.Strings;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Compares abstract and concrete values, deciding if the abstract value over-approximates the concrete value.
 * <p>
 * TODO this class could use a thorough re-implementation (GitHub #415)
 */
public class AbstractConcreteValueComparator {

    private static final Logger log = Logger.getLogger(AbstractConcreteValueComparator.class);

    private static final ConcreteRegularExpression loggerRegExp_STR_IDENTIFIER = new ConcreteRegularExpression(
            new ConcreteString("^[_$a-zA-Z\\xA0-\\uFFFF][_$a-zA-Z0-9\\xA0-\\uFFFF]*$"),
            new ConcreteBoolean(false), new ConcreteBoolean(false), new ConcreteBoolean(false), new ConcreteNumber(0.0));

    private static final ConcreteRegularExpression loggerRegExp_STR_IDENTIFIER_PARTS = new ConcreteRegularExpression(
            new ConcreteString("^[_$a-zA-Z0-9\\xA0-\\uFFFF]*$"),
            new ConcreteBoolean(false), new ConcreteBoolean(false), new ConcreteBoolean(false), new ConcreteNumber(0.0));

    private final Set<String> canonicalNamesForBuiltins;

    private final ValueLogSourceLocationEqualityDecider equalityDecider;

    private final Set<SourceLocation> domObjectAllocationSites;

    private Predicate<SourceLocation> isHostEnvironmentSource;

    private ValueLogLocationInformation valueLogMappingInformation;

    public AbstractConcreteValueComparator(Set<String> canonicalNamesForBuiltins, ValueLogSourceLocationEqualityDecider equalityDecider, Set<SourceLocation> domObjectAllocationSites, ValueLogLocationInformation valueLogLocationInformation, Predicate<SourceLocation> isHostEnvironmentSource) {
        this.canonicalNamesForBuiltins = canonicalNamesForBuiltins;
        this.equalityDecider = equalityDecider;
        this.domObjectAllocationSites = domObjectAllocationSites;
        this.valueLogMappingInformation = valueLogLocationInformation;
        this.isHostEnvironmentSource = isHostEnvironmentSource;
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    public boolean isAbstractValueSound(ValueDescription concreteValue, Value abstractValue) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("isAbstractValueSound(%s, %s)", concreteValue, abstractValue));
        }
        if (abstractValue.isPolymorphicOrUnknown()) {
            return true;
        }

        return isAbstractValueSound_noStringSets(concreteValue, abstractValue.forgetExcludedIncludedStrings());
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    private boolean isAbstractValueSound_noStringSets(ValueDescription concreteValue, Value abstractValue) {
        if (isAbstractStringValueMorePreciseThanSemiConcreteValue(concreteValue, abstractValue)) {
            // technically, this could miss some unsoundness, but it is very convenient in practice.
            return true;
        }

        return concreteValue.accept(new ValueDescriptionVisitor<Boolean>() {
            @Override
            public Boolean visit(OtherDescription d) {
                return isAbstractValueSound(d, abstractValue);
            }

            @Override
            public Boolean visit(ConcreteStringDescription d) {
                return isAbstractValueSound(d, abstractValue);
            }

            @Override
            public Boolean visit(PrefixStringDescription d) {
                return isAbstractValueSound(d, abstractValue);
            }

            @Override
            public Boolean visit(ObjectDescription d) {
                return isAbstractValueSound(d, abstractValue);
            }
        });
    }

    /**
     * A log-entry can contain an abstracted concrete string. Thus a precise abstract value can in fact be more precise than a "concrete" value.
     */
    private boolean isAbstractStringValueMorePreciseThanSemiConcreteValue(ValueDescription concreteValue, Value abstractValue) {
        Value absValStrOnly = abstractValue.restrictToStr();
        if (!absValStrOnly.isMaybeSingleStr() && !abstractValue.isMaybeStrPrefix()) {
            return false;
        }
        return concreteValue.accept(new ValueDescriptionVisitor<Boolean>() {
            @Override
            public Boolean visit(OtherDescription d) {
                if (abstractValue.isMaybeStrPrefix()) {
                    return true; // TODO improve precision
                }
                String concreteValue = d.getDescription();
                if (concreteValue.equals("STR_UINT")) {
                    Value jalangiValue = Value.makeAnyStrUInt();
                    return jalangiValue.join(absValStrOnly).equals(jalangiValue);
                }
                if (concreteValue.equals("STR_OTHERNUM")) {
                    Value jalangiValue = Value.makeAnyStrOtherNum();
                    return jalangiValue.join(absValStrOnly).equals(jalangiValue);
                }
                if (concreteValue.equals("STR_IDENTIFIER") || concreteValue.equals("STR_IDENTIFIERPARTS")) {
                    // The classification of identifiers is immensely complex (see http://stackoverflow.com/a/9392578).
                    // Java/JavaScript tends to disagree on unicode-encodings, so we dispatch to the same regex that the logger made use of
                    ConcreteRegularExpression regexp = concreteValue.equals("STR_IDENTIFIER") ? loggerRegExp_STR_IDENTIFIER : loggerRegExp_STR_IDENTIFIER_PARTS;
                    return matchesLoggerRegExp(regexp, absValStrOnly.getStr());
                }
                if (concreteValue.equals("STR_PREFIX")) {
                    Value jalangiValue = Value.makeNone().joinPrefix(absValStrOnly.getStr());
                    return jalangiValue.join(absValStrOnly).equals(jalangiValue);
                }
                if (concreteValue.equals("STR_JSON")) {
                    Value jalangiValue = Value.makeJSONStr();
                    return jalangiValue.join(absValStrOnly).equals(jalangiValue);
                }
                if (concreteValue.equals("STR_OTHER")) {
                    Value jalangiValue = Value.makeNone().joinAnyStrOther();
                    Value temp = jalangiValue.join(absValStrOnly);
                    Boolean sound = temp.equals(jalangiValue);
                    return sound;
                }
                return false;
            }

            private Boolean matchesLoggerRegExp(ConcreteRegularExpression regexp, String abstractConcreteString) {
                MappedNativeResult<ConcreteValue> match = TAJSConcreteSemantics.getNative().apply("RegExp.prototype.test", regexp, Collections.singletonList(new ConcreteString(abstractConcreteString)));

                ConcreteBoolean value = (ConcreteBoolean) match.getResult().getValue();
                return value.getBooleanValue();
            }

            @Override
            public Boolean visit(ConcreteStringDescription d) {
                if (abstractValue.isMaybeStrPrefix()) {
                    return true; // TODO improve precision
                }
                if ((d.getString().startsWith("jQuery") && absValStrOnly.getStr().startsWith("jQuery")) ||
                        (d.getString().startsWith("sizzle") && absValStrOnly.getStr().startsWith("sizzle")) ||
                        (d.getString().startsWith("sizcache") && absValStrOnly.getStr().startsWith("sizcache")) ||
                        (d.getString().startsWith("script") && absValStrOnly.getStr().startsWith("script")) ||
                        (d.getString().startsWith("window.script") && absValStrOnly.getStr().startsWith("window.script")) ||
                        (d.getString().startsWith("<a name='script") && absValStrOnly.getStr().startsWith("<a name='script")) ||
                        (d.getString().startsWith("<input name='script") && absValStrOnly.getStr().startsWith("<input name='script")))
                    return true; // nasty hack related to -use-fixed-random (see also KnownUnsoundnesses)
                return d.getString().equals(absValStrOnly.getStr()) || isFunctionToStringComparison(absValStrOnly.getStr(), d.getString());
            }

            private boolean isFunctionToStringComparison(String s1, String s2) {
                return s1.startsWith("function") && s2.startsWith("function");
            }

            @Override
            public Boolean visit(PrefixStringDescription d) {
                if (abstractValue.isMaybeStrPrefix()) {
                    return true; // TODO improve precision
                }
                return absValStrOnly.getStr().startsWith(d.getString()) || isFunctionToStringComparison(absValStrOnly.getStr(), d.getString());
            }

            @Override
            public Boolean visit(ObjectDescription d) {
                return false;
            }
        });
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    private boolean isAbstractValueSound(OtherDescription d, Value abstractValue) {
        String concreteValue = d.getDescription();
        try {
            if (concreteValue.equals("-0")) {
                return abstractValue.isMaybeNum(-0.0);
            } else
                return abstractValue.isMaybeNum(Integer.parseInt(concreteValue));
        } catch (NumberFormatException e) {
            // squelch
        }

        if (concreteValue.equals("null")) {
            return abstractValue.isMaybeNull();
        }
        if (concreteValue.equals("undefined")) {
            return abstractValue.isMaybeUndef() || abstractValue.isMaybeAbsent();
        }
        if (concreteValue.equals("true")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.BOOLEAN)) {
                return true;
            }
            return abstractValue.isMaybeTrue();
        }
        if (concreteValue.equals("false")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.BOOLEAN)) {
                return true;
            }
            return abstractValue.isMaybeFalse();
        }
        if (concreteValue.equals("NUM_UINT")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.NUMBER)) {
                return true;
            }
            return abstractValue.isMaybeNumUInt() || (abstractValue.isMaybeSingleNum() && Value.isUInt32(abstractValue.getNum()));
        }
        if (concreteValue.equals("NUM_NAN")) {
            return abstractValue.isMaybeNaN();
        }
        if (concreteValue.equals("NUM_INF")) {
            return abstractValue.isMaybeInf();
        }
        if (concreteValue.equals("NUM_OTHER")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.NUMBER)) {
                return true;
            }
            return abstractValue.isMaybeNumOther() || (abstractValue.isMaybeSingleNum() && !Value.isUInt32(abstractValue.getNum()));
        }
        if (concreteValue.equals("STR_UINT")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            boolean isSingleUintString = false;
            try {
                isSingleUintString = (abstractValue.isMaybeSingleStr() && Value.isUInt32(Double.parseDouble(abstractValue.getStr())));
            } catch (NumberFormatException ignored) {
            }
            return abstractValue.isMaybeStrUInt() || isSingleUintString;
        }
        if (concreteValue.equals("STR_OTHERNUM")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            return abstractValue.isMaybeStrOtherNum();
        }
        if (concreteValue.equals("STR_IDENTIFIER")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            Value jalangiValue = Value.makeAnyStrIdent();
            boolean isStrPrefix = abstractValue.isMaybeStrPrefix() || (abstractValue.isMaybeSingleStr() && Strings.isIdentifierAndNotPrefixOfReservedName(abstractValue.getStr()));
            //In valuelogger there STR_PREFIX and STR_IDENTIFIER is not distinguished
            boolean x = jalangiValue.join(abstractValue).equals(abstractValue) || isStrPrefix;
            return x;
        }
        if (concreteValue.equals("STR_IDENTIFIERPARTS")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            Value jalangiValue = Value.makeNone().joinAnyStrIdentifierParts();
            return jalangiValue.join(abstractValue).equals(abstractValue);
        }
        if (concreteValue.equals("STR_PREFIX")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            return abstractValue.isMaybeStrPrefix() || (abstractValue.getStr() != null && Strings.isIdentifierAndNotPrefixOfReservedName(abstractValue.getStr()));
        }
        if (concreteValue.equals("STR_JSON")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            return abstractValue.isMaybeStrJSON();
        }
        if (concreteValue.equals("STR_OTHER")) {
            if (isBoxed(abstractValue, ObjectLabel.Kind.STRING)) {
                return true;
            }
            Value jalangiValue = Value.makeNone().joinAnyStrOther();
            return jalangiValue.join(abstractValue).equals(abstractValue);
        }

        throw new RuntimeException("Unexpected valueDescription " + concreteValue + " (expected: " + abstractValue + ")");
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    private Boolean isAbstractValueSound(ConcreteStringDescription d, Value abstractValue) {
        return abstractValue.isMaybeStr(d.getString());
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    private Boolean isAbstractValueSound(PrefixStringDescription d, Value abstractValue) {
        if (abstractValue.isMaybeStrPrefix() || abstractValue.isMaybeSingleStr()) {
            Value abstractConcretePrefix = Value.makeNone().joinPrefix(d.getString());
            return abstractConcretePrefix.join(abstractValue).equals(abstractValue);
        }
        return abstractValue.isMaybeFuzzyStr(); // TODO improve precision
    }

    /**
     * @return true iff the abstract value over-approximates the concrete value.
     */
    private boolean isAbstractValueSound(ObjectDescription d, Value abstractValue) {
        if (!abstractValue.isMaybeObjectOrSymbol()) {
            return false;
        }
        return d.accept(new ObjectDescriptionVisitor<Boolean>() {
            @Override
            public Boolean visit(OtherObjectDescription o) {
                // Not quite satisfactory. But it is the best we can do for now.
                return abstractValue.isMaybeObject();
            }

            @Override
            public Boolean visit(AllocationSiteObjectDescription o) {
                boolean success = abstractValue.getObjectLabels().stream()
                        .anyMatch(l -> {
                            boolean hasLocation = l.getSourceLocation().getLocation() != null; // Give up on trying to match source locations from code without a physical location
                            if (!hasLocation) {
                                return true;
                            }
                            boolean isMatchingAllocationSite = !l.isHostObject() && equalityDecider.areEqual(o.getAllocationSite(), l.getSourceLocation());
                            if (isMatchingAllocationSite) {
                                return true;
                            }
                            // declared getter/setters can not be intercepted: uses the allocation site of their literal (which is a mess to track)
                            boolean isUserFunction = l.getKind() == ObjectLabel.Kind.FUNCTION && !l.isHostObject();
                            Set<SourceLocation> declaredAccessorAllocationSites = valueLogMappingInformation.getDeclaredAccessorAllocationSites();
                            boolean isDeclaredAccessor = isUserFunction && declaredAccessorAllocationSites.contains(l.getSourceLocation());
                            if (isDeclaredAccessor) {
                                return true;
                            }
                            // `new ImageElement`, will make an object without an allocation site...
                            boolean isDOMObject = l.isHostObject() && l.getHostObject().getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL;
                            boolean isUserAllocatedDOMObject = isDOMObject && domObjectAllocationSites.stream().anyMatch(domAllocation -> equalityDecider.areEqual(o.getAllocationSite(), domAllocation));
                            return isUserAllocatedDOMObject;
                        });
                return success;
            }

            @Override
            public Boolean visit(BuiltinObjectDescription o) {
                // matching against Java-implemented native objects
                if (canonicalNamesForBuiltins.contains(o.getCanonicalName())) {
                    return abstractValue.getObjectLabels().stream()
                            .filter(ObjectLabel::isHostObject)
                            .anyMatch(l -> {

                                String hostToString = l.getHostObject().toString();
                                return hostToString.equals(o.getCanonicalName()) || (hostToString.startsWith("Window.") && hostToString.substring("Window.".length()).equals(o.getCanonicalName()));
                            });
                }

                // rough matching for unnamed polyfill-objects
                return abstractValue.getObjectLabels().stream().anyMatch(l -> isHostEnvironmentSource.test(l.getSourceLocation()));
            }

            @Override
            public Boolean visit(OtherSymbolDescription otherSymbolDescription) {
                // TODO: First raw approximation (jalangilogger doesn't collect symbol names and allocation sites, but the result of calling toString) - github #513
                return !abstractValue.getSymbols().isEmpty();
            }
        });
    }

    private boolean isBoxed(Value abstractValue, ObjectLabel.Kind kind) {
        // TODO check unboxed value instead
        return abstractValue.isMaybeObject() && abstractValue.getObjectLabels().stream().anyMatch(l -> l.getKind() == kind);
    }
}
