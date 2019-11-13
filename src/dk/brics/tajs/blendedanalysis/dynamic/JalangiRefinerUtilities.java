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

package dk.brics.tajs.blendedanalysis.dynamic;

import dk.au.cs.casa.jer.entries.AllocationSiteObjectDescription;
import dk.au.cs.casa.jer.entries.BuiltinObjectDescription;
import dk.au.cs.casa.jer.entries.CallEntry;
import dk.au.cs.casa.jer.entries.ConcreteStringDescription;
import dk.au.cs.casa.jer.entries.DynamicCodeEntry;
import dk.au.cs.casa.jer.entries.EntryVisitor;
import dk.au.cs.casa.jer.entries.FunctionEntry;
import dk.au.cs.casa.jer.entries.FunctionExitEntry;
import dk.au.cs.casa.jer.entries.IEntry;
import dk.au.cs.casa.jer.entries.ModuleExportsEntry;
import dk.au.cs.casa.jer.entries.ObjectDescription;
import dk.au.cs.casa.jer.entries.ObjectDescriptionVisitor;
import dk.au.cs.casa.jer.entries.OtherDescription;
import dk.au.cs.casa.jer.entries.OtherObjectDescription;
import dk.au.cs.casa.jer.entries.OtherSymbolDescription;
import dk.au.cs.casa.jer.entries.PrefixStringDescription;
import dk.au.cs.casa.jer.entries.ValueDescription;
import dk.au.cs.casa.jer.entries.ValueDescriptionVisitor;
import dk.au.cs.casa.jer.entries.VariableOrPropertyEntry;
import dk.brics.tajs.analysis.HostAPIs;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.dom.DOMObjects;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.lattice.HostObject;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.soundness.LogFileHelper;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static dk.brics.tajs.monitoring.soundness.ValueLoggerSourceLocationMapper.makeTAJSSourceLocation;
import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * Compare Jalangi value logs with TAJS values for blended analysis.
 */
public class JalangiRefinerUtilities {

    private static Logger log = Logger.getLogger(JalangiRefinerUtilities.class);

    private Map<SourceLocation, Set<VariableOrPropertyEntry>> varOrPropEntriesMap;

    private Map<SourceLocation, Set<CallEntry>> callEntriesMap;

    private Map<SourceLocation, Set<DynamicCodeEntry>> dynamicCodeEntriesMap;

    private Map<String, HostObject> string2hostObject = newMap();

    private Set<HostObject> registeredHostObjects;

    private Solver.SolverInterface c;

    private FlowGraph flowGraph;

    private Set<String> tajsVariablesNotJalangiVariables = Stream.of("Array", "RegExp", "undefined", "Infinity", "NaN").collect(Collectors.toSet());

    public JalangiRefinerUtilities() {
        loadLogFile();
    }

    public void setSolverInterface(Solver.SolverInterface c) {
        this.c = c;
        this.flowGraph = c.getFlowGraph();
    }

