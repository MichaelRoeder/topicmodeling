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

public abstract class AbstractDocumentProperty implements DocumentProperty {

    private static final long serialVersionUID = 4242987535838261287L;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("=\"");
        builder.append(this.getValue().toString());
        builder.append("\"");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DocumentProperty) {
            if (this.getValue() == null) {
                return ((DocumentProperty) o).getValue() == null;
            } else {
                return this.getValue().equals(((DocumentProperty) o).getValue());
            }
        }
        return super.equals(o);
    }
}
