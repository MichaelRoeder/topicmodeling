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
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public abstract class AbstractDocumentPropertyMapCreator<T extends DocumentProperty> extends
        AbstractDocumentSupplierDecorator {

    private final Class<T> DOCUMENT_PROPERTY_CLASS;
    private IntObjectOpenHashMap<T> properties = new IntObjectOpenHashMap<T>();

    public AbstractDocumentPropertyMapCreator(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        DOCUMENT_PROPERTY_CLASS = propertyClass;
    }

    @Override
    public Document getNextDocument() {
        Document document = documentSource.getNextDocument();
        if (document != null)
        {
            document = prepareDocument(document);
        } else {
            mapCreated(properties);
        }
        return document;
    }

    @Override
    protected Document prepareDocument(Document document) {
        T property = document.getProperty(DOCUMENT_PROPERTY_CLASS);
        if (property != null) {
            properties.put(document.getDocumentId(), property);
        }
        return document;
    }

    protected abstract void mapCreated(IntObjectOpenHashMap<T> properties);

}
