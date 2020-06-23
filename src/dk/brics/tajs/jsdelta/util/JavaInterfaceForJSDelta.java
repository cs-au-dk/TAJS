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

package dk.brics.tajs.jsdelta.util;

import dk.brics.tajs.options.ExternalDependencies;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Java interface for invoking JSDelta in an external process.
 */
public class JavaInterfaceForJSDelta {

    private static final Logger log = Logger.getLogger(JavaInterfaceForJSDelta.class);

    public static Path run(Path target, Path predicate, Path dir, boolean quick, boolean batchMode) {
        assert (Files.exists(target));
        assert (Files.exists(predicate));
        List<String> cmd = newList();
        cmd.add(TAJSEnvironmentConfig.get().getNode().toString());
        try {
            List<String> testNodeCmd = newList();
            testNodeCmd.add(TAJSEnvironmentConfig.get().getNode().toString());
            testNodeCmd.add("--eval");
            testNodeCmd.add("42");
            Process testNodeProcess = new ProcessBuilder(testNodeCmd).start();
            testNodeProcess.waitFor();
            if (testNodeProcess.exitValue() != 0) {
                throw new RuntimeException("Failed to test the node process: " + testNodeProcess.exitValue());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to test the node process", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cmd.add(ExternalDependencies.getJSDelta().toString());
        cmd.add("--optimize");
        if (quick) {
            cmd.add("--quick");
        }
        if (dir != null) {
            cmd.add("--dir");
            cmd.add(dir.toAbsolutePath().toString());
            cmd.add(dir.relativize(target).toString());
        } else {
            cmd.add(target.toAbsolutePath().toString());
        }
        cmd.add(predicate.toAbsolutePath().toString());
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            //pb.directory(jsdelta_dir.toFile());
            //pb.redirectOutput(Redirect.INHERIT);
            if (!batchMode) {
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            }
            Process p = pb.start();
            log.info(String.format("Started JSDelta process for target: %s (at time: %s, in thread: %s)", target.toAbsolutePath().toString(), LocalDateTime.now().toString(), Thread.currentThread().getName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            Pattern pattern = Pattern.compile("^Minimisation finished; final version is at (.*?) \\(\\d+ bytes\\)$"); // Note: Should be Minimisation with s, since that is the output of jsdelta
            Path finalFile = null;
            boolean echoJSDeltaStdOut = true;
            while ((line = br.readLine()) != null) {
                if (!batchMode && line.equals("") && echoJSDeltaStdOut) {
                    log.info("(disabling jsdelta-stdout echoing temporarily)");
                    // disable echoing - jsdelta is printing minimized source... (should use flag to disable in jsdelta)
                    echoJSDeltaStdOut = false;
                }
                Matcher matcher = pattern.matcher(line);
                if (echoJSDeltaStdOut || matcher.matches()) {
                    if (!batchMode) {
                        log.info(String.format("jsdelta (%s):::\t %s", Thread.currentThread().getName(), line));
                    }
                }
                if (matcher.matches()) {
                    finalFile = Paths.get(matcher.group(1));
                    break;
                }
            }
            p.waitFor();
            log.info(String.format("Ended JSDelta process for target: %s, with result: %s (at time: %s, in thread: %s)", target.toAbsolutePath().toString(), finalFile == null ? "NONE" : finalFile.toAbsolutePath().toString(), LocalDateTime.now().toString(), Thread.currentThread().getName()));

            return finalFile;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
