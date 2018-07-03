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

package dk.brics.tajs.lattice;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.ObjectLabel.Kind;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;
import dk.brics.tajs.util.Strings;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Abstract value.
 * Value objects are immutable.
 */
public final class Value implements Undef, Null, Bool, Num, Str, PKeys, DeepImmutable {

    private final static int BOOL_TRUE = 0x00000001; // true

    private final static int BOOL_FALSE = 0x00000002; // false

    private final static int UNDEF = 0x00000004; // undefined

    private final static int NULL = 0x00000008; // null

    private final static int STR_UINT = 0x00000010; // strings representing numbers that are UInt32

    private final static int STR_OTHERNUM = 0x00000020; // strings representing unbounded non-UInt32 numbers, including Infinity, -Infinity, and NaN

    private final static int STR_PREFIX = 0x00000040; // strings that consist of a fixed nonempty string followed by an unknown string

    private final static int STR_IDENTIFIER = 0x00000080; // strings that are valid identifiers (excluding reserved names but including "NaN" and "Infinity")

    private final static int STR_OTHERIDENTIFIERPARTS = 0x00000100; // strings that are valid identifier-parts (i.e. reserved names and identifiers without the start symbol), excluding STR_IDENTIFIER and STR_UINT

    private final static int STR_OTHER = 0x00000200; // strings not representing numbers and not identifier-parts

    private final static int STR_JSON = 0x00000400; // strings originating from a JSON source

    private final static int NUM_NAN = 0x00001000; // NaN

    private final static int NUM_INF = 0x00002000; // +/-Infinity

    private final static int NUM_UINT_POS = 0x00004000; // UInt32 numbers (not zero)

    private final static int NUM_OTHER = 0x00008000; // numbers that are not UInt32, not NaN, and not +/-Infinity

    private final static int ATTR_DONTENUM = 0x00010000; // [[DontEnum]] property

    private final static int ATTR_NOTDONTENUM = 0x00020000; // not [[DontEnum]] property

    private final static int ATTR_READONLY = 0x00040000; // [[ReadOnly]] property

    private final static int ATTR_NOTREADONLY = 0x00080000; // not [[ReadOnly]] property

    private final static int ATTR_DONTDELETE = 0x00100000; // [[DontDelete]] property

    private final static int ATTR_NOTDONTDELETE = 0x00200000; // not [[DontDelete]] property

    private final static int MODIFIED = 0x01000000; // maybe modified property (since function entry)

    private final static int ABSENT = 0x02000000; // maybe absent property

    private final static int PRESENT_DATA = 0x04000000; // maybe present data property, only used if var!=null

    private final static int PRESENT_ACCESSOR = 0x08000000; // maybe present getter/setter property, only used if var!=null

    private final static int UNKNOWN = 0x10000000; // unknown (lazy propagation)

    private final static int EXTENDEDSCOPE = 0x20000000; // for extended scope registers (for-in and finally blocks)

    private final static int NUM_ZERO = 0x40000000; // zero

    private final static int NUM_UINT = NUM_UINT_POS | NUM_ZERO; // UInt32 numbers

    private final static int BOOL = BOOL_TRUE | BOOL_FALSE;

    private final static int STR_IDENTIFIERPARTS = STR_UINT | STR_IDENTIFIER | STR_OTHERIDENTIFIERPARTS;

    private final static int STR = STR_OTHERNUM | STR_PREFIX | STR_IDENTIFIERPARTS | STR_OTHER | STR_JSON;

    private final static int NUM = NUM_NAN | NUM_INF | NUM_UINT | NUM_OTHER;

    private final static int ATTR_DONTENUM_ANY = ATTR_DONTENUM | ATTR_NOTDONTENUM;

    private final static int ATTR_READONLY_ANY = ATTR_READONLY | ATTR_NOTREADONLY;

    private final static int ATTR_DONTDELETE_ANY = ATTR_DONTDELETE | ATTR_NOTDONTDELETE;

    private final static int ATTR = ATTR_DONTENUM_ANY | ATTR_READONLY_ANY | ATTR_DONTDELETE_ANY;

    private final static int PROPERTYDATA = ATTR | MODIFIED;

    private final static int PRIMITIVE = UNDEF | NULL | BOOL | NUM | STR;

    private static Value theNone;

    private static Value theNoneModified;

    private static Value theUndef;

    private static Value theNull;

    private static Value theBoolTrue;

    private static Value theBoolFalse;

    private static Value theBoolAny;

    private static Value theStrAny;

    private static Value theStrUInt;

    private static Value theStrNotUInt;

    private static Value theJSONStr;

    private static Value theNumAny;

    private static Value theNumUInt;

    private static Value theNumUIntPos;

    private static Value theNumNotNaNInf;

    private static Value theNumOther;

    private static Value theNumNaN;

    private static Value theNumInf;

    private static Value theAbsent;

    private static Value theAbsentModified;

    private static Value theUnknown;

    /*
     * Representation invariant:
     * !((flags & (STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER)) != 0 && str != null)
     * &&
     * !((flags & STR_PREFIX) != 0 && (str == null || str.length == 0))
     * &&
     * !((flags & NUM_ANY) != 0 && num != null)
     * &&
     * !(object_labels != null && object_labels.isEmpty())
     * &&
     * !(getters != null && getters.isEmpty())
     * &&
     * !(setters != null && setters.isEmpty())
     * &&
     * !(num != null && Double.isNaN(num))
     * &&
     * !((flags & UNKNOWN) != 0 && ((flags & ~UNKNOWN) != 0 || str != null || num != null || !object_labels.isEmpty()) || !getters.isEmpty()) || !setters.isEmpty()))
     * &&
     * !(var != null && ((flags & PRIMITIVE) != 0 || str != null || num != null || !object_labels.isEmpty()) || !getters.isEmpty()) || !setters.isEmpty()))
     * &&
     * !((flags & (PRESENT_DATA | PRESENT_ACCESSOR) != 0 && var == null)
     * 
     * For the String facet, note that the various categories are not all disjoint.
     */

    /**
     * Value flags.
     */
    private int flags; // see invariant above

    /**
     * Constant number, may be +/-Infinity but not NaN.
     */
    private Double num;

    /**
     * Constant string or prefix.
     */
    private String str;

    /**
     * Property reference for polymorphic value.
     */
    private ObjectProperty var; // polymorphic if non-null

    /**
     * Possible values regarding object references and symbols.
     */
    private Set<ObjectLabel> object_labels;

    /**
     * Possible values regarding getters.
     */
    private Set<ObjectLabel> getters;

    /**
     * Possible values regarding setters.
     */
    private Set<ObjectLabel> setters;

    /**
     * Hash code.
     */
    private int hashcode;

    private static boolean canonicalizing; // set during canonicalization

    static {
        init();
    }

    private static void init() {
        theNone = reallyMakeNone();
        theNoneModified = reallyMakeNoneModified();
        theUndef = reallyMakeUndef(null);
        theNull = reallyMakeNull(null);
        theBoolTrue = reallyMakeBool(true);
        theBoolFalse = reallyMakeBool(false);
        theBoolAny = reallyMakeBool(null);
        theStrAny = reallyMakeAnyStr();
        theStrUInt = reallyMakeAnyStrUInt();
        theStrNotUInt = reallyMakeAnyStrNotUInt();
        theJSONStr = reallyMakeJSONStr();
        theNumAny = reallyMakeAnyNum();
        theNumUInt = reallyMakeAnyUInt();
        theNumUIntPos = reallyMakeAnyUIntPos();
        theNumNotNaNInf = reallyMakeAnyNumNotNaNInf();
        theNumOther = reallyMakeAnyNumOther();
        theNumNaN = reallyMakeNumNaN();
        theNumInf = reallyMakeNumInf();
        theAbsent = reallyMakeAbsent();
        theAbsentModified = reallyMakeAbsentModified();
        theUnknown = reallyMakeUnknown();
    }

    /**
     * Constructs a new none-value.
     */
    private Value() {
        flags = 0;
        num = null;
        str = null;
        object_labels = getters = setters = null;
        var = null;
        hashcode = 0;
    }

    /**
     * Constructs a shallow clone of the given value object.
     */
    private Value(Value v) {
        flags = v.flags;
        num = v.num;
        str = v.str;
        object_labels = v.object_labels;
        getters = v.getters;
        setters = v.setters;
        var = v.var;
        hashcode = v.hashcode;
    }

    /**
     * Put the value into canonical form.
     */
    private static Value canonicalize(Value v) {
        if (Options.get().isDebugOrTestEnabled()) { // checking representation invariants
            String msg = null;
            if ((v.flags & (STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER)) != 0 && v.str != null)
                msg = "fixed string and flags inconsistent";
            else if ((v.flags & STR_PREFIX) != 0 && (v.str == null || v.str.isEmpty()))
                msg = "prefix string inconsistent";
            else if ((v.flags & NUM) != 0 && v.num != null)
                msg = "number facet inconsistent";
            else if (v.num != null && Double.isNaN(v.num))
                msg = "number constant is NaN";
            else if (v.object_labels != null && v.object_labels.isEmpty())
                msg = "empty set of object labels";
            else if (v.getters != null && v.getters.isEmpty())
                msg = "empty set of getters";
            else if (v.setters != null && v.setters.isEmpty())
                msg = "empty set of setters";
            else if ((v.flags & UNKNOWN) != 0 && ((v.flags & ~UNKNOWN) != 0 || v.str != null || v.num != null
                    || (v.object_labels != null && !v.object_labels.isEmpty())
                    || (v.getters != null && !v.getters.isEmpty())
                    || (v.setters != null && !v.setters.isEmpty())))
                msg = "'unknown' inconsistent with other flags";
            else if (v.var != null && ((v.flags & PRIMITIVE) != 0 || v.str != null || v.num != null
                    || (v.object_labels != null && !v.object_labels.isEmpty())
                    || (v.getters != null && !v.getters.isEmpty())
                    || (v.setters != null && !v.setters.isEmpty())))
                msg = "mix of polymorphic and ordinary value";
            else if ((v.flags & (PRESENT_DATA | PRESENT_ACCESSOR)) != 0 && v.var == null)
                msg = "PRESENT set for non-polymorphic value";
            if (msg != null)
                throw new AnalysisException("Invalid value (0x" + Integer.toHexString(v.flags) + ","
                        + Strings.escape(v.str) + "," + v.num + "," + v.object_labels
                        + "," + v.getters + "," + v.setters + "), " + msg);
            if (Options.get().isPolymorphicDisabled() && v.isPolymorphic())
                throw new AnalysisException("Unexpected polymorphic value");
        }
        canonicalizing = true;
        if ((v.flags & NUM) == NUM_ZERO) {
            v.flags = v.flags & ~NUM;
            v.num = 0.0;
        }
        if (v.object_labels != null)
            v.object_labels = Canonicalizer.get().canonicalizeSet(v.object_labels);
        if (v.getters != null)
            v.getters = Canonicalizer.get().canonicalizeSet(v.getters);
        if (v.setters != null)
            v.setters = Canonicalizer.get().canonicalizeSet(v.setters);
        v.hashcode = v.flags * 17
                + (v.var != null ? v.var.hashCode() : 0)
                + (v.num != null ? v.num.hashCode() : 0)
                + (v.str != null ? v.str.hashCode() : 0)
                + (v.object_labels != null ? v.object_labels.hashCode() : 0)
                + (v.getters != null ? v.getters.hashCode() : 0)
                + (v.setters != null ? v.setters.hashCode() : 0);
        Value cv = Canonicalizer.get().canonicalize(v);
        canonicalizing = false;
        return cv;
    }

