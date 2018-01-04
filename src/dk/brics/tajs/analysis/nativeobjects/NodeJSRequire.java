package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import dk.brics.tajs.util.Pair;
import dk.brics.tajs.util.PathAndURLUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Models the NodeJS 'require.resolve' mechanism by running 'node'.
 */
public class NodeJSRequire {

    private static NodeJSRequire instance;

    private Map<Pair<String, URL>, URL> resolveCache;

    public NodeJSRequire() {
        this.resolveCache = newMap();
    }

    public static NodeJSRequire get() {
        if (instance == null) {
            throw new AnalysisException("NodeJSRequire is not initialized!");
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public static void init() {
        if (instance != null) {
            throw new AnalysisException("NodeJSRequire is already initialized!");
        }
        instance = new NodeJSRequire();
    }

    /**
     * A wrapper around the 'require.resolve' function of nodejs.
     * <p>
     * If 'require.resolve' could not resolve the given string, 'null' is returned.
     *
     * @param arg      as the thing to resolve
     * @param location as the location the resolution should start from
     */
    public URL resolve(String arg, URL location) {
        Pair<String, URL> cacheKey = Pair.make(arg, location);
        if (resolveCache.containsKey(cacheKey)) {
            return resolveCache.get(cacheKey);
        }
        // Whitelist-security: only accept paths with alphanumerics, periods, dashes, slashes and underscores
        // Example accepted path: ../foo-42.js
        if (!arg.matches("^[a-zA-Z0-9./:_-]+$")) {
            throw new AnalysisException("Unsupported (insecure) argument for require.resolve: " + arg);
        }

        URL resolved = null;
        if(!arg.startsWith(".") && !arg.startsWith("/")) {
            try {
                resolved = HostEnvSources.resolve(Paths.get("nodejs/modules").resolve(arg + ".js").toString());
            } catch (AnalysisException e) {}
        }
        if (resolved == null) {
            if (!"file".equals(location.getProtocol())) {
                // TODO: if we implement NodeJS's 'require.resolve' manually, we could support other protocols
                throw new AnalysisLimitationException.NodeJSRequireException(String.format("NodeJS process cannot 'resolve' inside %s-protocol file systems", location.getProtocol()));
            }
            String expression = "console.log(require.resolve('" + arg + "'))"; // <-- malicious injection possible here
            String[] cmd = {TAJSEnvironmentConfig.get().getNode().toString(), "-e", expression};
            final ProcessBuilder pb = new ProcessBuilder(cmd);
            Path resolveLocation = PathAndURLUtils.toPath(location).toAbsolutePath();
            pb.directory((Files.isDirectory(resolveLocation) ? resolveLocation : resolveLocation.getParent()).toFile());
            try {
                Process process = pb.start();
                BufferedReader brStd = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                List<String> lineStds = brStd.lines().collect(Collectors.toList());
                List<String> lineErrs = brErr.lines().collect(Collectors.toList());
                process.waitFor();
                if (process.exitValue() != 0) {
                    if (lineErrs.contains("Error: Cannot find module '" + arg + "'")) {
                        return null;
                    }
                    throw new AnalysisLimitationException.NodeJSRequireException("NodeJS process exited with exit code: " + process.exitValue());
                }
                if (lineStds.size() != 1) {
                    throw new AnalysisLimitationException.NodeJSRequireException("Unexpected output from NodeJS process: " + lineStds);
                }
                resolved = PathAndURLUtils.toURL(Paths.get(lineStds.get(0)));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        resolveCache.put(cacheKey, resolved);
        return resolved;
    }
}