    private void loadLogFile() {
        varOrPropEntriesMap = newMap();
        callEntriesMap = newMap();
        dynamicCodeEntriesMap = newMap();
        LogFileHelper logFileHelper = new LogFileHelper();
        Set<IEntry> entries = LogFileHelper.makeLogParser(logFileHelper.createOrGetLogFile()).getEntries();

        entries.forEach(e ->
                e.accept(new EntryVisitor<Void>() {
                    @Override
                    public Void visit(VariableOrPropertyEntry e) {
                        addToMapSet(varOrPropEntriesMap, makeTAJSSourceLocation(e.getSourceLocation()), e);
                        return null;
                    }

                    @Override
                    public Void visit(CallEntry e) {
                        addToMapSet(callEntriesMap, makeTAJSSourceLocation(e.getSourceLocation()), e);
                        return null;
                    }

                    @Override
                    public Void visit(FunctionExitEntry functionExitEntry) {
                        return null;
                    }

                    @Override
                    public Void visit(FunctionEntry functionEntry) {
                        return null;
                    }

                    @Override
                    public Void visit(DynamicCodeEntry e) {
                        SourceLocation sl = makeTAJSSourceLocation(e.getSourceLocation());
                        // DynamicCodeEntry are used for eval, but at the argument position instead of the eval call-site.
                        // We assume the argument and eval call-site are on the same line.
                        SourceLocation slWithoutColumnNumber = new SourceLocation.StaticLocationMaker(sl.getLocation()).make(sl.getLineNumber(), 0, 0, 0);
                        addToMapSet(dynamicCodeEntriesMap, slWithoutColumnNumber, e);
                        return null;
                    }

                    @Override
                    public Void visit(ModuleExportsEntry moduleExportsEntry) {
                        return null;
                    }
                }));

        List<HostObject> allHostObjects = newList();
        allHostObjects.addAll(Arrays.asList(ECMAScriptObjects.values()));
        allHostObjects.addAll(Arrays.asList(DOMObjects.values()));
        allHostObjects.forEach(e -> string2hostObject.put(e.toString(), e));

        registeredHostObjects = newSet();
        InputStream stream = JalangiRefinerUtilities.class.getResourceAsStream("/RegisteredHostObjectsInValueLogger.txt");
        Stream<String> lines = new BufferedReader(new InputStreamReader(stream)).lines();
        List<BuiltinObjectDescription> builtIns = lines.map(BuiltinObjectDescription::new).collect(Collectors.toList());
        registeredHostObjects.addAll(builtIns.stream().map(e -> getHostObject(e.getCanonicalName())).filter(Objects::nonNull).collect(Collectors.toList()));

    }

    private HostObject getHostObject(String canonicalName) {
        return string2hostObject.get(canonicalName);
    }

    public Set<CallEntry> getCallEntriesAtSourceLocation(SourceLocation sourceLocation) {
        return getEntriesFromMap(callEntriesMap, sourceLocation, 0);
    }

    public Set<VariableOrPropertyEntry> getVarOrPropEntriesAtSourceLocation(SourceLocation sourceLocation, int columnOffset) {
        return getEntriesFromMap(varOrPropEntriesMap, sourceLocation, 0/*columnOffset*/);
    }

    public boolean hasDynamicCodeEntriesAtSourceLocation(SourceLocation sl) {
        SourceLocation slWithoutColumnNumber = new SourceLocation.StaticLocationMaker(sl.getLocation()).make(sl.getLineNumber(), 0, 0, 0);
        return dynamicCodeEntriesMap.containsKey(slWithoutColumnNumber);
    }

    public Value getCodeFromDynamicCodeEntryAtSourceLocation(SourceLocation sl) {
        SourceLocation slWithoutColumnNumber = new SourceLocation.StaticLocationMaker(sl.getLocation()).make(sl.getLineNumber(), 0, 0, 0);
        return Value.makeStrings(dynamicCodeEntriesMap.getOrDefault(slWithoutColumnNumber, newSet()).stream().map(e -> e.getCode()).collect(Collectors.toSet()));
    }

    public <E> Set<E> getEntriesFromMap(Map<SourceLocation, Set<E>> map, SourceLocation sourceLocation, int columnOffset) {
        Set<SourceLocation> jalangiAliases = flowGraph.getValueLogLocationInformation().getJalangiLocations(sourceLocation);
        return jalangiAliases.stream().map(sl -> addOffsetToTAJSLocation(sl, columnOffset)).flatMap(sl -> map.getOrDefault(sl, newSet()).stream()).collect(Collectors.toSet());
    }

    private SourceLocation addOffsetToTAJSLocation(SourceLocation sourceLocation, int columnOffset) {
        return new SourceLocation.StaticLocationMaker(sourceLocation.getLocation()).make(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber() + columnOffset, 0, 0);
    }

    public boolean valueDescriptionMatchesValue(ValueDescription valueDescription, Value tajsValue) {
        Value jalangiValue = valueDescription.accept(getConvertLogValuesToTAJSValuesVisitor(tajsValue));
        return !meetWithUnboxing(jalangiValue, tajsValue).isNone();
    }

