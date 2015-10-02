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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPropertyEditingDocumentSupplierDecorator<T extends DocumentProperty>
        extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractPropertyAppendingDocumentSupplierDecorator.class);

    private Class<T> propertyClass;

    public AbstractPropertyEditingDocumentSupplierDecorator(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        this.propertyClass = propertyClass;
    }

    @Override
    protected Document prepareDocument(Document document) {
        T property = document.getProperty(propertyClass);
        if (property == null) {
            LOGGER.info("Couldn't get property " + propertyClass.getSimpleName() + " for document #"
                    + document.getDocumentId() + ".(" + this.getClass().getSimpleName() + ")");
        } else {
            editDocumentProperty(property);
        }
        return document;
    }

    protected abstract void editDocumentProperty(T property);

}
