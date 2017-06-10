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

import org.apache.log4j.Logger;

import java.util.Map.Entry;

/**
 * Global analysis options.
 */
public class Options {

    private static final Logger log = Logger.getLogger(Options.class);

    private static OptionValues optionValues = new OptionValues();

    private Options() {
    }

    /**
     * Sets all the options.
     */
    public static void set(OptionValues optionValues) {
        Options.optionValues = optionValues.clone();
    }

    /**
     * Gets the options.
     */
    public static OptionValues get() {
        return optionValues;
    }

    /**
     * Resets all options.
     */
    public static void reset() {
        optionValues = new OptionValues();
    }

    /**
     * Prints the settings (if in debug mode).
     */
    public static void dump() {
        for (Entry<String, Object> optionValue : optionValues.getOptionValues().entrySet()) {
            log.debug(String.format("%-30s %20s", optionValue.getKey(), optionValue.getValue()));
        }
    }

    /**
     * Parses command line arguments <em>in addition to</em> the already set options.
     */
    public static void parse(String[] args) {
        optionValues = new OptionValues(optionValues, args);
    }
}
