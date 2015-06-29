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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * String loader.
 */
public class Loader {

    private Loader() {
    }

    /**
     * Returns the contents of the given resource (file path or URL) as a string.
     *
     * @param u       file path or URL
     * @param charset character encoding (if null, use system default)
     * @return contents
     * @throws IOException
     */
    public static String getString(String u, String charset) throws IOException {
        if (charset == null)
            charset = Charset.defaultCharset().name();
        try (InputStream in = new URL(u).openStream();
             InputStreamReader sr = new InputStreamReader(in, charset);
             BufferedReader r = new BufferedReader(sr)) {
            StringBuilder b = new StringBuilder();
            int c;
            while ((c = r.read()) != -1)
                b.append((char) c);
            return b.toString();
        } catch (MalformedURLException e) {
            try (FileChannel fc = new FileInputStream(u).getChannel()) {
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                return Charset.forName(charset).decode(bb).toString();
            }
        }
    }

    /**
     * Resolves relative paths/URLs.
     *
     * @param base base path/URL
     * @param u    path/URL to resolve (relative or absolute)
     * @return absolute URL
     * @throws MalformedURLException
     */
    public static String resolveRelative(String base, String u) throws MalformedURLException {
        String abs;
        try {
            abs = new URL(new URL(base), u).toString();
        } catch (MalformedURLException e) {
            abs = new URL(new URL(new File(base).toURI().toString()), u).toString();
        }
        if (abs.startsWith("file:"))
            abs = abs.substring(5);
        return abs;
    }
}