    /**
     * Resets the cache.
     */
    public static void reset() {
        init();
    }

    /**
     * Checks whether this value is polymorphic.
     */
    public boolean isPolymorphic() {
        return var != null;
    }

    /**
     * Checks whether this value is polymorphic or 'unknown'.
     */
    public boolean isPolymorphicOrUnknown() {
        return var != null || (flags & UNKNOWN) != 0;
    }

    /**
     * Returns the object property.
     * Only to be called if the value is polymorphic.
     */
    public ObjectProperty getObjectProperty() {
        return var;
    }

    /**
     * Constructs a fresh polymorphic value from the attributes (including absence and presence) of this value.
     */
    public Value makePolymorphic(ObjectProperty prop) {
        Value r = new Value();
        r.var = prop;
        r.flags |= flags & (ATTR | ABSENT | PRESENT_DATA | PRESENT_ACCESSOR | EXTENDEDSCOPE);
        if (isMaybePresentData())
            r.flags |= PRESENT_DATA;
        if (isMaybePresentAccessor())
            r.flags |= PRESENT_ACCESSOR;
        return canonicalize(r);
    }

    /**
     * Constructs a fresh non-polymorphic value using the attributes (excluding presence) of the given value.
     */
    public Value makeNonPolymorphic() {
        if (var == null)
            return this;
        Value r = new Value(this);
        r.var = null;
        r.flags &= ~(PRESENT_DATA | PRESENT_ACCESSOR);
        return canonicalize(r);
    }

    /**
     * Asserts that the value is not 'unknown'.
     *
     * @throws AnalysisException if the value is 'unknown'.
     */
    private void checkNotUnknown() {
        if (isUnknown())
            throw new AnalysisException("Unexpected 'unknown' value!");
    }

    /**
     * Asserts that the value is not polymorphic nor 'unknown'.
     *
     * @throws AnalysisException if the value is polymorphic or 'unknown'.
     */
    private void checkNotPolymorphicOrUnknown() {
        if (isPolymorphic())
            throw new AnalysisException("Unexpected polymorphic value!");
        if (isUnknown())
            throw new AnalysisException("Unexpected 'unknown' value!");
    }

    /**
     * Asserts that the value is not a getter/setter.
     *
     * @throws AnalysisException if the value contains getters or setters.
     */
    private void checkNoGettersSetters() {
        if (getters != null || setters != null)
            throw new AnalysisException("Unexpected getter/setter value!");
    }

    private static Value reallyMakeNone() {
        return canonicalize(new Value());
    }

    /**
     * Constructs the empty abstract value (= bottom, if not considering 'unknown').
     */
    public static Value makeNone() {
        return theNone;
    }

    private static Value reallyMakeNoneModified() {
        return canonicalize(new Value().joinModified());
    }

    /**
     * Constructs the empty abstract value that is marked as modified.
     */
    public static Value makeNoneModified() {
        return theNoneModified;
    }

    /**
     * Returns true if this abstract value represents no concrete values.
     * If a property value is "none", the abstract object represents zero concrete objects.
     * The modified flag, attributes, etc. are ignored.
     * "Unknown" is treated as non-"none".
     */
    public boolean isNone() {
        if (var == null)
            return (flags & (PRIMITIVE | ABSENT | UNKNOWN)) == 0 && num == null && str == null && object_labels == null && getters == null && setters == null;
        else
            return (flags & (ABSENT | PRESENT_DATA | PRESENT_ACCESSOR)) == 0;
    }

    /**
     * Checks whether this value is marked as maybe modified.
     */
    public boolean isMaybeModified() {
        if (Options.get().isModifiedDisabled())
            return !isUnknown(); // if we don't use the modified flag, use the fact that unknown implies non-modified
        return (flags & MODIFIED) != 0;
    }

