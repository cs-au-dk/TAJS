package dk.brics.tajs.test;

import com.google.gson.stream.JsonWriter;
import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.BasicBlock;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.flowgraph.jsnodes.CallNode;
import dk.brics.tajs.flowgraph.jsnodes.DefaultNodeVisitor;
import dk.brics.tajs.flowgraph.jsnodes.ReadPropertyNode;
import dk.brics.tajs.flowgraph.jsnodes.ReadVariableNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.PKeys;
import dk.brics.tajs.lattice.State;
import dk.brics.tajs.lattice.UnknownValueResolver;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.AnalysisMonitor;
import dk.brics.tajs.monitoring.AnalysisPhase;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitor;
import dk.brics.tajs.monitoring.DefaultAnalysisMonitoring;
import dk.brics.tajs.monitoring.PhaseMonitoring;
import dk.brics.tajs.monitoring.ProgressMonitor;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.solver.NodeAndContext;
import dk.brics.tajs.util.AnalysisException;
import org.kohsuke.args4j.CmdLineException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newMap;
import static dk.brics.tajs.util.Collections.newSet;

public class Stats {

    public static String[][] testSunspider = {
            {"test-resources/src/sunspider/3d-cube.js"},
            {"test-resources/src/sunspider/3d-morph.js"},
            {"test-resources/src/sunspider/3d-raytrace.js"},
            {"test-resources/src/sunspider/access-binary-trees.js"},
            {"test-resources/src/sunspider/access-fannkuch.js"},
            {"test-resources/src/sunspider/access-nbody.js"},
            {"test-resources/src/sunspider/access-nsieve.js"},
            {"test-resources/src/sunspider/bitops-3bit-bits-in-byte.js"},
            {"test-resources/src/sunspider/bitops-bitwise-and.js"},
            {"test-resources/src/sunspider/bitops-nsieve-bits.js"},
            {"test-resources/src/sunspider/controlflow-recursive.js"},
            {"test-resources/src/sunspider/crypto-aes.js"},
            {"test-resources/src/sunspider/crypto-md5.js"},
            {"test-resources/src/sunspider/crypto-sha1.js"},
            {"-uneval", "test-resources/src/sunspider/date-format-tofte.js"},
            {"-uneval", "test-resources/src/sunspider/date-format-xparb.js"},
            {"test-resources/src/sunspider/math-cordic.js"},
            {"test-resources/src/sunspider/math-partial-sums.js"},
            {"test-resources/src/sunspider/math-spectral-norm.js"},
            {"test-resources/src/sunspider/regexp-dna.js"},
            {"test-resources/src/sunspider/string-base64.js"},
            {"test-resources/src/sunspider/string-fasta.js"},
            {"-uneval", "test-resources/src/sunspider/string-tagcloud.js"},
            {"test-resources/src/sunspider/string-unpack-code.js"},
            {"test-resources/src/sunspider/string-validate-input.js"},
    };

    public static String[][] testGoogle = {
            {"test-resources/src/google/richards.js"},
            {"test-resources/src/google/benchpress.js"},
            {"test-resources/src/google/splay.js"},
            {"test-resources/src/google/delta-blue.js"},
            {"test-resources/src/google/cryptobench.js"},
    };

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

    public static String[][] test10K = {
            {"test-resources/src/10k/10k_snake.html"},
            {"test-resources/src/10k/10k_world.html"},
            {"test-resources/src/10k/3d_maker.html"},
            {"test-resources/src/10k/attractor.html"},
            {"test-resources/src/10k/canvas_aquarium.html"},
            {"test-resources/src/10k/defend_yourself.html"},
            {"test-resources/src/10k/earth_night_lights.html"},
            {"test-resources/src/10k/filterrific.html"},
            {"test-resources/src/10k/flatwar.html"},
            {"-polyfill-mdn", "test-resources/src/10k/floating_bubbles.html"},
            {"-uneval", "test-resources/src/10k/fractal_landscape.html"},
            {"test-resources/src/10k/gravity.html"},
            {"test-resources/src/10k/heatmap.html"},
            {"test-resources/src/10k/last_man_standing.html"},
            {"test-resources/src/10k/lines.html"},
            {"test-resources/src/10k/minesweeper.html"},
            {"-uneval", "test-resources/src/10k/nbody.html"},
            {"test-resources/src/10k/rgb_color_wheel.html"},
            {"test-resources/src/10k/sinuous.html"},
            {"test-resources/src/10k/snowpar.html"},
            {"test-resources/src/10k/stairs_to_heaven.html"},
            {"test-resources/src/10k/sudoku.html"},
            {"test-resources/src/10k/tictactoe.html"},
            {"test-resources/src/10k/zmeyko.html"},
    };

