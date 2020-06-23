/*
 * Copyright 2009-2020 Aarhus University
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

import dk.au.cs.casa.typescript.types.Signature;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Set of TypeScript function signatures.
 */
public class FunctionTypeSignatures implements DeepImmutable {

    private Set<Signature> signatures;

    private boolean isAny;

    private static FunctionTypeSignatures theAny = makeAny();

    private FunctionTypeSignatures(Collection<Signature> signatures) {
        this.signatures = signatures != null ? newSet(signatures) : null;
    }

    public static FunctionTypeSignatures make(Collection<Signature> signatures) {
        return Canonicalizer.get().canonicalizeViaImmutableBox(new FunctionTypeSignatures(signatures));
    }

    private static FunctionTypeSignatures makeAny() {
        FunctionTypeSignatures s = new FunctionTypeSignatures(null);
        s.isAny = true;
        return s;
    }

    @Nonnull
    public Set<Signature> getSignatures() {
        if (signatures == null)
            throw new AnalysisException("getSignatures shouldn't be called on 'any'");
        return signatures;
    }

    public boolean isAny() {
        return isAny;
    }

    public static FunctionTypeSignatures join(FunctionTypeSignatures s1, FunctionTypeSignatures s2) {
        if (s1 == null && s2 == null)
            return null;
        if (s1 == null || s2 == null || s1.isAny || s2.isAny)
            return theAny;
        Set<Signature> s = newSet(s1.signatures);
        s.addAll(s2.signatures);
        return new FunctionTypeSignatures(s);
    }

    public static FunctionTypeSignatures union(FunctionTypeSignatures s1, FunctionTypeSignatures s2) {
        if (s1 == null || (s2 != null && s2.isAny))
            return s2;
        if (s2 == null || s1.isAny)
            return s1;
        Set<Signature> s = newSet(s1.signatures);
        s.addAll(s2.signatures);
        return new FunctionTypeSignatures(s);
    }

    public static boolean containsAll(FunctionTypeSignatures s1, FunctionTypeSignatures s2) {
        if ((s1 != null && s1.isAny) || s2 == null)
            return true;
        if (s1 == null || s2.isAny)
            return false;
        return s1.signatures.containsAll(s2.signatures);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionTypeSignatures that = (FunctionTypeSignatures) o;
        if (isAny != that.isAny) return false;
        if (signatures != null ? !signatures.equals(that.signatures) : that.signatures != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = signatures != null ? signatures.hashCode() : 0;
        result = 31 * result + (isAny ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        if (isAny)
            return "AnyFunction";
        return "[" + signatures.stream().map(FunctionTypeSignatures::signatureToString).collect(Collectors.joining(",")) + "]";
    }

    private static String signatureToString(Signature s) {
        return "(" + s.getParameters().stream().map(p -> p.getType().toString()).collect(Collectors.joining(",")) + ")->" + s.getResolvedReturnType();
    }
}
