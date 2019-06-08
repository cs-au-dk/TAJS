/*
 * Copyright 2009-2019 Aarhus University
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

package dk.brics.tajs.monitoring.inspector.datacollection;

import java.net.URL;
import java.util.Objects;

/**
 * Represents a line of source code.
 */
public class SourceLine {

    private final URL location;

    private final int line;

    public SourceLine(URL location, int line) {
        this.location = location;
        this.line = line;
    }

    public URL getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceLine that = (SourceLine) o;

        if (line != that.line) return false;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + line;
        return result;
    }

    public int getLine() {
        return line;
    }
}
