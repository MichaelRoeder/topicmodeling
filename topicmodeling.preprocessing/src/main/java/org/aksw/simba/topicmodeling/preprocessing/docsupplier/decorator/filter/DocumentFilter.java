package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.aksw.simba.topicmodeling.utils.doc.Document;

public interface DocumentFilter {

    public boolean isDocumentGood(Document document);
}
