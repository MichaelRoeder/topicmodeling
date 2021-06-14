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

import java.util.Arrays;

public abstract class AbstractArrayContainingDocumentProperty implements ArrayContainingDocumentProperty {

    private static final long serialVersionUID = -4282985203144385913L;

    @Override
    public Object getValue() {
        return getValueAsArray();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("=\"");
        builder.append(Arrays.toString(this.getValueAsArray()));
        builder.append("\"");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getValueAsArray());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractArrayContainingDocumentProperty other = (AbstractArrayContainingDocumentProperty) obj;
        if (!Arrays.deepEquals(getValueAsArray(), other.getValueAsArray()))
            return false;
        return true;
    }
    
}
