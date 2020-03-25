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

package dk.brics.tajs.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * Triple of objects.
 */
public class Triple<F, S, T> implements Serializable {

    private F fst;

    private S snd;

    private T trd;

    public Triple(F fst, S snd, T trd) {
        this.fst = fst;
        this.snd = snd;
        this.trd = trd;
    }

    /**
     * Constructs a new triple.
     */
    public static <F, S, T> Triple<F, S, T> make(F fst, S snd, T trd) {
        return new Triple<>(fst, snd, trd);
    }

    /**
     * Returns the first object.
     */
    public F getFirst() {
        return fst;
    }

    /**
     * Returns the second object.
     */
    public S getSecond() {
        return snd;
    }

    /**
     * Returns the third object.
     */
    public T getThird() {
        return trd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd, trd);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Triple)) {
            return false;
        }
        Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
        return Objects.equals(fst, other.fst) &&
                Objects.equals(snd, other.snd) &&
                Objects.equals(trd, other.trd);
    }

    @Override
    public String toString() {
        return "(" + fst + ", " + snd + "," + trd + ")";
    }
}
