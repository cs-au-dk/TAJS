package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestStrLat2014 {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().setAnalysisTimeLimit(2 * 60);
    }

    @Test
    public void StrLat2014_3dcube() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/3d-cube.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014_3draytrace() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/3d-raytrace.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014accessnbody() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/access-nbody.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014cryptomd5() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/crypto-md5.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014garbochess() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/garbochess.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014javap() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/javap.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014richards() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/richards.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014simplex() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/simplex.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014splay() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/splay.js"};
        Misc.run(args);
    }

    @Test
    public void StrLat2014astar() throws Exception {
        Misc.init();
        String[] args = {"test/strlat2014benchmarks/astar.js"};
        Misc.run(args);
    }
}
