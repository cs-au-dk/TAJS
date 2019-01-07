/*
 * Copyright 2009-2019 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.analysis;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.PathAndURLUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Files and locations in files with where TAJS compute a result that is unsound with respect to a value log.
 * Note: this does not always mean that TAJS is unsound, sometimes it is simply a compatibility issue.
 */
public class KnownUnsoundnesses {

    /**
     * Files that contain syntax errors.
     */
    private static final Set<Path> syntaxFailureFiles = new HashSet<>(); // TODO consider if this is still valuable information (and remove if not)

    /**
     * Files with soundness errors that has not yet been categorized.
     */
    private static final Set<Path> uninspectedUnsoundFiles = new HashSet<>();

    /**
     * Files that are incompatible with the value logger (and therefore not possible to soundnesstest).
     */
    private static final Set<Path> unloggableFiles = new HashSet<>(); // TODO consider if this information belongs in a different class (the current class name is "KnownUnsoundnesses", which is a bad container name for something with "unloggableFiles").

    /**
     * The locations of soundness errors in TAJS.
     */
    private static final Set<SimpleSourceLocation> unsoundLocations_tajs = new HashSet<>();

    /**
     * The locations of false-positive soundness errors in TAJS. The false positive is caused by bad information in the value logs, due to Jalangi-limitations.
     */
    private static final Set<SimpleSourceLocation> unsoundLocations_jalangi = new HashSet<>();

    /**
     * Locations with soundness errors that has not yet been categorized.
     */
    private static final Set<SimpleSourceLocation> uninspectedUnsoundLocations = new HashSet<>();

