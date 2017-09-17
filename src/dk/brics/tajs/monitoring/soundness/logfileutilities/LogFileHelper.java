/*
 * Copyright 2009-2017 Aarhus University
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

package dk.brics.tajs.monitoring.soundness.logfileutilities;

import dk.au.cs.casa.jer.HashUtil;
import dk.au.cs.casa.jer.LogParser;
import dk.au.cs.casa.jer.Logger;
import dk.au.cs.casa.jer.Metadata;
import dk.au.cs.casa.jer.RawLogFile;
import dk.brics.tajs.analysis.KnownUnsoundnesses;
import dk.brics.tajs.options.OptionValues;
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
 * Utility class for creating, finding, or parsing the log file for a specific application.
 */
public class LogFileHelper {

    private final static boolean DEBUG = false;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogFileHelper.class);

    private static final String logSuffix = ".log";

    private static final String gzipSuffix = ".gz";

    private static final String loggzipSuffix = logSuffix + gzipSuffix;

    private static final Charset logFileEncoding = Charset.forName("UTF-8");

    private static WeakHashMap<URL, LogParser> cache = new WeakHashMap<>();

    private final OptionValues options;

    private final ResourceMap[] sourceToLogMapping = {
            new ResourceMap("test-resources/src", "test-resources", "logs"),
            new ResourceMap("benchmarks/tajs/src", "benchmarks", "tajs/logs"),
            new ResourceMap("out/temp-sources", "test-resources", "logs/temp-sources")
    };

    public LogFileHelper(OptionValues options) {
        this.options = options;
        if (Arrays.stream(sourceToLogMapping).map(m -> m.subpath).collect(Collectors.toSet()).size() != sourceToLogMapping.length)
            throw new AnalysisException("Subpaths are used by the class loader to resolve the correct resource folder, hence they must be distinct");
    }

    public static LogParser makeLogParser(URL logFile) {
        if (!cache.containsKey(logFile)) {
            cache.put(logFile, new LogParser(buildRawLogFileFromURL(logFile)));
        }
        return cache.get(logFile);
    }

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

    public URL getLogFile() {
        return getAllLogFilePositions().runtimeLocation;
    }

    public URL createOrGetLogFile() {
        LogFileLocations logFileLocations = getAllLogFilePositions();

        if (options.getSoundnessTesterOptions().isRegenerate()) {
            wipeLogFilesIfPossible(logFileLocations);
        }

        URL runtimeLocation = logFileLocations.runtimeLocation;
        boolean isConsumable = false, isEmpty = true;
        if ((isConsumable = PathAndURLUtils.isConsumable(runtimeLocation))
                && (!(isEmpty = isEmptyContent(runtimeLocation)))
                && (verifySha(logFileLocations, getMainFile()))) {
            return runtimeLocation;
        }

        boolean canProduceLogForFile = !KnownUnsoundnesses.isUnloggableMainFile(getMainFile());
        boolean canProduceLogAtAll = SoundnessTesterOptions.isLogCreationPossible();
        boolean isAllowedToProduceLog = options.getSoundnessTesterOptions().isGenerate();

        if(DEBUG) {
            System.out.println(String.format("Log creation info \n" +
                            "    runtime-path: %s, \n" +
                            "    persistent-path: %s, \n" +
                            "    consumable: %b, empty: %b, unloggable-main-file: %b, log-creation-possible: %b, allowed-to-produce: %b)",
                    logFileLocations.runtimeLocation, logFileLocations.persistentLocation, isConsumable, isEmpty, canProduceLogForFile, canProduceLogAtAll, isAllowedToProduceLog));
        }

        boolean createLog = canProduceLogAtAll && canProduceLogForFile && isAllowedToProduceLog;

        if (!canProduceLogForFile) {
            warn("Could not create value log file: main file is known to be unsupported (limitation of the jalangilogger project)");
        } else if (!isAllowedToProduceLog) {
            throw new LogFileException("Could not create value log file: creation of new log files is not enabled");
        } else if (!canProduceLogAtAll) {
            throw new LogFileException("Could not create value log file: missing jalangilogger installation (see README)");
        }

        if (createLog) {
            wipeLogFilesIfPossible(logFileLocations);
            RawLogFile rawLogFile = generateLogFile(getMainFile());
            Path runtimePath = PathAndURLUtils.toPath(runtimeLocation);
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
            if (logFileLocations.persistentLocation.isPresent()) {
                Path persistentPath = PathAndURLUtils.toPath(logFileLocations.persistentLocation.get());
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
                warn("Creating log file, but not persisting it (a 'tajs' entry in .tajsconfig is needed)");
            }

            return runtimeLocation;
        }

        return null;
    }

    private void wipeLogFilesIfPossible(LogFileLocations logFileLocations) {
        wipeFileIfPossible(logFileLocations.runtimeLocation);
        if (logFileLocations.persistentLocation.isPresent()) {
            wipeFileIfPossible(logFileLocations.persistentLocation.get());
        }
    }

    private boolean isEmptyContent(URL url) {
        try (InputStream is = url.openStream()) {
            return is.read() == -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LogFileLocations getAllLogFilePositions() {
        Optional<Path> slogFile = options.getSoundnessTesterOptions().getExplicitSoundnessLogFile();
        if (slogFile.isPresent()) {
            URL url = PathAndURLUtils.toURL(slogFile.get());
            return new LogFileLocations(Optional.of(url), url);
        }
        return inferLogFilePosition();
    }

    public Path getMainFile() {
        return options.getArguments().get(Options.get().getArguments().size() - 1);
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

    private void wipeFileIfPossible(URL location) {
        cache.remove(location);
        Path relativeLogFilePath = PathAndURLUtils.toPath(location);
        try {
            Files.deleteIfExists(relativeLogFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Infers all the locations of a log file for soundness testing of a JavaScript file using the source to log mapping.
     * When invoked in an IDE, both the build folder resource folder and the original resource folder are taken into account.
     * If the one in the original resource folder one is not available for any reason, the the only one returned is the one
     * accessible through resources.
     * <p>
     * Examples:
     * <pre>
     *     test-resources/src/bar/baz.js -> test-resources/logs/bar/baz.js.log
     * </pre>
     */
    private LogFileLocations inferLogFilePosition() {
        Path mainFile = getMainFile();
        if (mainFile.isAbsolute()) {
            mainFile = attemptToRelativizeAbsolutePath(mainFile);
        }

        String suffix = options.getSoundnessTesterOptions().isUseUncompressedLogFileForInference() ? logSuffix : loggzipSuffix;

        Path preMappedLogFile = Paths.get(makeLogFileNameFromPrefix(mainFile, suffix));
        List<ResourceMap> mappers = Arrays.stream(sourceToLogMapping)
                .filter(m -> m.map(preMappedLogFile).isPresent())
                .collect(Collectors.toList());

        if(mappers.size() != 1)
            throw new AnalysisException("Expected to be able to map " + mainFile + " to its logs location, viable mappers: " + mappers);

        Path mappedLogFilePath = mappers.get(0).map(preMappedLogFile).get();
        URL runtimeLogFileLocation = mappers.get(0).mapToResource(preMappedLogFile).get();

        Optional<URL> persistentLogFileLocation = inferPersistentLogFileLocation(mappedLogFilePath);

        return new LogFileLocations(persistentLogFileLocation, runtimeLogFileLocation);
    }

    private String makeLogFileNameFromPrefix(Path mainFile, String suffix) {
        return String.format("%s%s", mainFile, suffix);
    }

    private Path attemptToRelativizeAbsolutePath(Path mainFile) {
        if (TAJSEnvironmentConfig.get().hasProperty("tajs")) {
            Path tajs = Paths.get(TAJSEnvironmentConfig.get().getCustom("tajs"));
            try {
                Path resolved = mainFile.toRealPath(); // resolve symlinks
                boolean inTAJSDirectory = resolved.toAbsolutePath().startsWith(tajs.toAbsolutePath());
                if (inTAJSDirectory) {
                    return tajs.relativize(resolved);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException("Only relative paths supported for log file inference (maybe a 'tajs' entry in .tajsconfig would help): " + mainFile);
    }

    private Optional<URL> inferPersistentLogFileLocation(Path logFile) {
        String tajsConfigPropertyName = "tajs";
        if (!TAJSEnvironmentConfig.get().hasProperty(tajsConfigPropertyName)) {
            return Optional.empty();
        }
        Path persistentFilePath = Paths.get(TAJSEnvironmentConfig.get().getCustom(tajsConfigPropertyName)).resolve(logFile);
        return Optional.of(PathAndURLUtils.toURL(persistentFilePath));
    }

    private RawLogFile generateLogFile(Path testFile) {
        Path jalangilogger = SoundnessTesterOptions.getJalangiLogger();
        Path node = TAJSEnvironmentConfig.get().getNode();
        Path jjs = Paths.get(TAJSEnvironmentConfig.get().getCustom("jjs"));

        Optional<Integer> explicitTimeLimit = options.getSoundnessTesterOptions().getTimeLimitExplicitly();
        Optional<Integer> explicitInstrumentationTimeLimit = options.getSoundnessTesterOptions().getInstrumentationTimeLimitExplicitly();
        Optional<Logger.Environment> explicitEnvironment = options.getSoundnessTesterOptions().getGeneratorEnvironmentExplicitly();
        Logger.Environment environment = explicitEnvironment.orElseGet(() ->
                options.isDOMEnabled() ?
                        (options.getSoundnessTesterOptions().isNonInteractive() ?
                                Logger.Environment.DRIVEN_BROWSER : Logger.Environment.BROWSER)
                        :
                        Logger.Environment.NODE_GLOBAL);
        Integer timeLimit = explicitTimeLimit.orElse(30);
        Integer instrumentationTimeLimit = explicitInstrumentationTimeLimit.orElse(30);
        try {
            Path customRootDirectory = getCustomRootDirectoryForTest(testFile);
            List<Path> preambles = getPreambles();
            Optional<Set<Path>> onlyInclude = options.getSoundnessTesterOptions().getOnlyIncludesForInstrumentation();
            if (customRootDirectory == null) {
                return Logger.makeLoggerForIndependentMainFile(testFile, preambles, onlyInclude, instrumentationTimeLimit, timeLimit, environment, node, jalangilogger, jjs).log();
            } else {
                Path rootRelativeMain = customRootDirectory.relativize(testFile.toAbsolutePath());
                return Logger.makeLoggerForDirectoryWithMainFile(customRootDirectory, rootRelativeMain, preambles, onlyInclude, instrumentationTimeLimit, timeLimit, environment, node, jalangilogger, jjs).log();
            }
        } catch (IOException e) {
            throw new LogFileException("Failed to create new log file", e);
        }
    }

    private List<Path> getPreambles() {
        List<Path> args = options.getArguments();
        List<Path> preambles = new ArrayList<>();
        //The last element in args is the main file
        //Everything else is assumed to be a preamble
        for (int i = 0; i < args.size() - 1; i++) {
            Path preamble = args.get(i).toAbsolutePath();
            preambles.add(preamble);
        }
        return preambles;
    }

    private Path getCustomRootDirectoryForTest(Path main) {
        Path rootDirectoryFromMainDirectory = options.getSoundnessTesterOptions().getRootDirFromMainDirectory();
        if (rootDirectoryFromMainDirectory == null) {
            return null;
        }
        return main.getParent().resolve(rootDirectoryFromMainDirectory).toAbsolutePath().normalize();
    }

    private boolean verifySha(LogFileLocations logFileLocations, Path jsFile) {
        Metadata metadata = getMetaData(logFileLocations.runtimeLocation);
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

        if (options.getSoundnessTesterOptions().isIgnoreShaDifference()) {
            warn("Ignoring SHA-mismatch for %s (expected %s, got %s)", metadata.getRoot(), expectedSha, currentSha);
            return true;
        }
        if (options.getSoundnessTesterOptions().isForceUpdateSha()) {
            warn("Force-updating SHA-mismatch for %s (expected %s, got %s)", metadata.getRoot(), expectedSha, currentSha);
            forceUpdateSha(PathAndURLUtils.toPath(logFileLocations.runtimeLocation), expectedSha, currentSha);
            if (logFileLocations.persistentLocation.isPresent()) {
                forceUpdateSha(PathAndURLUtils.toPath(logFileLocations.persistentLocation.get()), expectedSha, currentSha);
            }
            return true;
        }

        return false;
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
     * Bean for the location of the log files to make use of.
     */
    private class LogFileLocations {

        /**
         * The persistent location of the log file. This will typically be the TAJS/resources/jalangilogfiles directory.
         * This location is used for creating/updating log files, which in turn will be copied to the runtimeLocation during a later build step.
         */
        private final Optional<URL> persistentLocation;

        /**
         * The location of the log file in the current runtime. This will typically be in a build-folder or inside a jar.
         */
        private final URL runtimeLocation;

        public LogFileLocations(Optional<URL> persistentLocation, URL runtimeLocation) {
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
         * Name of resource folder, used when transorming a path of the form
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

        public Optional<Path> map(Path p) {
            Path mapping = resourceFolder.resolve(subpath);
            if (p.startsWith(originalPathPart)) {
                return Optional.of(mapping.resolve(originalPathPart.relativize(p)));
            }
            return Optional.empty();
        }

        public Optional<URL> mapToResource(Path p) {
            Optional<Path> mapped = map(p);
            return mapped.map(m -> {
                Path folderInResourceFolder = resourceFolder.resolve(subpath);
                URL containing = Thread.currentThread().getContextClassLoader().getResource(subpath + "/");
                if (containing == null)
                    throw new AnalysisException("Could not find folder in resource folder " + subpath);
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
