package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class EntityPropagator extends EntityBasedTokenizer {

    @Override
    protected Term[] getTokensAfterPosTagging(NamedEntityInText entity, String surfaceForm) {
        // Create the single token that should be used after pos-tagging (it is allowed to contain spaces)
        Term term = new Term(entity.getNamedEntityUri());
        term.properties.setNoun(true);
        return new Term[] { term };
    }

}
