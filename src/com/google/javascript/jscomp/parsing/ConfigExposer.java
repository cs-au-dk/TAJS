package com.google.javascript.jscomp.parsing;

import java.util.Set;

/**
 * Gives access to com.google.javascript.jscomp.parsing.Config.
 */
public class ConfigExposer {
    public static Config createConfig(Set<String> annotationWhitelist, Set<String> suppressionNames,
                                      boolean isIdeMode, Config.LanguageMode languageMode,
                                      boolean acceptConstKeyword) {
        return new Config(annotationWhitelist, suppressionNames, isIdeMode, languageMode, acceptConstKeyword);
    }
}
