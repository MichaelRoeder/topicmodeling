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
 * Class DocumentId
 */
public class DocumentSource extends AbstractSimpleDocumentProperty<String> implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -6144552726686647479L;

    public DocumentSource() {
        super("");
    }

    public DocumentSource(String source) {
        super(source);
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
