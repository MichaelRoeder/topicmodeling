package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public class EntityBasedTokenizer extends AbstractNerPropagationPreprocessor implements
        EntityTokenSurfaceFormMappingSupplier {

    private static final String CHARS_TO_REPLACE = "[ \n\r\t\\.'" + ((char) 0xA0) + "\\(\\)\\[\\]\\{\\}]";
    private static final String CHARS_TO_INSERT = "";

    protected EntityTermMapping surfaceFormsMapping = new EntityTermMapping();

    @Override
    protected String processEntity(NamedEntityInText entity, String surfaceForm, Set<String> tokens) {
        String newSurfaceForm = surfaceForm.replaceAll(CHARS_TO_REPLACE, CHARS_TO_INSERT);
        tokens.add(newSurfaceForm);
        surfaceFormsMapping.entities.add(entity);
        surfaceFormsMapping.terms.add(getTokensAfterPosTagging(entity, surfaceForm));
        return newSurfaceForm;
    }

    protected Term[] getTokensAfterPosTagging(NamedEntityInText entity, String surfaceForm) {
        // Create the single token that should be used after pos-tagging (it is allowed to contain spaces)
        Term term = new Term(surfaceForm);
        term.properties.setNoun(true);
        return new Term[] { term };
    }

    @Override
    public EntityTermMapping getLastEntityTokenSurfaceFormMapping() {
        EntityTermMapping result = surfaceFormsMapping;
        surfaceFormsMapping = new EntityTermMapping();
        return result;
    }

}
