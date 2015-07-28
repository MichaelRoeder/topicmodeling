package org.aksw.simba.topicmodeling.preprocessing.docsupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;

public interface DocumentSupplier {

	public Document getNextDocument();
	
	public void setDocumentStartId(int documentStartId);
}
