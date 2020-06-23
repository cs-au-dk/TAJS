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

package dk.brics.tajs.blendedanalysis;

import dk.brics.tajs.flowgraph.SourceLocation;
import dk.brics.tajs.util.Collectors;

import java.io.Serializable;
import java.util.Set;

public class BlendedAnalysisOptionValues implements Serializable {

    /**
     * If not null, allow only blended analysis at the source locations in this set. If null, allow blended analysis at all locations.
     */
    private Set<String> allowedBlendedAnalysisSourceLocations = null;

    /**
     * If set, and enabling blended analysis filters the value to bottom, then use sound default instead.
     */
    private boolean disallowRefineToBottom = false;

    public boolean isDisableRefineToBottom() {
        return disallowRefineToBottom;
    }

    public void setDisallowRefineToBottom(boolean disallowRefineToBottom) {
        this.disallowRefineToBottom = disallowRefineToBottom;
    }

    /**
     * If set, blended analysis will be enabled when analyzing module initialization (top-level) code
     */
    private boolean onlyForModuleInit = false;

    public boolean isOnlyForModuleInit() {
        return onlyForModuleInit;
    }

    public void setOnlyForModuleInit(boolean onlyForModuleInit) {
        this.onlyForModuleInit = onlyForModuleInit;
    }

    public void reset() {
        allowedBlendedAnalysisSourceLocations = null;
        disallowRefineToBottom = false;
        onlyForModuleInit = false;
    }

    @Override
    public String toString() {
        return "BlendedAnalysisOptionValues{" +
                "allowedBlendedAnalysisSourceLocations=" + allowedBlendedAnalysisSourceLocations +
                ", disallowRefineToBottom=" + disallowRefineToBottom +
                ", onlyForModuleInit=" + onlyForModuleInit +
                '}';
    }

    public void setAllowedBlendedAnalysisSourceLocations(Set<SourceLocation> allowedBlendedAnalysisSourceLocations) {
        this.allowedBlendedAnalysisSourceLocations =
                allowedBlendedAnalysisSourceLocations.stream().map(SourceLocation::toString).collect(Collectors.toSet());
    }

    public boolean isBlendedAnalysisAtSourceLocationAllowed(SourceLocation sl) {
        return allowedBlendedAnalysisSourceLocations == null || allowedBlendedAnalysisSourceLocations.contains(sl.toString());
    }

}
