package dk.brics.tajs.flowgraph;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * JavaScript sources for models of host environments.
 */
public class HostEnvSources {

    private final static String filenNamePrefix = "TAJS-host-environment-sources";

    private final static SourceLocation loaderDummySourceLocation = new SourceLocation(0, 0, formatFileName("loader"));

    private static final boolean DEBUG = false;

    public static List<JavaScriptSource> get() {
        // NB: not using java.nio.Path since windows-hosts will use \ instead of /
        List<String> sourcePaths = newList();

        if (Options.get().isPolyfillMDNEnabled()) {
            sourcePaths.add("mdn-polyfills.js");
        }
        if (Options.get().isPolyfillES6CollectionsEnabled()) {
            sourcePaths.add("es6-collections.js");
        }
        if (Options.get().isPolyfillTypedArraysEnabled()) {
            sourcePaths.add("typed-arrays.js");
        }

        if (Options.get().isDOMEnabled()) {
            // add extra paths...
        }

        String root = "/hostenv";
        List<JavaScriptSource> sources = newList();
        for (String sourcePath : sourcePaths) {
            try {
                String fileName = formatFileName(sourcePath);
                String fullSourcePath = root + "/" + sourcePath;
                InputStream sourceStream = HostEnvSources.class.getResourceAsStream(fullSourcePath);
                if (sourceStream == null) {
                    throw new AnalysisException("Can't find resource " + fullSourcePath);
                }
                String code = Loader.getStringFromStream(sourceStream, "UTF-8");
                sources.add(JavaScriptSource.makeFileCode(fileName, code));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sources;
    }

    public static String formatFileName(String fileName) {
        return String.format("%s(%s)", filenNamePrefix, fileName);
    }

    public static boolean isHostEnvSource(SourceLocation sourceLocation) {
        if (DEBUG) {
            return false;
        }
        return sourceLocation.getFileName().startsWith(filenNamePrefix);
    }

    public static SourceLocation getLoaderDummySourceLocation() {
        return loaderDummySourceLocation;
    }
}