    /**
     * Constructs a value as a copy of this value but marked as maybe modified.
     */
    public Value joinModified() {
        checkNotUnknown();
        if (isMaybeModified())
            return this;
        Value r = new Value(this);
        r.flags |= MODIFIED;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but marked as definitely not modified.
     */
    public Value restrictToNotModified() {
        if (!isMaybeModified())
            return this;
        Value r = new Value(this);
        r.flags &= ~MODIFIED;
        return canonicalize(r);
    }

    /**
     * Constructs the absent value.
     */
    public static Value makeAbsent() {
        return theAbsent;
    }

    /**
     * Constructs the absent modified value.
     */
    public static Value makeAbsentModified() {
        return theAbsentModified;
    }

    /**
     * Constructs the unknown value.
     */
    public static Value makeUnknown() {
        return theUnknown;
    }

    /**
     * Constructs a value as a copy of this value but definitely not absent.
     */
    public Value restrictToNotAbsent() {
        checkNotUnknown();
        if (isNotAbsent())
            return this;
        Value r = new Value(this);
        r.flags &= ~ABSENT;
        if (r.var != null && (r.flags & (PRESENT_DATA | PRESENT_ACCESSOR)) == 0)
            r.var = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but only with getter/setter values.
     */
    public Value restrictToGetterSetter() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybePrimitive() && !isMaybeObjectOrSymbol())
            return this;
        Value r = new Value(this);
        r.flags &= ~PRIMITIVE;
        r.num = null;
        r.str = null;
        r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but only with getter values.
     */
    public Value restrictToGetter() {
        checkNotPolymorphicOrUnknown();
        if (getters == null)
            return theNone;
        Value r = new Value();
        r.getters = getters;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but only with setter values.
     */
    public Value restrictToSetter() {
        checkNotPolymorphicOrUnknown();
        if (setters == null)
            return theNone;
        Value r = new Value();
        r.setters = setters;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but definitely not a getter or setter.
     */
    public Value restrictToNotGetterSetter() {
        checkNotUnknown();
        if (getters == null && setters == null)
            return this;
        Value r = new Value(this);
        r.getters = r.setters = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but definitely not a getter.
     */
    public Value restrictToNotGetter() {
        checkNotUnknown();
        if (getters == null)
            return this;
        Value r = new Value(this);
        r.getters = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but definitely not a setter.
     */
    public Value restrictToNotSetter() {
        checkNotUnknown();
        if (setters == null)
            return this;
        Value r = new Value(this);
        r.setters = null;
        return canonicalize(r);
    }

    /**
     * Returns true if this value belongs to a maybe absent property.
     */
    public boolean isMaybeAbsent() {
        checkNotUnknown();
        return (flags & ABSENT) != 0;
    }

    /**
     * Returns true if this value belongs to a definitely present property.
     */
    public boolean isNotAbsent() {
        return !isMaybeAbsent() && isMaybePresent();
    }

    /**
     * Returns true if this value is 'unknown'.
     */
    public boolean isUnknown() {
        return (flags & UNKNOWN) != 0;
    }

    /**
     * Constructs a value as a copy of this value but marked as maybe absent.
     */
    public Value joinAbsent() {
        checkNotUnknown();
        if (isMaybeAbsent())
            return this;
        Value r = new Value(this);
        r.flags |= ABSENT;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but marked as maybe absent and maybe modified.
     */
    public Value joinAbsentModified() {
        checkNotUnknown();
        if (isMaybeAbsent() && isMaybeModified())
            return this;
        Value r = new Value(this);
        r.flags |= ABSENT | MODIFIED;
        return canonicalize(r);
    }

    private static Value reallyMakeAbsent() {
        Value r = new Value();
        r.flags |= ABSENT;
        return canonicalize(r);
    }

    private static Value reallyMakeAbsentModified() {
        Value r = new Value();
        r.flags |= ABSENT | MODIFIED;
        return canonicalize(r);
    }

    private static Value reallyMakeUnknown() {
        Value r = new Value();
        r.flags |= UNKNOWN;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of the given value but with all attributes definitely not set.
     */
    public Value removeAttributes() {
        checkNotUnknown();
        Value r = new Value(this);
        r.flags &= ~ATTR;
        r.flags |= ATTR_NOTDONTDELETE | ATTR_NOTDONTENUM | ATTR_NOTREADONLY;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with attributes set as in the given value.
     */
    public Value setAttributes(Value from) {
        checkNotUnknown();
        from.checkNotUnknown();
        Value r = new Value(this);
        r.flags &= ~ATTR;
        r.flags |= from.flags & ATTR;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with no information that only makes sense for object property values.
     */
    public Value setBottomPropertyData() {
        checkNotUnknown();
        Value r = new Value(this);
        r.flags &= ~PROPERTYDATA;
        return canonicalize(r);
    }

    /**
     * Returns true is this value belongs to a property which definitely has DontEnum set.
     */
    public boolean isDontEnum() {
        checkNotUnknown();
        return (flags & ATTR_DONTENUM_ANY) == ATTR_DONTENUM;
    }

    /**
     * Returns true is this value belongs to a property which maybe has DontEnum set.
     */
    public boolean isMaybeDontEnum() {
        checkNotUnknown();
        return (flags & ATTR_DONTENUM) != 0;
    }

    /**
     * Returns true is this value belongs to a property which definitely does not have DontEnum set.
     */
    public boolean isNotDontEnum() {
        checkNotUnknown();
        return (flags & ATTR_DONTENUM_ANY) == ATTR_NOTDONTENUM;
    }

    /**
     * Returns true is this value belongs to a property which maybe does not have DontEnum set.
     */
    public boolean isMaybeNotDontEnum() {
        checkNotUnknown();
        return (flags & ATTR_NOTDONTENUM) != 0;
    }

    /**
     * Returns true if this value has DontEnum information.
     */
    public boolean hasDontEnum() {
        checkNotUnknown();
        return (flags & ATTR_DONTENUM_ANY) != 0;
    }

    /**
     * Constructs a value as a copy of this value but with DontEnum definitely set.
     */
    public Value setDontEnum() {
        checkNotUnknown();
        if (isDontEnum())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_DONTENUM_ANY;
        r.flags |= ATTR_DONTENUM;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with DontEnum definitely not set.
     */
    public Value setNotDontEnum() {
        checkNotUnknown();
        if (isNotDontEnum())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_DONTENUM_ANY;
        r.flags |= ATTR_NOTDONTENUM;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with DontEnum maybe not set.
     */
    public Value joinNotDontEnum() {
        checkNotUnknown();
        if (isMaybeNotDontEnum())
            return this;
        Value r = new Value(this);
        r.flags |= ATTR_NOTDONTENUM;
        return canonicalize(r);
    }

    /**
     * Returns true is this value belongs to a property which definitely has DontDelete set.
     */
    public boolean isDontDelete() {
        checkNotUnknown();
        return (flags & ATTR_DONTDELETE_ANY) == ATTR_DONTDELETE;
    }

    /**
     * Returns true is this value belongs to a property which maybe has DontDelete set.
     */
    public boolean isMaybeDontDelete() {
        checkNotUnknown();
        return (flags & ATTR_DONTDELETE) != 0;
    }

    /**
     * Returns true is this value belongs to a property which definitely does not have DontDelete set.
     */
    public boolean isNotDontDelete() {
        checkNotUnknown();
        return (flags & ATTR_DONTDELETE_ANY) == ATTR_NOTDONTDELETE;
    }

    /**
     * Returns true is this value belongs to a property which maybe does not have DontDelete set.
     */
    public boolean isMaybeNotDontDelete() {
        checkNotUnknown();
        return (flags & ATTR_NOTDONTDELETE) != 0;
    }

    /**
     * Returns true if this value has DontDelete information.
     */
    public boolean hasDontDelete() {
        checkNotUnknown();
        return (flags & ATTR_DONTDELETE_ANY) != 0;
    }

    /**
     * Constructs a value as a copy of this value but with DontDelete definitely set.
     */
    public Value setDontDelete() {
        checkNotUnknown();
        if (isDontDelete())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_DONTDELETE_ANY;
        r.flags |= ATTR_DONTDELETE;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with DontDelete definitely not set.
     */
    public Value setNotDontDelete() {
        checkNotUnknown();
        if (isNotDontDelete())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_DONTDELETE_ANY;
        r.flags |= ATTR_NOTDONTDELETE;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with DontDelete maybe not set.
     */
    public Value joinNotDontDelete() {
        checkNotUnknown();
        if (isMaybeNotDontDelete())
            return this;
        Value r = new Value(this);
        r.flags |= ATTR_NOTDONTDELETE;
        return canonicalize(r);
    }

    /**
     * Returns true is this value belongs to a property which definitely has ReadOnly set.
     */
    public boolean isReadOnly() {
        checkNotUnknown();
        return (flags & ATTR_READONLY_ANY) == ATTR_READONLY;
    }

    /**
     * Returns true is this value belongs to a property which maybe has ReadOnly set.
     */
    public boolean isMaybeReadOnly() {
        checkNotUnknown();
        return (flags & ATTR_READONLY) != 0;
    }

    /**
     * Returns true is this value belongs to a property which definitely does not have ReadOnly set.
     */
    public boolean isNotReadOnly() {
        checkNotUnknown();
        return (flags & ATTR_READONLY_ANY) == ATTR_NOTREADONLY;
    }

    /**
     * Returns true is this value belongs to a property which maybe does not have ReadOnly set.
     */
    public boolean isMaybeNotReadOnly() {
        checkNotUnknown();
        return (flags & ATTR_NOTREADONLY) != 0;
    }

    /**
     * Returns true if this value has ReadOnly information.
     */
    public boolean hasReadOnly() {
        checkNotUnknown();
        return (flags & ATTR_READONLY_ANY) != 0;
    }

    /**
     * Constructs a value as a copy of this value but with ReadOnly definitely set.
     */
    public Value setReadOnly() {
        checkNotUnknown();
        if (isReadOnly())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_READONLY_ANY;
        r.flags |= ATTR_READONLY;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with ReadOnly definitely not set.
     */
    public Value setNotReadOnly() {
        checkNotUnknown();
        if (isNotReadOnly())
            return this;
        Value r = new Value(this);
        r.flags &= ~ATTR_READONLY_ANY;
        r.flags |= ATTR_NOTREADONLY;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with ReadOnly maybe not set.
     */
    public Value joinNotReadOnly() {
        checkNotUnknown();
        if (isMaybeNotReadOnly())
            return this;
        Value r = new Value(this);
        r.flags |= ATTR_NOTREADONLY;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with the given attributes.
     */
    public Value setAttributes(boolean dontenum, boolean dontdelete, boolean readonly) {
        checkNotUnknown();
        Value r = new Value(this);
        r.flags &= ~ATTR;
        if (dontdelete)
            r.flags |= ATTR_DONTDELETE;
        else
            r.flags |= ATTR_NOTDONTDELETE;
        if (readonly)
            r.flags |= ATTR_READONLY;
        else
            r.flags |= ATTR_NOTREADONLY;
        if (dontenum)
            r.flags |= ATTR_DONTENUM;
        else
            r.flags |= ATTR_NOTDONTENUM;
        return canonicalize(r);
    }

    /**
     * Constructs a value as the join of this value and the given value.
     */
    public Value join(Value v) {
        if (v == this)
            return this;
        Value r = new Value(this);
        if (r.joinMutable(v)) {
            return canonicalize(r);
        }
        return this;
    }

    /**
     * Constructs a value as the join of the given collection of values.
     */
    public static Value join(Collection<Value> values) {
        if (values.size() == 1)
            return values.iterator().next();
        Value r = null;
        boolean first = true;
        for (Value v : values)
            if (first) {
                r = new Value(v);
                first = false;
            } else
                r.joinMutable(v);
        if (r == null)
            r = makeNone();
        return canonicalize(r);
    }

    /**
     * Constructs a value as the join of the given collection of values.
     */
    public static Value join(Value... values) {
        return join(Arrays.asList(values));
    }

    /**
     * Joins the given value into this one.
     */
    private boolean joinMutable(Value v) {
        if (v.isUnknown())
            return false;
        if (isPolymorphic() && v.isPolymorphic() && !var.equals(v.var))
            throw new AnalysisException("Attempt to join polymorphic values of different name!");
        if (isUnknown() || (isPolymorphic() && !v.isPolymorphic())) {
            flags = v.flags;
            num = v.num;
            str = v.str;
            object_labels = v.object_labels;
            getters = v.getters;
            setters = v.setters;
            var = v.var;
            return true;
        }
        boolean modified = false;
        int oldflags = flags;
        if (!v.isPolymorphic()) {
            // numbers
            if (num != null)
                if (v.num != null) {
                    // both this and v are single numbers
                    if (!num.equals(v.num)) {
                        // both this and v are single numbers, and the numbers are different
                        joinSingleNumberAsFuzzy(num);
                        joinSingleNumberAsFuzzy(v.num);
                        num = null;
                        modified = true;
                    } // otherwise this and v are equal single numbers, so do nothing
                } else {
                    // this is a single number, v is not a single number
                    if ((v.flags & NUM) != 0) {
                        // this is a single number, v is fuzzy
                        joinSingleNumberAsFuzzy(num);
                        num = null;
                        modified = true;
                    } // otherwise v is empty. so do nothing
                }
            else if (v.num != null) {
                // this is not a single number, v is a single number
                if ((flags & NUM) != 0) {
                    // this is a fuzzy number, v is a single number
                    joinSingleNumberAsFuzzy(v.num);
                } else {
                    // this is empty, v is a single number
                    num = v.num;
                    modified = true;
                }
            } // otherwise, neither is a single number, so do nothing
            // strings
            modified |= joinSingleStringOrPrefixString(v);
            // objects
            if (v.object_labels != null) {
                if (object_labels == null) {
                    modified = true;
                    object_labels = v.object_labels;
                } else if (!object_labels.containsAll(v.object_labels)) {
                    modified = true;
                    object_labels = newSet(object_labels);
                    object_labels.addAll(v.object_labels);
                }
            }
            if (v.getters != null) {
                if (getters == null) {
                    modified = true;
                    getters = v.getters;
                } else if (!getters.containsAll(v.getters)) {
                    modified = true;
                    getters = newSet(getters);
                    getters.addAll(v.getters);
                }
            }
            if (v.setters != null) {
                if (setters == null) {
                    modified = true;
                    setters = v.setters;
                } else if (!setters.containsAll(v.setters)) {
                    modified = true;
                    setters = newSet(setters);
                    setters.addAll(v.setters);
                }
            }
        }
        // flags
        flags |= v.flags & ~STR_PREFIX; // STR_PREFIX is handled above by joinSingleStringOrPrefixString
        if (var == null)
            flags &= ~(PRESENT_DATA | PRESENT_ACCESSOR);
        if ((flags & (STR_OTHERIDENTIFIERPARTS | STR_IDENTIFIER)) != 0)
            flags &= ~STR_PREFIX;
        if (flags != oldflags)
            modified = true;
        return modified;
    }

    /**
     * Checks whether the given object is equal to this one.
     */
    @Override
    public boolean equals(Object obj) {
        if (!canonicalizing) // use object identity as equality, except during canonicalization
            return obj == this;
        if (obj == this)
            return true;
        if (!(obj instanceof Value))
            return false;
        Value v = (Value) obj;
        //noinspection StringEquality,NumberEquality
        return flags == v.flags
                && (var == v.var || (var != null && v.var != null && var.equals(v.var)))
                && (num == v.num || (num != null && v.num != null && num.equals(v.num)))
                && (str == v.str || (str != null && v.str != null && str.equals(v.str)))
                && (object_labels == v.object_labels || (object_labels != null && v.object_labels != null && object_labels.equals(v.object_labels)))
                && (getters == v.getters || (getters != null && v.getters != null && getters.equals(v.getters)))
                && (setters == v.setters || (setters != null && v.setters != null && setters.equals(v.setters)));
    }

    /**
     * Returns a description of the changes from the old value to this value.
     * It is assumed that the old value is less than this value.
     *
     * @param old The old value to diff against.
     * @param b   The string builder to print the diff to.
     */
    public void diff(Value old, StringBuilder b) {
        Value v = new Value(this);
        v.flags &= ~old.flags; // TODO: see Value.remove above (anyway, diff is only used for debug output)
        if (v.object_labels != null && old.object_labels != null) {
            v.object_labels = newSet(v.object_labels);
            v.object_labels.removeAll(old.object_labels);
        }
        if (v.getters != null && old.getters != null) {
            v.getters = newSet(v.getters);
            v.getters.removeAll(old.getters);
        }
        if (v.setters != null && old.setters != null) {
            v.setters = newSet(v.setters);
            v.setters.removeAll(old.setters);
        }
        b.append(v);
    }

    /**
     * Returns the hash code for this value.
     */
    @Override
    public int hashCode() {
        return hashcode;
    }

    /**
     * Returns the source locations of the objects and symbols in this value.
     */
    public Set<SourceLocation> getObjectSourceLocations() {
        Set<SourceLocation> res = newSet();
        if (object_labels != null)
            for (ObjectLabel objlabel : object_labels)
                res.add(objlabel.getSourceLocation());
        if (getters != null)
            for (ObjectLabel objlabel : getters)
                res.add(objlabel.getSourceLocation());
        if (setters != null)
            for (ObjectLabel objlabel : setters)
                res.add(objlabel.getSourceLocation());
        return res;
    }

    /**
     * Produces a string description of this value.
     * Ignores attributes and modified flag.
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean any = false;
        if (isUnknown()) {
            b.append("?");
            any = true;
        } else if (isPolymorphic()) {
            b.append("^(").append(var).append('[');
            if (isMaybeAbsent()) {
                b.append("absent");
                any = true;
            }
            if (isMaybePresent()) {
                if (any)
                    b.append('|');
                b.append("present");
            }
            b.append("])");
//            if (var_summarized != null)
//            b.append('<').append(var_summarized).append('>');
            any = true;
        } else {
            if (isMaybeUndef()) {
                b.append("Undef");
                any = true;
            }
            if (isMaybeNull()) {
                if (any)
                    b.append('|');
                b.append("Null");
                any = true;
            }
            if (isMaybeAnyBool()) {
                if (any)
                    b.append('|');
                b.append("Bool");
                any = true;
            } else if (isMaybeTrueButNotFalse()) {
                if (any)
                    b.append('|');
                b.append("true");
                any = true;
            } else if (isMaybeFalseButNotTrue()) {
                if (any)
                    b.append('|');
                b.append("false");
                any = true;
            }
            if (isMaybeAnyNum()) {
                if (any)
                    b.append('|');
                b.append("Num");
                any = true;
            } else {
                if (num == null && isMaybeZero() && !isMaybeNumUIntPos()) {
                    if (any)
                        b.append('|');
                    b.append("Zero");
                    any = true;
                } else if (!isMaybeZero() && isMaybeNumUIntPos()) {
                    if (any)
                        b.append('|');
                    b.append("UIntPos");
                    any = true;
                } else if (isMaybeNumUInt()) {
                    if (any)
                        b.append('|');
                    b.append("UInt");
                    any = true;
                }
                if (isMaybeNumOther()) {
                    if (any)
                        b.append('|');
                    b.append("NotUInt");
                    any = true;
                }
                if (isMaybeNaN()) {
                    if (any)
                        b.append('|');
                    b.append("NaN");
                    any = true;
                }
                if (isMaybeInf()) {
                    if (any)
                        b.append('|');
                    b.append("Inf");
                    any = true;
                }
                if (num != null) {
                    if (any)
                        b.append('|');
                    b.append(num);
                    any = true;
                }
            }
            if (isMaybeAnyStr()) {
                if (any)
                    b.append('|');
                b.append("Str");
                any = true;
            } else {
                if (isMaybeStrUInt()) {
                    if (any)
                        b.append('|');
                    b.append("UIntStr");
                    any = true;
                }
                if (isMaybeStrOtherNum()) {
                    if (any)
                        b.append('|');
                    b.append("NotUIntStr"); // TODO: change to OtherNumStr?
                    any = true;
                }
                if (isMaybeStrIdentifier()) {
                    if (any)
                        b.append('|');
                    b.append("IdentStr");
                    any = true;
                }
                if (isMaybeStrOtherIdentifierParts()) {
                    if (any)
                        b.append('|');
                    b.append("OtherIdentPartsStr");
                    any = true;
                }
                if (isMaybeStrOther()) {
                    if (any)
                        b.append('|');
                    b.append("OtherStr");
                    any = true;
                }
                if (isStrJSON()) {
                    if (any)
                        b.append("|");
                    b.append("JSONStr");
                    any = true;
                }
                if (isMaybeStrPrefix()) {
                    if (any)
                        b.append('|');
                    b.append("PrefixStr[").append(Strings.escape(str)).append(']');
                    any = true;
                } else if (str != null) {
                    if (any)
                        b.append('|');
                    b.append('"').append(Strings.escape(str)).append('"');
                    any = true;
                }
            }
            if (object_labels != null) {
                if (any)
                    b.append('|');
                b.append(object_labels);
                any = true;
            }
            if (getters != null) {
                if (any)
                    b.append('|');
                b.append("getter ").append(getters);
                any = true;
            }
            if (setters != null) {
                if (any)
                    b.append('|');
                b.append("setter ").append(setters);
                any = true;
            }
            if (isMaybeAbsent()) {
                if (any)
                    b.append('|');
                b.append("absent");
                any = true;
            }
        }
        if (!any)
            b.append("<no value>");
//         if (isMaybeModified())
//         b.append("%");
        return b.toString();
    }

    public String printFlags() {
        StringBuilder b = new StringBuilder();
        boolean any = false;
        if ((flags & BOOL_TRUE) != 0) {
//            if (any)
//                b.append("|");
            b.append("BOOL_TRUE");
            any = true;
        }
        if ((flags & BOOL_FALSE) != 0) {
            if (any)
                b.append("|");
            b.append("BOOL_FALSE");
            any = true;
        }
        if ((flags & UNDEF) != 0) {
            if (any)
                b.append("|");
            b.append("UNDEF");
            any = true;
        }
        if ((flags & NULL) != 0) {
            if (any)
                b.append("|");
            b.append("NULL");
            any = true;
        }
        if ((flags & STR_UINT) != 0) {
            if (any)
                b.append("|");
            b.append("STR_UINT");
            any = true;
        }
        if ((flags & STR_OTHERNUM) != 0) {
            if (any)
                b.append("|");
            b.append("STR_OTHERNUM");
            any = true;
        }
        if ((flags & STR_PREFIX) != 0) {
            if (any)
                b.append("|");
            b.append("STR_PREFIX");
            any = true;
        }
        if ((flags & STR_OTHERIDENTIFIERPARTS) != 0) {
            if (any)
                b.append("|");
            b.append("STR_IDENTIFIERPARTS"); // TODO: change to STR_OTHERIDENTIFIERPARTS and remove "else" below?
            any = true;
        } else if ((flags & STR_IDENTIFIER) != 0) {
            if (any)
                b.append("|");
            b.append("STR_IDENTIFIER");
            any = true;
        }
        if ((flags & STR_OTHER) != 0) {
            if (any)
                b.append("|");
            b.append("STR_OTHER");
            any = true;
        }
        if ((flags & STR_JSON) != 0) {
            if (any)
                b.append("|");
            b.append("STR_JSON");
            any = true;
        }
        if ((flags & NUM_NAN) != 0) {
            if (any)
                b.append("|");
            b.append("NUM_NAN");
            any = true;
        }
        if ((flags & NUM_INF) != 0) {
            if (any)
                b.append("|");
            b.append("NUM_INF");
            any = true;
        }
        if ((flags & NUM_UINT) != 0) {
            if (any)
                b.append("|");
            b.append("NUM_UINT");
            any = true;
        }
        if ((flags & NUM_OTHER) != 0) {
            if (any)
                b.append("|");
            b.append("NUM_OTHER");
            any = true;
        }
        if ((flags & ATTR_DONTENUM) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_DONTENUM");
            any = true;
        }
        if ((flags & ATTR_NOTDONTENUM) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_NOTDONTENUM");
            any = true;
        }
        if ((flags & ATTR_READONLY) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_READONLY");
            any = true;
        }
        if ((flags & ATTR_NOTREADONLY) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_NOTREADONLY");
            any = true;
        }
        if ((flags & ATTR_DONTDELETE) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_DONTDELETE");
            any = true;
        }
        if ((flags & ATTR_NOTDONTDELETE) != 0) {
            if (any)
                b.append("|");
            b.append("ATTR_NOTDONTDELETE");
            any = true;
        }
        if ((flags & MODIFIED) != 0) {
            if (any)
                b.append("|");
            b.append("MODIFIED");
            any = true;
        }
        if ((flags & ABSENT) != 0) {
            if (any)
                b.append("|");
            b.append("ABSENT");
            any = true;
        }
        if ((flags & PRESENT_DATA) != 0) {
            if (any)
                b.append("|");
            b.append("PRESENT_DATA");
            any = true;
        }
        if ((flags & PRESENT_ACCESSOR) != 0) {
            if (any)
                b.append("|");
            b.append("PRESENT_ACCESSOR");
            any = true;
        }
        if ((flags & UNKNOWN) != 0) {
            if (any)
                b.append("|");
            b.append("UNKNOWN");
            any = true;
        }
        if ((flags & EXTENDEDSCOPE) != 0) {
            if (any)
                b.append("|");
            b.append("EXTENDEDSCOPE");
//            any = true;
        }
        return b.toString();
    }

    /**
     * Produces a string description of the attributes of this value.
     */
    public String printAttributes() {
        checkNotUnknown();
        StringBuilder b = new StringBuilder();
        if (hasDontDelete()) {
            b.append("(DontDelete");
            if (isMaybeDontDelete())
                b.append("+");
            if (isMaybeNotDontDelete())
                b.append("-");
            b.append(")");
        }
        if (hasDontEnum()) {
            b.append("(DontEnum");
            if (isMaybeDontEnum())
                b.append("+");
            if (isMaybeNotDontEnum())
                b.append("-");
            b.append(")");
        }
        if (hasReadOnly()) {
            b.append("(ReadOnly");
            if (isMaybeReadOnly())
                b.append("+");
            if (isMaybeNotReadOnly())
                b.append("-");
            b.append(")");
        }
        return b.toString();
    }

    /* the Undef facet */

    @Override
    public boolean isMaybeUndef() {
        checkNotPolymorphicOrUnknown();
        return (flags & UNDEF) != 0;
    }

    @Override
    public boolean isNotUndef() {
        checkNotPolymorphicOrUnknown();
        return (flags & UNDEF) == 0;
    }

    @Override
    public boolean isMaybeOtherThanUndef() {
        checkNotPolymorphicOrUnknown();
        return (flags & (NULL | BOOL | NUM | STR)) != 0 || num != null || str != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public Value joinUndef() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeUndef())
            return this;
        else
            return reallyMakeUndef(this);
    }

    @Override
    public Value restrictToNotUndef() {
        checkNotPolymorphicOrUnknown();
        if (isNotUndef())
            return this;
        Value r = new Value(this);
        r.flags &= ~UNDEF;
        return canonicalize(r);
    }

    @Override
    public Value restrictToUndef() {
        checkNotPolymorphicOrUnknown();
        if (isNotUndef())
            return theNone;
        return theUndef;
    }

    private static Value reallyMakeUndef(Value v) {
        Value r = (v == null) ? new Value() : new Value(v);
        r.flags |= UNDEF;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing definitely undefined.
     */
    public static Value makeUndef() {
        return theUndef;
    }

    /* The Null facet */

    @Override
    public boolean isMaybeNull() {
        checkNotPolymorphicOrUnknown();
        return (flags & NULL) != 0;
    }

    @Override
    public boolean isNotNull() {
        checkNotPolymorphicOrUnknown();
        return (flags & NULL) == 0;
    }

    @Override
    public boolean isMaybeOtherThanNull() {
        checkNotPolymorphicOrUnknown();
        return (flags & (UNDEF | BOOL | NUM | STR)) != 0 || num != null || str != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public Value joinNull() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeNull())
            return this;
        else
            return reallyMakeNull(this);
    }

    @Override
    public Value restrictToNotNull() {
        checkNotPolymorphicOrUnknown();
        if (isNotNull())
            return this;
        Value r = new Value(this);
        r.flags &= ~NULL;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNull() {
        checkNotPolymorphicOrUnknown();
        if (isNotNull())
            return theNone;
        return theNull;
    }

    /**
     * Returns true if this value is definitely null or undefined.
     */
    public boolean isNullOrUndef() {
        checkNotPolymorphicOrUnknown();
        return (flags & (NULL | UNDEF)) != 0
                && (flags & (NUM | STR | BOOL)) == 0 && num == null && str == null && object_labels == null && getters == null && setters == null;
    }

    /**
     * Constructs a value as a copy of this value but definitely not null nor undefined.
     */
    public Value restrictToNotNullNotUndef() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeNull() && !isMaybeUndef())
            return this;
        Value r = new Value(this);
        r.flags &= ~(NULL | UNDEF);
        return canonicalize(r);
    }

    private static Value reallyMakeNull(Value v) {
        Value r = (v == null) ? new Value() : new Value(v);
        r.flags |= NULL;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing definitely null.
     */
    public static Value makeNull() {
        return theNull;
    }

    /* The Bool facet */

    @Override
    public boolean isMaybeAnyBool() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL) == BOOL;
    }

    @Override
    public boolean isMaybeTrueButNotFalse() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL) == BOOL_TRUE;
    }

    @Override
    public boolean isMaybeFalseButNotTrue() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL) == BOOL_FALSE;
    }

    @Override
    public boolean isMaybeTrue() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL_TRUE) == BOOL_TRUE;
    }

    @Override
    public boolean isMaybeFalse() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL_FALSE) == BOOL_FALSE;
    }

    @Override
    public boolean isNotBool() {
        checkNotPolymorphicOrUnknown();
        return (flags & BOOL) == 0;
    }

    @Override
    public boolean isMaybeOtherThanBool() {
        checkNotPolymorphicOrUnknown();
        return (flags & (UNDEF | NULL | NUM | STR)) != 0 || num != null || str != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public Value joinAnyBool() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeAnyBool())
            return this;
        Value r = new Value(this);
        r.flags |= BOOL;
        return canonicalize(r);
    }

    @Override
    public Value joinBool(boolean x) {
        checkNotPolymorphicOrUnknown();
        if (isMaybeAnyBool() || (x ? isMaybeTrueButNotFalse() : isMaybeFalseButNotTrue()))
            return this;
        Value r = new Value(this);
        r.flags |= x ? BOOL_TRUE : BOOL_FALSE;
        return canonicalize(r);
    }

    @Override
    public Value joinBool(Value x) {
        checkNotPolymorphicOrUnknown();
        x.checkNotPolymorphicOrUnknown();
        if (isMaybeAnyBool() || x.isMaybeAnyBool() || (isMaybeTrue() && x.isMaybeFalse()) || (isMaybeFalse() && x.isMaybeTrue()))
            return theBoolAny;
        if (isNotBool())
            return x;
        else
            return this;
    }

    private static Value reallyMakeBool(Boolean b) {
        Value r = new Value();
        if (b == null)
            r.flags |= BOOL;
        else if (b)
            r.flags |= BOOL_TRUE;
        else
            r.flags |= BOOL_FALSE;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotBool() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~BOOL;
        return canonicalize(r);
    }

    /**
     * Constructs the value representing any boolean.
     */
    public static Value makeAnyBool() {
        return theBoolAny;
    }

    /**
     * Constructs the value describing the given boolean.
     */
    public static Value makeBool(boolean b) {
        if (b)
            return theBoolTrue;
        else
            return theBoolFalse;
    }

    /**
     * Constructs the value describing the given Bool.
     */
    public static Value makeBool(Bool b) {
        if (b.isMaybeAnyBool())
            return theBoolAny;
        else if (b.isMaybeTrueButNotFalse())
            return theBoolTrue;
        else if (b.isMaybeFalseButNotTrue())
            return theBoolFalse;
        else
            return theNone;
    }

    /**
     * Constructs a value from this value where only the boolean facet is considered.
     */
    public Value restrictToBool() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeAnyBool())
            return theBoolAny;
        else if (isMaybeTrueButNotFalse())
            return theBoolTrue;
        else if (isMaybeFalseButNotTrue())
            return theBoolFalse;
        else
            return theNone;
    }

    /**
     * Constructs a value as a copy of this value but definitely not falsy.
     */
    public Value restrictToTruthy() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        if ((r.flags & STR_PREFIX) == 0 && r.str != null && r.str.isEmpty())
            r.str = null;
        if (r.num != null && Math.abs(r.num) == 0.0)
            r.num = null;
        r.flags &= ~(BOOL_FALSE | NULL | UNDEF | NUM_NAN | NUM_ZERO);
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but definitely not truthy.
     */
    public Value restrictToFalsy() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        if ((r.flags & STR_PREFIX) != 0 || (r.str != null && !r.str.isEmpty()))
            r.str = null;
        if (r.num != null && Math.abs(r.num) != 0.0)
            r.num = null;
        r.object_labels = r.getters = r.setters = null;
        r.flags &= ~(BOOL_TRUE | STR_PREFIX | (NUM & ~(NUM_ZERO | NUM_NAN)));
        return canonicalize(r);
    }

    /**
     * Constructs a value from this value where only the string/boolean/number facets are considered.
     */
    public Value restrictToStrBoolNum() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.object_labels = r.getters = r.setters = null;
        r.flags &= STR | BOOL | NUM;
        return canonicalize(r);
    }

