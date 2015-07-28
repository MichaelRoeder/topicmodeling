package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;


public abstract class AbstractDocumentSupplierDecoratorTest implements DocumentSupplier {

    private Document document;

    public AbstractDocumentSupplierDecoratorTest(DocumentProperty... properties) {
        document = new Document(0, properties);
    }

    @Override
    public Document getNextDocument() {
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        /* nothing to do */
    }
}
