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

package dk.brics.tajs.typescript;

import dk.au.cs.casa.typescript.types.BooleanLiteral;
import dk.au.cs.casa.typescript.types.InterfaceType;
import dk.au.cs.casa.typescript.types.IntersectionType;
import dk.au.cs.casa.typescript.types.NumberLiteral;
import dk.au.cs.casa.typescript.types.Signature;
import dk.au.cs.casa.typescript.types.SimpleType;
import dk.au.cs.casa.typescript.types.SimpleTypeKind;
import dk.au.cs.casa.typescript.types.StringLiteral;
import dk.au.cs.casa.typescript.types.Type;
import dk.au.cs.casa.typescript.types.UnionType;
import dk.brics.tajs.analysis.PropVarOperations;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.Filtering;
import dk.brics.tajs.lattice.FunctionTypeSignatures;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.Restriction;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Lists;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.Triple;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Type filtering using TypeScript types (assuming that the types are correct).
 */
public class TypeFiltering {

    private static Logger log = Logger.getLogger(TypeFiltering.class);

//    static {
//        LogManager.getLogger(TypeFiltering.class).setLevel(Level.DEBUG);
//    }

    private Filtering filtering;

    private Solver.SolverInterface c;

    private PropVarOperations pv;

    public TypeFiltering(Solver.SolverInterface c) {
        this.c = c;
        filtering = c.getAnalysis().getFiltering();
        pv = c.getAnalysis().getPropVarOperations();
    }

    /**
     * Applies type filtering on module.exports according to the type (if non-null).
     */
    public void assumeModuleType(Type t, Value module) {
        if (t != null) {
            if (module.isMaybePrimitive()) {
                log.info("Expected abstract object as module, was " + module);
            }
            assumeObjectPropertyHasType(module.getObjectLabels(), PKey.StringPKey.make("exports"), t, newSet());
        }
    }

    /**
     * Applies type filtering on a function argument value.
     */
    public Value assumeParameterType(Value v, FunctionTypeSignatures signatures, int i) {
        // TODO: any other important fields of signatures.getSignatures() or can they safely be ignored? (same for assumeReturnValueType)
        if (!signatures.isAny() && signatures.getSignatures().size() == 1) {
            List<Signature.Parameter> ps = signatures.getSignatures().iterator().next().getParameters();
            if (i < ps.size()) {
                Value res = assumeValueHasType(v, ps.get(i).getType(), newSet());
                if (log.isDebugEnabled() && c.isScanning() && res.isNone()) // TODO: move to monitoring?
                    log.debug("Function argument filtered to bottom at " + c.getNode().getSourceLocation());
                return res;
            } // TODO: filter away the entire function if i >= ps.size()?
        } else {
            log.debug("No type filtering implemented for function signatures " + signatures); // TODO: handle multiple signatures, like union
        }
        return v;
    }

    /**
     * Applies type filtering on a function return value.
     */
    public Value assumeReturnValueType(Value v, FunctionTypeSignatures signatures) {
        if (!signatures.isAny() && signatures.getSignatures().size() == 1) {
            Value res = assumeValueHasType(v, signatures.getSignatures().iterator().next().getResolvedReturnType(), newSet());
            if (log.isDebugEnabled() && c.isScanning() && res.isNone()) // TODO: move to monitoring? (note: this can happen at spurious call edges!)
                log.debug("Function return value filtered to bottom at " + c.getNode().getSourceLocation());
            return res;
        } else {
            log.debug("No type filtering implemented for function signatures " + signatures); // TODO: handle multiple signatures, like union
        }
        return v;
    }

