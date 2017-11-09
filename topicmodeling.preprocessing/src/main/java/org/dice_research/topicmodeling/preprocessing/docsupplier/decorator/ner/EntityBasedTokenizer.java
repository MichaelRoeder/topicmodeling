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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;


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
