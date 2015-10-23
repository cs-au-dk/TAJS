/*
 * Copyright 2009-2015 Aarhus University
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

package dk.brics.tajs.solver;

/**
 * Interface for analysis contexts.
 * Must be immutable.
 */
public interface IContext<ContextType extends IContext<?>> {

    /**
     * Reconstructs the context at function or for-in entry.
     */
    ContextType makeEntryContext();

    /**
     * Checks whether this context is equal to the given object.
     */
    @Override
    boolean equals(Object obj);

    /**
     * Computes a hash code for this context.
     */
    @Override
    int hashCode();

    /**
     * Returns a description of this context.
     */
    @Override
    String toString();
}
