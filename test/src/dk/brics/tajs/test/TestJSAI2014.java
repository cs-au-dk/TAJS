package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestJSAI2014 {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().setAnalysisTransferLimit(100000);
    }

    @Test(expected = AnalysisLimitationException.class)
    public void JSAI2014_ems_aha() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-aha.js");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_ems_fannkuch() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-fannkuch.js");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_ems_fasta() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-fasta.js");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_ems_fourinarow() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-fourinarow.js");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_ems_hashtest() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-hashtest.js");
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void JSAI2014_ems_llubenchmark() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-llubenchmark.js");
    }

    @Test(expected = AnalysisLimitationException.class)
    public void JSAI2014_ems_sgefa() throws Exception {
        Options.get().enablePolyfillTypedArrays();
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/ems-sgefa.js");
    }

    @Test
    public void JSAI2014_opn_aes() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-aes.js");
    }

    @Test
    public void JSAI2014_opn_linq_action() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-linq_action.js");
    }

    @Test
    public void JSAI2014_opn_linq_aggregate() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-linq_aggregate.js");
    }

    @Test
    public void JSAI2014_opn_linq_dictionary() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-linq_dictionary.js");
    }

    @Test
    public void JSAI2014_opn_linq_enumerable() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-linq_enumerable.js");
    }

    @Test
    public void JSAI2014_opn_linq_functional() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-linq_functional.js");
    }

    @Test
    public void JSAI2014_opn_rsa() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/opn-rsa.js");
    }

    @Test
    public void JSAI2014_std_3dcube() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-3d-cube.js");
    }

    @Test
    public void JSAI2014_std_3draytrace() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-3d-raytrace.js");
    }

    @Test
    public void JSAI2014_std_accessNbody() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-access-nbody.js");
    }

    @Test
    public void JSAI2014_std_cryptobench() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-cryptobench.js");
    }

    @Test
    public void JSAI2014_std_cryptoSha1() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-crypto-sha1.js");
    }

    @Test
    public void JSAI2014_std_richards() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-richards.js");
    }

    @Test
    public void JSAI2014_std_splay() throws Exception {
        Misc.run("benchmarks/tajs/src/jsai2014benchmarks/std-splay.js");
    }
}
