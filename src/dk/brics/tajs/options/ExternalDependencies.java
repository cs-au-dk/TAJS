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

package dk.brics.tajs.options;

import dk.brics.tajs.util.AnalysisException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Resolves the external dependencies of TAJS that can be installed using "extras/install.sh".
 * This resolution can be overridden with entries in tajs.properties.
 */
public class ExternalDependencies {

    public static Optional<Path> getJalangiLoggerDirectory() {
        return getExternalDependency(
                "jalangilogger",
                Paths.get("javascript"));
    }

    public static Optional<Path> getTSSpecReaderDirectory() {
        return getExternalDependency(
                "ts-spec-reader",
                Paths.get("."));
    }

    public static Path getJSDelta() {
        Optional<Path> jsdelta = getExternalDependency(
                "jsdelta",
                Paths.get("node_modules/jsdelta/delta.js"));
        if (!jsdelta.isPresent()) {
            throw new AnalysisException(String.format("Couldn't find jsdelta."));
        }
        return jsdelta.get();
    }

    /**
     * Finds the external-dependency of the specified dependency.
     */
    private static Optional<Path> getExternalDependency(String dependencyName, Path pathInDependencyContainer) {
        boolean has = TAJSEnvironmentConfig.get().hasProperty(dependencyName);
        if (has) {
            return Optional.of(Paths.get(TAJSEnvironmentConfig.get().getCustom(dependencyName)).resolve(pathInDependencyContainer));
        }
        boolean hasTAJS = TAJSEnvironmentConfig.get().hasProperty("tajs");
        if (hasTAJS) {
            Path dependencyContainer = Paths.get(TAJSEnvironmentConfig.get()
                    .getCustom("tajs"))
                    .resolve("extras")
                    .resolve(dependencyName);
            Path actualDependency = dependencyContainer.resolve(pathInDependencyContainer);
            if (Files.exists(actualDependency)) {
                return Optional.of(actualDependency);
            }
        }
        return Optional.empty();
    }
}
