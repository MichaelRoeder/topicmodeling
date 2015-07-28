package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentClassificationResult;

public interface ClassificationModel extends Model {

	public DocumentClassificationResult getClassificationForDocument(Document document);
}
