/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;

public abstract class AbstractDocumentPropertyBasedFilter<T extends DocumentProperty> implements DocumentFilter {

    private final Class<T> DOCUMENT_PROPERTY_CLASS;
    private final boolean ACCEPT_DOCUMENT_WITHOUT_PROPERTY;

    public AbstractDocumentPropertyBasedFilter(Class<T> propertyClass) {
        DOCUMENT_PROPERTY_CLASS = propertyClass;
        ACCEPT_DOCUMENT_WITHOUT_PROPERTY = false;
    }

    public AbstractDocumentPropertyBasedFilter(Class<T> propertyClass, boolean acceptDocumentWithoutProperty) {
        DOCUMENT_PROPERTY_CLASS = propertyClass;
        ACCEPT_DOCUMENT_WITHOUT_PROPERTY = acceptDocumentWithoutProperty;
    }

    @Override
    public boolean isDocumentGood(Document document) {
        T property = document.getProperty(DOCUMENT_PROPERTY_CLASS);
        if (property == null) {
            return ACCEPT_DOCUMENT_WITHOUT_PROPERTY;
        } else {
            return isDocumentPropertyGood(property);
        }
    }

    protected abstract boolean isDocumentPropertyGood(T property);
}
