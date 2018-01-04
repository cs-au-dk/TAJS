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

public interface ConcreteValueVisitor<T> {

    T visit(ConcreteNumber v);

    T visit(ConcreteString v);

    T visit(ConcreteArray v);

    T visit(ConcreteUndefined v);

    T visit(ConcreteRegularExpression v);

    T visit(ConcreteNull v);

    T visit(ConcreteNullOrUndefined v);

    T visit(ConcreteBoolean v);
}
