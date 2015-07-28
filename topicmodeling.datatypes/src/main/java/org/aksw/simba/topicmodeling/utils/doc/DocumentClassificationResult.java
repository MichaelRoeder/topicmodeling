package org.aksw.simba.topicmodeling.utils.doc;

import org.aksw.simba.topicmodeling.algorithms.Model;

public interface DocumentClassificationResult extends DocumentProperty {

	public int getClassId();
	
	/**
	 * 
	 * @return the model used for classification
	 */
	public Model getModel();
	
	/**
	 * 
	 * @return the version the Model had at the moment the document was classified
	 */
	public int getModelVersion();
}
