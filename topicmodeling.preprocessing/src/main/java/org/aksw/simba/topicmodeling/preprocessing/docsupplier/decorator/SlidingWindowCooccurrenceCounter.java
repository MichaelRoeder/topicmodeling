package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentTextWordIds;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCooccurrenceCount;
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
