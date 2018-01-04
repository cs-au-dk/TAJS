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

package dk.brics.tajs.monitoring.inspector.dataprocessing;

import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.flowgraph.jsnodes.TypeofNode;
import dk.brics.tajs.flowgraph.jsnodes.WritePropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.WriteVariableNode;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Utility-methods for matching simple source positions (file/line/column) to {@link AbstractNode}.
 */
public class SyntaxMatcher {

    Map<URL, Map<Integer, Map<Integer, Set<AbstractNode>>>> locLineColumnMap = newMap();

    private Map<Pair<String, ?>, Set<AbstractNode>> cache = newMap();

    public SyntaxMatcher(FlowGraph flowGraph) {
        flowGraph.getFunctions().stream()
                .flatMap(f -> f.getBlocks().stream())
                .flatMap(b -> b.getNodes().stream())
                .filter(n -> n.getSourceLocation().getLocation() != null)
                .forEach(n -> {
                    SourceLocation sourceLocation = n.getSourceLocation();
                    if (n instanceof TypeofNode) {
                        sourceLocation = ((TypeofNode) n).getOperandSourceLocation();
                    }
                    URL url = sourceLocation.getLocation();
                    int lineNumber = sourceLocation.getLineNumber();
                    int columnNumber = sourceLocation.getColumnNumber();
                    locLineColumnMap.putIfAbsent(url, newMap());
                    locLineColumnMap.get(url).putIfAbsent(lineNumber, newMap());
                    locLineColumnMap.get(url).get(lineNumber).putIfAbsent(columnNumber, newSet());
                    locLineColumnMap.get(url).get(lineNumber).get(columnNumber).add(n);
                });
    }

    public Set<AbstractNode> getNodeFromFixedAccessAtTAJSSourceLocation(SourceLocation location, String name) {
        Pair<String, Pair<SourceLocation, String>> cacheKey = Pair.make("getNodeFromFixedAccessAtTAJSSourceLocation", Pair.make(location, name));
        if (!cache.containsKey(cacheKey)) {
            cache.put(cacheKey, computeNodeFromFixedAccessAtTAJSSourceLocation(location, name));
        }
        return cache.get(cacheKey);
    }

    private Set<AbstractNode> computeNodeFromFixedAccessAtTAJSSourceLocation(SourceLocation location, String name) {
        URL url = location.getLocation();
        Set<AbstractNode> candidates = getNodesStartingAt(url, location.getLineNumber(), location.getColumnNumber());
        Set<AbstractNode> results = candidates.stream()
                .filter(n -> {
                    if (n instanceof ReadVariableNode) {
                        return name.equals(((ReadVariableNode) n).getVariableName());
                    } else if (n instanceof WriteVariableNode) {
                        return name.equals(((WriteVariableNode) n).getVariableName());
                    } else if (n instanceof ReadPropertyNode) {
                        return name.equals(((ReadPropertyNode) n).getPropertyString());
                    } else if (n instanceof WritePropertyNode) {
                        return name.equals(((WritePropertyNode) n).getPropertyString());
                    } else if (n instanceof TypeofNode) {
                        return name.equals(((TypeofNode) n).getVariableName());
                    }
                    return false;
                })
                .collect(Collectors.toSet());
        return results;
    }

    private Set<AbstractNode> getNodesStartingAt(URL url, int lineNumber, int columnNumber) {
        return locLineColumnMap.getOrDefault(url, newMap()).getOrDefault(lineNumber, newMap()).getOrDefault(columnNumber, newSet());
    }

    public Optional<AbstractNode> getLastEnclosingNode(URL url, int targetLine, int targetColumn) {
        // An expression can be uniquely identified by a source position by picking the expression that covers the smallest source code region that contains the source position.
        // Or put another way: the smallest subtree in an AST which contains a source position is unique (in practice).
        // NB: a range-tree yields an efficient implementation.
        Set<AbstractNode> nodesForFile = getNodesForFile(url);
        Set<AbstractNode> candidates = nodesForFile.stream()
                .filter(n -> {
                    int lineBegin = n.getSourceLocation().getLineNumber();
                    int columnBegin = n.getSourceLocation().getColumnNumber();
                    int lineEnd = n.getSourceLocation().getEndLineNumber();
                    int columnEnd = n.getSourceLocation().getEndColumnNumber();
                    boolean beginOK = targetLine == lineBegin ? columnBegin <= targetColumn : lineBegin < targetLine;
                    boolean endOK = targetLine == lineEnd ? targetColumn <= columnEnd : targetLine < lineEnd;
                    return beginOK && endOK;
                })
                .collect(Collectors.toSet());
        Optional<AbstractNode> last = candidates.stream()
                .sorted(Comparator.comparing((AbstractNode n) -> n.getSourceLocation().getEndLineNumber() - n.getSourceLocation().getLineNumber()) // prefer smallest line range
                        .thenComparing(n -> n.getSourceLocation().getEndColumnNumber() - n.getSourceLocation().getColumnNumber()) // prefer smallest column range
                        .thenComparing(
                                // in case of ambiguities due to (aux) nodes with the same source position
                                Comparator.comparing((AbstractNode n) -> !n.isArtificial()) // prefer non-artificials
                                        .thenComparing(n -> n.getBlock().getOrder()) // prefer later blocks
                                        // in case of (unexpected) overlaps
                                        .thenComparing(n -> n.getSourceLocation().getEndLineNumber()) // prefer biggest line and column
                                        .thenComparing(n -> n.getSourceLocation().getEndColumnNumber())
                                        .reversed()
                        ))
                .findFirst();
        return last;
    }

    private Set<AbstractNode> getNodesForFile(URL url) {
        return locLineColumnMap.getOrDefault(url, newMap()).values().stream()
                .flatMap(e -> e.values().stream())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<AbstractNode> getNodesForLine(URL url, int line) {
        // nodes starting on a specific line
        return getNodesForFile(url).stream()
                .filter(n -> n.getSourceLocation().getLineNumber() == line)
                .collect(Collectors.toSet());
    }
}
