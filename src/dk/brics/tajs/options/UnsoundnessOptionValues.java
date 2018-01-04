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

package dk.brics.tajs.options;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Option values for unsoundness.
 * <p>
 * All options are disabled by default.
 * <p>
 * (Unlike {@link OptionValues}, this is a bean with regular getters and setters.)
 */
public class UnsoundnessOptionValues {

    @Option(name = "-no-implicit-global-var-declarations", usage = "Allows ignoring implicit declaration of global variables")
    private boolean noImplicitGlobalVarDeclarations;

    @Option(name = "-ignore-missing-native-models", usage = "Allows ignoring invocations of unmodeled native functions")
    private boolean ignoreMissingNativeModels;

    @Option(name = "-use-precise-function-toString", usage = "Allows Function.prototype.toString to produce a deterministic value")
    private boolean usePreciseFunctionToString;

    @Option(name = "-ignore-imprecise-evals", usage = "Allows ignoring imprecise calls to eval")
    private boolean ignoreImpreciseEvals;

    @Option(name = "-ignore-async-evals", usage = "Allows ignoring eval as an event handler")
    private boolean ignoreAsyncEvals;

    @Option(name = "-use-ordered-object-keys", usage = "Allows a determistic ordering of the Object.keys array")
    private boolean useOrderedObjectKeys;

    @Option(name = "-ignore-locale", usage = "Allows the use of a fixed locale for locale specific functions")
    private boolean ignoreLocale;

    // TODO: this should be true by default, but old TAJS behaviour assumes false (GitHub #355)
    @Option(name = "-warn-about-all-string-coercions", usage = "Allows omitting some warnings about toString coercions")
    private boolean warnAboutAllStringCoercions;

    @Option(name = "-ignore-imprecise-function-constructor", usage = "Allows ignoring imprecise calls to Function")
    private boolean ignoreImpreciseFunctionConstructor;

    @Option(name = "-ignore-unlikely-property-reads", usage = "Allows ignoring some unlikely properties during a dynamic property read")
    private boolean ignoreUnlikelyPropertyReads;

    @Option(name = "-show-unsoundness-usage", usage = "Shows all the usages of unsoundness")
    private boolean showUnsoundnessUsage;

    @Option(name = "-ignore-some-prototypes-during-dynamic-property-reads", usage = "Allows ignoring some unlikely prototypes during a dynamic property read")
    private boolean ignoreSomePrototypesDuringDynamicPropertyReads;

