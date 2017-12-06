package org.dice_research.topicmodeling.preprocessing.consume;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

public class DocumentFrequencyDeterminer implements DocumentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFrequencyDeterminer.class);

    private AtomicIntegerArray counts;

    public DocumentFrequencyDeterminer(Vocabulary vocabulary) {
        this.counts = new AtomicIntegerArray(vocabulary.size());
    }

    @Override
    public void consumeDocument(Document document) {
        DocumentWordCounts wordCounts = document.getProperty(DocumentWordCounts.class);
        if (wordCounts != null) {
            IntIntOpenHashMap countsMap = wordCounts.getWordCounts();
            for (int w = 0; w < countsMap.allocated.length; ++w) {
                if (countsMap.allocated[w] && (countsMap.values[w] > 0)) {
                    counts.incrementAndGet(countsMap.keys[w]);
                }
            }
        } else {
            DocumentTextWordIds wordIds = document.getProperty(DocumentTextWordIds.class);
            if (wordIds != null) {
                BitSet seenWords = new BitSet();
                int ids[] = wordIds.getWordIds();
                for (int w = 0; w < ids.length; ++w) {
                    if(!seenWords.get(ids[w])) {
                        counts.incrementAndGet(ids[w]);
                        seenWords.set(ids[w]);
                    }
                }
            } else {
                LOGGER.error(
                        "Got a Document object without the needed DocumentWordCounts or DocumentTextWordIds property! Returning null.");
            }
        }
    }

    public AtomicIntegerArray getCounts() {
        return counts;
    }

}
