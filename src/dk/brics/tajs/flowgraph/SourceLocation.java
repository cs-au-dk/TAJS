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

package dk.brics.tajs.flowgraph;

/**
 * Source location.
 */
public class SourceLocation implements Comparable<SourceLocation> {

    private int linenumber;

    private int columnnumber;

    private String filename;

    /**
     * Constructs a new source location.
     * 0 means "no number".
     */
    public SourceLocation(int linenumber, int columnnumber, String filename) {
        this.linenumber = linenumber;
        this.columnnumber = columnnumber;
        this.filename = filename;
    }

    /**
     * Returns the source file name.
     */
    public String getFileName() {
        return filename;
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
        return filename + (linenumber > 0 ? ":" + linenumber + (columnnumber > 0 ? ":" + columnnumber : "") : "");
    }

    /**
     * Returns a hash code for this object.
     */
    @Override
    public int hashCode() {
        return filename.hashCode() * 31 + linenumber * 3 + columnnumber * 7;
    }

    /**
     * Checks whether this and the given object represent the same source location.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SourceLocation other = (SourceLocation) obj;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        return !(linenumber != other.linenumber || columnnumber != other.columnnumber);
    }

    /**
     * Compares source locations first by file name, then by line number, and finally by column number.
     */
    @Override
    public int compareTo(SourceLocation e) {
        int c = filename.compareTo(e.filename);
        if (c != 0)
            return c;
        c = linenumber - e.linenumber;
        if (c != 0)
            return c;
        return columnnumber - e.columnnumber;
    }
}