    public Value convertValueDescriptionToTAJSValue(ValueDescription valueDescription, Value soundDefault) {
        return valueDescription.accept(getConvertLogValuesToTAJSValuesVisitor(soundDefault));
    }

    public ValueDescriptionVisitor<Value> getConvertLogValuesToTAJSValuesVisitor(Value soundDefault) {
        return new ValueDescriptionVisitor<Value>() {
            @Override
            public Value visit(OtherDescription otherDescription) {
                return getTAJSValueFromJalangiValue(otherDescription.getDescription());
            }

            @Override
            public Value visit(ConcreteStringDescription concreteStringDescription) {
                return Value.makeStr(concreteStringDescription.getString());
            }

            @Override
            public Value visit(PrefixStringDescription prefixStringDescription) {
                return Value.makeNone().joinPrefix(prefixStringDescription.getString());
            }

            @Override
            public Value visit(ObjectDescription objectDescription) {
                return objectDescription.accept(new ObjectDescriptionVisitor<Value>() {
                    @Override
                    public Value visit(OtherObjectDescription otherObjectDescription) {
                        Set<ObjectLabel> objectLabels = soundDefault.getObjectLabels();

                        Set<ObjectLabel> filteredObjectLabels = objectLabels.stream()
                                .filter(objLabel -> {
                                    if (flowGraph.isHostEnvironmentSource(objLabel.getSourceLocation()) // isHostEnvironmentSource
                                            || objLabel.isHostObject() && !registeredHostObjects.contains(objLabel.getHostObject()) // isHostObjectNotKnownByJalangi
                                            || objLabel.getSourceLocation().getLoaderLocation() != null // definedInDynamicCode
                                    ) {
                                        // The origin of the object in Jalangi and TAJS are uncomparable
                                        return true;
                                    }
                                    Stream<ObjectLabel.Kind> uncomparableObjectKinds = Stream.of(ObjectLabel.Kind.ARGUMENTS, ObjectLabel.Kind.ERROR, ObjectLabel.Kind.STRING, ObjectLabel.Kind.NUMBER, ObjectLabel.Kind.BOOLEAN);
                                    if (uncomparableObjectKinds.anyMatch(k -> k == objLabel.getKind())) {
                                        // The objects might have been allocated in native transfers.
                                        return true;
                                    }

                                    if (objLabel.getNode() instanceof CallNode) {
                                        boolean createdInNativeTransfer = !((CallNode) objLabel.getNode()).isConstructorCall();
                                        // If not created in native transfer, it should be an allocation-site-object, i.e., not an OtherObjectDescription.
                                        // If created in native transfer we cannot compare it with Jalangi.
                                        return createdInNativeTransfer;
                                    }

                                    return false;
                                })
                                .collect(Collectors.toSet());
                        return Value.makeObject(filteredObjectLabels);
                    }

                    @Override
                    public Value visit(AllocationSiteObjectDescription allocationSiteObjectDescription) {
                        Set<ObjectLabel> objectLabels = soundDefault.getObjectLabels();
                        SourceLocation jalangiLocation = makeTAJSSourceLocation(allocationSiteObjectDescription.getAllocationSite());

                        Set<Value> tajsObjects = objectLabels.stream()
                                .filter(obj -> {
                                    SourceLocation tajsLocation = obj.getSourceLocation();
                                    return isUserAllocatedDOMObject(allocationSiteObjectDescription, obj) || (!obj.isHostObject() &&
                                            tajsLocation.equals(jalangiLocation) ||
                                            (tajsLocation.getLoaderLocation() != null &&
                                                    tajsLocation.getLoaderLocation().equals(jalangiLocation)));
                                })
                                .map(obj -> Value.makeObject(obj))
                                .collect(Collectors.toSet());

                        return Value.join(tajsObjects);
                    }

                    @Override
                    public Value visit(BuiltinObjectDescription builtinObjectDescription) {
                        String builtinName = builtinObjectDescription.getCanonicalName();
                        HostObject host = getHostObject(builtinName);

                        Set<ObjectLabel> objectLabels = soundDefault.getObjectLabels();

                        Set<Value> tajsBuiltinObject = objectLabels.stream().filter(obj -> (obj.isHostObject() &&
                                (obj.getHostObject().equals(host))) || (host == ECMAScriptObjects.GLOBAL && obj.getKind() == ObjectLabel.Kind.ACTIVATION))
                                .map(obj -> Value.makeObject(obj))
                                .collect(Collectors.toSet());

                        Set<ObjectLabel> objLabels = newSet();

                        if (tajsBuiltinObject.isEmpty()) {
                            // TODO: Could be improved by mapping polyfill functions to their builtinName
                            objLabels = soundDefault.getObjectLabels().stream()
                                    .filter(obj -> c.getFlowGraph().isHostEnvironmentSource(obj.getSourceLocation()))
                                    .collect(Collectors.toSet());
                        }
                        return Value.join(tajsBuiltinObject).join(Value.makeObject(objLabels));
                    }

                    @Override
                    public Value visit(OtherSymbolDescription otherSymbolDescription) {
                        //TODO: We cannot leverage the dynamically observed SymbolDescription, since we abstract away the symbol names - github #513
                        return Value.makeObject(soundDefault.getSymbols());
                    }
                });
            }
        };
    }

