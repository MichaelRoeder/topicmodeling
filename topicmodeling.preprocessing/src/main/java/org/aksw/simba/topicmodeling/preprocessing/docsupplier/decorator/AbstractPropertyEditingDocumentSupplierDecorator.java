package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPropertyEditingDocumentSupplierDecorator<T extends DocumentProperty>
        extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractPropertyAppendingDocumentSupplierDecorator.class);

    private Class<T> propertyClass;

    public AbstractPropertyEditingDocumentSupplierDecorator(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        this.propertyClass = propertyClass;
    }

    @Override
    protected Document prepareDocument(Document document) {
        T property = document.getProperty(propertyClass);
        if (property == null) {
            LOGGER.info("Couldn't get property " + propertyClass.getSimpleName() + " for document #"
                    + document.getDocumentId() + ".(" + this.getClass().getSimpleName() + ")");
        } else {
            editDocumentProperty(property);
        }
        return document;
    }

    protected abstract void editDocumentProperty(T property);

}
