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

import java.net.URL;

/**
 * Source location.
 */
public class SourceLocation implements Comparable<SourceLocation> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceLocation that = (SourceLocation) o;

        if (linenumber != that.linenumber) return false;
        if (columnnumber != that.columnnumber) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        return prettyFileName != null ? prettyFileName.equals(that.prettyFileName) : that.prettyFileName == null;
    }

    @Override
    public int hashCode() {
        int result = linenumber;
        result = 31 * result + columnnumber;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (prettyFileName != null ? prettyFileName.hashCode() : 0);
        return result;
    }

    private int linenumber;

    private int columnnumber;

    private final URL location;

    private String prettyFileName;

    /**
     * Constructs a new source location.
     * 0 means "no number".
     * Each occurrence of '\' is replaced by '/' in the pretty file name.
     */
    public SourceLocation(int linenumber, int columnnumber, String prettyFileName, URL location) {
        this.linenumber = linenumber;
        this.columnnumber = columnnumber;
        this.prettyFileName = prettyFileName.replace('\\', '/');
        this.location = location;
    }

    /**
     * Returns the source file name.
     */
    public String getPrettyFileName() {
        return prettyFileName;
    }

    /**
     * Returns the source line number.
     * 0 means "no number".
     */
    public int getLineNumber() {
        return linenumber;
    }

    /**
     * Returns the source column number.
     * 0 means "no number".
     */
    public int getColumnNumber() {
        return columnnumber;
    }

    /**
     * Returns a string description of the source information associated with this node.
     */
    @Override
    public String toString() {
        return prettyFileName + (linenumber > 0 ? ":" + linenumber + (columnnumber > 0 ? ":" + columnnumber : "") : "");
    }

    /**
     * Compares source locations first by file name, then by line number, and finally by column number.
     */
    @Override
    public int compareTo(SourceLocation e) {
        int c = prettyFileName.compareTo(e.prettyFileName);
        if (c != 0)
            return c;
        c = linenumber - e.linenumber;
        if (c != 0)
            return c;
        return columnnumber - e.columnnumber;
    }

    public URL getLocation() {
        return location;
    }
}
