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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PathAndURLUtils {

    /**
     * Converts a Path to a URL.
     */
    public static URL toURL(Path p) {
        try {
            return p.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new AnalysisException(e);
        }
    }

    /**
     * Converts a URL to a Path.
     * This might "mount" a new file system in the JVM.
     * Particularly useful when loading JavaScript models within jar files.
     */
    public static Path toPath(URL url) {
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
     * Makes a relative Path that is relative to the working directory.
     */
    public static Path getRelativeToWorkingDirectory(Path path) {
        return Paths.get("").toAbsolutePath().normalize().relativize(path.toAbsolutePath()).normalize();
    }
}
