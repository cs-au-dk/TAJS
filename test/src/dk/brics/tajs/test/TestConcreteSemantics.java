package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.nativeobjects.concrete.TAJSSplitConcreteSemantics;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

/**
 * Sanity checks of {@link dk.brics.tajs.analysis.nativeobjects.concrete.TAJSConcreteSemantics}, depends on String.prototype.substring
 */
public class TestConcreteSemantics {

    private IAnalysisMonitoring monitor;

    @Before
    public void before() {
        Main.reset();
        Main.initLogging();
        Options.get().enableTest();
        monitor = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableChecker());
    }

    @Test
    public void singleConcretes() {
        Misc.runSource(new String[]{
                "TAJS_assert('abc'.substring(1, 2) === 'b');"
        }, monitor);
    }

//    @Test
//    public void multiConcrete() {
//        Misc.runSource(new String[]{
//                "var x = Math.random()? 1: '1';",
//                "TAJS_assert('abc'.substring(x, 2) === 'b');"
//        }, monitor);
//    }

//    @Test
//    public void multiConcrete2() {
//        Misc.runSource(new String[]{
//                "var x = Math.random()? undefined: 0;",
//                "TAJS_assert('abc'.substring(x, 2) === 'ab');"
//        }, monitor);
//    }

//    @Test
//    public void multiConcreteMultiResults() {
//        Misc.runSource(new String[]{
//                "var x = Math.random()? 0: '1';",
//                "var result = 'abc'.substring(x, 2);",
//                "TAJS_assert(result, 'isMaybeAnyStr', false);",
//                "TAJS_assert(result, 'isMaybeStrIdentifier');"
//        }, monitor);
//    }

//    @Test
//    public void multiConcreteMultiResults2() {
//        Misc.runSource(new String[]{
//                "var x = Math.random()? undefined: '1';",
//                "var result = 'abc'.substring(x, 2);",
//                "TAJS_assert(result, 'isMaybeAnyStr', false);",
//                "TAJS_assert(result, 'isMaybeStrIdentifier');"
//        }, monitor);
//    }

//    @Test
//    public void multiConcretes() {
//        Misc.runSource(new String[]{
//                "var x = Math.random()? 1: '1';",
//                "var y = Math.random()? 2: '2';",
//                "TAJS_assert('abc'.substring(x, y) === 'b');"
//        }, monitor);
//    }

    @Test
    public void abstracts() {
        Misc.runSource(new String[]{
                "var x = Math.random()? 0: 1;",
                "TAJS_assert('abc'.substring(x, 2), 'isMaybeAnyStr');"
        }, monitor);
    }

    @Test
    public void multiArgumentPowerSetsEmpty() {
        Set<String> source1 = newSet();
        Set<String> source2 = newSet();
        Set<String> source3 = newSet();
        List<Set<String>> source = Arrays.asList(source1, source2, source3);
        Set<List<String>> power = TAJSSplitConcreteSemantics.makePowerSetOfListsFromListsOfSets(source);
        assertEquals(0, source1.size() * source2.size() * source3.size());
        assertEquals(0, power.size());
    }

    @Test
    public void multiArgumentPowerSetsSingle() {
        Set<String> source1 = newSet(Arrays.asList("1a"));
        Set<String> source2 = newSet(Arrays.asList("2a"));
        Set<String> source3 = newSet(Arrays.asList("3a"));
        List<Set<String>> source = Arrays.asList(source1, source2, source3);
        Set<List<String>> power = TAJSSplitConcreteSemantics.makePowerSetOfListsFromListsOfSets(source);
        assertEquals(1, source1.size() * source2.size() * source3.size());
        assertEquals(1, power.size());
    }

    @Test
    public void multiArgumentPowerSetsIncreasing() {
        Set<String> source1 = newSet(Arrays.asList("1a"));
        Set<String> source2 = newSet(Arrays.asList("2a", "2b"));
        Set<String> source3 = newSet(Arrays.asList("3a", "3b", "3c"));
        List<Set<String>> source = Arrays.asList(source1, source2, source3);
        Set<List<String>> power = TAJSSplitConcreteSemantics.makePowerSetOfListsFromListsOfSets(source);
        assertEquals(6, source1.size() * source2.size() * source3.size());
        assertEquals(6, power.size());
    }

    @Test
    public void multiArgumentPowerSetsDecreasing() {
        Set<String> source1 = newSet(Arrays.asList("1a", "1b", "1c"));
        Set<String> source2 = newSet(Arrays.asList("2a", "2b"));
        Set<String> source3 = newSet(Arrays.asList("3a"));
        List<Set<String>> source = Arrays.asList(source1, source2, source3);
        Set<List<String>> power = TAJSSplitConcreteSemantics.makePowerSetOfListsFromListsOfSets(source);
        assertEquals(6, source1.size() * source2.size() * source3.size());
        assertEquals(6, power.size());
    }

    @Test
    public void multiArgumentPowerSetsDuplicates() {
        Set<String> source1 = newSet(Arrays.asList("a", "b", "c"));
        Set<String> source2 = newSet(Arrays.asList("a", "b"));
        Set<String> source3 = newSet(Arrays.asList("a"));
        List<Set<String>> source = Arrays.asList(source1, source2, source3);
        Set<List<String>> power = TAJSSplitConcreteSemantics.makePowerSetOfListsFromListsOfSets(source);
        assertEquals(6, source1.size() * source2.size() * source3.size());
        assertEquals(6, power.size());
    }
}
