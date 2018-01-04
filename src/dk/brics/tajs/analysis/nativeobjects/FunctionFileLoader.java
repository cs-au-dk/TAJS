/*
 * Copyright 2009-2018 Aarhus University
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

package dk.brics.tajs.analysis.nativeobjects;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.js.UserFunctionCalls;
import dk.brics.tajs.flowgraph.Function;
import dk.brics.tajs.flowgraph.HostEnvSources;
import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.js2flowgraph.FlowGraphMutator;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.ScopeChain;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.PathAndURLUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static dk.brics.tajs.util.PathAndURLUtils.normalizeFileURL;

/**
 * Utility for loading the content of a file as a function body.
 */
public class FunctionFileLoader {

    /**
     * Loads a JavaScript file as a function with the chosen parameter, and optionally marks the function as host-environment function.
     *
     * @param target file containing the source code of the function.
     * @param isHostEnvironment true iff the function should be marked as a host environment function.
     * @param parameterNames the parameter names of the function.
     * @return value representing the loaded and instantiated function.
     */
    public static Value loadFunction(String target, boolean isHostEnvironment, List<String> parameterNames, Solver.SolverInterface c) {
        URL url;
        try {
            url = new URL(target);
        } catch (MalformedURLException e1) {
            Path likelyPath = Paths.get(target);
            if (!likelyPath.isAbsolute()) {
                // resolve relative to the file the loading occurs from
                URL location = c.getNode().getSourceLocation().getLocation();
                Path callDirectory;
                if (location == null) {
                    throw new AnalysisException(c.getNode() + ": Cannot load relative path from unknown physical location: " + c.getNode().getSourceLocation());
                } else {
                    if (HostEnvSources.PROTOCOL_NAME.equals(location.getProtocol())) {
                        location = HostEnvSources.resolve(location.getPath());
                    }
                    callDirectory = PathAndURLUtils.toPath(location).getParent();
                }
                likelyPath = callDirectory.resolve(target);
            }
            if (!Files.exists(likelyPath)) {
                throw new AnalysisException("File to be loaded does not exist (could be an error in the analyzed program)", new NoSuchFileException(likelyPath.toAbsolutePath().toString()));
            }
            // now we have a valid URL
            url = PathAndURLUtils.toURL(likelyPath);
        }
        url = normalizeFileURL(url);
        final SourceLocation.SourceLocationMaker sourceLocationMaker;
        if (isHostEnvironment) {
            String customName = String.format("HOST(%s)", url.getPath());
            sourceLocationMaker = new SourceLocation.CustomStaticLocationMaker(customName, url);
        } else {
            sourceLocationMaker = new SourceLocation.StaticLocationMaker(url);
        }
        Function function = FlowGraphMutator.extendFlowGraphWithTopLevelFunction(parameterNames, url, isHostEnvironment, c.getFlowGraph(), sourceLocationMaker);
        ObjectLabel functionLabel = UserFunctionCalls.instantiateFunction(function, ScopeChain.make(InitialStateBuilder.GLOBAL), c.getNode(), c.getState(), c);
        return Value.makeObject(functionLabel);
    }
}
