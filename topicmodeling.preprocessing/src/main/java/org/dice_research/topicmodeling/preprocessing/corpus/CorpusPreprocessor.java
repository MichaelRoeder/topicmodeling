package org.dice_research.topicmodeling.preprocessing.corpus;

import org.dice_research.topicmodeling.utils.corpus.Corpus;

/**
 * This preprocessor needs the complete corpus to fulfill its task.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public interface CorpusPreprocessor {

    public Corpus preprocess(Corpus corpus);
}