    public static String[][] testChromeExperiments = {
            {"test-resources/src/chromeexperiments/3ddemo.html"},
            {"test-resources/src/chromeexperiments/anotherworld.html"},
            {"test-resources/src/chromeexperiments/apophis.html"},
            {"test-resources/src/chromeexperiments/aquarium.html"},
            {"test-resources/src/chromeexperiments/bingbong.html"},
            {"test-resources/src/chromeexperiments/blob.html"},
            {"test-resources/src/chromeexperiments/bomomo.html"},
            {"test-resources/src/chromeexperiments/breathinggalaxies.html"},
            {"test-resources/src/chromeexperiments/browserball.html"},
            {"test-resources/src/chromeexperiments/burncanvas.html"},
            {"test-resources/src/chromeexperiments/catchit.html"},
            {"test-resources/src/chromeexperiments/core.html"},
            {"test-resources/src/chromeexperiments/deepseastress.html"},
            {"test-resources/src/chromeexperiments/harmony.html"},
            {"test-resources/src/chromeexperiments/jstouch.html"},
            {"test-resources/src/chromeexperiments/kaleidoscope.html"},
            {"test-resources/src/chromeexperiments/keylight.html"},
            {"test-resources/src/chromeexperiments/liquidparticles.html"},
            {"test-resources/src/chromeexperiments/magnetic.html"},
            {"test-resources/src/chromeexperiments/orangetunnel.html"},
            {"test-resources/src/chromeexperiments/planedeformations.html"},
            {"test-resources/src/chromeexperiments/plasma.html"},
            {"test-resources/src/chromeexperiments/raytracer.html"},
            {"test-resources/src/chromeexperiments/sandtrap.html"},
            {"test-resources/src/chromeexperiments/starfield.html"},
            {"test-resources/src/chromeexperiments/strangeattractor.html"},
            {"-uneval", "test-resources/src/chromeexperiments/tetris.html"},
            {"test-resources/src/chromeexperiments/trail.html"},
            {"test-resources/src/chromeexperiments/tunneler.html"},
            {"test-resources/src/chromeexperiments/voronoi.html"},
            {"test-resources/src/chromeexperiments/voxels.html"},
            {"test-resources/src/chromeexperiments/watertype.html"},
    };

    public static String[][] test1K2012Love = {
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1001.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1005.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1008.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1010.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1015.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1018.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1019.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1024.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1025.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1027.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1028.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1030.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1031.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1041.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1044.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1045.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1047.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1048.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1050.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1053.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1054.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1055.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1057.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1061.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1063.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1066.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1068.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1071.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1072.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1079.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1091.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1092.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1093.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1095.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1102.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1103.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1107.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1113.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1115.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1120.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1134.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1140.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1143.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1148.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1149.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1154.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1155.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1156.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1157.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1160.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1163.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1166.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1168.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1169.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1170.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1171.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1175.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1176.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1183.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1186.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1188.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1189.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1190.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1191.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1195.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1196.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1199.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1201.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1203.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1209.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1218.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1219.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1224.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1225.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1229.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1230.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1232.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1240.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1243.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1245.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1246.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1247.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1249.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1250.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1251.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1252.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1254.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1257.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1258.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1260.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1265.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1269.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1270.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1271.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1274.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1275.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1276.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1279.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1280.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1281.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2012love/shim.js", "test-resources/src/1k2012love/1284.js"},
    };

