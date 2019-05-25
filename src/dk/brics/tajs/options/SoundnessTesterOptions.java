/*
 * Copyright 2009-2019 Aarhus University
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

package dk.brics.tajs.options;

import dk.au.cs.casa.jer.Logger;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

/**
 * Options to be used for soundness testing.
 */
public class SoundnessTesterOptions {
   // TODO separate further into options for soundness testing and options for log files.
   // TODO somehow include the information from KnownUnsoundness here, to allow for configurable known unsoundnesses (will violate package dependencies if done naively).

    /**
     * Should soundness testing be performed.
     */
    private boolean test = false;

    /**
     * Can log files be generated if needed.
     */
    private boolean generate = false;

    /**
     * Force regeneration of log files.
     */
    private boolean regenerate = false;

    /**
     * The relative path from the main file to the directory of the entire application to soundness test. Is null for single-file applications. For applications where the main file is in the root directory of the application, the value is Paths.get(".").
     * If this path is wrong, then log file generation will happen on too few files.
     */
    private Path rootDirFromMainDirectory = null;

    /**
     * The location of the log file. If not present, the log file is expected to be located in /test-resources/jalangilogfiles/.
     */
    private Optional<Path> explicitSoundnessLogFile = Optional.empty();

    /**
     * Use headless browser for browser for generating log files in a browser.
     */
    private boolean nonInteractive = false;

    /**
     * The environment to generate the log file in. If not present, an environment is heuristically selected.
     */
    private Optional<Logger.Environment> generatorEnvironmentExplicitly = Optional.empty();

    /**
     * The time (in seconds) the application is allowed to run when generating log files. If not present, a default value is chosen (10 seconds at the time of writing).
     */
    private Optional<Integer> timeLimitExplicitly = Optional.empty();

    /**
     * The time (in seconds) the instrumentation is allowed to run when generating log files. If not present, a default value is chosen (10 seconds at the time of writing).
     */
    private Optional<Integer> instrumentationTimeLimitExplicitly = Optional.empty();

    /**
     * The files that should be instrumented. If not present, all files in the application directory are instrumented. Useful for instrumenting applications with many irrelevant files (e.g. test files).
     */
    private Optional<Set<Path>> onlyIncludesForInstrumentation = Optional.empty();

    /**
     * Use log files regardless of their sha-value. For development only.
     */
    private boolean ignoreShaDifference = false;

    /**
     * Do not compress log files.
     */
    private boolean useUncompressedLogFileForInference = false;

    /**
     * When soundness testing, print errors instead of throwing exception.
     */
    private boolean printErrorsWithoutThrowingException = false;

    /**
     * Update the sha in log files without regenerating the entire log file. For development only.
     */
    private boolean forceUpdateSha = false;

    /**
     * Generates the log files before performing the analysis
     */
    private boolean generateBeforeAnalysis = false;

    /**
     * Automatically detects which files should be instrumented and sets rootDirFromMainDirectory, based on the main JavaScript file.
     */
    private boolean generateOnlyIncludeAutomatically = false;

    /**
     * Automatically detects which files should be instrumented and sets rootDirFromMainDirectory, based on the main HTML file.
     */
    private boolean generateOnlyIncludeAutomaticallyForHTMLFiles = false;

    public static Path getJalangiLogger() {
        return ExternalDependencies.getJalangiLoggerDirectory().get();
    }

    public static boolean isLogCreationPossible() {
        return ExternalDependencies.getJalangiLoggerDirectory().isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundnessTesterOptions that = (SoundnessTesterOptions) o;

        if (test != that.test) return false;
        if (generate != that.generate) return false;
        if (regenerate != that.regenerate) return false;
        if (nonInteractive != that.nonInteractive) return false;
        if (ignoreShaDifference != that.ignoreShaDifference) return false;
        if (useUncompressedLogFileForInference != that.useUncompressedLogFileForInference) return false;
        if (generateBeforeAnalysis != that.generateBeforeAnalysis) return false;
        if (generateOnlyIncludeAutomatically != that.generateOnlyIncludeAutomatically) return false;
        if (generateOnlyIncludeAutomaticallyForHTMLFiles != that.generateOnlyIncludeAutomaticallyForHTMLFiles) return false;
        if (rootDirFromMainDirectory != null ? !rootDirFromMainDirectory.equals(that.rootDirFromMainDirectory) : that.rootDirFromMainDirectory != null)
            return false;
        if (explicitSoundnessLogFile != null ? !explicitSoundnessLogFile.equals(that.explicitSoundnessLogFile) : that.explicitSoundnessLogFile != null)
            return false;
        if (generatorEnvironmentExplicitly != null ? !generatorEnvironmentExplicitly.equals(that.generatorEnvironmentExplicitly) : that.generatorEnvironmentExplicitly != null)
            return false;
        if (timeLimitExplicitly != null ? !timeLimitExplicitly.equals(that.timeLimitExplicitly) : that.timeLimitExplicitly != null)
            return false;
        if (instrumentationTimeLimitExplicitly != null ? !instrumentationTimeLimitExplicitly.equals(that.instrumentationTimeLimitExplicitly) : that.instrumentationTimeLimitExplicitly != null)
            return false;
        return onlyIncludesForInstrumentation != null ? onlyIncludesForInstrumentation.equals(that.onlyIncludesForInstrumentation) : that.onlyIncludesForInstrumentation == null;
    }

