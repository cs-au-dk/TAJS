package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.PathAndURLUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

import static dk.brics.tajs.util.Collections.addToMapSet;
import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Test for enforcing the allowed package dependencies.
 * See /README.md and /misc/package-dependencies.png
 */
@RunWith(Parameterized.class)
public class PackageDependencyTest {

    static final private boolean DEBUG = false;

    @Parameterized.Parameter
    public Dependency antiDependency;

    @Parameterized.Parameters(name = "{0}")
    public static List<Dependency> data() {
        Graph<TAJSPackage> graph = makeGraph();
        Set<TAJSPackage> froms = graph.getNodes();
        List<Dependency> antiDependencyPairs = froms.stream()
                .flatMap(from -> Stream.concat(
                        graph.getAllUnreachableNodes(from).stream().map(to -> new Dependency(Dependency.Kind.TRANSITIVE, from, to)),
                        graph.getAllUnreachableNodesInOneStep(from).stream().map(to -> new Dependency(Dependency.Kind.NEIGHBOUR, from, to))
                ))
                .sorted(Comparator.comparing((Dependency d) -> d.kind).thenComparing((Dependency d) -> d.from.getLastName()).thenComparing((Dependency d) -> d.to.getLastName()))
                .collect(Collectors.toList());
        return antiDependencyPairs;
    }

