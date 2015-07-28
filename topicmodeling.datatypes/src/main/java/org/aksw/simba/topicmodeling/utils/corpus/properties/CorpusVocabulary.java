package org.aksw.simba.topicmodeling.utils.corpus.properties;

import org.aksw.simba.topicmodeling.algorithms.VocabularyContaining;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;


public class CorpusVocabulary extends AbstractSimpleCorpusProperty<Vocabulary> implements VocabularyContaining {

    private static final long serialVersionUID = -7115604284003020110L;

    public CorpusVocabulary(Vocabulary value) {
        super(value);
    }

    @Override
    public Vocabulary getVocabulary() {
        return get();
    }

}
