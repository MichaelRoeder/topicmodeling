package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter.DocumentFilter;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class DocumentFilteringSupplierDecorator implements DocumentSupplierDecorator {

    protected DocumentSupplier documentSource;
    private DocumentFilter filter;

    public DocumentFilteringSupplierDecorator(DocumentSupplier documentSource, DocumentFilter filter) {
        this.documentSource = documentSource;
        this.filter = filter;
    }

    @Override
    public Document getNextDocument() {
        Document document = null;
        do {
            document = documentSource.getNextDocument();
        } while ((document != null) && (!filter.isDocumentGood(document)));
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        documentSource.setDocumentStartId(documentStartId);
    }

    @Override
    public DocumentSupplier getDecoratedDocumentSupplier() {
        return documentSource;
    }

    @Override
    public void setDecoratedDocumentSupplier(DocumentSupplier supplier) {
        this.documentSource = supplier;
    }
}
