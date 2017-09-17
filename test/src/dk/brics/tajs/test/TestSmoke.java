package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * Checks many of the complex option combinations for crashes.
 */
@RunWith(Parameterized.class)
public class TestSmoke {

    @Parameterized.Parameter
    public Configuration configuration;

    @Parameterized.Parameters(name = "{0}")
    public static List<Configuration> configurations() {
        List<String> files = Arrays.asList(
                "test-resources/src/sunspider/3d-cube.js",
                "test-resources/src/google/delta-blue.js"
        );
        Set<Set<Feature>> featureSets = powerSet(newSet(Arrays.asList(Feature.values())));
        List<Configuration> configurations = newList();
        files.forEach(f ->
                featureSets.forEach(featureSet ->
                        configurations.add(new Configuration(f, featureSet))
                )
        );
        configurations.removeAll(knownUntestableIssues());
        Collections.sort(configurations, (e1, e2) -> {
            int fileCmp = e1.file.compareTo(e2.file);
            if (fileCmp != 0) {
                return fileCmp;
            }
            List<Feature> e1f = newList(e1.features);
            Collections.sort(e1f);
            List<Feature> e2f = newList(e2.features);
            Collections.sort(e2f);
            return e1f.toString().compareTo(e2f.toString());
        });
        return configurations;
    }

    private static Set<Configuration> knownUntestableIssues() {
        Set<Configuration> knownUntestableIssues = newSet();
        return knownUntestableIssues;
    }

    private static Set<Configuration> knownIssues() {
        Set<Configuration> knownIssues = newSet();
        return knownIssues;
    }

    private static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = newSet();
        if (originalSet.isEmpty()) {
            sets.add(newSet());
            return sets;
        }
        List<T> list = newList(originalSet);
        T head = list.get(0);
        Set<T> rest = newSet(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = newSet();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }

    @BeforeClass
    public static void beforeClass() {
        Main.reset();
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableNoMessages();
        if (configuration.features.contains(Feature.NO_LAZY)) {
            Options.get().enableNoLazy();
        }
        if (configuration.features.contains(Feature.NO_POLYMORPHIC)) {
            Options.get().enableNoPolymorphic();
        }
        if (configuration.features.contains(Feature.NO_RECENCY)) {
            Options.get().enableNoRecency();
        }
        if (configuration.features.contains(Feature.DETERMINACY)) {
            Options.get().enableDeterminacy();
        }
    }

    @Test
    public void smoke() throws Exception {
        Exception exception = null;
        if (requiresMDNPolyFill(configuration.file)) {
            Options.get().enablePolyfillMDN();
        }
        if (requiresDOM(configuration.file)) {
            Options.get().enableIncludeDom();
        }
        try {
            Options.get().setAnalysisTimeLimit(10); //  should be enough to excercise interesting paths
            Misc.run(configuration.file);
        } catch (AnalysisLimitationException e) {
            // ignore
        } catch (Exception e) {
            exception = e;
        }
        boolean exceptionExpected = knownIssues().contains(configuration);
        boolean hasException = exception != null;
        if (exceptionExpected != hasException) {
            if (hasException) {
                throw exception;
            } else {
                Assert.fail("Expected exception - has the issue been fixed?");
            }
        }
    }

    private boolean requiresDOM(String file) {
        Set<String> domFiles = newSet(Arrays.asList(
                "test-resources/src/chromeexperiments/3ddemo.html",
                "benchmarks/tajs/src/apps/minesweeper/minesweeper.html",
                "test-resources/src/10k/10k_snake.html",
                "benchmarks/tajs/src/jquery-load/jquery-1.0.js-orig.js",
                "test-resources/src/sunspider/3d-cube.js"
        ));
        return domFiles.contains(file);
    }

    private boolean requiresMDNPolyFill(String file) {
        return "test-resources/src/chromeexperiments/3ddemo.html".equals(file);
    }

    private enum Feature {
        NO_LAZY,
        NO_POLYMORPHIC,
        NO_RECENCY,
        DETERMINACY,
        NO_CHARGED_CALLS,
        NO_GC
    }

    private static class Configuration {

        private String file;

        private Set<Feature> features;

        public Configuration(String file, Set<Feature> features) {
            this.file = file;
            this.features = features;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Configuration that = (Configuration) o;
            if (file != null ? !file.equals(that.file) : that.file != null) return false;
            return features != null ? features.equals(that.features) : that.features == null;
        }

        @Override
        public int hashCode() {
            int result = file != null ? file.hashCode() : 0;
            result = 31 * result + (features != null ? features.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return String.format("%s w. %s", file, features);
        }
    }
}
