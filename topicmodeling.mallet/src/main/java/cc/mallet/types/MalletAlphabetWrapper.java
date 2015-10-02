/**
 * This file is part of topicmodeling.mallet.
 *
 * topicmodeling.mallet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.mallet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.mallet.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.mallet.types;

import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;

@SuppressWarnings("unchecked")
public class MalletAlphabetWrapper extends Alphabet implements Vocabulary {

    private static final long serialVersionUID = -7355254832346011464L;

    public MalletAlphabetWrapper(Alphabet alphabet) {
        this.map = alphabet.map;
        this.entries = alphabet.entries;
        this.growthStopped = alphabet.growthStopped;
        this.entryClass = alphabet.entryClass;
    }

    @Override
    public void add(String word) {
        lookupIndex(word, true);
    }

    @Override
    public Integer getId(String word) {
        return lookupIndex(word, false);
    }

    public Alphabet getAlphabet() {
        return this;
    }

    @Override
    public String getWord(int wordId) {
        return (String) lookupObject(wordId);
    }

    public static MalletAlphabetWrapper createFromVocabulary(Vocabulary vocabulary) {
        String wordArray[] = new String[vocabulary.size()];
        for (int w = 0; w < wordArray.length; w++) {
            wordArray[w] = vocabulary.getWord(w);
        }

        Alphabet alphabet = new Alphabet(wordArray);
        return new MalletAlphabetWrapper(alphabet);
    }

    @Override
    public void setWord(String word, String newWord) {
        int wordId = getId(word);
        if (wordId != -1) {
            map.put(newWord, wordId);
            map.remove(word);
        }
    }
}
