package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.preprocessing.PreprocessorFactory;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;


public interface ModelingAlgorithm extends PreprocessorFactory {
	
	public void initialize(Corpus corpus);

	/**
	 */
	public void performNextStep();

	/**
	 * @return com.unister.semweb.topic_modeling.algorithms.Model
	 */
	public Model getModel();

}
