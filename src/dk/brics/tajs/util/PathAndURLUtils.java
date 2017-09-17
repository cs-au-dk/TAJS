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

package dk.brics.tajs.util;

import dk.brics.tajs.options.TAJSEnvironmentConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

public class PathAndURLUtils {

    private static Map<Path, URL> pathURLCache = new WeakHashMap<>();

    private static Map<URL, Path> urlPathCache = new WeakHashMap<>();

    /**
     * Converts a Path to a URL.
     */
    public static URL toURL(Path p) {
        return pathURLCache.computeIfAbsent(p, k -> {
            try {
                // normalize the path before making it a URL, the URL normalization might not work!
                Path normalized = p.normalize();
                return normalized.toUri().toURL();
            } catch (MalformedURLException e) {
                throw new AnalysisException(e);
            }
        });
    }

    /**
     * Converts a URL to a Path.
     * This might "mount" a new file system in the JVM.
     * Particularly useful when loading JavaScript models within jar files.
     */
    public static Path toPath(URL url) {
        return urlPathCache.computeIfAbsent(url, k -> {
            URI uri;
            try {
                uri = url.toURI();
            } catch (URISyntaxException e) {
                throw new AnalysisException(e);
            }
            if ("file".equals(url.getProtocol())) {
                return Paths.get(uri);
            }
            try {
                FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException e) {
                try {
                    // prevent runtime error when reading from jar file
                    FileSystems.newFileSystem(uri, new HashMap<>(), null);
                } catch (IOException e1) {
                    throw new AnalysisException(e1);
                }
            }
            return Paths.get(uri);
        });
    }

    /**
     * Normalizes a file URL using the underlying path normalization.
     */
    public static URL normalizeFileURL(URL url) {
        if ("file".equals(url.getProtocol())) {
            return toURL(toPath(url).toAbsolutePath().normalize());
        }
        return url;
    }

    /**
     * Checks if a URL points to something readable.
     */
    public static boolean isConsumable(URL url) {
        try (InputStream ignored = url.openStream()) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Makes a relative Path that is relative to the working directory.
     */
    public static Path getRelativeToWorkingDirectory(Path path) {
        return getRelativeTo(getWorkingDirectory(), path);
    }

    /**
     * Makes a relative Path that is relative to the 'from' directory.
     */
    public static Path getRelativeTo(Path from, Path to) {
        return from.toAbsolutePath().normalize().relativize(to.toAbsolutePath()).normalize();
    }

    /**
     * Makes a relative Path that is relative to the TAJS directory, if possible.
     */
    public static Optional<Path> getRelativeToTAJS(Path to) {
        Optional<Path> relativeToParentTAJS = getRelativeToParentTAJS(to);
        if (relativeToParentTAJS.isPresent()) {
            return relativeToParentTAJS;
        } else if (TAJSEnvironmentConfig.get().hasProperty("tajs")) {
            try {
                Path tajs = Paths.get(TAJSEnvironmentConfig.get().getCustom("tajs"));
                return Optional.of(getRelativeTo(tajs.toAbsolutePath(), to.toRealPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    /**
     * Makes a relative path from the root tajs directory to the given (descendant) file, if possible.
     */
    private static Optional<Path> getRelativeToParentTAJS(Path to) {
        try {
            Path maybeTajs = to.toAbsolutePath().toRealPath();
            while (maybeTajs != null) {
                if (isTAJSRootDirectory(maybeTajs)) {
                    return Optional.of(getRelativeTo(maybeTajs.toAbsolutePath(), to.toRealPath()));
                }
                maybeTajs = maybeTajs.getParent();
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decides if the given directory is the TAJS root directory
     */
    private static boolean isTAJSRootDirectory(Path dir) {
        String thisRelativeSourceFilePath = PathAndURLUtils.class.getCanonicalName().replace('.', File.separatorChar) + ".java";
        Path thisSourceFilePath = dir.resolve("src").resolve(thisRelativeSourceFilePath);
        return Files.exists(thisSourceFilePath);
    }

    /**
     * Returns the most specific common ancestor of the given nonempty set of paths.
     */
    public static Path getCommonAncestorDirectory(Set<Path> paths) {
        if (paths.isEmpty()) {
            throw new IllegalArgumentException("Empty set of paths");
        }
        // Implementation note: ought to build the parent path forwards instead of backwards for more efficiency)
        paths = paths.stream().map(Path::toAbsolutePath).collect(Collectors.toSet());
        Path shortestPath = paths.stream().sorted(Comparator.comparingInt(Path::getNameCount)).findFirst().get();
        Path parent = shortestPath;
        while (parent != null) {
            Path finalParent = parent;
            if (paths.stream().allMatch(child -> child.startsWith(finalParent))) {
                return parent;
            }
            parent = parent.getParent();
        }
        throw new IllegalArgumentException("No common ancestor of paths: " + paths);
    }

    /**
     * Returns the current working directory.
     */
    public static Path getWorkingDirectory() {
        return Paths.get("");
    }

    /**
     * OS-independent toString method for paths.
     */
    public static String toPortableString(Path path) {
        return path.toString().replace(File.separatorChar, '/');
    }
}
