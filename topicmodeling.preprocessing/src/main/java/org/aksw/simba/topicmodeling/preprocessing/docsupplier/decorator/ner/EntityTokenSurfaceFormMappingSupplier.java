package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;


/**
 * Interface implemented by a {@link NerPropagationPreprocessor} that replaces named entities with a single token. It is
 * used by the {@link EntityTokenReplaceingPostprocessor} to get a mapping from the token passed through the POS-tagging
 * to the real tokens that should be inserted.
 * 
 * @author m.roeder
 * 
 */
public interface EntityTokenSurfaceFormMappingSupplier {

    public static final int ENTITY_TOKEN_GONE_THROUGH_POS_TAGGING_ID = 0;
    public static final int CORRECT_SURFACE_FORM_ID = 1;

    /**
     * Returns the mapping created since the last call of this method.
     * 
     * @return
     */
    public EntityTermMapping getLastEntityTokenSurfaceFormMapping();

}
