package org.aksw.simba.topicmodeling.algorithms;

import java.io.Serializable;

public interface ProbTopicModelingAlgorithmStateSupplier extends VocabularyContaining, Serializable {

    public int[] getWordTopicAssignmentForDocument(int documentId);

    public int[] getWordsOfDocument(int documentId);

    public WordCounter getWordCounts();

    public int getNumberOfTopics();

    public int getNumberOfDocuments();

    public int getNumberOfWords();

    public long getSeed();
}
