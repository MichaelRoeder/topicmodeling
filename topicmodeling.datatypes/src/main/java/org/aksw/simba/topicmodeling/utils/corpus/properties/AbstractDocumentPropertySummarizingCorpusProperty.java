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
package org.aksw.simba.topicmodeling.utils.corpus.properties;

import java.lang.reflect.Array;

import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractDocumentPropertySummarizingCorpusProperty<T extends DocumentProperty> extends
        AbstractCorpusProperty implements StateStoringProperty {

    private static final long serialVersionUID = -6676722966128172229L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractDocumentPropertySummarizingCorpusProperty.class);

    protected T documentProperties[];
    protected Class<T> propertyClass;
    protected Corpus corpus;

    @SuppressWarnings("unchecked")
    public AbstractDocumentPropertySummarizingCorpusProperty(Corpus corpus, Class<T> propertyClass) {
        this.corpus = corpus;
        this.propertyClass = propertyClass;
        documentProperties = (T[]) Array.newInstance(propertyClass, 0);
    }

    @SuppressWarnings("unchecked")
    protected void summarizeProperties() {
        documentProperties = (T[]) Array.newInstance(propertyClass, corpus.getNumberOfDocuments());
        T property;
        int nullCount = 0;
        for (int d = 0; d < corpus.getNumberOfDocuments(); ++d) {
            property = corpus.getDocument(d).getProperty(propertyClass);
            documentProperties[d] = property;
            if (property == null) {
                ++nullCount;
            }
        }
        if (nullCount > 0) {
            LOGGER.warn("Got " + nullCount + " of the " + documentProperties.length
                    + " processed documents without a property of the type " + propertyClass.getSimpleName() + ".");
        }
    }

    protected T getDocumentProperty(int documentId) {
        if ((documentId >= 0) && (documentId < documentProperties.length)) {
            if (documentProperties[documentId] == null) {
                Document doc = corpus.getDocument(documentId);
                documentProperties[documentId] = doc.getProperty(propertyClass);
            }
            return documentProperties[documentId];
        } else {
            if (documentProperties.length < corpus.getNumberOfDocuments()) {
                summarizeProperties();
                return getDocumentProperty(documentId);
            }
            LOGGER.error("Couldn't return DocumentWordCounts for unknown document #"
                    + documentId
                    + ". Returning null.");
            return null;
        }
    }

    @Override
    public Object getValue() {
        return documentProperties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        documentProperties = (T[]) Array.newInstance(propertyClass, 0);
    }
}
