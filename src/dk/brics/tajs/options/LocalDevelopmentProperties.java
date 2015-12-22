package dk.brics.tajs.options;

import dk.brics.tajs.util.AnalysisException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Manages system-local properties that are relevant during development of TAJS.
 * <p>
 * Reads the locally defined "/resources/local-development.properties" file.
 * That file should *not* be checked into version control.
 * <p>
 * See "/resources/local-development.example.properties" for an example configuration.
 */
public class LocalDevelopmentProperties {

    private static LocalDevelopmentProperties instance;

    private final Path jsdelta_dir;

    private final String classpath;

    private LocalDevelopmentProperties() {
        String filename = "/local-development.properties";
        InputStream input = LocalDevelopmentProperties.class.getResourceAsStream(filename);
        if (input == null) {
            throw new AnalysisException(String.format("Could not find: resources/%s. (did you create it?)", filename));
        }

        Properties prop = new Properties();
        try {
            prop.load(input);
            this.jsdelta_dir = Paths.get(prop.getProperty("jsdelta_dir"));
            this.classpath = prop.getProperty("classpath");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDevelopmentProperties get() {
        if (instance == null) {
            instance = new LocalDevelopmentProperties();
        }
        return instance;
    }

    public Path getJSDeltaDir() {
        return jsdelta_dir;
    }

    public String getClasspath() {
        return classpath;
    }
}
