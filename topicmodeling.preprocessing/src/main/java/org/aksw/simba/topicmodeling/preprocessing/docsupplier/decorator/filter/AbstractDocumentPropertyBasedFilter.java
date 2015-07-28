package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;

public abstract class AbstractDocumentPropertyBasedFilter<T extends DocumentProperty> implements DocumentFilter {

    private final Class<T> DOCUMENT_PROPERTY_CLASS;
    private final boolean ACCEPT_DOCUMENT_WITHOUT_PROPERTY;

    public AbstractDocumentPropertyBasedFilter(Class<T> propertyClass) {
        DOCUMENT_PROPERTY_CLASS = propertyClass;
        ACCEPT_DOCUMENT_WITHOUT_PROPERTY = false;
    }

    public AbstractDocumentPropertyBasedFilter(Class<T> propertyClass, boolean acceptDocumentWithoutProperty) {
        DOCUMENT_PROPERTY_CLASS = propertyClass;
        ACCEPT_DOCUMENT_WITHOUT_PROPERTY = acceptDocumentWithoutProperty;
    }

    @Override
    public boolean isDocumentGood(Document document) {
        T property = document.getProperty(DOCUMENT_PROPERTY_CLASS);
        if (property == null) {
            return ACCEPT_DOCUMENT_WITHOUT_PROPERTY;
        } else {
            return isDocumentPropertyGood(property);
        }
    }

    protected abstract boolean isDocumentPropertyGood(T property);
}