    public Value meetWithUnboxing(Value jalangi, Value tajs) {
        //jalangi has maybe primitives, tajs has possibly boxed value
        Value meet = tajs.restrictToStrictEquals(jalangi);
        if (jalangi.isMaybeSingleStr() && meet.isNone()) {
            // if a String value comes back from Nashorn, it might have been normalized so we need to check the normalized Jalangi value also.
            Value meetEscaped = tajs.restrictToStrictEquals(Value.makeStr(escapeDoubleQuotesAndNewLines(jalangi.getStr())));
            if (!meetEscaped.isNone())
                meet = meet.join(meetEscaped).join(Value.makeStr(jalangi.getStr()));
        }
        if (!jalangi.restrictToNum().isNone() || !jalangi.restrictToBool().isNone() || (!jalangi.restrictToStr().isNone() || jalangi.isMaybeSingleStr()) && tajs.restrictToStr().isNone()) {
            // if internal value of string matches primitive, return the boxed value
            Set<ObjectLabel> meetUnboxedBoxed = tajs.getObjectLabels().stream()
                    .filter(obj -> obj.getKind() == ObjectLabel.Kind.STRING || obj.getKind() == ObjectLabel.Kind.NUMBER || obj.getKind() == ObjectLabel.Kind.BOOLEAN)
                    .map(obj -> Pair.make(obj, UnknownValueResolver.getRealValue(c.getState().readInternalValue(singleton(obj)), c.getState())))
                    .filter(pair -> !pair.getSecond().restrictToStrictEquals(jalangi).isNone() ||
                            !pair.getSecond().restrictToStrictEquals(jalangi.isMaybeSingleStr() ? Value.makeStr(escapeDoubleQuotesAndNewLines(jalangi.getStr())) : Value.makeNone()).isNone())
                            // if a String value comes back from Nashorn, it might have been normalized so we need to check the normalized Jalangi value also.
                    .map(pair -> pair.getFirst())
                    .collect(Collectors.toSet());
            return meet.join(Value.makeObject(meetUnboxedBoxed));
        }
        return meet;
    }

    private String escapeDoubleQuotesAndNewLines(String string) {
        return string
                .replace('\u2028', '\n') // need to normalize for Nashorn
                .replace('\u2029', '\n'); // need to normalize for Nashorn
    }

    private boolean isUserAllocatedDOMObject(AllocationSiteObjectDescription allocationSiteObjectDescription, ObjectLabel obj) {
        boolean isDOMObject = obj.isHostObject() && obj.getHostObject().getAPI() == HostAPIs.DOCUMENT_OBJECT_MODEL;
        return isDOMObject;
        // TODO: Could be improved by checking allocation-site against DOM allocation sites, which is collected by SoundnessTesterMonitor
    }