    public static String[][] test1K2013Spring = {
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1307.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1309.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1310.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1311.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1316.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1319.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1323.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1334.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1336.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1337.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1344.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1345.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1350.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1358.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1360.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1362.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1375.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1376.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1377.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1379.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1384.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1388.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1392.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1398.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1400.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1404.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1411.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1415.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1417.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1420.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1421.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1423.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1425.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1426.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1427.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1428.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1429.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1430.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1436.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1437.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1438.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1442.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1443.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1449.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1450.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1454.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1457.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1458.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1462.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1470.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1472.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1473.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1475.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1478.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1479.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1483.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1484.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1486.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1498.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1502.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1506.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1507.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1510.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1511.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1514.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1524.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1525.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1526.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1528.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1529.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1533.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1535.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1536.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1537.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1539.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1541.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1542.js"},
            {"-uneval", "-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1544.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1547.js"},
            {"-dom", "-polyfill-mdn", "test-resources/src/1k2013spring/shim.js", "test-resources/src/1k2013spring/1557.js"},
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

    public static String[][] testJQueryLoad = {
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.0.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.1.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.2.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.3.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.4.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.5.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.6.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.7.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.8.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.9.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.10.js"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.11.js"}
    };

    public static String[][] testJQueryLoad_ignoreUnreached = {
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.0.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.1.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.2.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.3.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.4.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.5.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.6.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.7.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.8.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.9.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.10.js"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/libraries/jquery-1.11.js"}
    };

    public static String[][] testJQueryUse = {
            // analyzable in OOPSLA'14:
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progressErrorCallback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stop.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_getWindow.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_set.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setCssAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeIn.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeInOut.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeToggleSpeed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_down.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downSpeeds.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_toggle.html"},
            // unanalyzable in OOPSLA'14:
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_loadWithCallback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeTimed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_jsonp.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_callback.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_queue.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simpleSpeed.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_get.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleCssAttributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleAttributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_unbind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindData.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_fullName.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_ready.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_rename.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributeValues.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributes.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_child.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_class.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest1.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_descendant.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downUp.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_id.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest3.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live2.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSet.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_set.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSetAttribute.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simple.html"},
            {"-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_post.html"},
    };

    public static String[][] testJQueryUse_ignoreUnreached = {
            // analyzable in OOPSLA'14:
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_abort2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progress2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_progressErrorCallback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stop.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_stopQueue2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_getWindow.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dimensions_set.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_appendPrepend3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_beforeAfter3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_removeEmpty2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setCssAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeIn.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeInOut.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeToggleSpeed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_down.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downSpeeds.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_toggle.html"},
            // unanalyzable in OOPSLA'14:
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_loadWithCallback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/fading_fadeTimed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_jsonp.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_load2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_callback.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_queue.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simpleSpeed.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_get.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleCssAttributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_setMultipleAttributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_unbind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bind3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindData.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/methodChaining_divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_fullName.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_ready.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/noConflict_rename.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributeValues.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_attributes.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_child.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_class.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/divTest1.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_descendant.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/sliding_downUp.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/selectors_id.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ready_divTest3.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_live2.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/events_bindUnbindBind.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSet.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_set.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/dom_getSetAttribute.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/animate_simple.html"},
            {"-ignore-unreached", "-dom", "-uneval", "-determinacy", "benchmarks/tajs/src/jquery/test-suite/1.10/ajax_post.html"},
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

    public static String[][] lodash3TestSuite = new String[176][];
    static {
        for (int i = 0; i < 176; i++) {
            lodash3TestSuite[i] = new String[]{"benchmarks/tajs/src/lodash/test-suite/3.0.0/lodash_test_" + (i + 1) + ".html"};
        }
    }

    public static String[][] lodash4TestSuite = new String[306][];
    static {
        for (int i = 0; i < 306; i++) {
            lodash4TestSuite[i] = new String[]{"benchmarks/tajs/src/lodash/test-suite/4.17.10/lodash_test_" + (i + 1) + ".html"};
        }
    }

    public static String[][] prototypeTestSuite = {
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/ajax.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/classes.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/justload.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/observe.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/query.html"},
            {"benchmarks/tajs/src/popular-libs/prototype/prototype-1.7.2-modified/testcases/trythese.html"}
    };

    public static String[][] scriptaculousTestSuite = {
            {"benchmarks/tajs/src/popular-libs/scriptaculous-modified/justload.html"}
    };

    public static class Standard {

        public static void main(String[] args) throws IOException, CmdLineException {
            String outfile = args.length > 0 ? args[0] : "standard";
            OptionValues defaultOptions = new OptionValues();
            defaultOptions.getUnsoundness().setUseFixedRandom(true);
            defaultOptions.getUnsoundness().setShowUnsoundnessUsage(true);
            run(outfile, 60, 100000, Optional.of(defaultOptions),
                    // from RunMedium:
                    testSunspider,
                    testGoogle,
                    test10K,
                    testChromeExperiments,
                    test1K2012Love,
                    test1K2013Spring,
                    testJQueryLoad,
                    testJQueryLoad_ignoreUnreached);
        }
    }

    public static class TSTest {

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
            run(outfile, 10 * 60, 500000, Optional.of(defaultOptions), tstest);
        }
    }


    public static class Extra {

        public static void main(String[] args) throws IOException, CmdLineException {
            String outfile = args.length > 0 ? args[0] : "extra";
            run(outfile, 120, 200000, Optional.empty(),
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

    public static class JQuery {

        public static void main(String[] args) throws IOException, CmdLineException {
            String outfile = args.length > 0 ? args[0] : "jquery";
            OptionValues defaultOptions = new OptionValues();
            defaultOptions.getUnsoundness().setIgnoreSomePrototypesDuringDynamicPropertyReads(true);
            defaultOptions.getUnsoundness().setIgnoreImpreciseEvals(true);
            defaultOptions.getUnsoundness().setIgnoreUnlikelyUndefinedAsFirstArgumentToAddition(true);
            defaultOptions.getUnsoundness().setAssumeInOperatorReturnsTrueWhenSoundResultIsMaybeTrueAndPropNameIsNumber(true);
            defaultOptions.getUnsoundness().setIgnoreUnlikelyPropertyReads(true);
            defaultOptions.getUnsoundness().setUseFixedRandom(true);
            defaultOptions.getUnsoundness().setShowUnsoundnessUsage(true);
            defaultOptions.getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);
            run(outfile, 120, 200000, Optional.of(defaultOptions),
                    testJQueryLoad_ignoreUnreached,
                    testJQueryLoad,
                    testJQueryUse_ignoreUnreached,
                    testJQueryUse
            );
        }
    }

    public static class Libs {

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
            defaultOptions.enablePropNamePartitioning();
            defaultOptions.getUnsoundness().setIgnoreUndefinedPartitions(true);
            run(outfile, 220, 500000, Optional.of(defaultOptions), libs);
        }
    }

    public static class TestUnderscore {

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
            run("underscore", 220, 500000, Optional.of(optionValues), underscoreTestSuite);
        }
    }

    public static class TestLodash3 {

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
            run("lodash_3.0.0", 220, 500000, Optional.of(optionValues), lodash3TestSuite);
        }
    }

    public static class TestLodash4 {

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
            run("lodash_4.17.10", 120, 500000, Optional.of(optionValues), lodash4TestSuite);
        }
    }

    public static class TestPrototype {

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

            optionValues.enablePropNamePartitioning();

            run("prototype", 600, 500000, Optional.of(optionValues), prototypeTestSuite);
        }
    }

    public static class TestScriptaculous {

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
            optionValues.enablePropNamePartitioning();

            run("scriptaculous", 600, 500000, Optional.of(optionValues), scriptaculousTestSuite);
        }
    }

    public static void run(String outfile, int secondsTimeLimit, int nodeTransferLimit, Optional<OptionValues> initialOptions, String[][]... tests) throws IOException, CmdLineException {
        Path statDir = Paths.get("out/stats");
        Path f = statDir.resolve(outfile + ".jsonp");
        Path statsFile = statDir.resolve("stats.html");
        //noinspection ResultOfMethodCallIgnored
        statDir.toFile().mkdirs();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("stats.html")) {
            if (is == null)
                throw new IOException("stats.html not found");
            Files.copy(is, statsFile, StandardCopyOption.REPLACE_EXISTING);
        }
        try (FileWriter fw = new FileWriter(f.toFile())) {
            Main.initLogging();
            JsonWriter w = new JsonWriter(fw);
            String machine = System.getenv("CI_RUNNER_DESCRIPTION");
            if (machine == null)
                machine = System.getenv("COMPUTERNAME");
            if (machine == null)
                machine = System.getenv("HOSTNAME");
            if (machine == null)
                machine = "?";
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long cpustart = threadMXBean.getCurrentThreadCpuTime();
            fw.write("timestamp = " + System.currentTimeMillis() + ";\n");
            fw.write("machine = \"" + machine + "\";\n");
            fw.write("defaultOptions = \"" + (initialOptions.isPresent() ? initialOptions.get() : "") + "\";\n");
            fw.write("data = ");
            w.beginArray();
            int numberOfTests = Arrays.stream(tests).mapToInt(ts -> ts.length).sum();
            int currentTest = 0;
            for (String[][] testset : tests) {
                for (String[] testArgs : testset) {
                    System.out.format("[%d/%d] %s\n", ++currentTest, numberOfTests, String.join(" ", testArgs));
                    OptionValues options = initialOptions.map(OptionValues::clone).orElseGet(OptionValues::new);
                    options.parse(testArgs);
                    options.checkConsistency();
                    options.enableNoMessages();
                    if (System.getProperty("statsquiet", "false").equalsIgnoreCase("true"))
                        options.enableQuiet();

                    ProgressMonitor progressMonitor = new ProgressMonitor(false);
                    AnalysisTimeLimiter analysisTimeLimiter = new AnalysisTimeLimiter(secondsTimeLimit, nodeTransferLimit, false);
                    SuspiciousnessMonitor suspiciousnessMonitor = new SuspiciousnessMonitor();
                    TerminationMonitor terminationMonitor = new TerminationMonitor();
                    PrecisionMonitor precisionMonitor = new PrecisionMonitor();
                    Analysis a = null;
                    Throwable throwable = null;
                    try {
                        a = Main.init(options, CompositeMonitor.make(new AnalysisMonitor(), progressMonitor, analysisTimeLimiter, suspiciousnessMonitor, terminationMonitor, precisionMonitor), null);
                        if (a == null)
                            throw new AnalysisException("Error during initialization");
                        Main.run(a);
                    } catch (Throwable e) {
                        if (terminationMonitor.getTerminatedEarlyMsg() == null)
                            System.out.println("Error: " + e.getMessage());
                        throwable = e;
                    }
                    w.beginObject();
                    String name = testArgs[testArgs.length - 1];
                    String exceptionMsg = throwable != null ? throwable.getMessage() : null;
                    String terminatedEarlyMsg = terminationMonitor.getTerminatedEarlyMsg();
                    String errorMsg = exceptionMsg != null ? exceptionMsg : terminatedEarlyMsg != null ? terminatedEarlyMsg : "";
                    w.name("name").value(name.replace("test-resources/src", "").replace("benchmarks/tajs/src", ""));
                    w.name("options").value(Arrays.stream(testArgs).filter(s -> !s.endsWith(".js") && !s.endsWith(".html")).collect(java.util.stream.Collectors.joining(" ")));
                    if (errorMsg.isEmpty()) {
                        w.name("error").value("");
                    } else {
                        w.name("error").value(categorizeErrorMsg(errorMsg) + ": " + (errorMsg.length() > 500 ? errorMsg.substring(0, 500) + "..." : errorMsg));
                    }
                    if (a != null) {
                        long time = progressMonitor.getPreScanMonitor().getAnalysisTime();
                        w.name("time").value(((double)time)/1000);
                        w.name("node_transfers").value(progressMonitor.getPreScanMonitor().getNodeTransfers());
                        w.name("visited_usercode_node").value(progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size());
                        w.name("transfers_per_visited_node").value(!progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().isEmpty() ? ((double) progressMonitor.getPreScanMonitor().getNodeTransfers()) / progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size() : -1);
                        w.name("visited_div_total_nodes").value(a.getSolver().getFlowGraph().getNumberOfUserCodeNodes() != 0 ? ((double) progressMonitor.getPreScanMonitor().getVisitedNonHostNodes().size()) / a.getSolver().getFlowGraph().getNumberOfUserCodeNodes() : -1);
                        w.name("total_usercode_nodes").value(a.getSolver().getFlowGraph().getNumberOfUserCodeNodes());
                        w.name("total_call_nodes").value(suspiciousnessMonitor.getScanMonitor().getNumberOfCallNodes());
                        w.name("abstract_states").value(a.getSolver().getAnalysisLatticeElement().getNumberOfStates());
                        w.name("states_per_block").value(((double) a.getSolver().getAnalysisLatticeElement().getNumberOfStates()) / a.getSolver().getFlowGraph().getNumberOfBlocks());
                        w.name("average_state_size").value(((double) progressMonitor.getPreScanMonitor().getStateSize()) / a.getSolver().getAnalysisLatticeElement().getNumberOfStates());
                        w.name("average_node_transfer_time").value( progressMonitor.getPreScanMonitor().getNodeTransfers() != 0 ? ((double)time / progressMonitor.getPreScanMonitor().getNodeTransfers()) : -1);
                        w.name("callgraph_edges").value(a.getSolver().getAnalysisLatticeElement().getCallGraph().getSizeIgnoringContexts());
                        w.name("callnodes_to_nonfunction").value(suspiciousnessMonitor.getScanMonitor().getCallToNonFunction().size());
                        w.name("callnodes_to_mixed_functions").value(suspiciousnessMonitor.getScanMonitor().getCallToMixedFunctions().size());
                        w.name("callnodes_polymorphic").value(suspiciousnessMonitor.getScanMonitor().getCallPolymorphic().size());
                        w.name("mixed_readwrites").value(suspiciousnessMonitor.getScanMonitor().getMixedReadOrWrite().size());
                        w.name("average_types").value(precisionMonitor.getScanMonitor().getAverageNumberOfTypesAtReads());
                        w.name("unique_type").value(precisionMonitor.getScanMonitor().getFractionUniqueTypesAtReads());
                        w.name("unique_callee").value(precisionMonitor.getScanMonitor().getFractionUniqueCallees());
                    }
                    // TODO: average and max suspiciousness at function value at call, property name at dynamic-property-accesses, value at read-property, value at read-variable
                    w.endObject();
                    w.flush();
                    Main.reset();
                    System.gc();
                }
            }
            w.endArray();
            fw.write(";\ncputime = " + (threadMXBean.getCurrentThreadCpuTime() - cpustart) + ";\n");
        }
        System.out.println("Output written to " + f + ", open stats.html?" + outfile + " in a browser to view the results");
    }

    private static String categorizeErrorMsg(String errorMsg) {
        if (errorMsg.contains("Likely significant loss of precision (mix of multiple native and non-native functions)")
                || errorMsg.contains("Too imprecise calls to Function")
                || errorMsg.contains("Unevalable eval")) {
            return "[Precision loss]";
        }
        if (errorMsg.contains("not yet supported") || errorMsg.contains("let is not supported") || errorMsg.contains("No support for")) {
            return "[Syntactic limitations]";
        }
        if (errorMsg.contains("No transfer function for native function")
                || errorMsg.contains("Error.captureStackTrace")
                || errorMsg.contains("Array.prototype.values")) {
            return "[EcmaScript modelling]";
        }
        if (errorMsg.contains("No model for Partial")) {
            return "[Node modelling]";
        }
        if (errorMsg.contains("Terminating fixpoint solver early and unsoundly!")
                || errorMsg.contains("GC overhead limit exceeded")
                || errorMsg.contains("Java heap space")) {
            return "[Scalability]";
        }
        if (errorMsg.contains("TAJS_nodeRequireResolve")) {
            return "[Require]";
        }
        if (errorMsg.contains("SoundnessTesting failed")) {
            return "[Soundness failure]";
        }
        if (errorMsg.contains("Log file indicates at the instrumentation was unsuccessful")
                || errorMsg.contains("Unhandled result kind: syntax-error")
                || errorMsg.contains("Something went wrong while checking location")) {
            return "[Log file error]";
        }
        return "[Other]";
    }

    public static class TerminationMonitor extends DefaultAnalysisMonitoring {

        private String terminatedEarlyMsg = null;

        public String getTerminatedEarlyMsg() {
            return terminatedEarlyMsg;
        }

        @Override
        public void visitIterationDone(String terminatedEarlyMsg) {
            this.terminatedEarlyMsg = terminatedEarlyMsg;
        }

    }

    public static class SuspiciousnessMonitor extends PhaseMonitoring<DefaultAnalysisMonitoring, SuspiciousnessMonitor.ScanSuspiciousnessMonitor> { // TODO: would be nice to be able to extract information from other monitors...

        public SuspiciousnessMonitor() {
            super(new DefaultAnalysisMonitoring(), new SuspiciousnessMonitor.ScanSuspiciousnessMonitor());
        }

        public static class ScanSuspiciousnessMonitor extends DefaultAnalysisMonitoring {

            private Solver.SolverInterface c;

            /**
             * Number of call/construct nodes.
             */
            private int call_nodes = 0;

            /**
             * Call/construct nodes that may involve non-function non-undefined values.
             */
            private Set<AbstractNode> call_to_non_function_or_undef = newSet();

            /**
             * Variable/property read/write nodes where the value may involve a mix of two or more native and one or more user-defined functions in the same call context.
             */
            private Set<AbstractNode> mixed_readwrites = newSet();

            /**
             * Call/construct nodes that may involve a mix of two or more native and one or more user-defined functions in the same call context.
             */
            private Set<AbstractNode> call_to_mixed_functions = newSet();

            /**
             * Call/construct nodes that may involve calls to multiple user-defined functions in the same call context (ignoring callee contexts).
             */
            private Set<AbstractNode> call_polymorphic = newSet();

            public int getNumberOfCallNodes() {
                return call_nodes;
            }

            public Set<AbstractNode> getCallToNonFunction() {
                return call_to_non_function_or_undef;
            }

            public Set<AbstractNode> getMixedReadOrWrite() {
                return mixed_readwrites;
            }

            public Set<AbstractNode> getCallToMixedFunctions() {
                return call_to_mixed_functions;
            }

            public Set<AbstractNode> getCallPolymorphic() {
                return call_polymorphic;
            }

            @Override
            public void setSolverInterface(Solver.SolverInterface c) {
                this.c = c;
            }

            @Override
            public void visitPhasePre(AnalysisPhase phase) {
                for (Function f : c.getFlowGraph().getFunctions()) {
                    for (BasicBlock b : f.getBlocks()) {
                        for (AbstractNode n : b.getNodes()) {
                            n.visitBy(new DefaultNodeVisitor() {
                                @Override
                                public void visit(CallNode n) {
                                    call_nodes++;
                                }
                            });
                        }
                    }
                }
            }

            private long getNumberOfNativeFunctions(Set<ObjectLabel> objs) {
                return objs.stream().filter(objlabel -> objlabel.isHostObject() && objlabel.getKind() == ObjectLabel.Kind.FUNCTION).map(ObjectLabel::getHostObject).count();
            }

            private long getNumberOfNonNativeFunctions(Set<ObjectLabel> objs) {
                return objs.stream().filter(objlabel -> !objlabel.isHostObject() && objlabel.getKind() == ObjectLabel.Kind.FUNCTION).map(ObjectLabel::getHostObject).count();
            }

            private boolean isMixed(Value v) {
                Set<ObjectLabel> objs = v.getObjectLabels();
                return getNumberOfNativeFunctions(objs) >= 2 && getNumberOfNonNativeFunctions(objs) >= 1;
            }

            @Override
            public void visitVariableOrProperty(AbstractNode n, String var, SourceLocation loc, Value value, Context context, State state) {
                if (isMixed(value)) {
                    mixed_readwrites.add(n);
                }
            }

            @Override
            public void visitCall(AbstractNode n, Value funval) {
                boolean is_primitive_non_undef = funval.restrictToNotUndef().isMaybePrimitive();
                boolean is_non_function_object = funval.getObjectLabels().stream().anyMatch(objlabel -> objlabel.getKind() != ObjectLabel.Kind.FUNCTION);
                if (is_primitive_non_undef || is_non_function_object) {
                    call_to_non_function_or_undef.add(n);
                }
                if (isMixed(funval)) {
                    call_to_mixed_functions.add(n);
                }
                if (getNumberOfNonNativeFunctions(funval.getObjectLabels()) > 1) {
                    call_polymorphic.add(n);
                }
            }
        }
    }

    public static class PrecisionMonitor extends PhaseMonitoring<DefaultAnalysisMonitoring, PrecisionMonitor.ScanPrecisionMonitor> { // TODO: would be nice to be able to extract information from other monitors...

        public PrecisionMonitor() {
            super(new DefaultAnalysisMonitoring(), new ScanPrecisionMonitor());
        }

        public static class ScanPrecisionMonitor extends DefaultAnalysisMonitoring {

            /**
             * Number of call/construct nodes visited context sensitively.
             */
            private int call_nodes_context_sensitively = 0;

            /**
             * Number of reachable read variable nodes context sensitively.
             */
            private int number_read_variables_with_single_type = 0;

            /**
             * Number of reachable read property nodes context sensitively.
             */
            private int number_read_property_with_single_type = 0;

            /**
             * Call/construct nodes that may involve calls to multiple user-defined functions in the same call context (ignoring callee contexts).
             */
            private Set<AbstractNode> call_polymorphic = newSet();

            /**
             * ReadVariableNode/ReadPropertyNode and contexts that yield values of different type.
             */
            private Map<NodeAndContext, Integer> polymorphic_reads_context_sensitive = newMap();

            public ScanPrecisionMonitor() {}

            /**
             * Returns the average number of types read at ReadVariableNodes and ReadPropertyNodes (context sensitively).
             */
            public float getAverageNumberOfTypesAtReads() {
                float numberPreciseReads = number_read_property_with_single_type + number_read_variables_with_single_type;
                float totalNumberTypes = numberPreciseReads + polymorphic_reads_context_sensitive.values().stream().reduce(0, Integer::sum);
                float totalNumberReads = numberPreciseReads + polymorphic_reads_context_sensitive.size();
                return totalNumberTypes / totalNumberReads;
            }

            /**
             * Returns the fraction of ReadVariableNodes and ReadPropertyNodes with unique type (context sensitively).
             */
            public float getFractionUniqueTypesAtReads() {
                float numberPreciseReads = number_read_property_with_single_type + number_read_variables_with_single_type;
                float totalNumberReads = numberPreciseReads + polymorphic_reads_context_sensitive.size();
                return numberPreciseReads / totalNumberReads;
            }

            /**
             * Returns the fraction of CallNodes with unique callee (context sensitively).
             */
            public float getFractionUniqueCallees() {
                float numberCallNodes = call_nodes_context_sensitively;
                float numberPreciseCallNodes = numberCallNodes - call_polymorphic.size();
                return numberPreciseCallNodes / numberCallNodes;
            }

            @Override
            public void visitNodeTransferPre(AbstractNode n, State s) {
                if (n instanceof CallNode) {
                    call_nodes_context_sensitively++;
                }
            }

            @Override
            public void visitReadProperty(ReadPropertyNode n, Set<ObjectLabel> objlabels, PKeys propertyname, boolean maybe, State state, Value v, ObjectLabel global_obj) {
                int numberTypes = UnknownValueResolver.getRealValue(v, state).typeSize();
                if (numberTypes <= 1) {
                    number_read_property_with_single_type++;
                } else {
                    polymorphic_reads_context_sensitive.put(new NodeAndContext<>(n, state.getContext()), numberTypes);
                }
            }

            @Override
            public void visitReadVariable(ReadVariableNode n, Value v, State state) {
                int numberTypes = UnknownValueResolver.getRealValue(v, state).typeSize();
                if (numberTypes <= 1) {
                    number_read_variables_with_single_type++;
                } else {
                    polymorphic_reads_context_sensitive.put(new NodeAndContext<>(n, state.getContext()), numberTypes);
                }
            }

            @Override
            public void visitCall(AbstractNode n, Value funval) {
                if (funval.getObjectLabels().size() > 1) {
                    call_polymorphic.add(n);
                }
            }
        }
    }
}
