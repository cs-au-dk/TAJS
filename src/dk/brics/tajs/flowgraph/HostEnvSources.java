package dk.brics.tajs.flowgraph;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.Loader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newList;
import static dk.brics.tajs.util.Collections.newSet;

/**
 * JavaScript sources for models of host environments.
 */
public class HostEnvSources {

    private final static SourceLocation loaderDummySourceLocation = new SourceLocation(0, 0, "hostenv-sources-loader");

    private static final boolean DEBUG = false;

    private static Set<Path> paths = newSet();

    public static List<JavaScriptSource> get() {
        final Path dir;
        try {
            dir = Paths.get(HostEnvSources.class.getResource("/hostenv").toURI());
            assert (Files.exists(dir));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<Path> paths = newList();

        if (Options.get().isPolyfillMDNEnabled()) {
            paths.add(dir.resolve("mdn-polyfills.js"));
        }
        if (Options.get().isPolyfillES6CollectionsEnabled()) {
            paths.add(dir.resolve("es6-collections.js"));
        }

        if (Options.get().isDOMEnabled()) {
            // add extra paths...
        }

        HostEnvSources.paths.addAll(paths);
        List<JavaScriptSource> sources = newList();
        for (Path path : paths) {
            try {
                assert (Files.exists(path));
                sources.add(JavaScriptSource.makeFileCode(path.toString(), Loader.getString(path.toString(), "UTF-8")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sources;
    }

    public static boolean isHostEnvSource(SourceLocation sourceLocation) {
        if (DEBUG) {
            return false;
        }
        Path sourcePath;
        try {
            sourcePath = Paths.get(sourceLocation.getFileName());
        } catch (Exception e/* if the path is invalid (Windows) */) {
            return false;
        }
        return sourceLocation == loaderDummySourceLocation || paths.contains(sourcePath);
    }

    public static SourceLocation getLoaderDummySourceLocation() {
        return loaderDummySourceLocation;
    }
}
