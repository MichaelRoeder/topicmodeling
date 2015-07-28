package org.aksw.simba.topicmodeling.utils.doc;

import java.io.Serializable;
import java.util.Iterator;

import org.aksw.simba.topicmodeling.utils.doc.AbstractDocumentProperty;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;

public class DocumentWordCooccurrenceCount extends AbstractDocumentProperty
        implements
        Iterable<ObjectIntCursor<DocumentWordCooccurrenceCount.WordCooccurrenceDef>> {

    private static final long serialVersionUID = -7776756413671702108L;

    private ObjectIntOpenHashMap<WordCooccurrenceDef> cooccurrenceMap = new ObjectIntOpenHashMap<WordCooccurrenceDef>();

    public int size() {
        return cooccurrenceMap.size();
    }

    @Override
    public Object getValue() {
        return cooccurrenceMap;
    }

    public int getCooccurrenceCount(int wordId1, int wordId2) {
        WordCooccurrenceDef def = new WordCooccurrenceDef(wordId1, wordId2);
        if (cooccurrenceMap.containsKey(def)) {
            return cooccurrenceMap.get(def);
        } else {
            return 0;
        }
    }

    public void increaseCooccurrenceCount(int wordId1, int wordId2, int value) {
        WordCooccurrenceDef def = new WordCooccurrenceDef(wordId1, wordId2);
        cooccurrenceMap.putOrAdd(def, value, value);
    }

    @Override
    public Iterator<ObjectIntCursor<DocumentWordCooccurrenceCount.WordCooccurrenceDef>> iterator() {
        return cooccurrenceMap.iterator();
    }

    public static class WordCooccurrenceDef implements Comparable<WordCooccurrenceDef>, Serializable {

        private static final long serialVersionUID = 3184064067465284127L;

        public int wordId1;
        public int wordId2;

        public WordCooccurrenceDef(int wordId1, int wordId2) {
            if (wordId1 <= wordId2) {
                this.wordId1 = wordId1;
                this.wordId2 = wordId2;
            } else {
                this.wordId1 = wordId2;
                this.wordId2 = wordId1;
            }
        }

        @Override
        public int compareTo(WordCooccurrenceDef def) {
            if (def == null) {
                throw new NullPointerException("Got null as WordCooccurrenceDef object.");
            }
            if (this.wordId1 < def.wordId1) {
                return -1;
            } else if (this.wordId1 > def.wordId1) {
                return 1;
            } else if (this.wordId2 < def.wordId2) {
                return -1;
            } else if (this.wordId2 > def.wordId2) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof WordCooccurrenceDef) {
                return this.compareTo((WordCooccurrenceDef) obj) == 0;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return (wordId1 >> 16) ^ wordId2;
        }

        @Override
        public String toString() {
            return "[" + wordId1 + ", " + wordId2 + "]";
        }
    }
}
