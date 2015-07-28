package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;



public class SimpleDocumentPropertyStorage<T extends DocumentProperty> extends AbstractDocumentSupplierDecorator {

    private Class<T> propertyClass;
    private T property;

    public SimpleDocumentPropertyStorage(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        this.propertyClass = propertyClass;
    }

    public T getProperty() {
        return property;
    }

    @Override
    protected Document prepareDocument(Document document) {
        property = document.getProperty(propertyClass);
        return document;
    }
}
