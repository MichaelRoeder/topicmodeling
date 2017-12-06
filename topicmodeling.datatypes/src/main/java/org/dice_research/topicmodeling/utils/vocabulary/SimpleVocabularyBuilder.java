package org.dice_research.topicmodeling.utils.vocabulary;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder class for a {@link SimpleVocabulary} offering more methods to
 * manipulate the vocabulary.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class SimpleVocabularyBuilder implements VocabularyFactory {

    protected int nextId = 0;
    protected BitSet usedIds = new BitSet();
    protected Map<String, Integer> wordIndexMap = new HashMap<>();
    protected Map<Integer, String> indexWordMap = new HashMap<>();

    public SimpleVocabularyBuilder() {
    }

    public SimpleVocabularyBuilder(Vocabulary source) {
        for (String word : source) {
            set_unsecured(word, source.getId(word));
        }
    }

    private void set_unsecured(String word, int id) {
        wordIndexMap.put(word, id);
        indexWordMap.put(id, word);
        usedIds.set(id);
    }

    public synchronized int remove(String word) {
        Integer id = wordIndexMap.remove(word);
        if (id != null) {
            indexWordMap.remove(id);
            usedIds.clear(id);
            updateNextId();
            return id;
        } else {
            return -1;
        }
    }

    public synchronized String remove(int id) {
        String word = indexWordMap.remove(id);
        if (word != null) {
            wordIndexMap.remove(word);
            usedIds.clear(id);
            updateNextId();
        }
        return word;
    }

    public synchronized int addWord(String word) {
        // If the map already contains the word
        if (wordIndexMap.containsKey(word)) {
            // Return the index that it already has
            return wordIndexMap.get(word);
        } else {
            int id = usedIds.nextClearBit(0);
            set_unsecured(word, id);
            updateNextId();
            return id;
        }
    }

    public synchronized void setWord(String word, int id) {
        // make sure that the word and id are free;
        remove(word);
        remove(id);
        set_unsecured(word, id);
    }

    private void updateNextId() {
        nextId = usedIds.previousSetBit(nextId) + 1;
    }

    public Integer getId(String word) {
        if (wordIndexMap.containsKey(word)) {
            Integer wordId = wordIndexMap.get(word);
            if (wordId == null) {
                return -1;
            } else {
                return wordId;
            }
        } else {
            return -1;
        }
    }

    public String getWord(int wordId) {
        if (indexWordMap.containsKey(wordId)) {
            return indexWordMap.get(wordId);
        } else {
            return null;
        }
    }

    /**
     * @throws IllegalStateException
     *             if the vocabulary has not been build correctly.
     */
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
