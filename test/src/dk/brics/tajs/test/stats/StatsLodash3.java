package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class StatsLodash3 {

    public static String[][] lodash3TestSuite = new String[176][];

    static {
        for (int i = 0; i < 176; i++) {
            lodash3TestSuite[i] = new String[]{"benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_" + (i + 1) + ".html"};
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
        Stats.run("lodash_3.0.0", 220, 500000, Optional.of(optionValues), lodash3TestSuite);
    }
}
