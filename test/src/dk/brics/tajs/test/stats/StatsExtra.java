package dk.brics.tajs.test.stats;

import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;
import java.util.Optional;

public class StatsExtra {

    public static String[][] testGoogle2 = {
            {"test-resources/src/google2/richards.js"},
            {"test-resources/src/google2/earley-boyer.js"},
            {"test-resources/src/google2/raytrace.js"},
            {"test-resources/src/google2/splay.js"},
            {"test-resources/src/google2/regexp.js"},
            {"test-resources/src/google2/crypto.js"},
            {"test-resources/src/google2/deltablue.js"},
            {"test-resources/src/google2/navier-stokes.js"},
    };

    public static String[][] testApps = {
            {"benchmarks/tajs/src/apps/jscrypto/encrypt_cookie.html"},
            {"benchmarks/tajs/src/apps/jscrypto/encrypt_from_form.html"},
            {"-determinacy", "benchmarks/tajs/src/apps/mceditor/simple.html"},
            {"-determinacy", "benchmarks/tajs/src/apps/mceditor/full.html"},
            {"benchmarks/tajs/src/apps/minesweeper/minesweeper.html"},
            {"benchmarks/tajs/src/apps/paint/index.html"},
            {"benchmarks/tajs/src/apps/projavascript/clock/07-clock.html"},
            {"benchmarks/tajs/src/apps/projavascript/gallery/index.html"},
            {"benchmarks/tajs/src/apps/projavascript/sun/08-sun.html"},
            {"benchmarks/tajs/src/apps/samegame/index.html"},
            {"benchmarks/tajs/src/apps/simplecalc/math2.html"},
            {"benchmarks/tajs/src/apps/solitaire/spider.html"},
    };

    public static String[][] testJSAI2014 = {
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-aha.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-fannkuch.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-fasta.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-fourinarow.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-hashtest.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-llubenchmark.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/ems-sgefa.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-aes.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-linq_action.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-linq_aggregate.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-linq_dictionary.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-linq_enumerable.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-linq_functional.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/opn-rsa.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-3d-cube.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-3d-raytrace.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-access-nbody.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-cryptobench.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-crypto-sha1.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-richards.js"},
            {"-polyfill-typed-arrays", "benchmarks/tajs/src/jsai2014benchmarks/std-splay.js"},
    };

    public static String[][] testJSAI2015 = {
            {"benchmarks/tajs/src/jsai2015benchmarks/buckets_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/cryptobench_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/jsparse_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/linq_action_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/linq_aggregate_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/linq_functional_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/md5_many_extra_prints.js"},
            {"benchmarks/tajs/src/jsai2015benchmarks/numbers_many_extra_prints.js"},
    };

    public static String[][] testStrLat2014 = {
            {"benchmarks/tajs/src/strlat2014benchmarks/3d-cube.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/3d-raytrace.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/access-nbody.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/crypto-md5.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/garbochess.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/javap.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/richards.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/simplex.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/splay.js"},
            {"benchmarks/tajs/src/strlat2014benchmarks/astar.js"},
    };

    public static String[][] testSparse2014 = {
            {"benchmarks/tajs/src/sparse2014benchmarks/3d-cube.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/3d-raytrace.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/access-nbody.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/crypto-aes.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/crypto-md5.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/deltablue.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/garbochess.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/javap.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/jpg.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/richards.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/simplex.js"},
            {"benchmarks/tajs/src/sparse2014benchmarks/splay.js"},
    };

    public static String[][] testRevamp2016 = {
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_app0.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_app1.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_app2.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_app3.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_app4.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_a.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_b.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_c.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_d.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_e.html"},
            {"-determinacy", "test-resources/src/revamp/jquery/jquery_tutorial_example_f.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/ajax.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/classes.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/justload.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/observe.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/query.html"},
            {"-determinacy", "test-resources/src/revamp/prototype/trythese.html"},
            {"-determinacy", "test-resources/src/revamp/scriptaculous/justload.html"},
    };

    public static void main(String[] args) throws IOException, CmdLineException {
        String outfile = args.length > 0 ? args[0] : "extra";
        Stats.run(outfile, 120, 200000, Optional.empty(),
                // from RunMedium:
                testGoogle2,
                testApps,
                testJSAI2014,
                testJSAI2015,
                testStrLat2014,
                testSparse2014

                // from RunSlow:
//                    testJQueryUse,
//                    testJQueryUse_ignoreUnreached,
//                    testRevamp2016

                    /* TODO:
                    TestKaistAlexaBenchmarksFlowgraph (extended only)
                    other:
                    Oracle benchmark?
                    */
        );
    }
}
