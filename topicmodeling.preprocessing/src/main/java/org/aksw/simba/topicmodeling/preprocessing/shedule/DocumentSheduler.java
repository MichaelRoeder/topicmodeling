package org.aksw.simba.topicmodeling.preprocessing.shedule;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;

public interface DocumentSheduler {
	
	public DocumentSupplier getPartialDocumentSupplier(int partId);
	
	public int getNumberOfParts();
}
