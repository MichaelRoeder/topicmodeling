package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;


public class PropertyRemovingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private ArrayList<Class<? extends DocumentProperty>> removableProperties;

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>();
    }

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource,
            Class<? extends DocumentProperty> removableDocumentProperty) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>();
        removableProperties.add(removableDocumentProperty);
    }

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource,
            Collection<? extends Class<? extends DocumentProperty>> removableDocumentProperties) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>(removableDocumentProperties);
    }

    @Override
    public Document prepareDocument(Document document) {
        for (Class<? extends DocumentProperty> docPropClass : removableProperties) {
            document.removeProperty(docPropClass);
        }
        return document;
    }

    public ArrayList<Class<? extends DocumentProperty>> getRemovableProperties() {
        return removableProperties;
    }

    public void setRemovableProperties(
            ArrayList<Class<? extends DocumentProperty>> removableProperties) {
        this.removableProperties = removableProperties;
    }

    public void addRemovableProperty(
            Class<? extends DocumentProperty> documentPropertyClass) {
        removableProperties.add(documentPropertyClass);
    }

}
