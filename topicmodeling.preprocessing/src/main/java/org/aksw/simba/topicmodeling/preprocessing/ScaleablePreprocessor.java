package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;


public interface ScaleablePreprocessor extends Preprocessor {

    /**
     * @return com.unister.semweb.topic_modeling.utils.Corpus
     */
    public Corpus getCorpus(int numberOfDocuments);

}
