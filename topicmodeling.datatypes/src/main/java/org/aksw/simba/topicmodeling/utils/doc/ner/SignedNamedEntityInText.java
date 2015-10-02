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
package org.aksw.simba.topicmodeling.utils.doc.ner;

public class SignedNamedEntityInText extends NamedEntityInText {

    private static final long serialVersionUID = -5165915876107803417L;

    private String source;

    public SignedNamedEntityInText(NamedEntityInText entity) {
        super(entity);
        if (entity instanceof SignedNamedEntityInText) {
            this.source = ((SignedNamedEntityInText) entity).source;
        }
    }

    public SignedNamedEntityInText(NamedEntityInText entity, String source) {
        super(entity);
        this.source = source;
    }

    public SignedNamedEntityInText(NamedEntityInText entity, int startPos, int length) {
        super(entity, startPos, length);
        if (entity instanceof SignedNamedEntityInText) {
            this.source = ((SignedNamedEntityInText) entity).source;
        }
    }

    public SignedNamedEntityInText(int startPos, int length, String namedEntityUri, String source) {
        super(startPos, length, namedEntityUri);
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(\"");
        result.append(getNamedEntityUri());
        result.append("\", ");
        result.append(getStartPos());
        result.append(", ");
        result.append(getLength());
        if (source != null) {
            result.append(", source=\"");
            result.append(source);
            result.append("\")");
        } else {
            result.append(")");
        }
        return result.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SignedNamedEntityInText(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SignedNamedEntityInText) {
            if (compareTo((SignedNamedEntityInText) obj) == 0) {
                return this.source.equals(((SignedNamedEntityInText) obj).source);
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ source.hashCode();
    }
}
