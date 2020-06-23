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

package dk.brics.tajs.monitoring.inspector.dataprocessing;

import dk.brics.inspector.api.model.ids.AbstractID;
import dk.brics.inspector.api.model.ids.ContextID;
import dk.brics.inspector.api.model.ids.FileID;
import dk.brics.inspector.api.model.ids.LocationID;
import dk.brics.inspector.api.model.ids.ObjectID;
import dk.brics.tajs.flowgraph.AbstractNode;
import dk.brics.tajs.lattice.Context;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.util.Pair;

import java.net.URL;
import java.util.Map;
import java.util.function.Function;

import static dk.brics.tajs.util.Collections.newMap;

/**
 * Manager (preferably used as a singleton) for mapping complex objects and opaque identifiers.
 */
public class IDManager {

    private final Map<Object, Object> map = newMap();

    public FileID make(URL url) {
        return makeRaw(url, FileID::new); // XXX relying on URL equals
    }

    @SuppressWarnings("unchecked")
    private <T> T resolveRaw(AbstractID id) {
        return (T) getRaw(id);
    }

    private Object getRaw(Object key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Key does not exist: " + key);
        }
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractID> T makeRaw(Object nonID, Function<Integer, T> idMaker) {
        if (nonID == null) {
            throw new NullPointerException("null can not be assigned an ID!");
        }
        assert !(nonID instanceof AbstractID);
        if (!map.containsKey(nonID)) {
            T id = idMaker.apply(map.size() / 2);
            map.put(nonID, id);
            map.put(id, nonID);
        }
        return (T) getRaw(nonID);
    }

    public ObjectLabel resolve(ObjectID id) {
        return resolveRaw(id);
    }

    public ObjectID make(ObjectLabel objectLabel) {
        return makeRaw(objectLabel, ObjectID::new);
    }

    public Pair<AbstractNode, Context> resolve(LocationID locationID) {
        return resolveRaw(locationID);
    }

    public ContextID make(Context context) {
        return makeRaw(context, ContextID::new);
    }

    public LocationID make(Pair<AbstractNode, Context> location) {
        return makeRaw(location, LocationID::new);
    }

    public LocationID make(AbstractNode node) {
        return makeRaw(Pair.make(node, null), LocationID::new);
    }

    public URL resolve(FileID id) {
        return resolveRaw(id);
    }

    public Context resolve(ContextID id) {
        return resolveRaw(id);
    }
}
