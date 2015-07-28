package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;

public interface ProbabilisticWordTopicModel extends VocabularyContainingClassificationModel {

	public double getSmoothedProbabilityOfWord(int wordId, int topicId);
	
	public double getProbabilityOfWord(int wordId, int topicId);
	
	public double getSmoothedProbabilityOfTopic(int topicId);
	
	public int getNumberOfTopics();
	
	public double[] getTopicProbabilitiesForDocument(DocumentWordCounts wordCounts);
//	public double[] getTopicProbabilitiesForDocument_test(Vector<Integer> wordCounts);
}
