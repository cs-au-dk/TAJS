package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.HybridArrayHashSet;
import dk.brics.tajs.util.PathAndURLUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
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
        try {
            SourceLocation.StaticLocationMaker m1 = new SourceLocation.StaticLocationMaker(mkURL("file:/test1.js"));
            list.add(m1.make(1, 1, 1,1));
            list.add(m1.make(1, 1, 1,1));

            SourceLocation.StaticLocationMaker m2 = new SourceLocation.StaticLocationMaker(mkURL("file:/test2.js"));
            list.add(m2.make(1, 1, 1,1));
            list.add(m2.make(1, 1, 1,1));

            SourceLocation.StaticLocationMaker m3 = new SourceLocation.StaticLocationMaker(mkURL("file:/test3.js"));
            list.add(m3.make(1, 1, 1,1));
            list.add(m3.make(1, 1, 1,1));

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private URL mkURL(String f) throws MalformedURLException {
        return PathAndURLUtils.normalizeFileURL(new URL(f));
    }
}
