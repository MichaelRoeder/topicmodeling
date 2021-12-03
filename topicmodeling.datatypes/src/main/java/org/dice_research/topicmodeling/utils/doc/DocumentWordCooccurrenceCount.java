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
package org.dice_research.topicmodeling.utils.doc;

import java.io.Serializable;
import java.util.Iterator;

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