    static {
        unloggableFiles.addAll(Stream.of(
                // Jalangi throws: "SyntaxError: Unterminated string constant (1:0)"
                "test-resources/src/v8tests/newline-in-string.js",

                // Jalangi does not handle Array redefinitions nicely
                "out/temp-sources/TestMicro.redefinedArrayLiteralConstructor.js",

                // Jalangi fails to instrument `onlick="return false"` due to top-level return when using --instrumentInline
                "test-resources/src/flowgraphbuilder/flowgraph_builder0158.html",

                // TAJS_dumpValue in preamble-file
                "test-resources/src/flowgraphbuilder/flowgraph_builder0149b.js",

                // too TAJS-specialized
                "out/temp-sources/TestMicro.array_unknownConstructorLength.js",
                "test-resources/src/micro/testToPrimitive.js",
                "test-resources/src/micro/unexpectedValueBug.js",

                // no log file??!
                "test-resources/src/1k2012love/1045.js",

                // SLOW
                "test-resources/src/10k/attractor.html",
                "test-resources/src/1k2012love/1245.js",
                "test-resources/src/1k2012love/1247.js",
                "test-resources/src/1k2013spring/1392.js",
                "test-resources/src/1k2013spring/1427.js",
                "test-resources/src/1k2013spring/1450.js",
                "test-resources/src/1k2013spring/1454.js",
                "test-resources/src/1k2013spring/1535.js",
                "test-resources/src/1k2013spring/1544.js",
                "test-resources/src/1k2013spring/1547.js",
                "test-resources/src/1k2013spring/1557.js",

                // INSPECTED FAILURES (TODO move to separate set of strings)

                // string-formatting of doubles
                "test-resources/src/v8tests/number-tostring.js",

                // TODO: Escaped line breaks in strings parsed incorrectly (github #432)
                // (misuse of "unloggableFiles", not really an issue with the value logger...)
                "test-resources/src/micro/multilineStrings.js",
                "test-resources/src/sunspider/crypto-aes.js",
                "test-resources/src/sunspider/crypto-md5.js",
                "test-resources/src/sunspider/crypto-sha1.js",
                "test-resources/src/sunspider/regexp-dna.js",
                "benchmarks/tajs/src/sparse2014benchmarks/crypto-aes.js",
                "benchmarks/tajs/src/sparse2014benchmarks/crypto-md5.js",
                "benchmarks/tajs/src/strlat2014benchmarks/crypto-md5.js",
                "benchmarks/tajs/src/jsai2014benchmarks/std-crypto-sha1.js"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        syntaxFailureFiles.addAll(Stream.of(
                "test-resources/src/10k/rgb_color_wheel.html"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        uninspectedUnsoundFiles.addAll(Stream.of(
                // TODO move some of these to yet another category

                // Minor ES3 legacy, catch no longer introduces a new with-like scope object
                "test-resources/src/flowgraphbuilder/flowgraph_builder0123.js",

                // f.arguments is not modeled at all
                "test-resources/src/v8tests/arguments-indirect.js",
                "test-resources/src/v8tests/extra-arguments.js",

                // Arrays.from does not support ES6 iterators
                "benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_73.html",

                // missing TAJS call at inline eventhandlers???
                // <button onclick="load()">Render</button><br/><br/>
                "test-resources/src/chromeexperiments/raytracer.html",
                // <input value="BUILD ME A WORLD!!!!!!!!!!!" onclick="createTerrain()" type="button">
                "test-resources/src/10k/fractal_landscape.html",

                "test-resources/src/chromeexperiments/deepseastress.html",
                "test-resources/src/chromeexperiments/apophis.html",

                // lots of allocation site mismatches
                "test-resources/src/10k/10k_world.html",
                "test-resources/src/10k/heatmap.html",

                "test-resources/src/chromeexperiments/voxels.html",

                "benchmarks/tajs/src/apps/simplecalc/math2.html",
                "benchmarks/tajs/src/apps/minesweeper/minesweeper.html",
                "benchmarks/tajs/src/apps/solitaire/spider.html",

                "test-resources/src/1k2012love/1008.js",
                "test-resources/src/1k2012love/1168.js",
                "test-resources/src/1k2012love/1189.js",
                "test-resources/src/1k2012love/1240.js",
                "test-resources/src/1k2012love/1252.js",
                "test-resources/src/1k2012love/1271.js",
                "test-resources/src/1k2012love/1281.js", // missing model of websocket?

                "test-resources/src/1k2013spring/1472.js",
                "test-resources/src/1k2013spring/1506.js",
                "test-resources/src/1k2013spring/1524.js",
                "test-resources/src/1k2013spring/1526.js",

                "test-resources/src/v8tests/const.js",
                "test-resources/src/v8tests/regexp-static.js",

                "test-resources/src/sunspider/string-tagcloud.js",

                "test-resources/src/wala/portal-example-simple.html"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        uninspectedUnsoundLocations.addAll(
                Arrays.asList(
                        make("test-resources/src/1k2012love/1190.js", 86, 7),
                        make("test-resources/src/1k2012love/1190.js", 87, 7),

                        make("test-resources/src/1k2013spring/1337.js", 18, 45),

                        make("test-resources/src/1k2013spring/1350.js", 63, 7),

                        make("test-resources/src/1k2013spring/1429.js", 55, 5),
                        make("test-resources/src/1k2013spring/1429.js", 60, 9),
                        make("test-resources/src/1k2013spring/1429.js", 60, 19),
                        make("test-resources/src/1k2013spring/1429.js", 60, 30),

                        make("test-resources/src/1k2013spring/1457.js", 31, 14),

                        make("test-resources/src/1k2013spring/1525.js", 62, 15),
                        make("test-resources/src/1k2013spring/1525.js", 62, 30),

                        make("test-resources/src/chromeexperiments/bingbong.html", 841, 702),

                        make("test-resources/src/chromeexperiments/bomomo.html", 2858, 297),
                        make("test-resources/src/chromeexperiments/bomomo.html", 2858, 499)
                )
        );
        unsoundLocations_tajs.addAll(
                Arrays.asList(
                        // Object.assign should throw type error for read-only properties
                        make("test-resources/src/mdnexamples/Object.assign_8.js", 0, 0),

                        // UInt + UInt -> UInt
                        make("test-resources/src/v8tests/greedy.js", 33, 14),
                        make("test-resources/src/v8tests/greedy.js", 33, 14),
                        make("test-resources/src/v8tests/greedy.js", 33, 19),
                        make("test-resources/src/v8tests/greedy.js", 34, 10),
                        make("test-resources/src/v8tests/greedy.js", 34, 10),
                        make("test-resources/src/v8tests/greedy.js", 34, 19),
                        make("test-resources/src/v8tests/greedy.js", 34, 19),
                        make("test-resources/src/v8tests/greedy.js", 36, 10),
                        make("test-resources/src/v8tests/greedy.js", 60, 1),

                        // DOM-element ids should be available on the window object

                        // global.toString() output
                        make("test-resources/src/v8tests/this.js", 31, 1),

                        // const treated as var
                        make("out/temp-sources/TestFlowgraphBuilder.constConstDoubleDeclaration.js", 0, 0),
                        make("out/temp-sources/TestFlowgraphBuilder.constConstDoubleDeclaration_singleStatement.js", 0, 0),
                        make("out/temp-sources/TestFlowgraphBuilder.varConstDoubleDeclaration.js", 0, 0),

                        // missing support for document.write
                        make("test-resources/src/1k2012love/1274.js", 29, 24),

                        //
                        // MINOR UNSOUNDNESS:
                        //

                        // LocalStorage converts fields to strings
                        make("test-resources/src/1k2012love/1218.js", 22, 14),
                        make("test-resources/src/1k2012love/1218.js", 22, 22),
                        make("test-resources/src/1k2012love/1218.js", 52, 5),
                        make("test-resources/src/1k2012love/1218.js", 53, 5),

                        // dynamic insertion of script tags (i.e. eval through the DOM)
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.3.js", 3054, 7),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.3.js", 3055, 3),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.3.js", 3056, 10),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.3.js", 3056, 18),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.4.js", 882, 7),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.4.js", 883, 3),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.4.js", 884, 10),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.4.js", 884, 18),

                        // redefinition of non-configurable properties
                        make("out/temp-sources/JSObject_defineProperty_test.nonConfigurable1.js", 0, 0),

                        // vars from eval can not be deleted currently
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 34, 21),
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 40, 19),
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 33, 3),
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 34, 3),
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 39, 1),
                        make("test-resources/src/v8tests/delete-vars-from-eval.js", 40, 1),

