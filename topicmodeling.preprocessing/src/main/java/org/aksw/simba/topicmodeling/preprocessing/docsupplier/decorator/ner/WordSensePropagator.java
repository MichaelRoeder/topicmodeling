package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class WordSensePropagator extends AbstractNerPropagationPreprocessor {

    private final String word;

    public WordSensePropagator(String word) {
        this.word = word;
    }

    @Override
    protected String processEntity(NamedEntityInText entity, String surfaceForm, Set<String> tokens) {
        String uri = entity.getNamedEntityUri();
        int pos = uri.indexOf('/');
        if (pos > 0) {
            uri = uri.substring(0, pos);
        }
        uri = uri.replaceAll("_", "");
        tokens.add(uri);
        return surfaceForm.replaceAll(word, uri);
    }

}
