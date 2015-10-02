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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;


public abstract class AbstractNerPropagationPreprocessor implements NerPropagationPreprocessor {

    private static final String SPACE = " ";

    @Override
    public Set<String> preprocessNamedEntities(DocumentText text, NamedEntitiesInText nes) {
        Set<String> tokens = new HashSet<String>();
        List<String> textParts = new ArrayList<String>();
        List<NamedEntityInText> entities = nes.getNamedEntities();
        Collections.sort(entities);
        String originalText = text.getText();
        // start with the last entity and add the parts of the new text beginning
        // with its end to the array
        // Note that we are expecting that the entities are sorted descending by
        // there position in the text!
        int startFormerLabel = originalText.length();
        for (NamedEntityInText currentNE : entities) {
            // proof if this entity undercuts the last one.
            if (startFormerLabel >= currentNE.getEndPos()) {
                // append the text between this entity and the former one
                textParts.add(trimText(originalText.substring(currentNE.getEndPos(),
                        startFormerLabel)));
                // append a space to separate the entity from the text
                textParts.add(SPACE);
                // append the entity
                textParts.add(processEntity(currentNE, originalText.substring(currentNE.getStartPos(),
                        currentNE.getEndPos()), tokens));
                // append a space to separate the entity from the text
                textParts.add(SPACE);
                // remember the start position of this label
                startFormerLabel = currentNE.getStartPos();
            }
        }
        textParts.add(trimText(originalText.substring(0, startFormerLabel)));

        // Form the new text beginning with its end
        StringBuilder newText = new StringBuilder();
        NamedEntityInText currentEntity;
        for (int i = textParts.size() - 1; i >= 0; --i) {
            // if this part is a named entity
            if ((i & 3) == 2) {
                // update the named entity object
                currentEntity = entities.get((i - 1) / 4);
                currentEntity.setStartPos(newText.length());
                currentEntity.setLength(textParts.get(i).length());
            }
            newText.append(textParts.get(i));
        }
        text.setText(newText.toString());
        return tokens;
    }

    private String trimText(String text) {
        if (text.length() == 0) {
            return text;
        }
        int start = 0;
        int end = text.length();
        while ((start < end) && (text.charAt(start) == '-')) {
            ++start;
        }
        while ((end > start) && (text.charAt(end - 1) == '-')) {
            --end;
        }
        return text.substring(start, end);
    }

    @SuppressWarnings("unused")
    @Deprecated
    private boolean isSpaceOrDash(char c) {
        switch (c) {
        case '-':
        case ' ':
        case '\n':
        case '\t':
        case '\r':
        case 0xA0: {
            return true;
        }
        default: {
            return false;
        }
        }
    }

    protected abstract String processEntity(NamedEntityInText entity, String surfaceForm, Set<String> tokens);
}