                        // f.arguments is not modeled at all
                        make("test-resources/src/v8tests/function-arguments-null.js", 30, 1),
                        make("test-resources/src/v8tests/function-arguments-null.js", 30, 12),

                        // f.caller is not modeled at all
                        make("test-resources/src/v8tests/function-caller.js", 33, 3),
                        make("test-resources/src/v8tests/function-caller.js", 33, 19),
                        make("test-resources/src/v8tests/function-caller.js", 34, 3),
                        make("test-resources/src/v8tests/function-caller.js", 34, 23),

                        // (in the parser?): '\400' ->  '\u0100' != STR_OTHER
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 31, 35),
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 32, 1),
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 32, 19),
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 30, 1),
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 30, 17),
                        make("test-resources/src/v8tests/no-octal-constants-above-256.js", 31, 1),

                        // RegExp rightContext & RegExp leftContext
                        make("test-resources/src/v8tests/regexp-indexof.js", 40, 5),
                        make("test-resources/src/v8tests/regexp-indexof.js", 40, 39),
                        make("test-resources/src/v8tests/regexp-indexof.js", 41, 5),
                        make("test-resources/src/v8tests/regexp-indexof.js", 41, 45),

                        // ES3 legacy, catch no longer introduce a new with-like scope object
                        make("test-resources/src/micro/test127.js", 8, 9),
                        make("test-resources/src/micro/test127.js", 12, 10),
                        make("test-resources/src/micro/test127.js", 12, 10),
                        make("test-resources/src/micro/test127.js", 15, 1),
                        make("test-resources/src/micro/test127.js", 15, 16),
                        make("test-resources/src/micro/test127.js", 16, 1),
                        make("test-resources/src/micro/test127.js", 16, 13),

                        // missing range-error on infinite recursion
                        make("test-resources/src/getterssetters/so7.js", 0, 0),
                        make("test-resources/src/getterssetters/so8.js", 0, 0),
                        make("test-resources/src/micro/testEval.js", 0, 0),

                        // Fails due to Nashorn bug. Sent email to Nashorn mailing list (nashorn-dev@openjdk.java.net): "Minor discepancy for multiline regexps" 2017-1-6
                        make("test-resources/src/v8tests/regexp.js", 201, 1),
                        make("test-resources/src/v8tests/regexp.js", 201, 17),
                        make("test-resources/src/v8tests/regexp.js", 202, 1),

                        // Fails due to Nashorn bug(?). Character classes with \S behave differently from v8
                        make("test-resources/src/v8tests/regexp.js", 108, 1),
                        make("test-resources/src/v8tests/regexp.js", 109, 1),
                        make("test-resources/src/v8tests/regexp.js", 110, 1),
                        make("test-resources/src/v8tests/regexp.js", 126, 1),
                        make("test-resources/src/v8tests/regexp.js", 127, 1),
                        make("test-resources/src/v8tests/regexp.js", 128, 1),

                        // Fails due to wrong receiver, for a function called from a function defined by new Function
                        make("test-resources/src/refinement/underscore/test-suite/utility/test180.html", 14, 18),
                        make("test-resources/src/refinement/underscore/test-suite/utility/test180.html", 17, 25),

                        // Fails due to 0x80000000 | 0 incorrectly being abstractly evaluated to 2147483647 (should be -2147483648)
                        make("benchmarks/tajs/src/jsai2014benchmarks/ems-fourinarow.js", 6000, 30),

                        // Fails due to -use-fixed-random (see also AbstractConcreteValueComparator.isAbstractStringValueMorePreciseThanSemiConcreteValue)
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.5.js", 1310, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.6.js", 1391, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.7.js", 1684, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.7.js", 3923, 25),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.8.js", 1521, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.8.js", 3811, 3),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.9.js", 1712, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.9.js", 4050, 3),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.10.js", 381, 22),
                        make("benchmarks/tajs/src/jquery/libraries/jquery-1.11.js", 242, 22),

                        // Fails due to wrong receiver, for a function called from a function defined by new Function
                        make("benchmarks/tajs/src/underscore/test-suite/utility/test180.html", 14, 18),
                        make("benchmarks/tajs/src/underscore/test-suite/utility/test180.html", 17, 25),

                        //
                        // DELIBERATE UNSOUNDNESS:
                        //

                        // Custom inputs
                        make("test-resources/src/customInputs/newDateValueOfCustom1.js", 1, 1),
                        make("test-resources/src/customInputs/newDateValueOfCustom2.js", 3, 1),
                        make("test-resources/src/customInputs/newDateValueOfCustom2.js", 4, 1),
                        make("test-resources/src/customInputs/randomCustom.js", 1, 1),
                        make("test-resources/src/customInputs/newDateGetFullYearCustom1.js", 3, 1),
                        make("test-resources/src/customInputs/newDateGetFullYearCustom2.js", 3, 1),
                        make("test-resources/src/customInputs/dateNowCustom.js", 1, 1),

                        // no implicit global vars
                        make("out/temp-sources/TestMicro.testNoImplicitGlobalVarDeclarations.js", 4, 1),
                        make("out/temp-sources/TestMicro.testNoImplicitGlobalVarDeclarations.js", 4, 20),

                        // Ignoring type error for: property with getter, but no setter, that is not writable from *native* code. // GitHub #393
                        make("test-resources/src/getterssetters/implicits_unsound.js", 12, 23),
                        make("test-resources/src/getterssetters/implicits_unsound.js", 15, 1),
                        make("test-resources/src/getterssetters/implicits_unsound.js", 15, 14)
                )
        );
        unsoundLocations_jalangi.addAll(
                Arrays.asList(
                        //
                        // JALANGI/TAJS compatibility issues:
                        //

                        // Jalangi does not support the with-statement calls properly: https://github.com/Samsung/jalangi2/issues/57
                        make("test-resources/src/flowgraphbuilder/flowgraph_builder0137.js", 1, 20),
                        make("test-resources/src/flowgraphbuilder/flowgraph_builder0137.js", 4, 2),
                        make("test-resources/src/flowgraphbuilder/flowgraph_builder0137.js", 4, 17),
                        make("test-resources/src/1k2012love/1008.js", 1, 2),
                        make("test-resources/src/1k2012love/1008.js", 7, 3),
                        make("test-resources/src/1k2012love/1008.js", 30, 3),
                        make("test-resources/src/1k2012love/1028.js", 14, 9),
                        make("test-resources/src/1k2012love/1050.js", 4, 32),
                        make("test-resources/src/1k2012love/1050.js", 4, 38),
                        make("test-resources/src/1k2012love/1050.js", 15, 3),
                        make("test-resources/src/1k2012love/1053.js", 46, 3),
                        make("test-resources/src/1k2012love/1057.js", 36, 4),
                        make("test-resources/src/1k2012love/1092.js", 89, 9),
                        make("test-resources/src/1k2012love/1092.js", 94, 4),
                        make("test-resources/src/1k2012love/1107.js", 52, 5),
                        make("test-resources/src/1k2012love/1113.js", 1, 420),
                        make("test-resources/src/1k2012love/1183.js", 18, 17),
                        make("test-resources/src/1k2012love/1196.js", 67, 4),
                        make("test-resources/src/1k2012love/1188.js", 75, 9),
                        make("test-resources/src/1k2012love/1188.js", 67, 4),
                        make("test-resources/src/1k2012love/1219.js", 14, 10),
                        make("test-resources/src/1k2012love/1269.js", 14, 10),
                        make("test-resources/src/1k2013spring/1319.js", 50, 24),
                        make("test-resources/src/1k2013spring/1319.js", 50, 57),
                        make("test-resources/src/1k2013spring/1319.js", 59, 11),
                        make("test-resources/src/1k2013spring/1319.js", 64, 13),
                        make("test-resources/src/1k2013spring/1319.js", 76, 13),
                        make("test-resources/src/1k2013spring/1319.js", 95, 22),
                        make("test-resources/src/1k2013spring/1319.js", 95, 22),
                        make("test-resources/src/1k2013spring/1319.js", 95, 39),
                        make("test-resources/src/1k2013spring/1319.js", 95, 39),
                        make("test-resources/src/1k2013spring/1462.js", 104, 5),
                        make("test-resources/src/1k2013spring/1511.js", 69, 4),
                        make("test-resources/src/1k2013spring/1529.js", 41, 25),

                        make("test-resources/src/micro/test129.js", 7, 14),
                        make("test-resources/src/micro/test129.js", 10, 10),
                        make("test-resources/src/micro/test129.js", 10, 10),
                        make("test-resources/src/micro/test129.js", 14, 1),
                        make("test-resources/src/micro/test129.js", 14, 16),
                        make("test-resources/src/micro/test129.js", 15, 1),
                        make("test-resources/src/micro/test129.js", 15, 13),

                        make("test-resources/src/micro/testCall2.js", 1, 20),
                        make("test-resources/src/micro/testCall2.js", 1, 39),
                        make("test-resources/src/micro/testCall2.js", 4, 10),
                        make("test-resources/src/micro/testCall2.js", 4, 10),
                        make("test-resources/src/micro/testCall2.js", 5, 2),
                        make("test-resources/src/micro/testCall2.js", 5, 17),

                        // strict mode is not supported by Jalangi (it is in the tracifier project fork)
                        make("out/temp-sources/TestStrict.selfStrict.js", 3, 6),
                        make("out/temp-sources/TestStrict.selfStrict.js", 3, 33),
                        make("out/temp-sources/TestStrict.callStrictWithoutReceiver.js", 1, 2),
                        make("out/temp-sources/TestStrict.callStrictWithoutReceiver.js", 4, 6),
                        make("out/temp-sources/TestStrict.callStrictWithoutReceiver.js", 4, 19),
                        make("out/temp-sources/TestStrict.deepInheritStrict.js", 1, 2),
                        make("out/temp-sources/TestStrict.deepInheritStrict.js", 3, 5),
                        make("out/temp-sources/TestStrict.deepInheritStrict.js", 5, 12),
                        make("out/temp-sources/TestStrict.deepInheritStrict.js", 5, 25),
                        make("out/temp-sources/TestStrict.inheritStrict.js", 1, 2),
                        make("out/temp-sources/TestStrict.inheritStrict.js", 4, 6),
                        make("out/temp-sources/TestStrict.inheritStrict.js", 4, 19),
                        make("out/temp-sources/TestStrict.callStringThisStrict.js", 1, 2),
                        make("out/temp-sources/TestStrict.callStringThisStrict.js", 4, 6),
                        make("out/temp-sources/TestStrict.callStringThisStrict.js", 4, 19),
                        make("out/temp-sources/TestStrict.strictUndefined.js", 1, 2),
                        make("out/temp-sources/TestStrict.strictUndefined.js", 3, 4),
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 1, 2),
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 3, 4),
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 4, 4),
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 5, 8),
                        make("benchmarks/tajs/src/sparse2014benchmarks/jpg.js", 27, 18),
                        make("benchmarks/tajs/src/sparse2014benchmarks/jpg.js", 522, 7),
                        make("benchmarks/tajs/src/jsai2015benchmarks/buckets_many_extra_prints.js", 3, 2),
                        make("benchmarks/tajs/src/jsai2015benchmarks/buckets_many_extra_prints.js", 36, 29),
                        make("benchmarks/tajs/src/jsai2015benchmarks/buckets_many_extra_prints.js", 1905, 22),
                        make("benchmarks/tajs/src/jsai2015benchmarks/buckets_many_extra_prints.js", 2052, 33),

                        // this-mismatch between TAJS/Jalangi (!!??)
                        make("out/temp-sources/TestUneval.uneval_stackOverflowRegression.js", 2, 2),

                        // non-terminating concrete execution (deliberate) -- maybe-terminating abstract execution
                        make("test-resources/src/getterssetters/callToWeakRecursiveGetter.js", 0, 0),

                        // Jalangi makes some delete operations return true???
                        make("test-resources/src/micro/test64.js", 14, 1),

                        // 2016-09-15 logger produces stackoverflow exceptions on both node and jjs
                        make("out/temp-sources/TestMicro.maybe_infinite_loop.js", 0, 0)

                )
        );
    }

    public static boolean isUnsoundLocation(SourceLocation location) {
        return isJalangiUnsoundLocation(location) || isTAJSUnsoundLocation(location) || isUninspectedUnsoundLocation(location);
    }

    public static boolean isSyntaxFailureFile(Path mainFile) {
        return containsTAJSRelativePath(syntaxFailureFiles, mainFile);
    }

    public static boolean isUnloggableMainFile(Path mainFile) {
        return containsTAJSRelativePath(unloggableFiles, mainFile);
    }

    public static boolean isUninspectedUnsoundFile(Path mainFile) {
        return containsTAJSRelativePath(uninspectedUnsoundFiles, mainFile);
    }

    public static boolean isUninspectedUnsoundLocation(SourceLocation sourceLocation) {
        return containsTAJSRelativeLocation(uninspectedUnsoundLocations, sourceLocation);
    }

    public static boolean isTAJSUnsoundLocation(SourceLocation sourceLocation) {
        return containsTAJSRelativeLocation(unsoundLocations_tajs, sourceLocation);
    }

    public static boolean isJalangiUnsoundLocation(SourceLocation sourceLocation) {
        return containsTAJSRelativeLocation(unsoundLocations_jalangi, sourceLocation);
    }

    private static boolean containsTAJSRelativePath(Set<Path> set, Path path) {
        Optional<Path> relativeToTAJS = PathAndURLUtils.getRelativeToTAJS(path);
        if (!relativeToTAJS.isPresent()) {
            return false;
        }
        return set.contains(relativeToTAJS.get());
    }

    private static boolean containsTAJSRelativeLocation(Set<SimpleSourceLocation> set, SourceLocation sourceLocation) {
        Path path = PathAndURLUtils.toPath(sourceLocation.getLocation(), false);
        Optional<Path> relativeToTAJS = PathAndURLUtils.getRelativeToTAJS(path);
        if (!relativeToTAJS.isPresent()) {
            return false;
        }
        return set.contains(new SimpleSourceLocation(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber(), relativeToTAJS.get()));
    }

    /**
     * Convenience constructor-method for creating a {@link SimpleSourceLocation} for a file in TAJS.
     */
    private static SimpleSourceLocation make(String tajsRootRelativeFile, int lineNumber, int columnNumber) {
        return new SimpleSourceLocation(lineNumber, columnNumber, make(tajsRootRelativeFile));
    }

    /**
     * Convenience constructor-method for creating a {@link Path} to a file in TAJS.
     */
    private static Path make(String tajsRootRelativeFile) {
        return Paths.get(tajsRootRelativeFile);
    }

    /**
     * Simple representation of a source location.
     */
    private static class SimpleSourceLocation {

        private final int lineNumber;

        private final int columnNumber;

        private final Path file;

        public SimpleSourceLocation(int lineNumber, int columnNumber, Path file) {
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
            this.file = file;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final SimpleSourceLocation that = (SimpleSourceLocation) o;

            if (lineNumber != that.lineNumber) return false;
            if (columnNumber != that.columnNumber) return false;
            return file != null ? file.equals(that.file) : that.file == null;
        }

        @Override
        public int hashCode() {
            int result = lineNumber;
            result = 31 * result + columnNumber;
            result = 31 * result + (file != null ? file.hashCode() : 0);
            return result;
        }
    }
}
