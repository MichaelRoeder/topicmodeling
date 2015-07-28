package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;


public interface Preprocessor {
	/**
	 * @param documentFactory
	 */
    @Deprecated
	public void addDocuments(DocumentSupplier documentFactory);

	/**
	 * @return com.unister.semweb.topic_modeling.utils.Corpus
	 */
	public Corpus getCorpus();
	
	/**
	 * Returns true if the preprocessor has an already created corpus.
	 * 
	 * @return
	 */
	public boolean hasCorpus();
	
	public void deleteCorpus();
}
