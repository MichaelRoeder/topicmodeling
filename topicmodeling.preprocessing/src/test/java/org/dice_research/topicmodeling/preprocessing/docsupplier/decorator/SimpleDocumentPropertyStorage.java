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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;



public class SimpleDocumentPropertyStorage<T extends DocumentProperty> extends AbstractDocumentSupplierDecorator {

    private Class<T> propertyClass;
    private T property;

    public SimpleDocumentPropertyStorage(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        this.propertyClass = propertyClass;
    }

    public T getProperty() {
        return property;
    }

    @Override
    protected Document prepareDocument(Document document) {
        property = document.getProperty(propertyClass);
        return document;
    }
}
