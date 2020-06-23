package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.util.Optional;

public class StatsLibs {

    /**
     * Real world libraries, see #455 for the expected outcomes.
     */
    public static String[][] libs = {
            {"benchmarks/tajs/src/popular-libs/accounting/accounting-0.1.4/index.html"},
            {"benchmarks/tajs/src/popular-libs/accounting/accounting-0.2.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/accounting/accounting-0.3.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/accounting/accounting-0.4.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/angular/angular-1.3.20/index.html"},
            {"benchmarks/tajs/src/popular-libs/angular/angular-1.6.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/angular/angular-1.5.8/index.html"},
            {"benchmarks/tajs/src/popular-libs/angular/angular-1.4.14/index.html"},
            {"benchmarks/tajs/src/popular-libs/async/async-1.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/async/async-2.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/async/async-2.1.5/index.html"},
            {"benchmarks/tajs/src/popular-libs/axios/axios-0.13.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/axios/axios-0.14.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/axios/axios-0.15.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/axios/axios-0.16.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/backbone/backbone-1.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/backbone/backbone-1.1.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/backbone/backbone-1.2.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/backbone/backbone-1.3.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-1.2.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-2.11.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.0.6/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.1.5/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.2.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.3.5/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.4.7/index.html"},
            {"benchmarks/tajs/src/popular-libs/bluebird/bluebird-3.5.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/chart/chart-2.2.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/chart/chart-2.3.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/chart/chart-2.4.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/chart/chart-2.5.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/d3/d3-4.2.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/esprima/esprima-2.7.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/handlebars/handlebars-2.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/handlebars/handlebars-3.0.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/handlebars/handlebars-4.0.5/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.10.0-tajs/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.12.4/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.12.4-min/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.8.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.9.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.9.0-min/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.9.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-1.9.1-min/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-2.2.4/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-2.2.4-min/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-3.1.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-3.1.0-min/index.html"},
            {"benchmarks/tajs/src/popular-libs/jquery/jquery-3.2.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-1.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-1.1.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-1.2.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-1.3.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-2.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-2.1.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-2.2.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-2.3.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-2.4.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.1.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.10.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.2.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.3.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.4.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.5.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.6.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.7.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.8.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-3.9.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/lodash/lodash-4.16.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/modernizr/modernizr-3.3.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/moment/moment-2.15.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/moontools/mootools-1.6.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.5.1.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.6.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/react/react-0.12.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/react/react-0.13.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/react/react-0.14.8/index.html"},
            {"benchmarks/tajs/src/popular-libs/react/react-15.3.1/index.html"},
            {"benchmarks/tajs/src/popular-libs/three/three-50/index.html"},
            {"benchmarks/tajs/src/popular-libs/three/three-60/index.html"},
            {"benchmarks/tajs/src/popular-libs/three/three-70/index.html"},
            {"benchmarks/tajs/src/popular-libs/three/three-80/index.html"},
            {"benchmarks/tajs/src/popular-libs/underscore/underscore-1.4.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/underscore/underscore-1.5.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/underscore/underscore-1.6.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/underscore/underscore-1.7.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/underscore/underscore-1.8.3/index.html"},
            {"benchmarks/tajs/src/popular-libs/vue/vue-1.0.26/index.html"},
            {"benchmarks/tajs/src/popular-libs/vue/vue-2.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/vue/vue-2.1.10/index.html"},
            {"benchmarks/tajs/src/popular-libs/vue/vue-2.2.4/index.html"},
            {"benchmarks/tajs/src/popular-libs/yui/yui-3.0.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/yui/yui-3.15.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/yui/yui-3.16.0/index.html"},
            {"benchmarks/tajs/src/popular-libs/yui/yui-3.17.2/index.html"},
            {"benchmarks/tajs/src/popular-libs/yui/yui-3.18.1/index.html"}
    };

    public static void main(String[] args) throws IOException, CmdLineException {
        String outfile = args.length > 0 ? args[0] : "libs";
        OptionValues defaultOptions = new OptionValues();
        defaultOptions.enableIncludeDom();
        defaultOptions.enableUnevalizer();
        defaultOptions.enableDeterminacy();
        defaultOptions.enablePolyfillMDN();
        defaultOptions.enablePolyfillTypedArrays();
        defaultOptions.enablePolyfillES6Collections();
        defaultOptions.enablePolyfillES6Promises();
        defaultOptions.enableConsoleModel();
        defaultOptions.getUnsoundness().setIgnoreSomePrototypesDuringDynamicPropertyReads(true);
        defaultOptions.getUnsoundness().setIgnoreImpreciseEvals(true);
        defaultOptions.getUnsoundness().setIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(true);
        defaultOptions.getUnsoundness().setAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(true);
        defaultOptions.getUnsoundness().setIgnoreUnlikelyPropertyReads(true);
        defaultOptions.getUnsoundness().setUseFixedRandom(true);
        defaultOptions.getUnsoundness().setShowUnsoundnessUsage(true);
        defaultOptions.getUnsoundness().setIgnoreMissingNativeModels(true);
        defaultOptions.getUnsoundness().setIgnoreUndefinedPartitions(true);
        Stats.run(outfile, 220, 500000, Optional.of(defaultOptions), libs);
    }
}
