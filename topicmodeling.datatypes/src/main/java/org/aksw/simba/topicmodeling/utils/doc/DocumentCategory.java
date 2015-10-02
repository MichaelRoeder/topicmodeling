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


/**
 * Class DocumentClass
 */
public class DocumentCategory extends AbstractSimpleDocumentProperty<String> implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -799781054037155226L;

    public DocumentCategory() {
        super("");
    }

    public DocumentCategory(String category) {
        super(category);
    }

    public String getCategory() {
        return get();
    }

    public void setCategory(String category) {
        set(category);
    }

    @Override
    public void parseValue(String value) {
        set(value);
    }

    @Override
    public String getStringValue() {
        return get();
    }
}
