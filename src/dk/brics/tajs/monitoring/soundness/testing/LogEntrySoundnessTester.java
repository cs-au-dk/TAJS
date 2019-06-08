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

package dk.brics.tajs.monitoring.soundness.testing;

import dk.au.cs.casa.jer.entries.AllocationSiteObjectDescription;
import dk.au.cs.casa.jer.entries.BuiltinObjectDescription;
import dk.au.cs.casa.jer.entries.CallEntry;
import dk.au.cs.casa.jer.entries.ConcreteStringDescription;
import dk.au.cs.casa.jer.entries.DynamicCodeEntry;
import dk.au.cs.casa.jer.entries.EntryVisitor;
import dk.au.cs.casa.jer.entries.FunctionEntry;
import dk.au.cs.casa.jer.entries.FunctionExitEntry;
import dk.au.cs.casa.jer.entries.IEntry;
import dk.au.cs.casa.jer.entries.ObjectDescription;
import dk.au.cs.casa.jer.entries.ObjectDescriptionVisitor;
import dk.au.cs.casa.jer.entries.OtherDescription;
import dk.au.cs.casa.jer.entries.OtherObjectDescription;
import dk.au.cs.casa.jer.entries.OtherSymbolDescription;
import dk.au.cs.casa.jer.entries.PrefixStringDescription;
import dk.au.cs.casa.jer.entries.ValueDescription;
import dk.au.cs.casa.jer.entries.ValueDescriptionVisitor;
import dk.au.cs.casa.jer.entries.VariableOrPropertyEntry;
import dk.brics.tajs.analysis.Conversion;
import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.ValueLogLocationInformation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.ConstantNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.PKey.StringPKey;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.soundness.ValueLogSourceLocationEqualityDecider;
import dk.brics.tajs.monitoring.soundness.ValueLoggerSourceLocationMapper;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * The complex part of soundness testing: checks that log entries from a concete execution are over-approximated by the abstract states computed by the static analysis.
 *
 * TODO this class could use a thorough re-implementation. (GitHub #415)
 */
class LogEntrySoundnessTester implements EntryVisitor<Void> {

    private static final Logger log = Logger.getLogger(LogEntrySoundnessTester.class);

    public final Solver.SolverInterface c;

    private final Map<Pair<SourceLocation, String>, Set<Value>> type_map;

    private final Set<SoundnessCheck> checks;

    private final AbstractConcreteValueComparator abstractConcreteValueComparator;

    private Map<Class<? extends AbstractNode>, Map<SourceLocation, Set<AbstractNode>>> loc2nodes;

    public LogEntrySoundnessTester(Map<Pair<SourceLocation, String>, Set<Value>> type_map, Map<Class<? extends AbstractNode>, Map<SourceLocation, Set<AbstractNode>>> loc2nodes, Set<SoundnessCheck> checks, ValueLogSourceLocationEqualityDecider equalityDecider, ValueLogLocationInformation valueLogLocationInformation, Set<SourceLocation> domObjectAllocationSites, Solver.SolverInterface c) {
        this.type_map = type_map;
        this.loc2nodes = loc2nodes;
        this.checks = checks;
        this.c = c;
        Set<HostObject> builtins = newSet();
        builtins.addAll(Arrays.asList(ECMAScriptObjects.values()));
        if (Options.get().isDOMEnabled()) {
            builtins.addAll(Arrays.asList(DOMObjects.values()));
        }
        Set<String> canonicalNamesForBuiltins = builtins.stream()
                .map(HostObject::toString)
                // workaround for "bad" toString
                .map(name -> name.startsWith("Window.") ? name.substring("Window.".length()) : name)
                .collect(Collectors.toSet());
        abstractConcreteValueComparator = new AbstractConcreteValueComparator(canonicalNamesForBuiltins, equalityDecider, domObjectAllocationSites, valueLogLocationInformation, c.getFlowGraph()::isHostEnvironmentSource);
    }

    boolean isAbstractValueSound(ValueDescription concreteValue, Value abstractValue) {
        return abstractConcreteValueComparator.isAbstractValueSound(concreteValue, abstractValue);
    }

    /**
     * Collects all the values at a single location.
     */
    private Collection<Value> getValuesForAllContexts(dk.au.cs.casa.jer.entries.SourceLocation jalangiLocation, String varOrProp) {
        return type_map.getOrDefault(Pair.make(ValueLoggerSourceLocationMapper.makeTAJSSourceLocation(jalangiLocation), varOrProp), newSet());
    }

    @Override
    public Void visit(VariableOrPropertyEntry e) {
        String varOrProp = e.getVarOrProp().accept(new ValueDescriptionVisitor<String>() {
            @Override
            public String visit(OtherDescription d) {
                return null;
            }

            @Override
            public String visit(ConcreteStringDescription d) {
                return d.getString();
            }

            @Override
            public String visit(PrefixStringDescription prefixStringDescription) {
                return null;
            }

            @Override
            public String visit(ObjectDescription d) {
                return null;
            }
        });

        if (varOrProp == null || "this".equals(varOrProp) || varOrProp.startsWith("TAJS_")) {
            // varOrProp is not a constant string... (likely a numeric property)
            // TODO add support for this case?
            // not checking value of the 'this' variable (it is not stored in the TAJS type map) -- it should be sufficient to verify the places 'this' is used, and at function-entries
            return null;
        }
        dk.au.cs.casa.jer.entries.SourceLocation sourceLocation = e.getSourceLocation();

        // NB not during any matching of contexts, it might be the case that the state in a context is imprecise enough to cover all the values of the concrete contexts!

        Collection<Value> values = getValuesForAllContexts(sourceLocation, varOrProp);

        //HACK
        values.addAll(getValuesForAllContexts(sourceLocation, "null" /* TAJS handles DPAs in this way... */));

        boolean someMatch = values.stream().anyMatch(v -> isAbstractValueSound(e.getValueDescription(), v));
        ValueCheck check = new ValueCheck(e.getSourceLocation(), String.format("var/prop %s", varOrProp), e.getValueDescription(), values, !someMatch);
        checks.add(check);
        if (check.isFailure() && isNativeObject(e.getBase()) && values.stream().allMatch(v -> ((v.isMaybeUndef() && !v.isMaybeOtherThanUndef()) || (v.isMaybeAbsent() && !v.isNotAbsent())))) {
            check.setFailureKind(SoundnessCheck.FailureKind.MISSING_NATIVE_PROPERTY);
        }
        return null;
    }

    private boolean isNativeObject(ValueDescription v) {
        return v != null && v.accept(new ValueDescriptionVisitor<Boolean>() {
            @Override
            public Boolean visit(OtherDescription otherDescription) {
                return true;
            }

            @Override
            public Boolean visit(ConcreteStringDescription concreteStringDescription) {
                return false;
            }

            @Override
            public Boolean visit(PrefixStringDescription prefixStringDescription) {
                return false;
            }

            @Override
            public Boolean visit(ObjectDescription objectDescription) {
                return objectDescription.accept(new ObjectDescriptionVisitor<Boolean>() {
                    @Override
                    public Boolean visit(OtherObjectDescription otherObjectDescription) {
                        return true;
                    }

                    @Override
                    public Boolean visit(AllocationSiteObjectDescription allocationSiteObjectDescription) {
                        return false;
                    }

                    @Override
                    public Boolean visit(BuiltinObjectDescription builtinObjectDescription) {
                        return true;
                    }

                    @Override
                    public Boolean visit(OtherSymbolDescription otherSymbolDescription) {
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public Void visit(CallEntry e) {
        // syntactic ambiguity for `f().g()`: both calls occur at `f`...
        // Strategy: fetch all callnodes, fail if they are *all* unsound

        Set<CallNode> cns = getNodes(e.getSourceLocation(), CallNode.class);
        boolean isTAJSFunction = cns.stream().anyMatch(n -> n.getTajsFunctionName() != null);
        if (!isTAJSFunction) {
            checks.add(new CallCheck(e.getSourceLocation(), cns.isEmpty()));
        }

        boolean isCall;
        boolean isApply;
        if (isTAJSFunction) {
            isCall = false;
            isApply = false;
        } else {
            ValueDescription dynamicCallee = e.getFunction();
            Set<Value> staticCallees = getCallees(cns);

            isCall = staticCallees.stream()
                    .anyMatch(staticCallee -> staticCallee.getObjectLabels().stream()
                            .anyMatch(l -> l.isHostObject() && l.getHostObject() == ECMAScriptObjects.FUNCTION_CALL));
            isApply = staticCallees.stream()
                    .anyMatch(staticCallee -> staticCallee.getObjectLabels().stream()
                            .anyMatch(l -> l.isHostObject() && l.getHostObject() == ECMAScriptObjects.FUNCTION_APPLY));

            ValueDescription dynamicReceiver = e.getBase();
            Set<Value> staticReceivers = getReceivers(cns, isCall || isApply);

            boolean foundCalleeMatch = staticCallees.stream().anyMatch(staticCallee -> isAbstractValueSound(dynamicCallee, staticCallee));
            boolean foundReceiverMatch = staticReceivers.stream().anyMatch(staticReceiver -> isAbstractValueSound(dynamicReceiver, staticReceiver));

            if (!isCall && !isApply /* XXX skipping the checks for call and apply, they seem to behave weirdly */) {
                checks.add(new ValueCheck(e.getSourceLocation(), "callee", dynamicCallee, staticCallees, !foundCalleeMatch));
                checks.add(new ValueCheck(e.getSourceLocation(), "call receiver", dynamicReceiver, staticReceivers, !foundReceiverMatch));
            }
        }

        List<ValueDescription> dynamicArguments = e.getArguments();
        Set<List<Value>> staticArguments = getArguments(cns, isCall || isApply);
        if (!isCall && !isApply  /* XXX skipping the checks for call and apply, they seem to behave weirdly */) {
            compareArgumentsLists(staticArguments, dynamicArguments, e.getSourceLocation());
        }

        //Check arguments
        cns.stream().forEach(cn -> {
            BasicBlock block = cn.getBlock();
            // XXX skipping these checks since call/apply is skipped anyway (the checks might need an update as well)
            //
            if (true) {
                return;
            }

            List<Value> staticArgumentsCall = newList();
            List<Value> staticArgumentsApply = newList();

            for (int i = 0; i < cn.getNumberOfArgs(); i++) {
                Value statArg = readRegister(block, cn.getArgRegister(i));
                if (isCall && i > 0) {
                    staticArgumentsCall.add(statArg);
                }
            }
            if (isApply && cn.getNumberOfArgs() > 1) {
                int statArgRegister = cn.getArgRegister(1);
                Value argArrayLengthValue = readProperty(cn, statArgRegister, StringPKey.make("length"), AbstractNode.NO_VALUE);
                if (argArrayLengthValue.isMaybeSingleNum() && !argArrayLengthValue.isMaybeOtherThanNum()) {
                    int argArrayLength = argArrayLengthValue.getNum().intValue();
                    for (int i = 0; i < argArrayLength; i++) {
                        staticArgumentsApply.add(readProperty(cn, statArgRegister, StringPKey.make("" + i), AbstractNode.NO_VALUE));
                    }
                } else {
                    return; // TODO support unknown length of arguments array to .apply
                }
            }

            if (isCall) {
                compareArgumentsLists(singleton(staticArgumentsCall), dynamicArguments, e.getSourceLocation());
            }

            if (isApply) {
                compareArgumentsLists(singleton(staticArgumentsApply), dynamicArguments, e.getSourceLocation());
            }
        });

        return null;
    }

    private Set<List<Value>> getArguments(Set<CallNode> cns, boolean isCallOrApply) {
        return cns.stream().map(cn -> {
            BasicBlock block = cn.getBlock();
            List<Value> staticArguments = newList();
            for (int i = 0; i < cn.getNumberOfArgs(); i++) {
                int argRegister = cn.getArgRegister(i);
                Value statArg;
                if (argRegister != AbstractNode.NO_VALUE) {
                    statArg = readRegister(block, argRegister);
                } else {
                    statArg = Value.makeNone();
                }
                staticArguments.add(statArg);
            }
            return staticArguments;
        }).collect(Collectors.toSet());
    }

    private Set<Value> getReceivers(Set<CallNode> cns, boolean isCallOrApply) {
        return cns.stream().map(cn -> {
            BasicBlock block = cn.getBlock();
            int staticBaseRegister = cn.getBaseRegister();
            if (staticBaseRegister == AbstractNode.NO_VALUE) {
                return Value.makeObject(InitialStateBuilder.GLOBAL).joinUndef(); //TODO: see #473
            } else {
                Value staticBase = readRegister(block, staticBaseRegister);
                if (staticBase.getObjectLabels().stream().allMatch(l -> l.getKind() == ObjectLabel.Kind.ACTIVATION)) {
                    staticBase = staticBase.restrictToNotObject().joinObject(InitialStateBuilder.GLOBAL); // XXX is this sufficient?
                }

                if (isCallOrApply && cn.getNumberOfArgs() > 0) {
                    staticBase = staticBase.join(readRegister(block, cn.getArgRegister(0)));
                }

                // the valuelogger provides unboxed values for call bases - unbox the TAJS value if possible
                staticBase = staticBase.join(getInnerValueIfBoxed(block, staticBase));

                if (staticBase.getObjectLabels().stream().anyMatch((o) ->  o == InitialStateBuilder.GLOBAL)) { //TODO: see #473
                    staticBase = staticBase.joinUndef();
                }


                return staticBase;
            }
        }).collect(Collectors.toSet());
    }

    private Set<Value> getCallees(Set<CallNode> cns) {
        return cns.stream()
                .map(cn -> {
                    BasicBlock block = cn.getBlock();
                    int functionRegister = cn.getFunctionRegister();
                    if (functionRegister != AbstractNode.NO_VALUE) {
                        return readRegister(block, functionRegister);
                    } else if (cn.getPropertyString() != null || cn.getPropertyRegister() != AbstractNode.NO_VALUE) {
                        StringPKey k = cn.getPropertyString() != null ? StringPKey.make(cn.getPropertyString()) : null;
                        return readProperty(cn, cn.getBaseRegister(), k, cn.getPropertyRegister());
                    } else {
                        throw new AnalysisException("Unhandled CallNode case!?!");
                    }
                })
                .filter(v -> !v.isNone())
                .collect(Collectors.toSet());
    }

    private boolean isGlobalObject(ValueDescription dynBase) {
        return dynBase.accept(new ValueDescriptionVisitor<Boolean>() {

            @Override
            public Boolean visit(OtherDescription d) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteStringDescription d) {
                return false;
            }

            @Override
            public Boolean visit(PrefixStringDescription d) {
                return false;
            }

            @Override
            public Boolean visit(ObjectDescription d) {
                return d.accept(new ObjectDescriptionVisitor<Boolean>() {

                    @Override
                    public Boolean visit(OtherObjectDescription o) {
                        return false;
                    }

                    @Override
                    public Boolean visit(AllocationSiteObjectDescription o) {
                        return false;
                    }

                    @Override
                    public Boolean visit(BuiltinObjectDescription o) {
                        return o.getCanonicalName().equals("<the global object>");
                    }

                    @Override
                    public Boolean visit(OtherSymbolDescription otherSymbolDescription) {
                        return false;
                    }
                });
            }
        });
    }

    private void compareArgumentsLists(Set<List<Value>> staticArgumentLists, List<ValueDescription> dynamicArgumentList, dk.au.cs.casa.jer.entries.SourceLocation sl) {
        for (int i = 0; i < dynamicArgumentList.size(); i++) {
            ValueDescription dynArg = dynamicArgumentList.get(i);
            Value statArg = Value.makeNone();

            for (List<Value> staticArgumentList : staticArgumentLists) {
                if (i >= staticArgumentList.size()) {
                    statArg = statArg.join(Value.makeNone());
                } else {
                    statArg = statArg.join(staticArgumentList.get(i));
                }
            }
            // NB: doing an argument-by-argument matching and not the full list at once!
            checks.add(new ValueCheck(sl, String.format("arguments[%d] for call", i), dynArg, statArg, !isAbstractValueSound(dynArg, statArg)));
        }
    }

    private boolean isObjectDescription(ValueDescription value) {
        return value.accept(new ValueDescriptionVisitor<Boolean>() {
            @Override
            public Boolean visit(OtherDescription d) {
                return false;
            }

            @Override
            public Boolean visit(ConcreteStringDescription d) {
                return false;
            }

            @Override
            public Boolean visit(PrefixStringDescription prefixStringDescription) {
                return false;
            }

            @Override
            public Boolean visit(ObjectDescription d) {
                return true;
            }
        });
    }

    private Value getInnerValueIfBoxed(BasicBlock block, Value value) {
        Value result = value;

        boolean isBoxedPrimitive = value.getObjectLabels().stream().anyMatch(objLabel ->
                (objLabel.getKind() == ObjectLabel.Kind.NUMBER ||
                        objLabel.getKind() == ObjectLabel.Kind.STRING ||
                        objLabel.getKind() == ObjectLabel.Kind.BOOLEAN) &&
                        !objLabel.isHostObject());

        if (isBoxedPrimitive) {
            Map<Context, State> states;
            states = c.getAnalysisLatticeElement().getStates(block);
            List<Value> values = states.values().stream().map(s -> {
                //Set<ObjectLabel> baseObjects = s.readRegister(value).getObjectLabels();
                return UnknownValueResolver.getRealValue(s.readInternalValue(value.getObjectLabels()), s);
            }).collect(Collectors.toList());
            result = Value.join(values);
        }
        if (log.isDebugEnabled()) {
            log.debug(String.format("getInnerValueIfBoxed(%s, %s) = %s", block, value, result));
        }
        return result;
    }

    private Value readProperty(AbstractNode node, int base, PKey propName, int propertyNameRegister) {
        Map<Context, State> states = c.getAnalysisLatticeElement().getStates(node.getBlock());
        List<Value> values = states.values().stream().map(s -> {
            Value result = c.withStateAndNode(s, node, () -> {
                Set<ObjectLabel> baseObjects = Conversion.toObject(node, UnknownValueResolver.getRealValue(s.readRegister(base), s), c).getObjectLabels();
                Set<Value> allPropertyNames = singleton(propName != null ? propName.toValue() : Conversion.toString(UnknownValueResolver.getRealValue(s.readRegister(propertyNameRegister), s), c));
                Set<Value> propertyValues = allPropertyNames.stream()
                        .map(name -> {
                            Value value = c.getAnalysis().getPropVarOperations().readPropertyValue(baseObjects, name);
                            value = UnknownValueResolver.getRealValue(value, c.getState());
                            return value;
                        }).collect(Collectors.toSet());
                return Value.join(propertyValues);
            });
            return result;
        }).collect(Collectors.toList());
        Value result = Value.join(values);
        if (log.isDebugEnabled()) {
            if (propName != null) {
                log.debug(String.format("readProperty(%s, %d, %s) = %s", node.getBlock(), base, propName, result));
            } else {
                log.debug(String.format("readProperty(%s, %d, %d) = %s", node.getBlock(), base, propertyNameRegister, result));
            }
        }
        return result;
    }

    private Value readRegister(BasicBlock block, int register) {
        Map<Context, State> states = c.getAnalysisLatticeElement().getStates(block);
        List<Value> values = states.values().stream().map(s ->
                s.isBottom() ? Value.makeNone() : UnknownValueResolver.getRealValue(s.readRegister(register), s)
        ).collect(Collectors.toList());
        Value result = Value.join(values);
        if (log.isDebugEnabled()) {
            log.debug(String.format("readRegister(%s, %d) = %s", block, register, result));
        }
        return result;
    }

    private Value readVariable(BasicBlock block, String name) {
        Map<Context, State> states = c.getAnalysisLatticeElement().getStates(block);
        List<Value> values = states.values().stream().map(s -> c.withState(s, () ->
                UnknownValueResolver.getRealValue(c.getAnalysis().getPropVarOperations().readVariable(name, null), s)
        )
        ).collect(Collectors.toList());
        Value result = Value.join(values);
        if (log.isDebugEnabled()) {
            log.debug(String.format("readVariable(%s, %s) = %s", block, name, result));
        }
        return result;
    }

    private Value readThis(BasicBlock block) {
        Map<Context, State> states = c.getAnalysisLatticeElement().getStates(block);
        List<Value> values = states.values().stream().map(s ->
                UnknownValueResolver.getRealValue(s.readThis(), s)
        ).collect(Collectors.toList());
        Value result = Value.join(values);
        if (log.isDebugEnabled()) {
            log.debug(String.format("readThis(%s) = %s", block, result));
        }
        return result;
    }

    /**
     * @see #getNode(dk.au.cs.casa.jer.entries.SourceLocation, Class, Predicate)
     */
    private <T extends AbstractNode> Optional<T> getNode(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, Class<T> class1) {
        return getNode(sourceLocation, class1, (n) -> true);
    }

    /**
     * Finds a single (optional) nodes of a particular class at some source location that also satisfies a filter
     */
    private <T extends AbstractNode> Optional<T> getNode(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, Class<T> class1, Predicate<T> extraFilter) {
        final Predicate<T> coreFilter = t -> {
            // remove duplicates
            // NB: this means that some nodes will be ignored which might cause spurious unsoundness failures! In particular, the exceptional finally flow is ignored.
            if (t.getDuplicateOf() != null) {
                return false;
            }

            // remove custom
            if (!extraFilter.test(t)) {
                return false;
            }

            return true;
        };

        Set<T> nodes = getNodes(sourceLocation, class1, coreFilter);

        if (nodes.isEmpty()) {
            return Optional.empty();
        } else if (nodes.size() > 1 && !Options.get().getSoundnessTesterOptions().isDoNotCheckAmbiguousNodeQueries()) {
            throw new AnalysisException(String.format("Ambiguous node query for %s at %s: %s. Add a custom filter?", class1.getSimpleName(), sourceLocation, nodes));
        }
        return nodes.stream().findFirst();
    }

    /**
     * @see #getNodes(dk.au.cs.casa.jer.entries.SourceLocation, Class, Predicate)
     */
    private <T extends AbstractNode> Set<T> getNodes(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, Class<T> class1) {
        return getNodes(sourceLocation, class1, (n) -> true);
    }

    /**
     * Finds all nodes of a particular class at some source location that also satisfies a filter
     */
    private <T extends AbstractNode> Set<T> getNodes(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, Class<T> class1, Predicate<T> filter) {
        Set<AbstractNode> candidates = loc2nodes.getOrDefault(class1, newMap()).getOrDefault(ValueLoggerSourceLocationMapper.makeTAJSSourceLocation(sourceLocation), newSet());
        @SuppressWarnings("unchecked")
        Set<T> typedCandidates = (Set<T>) candidates /* Safe due to the class-check-filter above */;
        return typedCandidates.stream().filter(filter).collect(Collectors.toSet());
    }

    @Override
    public Void visit(FunctionExitEntry e) {
        if (true) {
            // XXX find out why TAJS does not provide the return/exception value in the code below (always undefined or none)
            return null;
        }

        ValueDescription dynReturnValue = e.getReturnValue();
        ValueDescription dynException = e.getException();

        //TAJS takes the sl of the return at the last } in the function
        //JALANGI takes it at the end.
        AbstractNode nodeInSameBlock = null; // XXX
        Function function = nodeInSameBlock.getBlock().getFunction();
        if (dynException != null) {
            BasicBlock block = function.getExceptionalExit();
            Value staticException = readRegister(block, AbstractNode.EXCEPTION_REG);
            checks.add(new ValueCheck(e.getSourceLocation(), "exception-value", dynException, staticException, !isAbstractValueSound(dynException, staticException)));
        } else {
            BasicBlock block = function.getOrdinaryExit();
            Value staticReturnValue = readRegister(block, AbstractNode.RETURN_REG);
            boolean isSound = isAbstractValueSound(dynReturnValue, staticReturnValue) || (staticReturnValue.isNone());
            //assumes sound when staticReturnValue is no value, since valuelogger do not give correct return value.
            checks.add(new ValueCheck(e.getSourceLocation(), "return-value", dynReturnValue, staticReturnValue, !isSound));
        }
        return null;
    }

    @Override
    public Void visit(FunctionEntry e) {
        Optional<ConstantNode> nodeOpt = getNode(e.getSourceLocation(), ConstantNode.class, (n) -> {
            boolean isRequireWrapper = Options.get().isNodeJS()
                    && n.getBlock().getFunction().toString().equals("function(exports,require,module,__filename,__dirname)")
                    && n.getSourceLocation().getLineNumber() == 1 && n.getSourceLocation().getColumnNumber() == 1;
            boolean isMainFunction = n.getBlock().getFunction().isMain();
            return !isMainFunction && !isRequireWrapper;
        });
        checks.add(new DataflowCheck(e.getSourceLocation(), "function-entry", !nodeOpt.isPresent()));

        if (!nodeOpt.isPresent()) {
            return null;
        }

        BasicBlock block = nodeOpt.get().getBlock();

        List<String> parameterNames = block.getFunction().getParameterNames();

        for (int i = 0; i < Math.min(e.getArguments().size(), parameterNames.size()); i++) {
            Value statArg = readVariable(block, parameterNames.get(i));
            ValueDescription dynArg = e.getArguments().get(i);
            checks.add(new ValueCheck(e.getSourceLocation(), String.format("arguments[%d]", i), dynArg, statArg, !isAbstractValueSound(dynArg, statArg)));
        }

        Value staticBase = readThis(block);
        ValueDescription dynBase = e.getBase();
        checks.add(new ValueCheck(e.getSourceLocation(), "receiver", dynBase, staticBase, !isAbstractValueSound(dynBase, staticBase)));

        return null;
    }

    @Override
    public Void visit(DynamicCodeEntry e) {
        return null;
    }

    public void test(Set<IEntry> entries) {
        entries.stream().forEach(e -> testEntry(this, e));
    }

    private void testEntry(EntryVisitor<Void> soundnessVisitor, IEntry e) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(String.format("SoundnessTestingVisitor#visit(%s)", e.toString()));
            }
            // System.out.println(e.getSourceLocation() + ": " + e.toString());
            e.accept(soundnessVisitor);
        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = String.format("Something went wrong while checking location %s", e.getSourceLocation().toString());
            throw new RuntimeException(msg, ex);
        }
    }

    class ValueCheck implements SoundnessCheck {

        private final SourceLocation sourceLocation;

        private final String kind;

        private final ValueDescription concreteValue;

        private final Collection<Value> abstractValues;

        private final boolean failure;

        private FailureKind failureKind = FailureKind.WRONG_VALUE;

        public ValueCheck(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, String kind, ValueDescription concreteValue, Value abstractValue, boolean failure) {
            this(sourceLocation, kind, concreteValue, singleton(abstractValue), failure);
        }

        public ValueCheck(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, String kind, ValueDescription concreteValue, Collection<Value> abstractValues, boolean failure) {
            this.sourceLocation = ValueLoggerSourceLocationMapper.makeTAJSSourceLocation(sourceLocation);
            this.kind = kind;
            this.concreteValue = concreteValue;
            this.abstractValues = newSet(abstractValues);
            this.abstractValues.remove(Value.makeNone());
            this.failure = failure;
        }

        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        @Override
        public String getMessage() {
            Collection<String> abstractValueStrings = abstractValues.stream().map(v -> {
                Set<SourceLocation> locs = v.getObjectSourceLocations();
                return locs.isEmpty() ? v.toString() : v + "@" + locs;
            }).collect(Collectors.toList());
            return String.format("Value mismatch for %s. Concrete: %s. Abstract: %s.", kind, concreteValue, abstractValueStrings); // XXX do *not* move this computation to the constructor. It can be very expensive!
        }

        @Override
        public boolean isFailure() {
            return failure;
        }

        @Override
        public boolean hasDataFlow() {
            return !(abstractValues.isEmpty() || (abstractValues.size() == 1 && abstractValues.iterator().next().isNone()));
        }

        @Override
        public FailureKind getFailureKind() {
            return failureKind;
        }

        public void setFailureKind(FailureKind failureKind) {
            this.failureKind = failureKind;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ValueCheck that = (ValueCheck) o;

            if (failure != that.failure) return false;
            if (!Objects.equals(sourceLocation, that.sourceLocation)) return false;
            if (!Objects.equals(kind, that.kind)) return false;
            if (!Objects.equals(concreteValue, that.concreteValue)) return false;
            if (!Objects.equals(abstractValues, that.abstractValues)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = sourceLocation != null ? sourceLocation.hashCode() : 0;
            result = 31 * result + (kind != null ? kind.hashCode() : 0);
            result = 31 * result + (concreteValue != null ? concreteValue.hashCode() : 0);
            result = 31 * result + (abstractValues != null ? abstractValues.hashCode() : 0);
            result = 31 * result + (failure ? 1 : 0);
            return result;
        }
    }

    private class DataflowCheck extends SoundnessCheckImpl {

        public DataflowCheck(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, String description, boolean failure) {
            super(sourceLocation, String.format("Missing dataflow for %s", description), failure);
        }

        @Override
        public boolean hasDataFlow() {
            return !isFailure();
        }

        @Override
        public FailureKind getFailureKind() {
            return FailureKind.MISSING_DATAFLOW;
        }
    }

    private class CallCheck extends SoundnessCheckImpl {

        public CallCheck(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation, boolean failure) {
            super(sourceLocation, "No call at source location", failure);
        }

        @Override
        public boolean hasDataFlow() {
            return isFailure();
        }

        @Override
        public FailureKind getFailureKind() {
            return FailureKind.MISSING_CALL;
        }
    }
}
