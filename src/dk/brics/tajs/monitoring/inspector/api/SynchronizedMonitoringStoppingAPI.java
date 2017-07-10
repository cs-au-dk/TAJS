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

package dk.brics.tajs.monitoring.inspector.api;

import dk.brics.inspector.api.InspectorAPI;
import dk.brics.inspector.api.model.Optional;
import dk.brics.inspector.api.model.RelatedLocationKind;
import dk.brics.inspector.api.model.ids.ContextID;
import dk.brics.inspector.api.model.ids.FileID;
import dk.brics.inspector.api.model.ids.LocationID;
import dk.brics.inspector.api.model.ids.ObjectID;
import dk.brics.inspector.api.model.lines.Gutter;
import dk.brics.inspector.api.model.lines.LineValue;
import dk.brics.inspector.api.model.locations.ContextSensitiveDescribedLocation;
import dk.brics.inspector.api.model.locations.DescribedContext;
import dk.brics.inspector.api.model.locations.DescribedLocation;
import dk.brics.inspector.api.model.locations.FileDescription;
import dk.brics.inspector.api.model.options.OptionData;
import dk.brics.inspector.api.model.values.DescribedProperties;
import dk.brics.tajs.monitoring.TogglableMonitor.Toggler;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Synchronized decorator that disables the monitoring system before invoking the decoratee method.
 * This is needed because the decoratee might use features that are being monitored, but we do not want such usages to be monitored!
 */
public class SynchronizedMonitoringStoppingAPI implements InspectorAPI {

    private final InspectorAPI api;

    private final Toggler toggler;

    public SynchronizedMonitoringStoppingAPI(InspectorAPI api, Toggler toggler) {
        this.api = api;
        this.toggler = toggler;
    }

    @Override
    public Set<FileID> getFileIDs() {
        return wrap(api::getFileIDs);
    }

    private synchronized <T> T wrap(Supplier<T> f) {
        boolean wasEnabled = toggler.isEnabled();
        toggler.setEnabled(false);
        T t = f.get();
        toggler.setEnabled(wasEnabled);
        return t;
    }

    @Override
    public FileDescription getFileDescription(FileID id) {
        return wrap(() -> api.getFileDescription(id));
    }

    @Override
    public Set<Gutter<?>> getGutters(FileID id) {
        return wrap(() -> api.getGutters(id));
    }

    @Override
    public OptionData getOptions() {
        return wrap(api::getOptions);
    }

    @Override
    public Set<LineValue> getLineValues(FileID fileID, int line) {
        return wrap(() -> api.getLineValues(fileID, line));
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getAllocationLocations(ObjectID objectID) {
        return wrap(() -> api.getAllocationLocations(objectID));
    }

    @Override
    public DescribedProperties getObjectProperties(ObjectID objectID, LocationID locationID) {
        return wrap(() -> api.getObjectProperties(objectID, locationID));
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getCallLocations(ObjectID objectID) {
        return wrap(() -> api.getCallLocations(objectID));
    }

    @Override
    public Set<ContextSensitiveDescribedLocation> getEventHandlerRegistrationLocations(ObjectID objectID) {
        return wrap(() -> api.getEventHandlerRegistrationLocations(objectID));
    }

    @Override
    public Set<? extends DescribedLocation> getRelatedLocations(LocationID locationID, boolean forwards, RelatedLocationKind kind, boolean intraprocedural) {
        return wrap(() -> api.getRelatedLocations(locationID, forwards, kind, intraprocedural));
    }

    @Override
    public Set<ObjectID> getEnclosingFunction(LocationID locationID) {
        return wrap(() -> api.getEnclosingFunction(locationID));
    }

    @Override
    public Optional<DescribedLocation> getPositionalLocationID(FileID fileID, int line, int column, java.util.Optional<ContextID> context) {
        return wrap(() -> api.getPositionalLocationID(fileID, line, column, context));
    }

    @Override
    public Set<DescribedContext> getFilteredContexts(LocationID locationID, String expressionString) {
        return wrap(() -> api.getFilteredContexts(locationID, expressionString));
    }
}
