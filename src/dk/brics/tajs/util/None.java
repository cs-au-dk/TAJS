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

package dk.brics.tajs.util;

/**
 * Optional object, absent.
 */
public class None<T> implements Optional<T> {

    private static None<?> instance;

    /**
     * Makes an absent object.
     */
    @SuppressWarnings("unchecked")
    public static <T> None<T> make() {
        if (instance == null) {
            instance = new None<>();
        }
        return (None<T>) instance;
    }

    @Override
    public <S> S apply(OptionalObjectVisitor<S, T> v) {
        return v.visit(this);
    }
}
