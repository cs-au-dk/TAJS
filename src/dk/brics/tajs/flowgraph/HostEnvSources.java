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

package dk.brics.tajs.flowgraph;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.List;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newList;

/**
 * JavaScript sources for models of host environments.
 */
public class HostEnvSources {

    private static final String PROTOCOL_NAME = "tajs-host-env";

    static {
        registerProtocol();
    }

    private static void registerProtocol() {
        // custom protocol to avoid system-specific paths in the output
        URL.setURLStreamHandlerFactory(protocol -> PROTOCOL_NAME.equals(protocol) ? new URLStreamHandler() {
            protected URLConnection openConnection(URL url) {
                return new URLConnection(url) {
                    public void connect() throws IOException {
                        resolve(url.getPath()).openConnection().connect();
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return resolve(url.getPath()).openConnection().getInputStream();
                    }

                    private URL resolve(String path) {
                        String root = "/hostenv";
                        String fullSourcePath = root + "/" + path;
                        URL resource = HostEnvSources.class.getResource(fullSourcePath);
                        if (resource == null) {
                            throw new AnalysisException("Can't find resource " + fullSourcePath);
                        }
                        return resource;
                    }
                };
            }
        } : null);
    }

    /**
     * Loads all host environment JavaScript models according to currently selected options.
     */
    public static List<URL> getAccordingToOptions() {

        // note: not using java.nio.Path since Windows uses \ instead of /
        List<String> sourcePaths = newList();

        sourcePaths.add("string-replace-model.js");

        // be careful about changing orders here! the later files might depend on the earlier ones
        if (Options.get().isPolyfillMDNEnabled() || Options.get().isPolyfillTypedArraysEnabled()) {
            sourcePaths.add("mdn-polyfills.js");
        }
        if (Options.get().isPolyfillES6CollectionsEnabled()) {
            sourcePaths.add("es6-collections.js");
        }
        if (Options.get().isPolyfillTypedArraysEnabled()) {
            sourcePaths.add("typed-arrays-model.js");
        }
        if (Options.get().isCommonAsyncPolyfillEnabled()) {
            sourcePaths.add("common-async-polyfill.js");
        }
        if (Options.get().isConsoleModelEnabled()) {
            sourcePaths.add("console-model.js");
        }
        return sourcePaths.stream().map(s -> {
            try {
                return new URL(PROTOCOL_NAME, null, s);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