    /**
     * Converts a simple type to a restriction.
     * Return null if there is no restriction for the given type.
     */
    private static Restriction simpleTypeToRestriction(SimpleType t) {
        switch (t.getKind()) {
            case String:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeAnyStr());
            case Number:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeAnyNum());
            case Boolean:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeAnyBool());
            case Symbol:
                return new Restriction(Restriction.Kind.TYPEOF_SYMBOL);
            case Undefined:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeUndef());
            case Object:
                return new Restriction(Restriction.Kind.TYPEOF_OBJECT_OR_FUNCTION);
            case Null:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeNull());
            case Never:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeNone());
            case Void:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeUndef().joinNull());
            case Enum:
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeAnyNumUInt().joinAnyStr());
            case Any: // do nothing
            default:
                return null;
        }
    }

    /**
     * Converts an object type to a restriction.
     */
    private Restriction objectTypeToRestriction(Type t, List<Signature> functionSignatures, List<Signature> constructorSignatures) {
        ObjectLabel.Kind stdType = TypeScriptDeclLoader.getStdTypes().get(t);
        if (stdType != null)
            return new Restriction(Restriction.Kind.OBJKIND).set(stdType);
        else if (functionSignatures.isEmpty() && constructorSignatures.isEmpty()) // the object property value must be an (non-function) object
            return new Restriction(Restriction.Kind.TYPEOF_OBJECT); // FIXME: call/constructor signatures may be inherited?
        else // the object property value must be a function, and when called the parameters and return value must match one of the signatures
            return new Restriction(Restriction.Kind.TYPED_FUNCTION).set(FunctionTypeSignatures.make(Lists.concat(functionSignatures, constructorSignatures))); // TODO: keep function signatures and constructor signatures separate
    }

    /**
     * Converts a primitive type (including unions) to a restriction.
     * Return null if there is no restriction for the given type.
     */
    private Restriction primitiveTypeToRestriction(Type t) {
        return t.accept(new DefaultTypeVisitor<Restriction>() {

            @Override
            public Restriction visit(SimpleType t) {
                return simpleTypeToRestriction(t);
            }

            @Override
            public Restriction visit(StringLiteral t) {
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeStr(t.getText()));
            }

            @Override
            public Restriction visit(BooleanLiteral t) {
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeBool(t.getValue()));
            }

            @Override
            public Restriction visit(NumberLiteral t) {
                return new Restriction(Restriction.Kind.STRICT_EQUAL).set(Value.makeNum(t.getValue()));
            }

            @Override
            public Restriction visit(UnionType t) {
                List<Restriction> rs = newList();
                for (Type t2 : t.getElements()) {
                    Restriction r = primitiveTypeToRestriction(t2);
                    if (r == null) // if one of the types can't be converted, abort
                        return null;
                    rs.add(r);
                }
                return new Restriction(Restriction.Kind.UNION).set(rs);
            }
        });
    }

    /**
     * Replaces interface types for boxed primitives by the corresponding primitive types.
     */
    private Type transformInterfaceTypeToSimpleType(Type t) {
        ObjectLabel.Kind stdType = TypeScriptDeclLoader.getStdTypes().get(t);
        if (stdType != null)
            switch (stdType) {
                case BOOLEAN:
                    return new SimpleType(SimpleTypeKind.Boolean);
                case NUMBER:
                    return new SimpleType(SimpleTypeKind.Number);
                case STRING:
                    return new SimpleType(SimpleTypeKind.String);
                case OBJECT:
                    return new SimpleType(SimpleTypeKind.Object);

            }
        return t;
    }

    /**
     * Assumes that that the given object property has the given type.
     */
    private void assumeObjectPropertyHasType(Set<ObjectLabel> baseobjs, PKey propname, Type t, Set<Triple<Set<ObjectLabel>, PKey, Type>> visited) {
        Triple<Set<ObjectLabel>, PKey, Type> p = Triple.make(baseobjs, propname, t);
        if (visited.contains(p))
            return;
        visited.add(p);
        t = transformInterfaceTypeToSimpleType(t);
        if (log.isDebugEnabled())
            log.debug("Type filtering object property " + baseobjs + "." + propname + " with " + t);
        Object r = t.accept(new DefaultTypeVisitor<Object>() {

            @Override
            public Object visit(InterfaceType t) {
                filtering.assumeObjectPropertySatisfies(baseobjs, propname, objectTypeToRestriction(t, t.getDeclaredCallSignatures(), t.getDeclaredConstructSignatures()));
                if (!t.getDeclaredProperties().isEmpty() && ObjectLabel.allowStrongUpdate(baseobjs)) { // the object must have the properties described by t.declaredProperties
                    Value v = pv.readPropertyWithAttributes(baseobjs, propname.toValue());
                    v = UnknownValueResolver.getRealValue(v, c.getState());
                    for (Map.Entry<String, Type> propType : t.getDeclaredProperties().entrySet())
                        assumeObjectPropertyHasType(v.getObjectLabels(), PKey.StringPKey.make(propType.getKey()), propType.getValue(), visited);
                }
                for (Type bt : t.getBaseTypes()) // also restrict using the base types
                    assumeObjectPropertyHasType(baseobjs, propname, bt, visited);
                return true;
            }

            @Override
            public Object visit(IntersectionType t) {
                for (Type t2 : t.getElements())
                    assumeObjectPropertyHasType(baseobjs, propname, t2, visited);
                return true;
            }

            @Override
            public Object visitDefault(Type t) {
                return filtering.assumeObjectPropertySatisfies(baseobjs, propname, primitiveTypeToRestriction(t));
            }
        });
        if (r == null && !isAny(t))
            log.debug("No object property type filtering implemented for " + t); // TODO: TupleType, AnonymousType, ClassType, GenericType, ReferenceType, TypeParameterType, ClassInstanceType, ThisType, IndexType, IndexedAccessType? (see also assumeValueHasType)
    }

    private boolean isAny(Type t) {
        return t instanceof SimpleType && ((SimpleType)t).getKind() == SimpleTypeKind.Any;
    }

    /**
     * Assumes that that the given value has the given type.
     */
    private Value assumeValueHasType(Value v, Type t, Set<Pair<Value, Type>> visited) {
        Pair<Value, Type> p = Pair.make(v, t);
        if (visited.contains(p))
            return v;
        visited.add(p);
        t = transformInterfaceTypeToSimpleType(t);
        if (log.isDebugEnabled())
            log.debug("Type filtering value " + v + " with " + t);
        Value v2 = t.accept(new DefaultTypeVisitor<Value>() {

            @Override
            public Value visit(InterfaceType t) {
                Value res = filtering.assumeValueSatisfies(v, objectTypeToRestriction(t, t.getDeclaredCallSignatures(), t.getDeclaredConstructSignatures()));
                Set<ObjectLabel> baseobjs = res.getObjectLabels();
                if (!t.getDeclaredProperties().isEmpty() && ObjectLabel.allowStrongUpdate(baseobjs)) // the object must have the properties described by t.declaredProperties
                    for (Map.Entry<String, Type> propType : t.getDeclaredProperties().entrySet())
                        assumeObjectPropertyHasType(baseobjs, PKey.StringPKey.make(propType.getKey()), propType.getValue(), newSet());
                for (Type bt : t.getBaseTypes()) // also restrict using the base types
                    res = assumeValueHasType(res, bt, visited);
                return res;
            }

            @Override
            public Value visit(IntersectionType t) {
                Value v2 = v;
                for (Type t2 : t.getElements())
                    v2 = assumeValueHasType(v2, t2, visited);
                return v2;
            }

            @Override
            public Value visitDefault(Type t) {
                return filtering.assumeValueSatisfies(v, primitiveTypeToRestriction(t));
            }
        });
        if (v2 == null) {
            if (!isAny(t))
                log.debug("No value type filtering implemented for " + t); // TODO: TupleType, AnonymousType, ClassType, GenericType, ReferenceType, TypeParameterType, ClassInstanceType, ThisType, IndexType, IndexedAccessType? (see also assumeObjectPropertyHasType)
            return v;
        }
        return v2;
    }
}
