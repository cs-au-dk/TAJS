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
import dk.brics.tajs.util.Canonicalizer;
import dk.brics.tajs.util.DeepImmutable;
import dk.brics.tajs.util.PathAndURLUtils;

import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;

/**
 * Source location.
 */
public class SourceLocation implements Comparable<SourceLocation>, DeepImmutable, Serializable {

    /**
     * A custom name for this source location (only used for pretty printing and sorting).
     */
    private final String customName;

    /**
     * The location where the source code resides.
     */
    private final URL location;

    /**
     * The line number inside the source code (starts at 1).
     */
    private final int lineNumber;

    /**
     * The column number inside the source code (starts at 1).
     */
    private final int columnNumber;

    /**
     * The location where the source code is created and loaded dynamically (only used for pretty printing and sorting).
     */
    private final SourceLocation loaderLocation;

    /**
     * The kind of the source location (only used for pretty printing and sorting).
     */
    private final Kind kind;

    private final int endLineNumber;

    private final int endColumnNumber;

    private final int hashCode;

    /**
     * Constructs a new source location.
     * 0 means "no number".
     */
    private SourceLocation(String customName, URL location, int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber, SourceLocation loaderLocation, Kind kind) {
        checkNormalizedFileURL(location);
        this.customName = customName;
        this.location = location;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        // TODO make equal/hashcode work for the end-positions as well (GitHub #360)
        this.endLineNumber = endLineNumber;
        this.endColumnNumber = endColumnNumber;
        this.loaderLocation = loaderLocation;
        this.kind = kind;
        this.hashCode = computeHashCode();
    }

    private static void checkNormalizedFileURL(URL location) {
        if (location != null && Options.get().isDebugOrTestEnabled()) {
            URL normalized = PathAndURLUtils.normalizeFileURL(location);
            if (!location.equals(normalized)) {
                throw new IllegalArgumentException(String.format("File URLs should be normalized before being used for source locations: %s normalizes to %s!", location, normalized));
            }
        }
    }

