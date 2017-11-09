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
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCooccurrenceCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SlidingWindowCooccurrenceCounter extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlidingWindowCooccurrenceCounter.class);

    private int windowSize;

    public SlidingWindowCooccurrenceCounter(DocumentSupplier documentSource, int windowSize) {
        super(documentSource);
        this.windowSize = windowSize;
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentTextWordIds wordIds = document.getProperty(DocumentTextWordIds.class);
        if (wordIds == null) {
            LOGGER.error("Got document #" + document.getDocumentId()
                    + " without the needed DocumentTextWordIds property.");
        } else {
            document.addProperty(countWordCooccurrences(wordIds));
        }
        return document;
    }

    private DocumentWordCooccurrenceCount countWordCooccurrences(DocumentTextWordIds wordIds) {
        DocumentWordCooccurrenceCount cooccurrences = new DocumentWordCooccurrenceCount();
        int ids[] = wordIds.getWordIds();
        for (int i = 1; i < Math.min(windowSize, ids.length); ++i) {
            for (int j = 0; j < i; ++j) {
                cooccurrences.increaseCooccurrenceCount(ids[i], ids[j], 1);
            }
        }
        for (int i = windowSize; i < ids.length; ++i) {
            for (int j = (i + 1) - windowSize; j < i; ++j) {
                cooccurrences.increaseCooccurrenceCount(ids[i], ids[j], 1);
            }
        }
        return cooccurrences;
    }
}
