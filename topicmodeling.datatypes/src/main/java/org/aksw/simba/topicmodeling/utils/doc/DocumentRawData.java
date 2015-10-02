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
package org.aksw.simba.topicmodeling.utils.doc;

import java.util.Arrays;

public class DocumentRawData extends AbstractDocumentProperty {

    private static final long serialVersionUID = 6808522383796850927L;

    private final byte data[];

    public DocumentRawData(byte data[]) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public Object getValue() {
        return data;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("=\"");
        builder.append(Arrays.toString(data));
        builder.append("\"");
        return builder.toString();
    };

    @Override
    public boolean equals(Object o) {
        if (o instanceof DocumentRawData) {
            if (this.data == null) {
                return ((DocumentRawData) o).data == null;
            } else {
                return Arrays.equals(this.data, ((DocumentRawData) o).data);
            }
        } else {
            return super.equals(o);
        }
    }
}
