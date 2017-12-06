package org.dice_research.topicmodeling.utils.vocabulary;

import java.util.HashMap;
import java.util.Map;

import com.carrotsearch.hppc.BitSet;

public class SimpleVocabularyBuilder implements VocabularyFactory {

    protected int nextId = 0;
    protected BitSet usedIds = new BitSet();
    protected Map<String, Integer> wordIndexMap = new HashMap<>();
    protected Map<Integer, String> indexWordMap = new HashMap<>();

    public SimpleVocabularyBuilder() {
    }

    public SimpleVocabularyBuilder(Vocabulary source) {
        // TODO
    }

    private void set_unsecured(String word, int id) {
        wordIndexMap.put(word, id);
        indexWordMap.put(id, word);
        usedIds.set(id);
    }

    public synchronized int addWord(String word) {
        // If the map already contains the word
        if (wordIndexMap.containsKey(word)) {
            // Return the index that it already has
            return wordIndexMap.get(word);
        } else {
            if (usedIds.cardinality() == nextId) {
                set_unsecured(word, nextId);
                return nextId++;
            } else {
                // this is not very performant :(
                BitSet freeIds = (BitSet) usedIds.clone();
                int id = freeIds.nextSetBit(0);
                if (id == -1) {
                    throw new IllegalStateException(
                            "There should be a free id but I couldn't find it. It seems to be a programming error.");
                } else {
                    set_unsecured(word, id);
                    return id;
                }
            }
        }
    }

    @Override
    public Vocabulary getVocabulary() {
        if (usedIds.cardinality() == nextId) {
            return new SimpleVocabulary(wordIndexMap);
        } else {
            throw new IllegalStateException("There are " + (nextId - usedIds.cardinality())
                    + " empty slots in this vocabulary! It won't be build.");
        }
    }

}
