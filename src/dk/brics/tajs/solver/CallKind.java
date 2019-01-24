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

package dk.brics.tajs.solver;

/**
 * Description of call as ordinary, implicit, or implicit constructor call.
 */
public enum CallKind {

    /**
     * Ordinary (i.e. non-implicit) call or constructor call.
     */
    ORDINARY(false, false),

    /**
     * Implicit (non-constructor) call.
     * (Typically used for implicit calls to valueOf and toString.)
     */
    IMPLICIT(true, false),

    /**
     * Implicit constructor call.
     */
    IMPLICIT_CONSTRUCTOR(true, true);

    private boolean implicit;

    private boolean implicitConstructorCall;

    CallKind(boolean implicit, boolean implicitConstructorCall) {
        this.implicit = implicit;
        this.implicitConstructorCall = implicitConstructorCall;
        assert (!implicitConstructorCall || implicit); // implicitConstructorCall ==> implicit
    }

    public boolean isImplicit() {
        return implicit;
    }

    public boolean isImplicitConstructorCall() {
        return implicitConstructorCall;
    }
}
