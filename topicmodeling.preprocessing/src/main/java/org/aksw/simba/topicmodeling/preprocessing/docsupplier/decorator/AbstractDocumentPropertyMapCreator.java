package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public abstract class AbstractDocumentPropertyMapCreator<T extends DocumentProperty> extends
        AbstractDocumentSupplierDecorator {

    private final Class<T> DOCUMENT_PROPERTY_CLASS;
    private IntObjectOpenHashMap<T> properties = new IntObjectOpenHashMap<T>();

    public AbstractDocumentPropertyMapCreator(DocumentSupplier documentSource, Class<T> propertyClass) {
        super(documentSource);
        DOCUMENT_PROPERTY_CLASS = propertyClass;
    }

    @Override
    public Document getNextDocument() {
        Document document = documentSource.getNextDocument();
        if (document != null)
        {
            document = prepareDocument(document);
        } else {
            mapCreated(properties);
        }
        return document;
    }

    @Override
    protected Document prepareDocument(Document document) {
        T property = document.getProperty(DOCUMENT_PROPERTY_CLASS);
        if (property != null) {
            properties.put(document.getDocumentId(), property);
        }
        return document;
    }

    protected abstract void mapCreated(IntObjectOpenHashMap<T> properties);

}
