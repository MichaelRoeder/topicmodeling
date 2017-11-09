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
package org.dice_research.topicmodeling.utils.corpus.properties;

public abstract class AbstractCorpusProperty implements CorpusProperty {

    private static final long serialVersionUID = -449507835435340908L;

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
        result.append("=\"");
        result.append(getValue().toString());
        result.append('"');
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof CorpusProperty) {
            Object value = this.getValue();
            if (value == null) {
                return ((CorpusProperty) obj).getValue() == null;
            } else {
                return value.equals(((CorpusProperty) obj).getValue());
            }
        } else {
            return super.equals(obj);
        }
    }
}
