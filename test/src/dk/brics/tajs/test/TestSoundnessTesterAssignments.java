package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class TestSoundnessTesterAssignments {

    @Parameterized.Parameter
    public Path file;

    @Parameterized.Parameters(name = "{0}")
    public static List<Path> files() throws IOException {
        Path root = Paths.get("test-resources/src/soundnesstester/");
        return Files.list(root).sorted().collect(Collectors.toList());
    }

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void test() throws IOException {
//        System.out.println(String.join("\n", Files.readAllLines(file)));
        Misc.runPart("[" + file.getFileName() + "]", Monitoring.make(), file.toString());
    }
}
