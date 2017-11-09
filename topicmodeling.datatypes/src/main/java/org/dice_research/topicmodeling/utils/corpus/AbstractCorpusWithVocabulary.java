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
package org.dice_research.topicmodeling.utils.corpus;

import org.dice_research.topicmodeling.algorithms.VocabularyContaining;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;


@Deprecated
public abstract class AbstractCorpusWithVocabulary extends AbstractCorpus implements VocabularyContaining {

    private static final long serialVersionUID = -2333098963261806887L;

    protected Vocabulary vocabulary;

    public AbstractCorpusWithVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public int getNumberOfWords() {
        return vocabulary.size();
    }

    public void mergeVocabularyWithCorpus(
            AbstractCorpusWithVocabulary otherCorpus)
    {
        // TODO
    }
}
