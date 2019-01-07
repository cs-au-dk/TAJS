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

package dk.brics.tajs.blendedanalysis.solver;

import dk.brics.tajs.blendedanalysis.InstructionComponent;
import dk.brics.tajs.lattice.Value;

import java.util.Objects;

public class Constraint {

    private InstructionComponent ic;

    private Value value;

    /**
     *  A constraint specifies a value for the instructionComponent.
     */
    public Constraint(InstructionComponent ic, Value value) {
        this.ic = ic;
        this.value = value;
    };

    public InstructionComponent getInstructionComponent() {
        return ic;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "ic=" + ic +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(ic, that.ic) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ic, value);
    }
}
