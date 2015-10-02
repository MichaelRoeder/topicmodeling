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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class Document
 */
public class Document implements Comparable<Document>, Serializable, Iterable<DocumentProperty> {

    private static final long serialVersionUID = -3213426637730517409L;

    protected int documentId;
    protected HashMap<Class<? extends DocumentProperty>, DocumentProperty> properties = new HashMap<Class<? extends DocumentProperty>, DocumentProperty>();

    public Document() {
    }

    public Document(int documentId) {
        this.documentId = documentId;
    }

    public Document(int documentId, DocumentProperty properties[]) {
        this.documentId = documentId;
        for (DocumentProperty documentProperty : properties) {
            addProperty(documentProperty);
        }
    }

    public Iterator<Class<? extends DocumentProperty>> getPropertiesIterator() {
        return properties.keySet().iterator();
    }

    public void removeProperty(Class<? extends DocumentProperty> propertyClass) {
        properties.remove(propertyClass);
    }

    /**
     * @param property
     */
    public <T extends DocumentProperty> void addProperty(T property) {
        properties.put(property.getClass(), property);
    }

    /**
     * @return com.unister.semweb.topic_modeling.utils.DocumentProperty
     * @param propertyClass
     */
    @SuppressWarnings("unchecked")
    public <T extends DocumentProperty> T getProperty(Class<T> propertyClass) {
        return (T) properties.get(propertyClass);
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Document) {
            return this.documentId == ((Document) obj).getDocumentId();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int compareTo(Document document) {
        if (this.documentId == document.getDocumentId()) {
            return 0;
        }
        return this.documentId < document.getDocumentId() ? -1 : 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Document id=" + documentId);
        result.append("\n[ ");
        Iterator<Class<? extends DocumentProperty>> iterator = getPropertiesIterator();
        boolean firstProp = true;
        while (iterator.hasNext()) {
            if (firstProp) {
                firstProp = false;
            } else {
                result.append(",\n ");
            }
            result.append(getProperty(iterator.next()).toString());
        }
        result.append("]\n");
        return result.toString();
    }

    public Iterator<DocumentProperty> iterator() {
        return new DocumentPropertyIterator(this);
    }
}
