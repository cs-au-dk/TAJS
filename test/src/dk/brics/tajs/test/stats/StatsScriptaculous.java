package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class StatsScriptaculous {

    public static String[][] scriptaculousTestSuite = {
            {"benchmarks/tajs/src/popular-libs/scriptaculous-modified/justload.html"}
    };

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

        optionValues.getUnsoundness().setUsePreciseFunctionToString(true);

        optionValues.getSoundnessTesterOptions().setDoNotCheckAmbiguousNodeQueries(true);

        Stats.run("scriptaculous", 600, 500000, Optional.of(optionValues), scriptaculousTestSuite);
    }
}
