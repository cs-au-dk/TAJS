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
public class TestJSAI2014 {

    private IAnalysisMonitoring monitoring;

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new AnalysisTimeLimiter(2 * 60, true));
    }

    @Test
    public void JSAI2014_ems_aha() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-aha.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void JSAI2014_ems_fannkuch() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-fannkuch.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void JSAI2014_ems_fasta() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-fasta.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_ems_fourinarow() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-fourinarow.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void JSAI2014_ems_hashtest() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-hashtest.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_ems_llubenchmark() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-llubenchmark.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.SyntacticSupportNotImplemented.class)
    public void JSAI2014_ems_sgefa() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/ems-sgefa.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_opn_aes() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-aes.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_opn_linq_action() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-linq_action.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_opn_linq_aggregate() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-linq_aggregate.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_opn_linq_dictionary() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-linq_dictionary.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_opn_linq_enumerable() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-linq_enumerable.js"};
        Misc.run(args, monitoring);
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_opn_linq_functional() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-linq_functional.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_opn_rsa() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/opn-rsa.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_3dcube() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-3d-cube.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_3draytrace() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-3d-raytrace.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_accessNbody() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-access-nbody.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_cryptobench() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-cryptobench.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_cryptoSha1() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-crypto-sha1.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_richards() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-richards.js"};
        Misc.run(args, monitoring);
    }

    @Test
    public void JSAI2014_std_splay() throws Exception {
        Misc.init();
        String[] args = {"test/jsai2014benchmarks/std-splay.js"};
        Misc.run(args, monitoring);
    }
}
