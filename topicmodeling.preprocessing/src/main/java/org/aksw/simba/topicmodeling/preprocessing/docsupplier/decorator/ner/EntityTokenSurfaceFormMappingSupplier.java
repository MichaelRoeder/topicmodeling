/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
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
