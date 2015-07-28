package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class DocumentConsumerAdaptingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private DocumentConsumer consumer;

    public DocumentConsumerAdaptingSupplierDecorator(DocumentSupplier documentSource, DocumentConsumer consumer) {
        super(documentSource);
        this.consumer = consumer;
    }

    @Override
    protected Document prepareDocument(Document document) {
        if (document != null) {
            consumer.consumeDocument(document);
        }
        return document;
    }

}
