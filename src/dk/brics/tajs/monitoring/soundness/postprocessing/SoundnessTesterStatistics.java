/*
 * Copyright 2009-2017 Aarhus University
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

package dk.brics.tajs.monitoring.soundness.postprocessing;

import dk.brics.tajs.options.TAJSEnvironmentConfig;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.PathAndURLUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import static dk.brics.tajs.monitoring.soundness.postprocessing.SoundnessTesterStatistics.Persistence.save;
import static dk.brics.tajs.util.Collections.newMap;
import static java.util.stream.Collectors.summingInt;

/**
 * Aggregated statistics for soundness testing. Persists results to disk, and loads them later.
 */
public class SoundnessTesterStatistics {

    private final Map<Path, TestResult> results;

    public SoundnessTesterStatistics(Map<Path, TestResult> results) {
        this.results = results;
    }

    public static void main(String[] args) throws IOException {
        TAJSEnvironmentConfig.init(TAJSEnvironmentConfig.findProperties());
        Map<Path, TestResult> results = EasyPersistence.load().results;

        System.out.printf("Soundness testing statistics:%n");

        System.out.printf("\tMain files: %d%n", results.size());
        System.out.printf("\tUninspected files with soundness errors: %d%n", results.values().stream().filter(e -> e.ignoredCompletely).count());
        System.out.printf("\tUnique checks:%n");
        showResults(results, r -> r.checkResultCounts, "\t\t");
        System.out.printf("\tUnique check locations:%n");
        showResults(results, r -> r.checkLocationResultCounts, "\t\t");
    }

    private static void showResults(Map<Path, TestResult> results, Function<TestResult, Map<TestResult.CheckResultKind, Integer>> get, String indentation) {
        Map<TestResult.CheckResultKind, Integer> kindSums = results.values().stream()
                .flatMap(r -> get.apply(r).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));

        System.out.printf("%sTotal count: %d%n", indentation, kindSums.values().stream().mapToInt(e -> e.intValue()).sum());
        System.out.printf("%sClassification counts:%n", indentation);
        kindSums.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(kv -> System.out.printf("%s\t%s: %d%n", indentation, kv.getKey(), kv.getValue()));
    }

    public void register(Path main, TestResult result) {
        this.results.put(main, result);
    }

    public static class Persistence {

        public static SoundnessTesterStatistics load(Path data) {
            return load(PathAndURLUtils.toURL(data));
        }

        public static SoundnessTesterStatistics load(URL data) {
            try (ObjectInputStream in = new ObjectInputStream(data.openConnection().getInputStream())) {
                @SuppressWarnings("unchecked")
                Map<String, TestResult> stringMap = (Map<String, TestResult>) in.readObject();
                Map<Path, TestResult> results = newMap();
                stringMap.forEach((k, v) -> results.put(Paths.get(k.toString()), v));
                return new SoundnessTesterStatistics(results);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public static void save(SoundnessTesterStatistics statistics, Path data) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(data.toFile()))) {
                Map<String, TestResult> stringMap = newMap();
                statistics.results.forEach((k, v) -> stringMap.put(k.toString(), v));
                out.writeObject(stringMap);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class EasyPersistence {

        private static final String key = SoundnessTesterStatistics.class.getSimpleName();

        /**
         * Hardwired flag for enabling/disabling.
         * (It is very time-costly to have the persistence on by default.)
         */
        private static final boolean ENABLED = false;

        private static boolean hasDataLocation = TAJSEnvironmentConfig.get().hasProperty(key);

        public static boolean isEnabled() {
            return ENABLED && hasDataLocation;
        }

        public static SoundnessTesterStatistics load() {
            Path data = getDataLocation();
            if (!Files.exists(data)) {
                return new SoundnessTesterStatistics(newMap());
            }
            return Persistence.load(data);
        }

        public static void update(Path mainFile, TestResult result) {
            Path data = getDataLocation();
            SoundnessTesterStatistics statistics = load();
            statistics.register(mainFile, result);
            save(statistics, data);
        }

        private static Path getDataLocation() {
            return Paths.get(TAJSEnvironmentConfig.get().getCustom(key));
        }
    }

    public static class TestResult implements Serializable {

        public final Map<CheckResultKind, Integer> checkResultCounts;

        public final Map<CheckResultKind, Integer> checkLocationResultCounts;

        public final boolean ignoredCompletely;

        public TestResult(SoundnessCheckCounts rawCounts,
                          SoundnessCheckCounts locationCounts,
                          boolean ignoredCompletely) {
            this.checkResultCounts = makeCountsMap(rawCounts);
            this.checkLocationResultCounts = makeCountsMap(locationCounts);
            this.ignoredCompletely = ignoredCompletely;
        }

        private Map<CheckResultKind, Integer> makeCountsMap(SoundnessCheckCounts counts) {
            Map<CheckResultKind, Integer> map = newMap();
            map.put(CheckResultKind.SUCCESS, counts.successCount);
            map.put(CheckResultKind.FAILURE_UNEXPECTED, counts.unexpectedFailureCount);
            map.put(CheckResultKind.FAILURE_UNINSPECTED, counts.uninspectedFailureCount);
            map.put(CheckResultKind.FAILURE_KNOWN, counts.knownUnsoundnessFailureCount);
            map.put(CheckResultKind.FAILURE_JALANGI, counts.knownJalangiFailureCount);
            return map;
        }

        public enum CheckResultKind {
            SUCCESS,
            FAILURE_UNEXPECTED,
            FAILURE_UNINSPECTED,
            FAILURE_KNOWN,
            FAILURE_JALANGI,
        }
    }
}
