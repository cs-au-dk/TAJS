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

package dk.brics.tajs.monitoring.inspector.api;

import dk.brics.inspector.api.InspectorAPI;
import dk.brics.inspector.api.model.OptionData;
import dk.brics.inspector.api.model.RelatedLocationKind;
import dk.brics.inspector.api.model.ids.ContextID;
import dk.brics.inspector.api.model.ids.FileID;
import dk.brics.inspector.api.model.ids.LocationID;
import dk.brics.inspector.api.model.ids.ObjectID;
import dk.brics.inspector.api.model.lines.Gutter;
import dk.brics.inspector.api.model.lines.LineValue;
import dk.brics.inspector.api.model.lines.LineValueKind;
import dk.brics.inspector.api.model.locations.ContextSensitiveDescribedLocation;
import dk.brics.inspector.api.model.locations.DescribedContext;
import dk.brics.inspector.api.model.locations.DescribedLocation;
import dk.brics.inspector.api.model.locations.FileDescription;
import dk.brics.inspector.api.model.values.CompositeValue;
import dk.brics.inspector.api.model.values.DescribedProperties;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKey;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.TypeCollector;
import dk.brics.tajs.monitoring.inspector.datacollection.InspectorData;
import dk.brics.tajs.monitoring.inspector.dataprocessing.ContextExpressionFilterer;
import dk.brics.tajs.monitoring.inspector.dataprocessing.DomainMapper;
import dk.brics.tajs.monitoring.inspector.dataprocessing.IDManager;
import dk.brics.tajs.monitoring.inspector.dataprocessing.LineValueComputer;
import dk.brics.tajs.monitoring.inspector.dataprocessing.SyntaxMatcher;
import dk.brics.tajs.monitoring.inspector.gutters.GutterProvider;
import dk.brics.tajs.options.ExperimentalOptions;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.solver.BlockAndContext;
import dk.brics.tajs.solver.CallGraph;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.Pair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;
import static dk.brics.tajs.util.Collections.singleton;

/**
 * The main TAJS implementation of {@link InspectorAPI}.
 */
public class TAJSInspectorAPI implements InspectorAPI {

    private static final Logger log = Logger.getLogger(TAJSInspectorAPI.class);

    private final Set<GutterProvider> gutters;

    private final Solver.SolverInterface c;

    private final IDManager idManager;

    private final SyntaxMatcher syntaxMatcher;

    private final InspectorData data;

    private final DomainMapper mapper;

    public TAJSInspectorAPI(InspectorData data, Set<GutterProvider> gutters, IDManager idManager, Solver.SolverInterface c) {
        this.data = data;
        this.gutters = gutters;
        this.idManager = idManager;
        this.mapper = new DomainMapper(idManager);
        this.syntaxMatcher = new SyntaxMatcher(c.getFlowGraph());
        this.c = c;
    }

    private Map<Pair<FileID, Integer>, Set<LineValue>> buildLineValuesMap(Map<TypeCollector.VariableSummary, Value> valueMap, IDManager idManager) {
        Map<Pair<FileID, Integer>, Set<LineValue>> map = newMap();
        Map<Pair<AbstractNode, String>, Set<Value>> contextInsensitiveMap = newMap();
        valueMap.forEach((key, value) -> {
            URL location = key.getVariableLocation().getLocation();
            if (location != null && !"null".equals(key.getVariableName())) {
                Set<AbstractNode> nodes = syntaxMatcher.getNodeFromFixedAccessAtTAJSSourceLocation(key.getVariableLocation(), key.getVariableName());
                if (nodes.size() != 1) {
                    log.warn(String.format("Need to make syntax matching more precise: %s/%s -> %d nodes", key.getVariableLocation(), key.getVariableName(), nodes.size())); // XXX fix this hackery
                    return;
                }
                AbstractNode node = nodes.iterator().next();
                FileID fileID = idManager.make(location);
                String variableName = key.getVariableName();
                Pair<FileID, Integer> mapKey = Pair.make(fileID, key.getVariableLocation().getLineNumber());

                addToMapSet(contextInsensitiveMap, Pair.make(node, key.getVariableName()), value);

                CompositeValue CompositeValue = mapper.makeCompositeValue(value);
                ContextSensitiveDescribedLocation describedLocation = mapper.makeDescribedLocation(node, key.getContext());
                addToMapSet(map, mapKey, new LineValue(LineValueKind.UNKNOWN, variableName, CompositeValue, describedLocation));
            }
        });

        contextInsensitiveMap.forEach((keys, values) -> {
            AbstractNode node = keys.getFirst();
            LineValue lineValue = new LineValue(LineValueKind.UNKNOWN, keys.getSecond(), mapper.makeCompositeValue(Value.join(values)), mapper.makeDescribedLocation(node));
            Pair<FileID, Integer> mapKey = Pair.make(idManager.make(node.getSourceLocation().getLocation()), node.getSourceLocation().getLineNumber());
            addToMapSet(map, mapKey, lineValue);
        });
        return map;
    }

