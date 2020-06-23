package dk.brics.tajs.test.stats;

import dk.brics.tajs.options.OptionValues;
import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.util.Optional;

public class StatsTSTest {

    /**
     * The benchmarks used in the evaluation of TSTest.
     */
    public static String[][] tstest = {
            {"benchmarks/tajs/src/tstest/accounting/accounting.js"},
            {"benchmarks/tajs/src/tstest/ace/ace.js"},
            {"benchmarks/tajs/src/tstest/angular1/angular1.js"},
            {"benchmarks/tajs/src/tstest/async/async.js"},
            {"benchmarks/tajs/src/tstest/axios/axios.js"},
            {"benchmarks/tajs/src/tstest/backbone/backbone.js"},
            {"benchmarks/tajs/src/tstest/bluebird/bluebird.js"},
            {"benchmarks/tajs/src/tstest/box2dweb/box2dweb.js"},
            {"benchmarks/tajs/src/tstest/chartjs/chart.js"},
            {"benchmarks/tajs/src/tstest/classnames/classnames.js"},
            {"benchmarks/tajs/src/tstest/codemirror/codemirror.js"},
            {"benchmarks/tajs/src/tstest/createjs/createjs.js"},
            {"benchmarks/tajs/src/tstest/d3/d3.js"},
            {"benchmarks/tajs/src/tstest/ember/ember.js"},
            {"benchmarks/tajs/src/tstest/fabric/fabric.js"},
            {"benchmarks/tajs/src/tstest/foundation/foundation.js"},
            {"benchmarks/tajs/src/tstest/hammer/hammer.js"},
            {"benchmarks/tajs/src/tstest/handlebars/handlebars.js"},
            {"benchmarks/tajs/src/tstest/highlight/highlight.js"},
            {"benchmarks/tajs/src/tstest/intro/intro.js"},
            {"benchmarks/tajs/src/tstest/ionic/ionic.js"},
            {"benchmarks/tajs/src/tstest/jasmine/jasmine.js"},
            {"benchmarks/tajs/src/tstest/jsyaml/jsyaml.js"},
            {"benchmarks/tajs/src/tstest/jquery/jquery.js"},
            {"benchmarks/tajs/src/tstest/knockout/knockout.js"},
            {"benchmarks/tajs/src/tstest/leaflet/leaflet.js"},
            {"benchmarks/tajs/src/tstest/lodash/lodash.js"},
            {"benchmarks/tajs/src/tstest/lunr/lunr.js"},
            {"benchmarks/tajs/src/tstest/materialize/materialize.js"},
            {"benchmarks/tajs/src/tstest/mathjax/mathjax.js"},
            {"benchmarks/tajs/src/tstest/medium-editor/medium-editor.js"},
            {"benchmarks/tajs/src/tstest/mime/mime.js"},
            {"benchmarks/tajs/src/tstest/minimist/minimist.js"},
            {"benchmarks/tajs/src/tstest/modernizr/modernizr.js"},
            {"benchmarks/tajs/src/tstest/moment/moment.js"},
            {"benchmarks/tajs/src/tstest/p2/p2.js"},
            {"benchmarks/tajs/src/tstest/pathjs/pathjs.js"},
            {"benchmarks/tajs/src/tstest/pdf/pdf.js"},
            {"benchmarks/tajs/src/tstest/peerjs/peerjs.js"},
            {"benchmarks/tajs/src/tstest/photoswipe/photoswipe.js"},
            {"benchmarks/tajs/src/tstest/pixi/pixi.js"},
            {"benchmarks/tajs/src/tstest/pleasejs/please.js"},
            {"benchmarks/tajs/src/tstest/polymer/polymer.js"},
            {"benchmarks/tajs/src/tstest/swiper/swiper.js"},
            {"benchmarks/tajs/src/tstest/q/q.js"},
            {"benchmarks/tajs/src/tstest/qunit/qunit.js"},
            {"benchmarks/tajs/src/tstest/react/react.js"},
            {"benchmarks/tajs/src/tstest/redux/redux.js"},
            {"benchmarks/tajs/src/tstest/rx/Rx.js"},
            {"benchmarks/tajs/src/tstest/reveal/reveal.js"},
            {"benchmarks/tajs/src/tstest/requirejs/require.js"},
            {"benchmarks/tajs/src/tstest/semver/semver.js"},
            {"benchmarks/tajs/src/tstest/sortable/sortable.js"},
            {"benchmarks/tajs/src/tstest/sugar/sugar.js"},
            {"benchmarks/tajs/src/tstest/three/three.js"},
            {"benchmarks/tajs/src/tstest/underscore/underscore.js"},
            {"benchmarks/tajs/src/tstest/uuid/uuid.js"},
            {"benchmarks/tajs/src/tstest/video/video.js"},
            {"benchmarks/tajs/src/tstest/vue/vue.js"},
            {"benchmarks/tajs/src/tstest/zepto/zepto.js"}
    };

    public static void main(String[] args) throws IOException, CmdLineException {
        String outfile = args.length > 0 ? args[0] : "tstest";
        OptionValues defaultOptions = new OptionValues();
        defaultOptions.enableIncludeDom();
        defaultOptions.enableUnevalizer();
        defaultOptions.enableDeterminacy();
        defaultOptions.enablePolyfillMDN();
        defaultOptions.enablePolyfillTypedArrays();
        defaultOptions.enablePolyfillES6Collections();
        defaultOptions.enablePolyfillES6Promises();
        defaultOptions.enableConsoleModel();
        Stats.run(outfile, 10 * 60, 500000, Optional.of(defaultOptions), tstest);
    }
}
