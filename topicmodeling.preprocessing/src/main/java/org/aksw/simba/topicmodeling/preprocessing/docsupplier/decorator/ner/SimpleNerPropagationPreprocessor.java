package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class SimpleNerPropagationPreprocessor extends AbstractNerPropagationPreprocessor {

    @Override
    protected String processEntity(NamedEntityInText entity, String surfaceForm, Set<String> tokens) {
        return surfaceForm;
    }

}
