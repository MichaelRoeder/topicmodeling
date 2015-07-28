package org.aksw.simba.topicmodeling.utils.corpus;

import org.aksw.simba.topicmodeling.algorithms.VocabularyContaining;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;


@Deprecated
public class VocabularyContainingCorpusDecorator extends AbstractCorpusDecorator implements VocabularyContaining {

    private static final long serialVersionUID = -3898615242915054721L;

    private Vocabulary vocabulary;

    public VocabularyContainingCorpusDecorator(Corpus corpus, Vocabulary vocabulary) {
        super(corpus);
        this.vocabulary = vocabulary;
    }

    @Override
    public Vocabulary getVocabulary() {
        return vocabulary;
    }
}
