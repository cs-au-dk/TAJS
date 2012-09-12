package dk.brics.tajs.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestSunspider {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestSunspider");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.enableTest();
		// Options.enableNoLazy();
		// Options.enablePolymorphic();
	}

	@Test
	public void testSunspider3DCube() throws Exception {  // FIXME: check output
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-cube.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspider3DMorph() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-morph.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspider3DRaytrace() throws Exception {  // FIXME: check output
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/3d-raytrace.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessBinaryTrees() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-binary-trees.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessFannkuch() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-fannkuch.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessNBody() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-nbody.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderAccessNSieve() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/access-nsieve.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitops3BitBitsInByte() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-3bit-bits-in-byte.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitopsBitwiseAnd() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-bitwise-and.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderBitopsNSieveBits() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/bitops-nsieve-bits.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderControlflowRecursive() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/controlflow-recursive.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void testSunspiderCryptoAES() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-aes.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderCryptoMD5() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-md5.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderCryptoSHA1() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/crypto-sha1.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: eval
	@Test
	public void testSunspiderDateFormatTofte() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/date-format-tofte.js"};
		Misc.run(args);
		fail("check output"); // TODO Misc.checkSystemOutput();
	}

	@Ignore // TODO: RegExp
	@Test
	public void testSunspiderDateFormatXParb() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/date-format-xparb.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathCordic() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-cordic.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathPartialSums() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-partial-sums.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderMathSpectralNorm() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/math-spectral-norm.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderRegexpDNA() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/regexp-dna.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderStringBase64() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-base64.js"}; // TODO: known to be buggy (according to Google Aarhus)
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void testSunspiderStringFasta() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-fasta.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringTagcloud() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-tagcloud.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringUnpackCode() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-unpack-code.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}

	@Ignore // TODO: for-in + RegExp
	@Test
	public void testSunspiderStringValidateInput() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/sunspider/string-validate-input.js"};
		Misc.run(args);
		fail(); // Misc.checkSystemOutput();
	}
}
