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

package dk.brics.tajs.jsdelta;

import dk.brics.tajs.jsdelta.util.JSDeltaCommandLineInterfaceToJava;
import dk.brics.tajs.jsdelta.util.JavaInterfaceForJSDelta;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Java interface to JSDelta.
 */
public class DeltaMinimizer {

    private static final String predicatePreamble;

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private static final Logger log = Logger.getLogger(DeltaMinimizer.class);

    static {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(DeltaMinimizer.class.getResourceAsStream("/jsdelta/jsdelta-TAJS-predicate.js")))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(String.format("%s%n", line));
            }
            predicatePreamble = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience method for {@link #reduce(Path, Class)}.
     * Infers the RunPredicate class to be the caller of this method.
     * <p><b>USE THIS METHOD WITH CAUTION: It inspects the call-stack!</b></p>
     *
     * @param reduceFile the file to reduce
     * @see #reduce(Path, Path, Class, String, boolean, boolean, boolean)
     */
    @SuppressWarnings("unchecked")
    public static void reduce(Path reduceFile) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        try {
            StackTraceElement caller = stack[2];
            Class<?> callerClass = Class.forName(caller.getClassName());
            Class<RunPredicate> predicate;
            try {
                predicate = (Class<RunPredicate>) callerClass;
            } catch (ClassCastException e) {
                throw new IllegalStateException(String.format("%s#%s(Path) should only be called from classes of type RunPredicate", stack[1].getClassName(), stack[1].getMethodName()));
            }
            reduce(reduceFile, predicate);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reduces a single JavaScript file.
     *
     * @see #reduce(Path, Path, Class, String, boolean, boolean, boolean)
     */
    public static Path reduce(Path reduceFile, Class<? extends RunPredicate> tester) {
        return reduce(reduceFile, tester, null, false, false);
    }

    /**
     * Reduces files in a directory with a main file.
     *
     * @see #reduce(Path, Path, Class, String, boolean, boolean, boolean)
     */
    public static Path reduce(Path root, Path rootRelativeMain, Class<? extends RunPredicate> tester) {
        return reduce(root, rootRelativeMain, tester, null, true, false, false);
    }

    /**
     * Reduces a single JavaScript file in a freshly created directory.
     *
     * @see #reduce(Path, Path, Class, String, boolean, boolean, boolean)
     */
    private static Path reduce(Path reduceFile, Class<? extends RunPredicate> tester, String testerArg, boolean quick, boolean batchMode) {
        try {
            Path fileName = reduceFile.getFileName();
            Path dir = Files.createTempDirectory(fileName.toString() + ".dir");
            Path reduceFileCopy = dir.resolve(fileName);
            Files.copy(reduceFile, reduceFileCopy);
            return reduce(dir, reduceFileCopy, tester, testerArg, false, quick, batchMode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Java method interface for invoking JSDelta.
     *
     * @param rootDir     as the root directory for the test to run, must contain mainFile (somewhere).
     * @param mainFile    as the file to invoke TAJS on
     * @param testerClass as the tester which decides if the analysis is successful or not
     * @param testerArg   as a string that should be passed to the testerClass constructor (useful for dynamically defined predicates)
     * @param dir         true if jsdelta should be invoked with the --dir flag
     * @param quick       true if jsdelta should be invoked with the --quick flag
     * @param batchMode   true if stdout should be silent and if a parallel execution environment can be expected (prevents std. execution of TAJS)
     */
    public static Path reduce(final Path rootDir, Path mainFile, Class<? extends RunPredicate> testerClass, String testerArg, boolean dir, boolean quick, boolean batchMode) {
        log.info("Initializing and sanity-checking minimization...");
        performSanityChecks(rootDir, mainFile, testerClass, testerArg, batchMode);

        try (DeltaMinimizerServer server = new DeltaMinimizerServer()) {
            Path predicateFile = writePredicateFile(testerArg, testerClass, server);
            log.info("Starting minimization...");
            Path minimized = JavaInterfaceForJSDelta.run(mainFile, predicateFile, dir ? rootDir : null, quick, batchMode);
            if (!batchMode) {
                if (minimized == null) {
                    log.info("Minimization failed...");
                } else if (dir) {
                    log.info(String.format("Minimization finished:\tlocation: %s%n", minimized.toAbsolutePath().toString()));
                } else {
                    List<String> lines = Files.readAllLines(minimized, UTF8);
                    String content = String.join("\n", lines);
                    log.info(String.format("Minimization finished:%n\tlocation: %s%n\tlines: %d%n\tcharacters: %d%n\tcontent:%n```%n%s%n```", minimized.toAbsolutePath().toString(), lines.size(), content.length(), content));
                }
            }
            return minimized;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void performSanityChecks(Path rootDir, Path mainFile, Class<? extends RunPredicate> testerClass, String testerArg, boolean batchMode) {
        // initial sanity checks
        if (testerClass.isAnonymousClass()) {
            throw new IllegalArgumentException("Anonymous classes are not supported");
        }
        if (testerClass.isMemberClass()) {
            throw new IllegalArgumentException("Inner classes are not supported");
        }

        final RunPredicate tester = JSDeltaCommandLineInterfaceToJava.instantiateTester(testerClass, testerArg);

        if (!batchMode && !tester.test(mainFile)) {
            throw new RuntimeException("Initial files do not satisfy the test!");
        }
        if (!mainFile.startsWith(rootDir)) {
            throw new IllegalArgumentException(String.format("Main file must reside in rootDir! (%s) vs. (%s)", mainFile, rootDir));
        }
    }

    private static Path writePredicateFile(String testerArg, Class<? extends RunPredicate> predicateClass, DeltaMinimizerServer server) throws IOException {
        StringBuilder predicate = new StringBuilder();
        predicate.append(predicatePreamble);
        TAJSEnvironmentConfig.init(TAJSEnvironmentConfig.findProperties());
        List<Integer> ports = TAJSEnvironmentConfig.get().getJSDeltaServerPorts();
        if (!ports.isEmpty()) {
            server.start(ports);
            predicate.append(makeServerpredicateString(testerArg, predicateClass, server.getPort()));
        } else {
            predicate.append(makeCommandLinePredicateString(testerArg, predicateClass));
        }

        Path predicateFile = Files.createTempFile("predicate", ".js");
        try (PrintWriter writer = new PrintWriter(predicateFile.toFile())) {
            writer.print(predicate.toString());
        }
        return predicateFile;
    }

    private static String makeCommandLinePredicateString(String testerArg, Class<? extends RunPredicate> predicateClass) {
        return String.format("exports.test = " +
                        "makeJavaProcessPredicate(%n" +
                        "['%s', '%s'], // TAJS-args %n" +
                        "['-Xmx4G', '-ea'], // Java-args %n" +
                        "'%s', // main-class %n" +
                        "'%s' // classpath %n" +
                        ");",
                predicateClass.getCanonicalName(), testerArg, // TAJS-args
                JSDeltaCommandLineInterfaceToJava.class.getCanonicalName(), // main-class
                System.getProperty("java.class.path") // classpath
        );
    }

    private static String makeServerpredicateString(String testerArg, Class<? extends RunPredicate> predicateClass, int port) {
        return String.format("exports.test = %n" +
                "makeServerProcessPredicate(%n" +
                "['%s', '%s'], %n" +
                "%d%n" +
                ");", predicateClass.getCanonicalName(), testerArg, port);
    }
}