    /**
     * Builds the graph that can be seen in /package-dependencies.png
     */
    private static Graph<TAJSPackage> makeGraph() {
        Graph<TAJSPackage> graph = new Graph<>();
        graph.addNode(TAJSPackage.analysis);
        graph.addNode(TAJSPackage.unevalizer);
        graph.addNode(TAJSPackage.monitoring);
        graph.addNode(TAJSPackage.lattice);
        graph.addNode(TAJSPackage.solver);
        graph.addNode(TAJSPackage.js2flowgraph);
        graph.addNode(TAJSPackage.flowgraph);
        graph.addNode(TAJSPackage.util);
        graph.addNode(TAJSPackage.options);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.monitoring);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.lattice);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.solver);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.js2flowgraph);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.util);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.options);
        graph.addEdge(TAJSPackage.analysis, TAJSPackage.unevalizer);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.analysis);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.monitoring);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.lattice);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.solver);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.js2flowgraph);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.util);
        graph.addEdge(TAJSPackage.unevalizer, TAJSPackage.options);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.analysis);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.lattice);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.solver);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.util);
        graph.addEdge(TAJSPackage.monitoring, TAJSPackage.options);
        graph.addEdge(TAJSPackage.lattice, TAJSPackage.solver);
        graph.addEdge(TAJSPackage.lattice, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.lattice, TAJSPackage.util);
        graph.addEdge(TAJSPackage.lattice, TAJSPackage.options);
        graph.addEdge(TAJSPackage.solver, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.solver, TAJSPackage.util);
        graph.addEdge(TAJSPackage.solver, TAJSPackage.options);
        graph.addEdge(TAJSPackage.js2flowgraph, TAJSPackage.flowgraph);
        graph.addEdge(TAJSPackage.js2flowgraph, TAJSPackage.util);
        graph.addEdge(TAJSPackage.js2flowgraph, TAJSPackage.options);
        graph.addEdge(TAJSPackage.flowgraph, TAJSPackage.util);
        graph.addEdge(TAJSPackage.flowgraph, TAJSPackage.options);
        graph.addEdge(TAJSPackage.util, TAJSPackage.options);
        graph.addEdge(TAJSPackage.options, TAJSPackage.util);
        return graph;
    }

    /**
     * Tests if a forbidden dependency is reachable from a specific package. Explains all failures.
     */
    private static Optional<String> testAntiDependency(TAJSPackage target, TAJSPackage antiDependency) {
        try {
            System.out.println("testing that " + target + " does not depend on " + antiDependency);
            ProcessBuilder pb = new ProcessBuilder();
            String antiDependencyPattern = String.format("%s.*", antiDependency.getCanonicalName());
            String targetPattern = String.format("%s.*", target.getCanonicalName());
            String classpath = System.getProperty("java.class.path");
            pb.command(Arrays.asList("jdeps", "-cp", classpath, "-e", antiDependencyPattern, "-v", "-include", targetPattern));
            if (DEBUG) {
                System.out.println(String.join(" ", pb.command()));
            }
            Process process = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> lines = newList();
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("split package"))
                    lines.add(line);
            }
            process.waitFor();
            if (lines.size() > 1) {
                return Optional.of(String.join(String.format("%n"), lines.subList(1, lines.size())));
            }
            return Optional.empty();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void antiDependencyIsNotPresent() {
        Optional<String> failure = testAntiDependency(antiDependency.getFrom(), antiDependency.getTo());
        if (!failure.isPresent()) {
            return;
        }
        String headline = String.format("Bad dependency '%s' is present: (output from 'jdeps')", antiDependency.toString());
        String fullOutput = failure.get();
        System.out.format("%s%n%s\n", headline, fullOutput);
        throw new AssertionError(headline);
    }

    /**
     * Simple graph with transitive closure reachability queries.
     */
    private static class Graph<T> {

        private Map<T, Set<T>> forwardEdges = newMap();

        public void addNode(T e) {
            if (forwardEdges.containsKey(e)) {
                throw new IllegalArgumentException("Node already in graph: " + e);
            }
            forwardEdges.put(e, newSet());
        }

        public void addEdge(T from, T to) {
            checkContains(from);
            checkContains(to);
            addToMapSet(forwardEdges, from, to);
        }

        private void checkContains(T e) {
            if (!forwardEdges.containsKey(e)) {
                throw new IllegalArgumentException("Node not in graph: " + e);
            }
        }

        public Set<T> getNodes() {
            return forwardEdges.keySet();
        }

        public Set<T> getAllReachableNodes(T from) {
            checkContains(from);
            Set<T> reachable = newSet();
            Stack<T> worklist = new Stack<T>();
            worklist.add(from);
            while (!worklist.isEmpty()) {
                T current = worklist.pop();
                if (reachable.contains(current)) {
                    continue;
                }
                reachable.add(current);
                worklist.addAll(forwardEdges.get(current));
            }
            return reachable;
        }

        public Set<T> getAllUnreachableNodes(T from) {
            Set<T> unreachable = newSet(forwardEdges.keySet());
            unreachable.removeAll(getAllReachableNodes(from));
            return unreachable;
        }

        public Set<T> getAllUnreachableNodesInOneStep(T from) {
            Set<T> unreachable = newSet(forwardEdges.keySet());
            unreachable.removeAll(forwardEdges.get(from));
            unreachable.remove(from);
            return unreachable;
        }
    }

    /**
     * The major packages in TAJS.
     */
    private static class TAJSPackage {

        public static final TAJSPackage analysis = new TAJSPackage("analysis");

        public static final TAJSPackage unevalizer = new TAJSPackage("unevalizer");

        public static final TAJSPackage monitoring = new TAJSPackage("monitoring");

        public static final TAJSPackage lattice = new TAJSPackage("lattice");

        public static final TAJSPackage solver = new TAJSPackage("solver");

        public static final TAJSPackage flowgraph = new TAJSPackage("flowgraph");

        public static final TAJSPackage js2flowgraph = new TAJSPackage("js2flowgraph");

        public static final TAJSPackage util = new TAJSPackage("util");

        public static final TAJSPackage options = new TAJSPackage("options");

        private final String lastName;

        private TAJSPackage(String lastName) {
            this.lastName = lastName;
        }

        public String getCanonicalName() {
            return "dk.brics.tajs." + lastName;
        }

        public String getLastName() {
            return lastName;
        }

        private String getLocation() {
            return PathAndURLUtils.toPath(Main.class.getResource(lastName), false).toString();
        }

        @Override
        public String toString() {
            return getCanonicalName();
        }
    }

    private static class Dependency {

        private final Kind kind;

        private final TAJSPackage from;

        private final TAJSPackage to;

        public Dependency(Kind kind, TAJSPackage from, TAJSPackage to) {
            this.kind = kind;
            this.from = from;
            this.to = to;
        }

        public TAJSPackage getFrom() {
            return from;
        }

        public TAJSPackage getTo() {
            return to;
        }

        @Override
        public String toString() {
            return String.format("%s: %s -> %s", kind.toString(), from.getLastName(), to.getLastName());
        }

        enum Kind {
            NEIGHBOUR,
            TRANSITIVE
        }
    }
}