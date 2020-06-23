package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class StatsPrototype {

    public static String[][] prototypeTestSuite = {
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/ajax.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/classes.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/justload.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/observe.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/query.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/trythese.html"}
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

        Stats.run("prototype", 600, 500000, Optional.of(optionValues), prototypeTestSuite);
    }
}
