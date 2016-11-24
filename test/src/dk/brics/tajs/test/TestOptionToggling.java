package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.AnalysisTimeLimiter;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.OptionValues;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Tests that each togglable option makes sense with and without debug. The analyzability of the test targets are not important in this test.
 */
@RunWith(Parameterized.class)
public class TestOptionToggling {

    @Parameterized.Parameter
    public Configuration configuration;

    @Parameterized.Parameters(name = "{0}")
    public static List<Configuration> configurations() {
        // *Lots* of output from these tests.
        // Squelch the loggers (see dk.brics.tajs.Main#initLogging):
        Properties prop = new Properties();
        prop.put("log4j.rootLogger", "INFO, tajs");
        prop.put("log4j.appender.tajs", "org.apache.log4j.varia.NullAppender");
        PropertyConfigurator.configure(prop);

        return Arrays.stream(OptionValues.class.getDeclaredMethods())
                .filter(m -> m.getName().startsWith("enable"))
                .filter(m -> !m.getName().equals("enableDebug"))
                .filter(m -> m.getParameterCount() == 0)
                .map(m -> {
                    OptionValues optionValues = new OptionValues();
                    try {
                        m.invoke(optionValues);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    return new Configuration(m.getName(), optionValues);
                }).collect(Collectors.toList());
    }

    @Before
    public void init() {
        Main.reset();
        Options.set(configuration.optionValues);
    }

    @Test
    public void plain_debug() throws Exception {
        Options.get().enableDebug();
        test("test/google/richards.js");
    }

    @Test
    public void dom_debug() throws Exception {
        Options.get().enableDebug();
        test("test/chromeexperiments/3ddemo.html");
    }

    @Test
    public void plain() throws Exception {
        test("test/google/richards.js");
    }

    @Test
    public void dom() throws Exception {
        test("test/chromeexperiments/3ddemo.html");
    }

    public void test(String file) {
        String[] args = {file};
        try {
            Misc.run(args, new CompositeMonitoring(new Monitoring(), new AnalysisTimeLimiter(30, false)));
        } catch (AnalysisLimitationException e) {
            // ignore
        }
    }

    private static class Configuration {

        private final String name;

        private final OptionValues optionValues;

        public Configuration(String name, OptionValues optionValues) {
            this.name = name;
            this.optionValues = optionValues;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
