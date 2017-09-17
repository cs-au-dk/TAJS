package dk.brics.tajs.test.nativeobjects;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests for source code snippets from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/.......
 * <p>
 * Usage: add files to the 'test/mdnexamples'-directory
 * // TODO scrape all the snippets?
 */
@RunWith(Parameterized.class)
public class TestMDNExamples {

    @Parameter
    public Path file;

    @Parameters(name = "{0}")
    public static List<Path> files() throws IOException {
        Path root = Paths.get("test-resources/src/mdnexamples/");
        return Files.list(root).sorted().collect(Collectors.toList());
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableDoNotExpectOrdinaryExit();
        Options.get().enableConsoleModel();
    }

    @Test
    public void test() throws IOException {
        try {
            Misc.runPart("[" + file.getFileName() + "]", Monitoring.make(), file.toString());
        } catch (AnalysisLimitationException e) {
            // Ignore. We only care about black-box soundness correctness
            // (likely caused by ES6 syntax)
        }
    }
}
