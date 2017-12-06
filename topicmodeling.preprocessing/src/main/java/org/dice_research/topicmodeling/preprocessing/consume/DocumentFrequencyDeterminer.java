package org.dice_research.topicmodeling.preprocessing.consume;

import java.util.concurrent.atomic.AtomicIntegerArray;

import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentFrequencyDeterminer implements DocumentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFrequencyDeterminer.class);

    private AtomicIntegerArray counts;

    public DocumentFrequencyDeterminer(Vocabulary vocabulary) {
        this.counts = new AtomicIntegerArray(vocabulary.size());
    }

    @Override
    public void consumeDocument(Document document) {
        DocumentTextWordIds wordIds = document.getProperty(DocumentTextWordIds.class);
        if (wordIds != null) {
            int ids[] = wordIds.getWordIds();
            for (int w = 0; w < ids.length; ++w) {
                counts.incrementAndGet(ids[w]);
            }
        } else {
            LOGGER.error("Got a Document object without the needed TermTokenizedText property! Returning null.");
        }
    }
    
    public AtomicIntegerArray getCounts() {
        return counts;
    }

}
