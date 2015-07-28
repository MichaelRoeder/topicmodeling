package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.AbstractCorpusDecorator;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CorpusWrappingPreprocessor extends AbstractCorpusDecorator implements Preprocessor {
	
	private static final long serialVersionUID = 7173055356063738593L;
	
	private static final Logger logger = LoggerFactory.getLogger(CorpusWrappingPreprocessor.class);
	
	public CorpusWrappingPreprocessor(Corpus corpus) {
		super(corpus);
	}

	@Override
	public void addDocuments(DocumentSupplier documentFactory) {
		logger.info("addDocuments was called. But my corpus is fixed and I can not change it.");
	}

    @Override
    public boolean hasCorpus() {
        return corpus != null;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
    }
}
