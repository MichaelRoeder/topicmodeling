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
import org.aksw.simba.topicmodeling.utils.doc.DocumentTextWordIds;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentWordCountingSupplierDecorator extends
        AbstractPropertyAppendingDocumentSupplierDecorator<DocumentWordCounts> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentWordCountingSupplierDecorator.class);

    public DocumentWordCountingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected DocumentWordCounts createPropertyForDocument(Document document) {
        DocumentTextWordIds textWordIds = document.getProperty(DocumentTextWordIds.class);
        DocumentWordCounts counts = new DocumentWordCounts();
        if (textWordIds != null) {
            int wordIds[] = textWordIds.getWordIds();
            for (int w = 0; w < wordIds.length; ++w) {
                counts.increaseWordCount(wordIds[w], 1);
            }
        } else {
            LOGGER.error("Got a Document object without the needed DocumentTextWordIds property! Returning empty DocumentWordCounts object.");
        }
        return counts;
    }
}
