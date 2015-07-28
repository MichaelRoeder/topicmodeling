package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.filter;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class NamedEntityFilteringSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private NamedEntitiesFilter filter;
    private boolean discardDocumentsWithoutNEs;

    public NamedEntityFilteringSupplierDecorator(DocumentSupplier documentSource, NamedEntitiesFilter filter) {
        this(documentSource, filter, false);
    }

    public NamedEntityFilteringSupplierDecorator(DocumentSupplier documentSource, NamedEntitiesFilter filter,
            boolean discardDocumentsWithoutNEs) {
        super(documentSource);
        this.filter = filter;
        this.discardDocumentsWithoutNEs = discardDocumentsWithoutNEs;
    }

    @Override
    protected Document prepareDocument(Document document) {
        NamedEntitiesInText entities = document.getProperty(NamedEntitiesInText.class);
        if (entities == null) {
            if (discardDocumentsWithoutNEs) {
                document = this.getNextDocument();
            }
        } else {
            entities = filterEntities(entities, document);
            document.addProperty(entities);
        }
        return document;
    }

    private NamedEntitiesInText filterEntities(NamedEntitiesInText entities, Document document) {
        if (entities.getNamedEntities().size() == 0) {
            return entities;
        }
        List<NamedEntityInText> filteredEntities = new ArrayList<NamedEntityInText>(entities.getNamedEntities().size());
        for (NamedEntityInText entity : entities) {
            if (filter.isNamedEntityGood(document, entity)) {
                filteredEntities.add(entity);
            }
        }
        return new NamedEntitiesInText(filteredEntities);
    }
}
