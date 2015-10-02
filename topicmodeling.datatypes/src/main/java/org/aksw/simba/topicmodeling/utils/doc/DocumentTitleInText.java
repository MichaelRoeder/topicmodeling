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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentTitleInText extends AbstractDocumentProperty implements ParseableDocumentProperty {

    private static final long serialVersionUID = -4284876415737605396L;

    private static final transient Logger LOGGER = LoggerFactory.getLogger(DocumentTitleInText.class);

    private int startId;
    private int length;

    public DocumentTitleInText(int startId, int length) {
        this.startId = startId;
        this.length = length;
    }

    @Override
    public Object getValue() {
        StringBuilder value = new StringBuilder();
        value.append(startId);
        value.append('|');
        value.append(length);
        return value.toString();
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DocumentTitleInText) {
            return (this.startId == ((DocumentTitleInText) obj).startId)
                    && (this.length == ((DocumentTitleInText) obj).length);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (startId * 31) + length;
    }

    @Override
    public String toString() {
        return "DocumentTitleInText=(start=" + startId + ", length=" + length + ")";
    }

    @Override
    public void parseValue(String value) {
        int pos = value.indexOf('|');
        if (pos != -1) {
            try {
                startId = Integer.parseInt(value.substring(0, pos));
                length = Integer.parseInt(value.substring(pos + 1));
            } catch (NumberFormatException e) {
                LOGGER.error("Couldn't parse DocumentTitleInText property from \"" + value
                        + "\". Setting startId and length to 0.");
                startId = 0;
                length = 0;
            }
        }
    }
}
