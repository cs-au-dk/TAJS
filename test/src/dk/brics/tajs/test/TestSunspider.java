package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestSunspider {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestSunspider");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void sunspider_3d_cube() throws Exception {  // FIXME: check output
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/3d-cube.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_3d_morph() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/3d-morph.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_3d_raytrace() throws Exception {  // FIXME: check output
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/3d-raytrace.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_binary_trees() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/access-binary-trees.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_fannkuch() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/access-fannkuch.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_nbody() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/access-nbody.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_nsieve() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/access-nsieve.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_3bit_bits_in_byte() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/bitops-3bit-bits-in-byte.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_bitwise_and() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/bitops-bitwise-and.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_nsieve_bits() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/bitops-nsieve-bits.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_controlflow_recursive() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/controlflow-recursive.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_aes() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/crypto-aes.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_md5() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/crypto-md5.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_sha1() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/crypto-sha1.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void sunspider_date_format_tofte() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/date-format-tofte.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void sunspider_date_format_xparb() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/date-format-xparb.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_cordic() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/math-cordic.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_partial_sums() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/math-partial-sums.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_spectral_norm() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/math-spectral-norm.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_regexp_dna() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/regexp-dna.js"}; // can't specialize for-in due to r/w conflict (in fact, the output is non-deterministic)
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_base64() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/string-base64.js"}; // TODO: known to be buggy (according to Google Aarhus)
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_fasta() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/string-fasta.js"}; // can't specialize for-in due to r/w conflict
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void sunspider_string_tagcloud() throws Exception {
        Misc.init();
        Options.get().enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/string-tagcloud.js"}; // can't specialize for-in due to r/w conflict
        Misc.run(args, new AnalysisTimeLimiter(10, true));
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_unpack_code() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/string-unpack-code.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_validate_input() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/sunspider/string-validate-input.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
