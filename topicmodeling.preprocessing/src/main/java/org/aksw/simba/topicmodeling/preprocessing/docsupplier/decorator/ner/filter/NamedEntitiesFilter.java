package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.filter;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public interface NamedEntitiesFilter {

    public boolean isNamedEntityGood(Document document, NamedEntityInText entity);
}
