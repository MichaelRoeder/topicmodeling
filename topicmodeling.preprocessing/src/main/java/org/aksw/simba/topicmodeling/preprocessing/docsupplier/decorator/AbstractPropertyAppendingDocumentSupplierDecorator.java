package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractPropertyAppendingDocumentSupplierDecorator<T extends DocumentProperty> extends
        AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractPropertyAppendingDocumentSupplierDecorator.class);
    
    public AbstractPropertyAppendingDocumentSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        T property = createPropertyForDocument(document);
        if (property == null) {
            LOGGER.debug("Couldn't create document property for document #" + + document.getDocumentId() + ".("
                    + this.getClass().getSimpleName() + ")");
        } else {
            document.addProperty(property);
        }
        return document;
    }

    protected abstract T createPropertyForDocument(Document document);
}
