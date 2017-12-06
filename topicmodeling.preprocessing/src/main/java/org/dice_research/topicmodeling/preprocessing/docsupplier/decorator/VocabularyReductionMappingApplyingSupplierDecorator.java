package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.Arrays;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;

import com.carrotsearch.hppc.BitSet;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;

public class VocabularyReductionMappingApplyingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    public static final int REMOVED_WORD = -1;

    public static int[] createMapping(Vocabulary vocabulary, BitSet keptWords) {
        int[] mapping = new int[vocabulary.size()];
        Arrays.fill(mapping, REMOVED_WORD);
        // Get the last word that we would like to keep
        int lastPos = lastSetBit(keptWords, mapping.length);
        for (int i = 0; i < mapping.length; ++i) {
            if (keptWords.get(i)) {
                // make sure that this word has not been moved to a different position
                if (mapping[i] == REMOVED_WORD) {
                    // Set it to its own id since we would like to keep it there
                    mapping[i] = i;
                }
            } else {
                // This position is free! Let's check whether we can move another word to this
                // position
                if (lastPos > i) {
                    // Let the last position point to this position
                    mapping[lastPos] = i;
                    // Search for the new last position
                    lastPos = lastSetBit(keptWords, lastPos);
                }
            }
        }
        return mapping;
    }

    public static int[] updateVocabulary(Vocabulary vocabulary, int[] mapping) {
        vocabulary.setWord(word, newWord);
        int[] mapping = new int[vocabulary.size()];
        Arrays.fill(mapping, REMOVED_WORD);
        // Get the last word that we would like to keep
        int lastPos = lastSetBit(keptWords, mapping.length);
        for (int i = 0; i < mapping.length; ++i) {
            if (keptWords.get(i)) {
                // make sure that this word has not been moved to a different position
                if (mapping[i] == REMOVED_WORD) {
                    // Set it to its own id since we would like to keep it there
                    mapping[i] = i;
                }
            } else {
                // This position is free! Let's check whether we can move another word to this
                // position
                if (lastPos > i) {
                    // Let the last position point to this position
                    mapping[lastPos] = i;
                    // Search for the new last position
                    lastPos = lastSetBit(keptWords, lastPos);
                }
            }
        }
        return mapping;
    }

    /**
     * Returns the last {@code 1} bit in the given bit set before the given position
     * (excluding) or {@code -1} if there is no such bit.
     * 
     * @param bits
     *            the bit set in which should be searched
     * @param from
     *            the position (excluding) from which the algorithm should start to
     *            search backwards
     * @return the position of the bit or {@code -1} if there is no such bit
     */
    protected static int lastSetBit(BitSet bits, int from) {
        --from;
        while (from > -1) {
            if (bits.get(from)) {
                return from;
            }
            --from;
        }
        return from;
    }

    private int[] mapping;

    public VocabularyReductionMappingApplyingSupplierDecorator(DocumentSupplier documentSource, int mapping[]) {
        super(documentSource);
        this.mapping = mapping;
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentTextWordIds wordIds = document.getProperty(DocumentTextWordIds.class);
        if (wordIds != null) {
            document.addProperty(updateWordIds(wordIds));
        }
        DocumentWordCounts counts = document.getProperty(DocumentWordCounts.class);
        if (counts != null) {
            document.addProperty(updateWordCounts(counts));
        }
        return document;
    }

    protected DocumentTextWordIds updateWordIds(DocumentTextWordIds wordIds) {
        int[] ids = wordIds.getWordIds();
        IntArrayList newIds = new IntArrayList(ids.length);
        int newId;
        for (int i = 0; i < ids.length; ++i) {
            newId = mapping[ids[i]];
            if (newId != REMOVED_WORD) {
                newIds.add(newId);
            }
        }
        return new DocumentTextWordIds(newIds.toArray());
    }

    protected DocumentWordCounts updateWordCounts(DocumentWordCounts counts) {
        IntIntOpenHashMap map = counts.getWordCounts();
        for (int i = 0; i < mapping.length; ++i) {
            // If the word has been removed
            if (mapping[i] == REMOVED_WORD) {
                map.remove(i);
            } else if (mapping[i] != i) {
                // if the word has been moved, move it in the map
                map.put(mapping[i], map.remove(i));
            }
        }
        return counts;
    }
}
