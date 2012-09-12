/*
 * Copyright 2012 Aarhus University
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

package dk.brics.tajs.lattice;

import static dk.brics.tajs.util.Collections.newSet;

import java.util.Map;
import java.util.Set;

public class Renaming {

	/**
	 * Renames the given object label according to the map.
	 * If the object label is null or it does not occur in the map, it is returned unchanged. 
	 */
	public static ObjectLabel apply(Map<ObjectLabel, ObjectLabel> m, ObjectLabel objlabel) {
		if (objlabel == null)
			return null;
		ObjectLabel new_objlabel = m.get(objlabel);
		if (new_objlabel == null)
			new_objlabel = objlabel;
		return new_objlabel;
	}

	/**
	 * Renames the given object labels according to the map.
	 */
	public static Set<ObjectLabel> apply(Map<ObjectLabel, ObjectLabel> m, Set<ObjectLabel> objlabels) {
		if (objlabels == null)
			return null;
		Set<ObjectLabel> new_objlabels = newSet();
		for (ObjectLabel objlabel : objlabels)
			new_objlabels.add(Renaming.apply(m, objlabel));
		return new_objlabels;
	}

	/**
	 * Renames the given property reference according to the map.
	 */
	public static PropertyReference apply(Map<ObjectLabel, ObjectLabel> m, PropertyReference p) {
		if (m.isEmpty())
			return p;
		return p.makeRenamed(apply(m, p.getObjectLabel()));
	}
}
