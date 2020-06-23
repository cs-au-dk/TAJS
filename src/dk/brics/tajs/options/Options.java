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

package dk.brics.tajs.options;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineParser;

import java.io.StringWriter;
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
     * Prints a description of the available options.
     */
    public static void showUsage() {
        StringWriter w = new StringWriter();
        CmdLineParser p = new CmdLineParser(optionValues);
        p.getProperties().withUsageWidth(150);
        p.printUsage(w, null);
        w.write("\n Arguments to option '-unsound':\n\n");
        CmdLineParser pu = new CmdLineParser(optionValues.getUnsoundness());
        pu.getProperties().withUsageWidth(150);
        pu.printUsage(w, null);
        log.info(w);
    }

    public static class Constants {

        private Constants() {}

        // General precision/performance tuning
        public static final int NUMBER_OF_UNKNOWN_ARGUMENTS_TO_KEEP_DISJOINT = 10;
        public static final int MAX_CONTEXT_SPECIALIZATION = 10;
        public static final int ARRAY_TRUNCATION_BOUND = 25;
        public static final int STRING_CONCAT_SETS_BOUND = 15;
        public static final int STRING_SETS_BOUND = 250;

        // Fine performance tuning
        public static final int HYBRID_ARRAY_HASH_SET_ARRAY_SIZE = 8;
        public static final int HYBRID_ARRAY_HASH_MAP_ARRAY_SIZE = 8;
    }
}
