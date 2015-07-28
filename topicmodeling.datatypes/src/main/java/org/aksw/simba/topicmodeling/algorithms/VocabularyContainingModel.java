package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;
import org.aksw.simba.topicmodeling.utils.vocabulary.VocabularyMapping;

public interface VocabularyContainingModel extends VocabularyContaining {

    VocabularyMapping getVocabularyMapping(Vocabulary otherVocabulary);

}
