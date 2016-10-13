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
 * Optional object, present.
 */
public class Some<T> implements Optional<T> {

    private final T obj;

    /**
     * Makes a present object.
     */
    public Some(T obj) {
        this.obj = obj;
    }

    /**
     * Makes a present object.
     */
    public static <T> Optional<T> make(T obj) {
        return new Some<>(obj);
    }

    @Override
    public <S> S apply(OptionalObjectVisitor<S, T> v) {
        return v.visit(this);
    }

    /**
     * Returns the object.
     */
    public T get() {
        return obj;
    }
}