    private static String format(String selectedFileName, int linenumber, int columnnumber, boolean showPosition) {
        if (showPosition) {
            if (linenumber > 0 && columnnumber > 0) {
                return String.format("%s:%d:%d", selectedFileName, linenumber, columnnumber);
            }
            if (linenumber > 0) {
                return String.format("%s:%d", selectedFileName, linenumber);
            }
        }
        return String.format("%s", selectedFileName);
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public int getEndColumnNumber() {
        return endColumnNumber;
    }

    public URL getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceLocation)) return false;

        final SourceLocation that = (SourceLocation) o;

        if (lineNumber != that.lineNumber) return false;
        if (columnNumber != that.columnNumber) return false;
        if (customName != null ? !customName.equals(that.customName) : that.customName != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (loaderLocation != null ? !loaderLocation.equals(that.loaderLocation) : that.loaderLocation != null)
            return false;
        return kind == that.kind;
    }

    private int computeHashCode() {
        int result = customName != null ? customName.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + lineNumber;
        result = 31 * result + columnNumber;
        result = 31 * result + (loaderLocation != null ? loaderLocation.hashCode() : 0);
        result = 31 * result + kind.hashCode();
        return result;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Returns the source line number.
     * 0 means "no number".
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the source column number.
     * 0 means "no number".
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * Returns a string description of the source location.
     */
    @Override
    public String toString() {
        return toUserFriendlyString(true);
    }

    private String toFullString() {
        String selectedFileName;
        switch (kind) {
            case SYNTHETIC:
                selectedFileName = customName;
                break;
            case DYNAMIC:
                selectedFileName = wrapDynamic(loaderLocation.toString());
                break;
            case STATIC:
                selectedFileName = location.toString();
                break;
            default:
                throw new AnalysisException("Unhandled switch case: " + kind);
        }
        return format(selectedFileName, lineNumber, columnNumber, true);
    }

    /**
     * Compares source locations first by line number, then by column number.
     */
    @Override
    public int compareTo(SourceLocation e) {

        int c = kind.compareTo(e.kind);
        if (c != 0) {
            return c;
        }

        c = customName == e.customName ? 0 : (customName == null ? -1 : (e.customName == null ? 1 : customName.compareTo(e.customName)));
        if (c != 0) {
            return c;
        }

        c = location == e.location ? 0 : (location == null ? -1 : (e.location == null ? 1 : location.getPath().compareTo(e.location.getPath())));
        if (c != 0) {
            return c;
        }

        c = loaderLocation == e.loaderLocation ? 0 : (loaderLocation == null ? -1 : (e.loaderLocation == null ? 1 : loaderLocation.compareTo(e.loaderLocation)));
        if (c != 0) {
            return c;
        }

        c = lineNumber - e.lineNumber;
        if (c != 0) {
            return c;
        }

        c = columnNumber - e.columnNumber;
        if (c != 0) {
            return c;
        }

        return 0;
    }

    /**
     * User friendly representation of this source location.
     *
     * @param showPosition true if the position inside the source file should be included in the output.
     */
    public String toUserFriendlyString(boolean showPosition) {
        final String selectedFileName;
        switch (kind) {
            case SYNTHETIC:
                selectedFileName = customName;
                break;
            case DYNAMIC:
                selectedFileName = wrapDynamic(loaderLocation.toUserFriendlyString(showPosition));
                break;
            case STATIC:
                if (location.getProtocol().equalsIgnoreCase("http") || location.getProtocol().equalsIgnoreCase("https")) {
                    selectedFileName = location.toString();
                } else if (customName != null) {
                    selectedFileName = customName;
                } else {
                    Path actual = PathAndURLUtils.toPath(location);
                    Path relativeToWorkingDirectory = PathAndURLUtils.getRelativeToWorkingDirectory(actual);
                    selectedFileName = PathAndURLUtils.toPortableString(relativeToWorkingDirectory);
                }
                break;
            default:
                throw new AnalysisException("Unhandled switch case: " + kind);
        }

        return format(selectedFileName, lineNumber, columnNumber, showPosition);
    }

    private String wrapDynamic(String loaderLocationString) {
        return String.format("TAJS-dynamic-code(%s)", loaderLocationString);
    }

    private enum Kind {

        /**
         * Synthetic locations, mainly used for testing.
         */
        SYNTHETIC,

        /**
         * Ordinary locations that exist in source files.
         */
        STATIC,

        /**
         * Locations created dynamically, through `eval` and related functions.
         */
        DYNAMIC,
    }

    /**
     * Abstract factory for creating SourceLocations
     */
    public abstract static class SourceLocationMaker {

        public abstract SourceLocation make(int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber);

        public SourceLocation makeUnspecifiedPosition() {
            return make(0, 0, 0, 0);
        }

        protected SourceLocation makeCanonical(String customName, URL location, int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber, SourceLocation loaderLocation, Kind kind) {
            SourceLocation instance = new SourceLocation(customName, location, lineNumber, columnNumber, endLineNumber, endColumnNumber, loaderLocation, kind);
            return Canonicalizer.get().canonicalize(instance);
        }
    }

    /**
     * Creates SourceLocations that are created dynamically, through `eval` and related functions.
     */
    public static class DynamicLocationMaker extends SourceLocationMaker {

        private final SourceLocation loaderLocation;

        /**
         * @param loaderLocation as the location where the source code is created and loaded dynamically.
         */
        public DynamicLocationMaker(SourceLocation loaderLocation) {
            this.loaderLocation = loaderLocation;
        }

        @Override
        public SourceLocation make(int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber) {
            return makeCanonical(null, null, lineNumber, columnNumber, endLineNumber, endColumnNumber, loaderLocation, Kind.DYNAMIC);
        }
    }

    /**
     * Creates SourceLocations that exist in static source files.
     */
    public static class StaticLocationMaker extends SourceLocationMaker {

        private final URL location;

        /**
         * @param location as the location of the static source file.
         */
        public StaticLocationMaker(URL location) {
            checkNormalizedFileURL(location);
            this.location = location;
        }

        @Override
        public SourceLocation make(int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber) {
            return makeCanonical(null, location, lineNumber, columnNumber, endLineNumber, endColumnNumber, null, Kind.STATIC);
        }
    }

    /**
     * As {@link StaticLocationMaker}, but with a custom name.
     */
    public static class CustomStaticLocationMaker extends SourceLocationMaker {

        private final String customName;

        private final URL location;

        /**
         * @param location as the location of the static source file.
         */
        public CustomStaticLocationMaker(String customName, URL location) {
            this.customName = customName;
            this.location = location;
        }

        @Override
        public SourceLocation make(int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber) {
            return makeCanonical(customName, location, lineNumber, columnNumber, endLineNumber, endColumnNumber, null, Kind.STATIC);
        }
    }

    /**
     * Creates SourceLocations for synthetic locations. Mainly used for testing.
     */
    public static class SyntheticLocationMaker extends SourceLocationMaker {

        private final String name;

        /**
         * @param name a descriptive name for the synthetic locations
         */
        public SyntheticLocationMaker(String name) {
            this.name = name;
        }

        @Override
        public SourceLocation make(int lineNumber, int columnNumber, int endLineNumber, int endColumnNumber) {
            return makeCanonical(name, null, lineNumber, columnNumber, endLineNumber, endColumnNumber, null, Kind.SYNTHETIC);
        }
    }
}
