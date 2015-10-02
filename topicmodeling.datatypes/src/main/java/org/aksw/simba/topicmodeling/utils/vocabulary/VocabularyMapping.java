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
package org.aksw.simba.topicmodeling.utils.vocabulary;

import com.carrotsearch.hppc.IntIntOpenHashMap;

public class VocabularyMapping {

    public static final int NO_MAPPING = -1;

    private Vocabulary sourceVocabulary;
    private Vocabulary destVocabulary;
    private IntIntOpenHashMap mapping;

    public static VocabularyMapping createMapping(Vocabulary sourceVocabulary, Vocabulary destVocabulary) {
        return new VocabularyMapping(sourceVocabulary, destVocabulary, createMappingHashMap(sourceVocabulary,
                destVocabulary));
    }

    protected static IntIntOpenHashMap createMappingHashMap(Vocabulary source, Vocabulary dest) {
        IntIntOpenHashMap mapping = new IntIntOpenHashMap(source.size());
        String word;
        int destId;
        for (int w = 0; w < source.size(); ++w) {
            word = source.getWord(w);
            destId = dest.getId(word);
            if (destId < 0) {
                destId = NO_MAPPING;
            }
            mapping.put(w, destId);
        }
        return mapping;
    }

    private VocabularyMapping(Vocabulary sourceVocabulary, Vocabulary destVocabulary, IntIntOpenHashMap mapping) {
        this.sourceVocabulary = sourceVocabulary;
        this.destVocabulary = destVocabulary;
        this.mapping = mapping;
    }

    public int getMappedWordId(int wordIdInSourceVocab) {
        return getMappedWordId(wordIdInSourceVocab, false);
    }

    public int getMappedWordId(int wordId, boolean addIfNoMapping) {
        int newWordId = NO_MAPPING;
        if (wordId >= 0) {
            if (mapping.containsKey(wordId)) {
                newWordId = mapping.get(wordId);
            }
            else {
                System.out.println("STOP");
            }
            if ((newWordId == NO_MAPPING) && (addIfNoMapping)) {
                String word = sourceVocabulary.getWord(wordId);
                destVocabulary.add(word);
                newWordId = destVocabulary.getId(word);
                mapping.put(wordId, newWordId);
            }
        }
        return newWordId;
    }

    public Vocabulary getSourceVocabulary() {
        return sourceVocabulary;
    }

    public Vocabulary getDestVocabulary() {
        return destVocabulary;
    }
}
