package dk.brics.tajs.test;

import dk.brics.tajs.Main;
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
        Misc.run("test-resources/src/sunspider/3d-cube.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_3d_morph() throws Exception {
        Misc.run("test-resources/src/sunspider/3d-morph.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_3d_raytrace() throws Exception {  // FIXME: check output
        Misc.run("test-resources/src/sunspider/3d-raytrace.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_binary_trees() throws Exception {
        Misc.run("test-resources/src/sunspider/access-binary-trees.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_fannkuch() throws Exception {
        Misc.run("test-resources/src/sunspider/access-fannkuch.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_nbody() throws Exception {
        Misc.run("test-resources/src/sunspider/access-nbody.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_access_nsieve() throws Exception {
        Misc.run("test-resources/src/sunspider/access-nsieve.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_3bit_bits_in_byte() throws Exception {
        Misc.run("test-resources/src/sunspider/bitops-3bit-bits-in-byte.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_bitwise_and() throws Exception {
        Misc.run("test-resources/src/sunspider/bitops-bitwise-and.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_bitops_nsieve_bits() throws Exception {
        Misc.run("test-resources/src/sunspider/bitops-nsieve-bits.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_controlflow_recursive() throws Exception {
        Misc.run("test-resources/src/sunspider/controlflow-recursive.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_aes() throws Exception {
        Misc.run("test-resources/src/sunspider/crypto-aes.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_md5() throws Exception {
        Misc.run("test-resources/src/sunspider/crypto-md5.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_crypto_sha1() throws Exception {
        Misc.run("test-resources/src/sunspider/crypto-sha1.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void sunspider_date_format_tofte() throws Exception {
        Misc.run("test-resources/src/sunspider/date-format-tofte.js");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisPrecisionLimitationException.class)
    public void sunspider_date_format_xparb() throws Exception {
        Misc.run("test-resources/src/sunspider/date-format-xparb.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_cordic() throws Exception {
        Misc.run("test-resources/src/sunspider/math-cordic.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_partial_sums() throws Exception {
        Misc.run("test-resources/src/sunspider/math-partial-sums.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_math_spectral_norm() throws Exception {
        Misc.run("test-resources/src/sunspider/math-spectral-norm.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_regexp_dna() throws Exception {
        Misc.run("test-resources/src/sunspider/regexp-dna.js"); // can't specialize for-in due to r/w conflict (in fact, the output is non-deterministic)
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_base64() throws Exception {
        Misc.run("test-resources/src/sunspider/string-base64.js"); // TODO: known to be buggy (according to Google Aarhus)
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_fasta() throws Exception {
        Misc.run("test-resources/src/sunspider/string-fasta.js"); // can't specialize for-in due to r/w conflict
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.AnalysisTimeException.class)
    public void sunspider_string_tagcloud() throws Exception {
        Options.get().enableUnevalizer();
        Options.get().setAnalysisTimeLimit(10);
        Misc.run("test-resources/src/sunspider/string-tagcloud.js"); // can't specialize for-in due to r/w conflict
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_unpack_code() throws Exception {
        Misc.run("test-resources/src/sunspider/string-unpack-code.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void sunspider_string_validate_input() throws Exception {
        Misc.run("test-resources/src/sunspider/string-validate-input.js");
        Misc.checkSystemOutput();
    }
}
