/*
 * Copyright 2009-2017 Aarhus University
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
import dk.brics.tajs.util.PathAndURLUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
                // Jalangi does not handle Array redefinitions nicely
                "out/temp-sources/TestMicro.redefinedArrayLiteralConstructor.js",

                // Jalangi fails to instrument `onlick="return false"` due to top-level return when using --instrumentInline
                "test/flowgraphbuilder/flowgraph_builder0158.html",

                // TAJS_dumpValue in preamble-file
                "test/flowgraphbuilder/flowgraph_builder0149b.js",

                // too TAJS-specialized
                "out/temp-sources/TestMicro.array_unknownConstructorLength.js",
                "test/micro/testToPrimitive.js",
                "test/micro/unexpectedValueBug.js",

                // no log file??!
                "test/1k2012love/1045.js",

                // SLOW
                "test/10k/attractor.html",
                "test/1k2012love/1245.js",
                "test/1k2012love/1247.js",
                "test/1k2013spring/1392.js",
                "test/1k2013spring/1427.js",
                "test/1k2013spring/1450.js",
                "test/1k2013spring/1454.js",
                "test/1k2013spring/1535.js",
                "test/1k2013spring/1544.js",
                "test/1k2013spring/1547.js",
                "test/1k2013spring/1557.js",
                "test/chromeexperiments/watertype.html",

                // INSPECTED FAILURES (TODO move to separate set of strings)

                // string-formatting of doubles
                "test/v8tests/number-tostring.js",

                // TODO: Escaped line breaks in strings parsed incorrectly (github #432)
                // (misuse of "unloggableFiles", not really an issue with the value logger...)
                "test/micro/multilineStrings.js",
                "test/sunspider/crypto-aes.js",
                "test/sunspider/crypto-md5.js",
                "test/sunspider/crypto-sha1.js",
                "test/sunspider/regexp-dna.js"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        syntaxFailureFiles.addAll(Stream.of(
                "test/10k/rgb_color_wheel.html"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        uninspectedUnsoundFiles.addAll(Stream.of(
                // TODO move some of these to yet another category

                // ES6 Symbol
                "test/libraries/jquery/jquery-1.12.0.js",
                "test/mdnexamples/Object.assign_7.js",

                // Minor ES3 legacy, catch no longer introduces a new with-like scope object
                "test/flowgraphbuilder/flowgraph_builder0123.js",

                // f.arguments is not modeled at all
                "test/v8tests/arguments-indirect.js",
                "test/v8tests/extra-arguments.js",

                // test/jquery-load/jquery-1.2.js-sliced.js crashes immediately due to "border" let/var duplicate declaration? (already a Jalangi issue?)

                // missing TAJS call at inline eventhandlers???
                // <button onclick="load()">Render</button><br/><br/>
                "test/chromeexperiments/raytracer.html",
                // <input value="BUILD ME A WORLD!!!!!!!!!!!" onclick="createTerrain()" type="button">
                "test/10k/fractal_landscape.html",

                "test/chromeexperiments/jstouch.html",
                "test/chromeexperiments/strangeattractor.html",
                "test/chromeexperiments/deepseastress.html",

                // LocalStorage converts fields to strings + like something more
                "test/chromeexperiments/harmony.html",

                // lots of allocation site mismatches
                "test/10k/10k_world.html",
                "test/10k/heatmap.html",

                "test/chromeexperiments/voxels.html",

                "test/apps/simplecalc/math2.html",
                "test/apps/minesweeper/minesweeper.html",
                "test/apps/solitaire/spider.html",

                "test/1k2012love/1168.js",
                "test/1k2012love/1189.js",
                "test/1k2012love/1190.js",
                "test/1k2012love/1240.js",
                "test/1k2012love/1254.js",
                "test/1k2012love/1269.js",
                "test/1k2012love/1271.js",
                "test/1k2012love/1281.js", // missing model of websocket?

                "test/1k2013spring/1425.js",
                "test/1k2013spring/1430.js",
                "test/1k2013spring/1442.js",
                "test/1k2013spring/1457.js",
                "test/1k2013spring/1472.js",
                "test/1k2013spring/1473.js",
                "test/1k2013spring/1506.js",
                "test/1k2013spring/1526.js",

                "test/v8tests/const.js",
                "test/v8tests/regexp-static.js",

                "test/sunspider/string-tagcloud.js",

                "test/wala/portal-example-simple.html"
        ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));

        uninspectedUnsoundLocations.addAll(
                Arrays.asList(
                        make("test/1k2013spring/1337.js", 18, 45),

                        make("test/1k2013spring/1350.js", 63, 7),

                        make("test/1k2013spring/1429.js", 55, 5),
                        make("test/1k2013spring/1429.js", 60, 9),
                        make("test/1k2013spring/1429.js", 60, 19),
                        make("test/1k2013spring/1429.js", 60, 30),

                        make("test/1k2013spring/1525.js", 62, 15),
                        make("test/1k2013spring/1525.js", 62, 30)
                )
        );
        unsoundLocations_tajs.addAll(
                Arrays.asList(
                        // UInt + UInt -> UInt
                        make("test/v8tests/greedy.js", 33, 14),
                        make("test/v8tests/greedy.js", 33, 14),
                        make("test/v8tests/greedy.js", 33, 19),
                        make("test/v8tests/greedy.js", 34, 10),
                        make("test/v8tests/greedy.js", 34, 10),
                        make("test/v8tests/greedy.js", 34, 19),
                        make("test/v8tests/greedy.js", 34, 19),
                        make("test/v8tests/greedy.js", 36, 10),
                        make("test/v8tests/greedy.js", 60, 1),

                        // DOM-element ids should be available on the window object

                        // global.toString() output
                        make("test/v8tests/this.js", 31, 1),

                        // const treated as var
                        make("out/temp-sources/TestFlowgraphBuilder.constConstDoubleDeclaration.js", 0, 0),
                        make("out/temp-sources/TestFlowgraphBuilder.constConstDoubleDeclaration_singleStatement.js", 0, 0),
                        make("out/temp-sources/TestFlowgraphBuilder.varConstDoubleDeclaration.js", 0, 0),

                        // missing support for document.write
                        make("test/1k2012love/1274.js", 29, 24),

                        //
                        // MINOR UNSOUNDNESS:
                        //

                        // LocalStorage converts fields to strings
                        make("test/1k2012love/1218.js", 22, 14),
                        make("test/1k2012love/1218.js", 22, 22),
                        make("test/1k2012love/1218.js", 52, 5),
                        make("test/1k2012love/1218.js", 53, 5),

                        // dynamic insertion of script tags (i.e. eval through the DOM)
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3217, 13),
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3218, 13),
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3218, 13),
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3218, 13),
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3219, 20),
                        make("test/jquery-load/jquery-1.3.js-sliced.js", 3219, 27),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 810, 13),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 811, 13),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 811, 13),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 811, 13),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 812, 20),
                        make("test/jquery-load/jquery-1.4.js-sliced.js", 812, 27),
                        make("test/jquery-load/jquery-1.4.js-orig.js", 882, 7),
                        make("test/jquery-load/jquery-1.4.js-orig.js", 883, 3),
                        make("test/jquery-load/jquery-1.4.js-orig.js", 884, 10),
                        make("test/jquery-load/jquery-1.4.js-orig.js", 884, 18),

                        make("test/libraries/jquery/jquery-1.3.0.js", 3054, 7),
                        make("test/libraries/jquery/jquery-1.3.0.js", 3055, 3),
                        make("test/libraries/jquery/jquery-1.3.0.js", 3055, 3),
                        make("test/libraries/jquery/jquery-1.3.0.js", 3055, 3),
                        make("test/libraries/jquery/jquery-1.3.0.js", 3056, 10),
                        make("test/libraries/jquery/jquery-1.3.0.js", 3056, 18),

                        make("test/libraries/jquery/jquery-1.4.0.js", 882, 7),
                        make("test/libraries/jquery/jquery-1.4.0.js", 883, 3),
                        make("test/libraries/jquery/jquery-1.4.0.js", 883, 3),
                        make("test/libraries/jquery/jquery-1.4.0.js", 883, 3),
                        make("test/libraries/jquery/jquery-1.4.0.js", 884, 10),
                        make("test/libraries/jquery/jquery-1.4.0.js", 884, 18),

                        // redefinition of non-configurable properties
                        make("out/temp-sources/JSObject_defineProperty_test.nonConfigurable1.js", 0, 0),

                        // vars from eval can not be deleted currently
                        make("test/v8tests/delete-vars-from-eval.js", 34, 21),
                        make("test/v8tests/delete-vars-from-eval.js", 40, 19),
                        make("test/v8tests/delete-vars-from-eval.js", 33, 3),
                        make("test/v8tests/delete-vars-from-eval.js", 34, 3),
                        make("test/v8tests/delete-vars-from-eval.js", 39, 1),
                        make("test/v8tests/delete-vars-from-eval.js", 40, 1),

                        // f.arguments is not modeled at all
                        make("test/v8tests/function-arguments-null.js", 30, 1),
                        make("test/v8tests/function-arguments-null.js", 30, 12),

                        // f.caller is not modeled at all
                        make("test/v8tests/function-caller.js", 33, 3),
                        make("test/v8tests/function-caller.js", 33, 19),
                        make("test/v8tests/function-caller.js", 34, 3),
                        make("test/v8tests/function-caller.js", 34, 23),

                        // f.arguments is not modeled at all + f.caller is not modeled at all
                        make("test/v8tests/fuzz-accessors.js", 84, 39),

                        // (in the parser?): '\400' ->  '\u0100' != STR_OTHER
                        make("test/v8tests/no-octal-constants-above-256.js", 31, 35),
                        make("test/v8tests/no-octal-constants-above-256.js", 32, 1),
                        make("test/v8tests/no-octal-constants-above-256.js", 32, 19),
                        make("test/v8tests/no-octal-constants-above-256.js", 30, 1),
                        make("test/v8tests/no-octal-constants-above-256.js", 30, 17),
                        make("test/v8tests/no-octal-constants-above-256.js", 31, 1),

                        // RegExp rightContext & RegExp leftContext
                        make("test/v8tests/regexp-indexof.js", 40, 5),
                        make("test/v8tests/regexp-indexof.js", 40, 39),
                        make("test/v8tests/regexp-indexof.js", 41, 5),
                        make("test/v8tests/regexp-indexof.js", 41, 45),

                        // "missing" models of String.prototype.bold/fontcolor/blink ...
                        // make("test/v8tests/function-names.js", 31, 28),

                        // ES3 legacy, catch no longer introduce a new with-like scope object
                        make("test/micro/test127.js", 8, 9),
                        make("test/micro/test127.js", 12, 10),
                        make("test/micro/test127.js", 12, 10),
                        make("test/micro/test127.js", 15, 1),
                        make("test/micro/test127.js", 15, 16),
                        make("test/micro/test127.js", 16, 1),
                        make("test/micro/test127.js", 16, 13),

                        // missing range-error on infinite recursion
                        make("test/getterssetters/so7.js", 0, 0),
                        make("test/getterssetters/so8.js", 0, 0),
                        make("test/micro/testEval.js", 0, 0),

                        // Fails due to Nashorn bug. Sent email to Nashorn mailing list (nashorn-dev@openjdk.java.net): "Minor discepancy for multiline regexps" 2017-1-6
                        make("test/v8tests/regexp.js", 201, 1),
                        make("test/v8tests/regexp.js", 201, 17),
                        make("test/v8tests/regexp.js", 202, 1),

                        // Fails due to Nashorn bug(?). Character classes with \S behave differently from v8
                        make("test/v8tests/regexp.js", 108, 1),
                        make("test/v8tests/regexp.js", 109, 1),
                        make("test/v8tests/regexp.js", 110, 1),
                        make("test/v8tests/regexp.js", 126, 1),
                        make("test/v8tests/regexp.js", 127, 1),
                        make("test/v8tests/regexp.js", 128, 1),

                        //
                        // DELIBERATE UNSOUNDNESS:
                        //

                        // Custom inputs
                        make("test/customInputs/newDateValueOfCustom1.js", 1, 1),
                        make("test/customInputs/newDateValueOfCustom2.js", 3, 1),
                        make("test/customInputs/newDateValueOfCustom2.js", 4, 1),
                        make("test/customInputs/randomCustom.js", 1, 1),
                        make("test/customInputs/newDateGetFullYearCustom1.js", 3, 1),
                        make("test/customInputs/newDateGetFullYearCustom2.js", 3, 1),
                        make("test/customInputs/dateNowCustom.js", 1, 1),

                        // no implicit global vars
                        make("out/temp-sources/TestMicro.testNoImplicitGlobalVarDeclarations.js", 4, 1),
                        make("out/temp-sources/TestMicro.testNoImplicitGlobalVarDeclarations.js", 4, 20),

                        // Ignoring type error for: property with getter, but no setter, that is not writable from *native* code. // GitHub #393
                        make("test/getterssetters/implicits_unsound.js", 12, 23),
                        make("test/getterssetters/implicits_unsound.js", 15, 1),
                        make("test/getterssetters/implicits_unsound.js", 15, 14)
                )
        );
        unsoundLocations_jalangi.addAll(
                Arrays.asList(
                        //
                        // JALANGI/TAJS compatibility issues:
                        //

                        // Jalangi does not support the with-statement calls properly: https://github.com/Samsung/jalangi2/issues/57
                        make("test/flowgraphbuilder/flowgraph_builder0137.js", 1, 20),
                        make("test/flowgraphbuilder/flowgraph_builder0137.js", 4, 2),
                        make("test/flowgraphbuilder/flowgraph_builder0137.js", 4, 17),
                        make("test/1k2012love/1008.js", 1, 2),
                        make("test/1k2012love/1008.js", 7, 3),
                        make("test/1k2012love/1008.js", 30, 3),
                        make("test/1k2012love/1028.js", 14, 9),
                        make("test/1k2012love/1050.js", 4, 32),
                        make("test/1k2012love/1050.js", 4, 38),
                        make("test/1k2012love/1050.js", 15, 3),
                        make("test/1k2012love/1053.js", 46, 3),
                        make("test/1k2012love/1057.js", 36, 4),
                        make("test/1k2012love/1092.js", 89, 9),
                        make("test/1k2012love/1092.js", 94, 4),
                        make("test/1k2012love/1107.js", 52, 5),
                        make("test/1k2012love/1113.js", 1, 420),
                        make("test/1k2012love/1183.js", 18, 17),
                        make("test/1k2012love/1196.js", 67, 4),
                        make("test/1k2012love/1188.js", 75, 9),
                        make("test/1k2012love/1188.js", 67, 4),
                        make("test/1k2012love/1219.js", 14, 10),
                        make("test/1k2012love/1269.js", 14, 10),
                        make("test/1k2013spring/1319.js", 50, 24),
                        make("test/1k2013spring/1319.js", 50, 57),
                        make("test/1k2013spring/1319.js", 59, 11),
                        make("test/1k2013spring/1319.js", 64, 13),
                        make("test/1k2013spring/1319.js", 76, 13),
                        make("test/1k2013spring/1319.js", 95, 22),
                        make("test/1k2013spring/1319.js", 95, 22),
                        make("test/1k2013spring/1319.js", 95, 39),
                        make("test/1k2013spring/1319.js", 95, 39),
                        make("test/1k2013spring/1462.js", 104, 5),
                        make("test/1k2013spring/1511.js", 69, 4),
                        make("test/1k2013spring/1529.js", 41, 25),

                        make("test/micro/test129.js", 7, 14),
                        make("test/micro/test129.js", 10, 10),
                        make("test/micro/test129.js", 10, 10),
                        make("test/micro/test129.js", 14, 1),
                        make("test/micro/test129.js", 14, 16),
                        make("test/micro/test129.js", 15, 1),
                        make("test/micro/test129.js", 15, 13),

                        make("test/micro/testCall2.js", 1, 20),
                        make("test/micro/testCall2.js", 1, 39),
                        make("test/micro/testCall2.js", 4, 10),
                        make("test/micro/testCall2.js", 4, 10),
                        make("test/micro/testCall2.js", 5, 2),
                        make("test/micro/testCall2.js", 5, 17),

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
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 7, 4),
                        make("out/temp-sources/TestStrict.strictUndefinedNullReceivers.js", 8, 8),
                        make("test/sparse2014benchmarks/jpg.js", 27, 18),
                        make("test/sparse2014benchmarks/jpg.js", 522, 7),

                        // this-mismatch between TAJS/Jalangi (!!??)
                        make("out/temp-sources/TestUneval.uneval_stackOverflowRegression.js", 2, 2),

                        // non-terminating concrete execution (deliberate) -- maybe-terminating abstract execution
                        make("test/getterssetters/callToWeakRecursiveGetter.js", 0, 0),

                        // Jalangi makes some delete operations return true???
                        make("test/micro/test64.js", 14, 1),

                        // 2016-09-15 logger produces stackoverflow exceptions on both node and jjs
                        make("out/temp-sources/TestMicro.maybe_infinite_loop.js", 0, 0),

                        // Misc. unmodeled ES6 properties
                        make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.function%28%29%7B%7D.js", 3, 1),
                        make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.Object.js", 3, 1),
                        make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.Object.prototype.js", 3, 1)
                )
        );

        KnownUnsoundnesses_fixedInExtended.modifyKnownTAJSUnsoundLocations(unsoundLocations_tajs);
        KnownUnsoundnesses_fixedInExtended.modifyKnownTAJSUnsoundFiles(uninspectedUnsoundFiles);
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
        Optional<Path> relativeToTAJS = relativize(path);
        if (!relativeToTAJS.isPresent()) {
            return false;
        }
        return set.contains(relativeToTAJS.get());
    }

    private static boolean containsTAJSRelativeLocation(Set<SimpleSourceLocation> set, SourceLocation sourceLocation) {
        Path path = PathAndURLUtils.toPath(sourceLocation.getLocation());
        Optional<Path> relativeToTAJS = relativize(path);
        if (!relativeToTAJS.isPresent()) {
            return false;
        }
        return set.contains(new SimpleSourceLocation(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber(), relativeToTAJS.get()));
    }

    /**
     * Makes a path relative to the TAJS directory.
     */
    private static Optional<Path> relativize(Path path) {
        return PathAndURLUtils.getRelativeToTAJS(path);
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

    // TODO remove when all features of "extended" are in master (GitHub #418)
    public static class KnownUnsoundnesses_fixedInExtended {

        public static void modifyKnownTAJSUnsoundFiles(Set<Path> current) {
            current.addAll(Stream.of(
                    "test/jquery-load/jquery-1.7.js-sliced.js",
                    "test/jquery-load/jquery-1.8.js-sliced.js",
                    "test/jquery-load/jquery-1.9.js-sliced.js",
                    "test/jquery-load/jquery-1.10.js-sliced.js",
                    "test/jquery-load/jquery-1.11.0.js-sliced.js",

                    "test/mdnexamples/Object.assign_1.js",
                    "test/mdnexamples/Object.assign_3.js",
                    "test/mdnexamples/Object.assign_4.js",
                    "test/mdnexamples/Object.assign_6.js",

                    // JSObject.assign
                    "out/temp-sources/JSObject_assign_test.fuzzy.js",
                    "out/temp-sources/JSObject_assign_test.getter.js",
                    "out/temp-sources/JSObject_assign_test.returnSelf.js",
                    "out/temp-sources/JSObject_assign_test.returnSelfBox.js",
                    "out/temp-sources/JSObject_assign_test.simple.js",
                    "out/temp-sources/JSObject_assign_test.weak.js",

                    // JSObject_keysAndOwnPropertyNames
                    "out/temp-sources/JSObject_keysAndOwnPropertyNames_test.keys.%27foo%27.js",
                    "out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.%27foo%27.js",
                    "out/temp-sources/JSObject_keysAndOwnPropertyNames_test.keys.42.js",
                    "out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.42.js"

            ).map(KnownUnsoundnesses::make).collect(Collectors.toList()));
        }

        public static void modifyKnownTAJSUnsoundLocations(Set<SimpleSourceLocation> current) {
            current.addAll(Arrays.asList(
                    // requestAnimationFrame
                    make("test/1k2012love/1008.js", 56, 11),
                    make("test/1k2012love/1251.js", 179, 12),
                    make("test/1k2012love/1252.js", 6, 14),
                    make("test/1k2013spring/1334.js", 7, 5),
                    make("test/1k2013spring/1379.js", 2, 12),
                    make("test/1k2013spring/1438.js", 2, 12),
                    make("test/1k2013spring/1524.js", 52, 3),
                    make("test/jquery-load/jquery-1.6.js-sliced.js", 7415, 53),

                    // Object.assign
                    make("test/mdnexamples/Object.assign_8.js", 6, 1),

                    // JSObject_keysAndOwnPropertyNames
                    make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.%28x%3D%5B%5D%2Cx.p%3D42%2Cx%29.js", 3, 1),
                    make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.%28x%3D%5B%5D%2Cx%5B42%5D%3D42%2Cx%29.js", 3, 1),
                    make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.%5B42%5D.js", 3, 1),
                    make("out/temp-sources/JSObject_keysAndOwnPropertyNames_test.getOwnPropertyNames.%5B%5D.js", 3, 1),

                    // uninspected
                    make("test/1k2013spring/1377.js", 164, 13)

            ));
            current.removeAll(Arrays.asList(
                    make("test/v8tests/fuzz-accessors.js", 84, 39)
            ));
        }
    }
}
