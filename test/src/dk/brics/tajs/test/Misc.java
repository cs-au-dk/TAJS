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
import dk.brics.tajs.util.Collectors;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.WriterAppender;
import org.junit.ComparisonFailure;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class Misc {

    private static final boolean RECREATE_EXPECTED_OUTPUT = false; // for quick replacement of all expected outputs

    private static Logger log = Logger.getLogger(Misc.class);

    private static ByteArrayOutputStream os;

    private static PrintStream ps;

    private static Locale oldLocaleDefault;

    public static void start(String suffix) {
        String m = getMethodName();
        if (m.equals("test") && suffix == null) {
            throw new AnalysisException("Looks like parameterized test, but suffix missing!");
        }
        System.out.println("testing " + getClassName(false) + "." + m + (suffix != null ? "." + suffix : ""));
        String appendername = Options.get().isDebugEnabled() ? "testdebug" : "test";
        Properties prop = new Properties();
        prop.put("log4j.rootLogger", (Options.get().isDebugEnabled() ? "DEBUG" : "INFO") + ", " + appendername);
        prop.put("log4j.appender." + appendername, "org.apache.log4j.ConsoleAppender");
        prop.put("log4j.appender." + appendername + ".layout", "org.apache.log4j.PatternLayout");
        prop.put("log4j.appender." + appendername + ".layout.ConversionPattern", "%m%n");
        PropertyConfigurator.configure(prop);
        fixLocale();
        if (Options.get().isQuietEnabled())
            captureSystemOutput();
    }

    private static String getMethodName() {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = s.length - 1; i >= 0; i--) {
            if (s[i].getClassName().startsWith("dk.brics.tajs.test") && !s[i].getClassName().equals("dk.brics.tajs.test.Misc")) {
                String m = s[i].getMethodName();
                if (!m.equals("main")) {
                    return m;
                }
            }
        }
        throw new AnalysisException("Can't find method name!?");
    }

    private static String getClassName(boolean full) {
        StackTraceElement[] s = Thread.currentThread().getStackTrace();
        for (int i = s.length - 1; i >= 0; i--) {
            String c = s[i].getClassName();
            if (c.startsWith("dk.brics.tajs.test")) {
                if (!s[i].getMethodName().equals("main")) {
                    return full ? c : c.substring(c.lastIndexOf('.') + 1);
                }
            }
        }
        throw new AnalysisException("Can't find class name!?");
    }

    public static void run(String arg) throws AnalysisException {
        run(new String[]{arg});
    }

    public static void run(String... args) throws AnalysisException {
        runPart(null, Monitoring.make(), args);
    }

    public static void run(String arg, IAnalysisMonitoring monitoring) throws AnalysisException {
        runPart(null, monitoring, arg);
    }

    public static void runPart(String suffix, IAnalysisMonitoring monitoring, String... args) throws AnalysisException {
        start(suffix);
        try {
            Options.get().getArguments().addAll(Arrays.stream(args).map(Paths::get).collect(Collectors.toList()));
            Analysis a = Main.init(Options.get(), monitoring, null);
            if (a == null)
                throw new AnalysisException("Error during initialization");
            Main.run(a);
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = e.toString();
            }
            try {
                Class<?> cls = Class.forName(getClassName(true));
                Method met = cls.getMethod(getMethodName());
                Test an = met.getDeclaredAnnotation(Test.class);
                boolean expected = !an.expected().equals(Test.None.class);
                if (expected)
                    System.out.println("Error (expected): " + msg);
                else {
                    System.out.println("TEST FAILED: " + msg);
                    if (!(e instanceof AnalysisException))
                        e.printStackTrace(System.out);
                }
            } catch (ClassNotFoundException | NoSuchMethodException e2) {
                System.out.println("ERROR: can't find test method");
                throw new AnalysisException(e2);
            }
            throw e;
        }
    }

    private static void checkOutput(String actual) {
        String m = getMethodName();
        if (m.equals("test")) {
            throw new AnalysisException("Looks like parameterized test, can't check output!");
        }
        Path file = Paths.get("test-resources/expected-output/" + m + ".out");
        Charset charset = Charset.forName("UTF-8");
        try {
            if (!Files.exists(file) || RECREATE_EXPECTED_OUTPUT) {
                if (Files.exists(file) && RECREATE_EXPECTED_OUTPUT) {
                    log.warn("Recreating all expected output!");
                }
                Files.deleteIfExists(file);
                Files.write(file, actual.getBytes(charset), StandardOpenOption.CREATE_NEW);
                log.warn(file.toAbsolutePath() + " generated");
            } else {
                String expected = new String(Files.readAllBytes(file), charset);
                String fixedexpected = fix(expected);
                String fixedactual = fix(actual);
                if (!fixedexpected.equals(fixedactual)) {
                    System.out.println("TEST FAILED: unexpected output");
                    throw new ComparisonFailure("unexpected output", fixedexpected, fixedactual);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fix(String s) {
        return s.replace(System.getProperty("line.separator"), "\n").replace("\r\n", "\n");
    }

    private static void captureSystemOutput() {
        if (ps != null) {
            ps.close();
            ps = null;
        }
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
        if (ps == null)
            throw new AnalysisException("No output collected (is -quiet disabled?)");
        ps.close();
        ps = null;
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
        File file = makeTempSourceFile(suffix, src);
        String[] args = {file.getPath()};
        if (monitoring == null) {
            Misc.runPart(suffix, Monitoring.make(), args);
        } else {
            Misc.runPart(suffix, monitoring, args);
        }
    }

    private static File makeTempSourceFile(String suffix, String[] src) {
        try {
            File dir = new File("out/temp-sources/");
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            String m = getMethodName();
            if (m.equals("test") && suffix == null) {
                throw new AnalysisException("Looks like parameterized test, but suffix missing!");
            }
            File file = new File(dir, getClassName(false) + "." + m + (suffix != null ? "." + suffix : "") + ".js"); // Windows chokes if reusing file names in one execution
            file.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                for (String aSrc : src) {
                    writer.write(aSrc);
                    writer.write("\n");
                }
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FlowGraph build(String... src) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String aSrc : src) {
                sb.append(aSrc).append("\n");
            }
            SourceLocation.SyntheticLocationMaker sourceLocationMaker = new SourceLocation.SyntheticLocationMaker("synthetic");
            FlowGraphBuilder flowGraphBuilder = FlowGraphBuilder.makeForMain(sourceLocationMaker);
            flowGraphBuilder.transformStandAloneCode(sb.toString(), sourceLocationMaker);
            return flowGraphBuilder.close();
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }
}
