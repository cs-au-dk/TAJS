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

package dk.brics.tajs.typescript;

import dk.au.cs.casa.typescript.types.AnonymousType;
import dk.au.cs.casa.typescript.types.BooleanLiteral;
import dk.au.cs.casa.typescript.types.ClassInstanceType;
import dk.au.cs.casa.typescript.types.ClassType;
import dk.au.cs.casa.typescript.types.GenericType;
import dk.au.cs.casa.typescript.types.IndexType;
import dk.au.cs.casa.typescript.types.IndexedAccessType;
import dk.au.cs.casa.typescript.types.InterfaceType;
import dk.au.cs.casa.typescript.types.IntersectionType;
import dk.au.cs.casa.typescript.types.NumberLiteral;
import dk.au.cs.casa.typescript.types.ReferenceType;
import dk.au.cs.casa.typescript.types.SimpleType;
import dk.au.cs.casa.typescript.types.StringLiteral;
import dk.au.cs.casa.typescript.types.ThisType;
import dk.au.cs.casa.typescript.types.TupleType;
import dk.au.cs.casa.typescript.types.Type;
import dk.au.cs.casa.typescript.types.TypeParameterType;
import dk.au.cs.casa.typescript.types.TypeVisitor;
import dk.au.cs.casa.typescript.types.UnionType;

/**
 * Type visitor with default null.
 */
public abstract class DefaultTypeVisitor<T> implements TypeVisitor<T> {
    
    public DefaultTypeVisitor() {}

    protected T visitDefault(Type t) {
        return null;
    }

    @Override
    public T visit(AnonymousType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(ClassType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(GenericType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(InterfaceType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(ReferenceType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(SimpleType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(TupleType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(UnionType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(TypeParameterType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(StringLiteral t) {
        return visitDefault(t);
    }

    @Override
    public T visit(BooleanLiteral t) {
        return visitDefault(t);
    }

    @Override
    public T visit(NumberLiteral t) {
        return visitDefault(t);
    }

    @Override
    public T visit(IntersectionType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(ClassInstanceType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(ThisType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(IndexType t) {
        return visitDefault(t);
    }

    @Override
    public T visit(IndexedAccessType t) {
        return visitDefault(t);
    }
}