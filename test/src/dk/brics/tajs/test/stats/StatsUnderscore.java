package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class StatsUnderscore {

    public static String[][] underscoreTestSuite = new String[182][];

    static {
        for (int i = 0; i < 29; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/arrays/test" + (i + 1) + ".html"};
        }
        for (int i = 29; i < 38; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/chaining/test" + (i + 1) + ".html"};
        }
        for (int i = 38; i < 81; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/collections/test" + (i + 1) + ".html"};
        }
        for (int i = 81; i < 113; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/functions/test" + (i + 1) + ".html"};
        }
        for (int i = 113; i < 152; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/objects/test" + (i + 1) + ".html"};
        }
        for (int i = 152; i < 182; i++) {
            underscoreTestSuite[i] = new String[]{"benchmarks/tajs/src/underscore/test-suite/utility/test" + (i + 1) + ".html"};
        }
    }

    public static void main(String[] args) throws IOException, CmdLineException {
        OptionValues optionValues = new OptionValues();
        optionValues.enableTest();
        optionValues.enableDeterminacy();
        optionValues.enableNoMessages();
        optionValues.enablePolyfillMDN();
        optionValues.enablePolyfillTypedArrays();
        optionValues.getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("../.."));
        optionValues.getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);
        optionValues.enableIncludeDom();
        optionValues.enableBlendedAnalysis();
        optionValues.enableIgnoreUnreached();
        Stats.run("underscore", 220, 500000, Optional.of(optionValues), underscoreTestSuite);
    }
}