    @Override
    public int hashCode() {
        int result = (test ? 1 : 0);
        result = 31 * result + (generate ? 1 : 0);
        result = 31 * result + (regenerate ? 1 : 0);
        result = 31 * result + (rootDirFromMainDirectory != null ? rootDirFromMainDirectory.hashCode() : 0);
        result = 31 * result + (explicitSoundnessLogFile != null ? explicitSoundnessLogFile.hashCode() : 0);
        result = 31 * result + (nonInteractive ? 1 : 0);
        result = 31 * result + (generatorEnvironmentExplicitly != null ? generatorEnvironmentExplicitly.hashCode() : 0);
        result = 31 * result + (timeLimitExplicitly != null ? timeLimitExplicitly.hashCode() : 0);
        result = 31 * result + (instrumentationTimeLimitExplicitly != null ? instrumentationTimeLimitExplicitly.hashCode() : 0);
        result = 31 * result + (onlyIncludesForInstrumentation != null ? onlyIncludesForInstrumentation.hashCode() : 0);
        result = 31 * result + (ignoreShaDifference ? 1 : 0);
        result = 31 * result + (useUncompressedLogFileForInference ? 1 : 0);
        result = 31 * result + (generateBeforeAnalysis ? 1 : 0);
        result = 31 * result + (generateOnlyIncludeAutomatically ? 1 : 0);
        result = 31 * result + (generateOnlyIncludeAutomaticallyForHTMLFiles ? 1 : 0);
        return result;
    }

    public Optional<Integer> getInstrumentationTimeLimitExplicitly() {
        return instrumentationTimeLimitExplicitly;
    }

    public void setInstrumentationTimeLimitExplicitly(Optional<Integer> instrumentationTimeLimitExplicitly) {
        this.instrumentationTimeLimitExplicitly = instrumentationTimeLimitExplicitly;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public Optional<Path> getExplicitSoundnessLogFile() {
        return explicitSoundnessLogFile;
    }

    public void setExplicitSoundnessLogFile(Optional<Path> file) {
        this.explicitSoundnessLogFile = file;
    }

    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public boolean isRegenerate() {
        return regenerate;
    }

    public void setRegenerate(boolean regenerate) {
        this.regenerate = regenerate;
    }

    public boolean isNonInteractive() {
        return nonInteractive;
    }

    public void setNonInteractive(boolean nonInteractive) {
        this.nonInteractive = nonInteractive;
    }

    public Path getRootDirFromMainDirectory() {
        return rootDirFromMainDirectory;
    }

    public void setRootDirFromMainDirectory(Path rootDirFromMainDirectory) {
        this.rootDirFromMainDirectory = rootDirFromMainDirectory;
    }

    public Optional<Logger.Environment> getGeneratorEnvironmentExplicitly() {
        return generatorEnvironmentExplicitly;
    }

    public void setGeneratorEnvironmentExplicitly(Optional<Logger.Environment> generatorEnvironmentExplicitly) {
        this.generatorEnvironmentExplicitly = generatorEnvironmentExplicitly;
    }

    public Optional<Integer> getTimeLimitExplicitly() {
        return timeLimitExplicitly;
    }

    public void setTimeLimitExplicitly(Optional<Integer> timeLimitExplicitly) {
        this.timeLimitExplicitly = timeLimitExplicitly;
    }

    public Optional<Set<Path>> getOnlyIncludesForInstrumentation() {
        return onlyIncludesForInstrumentation;
    }

    public void setOnlyIncludesForInstrumentation(Optional<Set<Path>> onlyIncludesForInstrumentation) {
        this.onlyIncludesForInstrumentation = onlyIncludesForInstrumentation;
    }

    public boolean isIgnoreShaDifference() {
        return ignoreShaDifference;
    }

    public boolean isUseUncompressedLogFileForInference() {
        return useUncompressedLogFileForInference;
    }

    public void setUseUncompressedLogFileForInference(boolean useUncompressedLogFileForInference) {
        this.useUncompressedLogFileForInference = useUncompressedLogFileForInference;
    }

    public boolean isPrintErrorsWithoutThrowingException() {
        return printErrorsWithoutThrowingException;
    }

    public void setPrintErrorsWithoutThrowingException(boolean printErrorsWithoutThrowingException) {
        this.printErrorsWithoutThrowingException = printErrorsWithoutThrowingException;
    }

    public boolean isForceUpdateSha() {
        return forceUpdateSha;
    }

    public void setForceUpdateSha(boolean forceUpdateSha) {
        this.forceUpdateSha = forceUpdateSha;
    }

    public boolean generateBeforeAnalysis() {
        return generateBeforeAnalysis;
    }

    public void setGenerateBeforeAnalysis(boolean b) {
        generateBeforeAnalysis = true;
    }

    public void setGenerateOnlyIncludeAutomatically(boolean generateOnlyIncludeAutomatically) {
        this.generateOnlyIncludeAutomatically = generateOnlyIncludeAutomatically;
    }

    public boolean isGenerateOnlyIncludeAutomatically() {
        return generateOnlyIncludeAutomatically;
    }

    public void setGenerateOnlyIncludeAutomaticallyForHTMLFiles(boolean generateOnlyIncludeAutomaticallyForHTMLFiles) {
        this.generateOnlyIncludeAutomaticallyForHTMLFiles = generateOnlyIncludeAutomaticallyForHTMLFiles;
    }

    public boolean isGenerateOnlyIncludeAutomaticallyForHTMLFiles() {
        return generateOnlyIncludeAutomaticallyForHTMLFiles;
    }
}
