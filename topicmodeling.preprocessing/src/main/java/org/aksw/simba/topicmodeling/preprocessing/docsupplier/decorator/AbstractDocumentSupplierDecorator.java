package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public abstract class AbstractDocumentSupplierDecorator implements DocumentSupplierDecorator {

    protected DocumentSupplier documentSource;

    public AbstractDocumentSupplierDecorator(DocumentSupplier documentSource) {
        this.documentSource = documentSource;
    }

    @Override
    public Document getNextDocument() {
        Document document = documentSource.getNextDocument();
        if (document != null)
        {
            document = prepareDocument(document);
        }
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

    protected abstract Document prepareDocument(Document document);
}
