package org.aksw.simba.topicmodeling.io;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;

public interface CorpusReader extends Preprocessor {

    public void readCorpus();
}
