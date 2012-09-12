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

package dk.brics.tajs.flowgraph.jsnodes;

/**
 * Shared subset of the interface that is common between ReadPropertyNode and WritePropertyNode.
 */
public interface IPropertyNode {
	
    /**
     * Returns the base register.
     */
    int getBaseRegister();

    /**
     * Sets the base register.
     */
    void setBaseRegister(int base_reg);

    /**
     * Returns the property register, or {@link dk.brics.tajs.flowgraph.AbstractNode#NO_VALUE} if not applicable.
     */
    int getPropertyRegister();

    /**
     * Set the property register.
     */
    void setPropertyRegister(int property_reg);

    /**
     * Returns the property string, or null if not fixed.
     */
    String getPropertyString();

    /**
     * Sets the property string.
     */
    void setPropertyString(String property_str);

    /**
     * Returns true if the property is a fixed string.
     */
    boolean isPropertyFixed();
}