    @Override
    public Set<FileID> getFileIDs() {
        return c.getFlowGraph().getFunctions().stream()
                .filter(f -> f.getSourceLocation().getLocation() != null)
                .map(f -> idManager.make(f.getSourceLocation().getLocation()))
                .collect(Collectors.toSet());
    }

    @Override
    public FileDescription getFileDescription(FileID id) {
        Pair<URL, String> fileAndPrettyFileName = getFileAndPrettyFileName(id);
        try {
            return new FileDescription(
                    id,
                    fileAndPrettyFileName.getSecond(),
                    Loader.getString(fileAndPrettyFileName.getFirst(), Charset.forName("UTF-8"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<URL, String> getFileAndPrettyFileName(FileID id) {
        SourceLocation sourceLocation = c.getFlowGraph().getFunctions().stream()
                .map(dk.brics.tajs.flowgraph.Function::getSourceLocation)
                .filter(Objects::nonNull)
                .filter(e -> {
                    FileID make = idManager.make(e.getLocation());
                    return id.equals(make);
                })
                .findFirst().get();
        return Pair.make(sourceLocation.getLocation(), sourceLocation.toUserFriendlyString(false));
    }

    @Override
    public Set<Gutter<?>> getGutters(FileID id) {
        return this.gutters.stream().flatMap(g -> g.create(idManager.resolve(id)).stream()).collect(Collectors.toSet());
    }

    @Override
    public OptionData getOptions() {
        Map<String, String> map = Options.get().getOptionValues().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() + ""));
        ExperimentalOptions.ExperimentalOptionsManager.get().getEnabled()
                .forEach(e -> map.put("XXX-ExperimentalOption:" + e.getClass().getSimpleName(), e.toString()));
        return new OptionData(map);
    }

    @Override
    public Set<LineValue> getLineValues(FileID fileID, int line) {
        return new LineValueComputer(syntaxMatcher, mapper, c).get(idManager.resolve(fileID), line);
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getAllocationLocations(ObjectID objectID) {
        ObjectLabel label = idManager.resolve(objectID);
        if (label.isHostObject()) {
            return newSet();
        }
        AbstractNode node = label.getNode() != null ? label.getNode() : label.getFunction().getNode();
        return c.getAnalysisLatticeElement().getStates(node.getBlock()).keySet().stream()
                .map(ctx -> mapper.makeDescribedLocation(node, ctx))
                .collect(Collectors.toSet());
    }

    @Override
    public DescribedProperties getObjectProperties(ObjectID objectID, LocationID locationID) {
        ObjectLabel label = idManager.resolve(objectID);
        Set<State> states = convertLocationIDToStates(locationID);

        CompositeValue array = mapper.makeCompositeValue(valueFromStates(states, state -> UnknownValueResolver.getDefaultArrayProperty(label, state)));
        CompositeValue nonArray = mapper.makeCompositeValue(valueFromStates(states, state -> UnknownValueResolver.getDefaultNonArrayProperty(label, state)));
        CompositeValue prototype = mapper.makeCompositeValue(valueFromStates(states, state -> UnknownValueResolver.getInternalPrototype(label, state, false)));
        CompositeValue internal = mapper.makeCompositeValue(valueFromStates(states, state -> UnknownValueResolver.getInternalValue(label, state, false)));
        Map<String, CompositeValue> metaProperties = newMap();
        metaProperties.put("<array>", array);
        metaProperties.put("<non-array>", nonArray);
        metaProperties.put("<prototype>", prototype);
        metaProperties.put("<internal>", internal);

        Map<PKey, Set<Value>> multiProperties = newMap();
        states.forEach(state ->
                UnknownValueResolver.getProperties(label, state)
                        .keySet().forEach(k -> {
                    Value realValue = UnknownValueResolver.getProperty(label, k, state, false);
                    addToMapSet(multiProperties, k, realValue);
                }));

        Map<String, CompositeValue> properties = multiProperties.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> mapper.makeCompositeValue(Value.join(e.getValue()))));

        return new DescribedProperties(metaProperties, properties);
    }

    private Set<State> convertLocationIDToStates(LocationID locationID) {
        Pair<AbstractNode, Context> location = idManager.resolve(locationID);
        Set<State> states;
        if (location.getSecond() != null) {
            State state = c.getAnalysisLatticeElement().getState(location.getFirst().getBlock(), location.getSecond());
            states = singleton(state);
        } else {
            // no explicit context: use all
            states = newSet(c.getAnalysisLatticeElement().getStates(location.getFirst().getBlock()).values());
        }
        return states;
    }

    private Value valueFromStates(Set<State> states, Function<State, Value> read) {
        return Value.join(states.stream().map(read).collect(Collectors.toList()));
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getCallLocations(ObjectID objectID) {
        ObjectLabel label = idManager.resolve(objectID);
        if (label.isHostObject()) {
            return newSet(); // TODO support calls to native functions
        }
        Map<BlockAndContext<Context>, Set<CallGraph.ReverseEdge<Context>>> callSources = c.getAnalysisLatticeElement().getCallGraph().getCallSources();
        BasicBlock entryBlock = label.getFunction().getEntry();
        return c.getAnalysisLatticeElement().getStates(entryBlock).entrySet().stream()
                .map(e -> new BlockAndContext<>(entryBlock, e.getKey()))
                .map(callSources::get)
                .flatMap(Set::stream)
                .map(e -> mapper.makeDescribedLocation(e.getCallNode(), e.getCallerContext()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getEventHandlerRegistrationLocations(ObjectID objectID) {
        ObjectLabel label = idManager.resolve(objectID);
        return data.eventHandlerRegistrationLocations.entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(v -> v.getObjectLabels().contains(label)))
                .map(Map.Entry::getKey)
                .map(e -> mapper.makeDescribedLocation(e.getFirst(), e.getSecond()))
                .collect(Collectors.toSet());
    }

    private Set<Pair<AbstractNode, Context>> getPredecessorNodes(Pair<AbstractNode, Context> location) {
        AbstractNode node = location.getFirst();
        List<AbstractNode> nodes = location.getFirst().getBlock().getNodes();
        int index = nodes.indexOf(node);
        if (index == 0) {
            Set<BlockAndContext<Context>> predecessors = data.propagationData.getBackwardsGraph().getOrDefault(new BlockAndContext<>(location.getFirst().getBlock(), location.getSecond()), newSet());
            return predecessors.stream()
                    .map(predecessor -> Pair.make(predecessor.getBlock().getLastNode(), predecessor.getContext())) // assuming there is only propagation from the end of a block
                    .collect(Collectors.toSet());
        }
        AbstractNode predecessorNode = nodes.get(index - 1);
        return singleton(Pair.make(predecessorNode, location.getSecond()));
    }

    private Set<Pair<AbstractNode, Context>> getSuccessorNodes(Pair<AbstractNode, Context> location) {
        AbstractNode node = location.getFirst();
        List<AbstractNode> nodes = location.getFirst().getBlock().getNodes();
        int index = nodes.indexOf(node);
        if (index == nodes.size() - 1) {
            Set<BlockAndContext<Context>> successors = data.propagationData.getForwardsGraph().getOrDefault(new BlockAndContext<>(location.getFirst().getBlock(), location.getSecond()), newSet());
            return successors.stream()
                    .map(successor -> Pair.make(successor.getBlock().getFirstNode(), successor.getContext()))
                    .collect(Collectors.toSet());
        }
        AbstractNode successorNode = nodes.get(index + 1);
        return singleton(Pair.make(successorNode, location.getSecond()));
    }

    @Override
    public Set<? extends DescribedLocation> getRelatedLocations(LocationID locationID, boolean forwards, RelatedLocationKind kind, boolean intraprocedural) {
        Pair<AbstractNode, Context> location = idManager.resolve(locationID);
        Set<? extends DescribedLocation> described;
        if (location.getSecond() == null) {
            Set<DescribedLocation> contextInsensitive = data.blockContexts.get(location.getFirst().getBlock()).stream()
                    .flatMap(context -> getRelatedLocations(Pair.make(location.getFirst(), context), forwards, kind, intraprocedural).stream())
                    .map(related -> mapper.makeDescribedLocation(related.getFirst()))
                    .collect(Collectors.toSet());
            described = contextInsensitive;
        } else {
            Set<Pair<AbstractNode, Context>> relatedLocations = getRelatedLocations(location, forwards, kind, intraprocedural);
            Set<ContextSensitiveDescribedLocation> contextSensitive = mapper.makeDescribedLocations(relatedLocations);
            described = contextSensitive;
        }
        return described;
    }

    @Override
    public Set<ObjectID> getEnclosingFunction(LocationID locationID) {
        // NB this is a weird way to get the enclosing function, but there is nothing in the state about *which* function that is currently being executed! So the result is context insensitive!
        Pair<AbstractNode, Context> location = idManager.resolve(locationID);
        dk.brics.tajs.flowgraph.Function function = location.getFirst().getBlock().getFunction();
        if (function.isMain()) {
            return newSet();
        }
        return data.allocationSiteMap.getOrDefault(function.getNode(), newSet()).stream()
                .filter(l -> l.getKind() == ObjectLabel.Kind.FUNCTION)
                .map(idManager::make)
                .collect(Collectors.toSet());
    }

    @Override
    public dk.brics.inspector.api.model.Optional<DescribedLocation> getPositionalLocationID(FileID fileID, int line, int column, Optional<ContextID> contextID) {
        URL url = idManager.resolve(fileID);
        Optional<AbstractNode> optionalNode = syntaxMatcher.getLastEnclosingNode(url, line, column);
        if (!optionalNode.isPresent()) {
            return new dk.brics.inspector.api.model.Optional<>();
        }
        AbstractNode node = optionalNode.get();
        if (!contextID.isPresent()) {
            return new dk.brics.inspector.api.model.Optional<>(mapper.makeDescribedLocation(node));
        }

        Context context = idManager.resolve(contextID.get());
        if (c.getAnalysisLatticeElement().getState(node.getBlock(), context).isBottom()) {
            return new dk.brics.inspector.api.model.Optional<>();
        }
        return new dk.brics.inspector.api.model.Optional<>(mapper.makeDescribedLocation(node, context));
    }

    @Override
    public Set<DescribedContext> getFilteredContexts(LocationID locationID, String expressionString) {
        Pair<AbstractNode, Context> location = idManager.resolve(locationID);
        Set<Context> contexts;
        if (location.getSecond() == null) {
            contexts = data.blockContexts.get(location.getFirst().getBlock());
        } else {
            contexts = singleton(location.getSecond()); // pointless?
        }

        Set<Context> filtered = new ContextExpressionFilterer(c).filter(location.getFirst(), contexts, expressionString);
        return filtered.stream().map(mapper::makeDescribedContext).collect(Collectors.toSet());
    }

    private Set<Pair<AbstractNode, Context>> getRelatedLocations(Pair<AbstractNode, Context> location, boolean forwards, RelatedLocationKind kind, boolean intraprocedural) {
        Function<Pair<AbstractNode, Context>, Set<Pair<AbstractNode, Context>>> relatedDirection = forwards ? this::getSuccessorNodes : this::getPredecessorNodes;
        Predicate<Pair<AbstractNode, Context>> predicate;
        switch (kind) {
            case NODE: {
                predicate = isOtherNode(location);
                break;
            }
            case BLOCK: {
                predicate = isOtherBlock(location);
                break;
            }
            case LINE: {
                predicate = isOtherLineInSameFile(location);
                break;
            }
            default:
                throw new UnsupportedOperationException("TODO: implement support for " + kind);
        }
        if (intraprocedural) {
            predicate = predicate.and(isSameFunction(location));
        }
        return getRelatedMatching(location, relatedDirection, predicate);
    }

    private Predicate<Pair<AbstractNode, Context>> isOtherNode(Pair<AbstractNode, Context> location) {
        return candidate -> !candidate.getFirst().equals(location.getFirst());
    }

    private Predicate<Pair<AbstractNode, Context>> isOtherBlock(Pair<AbstractNode, Context> location) {
        return candidate -> !candidate.getFirst().getBlock().equals(location.getFirst().getBlock());
    }

    private Predicate<Pair<AbstractNode, Context>> isSameFunction(Pair<AbstractNode, Context> location) {
        return candidate -> {
            boolean result = candidate.getFirst().getBlock().getFunction() == location.getFirst().getBlock().getFunction();
            return result;
        };
    }

    private Predicate<Pair<AbstractNode, Context>> isOtherLineInSameFile(Pair<AbstractNode, Context> location) {
        Predicate<Pair<AbstractNode, Context>> predicate;
        URL file = location.getFirst().getSourceLocation().getLocation();
        int line = location.getFirst().getSourceLocation().getLineNumber();
        Pair<URL, Integer> locationLine = Pair.make(file, line);
        predicate = candidate -> {
            Pair<URL, Integer> candidateLine = Pair.make(candidate.getFirst().getSourceLocation().getLocation(), candidate.getFirst().getSourceLocation().getLineNumber());
            return !candidateLine.equals(locationLine);
        };
        return predicate;
    }

    private Set<Pair<AbstractNode, Context>> getRelatedMatching(Pair<AbstractNode, Context> location, Function<Pair<AbstractNode, Context>, Set<Pair<AbstractNode, Context>>> relatedFetcher, Predicate<Pair<AbstractNode, Context>> predicate) {
        Set<Pair<AbstractNode, Context>> visited = newSet();
        Stack<Pair<AbstractNode, Context>> worklist = new Stack<>();
        Set<Pair<AbstractNode, Context>> matching = newSet();
        worklist.addAll(relatedFetcher.apply(location));
        while (!worklist.isEmpty()) {
            Pair<AbstractNode, Context> current = worklist.pop();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            if (predicate.test(current)) {
                matching.add(current); // terminal
            } else {
                worklist.addAll(relatedFetcher.apply(current)); // extend search radius
            }
        }
        return matching;
    }
}