    /* the Num facet */

    @Override
    public boolean isMaybeAnyNum() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) == NUM;
    }

    @Override
    public boolean isMaybeAnyNumNotNaNInf() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) == (NUM & ~NUM_INF & ~NUM_NAN);
    }

    @Override
    public boolean isMaybeSingleNum() {
        checkNotPolymorphicOrUnknown();
        return num != null;
    }

    @Override
    public boolean isMaybeSingleNumUInt() {
        checkNotPolymorphicOrUnknown();
        return num != null && isUInt32(num);
    }

    @Override
    public boolean isMaybeFuzzyNum() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) != 0;
    }

    @Override
    public boolean isMaybeNaN() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM_NAN) != 0;
    }

    @Override
    public boolean isNaN() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) == NUM_NAN && !isMaybeOtherThanNum();
    }

    @Override
    public boolean isMaybeInf() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM_INF) != 0;
    }

    @Override
    public boolean isInf() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) == NUM_INF && !isMaybeOtherThanNum();
    }

    @Override
    public boolean isMaybeNum(double num) {
        checkNotPolymorphicOrUnknown();
        if (this.num != null) {
            return this.num == num;
        } else if (Double.isInfinite(num)) {
            return (flags & NUM_INF) != 0;
        } else if (Double.isNaN(num)) {
            return (flags & NUM_NAN) != 0;
        } else if (isZero(num)) {
            return (flags & NUM_ZERO) != 0;
        } else if (isUInt32(num)) { // not zero due to the zero-check above
            return (flags & NUM_UINT_POS) != 0;
        } else {
            return (flags & NUM_OTHER) != 0;
        }
    }

    private boolean isZero(double num) {
        return num == 0;
    }

    @Override
    public boolean isMaybeNumUInt() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM_UINT) != 0;
    }

    @Override
    public boolean isMaybeNumOther() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM_OTHER) != 0;
    }

    @Override
    public boolean isMaybeOtherThanNum() {
        checkNotPolymorphicOrUnknown();
        return ((flags & (UNDEF | NULL | BOOL | STR)) != 0) || str != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public boolean isMaybeOtherThanNumUInt() {
        checkNotPolymorphicOrUnknown();
        return ((flags & (UNDEF | NULL | BOOL | STR | NUM_INF | NUM_NAN | NUM_OTHER)) != 0) || str != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public Double getNum() {
        checkNotPolymorphicOrUnknown();
        return num != null ? num : (flags & NUM_NAN) != 0 ? Double.NaN : null;
    }

    @Override
    public boolean isNotNum() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM) == 0 && num == null;
    }

    @Override
    public Value joinAnyNum() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeAnyNum())
            return this;
        Value r = new Value(this);
        r.num = null;
        r.flags |= NUM;
        return canonicalize(r);
    }

    @Override
    public Value joinAnyNumUInt() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeNumUIntPos() && isMaybeZero())
            return this;
        Value r = new Value(this);
        r.flags |= NUM_UINT;
        r.num = null;
        if (num != null)
            r.joinSingleNumberAsFuzzy(num);
        return canonicalize(r);
    }

    @Override
    public Value joinAnyNumOther() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeNumOther())
            return this;
        Value r = new Value(this);
        r.flags |= NUM_OTHER;
        r.num = null;
        if (num != null)
            r.joinSingleNumberAsFuzzy(num);
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotNaN() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeNaN())
            return this;
        Value r = new Value(this);
        r.flags &= ~NUM_NAN;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotInf() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeInf())
            return this;
        Value r = new Value(this);
        r.flags &= ~NUM_INF;
        return canonicalize(r);
    }

    /**
     * Checks whether the given number is a UInt32.
     */
    public static boolean isUInt32(double v) {
        return !Double.isNaN(v) && !Double.isInfinite(v) && v >= 0 && v <= Integer.MAX_VALUE * 2.0 + 1 && (v % 1) == 0;
    }

    /**
     * Joins the given single number as a fuzzy value.
     */
    private void joinSingleNumberAsFuzzy(double v) {
        if (Double.isNaN(v))
            flags |= NUM_NAN;
        else if (Double.isInfinite(v))
            flags |= NUM_INF;
        else if (isZero(v))
            flags |= NUM_ZERO;
        else if (isUInt32(v))
            flags |= NUM_UINT_POS; // not zero due to the zero-check above
        else
            flags |= NUM_OTHER;
    }

    @Override
    public Value joinNum(double v) {
        checkNotPolymorphicOrUnknown();
        if (Double.isNaN(v))
            return joinNumNaN();
        if (num != null && num == v)
            return this;
        Value r = new Value(this);
        if (isNotNum())
            r.num = v;
        else {
            if (num != null) {
                r.num = null;
                r.joinSingleNumberAsFuzzy(num);
            }
            r.joinSingleNumberAsFuzzy(v);
        }
        return canonicalize(r);
    }

    @Override
    public Value joinNumNaN() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeNaN())
            return this;
        Value r = new Value(this);
        r.flags |= NUM_NAN;
        r.num = null;
        if (num != null)
            r.joinSingleNumberAsFuzzy(num);
        return canonicalize(r);
    }

    @Override
    public Value joinNumInf() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeInf())
            return this;
        Value r = new Value(this);
        r.flags |= NUM_INF;
        r.num = null;
        if (num != null)
            r.joinSingleNumberAsFuzzy(num);
        return canonicalize(r);
    }

    private static Value reallyMakeAnyUInt() {
        Value r = new Value();
        r.flags = NUM_UINT;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyUIntPos() {
        Value r = new Value();
        r.flags = NUM_UINT_POS;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyNumOther() {
        Value r = new Value();
        r.flags = NUM_OTHER;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyNumNotNaNInf() {
        Value r = new Value();
        r.flags = NUM_UINT | NUM_OTHER;
        return canonicalize(r);
    }

    private static Value reallyMakeNumNaN() {
        Value r = new Value();
        r.flags = NUM_NAN;
        return canonicalize(r);
    }

    private static Value reallyMakeNumInf() {
        Value r = new Value();
        r.flags = NUM_INF;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyNum() {
        Value r = new Value();
        r.flags = NUM;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing the given number.
     */
    public static Value makeNum(double d) {
        if (Double.isNaN(d))
            return theNumNaN;
        if (Double.isInfinite(d))
            return theNumInf;
        Value r = new Value();
        r.num = d;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing NaN.
     */
    public static Value makeNumNaN() {
        return theNumNaN;
    }

    /**
     * Constructs the value describing +/-Inf.
     */
    public static Value makeNumInf() {
        return theNumInf;
    }

    /**
     * Constructs the value describing any number.
     */
    public static Value makeAnyNum() {
        return theNumAny;
    }

    /**
     * Constructs the value describing any UInt number.
     */
    public static Value makeAnyNumUInt() {
        return theNumUInt;
    }

    /**
     * Constructs the value describing any positive UInt number.
     */
    public static Value makeAnyNumUIntPos() {
        return theNumUIntPos;
    }

    /**
     * Constructs the value describing any non-UInt, non-+/-Inf, non-NaN number.
     */
    public static Value makeAnyNumOther() {
        return theNumOther;
    }

    /**
     * Constructs the value describing number except NaN and infinity.
     */
    public static Value makeAnyNumNotNaNInf() {
        return theNumNotNaNInf;
    }

    @Override
    public Value restrictToNum() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value();
        r.flags = flags & NUM;
        r.num = num;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotNum() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~NUM;
        r.num = null;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotNumUInt() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~NUM_UINT;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotNumOther() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~NUM_OTHER;
        return canonicalize(r);
    }

    /* the Str facet */

    @Override
    public boolean isMaybeAnyStr() {
        checkNotPolymorphicOrUnknown();
        return (flags & (STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER)) == (STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER);
    }

    @Override
    public boolean isMaybeStrUInt() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_UINT) != 0;
    }

    @Override
    public boolean isMaybeStrSomeUInt() {
        checkNotPolymorphicOrUnknown();
        return isMaybeStrUInt() || (str != null && Strings.isArrayIndex(str));
    }

    @Override
    public boolean isMaybeStrSomeNonUInt() {
        checkNotPolymorphicOrUnknown();
        return (flags & (STR_OTHERNUM | STR_PREFIX | STR_IDENTIFIER | STR_OTHERIDENTIFIERPARTS | STR_OTHER | STR_JSON)) != 0
                || (str != null && !Strings.isArrayIndex(str));
    }

    @Override
    public boolean isMaybeStrOtherNum() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_OTHERNUM) != 0;
    }

    @Override
    public boolean isMaybeStrIdentifier() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_IDENTIFIER) != 0;
    }

    @Override
    public boolean isMaybeStrOtherIdentifierParts() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_OTHERIDENTIFIERPARTS) != 0;
    }

    @Override
    public boolean isMaybeStrPrefix() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_PREFIX) != 0;
    }

    @Override
    public boolean isMaybeStrOther() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR_OTHER) != 0;
    }

    @Override
    public boolean isMaybeStrJSON() { // FIXME: rethink STR_JSON... (github #374)
        checkNotPolymorphicOrUnknown();
        return (flags & STR_JSON) != 0;
    }

    @Override
    public boolean isStrJSON() {
        checkNotPolymorphicOrUnknown();
        return (flags & PRIMITIVE) == STR_JSON && str == null && num == null && object_labels == null && getters == null && setters == null;
    }

    @Override
    public boolean isStrIdentifierParts() {
        checkNotPolymorphicOrUnknown();
        return (((flags & STR_IDENTIFIERPARTS) != 0 && (flags & PRIMITIVE & ~STR_IDENTIFIERPARTS) == 0)
                || (str != null && Strings.isIdentifierParts(str))) && num == null && object_labels == null && getters == null && setters == null;
    }

    @Override
    public boolean isStrIdentifier() {
        checkNotPolymorphicOrUnknown();
        return ((flags & PRIMITIVE) == STR_IDENTIFIER
                || (str != null && Strings.isIdentifier(str))) && num == null && object_labels == null && getters == null && setters == null;
    }

    @Override
    public boolean isMaybeStrOnlyUInt() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR) == STR_UINT;
    }

    @Override
    public boolean isMaybeSingleStr() {
        checkNotPolymorphicOrUnknown();
        return str != null && !isMaybeStrPrefix();
    }

    @Override
    public boolean isMaybeSymbol() {
        checkNotPolymorphicOrUnknown();
        return object_labels != null && object_labels.stream().anyMatch(x -> x.getKind() == Kind.SYMBOL);
    }

    @Override
    public boolean isMaybeFuzzyStrOrSymbol() {
        checkNotPolymorphicOrUnknown();
        return (!isNotStr() && isMaybeSymbol()) ||
                isMaybeFuzzyStr() ||
                (object_labels != null && object_labels.stream().filter(x -> x.getKind() == Kind.SYMBOL).count() > 1) ||
                (object_labels != null && object_labels.stream().filter(x -> x.getKind() == Kind.SYMBOL && !x.isSingleton()).count() > 0);
    }

    public boolean isMaybeOtherThanSymbol() {
        checkNotPolymorphicOrUnknown();
        if (isMaybePrimitive() || isMaybeGetterOrSetter()) {
            return true;
        }
        return object_labels != null && object_labels.stream().anyMatch(x -> x.getKind() != Kind.SYMBOL);
    }

    public boolean isMaybeOtherThanStrOrSymbol() {
        checkNotPolymorphicOrUnknown();
        if ((flags & (UNDEF | NULL | BOOL | NUM)) != 0 || num != null || getters != null || setters != null) {
            return true;
        }
        return object_labels != null && object_labels.stream().anyMatch(x -> x.getKind() != Kind.SYMBOL);
    }

    /**
     * Constructs a value as a copy of this value but definitely not a symbol.
     */
    public Value restrictToNotSymbol() {
        if (object_labels == null)
            return this;
        Value r = new Value(this);
        r.object_labels = newSet();
        for (ObjectLabel objlabel : object_labels)
            if (objlabel.getKind() != Kind.SYMBOL)
                r.object_labels.add(objlabel);
        if (r.object_labels.isEmpty())
            r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but definitely a symbol.
     */
    public Value restrictToSymbol() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~PRIMITIVE;
        r.num = null;
        r.str = null;
        r.getters = r.setters = null;
        r.object_labels = newSet();
        if (object_labels != null)
            for (ObjectLabel objlabel : object_labels)
                if (objlabel.getKind() == Kind.SYMBOL)
                    r.object_labels.add(objlabel);
        if (r.object_labels.isEmpty())
            r.object_labels = null;
        return canonicalize(r);
    }

    @Override
    public boolean isMaybeFuzzyStr() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR) != 0;
    }

    @Override
    public String getStr() {
        checkNotPolymorphicOrUnknown();
        if (str == null || isMaybeStrPrefix())
            throw new AnalysisException("Invoked getStr on a non-single string value");
        return str;
    }

    @Override
    public String getPrefix() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeStrPrefix())
            throw new AnalysisException("Invoked getPrefix on a non-prefix string value");
        return str;
    }

    @Override
    public boolean isNotStr() {
        checkNotPolymorphicOrUnknown();
        return (flags & STR) == 0 && str == null;
    }

    @Override
    public boolean isMaybeOtherThanStr() {
        checkNotPolymorphicOrUnknown();
        return (flags & (UNDEF | NULL | BOOL | NUM)) != 0 || num != null || object_labels != null || getters != null || setters != null;
    }

    @Override
    public Value joinAnyStr() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeAnyStr())
            return this;
        Value r = new Value(this);
        r.flags |= STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        return canonicalize(r);
    }

    @Override
    public Value joinAnyStrUInt() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeStrUInt())
            return this;
        Value r = new Value(this);
        r.flags |= STR_UINT;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        r.joinSingleStringOrPrefixString(this);
        return canonicalize(r);
    }

    @Override
    public Value joinAnyStrOtherNum() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeStrOtherNum())
            return this;
        Value r = new Value(this);
        r.flags |= STR_OTHERNUM;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        r.joinSingleStringOrPrefixString(this);
        return canonicalize(r);
    }

    @Override
    public Value joinAnyStrIdentifier() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeStrIdentifier())
            return this;
        Value r = new Value(this);
        r.flags |= STR_IDENTIFIER;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        r.joinSingleStringOrPrefixString(this);
        return canonicalize(r);
    }

    @Override
    public Value joinAnyStrIdentifierParts() {
        checkNotPolymorphicOrUnknown();
        if ((flags & STR_IDENTIFIERPARTS) == STR_IDENTIFIERPARTS)
            return this;
        Value r = new Value(this);
        r.flags |= STR_IDENTIFIERPARTS;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        r.joinSingleStringOrPrefixString(this);
        return canonicalize(r);
    }

    @Override
    public Value joinAnyStrOther() {
        checkNotPolymorphicOrUnknown();
        if (isMaybeStrOther())
            return this;
        Value r = new Value(this);
        r.flags |= STR_OTHER;
        r.flags &= ~STR_PREFIX;
        r.str = null;
        r.joinSingleStringOrPrefixString(this);
        return canonicalize(r);
    }

    @Override
    public Value joinStr(String s) {
        checkNotPolymorphicOrUnknown();
        if (str != null && str.equals(s))
            return this;
        Value r = new Value(this);
        Value tmp = new Value();
        tmp.str = s;
        r.joinSingleStringOrPrefixString(tmp);
        return canonicalize(r);
    }

    @Override
    public Value joinPrefix(String s) {
        checkNotPolymorphicOrUnknown();
        if (isMaybeStrPrefix() && str.equals(s))
            return this;
        Value r = new Value(this);
        Value tmp = new Value();
        tmp.flags |= STR_PREFIX;
        tmp.str = s;
        r.joinSingleStringOrPrefixString(tmp);
        return canonicalize(r);
    }

    /**
     * Joins the given single/prefix string as a fuzzy non-prefix value.
     * The current value is assumed not to be a single or prefix string.
     *
     * @param s           the other single/prefix string
     * @param s_is_prefix if true, the other string represents a prefix string, otherwise it represents a single string
     * @return true if this value is modified
     */
    private boolean joinSingleStringOrPrefixStringAsFuzzyNonPrefix(String s, boolean s_is_prefix) {
        int oldflags = flags;
        if (s_is_prefix) {
            // no knowledge about the suffix of a prefix: set all str-bits
            flags |= STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER;
        } else {
            // s is a single string
            if (Strings.isArrayIndex(s)) {
                flags |= STR_UINT;
            } else if (Strings.isNumber(s)) {
                flags |= STR_OTHERNUM;
            } else if (Strings.isIdentifier(s)) {
                flags |= STR_IDENTIFIER;
            } else if (Strings.isOtherIdentifierParts(s)) {
                flags |= STR_OTHERIDENTIFIERPARTS;
            } else {
                flags |= STR_OTHER;
            }
        }
        return flags != oldflags;
    }

    /**
     * Joins the single string or prefix string part of the given value into this value.
     * No other parts of v are used.
     * Also converts the existing single string or prefix string to a fuzzy value if necessary.
     *
     * @return true if this value is modified
     */
    private boolean joinSingleStringOrPrefixString(Value v) { // TODO: could be more precise in some cases...
        // NB: the Value lattice is a lattice due to implementation details: join("xA", "xB") results in PREFIX("x"), but it could as well have resulted in IDENTSTR!
        boolean modified = false;
        boolean this_is_prefix = (flags & STR_PREFIX) != 0;
        boolean v_is_prefix = (v.flags & STR_PREFIX) != 0;
        boolean switch_both_to_fuzzy = false;
        if (str != null)
            if (v.str != null) {
                if (!this_is_prefix && !v_is_prefix && str.equals(v.str)) {
                    return false; // same single string
                }
                // both this and v are single/prefix strings
                String sharedPrefix = Strings.getSharedPrefix(str, v.str);
                if (sharedPrefix.isEmpty()) {
                    switch_both_to_fuzzy = true;
                } else {
                    flags |= STR_PREFIX;
                    modified |= !str.equals(v.str);
                    str = sharedPrefix;
                }
            } else {
                // this is a single/prefix string, v is not a single/prefix string
                if ((v.flags & STR) != 0) {
                    // this is a single/prefix string, v is non-prefix fuzzy, so switch this to fuzzy
                    String oldstr = str;
                    str = null;
                    flags &= ~STR_PREFIX;
                    joinSingleStringOrPrefixStringAsFuzzyNonPrefix(oldstr, this_is_prefix);
                    modified = true;
                } // otherwise v is empty. so do nothing
            }
        else if (v.str != null) {
            // this is not a single/prefix string, v is a single/prefix string
            if ((flags & STR) == 0) {
                // this value is empty, so copy from v.str
                str = v.str;
                flags |= v.flags & STR_PREFIX;
                modified = true;
            } else {
                // this is a non-prefix fuzzy, v is a single/prefix string
                modified = joinSingleStringOrPrefixStringAsFuzzyNonPrefix(v.str, v_is_prefix);
            }
        } // otherwise, neither is a single/prefix string so do nothing
        if (switch_both_to_fuzzy) {
            String oldstr = str;
            str = null;
            flags &= ~STR_PREFIX;
            joinSingleStringOrPrefixStringAsFuzzyNonPrefix(v.str, v_is_prefix);
            joinSingleStringOrPrefixStringAsFuzzyNonPrefix(oldstr, this_is_prefix);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean isMaybeStr(String s) {
        checkNotPolymorphicOrUnknown();
        if ((flags & STR_JSON) != 0)
            return true; // TODO: check that the string is really a JSON string? (true is a sound approximation)
        if (str != null) {
            if ((flags & STR_PREFIX) != 0)
                return s.startsWith(str);
            else
                return s.equals(str);
        } else if (Strings.isArrayIndex(s))
            return (flags & STR_UINT) != 0;
        else if (s.equals("Infinity") || s.equals("NaN"))
            return (flags & (STR_OTHERNUM | STR_IDENTIFIER)) != 0;
        else if (Strings.isNumber(s))
            return (flags & STR_OTHERNUM) != 0;
        else if (Strings.isIdentifier(s))
            return (flags & STR_IDENTIFIER) != 0;
        else if (Strings.isIdentifierParts(s))
            return (flags & STR_OTHERIDENTIFIERPARTS) != 0;
        else
            return (flags & STR_OTHER) != 0;
    }

    private static Value reallyMakeAnyStr() {
        Value r = new Value();
        r.flags |= STR_OTHERNUM | STR_IDENTIFIERPARTS | STR_OTHER;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyStrUInt() {
        Value r = new Value();
        r.flags |= STR_UINT;
        return canonicalize(r);
    }

    private static Value reallyMakeAnyStrNotUInt() {
        Value r = new Value();
        r.flags |= STR_IDENTIFIER | STR_OTHER | STR_OTHERNUM;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing any string.
     */
    public static Value makeAnyStr() {
        return theStrAny;
    }

    /**
     * Constructs the value describing any UInt string.
     */
    public static Value makeAnyStrUInt() {
        return theStrUInt;
    }

    /**
     * Constructs the value describing any non-UInt string.
     */
    public static Value makeAnyStrNotUInt() {
        return theStrNotUInt;
    }

    private static Value reallyMakeJSONStr() {
        Value r = new Value();
        r.flags |= STR_JSON;
        return canonicalize(r);
    }

    /**
     * Constructs the value describing any JSON string.
     */
    public static Value makeJSONStr() {
        return theJSONStr;
    }

    /**
     * Constructs the value describing the given string.
     */
    public static Value makeStr(String s) {
        Value r = new Value();
        r.str = s;
        return canonicalize(r);
    }

    /**
     * Constructs a temporary value describing the given string.
     * The object is not canonicalized and should therefore not be stored in abstract states.
     */
    public static Value makeTemporaryStr(String s) {
        Value r = new Value();
        r.str = s;
        return r;
    }

    @Override
    public Value restrictToStr() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value();
        r.flags = flags & STR;
        r.str = str;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotStr() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~STR;
        r.str = null;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotStrIdentifierParts() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~STR_IDENTIFIERPARTS;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotStrPrefix() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        if ((r.flags & STR_PREFIX) != 0) {
            r.str = null;
        }
        r.flags &= ~STR_PREFIX;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotStrUInt() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~STR_UINT;
        return canonicalize(r);
    }

    @Override
    public Value restrictToNotStrOtherNum() {
        checkNotPolymorphicOrUnknown();
        Value r = new Value(this);
        r.flags &= ~STR_OTHERNUM;
        return canonicalize(r);
    }

    @Override
    public boolean isStrDisjoint(Str other) {
        if (Options.get().isDebugOrTestEnabled()) {
            if (isMaybeOtherThanStr() || other.isMaybeOtherThanStr()) {
                throw new AnalysisException(String.format("Expects String-only values, got (%s, %s)", this, other));
            }
        }
        return (this.mustOnlyBeIdentifierCharacters() && other.mustContainNonIdentifierCharacters()) ||
                (this.mustContainNonIdentifierCharacters() && other.mustOnlyBeIdentifierCharacters()); // TODO: add more cases ...
    }

    @Override
    public boolean isStrMayContainSubstring(Str other) {
        if (Options.get().isDebugOrTestEnabled()) {
            if (isMaybeOtherThanStr() || other.isMaybeOtherThanStr()) {
                throw new AnalysisException(String.format("Expects String-only values, got (%s, %s)", this, other));
            }
        }
        return !this.mustOnlyBeIdentifierCharacters() || !other.mustContainNonIdentifierCharacters(); // TODO: add more cases ...
    }

    @Override
    public boolean mustContainNonIdentifierCharacters() {
        return isMaybeSingleStr() && !Strings.isIdentifierParts(getStr()); // TODO: add more cases ...
    }

    @Override
    public boolean mustOnlyBeIdentifierCharacters() {
        return isStrIdentifierParts(); // TODO: add more cases?
    }

    /* object labels */

    /**
     * Constructs the value describing the given object label.
     */
    public static Value makeObject(ObjectLabel v) {
        if (v == null)
            throw new NullPointerException();
        Value r = new Value();
        r.object_labels = newSet();
        r.object_labels.add(v);
        return canonicalize(r);
    }

    /**
     * Constructs the value describing the given symbol object label.
     */
    public static Value makeSymbol(ObjectLabel v) {
        if (v == null)
            throw new NullPointerException();
        if (v.getKind() != Kind.SYMBOL)
            throw new RuntimeException("Creating symbol value with a non-symbol");
        return Value.makeObject(v);
    }

    /**
     * Constructs the value describing the given object labels.
     */
    public static Value makeObject(Set<ObjectLabel> v) {
        Value r = new Value();
        if (!v.isEmpty())
            r.object_labels = newSet(v);
        return canonicalize(r);
    }

    /**
     * Constructs a value as the join of this value and the given object label.
     */
    public Value joinObject(ObjectLabel objlabel) {
        checkNotPolymorphicOrUnknown();
        if (object_labels != null && object_labels.contains(objlabel))
            return this;
        Value r = new Value(this);
        if (r.object_labels == null)
            r.object_labels = newSet();
        else
            r.object_labels = newSet(r.object_labels);
        r.object_labels.add(objlabel);
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but only with object values. (Symbols are not objects.)
     */
    public Value restrictToObject() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybePrimitiveOrSymbol() && !isMaybeGetterOrSetter())
            return this;
        Value r = new Value(this);
        r.flags &= ~PRIMITIVE;
        r.num = null;
        r.str = null;
        r.getters = r.setters = null;
        r.object_labels = newSet();
        if (object_labels != null)
            for (ObjectLabel objlabel : object_labels)
                if (objlabel.getKind() != Kind.SYMBOL)
                    r.object_labels.add(objlabel);
        if (r.object_labels.isEmpty())
            r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but only with non-object values. (Symbols are not objects.)
     * Unknown and polymorphic values are returned unmodified.
     * @throws AnalysisException if the value contains getters or setters.
     */
    public Value restrictToNotObject() {
        checkNoGettersSetters();
        if (object_labels == null)
            return this;
        Value r = new Value(this);
        r.object_labels = newSet();
        if (object_labels != null)
            for (ObjectLabel objlabel : object_labels)
                if (objlabel.getKind() == Kind.SYMBOL)
                    r.object_labels.add(objlabel);
        if (r.object_labels.isEmpty())
            r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with the given object labels removed.
     * @throws AnalysisException if the value contains getters or setters.
     */
    public Value removeObjects(Set<ObjectLabel> objs) {
        checkNotPolymorphicOrUnknown();
        checkNoGettersSetters();
        if (object_labels == null)
            return this;
        Value r = new Value(this);
        r.object_labels = newSet(r.object_labels);
        r.object_labels.removeAll(objs);
        if (r.object_labels.isEmpty())
            r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Converts the object labels in this value into getters.
     */
    public Value makeGetter() {
        Value r = new Value(this);
        r.getters = object_labels;
        r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Converts the object labels in this value into setters.
     */
    public Value makeSetter() {
        Value r = new Value(this);
        r.setters = object_labels;
        r.object_labels = null;
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with object labels summarized.
     * If s is null or the value is unknown or polymorphic, this value is returned instead.
     */
    public Value summarize(Summarized s) {
        if (s == null || isUnknown() || isPolymorphic())
            return this;
        Set<ObjectLabel> ss = s.summarize(object_labels);
        Set<ObjectLabel> ss_getters = s.summarize(getters);
        Set<ObjectLabel> ss_setters = s.summarize(setters);
        if ((ss == null || ss.equals(object_labels))
                && (ss_getters == null || ss_getters.equals(getters))
                && (ss_setters == null || ss_setters.equals(setters)))
            return this;
        Value r = new Value(this);
        if (ss != null && ss.isEmpty())
            ss = null;
        r.object_labels = ss;
        if (ss_getters != null && ss_getters.isEmpty())
            ss_getters = null;
        r.getters = ss_getters;
        if (ss_setters != null && ss_setters.isEmpty())
            ss_setters = null;
        r.setters = ss_setters;
        r.flags |= MODIFIED;
        return canonicalize(r);
    }

    /**
     * Returns true if this value is maybe present.
     */
    public boolean isMaybePresent() {
        checkNotUnknown();
        if (isPolymorphic())
            return (flags & (PRESENT_DATA | PRESENT_ACCESSOR)) != 0;
        else
            return (flags & PRIMITIVE) != 0 || num != null || str != null || object_labels != null || getters != null || setters != null;
    }

    /**
     * Returns true if this value is maybe present as a data property.
     */
    public boolean isMaybePresentData() {
        checkNotUnknown();
        if (isPolymorphic())
            return (flags & PRESENT_DATA) != 0;
        else
            return (flags & PRIMITIVE) != 0 || num != null || str != null || object_labels != null;
    }

    /**
     * Returns true if this value is maybe present as a getter/setter property.
     */
    public boolean isMaybePresentAccessor() {
        checkNotUnknown();
        if (isPolymorphic())
            return (flags & PRESENT_ACCESSOR) != 0;
        else
            return getters != null || setters != null;
    }

    /**
     * Returns true if this value is maybe present in the polymorphic part.
     */
    public boolean isMaybePolymorphicPresent() {
        return (flags & (PRESENT_DATA | PRESENT_ACCESSOR)) != 0;
    }

    /**
     * Returns true if this value is maybe present or 'unknown'.
     */
    public boolean isMaybePresentOrUnknown() {
        return isUnknown() || isMaybePresent();
    }

    /**
     * Returns true if this value is definitely not present.
     */
    public boolean isNotPresent() {
        checkNotUnknown();
        return !isMaybePresent();
    }

    /**
     * Returns a value as this one but marked as having extended scope.
     */
    public Value makeExtendedScope() {
        checkNotUnknown();
        if (isExtendedScope())
            return this;
        Value r = new Value(this);
        r.flags |= EXTENDEDSCOPE;
        return canonicalize(r);
    }

    /**
     * Returns true if this value is marked as having extended scope.
     */
    public boolean isExtendedScope() {
        return (flags & EXTENDEDSCOPE) != 0;
    }

    /**
     * Returns true if this value maybe represents an object.
     */
    public boolean isMaybeObject() {
        checkNotPolymorphicOrUnknown();
        return object_labels != null && object_labels.stream().anyMatch(x -> x.getKind() != Kind.SYMBOL);
    }

    /**
     * Returns true if this value maybe represents an object or a symbol.
     */
    public boolean isMaybeObjectOrSymbol() {
        checkNotPolymorphicOrUnknown();
        return object_labels != null;
    }

    /**
     * Returns true if this value maybe represents a getter.
     */
    public boolean isMaybeGetter() {
        checkNotPolymorphicOrUnknown();
        return getters != null;
    }

    /**
     * Returns true if this value maybe represents a setter.
     */
    public boolean isMaybeSetter() {
        checkNotPolymorphicOrUnknown();
        return setters != null;
    }

    /**
     * Returns true if this value maybe represents a getter or setter.
     */
    public boolean isMaybeGetterOrSetter() {
        checkNotPolymorphicOrUnknown();
        return getters != null || setters != null;
    }

    /**
     * Returns true if this value may be a primitive, including undefined, null.
     */
    public boolean isMaybePrimitive() {
        checkNotPolymorphicOrUnknown();
        return (flags & PRIMITIVE) != 0 || num != null || str != null;
    }

    /**
     * Returns true if this value may be a non-object, including undefined, null, and symbols.
     */
    public boolean isMaybePrimitiveOrSymbol() {
        return isMaybePrimitive() || isMaybeSymbol();
    }

    /**
     * Returns the (immutable) set of object labels (including symbols).
     * Returns the empty set for polymorphic and 'unknown' values.
     * Getters and setters are ignored (see {@link #getAllObjectLabels()}).
     */
    public Set<ObjectLabel> getObjectLabels() {
        if (object_labels == null)
            return Collections.emptySet();
        if (Options.get().isDebugOrTestEnabled())
            return Collections.unmodifiableSet(object_labels);
        return object_labels;
    }

    /**
     * Returns the (immutable) set of object labels.
     * Returns the empty set for polymorphic and 'unknown' values.
     * Getters and setters are included (see {@link #getObjectLabels()}).
     */
    public Set<ObjectLabel> getAllObjectLabels() {
        if (object_labels == null && getters == null && setters == null)
            return Collections.emptySet();
        if (getters == null && setters == null)
            return getObjectLabels();
        Set<ObjectLabel> s = newSet();
        if (object_labels != null)
            s.addAll(object_labels);
        if (getters != null)
            s.addAll(getters);
        if (setters != null)
            s.addAll(setters);
        if (Options.get().isDebugOrTestEnabled())
            return Collections.unmodifiableSet(s);
        return s;
    }

    /**
     * Returns the (immutable) set of object labels representing symbols.
     */
    @Override
    public Set<ObjectLabel> getSymbols() {
        if (object_labels == null)
            return Collections.emptySet();
        Set<ObjectLabel> s = newSet();
        for (ObjectLabel objlabel : object_labels)
            if (objlabel.getKind() == Kind.SYMBOL)
                s.add(objlabel);
        return s;
    }

    /**
     * Returns the (immutable) set of getters.
     * Returns the empty set for polymorphic and 'unknown' values.
     */
    public Set<ObjectLabel> getGetters() {
        if (getters == null)
            return Collections.emptySet();
        if (Options.get().isDebugOrTestEnabled())
            return Collections.unmodifiableSet(getters);
        return getters;
    }

    /**
     * Returns the (immutable) set of setters.
     * Returns the empty set for polymorphic and 'unknown' values.
     */
    public Set<ObjectLabel> getSetters() {
        if (setters == null)
            return Collections.emptySet();
        if (Options.get().isDebugOrTestEnabled())
            return Collections.unmodifiableSet(setters);
        return setters;
    }

    /**
     * Returns a copy of this value where the given object label has been replaced, if present.
     *
     * @param oldlabel The object label to replace.
     * @param newlabel The object label to replace oldlabel with.
     */
    public Value replaceObjectLabel(ObjectLabel oldlabel, ObjectLabel newlabel) {
        if (oldlabel.equals(newlabel))
            throw new AnalysisException("Equal object labels not expected");
        if ((object_labels == null || !object_labels.contains(oldlabel)) &&
                (getters == null || !getters.contains(oldlabel)) &&
                (setters == null || !setters.contains(oldlabel)))
            return this;
        Value r = new Value(this);
        if (object_labels != null) {
            Set<ObjectLabel> newobjlabels = newSet(object_labels);
            newobjlabels.remove(oldlabel);
            newobjlabels.add(newlabel);
            r.object_labels = newobjlabels;
        }
        if (getters != null) {
            Set<ObjectLabel> newgetters = newSet(getters);
            newgetters.remove(oldlabel);
            newgetters.add(newlabel);
            r.getters = newgetters;
        }
        if (setters != null) {
            Set<ObjectLabel> newsetters = newSet(setters);
            newsetters.remove(oldlabel);
            newsetters.add(newlabel);
            r.setters = newsetters;
        }
        return canonicalize(r);
    }

//    /**
//     * Returns a copy of this value where the object labels have been replaced according to the given map.
//     * Does not change modified flags. Object labels not in the key set of the map are unchanged.
//     *
//     * @param m A map between old object labels and new object labels.
//     * @return A copy of the old value with the object labels replaced according to the map.
//    */
//    public Value replaceObjectLabels(Map<ObjectLabel, ObjectLabel> m) {
//        if (isPolymorphic()) {
//            ObjectProperty pr = Renaming.apply(m, var);
//            if (pr.getObjectLabel().equals(var.getObjectLabel()))
//                return this;
//            Value r = new Value(this);
//            r.var = pr;
//            return canonicalize(r);
//        }
//        if ((object_labels == null && getters == null && setters == null) || m.isEmpty())
//            return this;
//        Value r = new Value(this);
//        if (object_labels != null) {
//            Set<ObjectLabel> newobjlabels = newSet();
//            for (ObjectLabel objlabel : object_labels)
//                newobjlabels.add(Renaming.apply(m, objlabel));
//            r.object_labels = newobjlabels;
//        }
//        if (getters != null) {
//            Set<ObjectLabel> newgetters = newSet();
//            for (ObjectLabel objlabel : getters)
//                newgetters.add(Renaming.apply(m, objlabel));
//            r.getters = newgetters;
//        }
//        if (setters != null) {
//            Set<ObjectLabel> newsetters = newSet();
//            for (ObjectLabel objlabel : setters)
//                newsetters.add(Renaming.apply(m, objlabel));
//            r.setters = newsetters;
//        }
//        return canonicalize(r);
//    }

    /**
     * Checks that this value is non-empty (or polymorphic).
     *
     * @throws AnalysisException if empty
     */
    public void assertNonEmpty() {
        checkNotUnknown();
        if (isPolymorphic())
            return;
        if ((flags & PRIMITIVE) == 0 && num == null && str == null && object_labels == null && getters == null && setters == null
                && !Options.get().isPropagateDeadFlow())
            throw new AnalysisException("Empty value");
    }

    /**
     * Returns the number of different types of this value.
     * The possible types are here boolean/string/number/function/array/native/dom/other. Undef and null are ignored, except if they are the only value.
     * Polymorphic and unknown values also count as 0.
     */
    public int typeSize() {
        if (isUnknown() || isPolymorphic())
            return 0;
        int c = 0;
        if (!isNotBool())
            c++;
        if (!isNotStr())
            c++;
        if (!isNotNum())
            c++;
        if (object_labels != null) {
            boolean is_function = false;
            boolean is_array = false;
            boolean is_native = false;
            boolean is_dom = false;
            boolean is_other = false;
            for (ObjectLabel objlabel : object_labels) {
                if (objlabel.getKind() == Kind.FUNCTION)
                    is_function = true;
                else if (objlabel.getKind() == Kind.ARRAY)
                    is_array = true;
                else if (objlabel.isHostObject()) {
                    switch (objlabel.getHostObject().getAPI().getShortName()) {
                        case "native":
                            is_native = true;
                            break;
                        case "dom":
                            is_dom = true;
                            break;
                        default:
                            is_other = true;
                    }
                } else
                    is_other = true;
            }
            if (is_function)
                c++;
            if (is_array)
                c++;
            if (is_native)
                c++;
            if (is_dom)
                c++;
            if (is_other)
                c++;
        }
        if (getters != null)
            c++;
        if (setters != null)
            c++;
        if (c == 0 && (isMaybeNull() || isMaybeUndef())) {
            c = 1;
        }
        return c;
    }

    /**
     * Constructs a value as a copy of this value but for reading attributes.
     */
    public Value restrictToAttributes() {
        Value r = new Value(this);
        r.num = null;
        r.str = null;
        r.var = null;
        r.flags &= ATTR | ABSENT | UNKNOWN;
        if (!isUnknown() && isMaybePresent())
            r.flags |= UNDEF; // just a dummy value, to satisfy the representation invariant for PRESENT
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of this value but with all attributes set to bottom.
     */
    public Value restrictToNonAttributes() {
        Value r = new Value(this);
        r.flags &= ~(PROPERTYDATA | ABSENT | PRESENT_DATA | PRESENT_ACCESSOR);
        return canonicalize(r);
    }

    /**
     * Constructs a value as a copy of the given value but with the attributes from this value.
     */
    public Value replaceValue(Value v) {
        Value r = new Value(v);
        r.flags &= ~(PROPERTYDATA | ABSENT | PRESENT_DATA | PRESENT_ACCESSOR);
        r.flags |= flags & (PROPERTYDATA | ABSENT);
        if (r.var != null)
            r.flags |= flags & (PRESENT_DATA | PRESENT_ACCESSOR);
        return canonicalize(r);
    }

    /**
     * Returns true is this value contains exactly one object label.
     */
    public boolean isMaybeSingleObjectLabel() {
        checkNotPolymorphicOrUnknown();
        return object_labels != null && object_labels.size() == 1;
    }

    /**
     * Returns true is this value contains exactly one object source location.
     */
    public boolean isMaybeSingleAllocationSite() {
        checkNotPolymorphicOrUnknown();
        return object_labels != null && newSet(getObjectSourceLocations()).size() == 1;
    }

    /**
     * Returns true if this value does not contain a summarized object label.
     */
    public boolean isNotASummarizedObject() {
        checkNotPolymorphicOrUnknown();
        if (object_labels == null) {
            return true;
        }
        for (ObjectLabel object_label : object_labels) {
            if (!object_label.isSingleton()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if this value does not contain a singleton object label.
     */
    public boolean isNotASingletonObject() {
        checkNotPolymorphicOrUnknown();
        if (object_labels == null) {
            return true;
        }
        for (ObjectLabel object_label : object_labels) {
            if (object_label.isSingleton()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if this value contains the given object label.
     * @throws AnalysisException if the value contains getters or setters.
     */
    public boolean containsObjectLabel(ObjectLabel objlabel) {
        return (object_labels != null && object_labels.contains(objlabel)) ||
                (getters != null && getters.contains(objlabel)) ||
                (setters != null && setters.contains(objlabel));
    }

    @Override
    public Value restrictToNotNumZero() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeZero()) {
            return this;
        }
        Value r = new Value(this);
        if (r.num != null && isZero(r.num)) {
            r.num = null;
        }
        r.flags &= ~NUM_ZERO;
        return canonicalize(r);
    }

    @Override
    public boolean isMaybeZero() {
        checkNotPolymorphicOrUnknown();
        if (num != null && isZero(num)) {
            return true;
        }
        return (flags & NUM_ZERO) != 0;
    }

    @Override
    public boolean isMaybeNumUIntPos() {
        checkNotPolymorphicOrUnknown();
        return (flags & NUM_UINT_POS) != 0;
    }

    @Override
    public boolean isMaybeSameNumber(Value v) {
        checkNotPolymorphicOrUnknown();
        if (num != null) {
            return v.isMaybeNum(num);
        }
        if (v.num != null) {
            return isMaybeNum(v.num);
        }
        return (flags & v.flags & NUM) != 0;
    }

    @Override
    public boolean isMaybeSameNumberWhenNegated(Value v) {
        checkNotPolymorphicOrUnknown();
        if (num != null) {
            return v.isMaybeNum(-num);
        }
        if (v.num != null) {
            return isMaybeNum(-v.num);
        }
        boolean maybePos = (flags & NUM_UINT_POS) != 0;
        boolean maybeNeg = (flags & NUM_OTHER) != 0;
        boolean maybeZero = (flags & NUM_ZERO) != 0;

        boolean v_maybeNeg = (v.flags & NUM_OTHER) != 0;
        boolean v_maybePos = (v.flags & NUM_UINT_POS) != 0;
        boolean v_maybeZero = (v.flags & NUM_ZERO) != 0;

        boolean maybePosNeg = maybePos && v_maybeNeg;
        boolean maybeNegPos = maybeNeg && v_maybePos;
        boolean maybeZeroZero = maybeZero && v_maybeZero;
        return maybePosNeg || maybeNegPos || maybeZeroZero;
    }

    @Override
    public Value restrictToNotNumInf() {
        checkNotPolymorphicOrUnknown();
        if (!isMaybeInf())
            return this;
        Value r = new Value(this);
        r.flags &= ~NUM_INF;
        return canonicalize(r);
    }
}
