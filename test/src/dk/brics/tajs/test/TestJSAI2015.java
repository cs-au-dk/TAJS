package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestJSAI2015 {

    private IAnalysisMonitoring monitoring;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new AnalysisTimeLimiter(3 * 60, true));
    }

    @Test
    public void JSAI2015_buckets() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/buckets_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2015_cryptobench() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/cryptobench_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2015_jsparse() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/jsparse_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2015_linq_action() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/linq_action_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2015_linq_aggregate() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/linq_aggregate_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2015_linq_functional() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/linq_functional_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2015_md5() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/md5_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2015_numbers() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2015benchmarks/numbers_many_extra_prints.js"};
        Misc.run(args, monitoring);
    }
}
