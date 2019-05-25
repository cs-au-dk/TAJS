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
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
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

    private static Map<Path, Path> realPathCache = new WeakHashMap<>();

    private static Map<Path, Boolean> tajsRootDirectoryCache = new WeakHashMap<>();

    public static void reset() {
        pathURLCache.clear();
        urlPathCache.clear();
        realPathCache.clear();
        tajsRootDirectoryCache.clear();
    }

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
     * @param normalize if set, normalize the path (resolve symlinks)
     * @throws AnalysisException if 'normalize' is set and the file does not exist (or some other error occurred)
     */
    public static Path toPath(URL url, boolean normalize) {
        return urlPathCache.computeIfAbsent(url, k -> {
            URI uri;
            Path path;
            try {
                uri = url.toURI();
            } catch (URISyntaxException e) {
                throw new AnalysisException(e);
            }
            if ("file".equals(url.getProtocol())) {
                path = Paths.get(uri);
            } else {
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
                path = Paths.get(uri);
            }
            if (normalize)
                path =  toRealPath(path);
            return path;
        });
    }

    /**
     * Normalizes a file URL using the underlying path normalization.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static URL normalizeFileURL(URL url) {
        if ("file".equals(url.getProtocol())) {
            return toURL(toPath(url, true).normalize());
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
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static Path getRelativeToWorkingDirectory(Path path) {
        return getRelativeTo(getWorkingDirectory(), path);
    }

    /**
     * Makes a relative Path that is relative to the 'from' directory.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static Path getRelativeTo(Path from, Path to) {
        return toRealPath(from).normalize().relativize(toRealPath(to)).normalize();
    }

    /**
     * Makes a relative Path that is relative to the TAJS directory, if possible.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static Optional<Path> getRelativeToTAJS(Path to) {
        Optional<Path> relativeToParentTAJS = getRelativeToParentTAJS(to);
        if (relativeToParentTAJS.isPresent()) {
            return relativeToParentTAJS;
        } else if (TAJSEnvironmentConfig.get().hasProperty("tajs")) {
            Path tajs = Paths.get(TAJSEnvironmentConfig.get().getCustom("tajs"));
            return Optional.of(getRelativeTo(tajs, to));
        }
        return Optional.empty();
    }

    /**
     * Makes a relative path from the root tajs directory to the given (descendant) file, if possible.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    private static Optional<Path> getRelativeToParentTAJS(Path to) {
        Path maybeTajs = toRealPath(to);
        while (maybeTajs != null) {
            if (isTAJSRootDirectory(maybeTajs)) {
                return Optional.of(getRelativeTo(maybeTajs, to));
            }
            maybeTajs = maybeTajs.getParent();
        }
        return Optional.empty();
    }

    /**
     * Decides if the given directory is the TAJS root directory
     */
    private static boolean isTAJSRootDirectory(Path dir) {
        return tajsRootDirectoryCache.computeIfAbsent(dir, k -> {
            String thisRelativeSourceFilePath = PathAndURLUtils.class.getCanonicalName().replace('.', File.separatorChar) + ".java";
            Path thisSourceFilePath = dir.resolve("src").resolve(thisRelativeSourceFilePath);
            return Files.exists(thisSourceFilePath);
        });
    }

    /**
     * Wrapper for {@link Path#toRealPath(LinkOption...)} that converts {@link IOException} to {@link AnalysisException}
     * and caches the result.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static Path toRealPath(Path p) {
        return realPathCache.computeIfAbsent(p, k -> {
            try {
                return p.toRealPath();
            } catch (NoSuchFileException e) {
                throw new AnalysisException("No such file " + e.getMessage());
            } catch (IOException e) {
                throw new AnalysisException(e);
            }
        });
    }

    /**
     * Returns the most specific common ancestor of the given nonempty set of paths.
     * @throws AnalysisException if one of the files do not exist (or some other error occurred)
     */
    public static Path getCommonAncestorDirectory(Set<Path> paths) {
        if (paths.isEmpty()) {
            throw new IllegalArgumentException("Empty set of paths");
        }
        // Implementation note: ought to build the parent path forwards instead of backwards for more efficiency)
        paths = paths.stream().map(PathAndURLUtils::toRealPath).collect(Collectors.toSet());
        Path shortestPath = paths.stream().min(Comparator.comparingInt(Path::getNameCount)).get();
        Path parent = shortestPath;
        while (parent != null) {
            Path finalParent = parent;
            if (paths.stream().allMatch(child -> child.startsWith(finalParent))) {
                if (!Files.isDirectory(parent)) // Must return path to directory
                    parent = parent.getParent();

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

    /**
     * Converts a string to a URL.
     * @throws AnalysisException if the file does not exist (or some other error occurred)
     */
    public static URL toURL(String str) {
        URL url;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            Path strAsPath = Paths.get(str);
            if (Files.exists(strAsPath)) {
                url = toURL(strAsPath);
            } else {
                throw new AnalysisException(e);
            }
        }
        return url;
    }

    /**
     * Return the file extension of the given path (including the dot).
     */
    public static String getFileExtension(Path file) {
        String fileStr = file.toString();
        int index = fileStr.lastIndexOf(".");
        if(index == -1)
            return "";

        return fileStr.substring(index);
    }
}
