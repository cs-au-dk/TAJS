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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * String loader.
 */
public class Loader {

    /**
     * Returns the string contents of the given stream.
     * Empties the stream, but does not close it.
     */
    public static String getString(InputStream inputStream, Charset charset) throws IOException {
        try (InputStreamReader sr = new InputStreamReader(inputStream, charset); BufferedReader r = new BufferedReader(sr)) {
            StringBuilder b = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null)
                b.append(line).append("\n");
            return b.toString();
        }
    }

    /**
     * Returns the string contents of the given URL.
     */
    public static String getString(URL url, Charset charset) throws IOException {
        // attempt fast disk reading
        Path file;
        try {
            file = Paths.get(url.toURI());
        } catch (Exception e) {
            file = null;
        }
        if (file != null) {
            return getString(file, charset);
        }

        // default stream reading
        try (InputStream s = url.openStream()) {
            return getString(s, charset);
        }
    }

    /**
     * Returns the string contents of the given file.
     */
    public static String getString(Path file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file), charset);
    }
}
