package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class Misc {

    private static Logger log = Logger.getLogger(Misc.class);

    private static ByteArrayOutputStream os;

    private static PrintStream ps;

    private static Locale oldLocaleDefault;

    public static void init() {
        Main.initLogging(); // TODO: different log4j configuration for tests?
        log.info("=========== " + getClassName() + "." + getMethodName() + " ===========");
    }

    private static String getMethodName() {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = s.length - 1; i >= 0; i--) {
            if (s[i].getClassName().startsWith("dk.brics.tajs.test")) {
                String m = s[i].getMethodName();
                if (!m.equals("main")) {
                    return m;
                }
            }
        }
        throw new AnalysisException("Can't find method name!?");
    }

    private static String getClassName() {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = s.length - 1; i >= 0; i--) {
            String c = s[i].getClassName();
            if (c.startsWith("dk.brics.tajs.test")) {
                if (!s[i].getMethodName().equals("main")) {
                    return c.substring(c.lastIndexOf('.') + 1);
                }
            }
        }
        throw new AnalysisException("Can't find class name!?");
    }

    public static void run(String arg) throws AnalysisException {
        run(new String[]{arg});
    }

    public static void run(String... args) throws AnalysisException {
        run(args, new Monitoring());
    }

    public static void run(String arg, IAnalysisMonitoring monitoring) throws AnalysisException {
        run(new String[]{arg}, monitoring);
    }

    public static void run(String[] args, IAnalysisMonitoring monitoring) throws AnalysisException {
        try {
            Options.get().getArguments().addAll(Arrays.asList(args));
            Analysis a = Main.init(Options.get(), monitoring, null);
            if (a == null)
                throw new AnalysisException("Error during initialization");
            Main.run(a);
            Main.reset();
        } catch (AnalysisException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    private static void checkOutput(String actual) {
        Path file = Paths.get("test/expected/" + getMethodName() + ".out");
        Charset charset = Charset.forName("UTF-8");
        try {
            boolean RECREATE_EXPECTED_OUTPUT = false; // for quick replacement of all expected outputs
            if (!Files.exists(file) || RECREATE_EXPECTED_OUTPUT) {
                if (Files.exists(file) && RECREATE_EXPECTED_OUTPUT) {
                    log.warn("Recreating all expected output!");
                }
                Files.deleteIfExists(file);
                Files.write(file, actual.getBytes(charset), StandardOpenOption.CREATE_NEW);
                log.info(file.toAbsolutePath() + " generated");
            } else {
                String expected = new String(Files.readAllBytes(file), charset);
                assertEquals(fix(expected), fix(actual));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fix(String s) {
        return s.replace(System.getProperty("line.separator"), "\n").replace("\r\n", "\n");
    }

    public static void captureSystemOutput() {
        fixLocale();
        os = new ByteArrayOutputStream();
        try {
            ps = new PrintStream(os, false, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AnalysisException(e);
        }
        Logger rootlogger = Logger.getRootLogger();
        Appender old = rootlogger.getAppender("test");
        if (old != null)
            rootlogger.removeAppender(old);
        Appender a = new WriterAppender(new PatternLayout("%m%n"), ps);
        a.setName("test");
        rootlogger.addAppender(a);
    }

    private static void fixLocale() {
        // required for consistent textual output (number formatting in particular)
        oldLocaleDefault = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    private static void unfixLocale() {
        Locale.setDefault(oldLocaleDefault);
    }

    public static void checkSystemOutput() {
        ps.close();
        try {
            Misc.checkOutput(fix(os.toString("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new AnalysisException(e);
        }
        unfixLocale();
    }

    public static void runSource(String... src) {
        runSource(src, null);
    }

    public static void runSource(String[] src, IAnalysisMonitoring monitoring) {
        runSourcePart(null, src, monitoring);
    }

    public static void runSourcePart(String suffix, String... src) {
        runSourcePart(suffix, src, null);
    }

    private static void runSourcePart(String suffix, String[] src, IAnalysisMonitoring monitoring) {
        try {
            File dir = new File("out/temp-sources/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, getClassName() + "." + getMethodName() + (suffix != null ? "." + suffix : "") + ".js"); // Windows chokes if reusing file names in one execution
            file.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                for (String aSrc : src) {
                    writer.write(aSrc);
                    writer.write("\n");
                }
            }
            String[] args = {file.getPath()};
            if (monitoring == null) {
                Misc.run(args);
            } else {
                Misc.run(args, monitoring);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FlowGraph build(String... src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            sb.append(src[i]).append("\n");
        }

        SourceLocation.SyntheticLocationMaker sourceLocationMaker = new SourceLocation.SyntheticLocationMaker("synthetic");
        FlowGraphBuilder flowGraphBuilder = FlowGraphBuilder.makeForMain(sourceLocationMaker);
        flowGraphBuilder.transformStandAloneCode(sb.toString(), sourceLocationMaker);
        return flowGraphBuilder.close();
    }
}
