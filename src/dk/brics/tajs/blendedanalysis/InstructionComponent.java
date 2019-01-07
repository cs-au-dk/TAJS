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

package dk.brics.tajs.blendedanalysis;

import dk.brics.tajs.util.AnalysisException;

import java.util.Objects;
import java.util.Optional;

/**
 * Specify a component of a read, write or call.
 */
public class InstructionComponent {

    /**
     * Kind of instruction component.
     */
    private final InstructionComponentKinds instructionComponentKind;

    /**
     * If instructionComponentKind is argument, then argNumber specifies which argument number is the instruction component.
     * Otherwise, argNumber is NONE.
     */
    private final Optional<Integer> argNumber;

    private InstructionComponent(InstructionComponentKinds instructionComponentKind, Optional<Integer> argNumber) {
        if (argNumber.isPresent() && instructionComponentKind != InstructionComponentKinds.ARG) {
            throw new AnalysisException("argNumber should NOT be set, when the instruction component is NOT of type argument");
        }
        if (!argNumber.isPresent() && instructionComponentKind == InstructionComponentKinds.ARG) {
            throw new AnalysisException("argNumber should be set, when the instruction component is of type argument");
        }
        this.instructionComponentKind = instructionComponentKind;
        this.argNumber = argNumber;
    }

    private InstructionComponent(InstructionComponentKinds instructionComponentKind) {
        this(instructionComponentKind, Optional.empty());
    }

    public static InstructionComponent mkBase() {
        return new InstructionComponent(InstructionComponentKinds.BASE);
    }

    public static InstructionComponent mkProperty() {
        return new InstructionComponent(InstructionComponentKinds.PROPERTY);
    }

    public static InstructionComponent mkTarget() {
        return new InstructionComponent(InstructionComponentKinds.TARGET);
    }

    public static InstructionComponent mkArg(int argNumber) {
        return new InstructionComponent(InstructionComponentKinds.ARG, Optional.of(argNumber));
    }

    public boolean isBase() {
        return instructionComponentKind == InstructionComponentKinds.BASE;
    }

    public boolean isProperty() {
        return instructionComponentKind == InstructionComponentKinds.PROPERTY;
    }

    public boolean isTarget() {
        return instructionComponentKind == InstructionComponentKinds.TARGET;
    }

    public boolean isArgument() {
        return instructionComponentKind == InstructionComponentKinds.ARG;
    }

    public int getArgNumber() {
        if (!argNumber.isPresent()) {
            throw new AnalysisException("Instruction component is not of type argument");
        }
        return argNumber.get();
    }

    @Override
    public String toString() {
        if (instructionComponentKind == InstructionComponentKinds.ARG) {
            return instructionComponentKind.name() + argNumber.get();
        }
        return instructionComponentKind.name();
    }

    private enum InstructionComponentKinds {
        BASE,
        PROPERTY,
        TARGET,
        ARG
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructionComponent that = (InstructionComponent) o;
        return instructionComponentKind == that.instructionComponentKind &&
                Objects.equals(argNumber, that.argNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionComponentKind, argNumber);
    }
}
