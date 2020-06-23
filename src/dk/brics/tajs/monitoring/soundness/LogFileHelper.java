/*
 * Copyright 2009-2020 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.monitoring.soundness;

import dk.au.cs.casa.jer.HashUtil;
import dk.au.cs.casa.jer.LogParser;
import dk.au.cs.casa.jer.Logger;
import dk.au.cs.casa.jer.Metadata;
import dk.au.cs.casa.jer.RawLogFile;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.options.SoundnessTesterOptions;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;
import dk.brics.tajs.util.PathAndURLUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utility class for creating, finding, and parsing log files.
 */
public class LogFileHelper {

    private final static boolean DEBUG = false;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogFileHelper.class);

    private static final String logSuffix = ".log";

    private static final String gzipSuffix = ".gz";

    private static final String loggzipSuffix = logSuffix + gzipSuffix;

    private static final Charset logFileEncoding = Charset.forName("UTF-8");

    private static WeakHashMap<URL, LogParser> cache = new WeakHashMap<>();

    /**
     * Typical locations for JavaScript files and their corresponding logs.
     */
    private final ResourceMap[] sourceToLogMapping = {
            new ResourceMap("test-resources/src", "test-resources", "logs"),
            new ResourceMap("benchmarks/tajs/src", "benchmarks", "tajs/logs"),
            new ResourceMap("out/temp-sources", "test-resources", "logs/temp-sources")
    };

    public LogFileHelper() {
        if (Arrays.stream(sourceToLogMapping).map(m -> m.subpath).collect(Collectors.toSet()).size() != sourceToLogMapping.length)
            throw new AnalysisException("Subpaths are used by the class loader to resolve the correct resource folder, hence they must be distinct");
    }

    public static LogParser makeLogParser(URL logFile) {
        if (!cache.containsKey(logFile)) {
            cache.put(logFile, new LogParser(buildRawLogFileFromURL(logFile)));
        }
        return cache.get(logFile);
    }

    /**
     * Reads a jalangilogger log file from a URL.
     */
    private static RawLogFile buildRawLogFileFromURL(URL logFile) {
        String path = logFile.getPath();
        if (path.endsWith(gzipSuffix)) {
            try (GZIPInputStream zis = new GZIPInputStream(logFile.openStream())) {
                return buildRawLogFileFromStream(zis);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (InputStream inputStream = logFile.openStream()) {
                return buildRawLogFileFromStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates a jalangilogger log file from an input stream.
     */
    private static RawLogFile buildRawLogFileFromStream(InputStream inputStream) {
        List<String> logFileLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), logFileEncoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                logFileLines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new RawLogFile(logFileLines);
    }

    /**
     * Returns the URL where the log file can be read.
     */
    public URL getLogFile() {
        return getLogFileLocation().runtimeLocation;
    }

    /**
     * Returns the URL of the log file, and if necessary generates the file.
     */
    public URL createOrGetLogFile() {
        LogFileLocation logFileLocation = getLogFileLocation();
        if (Options.get().getSoundnessTesterOptions().isRegenerate()) {
            // if log file is asked for multiple times, do only regenerate the first time
            Options.get().getSoundnessTesterOptions().setRegenerate(false);
            wipeLogFilesIfPossible(logFileLocation);
        }
        URL runtimeLocation = logFileLocation.runtimeLocation;
        boolean isConsumable = false, isEmpty = true;
        if ((isConsumable = PathAndURLUtils.isConsumable(runtimeLocation))
                && (!(isEmpty = isEmptyContent(runtimeLocation)))
                && (verifySha(logFileLocation, getMainFile()))) {
            return runtimeLocation;
        }
        boolean canProduceLogForFile = !KnownUnsoundnesses.isUnloggableMainFile(getMainFile());
        boolean canProduceLogAtAll = SoundnessTesterOptions.isLogCreationPossible();
        boolean isAllowedToProduceLog = Options.get().getSoundnessTesterOptions().isGenerate();
        if (DEBUG) {
            System.out.println(String.format("Log creation info \n" +
                            "    runtime-path: %s, \n" +
                            "    persistent-path: %s, \n" +
                            "    consumable: %b, empty: %b, unloggable-main-file: %b, log-creation-possible: %b, allowed-to-produce: %b)",
                    logFileLocation.runtimeLocation, logFileLocation.persistentLocation, isConsumable, isEmpty, canProduceLogForFile, canProduceLogAtAll, isAllowedToProduceLog));
        }
        boolean createLog = canProduceLogAtAll && canProduceLogForFile && isAllowedToProduceLog;
        if (!canProduceLogForFile) {
            warn("Could not create value log file: main file is known to be unsupported (limitation of the jalangilogger project)");
        } else if (!isAllowedToProduceLog) {
            throw new LogFileException("Log file does not exist, and creation of new log files is not enabled (use -generate-log)");
        } else if (!canProduceLogAtAll) {
            throw new LogFileException("Could not create value log file: missing jalangilogger installation (see README)");
        }
        if (createLog) {
            wipeLogFilesIfPossible(logFileLocation);
            RawLogFile rawLogFile = generateLogFile(getMainFile());
            Path runtimePath = PathAndURLUtils.toPath(runtimeLocation, false);
            try {
                Files.createDirectories(runtimePath.getParent());
                if (runtimePath.getFileName().toString().endsWith(gzipSuffix)) {
                    gzipLogFile(rawLogFile, runtimePath);
                } else {
                    Files.write(runtimePath, rawLogFile.getLines(), logFileEncoding);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (logFileLocation.persistentLocation.isPresent()) {
                Path persistentPath = PathAndURLUtils.toPath(logFileLocation.persistentLocation.get(), false);
                if (!persistentPath.equals(runtimePath)) {
                    try (InputStream persistentStream = runtimeLocation.openStream()) {
                        Files.createDirectories(persistentPath.getParent());
                        if (Files.copy(persistentStream, persistentPath, StandardCopyOption.REPLACE_EXISTING) <= 0)
                            throw new RuntimeException("No byte written when persisting a copy of the log file");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                warn("Creating log file, but not persisting it (a 'tajs' entry in tajs.properties is needed)");
            }
            return runtimeLocation;
        }
        return null;
    }

    private void wipeLogFilesIfPossible(LogFileLocation logFileLocation) {
        wipeFileIfPossible(logFileLocation.runtimeLocation);
        if (logFileLocation.persistentLocation.isPresent()) {
            wipeFileIfPossible(logFileLocation.persistentLocation.get());
        }
    }

    private boolean isEmptyContent(URL url) {
        try (InputStream is = url.openStream()) {
            return is.read() == -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtains the log file location from the -log-file option, or tries to guess it.
     */
    private LogFileLocation getLogFileLocation() {
        Optional<Path> slogFile = Options.get().getSoundnessTesterOptions().getExplicitSoundnessLogFile();
        if (slogFile.isPresent()) {
            URL url = PathAndURLUtils.toURL(slogFile.get());
            return new LogFileLocation(Optional.of(url), url);
        }
        return inferLogFilePosition();
    }

    /**
     * Finds the main file.
     */
    public Path getMainFile() {
        return Options.get().getArguments().get(Options.get().getArguments().size() - 1);
    }

    private Metadata getMetaData(URL firstLogFile) {
        return makeLogParser(firstLogFile).getMetadata();
    }

    private void gzipLogFile(RawLogFile logFileLines, Path target) throws IOException {
        String fileName = target.getFileName().toString();
        if (!fileName.endsWith(gzipSuffix)) {
            throw new IllegalArgumentException("Attempting to gzip to non-gzip file: " + target);
        }
        byte[] newLine = "\n".getBytes(logFileEncoding);
        try (FileOutputStream zipFile = new FileOutputStream(target.toFile()); GZIPOutputStream zipOut = new GZIPOutputStream(zipFile)) {
            logFileLines.getLines().forEach(line -> {
                try {
                    zipOut.write(line.getBytes(logFileEncoding));
                    zipOut.write(newLine);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Deletes the given file.
     */
    private void wipeFileIfPossible(URL location) {
        cache.remove(location);
        Path relativeLogFilePath = PathAndURLUtils.toPath(location, false);
        try {
            Files.deleteIfExists(relativeLogFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Guesses the location of the log file for soundness testing of a JavaScript file using the source to log mapping.
     * When invoked in an IDE, both the build folder resource folder and the original resource folder are taken into account.
     * If the one in the original resource folder one is not available for any reason, then the only one returned is the one
     * accessible through resources.
     * <p>
     * Examples:
     * <pre>
     *     test-resources/src/bar/baz.js -> test-resources/logs/bar/baz.js.log
     * </pre>
     */
    private LogFileLocation inferLogFilePosition() {
        Path mainFile = getMainFile();
        if (mainFile.isAbsolute())
            mainFile = attemptToRelativizeAbsolutePath(mainFile);
        String suffix = Options.get().getSoundnessTesterOptions().isUseUncompressedLogFileForInference() ? logSuffix : loggzipSuffix;
        Path preMappedLogFile = Paths.get(mainFile + suffix);
        List<ResourceMap> mappers = Arrays.stream(sourceToLogMapping)
                .filter(m -> m.map(preMappedLogFile).isPresent())
                .collect(Collectors.toList());
        if (mappers.size() != 1)
            throw new AnalysisException("Unable to determine log file for " + mainFile + ", viable mappers: " + (mappers.isEmpty() ? "none" : mappers) + " (try option -log-file)");
        ResourceMap m = mappers.get(0);
        Path mappedLogFilePath = m.map(preMappedLogFile).get();
        URL runtimeLogFileLocation = m.mapToResource(preMappedLogFile).get();
        Optional<URL> persistentLogFileLocation = inferPersistentLogFileLocation(mappedLogFilePath);
        return new LogFileLocation(persistentLogFileLocation, runtimeLogFileLocation);
    }

    private Path attemptToRelativizeAbsolutePath(Path mainFile) { // TODO: use PathAndURLUtils.getRelativeToTAJS?
        if (TAJSEnvironmentConfig.get().hasProperty("tajs")) {
            Path tajs = Paths.get(TAJSEnvironmentConfig.get().getCustom("tajs"));
            Path resolved = PathAndURLUtils.toRealPath(mainFile); // resolve symlinks
            boolean inTAJSDirectory = resolved.startsWith(PathAndURLUtils.toRealPath(tajs));
            if (inTAJSDirectory) {
                return tajs.relativize(resolved);
            }
        }
        throw new IllegalArgumentException("Only relative paths supported for log file inference (maybe a 'tajs' entry in tajs.properties would help): " + mainFile);
    }

    private Optional<URL> inferPersistentLogFileLocation(Path logFile) {
        if (!TAJSEnvironmentConfig.get().hasProperty("tajs")) {
            return Optional.empty();
        }
        return Optional.of(PathAndURLUtils.toURL(Paths.get(TAJSEnvironmentConfig.get().getCustom("tajs")).resolve(logFile)));
    }

    /**
     * Generates log file for the given JavaScript application, using jalangilogger with an appropriate environment (Node.js / default browser / etc.).
     */
    private RawLogFile generateLogFile(Path testFile) {
        Path jalangilogger = SoundnessTesterOptions.getJalangiLogger();
        Optional<Integer> explicitTimeLimit = Options.get().getSoundnessTesterOptions().getTimeLimitExplicitly();
        Optional<Integer> explicitInstrumentationTimeLimit = Options.get().getSoundnessTesterOptions().getInstrumentationTimeLimitExplicitly();
        Optional<Logger.Environment> explicitEnvironment = Options.get().getSoundnessTesterOptions().getGeneratorEnvironmentExplicitly();
        boolean hasGraalVmNode = TAJSEnvironmentConfig.get().hasProperty("graalVmNode");
        Logger.Environment environment = explicitEnvironment.orElseGet(() -> {
            if (Options.get().isDOMEnabled())
                if (Options.get().getSoundnessTesterOptions().isNonInteractive())
                    return Logger.Environment.DRIVEN_BROWSER;
                else
                    return Logger.Environment.BROWSER;
            else if (Options.get().isNodeJS())
                return hasGraalVmNode ? Logger.Environment.NODE_PROF : Logger.Environment.NODE;
            else
                return hasGraalVmNode ? Logger.Environment.NODE_PROF_GLOBAL : Logger.Environment.NODE_GLOBAL;
        });
        Path node = TAJSEnvironmentConfig.get().getNode();
        Path jjs = environment == Logger.Environment.NASHORN ? Paths.get(TAJSEnvironmentConfig.get().getCustom("jjs")) : null;
        Path graalVmNode = environment == Logger.Environment.NODE_PROF || environment == Logger.Environment.NODE_PROF_GLOBAL ? Paths.get(TAJSEnvironmentConfig.get().getCustom("graalVmNode")) : null;
        Integer timeLimit = explicitTimeLimit.orElse(30);
        Integer instrumentationTimeLimit = explicitInstrumentationTimeLimit.orElse(30);
        try {
            Path customRootDirectory = getCustomRootDirectoryForTest(testFile);
            List<Path> preambles = getPreambles();
            Optional<Set<Path>> onlyInclude = Options.get().getSoundnessTesterOptions().getOnlyIncludesForInstrumentation();
            info("Generating log file for soundness testing...");
            if (customRootDirectory == null) {
                return Logger.makeLoggerForIndependentMainFile(testFile, preambles, onlyInclude, instrumentationTimeLimit, timeLimit, environment, node, jalangilogger, jjs, graalVmNode).log();
            } else {
                Path rootRelativeMain = customRootDirectory.relativize(testFile.toAbsolutePath());
                return Logger.makeLoggerForDirectoryWithMainFile(customRootDirectory, rootRelativeMain, preambles, onlyInclude, instrumentationTimeLimit, timeLimit, environment, node, jalangilogger, jjs, graalVmNode).log();
            }
        } catch (IOException e) {
            throw new LogFileException("Failed to create new log file", e);
        }
    }

    /**
     * Collects all non-main files being analyzed.
     */
    private List<Path> getPreambles() {
        List<Path> args = Options.get().getArguments();
        List<Path> preambles = new ArrayList<>();
        for (int i = 0; i < args.size() - 1; i++) {
            Path preamble = args.get(i).toAbsolutePath();
            preambles.add(preamble);
        }
        return preambles;
    }

    private Path getCustomRootDirectoryForTest(Path main) {
        Path rootDirectoryFromMainDirectory = Options.get().getSoundnessTesterOptions().getRootDirFromMainDirectory();
        if (rootDirectoryFromMainDirectory == null) {
            return null;
        }
        return main.getParent().resolve(rootDirectoryFromMainDirectory).toAbsolutePath().normalize();
    }

    /**
     * Checks whether the SHA stored in the log file matches the given file or directory.
     */
    private boolean verifySha(LogFileLocation logFileLocation, Path jsFile) {
        Metadata metadata = getMetaData(logFileLocation.runtimeLocation);
        String expectedSha = metadata.getSha();
        Path rootDirectoryFromMain = getCustomRootDirectoryForTest(jsFile);
        String currentSha;
        if (Options.get().getSoundnessTesterOptions().getOnlyIncludesForInstrumentation().isPresent()) {
            currentSha = HashUtil.shaDirOrFile(Options.get().getSoundnessTesterOptions().getOnlyIncludesForInstrumentation().get());
        } else {
            if (rootDirectoryFromMain == null) {
                currentSha = HashUtil.shaDirOrFile(jsFile);
            } else {
                currentSha = HashUtil.shaDirOrFile(getCustomRootDirectoryForTest(jsFile));
            }
        }
        if (currentSha.equals(expectedSha)) {
            return true;
        }
        if (Options.get().getSoundnessTesterOptions().isIgnoreShaDifference()) {
            warn("Ignoring SHA-mismatch for %s (expected %s, got %s)", metadata.getRoot(), expectedSha, currentSha);
            return true;
        }
        if (Options.get().getSoundnessTesterOptions().isForceUpdateSha()) {
            warn("Force-updating SHA-mismatch for %s (expected %s, got %s)", metadata.getRoot(), expectedSha, currentSha);
            forceUpdateSha(PathAndURLUtils.toPath(logFileLocation.runtimeLocation, false), expectedSha, currentSha);
            if (logFileLocation.persistentLocation.isPresent()) {
                forceUpdateSha(PathAndURLUtils.toPath(logFileLocation.persistentLocation.get(), false), expectedSha, currentSha);
            }
            return true;
        }
        return false;
    }

    private void info(String msg) {
        if (!Options.get().isQuietEnabled()) {
            log.info(msg);
        }
    }

    private void warn(String format, Object... args) {
        log.warn(String.format(format, args));
    }

    /**
     * Updates the registered SHA in the metadata of a log-file.
     */
    private void forceUpdateSha(Path logFile, String oldSha, String newSha) {
        try {
            RawLogFile rawLogFile = buildRawLogFileFromURL(PathAndURLUtils.toURL(logFile));
            List<String> lines = rawLogFile.getLines();
            lines.set(0, lines.get(0).replaceAll(oldSha, newSha)); // hacky, but easy
            gzipLogFile(rawLogFile, logFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Bean for the location of the log file.
     */
    private class LogFileLocation {

        /**
         * The persistent location of the log file. This will typically be in TAJS/test-resources/logs directory or TAJS/benchmarks/tajs/logs.
         * This location is used for creating/updating log files, which in turn will be copied to the runtimeLocation during a later build step.
         */
        private final Optional<URL> persistentLocation;

        /**
         * The location of the log file in the current runtime. This will typically be in a build-folder or inside a jar.
         */
        private final URL runtimeLocation;

        public LogFileLocation(Optional<URL> persistentLocation, URL runtimeLocation) {
            this.persistentLocation = persistentLocation;
            this.runtimeLocation = runtimeLocation;
        }
    }

    /**
     * Exception for situations where there is something wrong with a log file.
     */
    public static class LogFileException extends RuntimeException {

        public LogFileException(String s) {
            super(s);
        }

        public LogFileException(String s, Throwable t) {
            super(s, t);
        }
    }

    private static class ResourceMap {

        /**
         * The substring of the path that is mapped.
         */
        Path originalPathPart;

        /**
         * Name of resource folder, used when transforming a path of the form
         * <p>
         * test-resources/subpath...
         * <p>
         * into a resource path
         * <p>
         * /subpath...
         */
        Path resourceFolder;

        /**
         * The subpath from the resource folder.
         */
        Path subpath;

        ResourceMap(String originalPathPart, String resourceFolder, String subpath) {
            this.originalPathPart = Paths.get(originalPathPart);
            this.resourceFolder = Paths.get(resourceFolder);
            this.subpath = Paths.get(subpath);
            if (!this.resourceFolder.resolve(this.subpath).normalize().startsWith(resourceFolder))
                throw new AnalysisException("Subpath cannot point to a parent of the resource folder");
            if (this.resourceFolder.resolve(this.subpath).equals(this.resourceFolder))
                throw new AnalysisException("Need to specify a subfolder as subpath");
        }

        /**
         * Maps the given path according to this rule, or returns empty if no match.
         */
        public Optional<Path> map(Path p) {
            Path mapping = resourceFolder.resolve(subpath);
            if (p.startsWith(originalPathPart)) {
                return Optional.of(mapping.resolve(originalPathPart.relativize(p)));
            }
            return Optional.empty();
        }

        /**
         * Maps the given path according to this rule to produce a URL for reading, or returns empty if no match.
         */
        public Optional<URL> mapToResource(Path p) {
            return map(p).map(m -> {
                Path folderInResourceFolder = resourceFolder.resolve(subpath);
                URL containing = Thread.currentThread().getContextClassLoader().getResource(subpath + "/");
                if (containing == null)
                    throw new AnalysisException("Could not find folder in resource folder " + subpath + " (hint: use option -log-file)");
                Path relative = folderInResourceFolder.relativize(m);
                try {
                    return new URL(containing, relative.toString());
                } catch (MalformedURLException e) {
                    throw new AnalysisException("Malformed URL: " + folderInResourceFolder + "/" + relative);
                }
            });
        }

        @Override
        public String toString() {
            return originalPathPart + " -> " + resourceFolder.resolve(subpath);
        }
    }
}
