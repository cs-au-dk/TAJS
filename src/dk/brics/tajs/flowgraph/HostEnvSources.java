/*
 * Copyright 2009-2016 Aarhus University
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
import dk.brics.tajs.util.Loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static dk.brics.tajs.util.Collections.newList;

/**
 * JavaScript sources for models of host environments.
 */
public class HostEnvSources {

    private final static String fileNamePrefix = "TAJS-host-environment-sources";

    private final static SourceLocation loaderDummySourceLocation = new SourceLocation(0, 0, formatFileName("loader"), null);

    /**
     * Loads all host environment JavaScript models according to currently selected options.
     */
    public static List<JavaScriptSource> get() {

        // note: not using java.nio.Path since Windows uses \ instead of /
        List<String> sourcePaths = newList();

        if (Options.get().isPolyfillMDNEnabled() || Options.get().isPolyfillTypedArraysEnabled()) {
            sourcePaths.add("mdn-polyfills.js");
        }
        if (Options.get().isPolyfillES6CollectionsEnabled()) {
            sourcePaths.add("es6-collections.js");
        }
        if (Options.get().isPolyfillTypedArraysEnabled()) {
            sourcePaths.add("typed-arrays-model.js");
        }

        if (Options.get().isDOMEnabled()) {
            // TODO: add extra paths...
        }

        String root = "/hostenv";
        List<JavaScriptSource> sources = newList();
        for (String sourcePath : sourcePaths) {
            try {
                String fullSourcePath = root + "/" + sourcePath;
                URL resource = HostEnvSources.class.getResource(fullSourcePath);
                InputStream sourceStream = resource.openStream();
                if (sourceStream == null) {
                    throw new AnalysisException("Can't find resource " + fullSourcePath);
                }
                String code = Loader.getString(sourceStream, Charset.forName("UTF-8"));
                sources.add(JavaScriptSource.makeFileCode(resource, formatFileName(sourcePath), code));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sources;
    }

    public static String formatFileName(String fileName) {
        return String.format("%s(%s)", fileNamePrefix, fileName);
    }

    public static boolean isHostEnvSource(SourceLocation sourceLocation) {
        return sourceLocation.getPrettyFileName().startsWith(fileNamePrefix);
    }

    public static SourceLocation getLoaderDummySourceLocation() {
        return loaderDummySourceLocation;
    }
}
