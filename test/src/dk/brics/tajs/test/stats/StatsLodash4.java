package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class StatsLodash4 {

    public static String[][] lodash4TestSuite = new String[306][];

    static {
        for (int i = 0; i < 306; i++) {
            lodash4TestSuite[i] = new String[]{"benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_" + (i + 1) + ".html"};
        }
    }

    public static void main(String[] args) throws IOException, CmdLineException {
        OptionValues optionValues = new OptionValues();
        optionValues.enableTest();
        optionValues.getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("../../"));
        optionValues.getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);
        optionValues.enableDeterminacy();
        optionValues.enablePolyfillMDN();
        optionValues.enablePolyfillTypedArrays();
        optionValues.enablePolyfillES6Collections();
        optionValues.enablePolyfillES6Promises();
        optionValues.enableConsoleModel();
        optionValues.enableNoMessages();
        optionValues.enableIncludeDom();
        optionValues.enableBlendedAnalysis();
        optionValues.enableIgnoreUnreached();
        optionValues.getUnsoundness().setUsePreciseFunctionToString(true);
        Stats.run("lodash_4.17.10", 120, 500000, Optional.of(optionValues), lodash4TestSuite);
    }
}
