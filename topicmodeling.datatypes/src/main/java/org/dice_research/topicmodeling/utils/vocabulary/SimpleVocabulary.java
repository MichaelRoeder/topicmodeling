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
package org.dice_research.topicmodeling.utils.vocabulary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleVocabulary implements Vocabulary {

    private static final long serialVersionUID = 1647610274276283464L;

    protected Map<String, Integer> wordIndexMap;

    public SimpleVocabulary() {
        this(new HashMap<String, Integer>());
    }

    public SimpleVocabulary(Map<String, Integer> wordIndexMap) {
        synchronized (wordIndexMap) {
            this.wordIndexMap = wordIndexMap;
        }
    }

    public int size() {
        return wordIndexMap.size();
    }

    public void add(String word) {
        synchronized (wordIndexMap) {
            wordIndexMap.put(word, wordIndexMap.size());
        }
    }

    public Integer getId(String word) {
        Integer wordId = wordIndexMap.get(word);
        if (wordId == null) {
            return -1;
        }
        return wordId;
    }

    public Iterator<String> iterator() {
        return wordIndexMap.keySet().iterator();
    }

    public String getWord(int wordId) {
        Iterator<String> iterator = wordIndexMap.keySet().iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            if (getId(word) == wordId) {
                return word;
            }
        }
        return null;
    }

    @Override
    public void setWord(String word, String newWord) {
        synchronized (wordIndexMap) {
            wordIndexMap.put(newWord, wordIndexMap.get(word));
            wordIndexMap.remove(word);
        }
    }

    @Override
    public String toString() {
        if (wordIndexMap == null) {
            return "null";
        } else {
            return wordIndexMap.toString();
        }
    }
}
