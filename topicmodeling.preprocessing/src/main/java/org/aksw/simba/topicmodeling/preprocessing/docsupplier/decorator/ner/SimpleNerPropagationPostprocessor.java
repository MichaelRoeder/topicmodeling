package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleNerPropagationPostprocessor implements NerPropagationPostprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNerPropagationPostprocessor.class);

    @Override
    public NamedEntitiesInTokenizedText postprocessNamedEntities(NamedEntitiesInText nes, DocumentText text,
            TermTokenizedText tttext) {
        List<NamedEntityInText> entities = nes.getNamedEntities();
        if (entities.size() == 0) {
            return new NamedEntitiesInTokenizedText();
        }
        List<NamedEntityInText> entitiesInTT = new ArrayList<NamedEntityInText>(entities.size());
        String originalText = text.getText();
        List<Term> terms = tttext.getTermTokenizedText();

        Term term;
        int termId = 0;
        String label;
        int currentPos = 0, searchedTermPos, entityFirstTermId = -1;
        int currentEntityId = entities.size() - 1;
        NamedEntityInText entity = entities.get(currentEntityId);
        NamedEntityInText entityInTokens;
        int entityStartPos = entity.getStartPos(), entityEndPos = entity.getEndPos();
        while ((termId < terms.size()) && (entity != null)) {
            term = terms.get(termId);
            label = term.getWordForm();
            searchedTermPos = originalText.indexOf(label, currentPos);

            // if the current term could be found
            if (searchedTermPos >= 0) {
                currentPos = searchedTermPos;
                // if this entity has not been seen until now and can't be found
                // anymore
                while ((entityFirstTermId < 0) && (currentPos > entityEndPos)) {
                    // LOGGER.warn("Couldn't find tokenized entity! entitiy=" +
                    // entity.toString());
                    --currentEntityId;
                    if (currentEntityId < 0) {
                        entity = null;
                        entityStartPos = originalText.length();
                        entityEndPos = originalText.length();
                    } else {
                        entity = entities.get(currentEntityId);
                        entityStartPos = entity.getStartPos();
                        entityEndPos = entity.getEndPos();
                    }
                }

                // the entity has already started with a term before
                if (entityFirstTermId >= 0) {
                    if (currentPos > entityEndPos) {
                        // store entity
                        entityInTokens = new NamedEntityInText(entity, entityFirstTermId, termId - entityFirstTermId);
                        entitiesInTT.add(entityInTokens);
                        foundEntityPair(entity, entityInTokens);
                        entityFirstTermId = -1;
                        --currentEntityId;
                        if (currentEntityId < 0) {
                            entity = null;
                            entityStartPos = originalText.length();
                            entityEndPos = originalText.length();
                        } else {
                            entity = entities.get(currentEntityId);
                            entityStartPos = entity.getStartPos();
                            entityEndPos = entity.getEndPos();
                        }
                    }
                }
                // if a new entity starts with this term
                if ((entityFirstTermId < 0) && (currentPos >= entityStartPos)) {
                    entityFirstTermId = termId;
                }
                currentPos += label.length();
            } else {
                LOGGER.warn("Couldn't find the term \"" + label + "\" in the document text. Ignoring this.");
            }
            ++termId;
        }
        // if there is still an entity which has started before the end of the
        // terms
        if (entityFirstTermId >= 0) {
            // store entity
            entityInTokens = new NamedEntityInText(entity, entityFirstTermId, terms.size() - entityFirstTermId);
            entitiesInTT.add(entityInTokens);
            foundEntityPair(entity, entityInTokens);
        }

        Collections.reverse(entitiesInTT);
        return new NamedEntitiesInTokenizedText(entitiesInTT);
    }

    protected void foundEntityPair(NamedEntityInText entity, NamedEntityInText entityInTokens) {
        // nothing to do
    }
}
