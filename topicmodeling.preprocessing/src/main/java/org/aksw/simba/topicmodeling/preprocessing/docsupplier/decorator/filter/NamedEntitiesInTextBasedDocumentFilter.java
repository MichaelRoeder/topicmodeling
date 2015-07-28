package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;


public class NamedEntitiesInTextBasedDocumentFilter implements DocumentFilter {

    @Override
    public boolean isDocumentGood(Document document) {
        NamedEntitiesInText nes = document.getProperty(NamedEntitiesInText.class);
        return (nes != null) && (nes.getNamedEntities().size() > 0);
    }

}
