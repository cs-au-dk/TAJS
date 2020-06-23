package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.util.Optional;

public class StatsValuePartitioning {

    private static OptionValues createDefaultOptions() {
        OptionValues optionValues = new OptionValues();

        optionValues.enableTest();
        optionValues.getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        optionValues.enableDeterminacy();
        optionValues.enablePolyfillMDN();
        optionValues.enablePolyfillTypedArrays();
        optionValues.enablePolyfillES6Collections();
        optionValues.enablePolyfillES6Promises();
        optionValues.enableConsoleModel();
        optionValues.enableNoMessages();
        optionValues.enableIncludeDom();
        optionValues.enableUnevalizer();

        optionValues.getUnsoundness().setUsePreciseFunctionToString(true);
        return optionValues;
    }

    static class Underscore {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            Stats.run("value-partitioning-underscore", 300, 500000, Optional.of(optionValues), StatsUnderscore.underscoreTestSuite);
        }
    }

    static class Lodash3 {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            Stats.run("value-partitioning-lodash3", 300, 500000, Optional.of(optionValues), StatsLodash3.lodash3TestSuite);
        }
    }

    static class Lodash4 {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            Stats.run("value-partitioning-lodash4", 300, 500000, Optional.of(optionValues), StatsLodash4.lodash4TestSuite);
        }
    }

    static class Prototype {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            Stats.run("value-partitioning-prototype", 300, 500000, Optional.of(optionValues), StatsPrototype.prototypeTestSuite);
        }
    }

    static class Scriptaculous {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            optionValues.getSoundnessTesterOptions().setDoNotCheckAmbiguousNodeQueries(true);
            Stats.run("value-partitioning-scriptaculous", 300, 500000, Optional.of(optionValues), StatsScriptaculous.scriptaculousTestSuite);
        }
    }

    static class JQuery {
        public static void main(String[] args) throws IOException, CmdLineException {
            OptionValues optionValues = createDefaultOptions();
            optionValues.getUnsoundness().setIgnoreSomePrototypesDuringDynamicPropertyReads(true);
            optionValues.getUnsoundness().setIgnoreImpreciseEvals(true);
            optionValues.getUnsoundness().setIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(true);
            optionValues.getUnsoundness().setAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(true);
            optionValues.getUnsoundness().setIgnoreUnlikelyPropertyReads(true);
            optionValues.getUnsoundness().setUseFixedRandom(true);
            optionValues.getUnsoundness().setShowUnsoundnessUsage(true);
            Stats.run("value-partitioning-jquery", 300, 500000, Optional.of(optionValues), StatsJQuery.testJQueryUse);
        }
    }
}
