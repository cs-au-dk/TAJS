/*
 * Copyright 2009-2015 Aarhus University
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * String loader.
 */
public class Loader {

    private Loader() {
    }

    /**
     * Returns the contents of the given resource (file path or URL) as a string.
     *
     * @param location file path or URL
     * @param charset character encoding (if null, use system default)
     * @return contents of the file or URL
     * @throws IOException
     */
    public static String getString(String location, String charset) throws IOException {
        try {
            URL url = new URL(location);
            try (InputStream is = url.openStream()) {
                return getStringFromStream(is, charset);
            } catch (IOException e) {
                throw new AnalysisException("Could not load string from location: " + location);
            }
        } catch (MalformedURLException e) {
            // location was not a valid URL, check if it is a local file
            Path path = Paths.get(location);
            if (Files.exists(path)) {
                return getStringFromFile(path, charset);
            } else {
                throw new AnalysisException("Cannot get string from location: " + location);
            }
        }
    }

    /**
     * Returns the string contents of the given stream.
     */
    public static String getStringFromStream(InputStream inputStream, String charset) throws IOException {
        if (charset == null)
            charset = Charset.defaultCharset().name();
        try (InputStreamReader sr = new InputStreamReader(inputStream, charset);
             BufferedReader r = new BufferedReader(sr)) {
            StringBuilder b = new StringBuilder();
            int c;
            while ((c = r.read()) != -1)
                b.append((char) c);
            return b.toString();
        }
    }

    /**
     * Returns the string contents of the given file.
     */
    public static String getStringFromFile(Path file, String charset) throws IOException {
        try (FileChannel fc = new FileInputStream(file.toFile()).getChannel()) {
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.forName(charset).decode(bb).toString();
        }
    }
}
