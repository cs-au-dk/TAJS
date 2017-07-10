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

package dk.brics.tajs.monitoring.soundness.logfileutilities;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.PathAndURLUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dk.brics.tajs.util.PathAndURLUtils.normalizeFileURL;

/**
 * Domain-mapper between the source locations of TAJS and the log files.
 */
public class SourceLocationUtil {

    /**
     * Maps from a value logger source location to a TAJS source location.
     */
    public static SourceLocation makeTAJSSourceLocation(dk.au.cs.casa.jer.entries.SourceLocation sourceLocation) {
        URL location = normalizeFileURL(PathAndURLUtils.toURL(getMainDir().resolve(sourceLocation.getFileName())));
        return new SourceLocation.StaticLocationMaker(location).make(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber(), 0, 0);
    }

    private static Path getMainDir() {
        return Paths.get(Options.get().getArguments().get(Options.get().getArguments().size() - 1)).getParent();
    }

    /**
     * Maps from a TAJS source location to a value logger source location.
     */
    public static dk.au.cs.casa.jer.entries.SourceLocation makeLoggerSourceLocation(SourceLocation sourceLocation) {
        Path relative = PathAndURLUtils.getRelativeTo(getMainDir(), PathAndURLUtils.toPath(sourceLocation.getLocation()));
        return new dk.au.cs.casa.jer.entries.SourceLocation(sourceLocation.getLineNumber(), sourceLocation.getColumnNumber(), relative.toString());
    }
}