    public UnsoundnessOptionValues(UnsoundnessOptionValues base, String[] args) {
        if (base != null) {
            OptionsUtil.cloneAllFields(base, this);
        }
        if (args != null) {
            CmdLineParser parser = new CmdLineParser(this);
            try {
                parser.parseArgument(args);
            } catch (CmdLineException e) {
                throw new RuntimeException("Bad arguments: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnsoundnessOptionValues that = (UnsoundnessOptionValues) o;

        if (noImplicitGlobalVarDeclarations != that.noImplicitGlobalVarDeclarations) return false;
        if (ignoreMissingNativeModels != that.ignoreMissingNativeModels) return false;
        if (usePreciseFunctionToString != that.usePreciseFunctionToString) return false;
        if (ignoreImpreciseEvals != that.ignoreImpreciseEvals) return false;
        if (ignoreAsyncEvals != that.ignoreAsyncEvals) return false;
        if (useOrderedObjectKeys != that.useOrderedObjectKeys) return false;
        if (ignoreLocale != that.ignoreLocale) return false;
        if (warnAboutAllStringCoercions != that.warnAboutAllStringCoercions) return false;
        if (ignoreImpreciseFunctionConstructor != that.ignoreImpreciseFunctionConstructor) return false;
        if (ignoreUnlikelyPropertyReads != that.ignoreUnlikelyPropertyReads) return false;
        if (showUnsoundnessUsage != that.showUnsoundnessUsage) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (noImplicitGlobalVarDeclarations ? 1 : 0);
        result = 31 * result + (ignoreMissingNativeModels ? 1 : 0);
        result = 31 * result + (usePreciseFunctionToString ? 1 : 0);
        result = 31 * result + (ignoreImpreciseEvals ? 1 : 0);
        result = 31 * result + (ignoreAsyncEvals ? 1 : 0);
        result = 31 * result + (useOrderedObjectKeys ? 1 : 0);
        result = 31 * result + (ignoreLocale ? 1 : 0);
        result = 31 * result + (warnAboutAllStringCoercions ? 1 : 0);
        result = 31 * result + (ignoreImpreciseFunctionConstructor ? 1 : 0);
        result = 31 * result + (ignoreUnlikelyPropertyReads ? 1 : 0);
        result = 31 * result + (showUnsoundnessUsage ? 1 : 0);
        result = 31 * result + (ignoreSomePrototypesDuringDynamicPropertyReads ? 1 : 0);
        return result;
    }

    public boolean isIgnoreLocale() {
        return ignoreLocale;
    }

    public void setIgnoreLocale(boolean ignoreLocale) {
        this.ignoreLocale = ignoreLocale;
    }

    public boolean isIgnoreMissingNativeModels() {
        return ignoreMissingNativeModels;
    }

    public void setIgnoreMissingNativeModels(boolean ignoreMissingNativeModels) {
        this.ignoreMissingNativeModels = ignoreMissingNativeModels;
    }

    public boolean isUsePreciseFunctionToString() {
        return usePreciseFunctionToString;
    }

    public void setUsePreciseFunctionToString(boolean usePreciseFunctionToString) {
        this.usePreciseFunctionToString = usePreciseFunctionToString;
    }

    public boolean isIgnoreImpreciseEvals() {
        return ignoreImpreciseEvals;
    }

    public void setIgnoreImpreciseEvals(boolean ignoreImpreciseEvals) {
        this.ignoreImpreciseEvals = ignoreImpreciseEvals;
    }

    public boolean isUseOrderedObjectKeys() {
        return useOrderedObjectKeys;
    }

    public void setUseOrderedObjectKeys(boolean useOrderedObjectKeys) {
        this.useOrderedObjectKeys = useOrderedObjectKeys;
    }

    public boolean isIgnoreAsyncEvals() {
        return ignoreAsyncEvals;
    }

    public void setIgnoreAsyncEvals(boolean ignoreAsyncEvals) {
        this.ignoreAsyncEvals = ignoreAsyncEvals;
    }

    public boolean isWarnAboutAllStringCoercions() {
        return warnAboutAllStringCoercions;
    }

    public void setWarnAboutAllStringCoercions(boolean warnAboutAllStringCoercions) {
        this.warnAboutAllStringCoercions = warnAboutAllStringCoercions;
    }

    public boolean isIgnoreImpreciseFunctionConstructor() {
        return ignoreImpreciseFunctionConstructor;
    }

    public void setIgnoreImpreciseFunctionConstructor(boolean ignoreImpreciseFunctionConstructor) {
        this.ignoreImpreciseFunctionConstructor = ignoreImpreciseFunctionConstructor;
    }

    public boolean isNoImplicitGlobalVarDeclarations() {
        return noImplicitGlobalVarDeclarations;
    }

    public void setNoImplicitGlobalVarDeclarations(boolean noImplicitGlobalVarDeclarations) {
        this.noImplicitGlobalVarDeclarations = noImplicitGlobalVarDeclarations;
    }

    public boolean isIgnoreUnlikelyPropertyReads() {
        return ignoreUnlikelyPropertyReads;
    }

    public void setIgnoreUnlikelyPropertyReads(boolean ignoreUnlikelyPropertyReads) {
        this.ignoreUnlikelyPropertyReads = ignoreUnlikelyPropertyReads;
    }

    public boolean isShowUnsoundnessUsage() {
        return showUnsoundnessUsage;
    }

    public void setShowUnsoundnessUsage(boolean showUnsoundnessUsage) {
        this.showUnsoundnessUsage = showUnsoundnessUsage;
    }

    public boolean isIgnoreSomePrototypesDuringDynamicPropertyReads() {
        return ignoreSomePrototypesDuringDynamicPropertyReads;
    }

    public void setIgnoreSomePrototypesDuringDynamicPropertyReads(boolean ignoreSomePrototypesDuringDynamicPropertyReads) {
        this.ignoreSomePrototypesDuringDynamicPropertyReads = ignoreSomePrototypesDuringDynamicPropertyReads;
    }
}
