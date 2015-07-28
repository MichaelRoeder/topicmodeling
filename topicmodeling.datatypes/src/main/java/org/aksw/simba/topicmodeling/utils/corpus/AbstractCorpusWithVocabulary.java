package org.aksw.simba.topicmodeling.utils.corpus;

import org.aksw.simba.topicmodeling.algorithms.VocabularyContaining;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;


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
