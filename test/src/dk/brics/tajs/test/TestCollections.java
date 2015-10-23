package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.HybridArrayHashSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;
import static org.junit.Assert.assertEquals;

public class TestCollections {

    @BeforeClass
    public static void beforeClass() {
        Main.initLogging();
    }

    @Before
    public void before() {
        Main.reset();
    }

    @Test
    public void setsTest() {
        Options.get().enableTest();
        makeSetsTest();
    }

    @Test
    public void setsTestNoHybrid() {
        Options.get().enableTest();
        Options.get().enableNoHybridCollections();
        makeSetsTest();
    }

    @Test
    public void setsNoTest() {
        makeSetsTest();
    }

    @Test
    public void hybridSetBug() {
        assertEquals(1, 1);

        Set<Integer> byConstructor = new HybridArrayHashSet<>(newList(Arrays.asList(1, 1)));
        assertEquals(1, byConstructor.size());

        Set<Integer> byAddAll = new HybridArrayHashSet<>();
        byAddAll.addAll(newList(Arrays.asList(1, 1)));
        assertEquals(1, byAddAll.size());
    }

    public void makeSetsTest() {
        Set<SourceLocation> byConstructor = newSet(makeElements_6_3());
        Set<SourceLocation> byAddAll = newSet();
        byAddAll.addAll(makeElements_6_3());
        assertEquals(byAddAll, byConstructor);
        assertEquals(3, byConstructor.size());
        assertEquals(3, byAddAll.size());
    }

    public List<SourceLocation> makeElements_6_3() {
        List<SourceLocation> list = newList();
        list.add(new SourceLocation(1, 1, "test1.js"));
        list.add(new SourceLocation(1, 1, "test1.js"));
        list.add(new SourceLocation(1, 1, "test2.js"));
        list.add(new SourceLocation(1, 1, "test2.js"));
        list.add(new SourceLocation(1, 1, "test3.js"));
        list.add(new SourceLocation(1, 1, "test3.js"));
        return list;
    }
}
