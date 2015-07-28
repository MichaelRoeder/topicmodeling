package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.apache.commons.lang3.ArrayUtils;


public class EntityTokenReplaceingPostprocessor extends SimpleNerPropagationPostprocessor {

    protected EntityTokenSurfaceFormMappingSupplier preprocessor;
    protected Map<NamedEntityInText, Term[]> localEntityTokenMapping = new HashMap<NamedEntityInText, Term[]>();
    protected EntityTermMapping preprocEntityTokenMapping;

    public EntityTokenReplaceingPostprocessor(EntityTokenSurfaceFormMappingSupplier preprocessor) {
        this.preprocessor = preprocessor;
    }

    @Override
    public NamedEntitiesInTokenizedText postprocessNamedEntities(NamedEntitiesInText nes, DocumentText text,
            TermTokenizedText tttext) {
        preprocEntityTokenMapping = preprocessor.getLastEntityTokenSurfaceFormMapping();
        localEntityTokenMapping.clear();
        NamedEntitiesInTokenizedText neInTTT = super.postprocessNamedEntities(nes, text, tttext);

        // Go through the list of named entities (sorted ascending!) and replace
        // the tokens of the nes with the ones
        // given by the preprocessor
        List<NamedEntityInText> nesList = neInTTT.getNamedEntities();
        if ((nesList.size() > 0) && (localEntityTokenMapping.size() > 0)) {
            NamedEntityInText nesArray[] = nesList.toArray(new NamedEntityInText[nesList.size()]);
            Arrays.sort(nesArray);
            ArrayUtils.reverse(nesArray);

            List<Term> tokens = tttext.getTermTokenizedText();
            List<Term> newTokens = new ArrayList<Term>(tokens.size());
            Term neTokens[];
            NamedEntityInText ne;
            int newStartPos, oldLength, neInTokens, neId = 0, posInTokens = 0;
            while (neId < nesArray.length) {
                ne = nesArray[neId];
                neInTokens = ne.getStartPos();
                while (posInTokens < neInTokens) {
                    newTokens.add(tokens.get(posInTokens));
                    ++posInTokens;
                }
                newStartPos = newTokens.size();
                oldLength = ne.getLength();
                // Check whether this ne has to be replaced
                if (localEntityTokenMapping.containsKey(nesArray[neId])) {
                    neTokens = localEntityTokenMapping.get(ne);
                    // insert the new tokens for the ne
                    for (int i = 0; i < neTokens.length; ++i) {
                        newTokens.add(neTokens[i]);
                    }
                    // set the new length of the ne
                    ne.setLength(neTokens.length);
                }
                // set the new position of the ne
                ne.setStartPos(newStartPos);
                posInTokens += oldLength;

                ++neId;
            }
            // Insert the rest of tokens
            while (posInTokens < tokens.size()) {
                newTokens.add(tokens.get(posInTokens));
                ++posInTokens;
            }
            // Set the new term tokenized text
            tttext.setTermTokenizedText(newTokens);
        }
        return neInTTT;
    }

    @Override
    protected void foundEntityPair(NamedEntityInText entity, NamedEntityInText entityInTokens) {
        List<NamedEntityInText> entities = preprocEntityTokenMapping.entities;
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).equals(entity)) {
                localEntityTokenMapping.put(entityInTokens, preprocEntityTokenMapping.terms.get(i));
                return;
            }
        }
    }
}
