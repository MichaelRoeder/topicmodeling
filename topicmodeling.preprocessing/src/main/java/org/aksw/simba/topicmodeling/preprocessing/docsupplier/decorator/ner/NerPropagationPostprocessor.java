package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;


public interface NerPropagationPostprocessor {

    public NamedEntitiesInTokenizedText postprocessNamedEntities(NamedEntitiesInText nes, DocumentText text,
            TermTokenizedText tttext);
}
