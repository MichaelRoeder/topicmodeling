/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.utils.doc;

/**
 * This interface represents a {@link DocumentProperty} which contains a String
 * as value.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public interface StringContainingDocumentProperty extends DocumentProperty {

    /**
     * Getter returning the value of this property as String.
     * 
     * @return the value of this property
     */
    public String getStringValue();

    /**
     * This method returns <code>true</code> if the value of this property is empty,
     * i.e., the value is either <code>null</code> or it is the empty String.
     * 
     * @return <code>true</code> if the value of this property is <code>null</code>
     *         or the empty String, else <code>false</code>
     */
    public default boolean isEmpty() {
        String value = getStringValue();
        return value == null || value.isEmpty();
    }
}
