package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestSparse2014 {

    private IAnalysisMonitoring monitoring;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new AnalysisTimeLimiter(2 * 60, true));
    }

    @Test
    public void Sparse2014_3dcube() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/3d-cube.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014_3draytrace() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/3d-raytrace.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014accessnbody() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/access-nbody.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014cryptoaes() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/crypto-aes.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014cryptomd5() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/crypto-md5.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014deltablue() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/deltablue.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014garbochess() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/garbochess.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014javap() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/javap.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014jpg() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/jpg.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014richards() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/richards.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014simplex() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/simplex.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void Sparse2014splay() throws Exception {
        Misc.init();
        String[] args = {"test/sparse2014benchmarks/splay.js"};
        Misc.run(args, monitoring);
    }
}