    private Value getTAJSValueFromJalangiValue(String concreteValue) {
        Value jalangiValue;
        if (isNumeric(concreteValue)) {
            return Value.makeNum(Double.parseDouble(concreteValue));
        }

        switch (concreteValue) {
            case "null" :
                jalangiValue = Value.makeNull();
                break;
            case "undefined" :
                jalangiValue = Value.makeUndef();
                break;
            case "true" :
                jalangiValue = Value.makeBool(true);
                break;
            case "false" :
                jalangiValue = Value.makeBool(false);
                break;
            case "NUM_UINT" :
                jalangiValue = Value.makeAnyNumUInt();
                break;
            case "NUM_NAN" :
                jalangiValue = Value.makeNumNaN();
                break;
            case "NUM_INF" :
                jalangiValue = Value.makeNumInf();
                break;
            case "NUM_OTHER" :
                jalangiValue = Value.makeAnyNumOther();
                break;
                // The rest of the cases are not produced by the valuelogger
                // anymore, but is included for compatibility with old log files
            case "STR_UINT" :
                jalangiValue = Value.makeAnyStrUInt();
                break;
            case "STR_OTHERNUM" :
                jalangiValue = Value.makeAnyNum();
                break;
            case "STR_IDENTIFIER" :
                jalangiValue = Value.makeNone().joinAnyStrIdentifier();
                break;
            case "STR_IDENTIFIERPARTS" :
                jalangiValue = Value.makeNone().joinAnyStrIdentifierParts();
                break;
            case "STR_JSON" :
                jalangiValue = Value.makeJSONStr();
                break;
            case "STR_OTHER" :
                jalangiValue = Value.makeNone().joinAnyStrOther();
                break;
            default :
                throw new RuntimeException("Unhandled Jalangi value: " + concreteValue);
        }
        return jalangiValue;
    }

    private boolean isNumeric(String concreteValue) {
        return concreteValue.matches("-?\\d+");
    }

    public boolean isHostEnvironmentSource(SourceLocation sourceLocation) {
        return c.getFlowGraph().isHostEnvironmentSource(sourceLocation);
    }

    public boolean isTAJSVariableNotJalangiVariable(String varname) {
        return tajsVariablesNotJalangiVariables.contains(varname);
    }

    public boolean isReachable(AbstractNode n) {
        if (c.getFlowGraph().isHostEnvironmentSource(n.getSourceLocation()) || n.getSourceLocation().getKind() != SourceLocation.Kind.STATIC) {
            return true;
        }
        if (n instanceof ReadVariableNode) {
            ReadVariableNode rvn = (ReadVariableNode) n;
            if (tajsVariablesNotJalangiVariables.contains(rvn.getVariableName())) {
                return true;
            }
            if (c.getAnalysis().getPropVarOperations().readVariable(rvn.getVariableName(), null, true, false).isMaybeAbsent()) {
                // If the read throws a reference error, the log does not contain an entry for it.
                return true;
            }
        }
        if (n instanceof ReadVariableNode || n instanceof WriteVariableNode || n instanceof ReadPropertyNode) {
            return !getEntriesFromMap(varOrPropEntriesMap, n.getSourceLocation(), 0).isEmpty();
        }
        if (n instanceof CallNode) {
            final CallNode.LiteralConstructorKinds kind = ((CallNode) n).getLiteralConstructorKind();
            if (kind == CallNode.LiteralConstructorKinds.ARRAY || kind == CallNode.LiteralConstructorKinds.REGEXP) {
                return true;
            }

            int offset = ((CallNode) n).isConstructorCall() ? 4 : 0; // new f() has node sourcelocation at the beginning of new, but the VarOrProp entries locations is at the beginning of f
            return !(getEntriesFromMap(callEntriesMap, n.getSourceLocation(), 0).isEmpty()
                    && getEntriesFromMap(varOrPropEntriesMap, n.getSourceLocation(), offset).isEmpty());
        }
        if (n instanceof WritePropertyNode && !((WritePropertyNode) n).isPropertyFixed()) {
            return !getEntriesFromMap(varOrPropEntriesMap, n.getSourceLocation(), 0).isEmpty();
        }
        return true; // The value log does not contain enough information to determine reachability, so using sound default.
    }
}
