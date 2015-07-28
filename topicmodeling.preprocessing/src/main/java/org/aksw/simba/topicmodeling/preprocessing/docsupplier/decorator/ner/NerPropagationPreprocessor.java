package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;


public interface NerPropagationPreprocessor {

    public Set<String> preprocessNamedEntities(DocumentText text, NamedEntitiesInText nes);
}
