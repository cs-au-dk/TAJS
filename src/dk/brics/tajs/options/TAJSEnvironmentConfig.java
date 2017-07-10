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

package dk.brics.tajs.options;

import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Collectors;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static dk.brics.tajs.util.Collections.newList;

/**
 * Manages system-local environment properties.
 * <p>
 * Searches for a .tajsconfig file in the working directory or one of its ancestors until the file is found.
 */
public class TAJSEnvironmentConfig {

    private static final String filename = ".tajsconfig";

    private static TAJSEnvironmentConfig instance;

    private final Properties properties;

    public TAJSEnvironmentConfig(Properties properties) {
        this.properties = properties;
    }

    public static Properties findProperties() {
        Properties prop = new Properties();
        setDefaults(prop);
        findOrderedConfigFiles().forEach(config -> {
                    try {
                        prop.load(new FileInputStream(config.toFile()));
                        prop.forEach((k, v) -> {
                            try {
                                Path vpath = Paths.get((String) v);
                                if (!vpath.isAbsolute()) {
                                    String newPath = config.getParent().resolve(vpath).toAbsolutePath().toString();
                                    if (Files.exists(Paths.get(newPath))) {
                                        prop.setProperty((String) k, newPath);
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        });
                    } catch (IOException e) {
                        throw new AnalysisException(e);
                    }
                }
        );
        return prop;
    }

    private static Path resolveAccordingToPath(String cmd) {
        String PATH = System.getenv("PATH");
        String[] paths = PATH.split(":");
        for (String path : paths) {
            Path assumed = Paths.get(path).resolve(cmd);
            if (Files.exists(assumed)) {
                return assumed;
            }
        }
        return null;
    }

    private static void setDefaults(Properties p) {
        Path nodePath = resolveAccordingToPath("node");
        if (nodePath != null) {
            p.setProperty("node", nodePath.toAbsolutePath().toString());
        }
        Path jjsPath = resolveAccordingToPath("jjs");
        if (jjsPath != null) {
            p.setProperty("jjs", jjsPath.toAbsolutePath().toString());
        }
    }

    public static void init() {
        if (Options.get().getConfig() == null) {
            TAJSEnvironmentConfig.init(TAJSEnvironmentConfig.findProperties());
        } else {
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(Options.get().getConfig())) {
                properties.load(fis);
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
            TAJSEnvironmentConfig.init(properties);
        }
    }

    public static void init(Properties properties) {
        instance = new TAJSEnvironmentConfig(properties);
    }

    public static TAJSEnvironmentConfig get() {
        return instance;
    }

    /**
     * Finds all .tajsconfig files on the directory path from the working directory to the root of the file system.
     */
    private static List<Path> findOrderedConfigFiles() {
        Path parent = Paths.get("").toAbsolutePath();
        List<Path> configFiles = newList();
        while (parent != null && Files.exists(parent)) {
            Path file = parent.resolve(filename);
            if (Files.exists(file)) {
                configFiles.add(file);
            }
            parent = parent.getParent();
        }
        Collections.reverse(configFiles);
        return configFiles;
    }

    public Path getNode() {
        return Paths.get(getRequiredProperty("node"));
    }

    public Path getJSDelta() {
        return Paths.get(getRequiredProperty("jsdelta"));
    }

    private String getRequiredProperty(String name) {
        String property = properties.getProperty(name);
        if (property == null) {
            throw new AnalysisException(String.format("Property '%s' in %s is needed, but not defined!", name, filename));
        }
        return property;
    }

    public boolean isDesktopEnabled() {
        String property = properties.getProperty("desktop");
        if (property == null) {
            return true; // default enabled
        }
        Boolean desktop = Boolean.valueOf(property);
        if (desktop && !java.awt.Desktop.isDesktopSupported()) {
            throw new AnalysisException("Invalid TAJS configuration: desktop-usage is explicitly enabled, but the platform does not support desktops!");
        }
        return desktop;
    }

    public Path getGnuPlot() {
        return Paths.get(getRequiredProperty("gnuplot"));
    }

    public Path getLatex() {
        return Paths.get(getRequiredProperty("latex"));
    }

    public List<Integer> getJSDeltaServerPorts() {
        String property = properties.getProperty("jsdeltaserverports");
        if (property == null) {
            return newList(); // default none
        }
        return Arrays.stream(property.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public String getCustom(String name) {
        return getRequiredProperty(name);
    }

    public boolean hasProperty(String name) {
        return properties.getProperty(name) != null;
    }
}
