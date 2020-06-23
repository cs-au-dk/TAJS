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

package dk.brics.tajs.jsdelta.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.flowgraph.FlowGraph;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.js2flowgraph.FlowGraphBuilder;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.PathAndURLUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.PathAndURLUtils.normalizeFileURL;

/**
 * Utilities for (de-)serializing all the source locations of a JavaScript file to JSON.
 */
public class SourceLocationMinimizationUtilities {

    private static final Charset charset = StandardCharsets.UTF_8;

    private static Path serializeLocationsToJSON(Set<SourceLocation> locations) {
        String json = new Gson().toJson(locations);
        Path file;
        try {
            file = Files.createTempFile("locations", ".json");
            Files.write(file, Collections.singletonList(json), charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private static Set<SourceLocation> getSourceLocationsInJavaScriptFile(Path target) {
        try {
            URL url = normalizeFileURL(PathAndURLUtils.toURL(target));
            SourceLocation.StaticLocationMaker sourceLocationMaker = new SourceLocation.StaticLocationMaker(url);
            FlowGraphBuilder fgb = FlowGraphBuilder.makeForMain(sourceLocationMaker);
            String code = Loader.getString(url, charset);
            fgb.transformStandAloneCode(code, sourceLocationMaker);
            FlowGraph fg = fgb.close();

            return fg.getFunctions().stream()
                    .flatMap(f -> f.getBlocks().stream()
                            .flatMap(b -> b.getNodes().stream()
                                    .map(AbstractNode::getSourceLocation)))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<SourceLocation> deserializeLocationsFromJSON(Path jsonLikeFile) {
        Type type = new TypeToken<Set<SourceLocation>>() {
        }.getType();
        System.out.println(jsonLikeFile);
        String json;
        try {
            json = String.join(String.format("%n"), Files.readAllLines(jsonLikeFile, charset)).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // pre-parsing hacking: js -> json (needed because of JSDelta quirk)
        if (!json.isEmpty()) {
            //System.out.println(json);
            if ('(' == json.charAt(0)) {
                json = json.substring(1);
            }
            if (json.endsWith(";")) {
                json = json.substring(0, json.length() - 1);
            }
            if (json.endsWith(")")) {
                json = json.substring(0, json.length() - 1);
            }
        }
        return new Gson().fromJson(json, type);
    }

    public static Path makeInitialLocationsFile(Path target) {
        return serializeLocationsToJSON(getSourceLocationsInJavaScriptFile(target));
    }
}
