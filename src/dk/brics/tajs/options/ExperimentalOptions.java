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

package dk.brics.tajs.options;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Utility class for experimental options.
 * <p>
 * Immutable.
 */
public class ExperimentalOptions implements Serializable {

    /**
     * The set of enabled options.
     */
    private final Set<ExperimentalOption> enabled = newSet();

    /**
     * Constructor that enables the desired options (as a shallow copy).
     */
    public ExperimentalOptions(Collection<ExperimentalOption> enabled) {
        this.enabled.addAll(enabled);
    }

    /**
     * @see #ExperimentalOptions(Collection)
     */
    public ExperimentalOptions(ExperimentalOption... enabled) {
        this(Arrays.asList(enabled));
    }

    /**
     * Cloning constructor.
     *
     * @see #ExperimentalOptions(Collection)
     */
    public ExperimentalOptions(ExperimentalOptions experimental) {
        this(experimental.enabled);
    }

    /**
     * Utility function: Intersects two sets of enabled options.
     */
    @SuppressWarnings("unused")
    public ExperimentalOptions intersect(ExperimentalOptions b) {
        Set<ExperimentalOption> enabled = newSet();
        enabled.addAll(this.enabled);
        enabled.retainAll(b.enabled);
        return new ExperimentalOptions(enabled);
    }

    /**
     * Utility function: copies the set of enabled options, except for some specific options.
     */
    public ExperimentalOptions difference(ExperimentalOption toDisable) {
        ExperimentalOptions withDisabled = new ExperimentalOptions(this);
        withDisabled.enabled.remove(toDisable);
        return withDisabled;
    }

    public ExperimentalOptions join(ExperimentalOptions other) {
        ExperimentalOptions joined = new ExperimentalOptions();
        joined.enabled.addAll(this.enabled);
        joined.enabled.addAll(other.enabled);
        return joined;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ExperimentalOptions that = (ExperimentalOptions) o;
        return enabled.equals(that.enabled);
    }

    public Set<ExperimentalOption> getEnabled() {
        return newSet(enabled);
    }

    @Override
    public String toString() {
        return getEnabledOptionsString();
    }

    /**
     * A toString of the enabled options.
     */
    public String getEnabledOptionsString() {
        StringBuilder sb = new StringBuilder();
        for (ExperimentalOption option : enabled) {
            if (sb.length() != 0) {
                sb.append(" ");
            }
            sb.append(option);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return enabled.hashCode();
    }

    public boolean isEnabled(ExperimentalOption option) {
        return enabled.contains(option);
    }

    /**
     * Interface for TAJS variants. Version-control-wise: new subclasses should not be merged upstream to master.
     */
    public interface ExperimentalOption {

    }

    /**
     * Singleton class for managing experimental options.
     * <p>
     * (similar to {@link Options}
     */
    public static class ExperimentalOptionsManager {

        private static ExperimentalOptions options = null;

        public static ExperimentalOptions get() {
            if (options == null) {
                options = new ExperimentalOptions();
            }
            return options;
        }

        public static void reset() {
            options = null;
        }

        public static void set(ExperimentalOptions options) {
            ExperimentalOptionsManager.options = options;
        }
    }
}
