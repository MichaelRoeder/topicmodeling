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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.ner.filter;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StopwordBasedEntityFilter implements NamedEntitiesFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopwordBasedEntityFilter.class);

    private static final int MINIMUM_LENGTH_OF_NE = 2;

    private Set<String> stopwordlist;

    public StopwordBasedEntityFilter() {
        createWordList(new File(this.getClass().getClassLoader().getResource("englishStopwordlist.txt")
                .toString().replace("file:", "")));
    }

    protected void createWordList(File stopwordfile) {
        try {
            stopwordlist = new HashSet<String>(FileUtils.readLines(stopwordfile));
        } catch (Exception e) {
            LOGGER.error("Couldn't read word list from file. This PosTaggingTermFilter won't work as expected!", e);
        }
    }

    @Override
    public boolean isNamedEntityGood(Document document, NamedEntityInText entity) {
        if (entity.getLength() < MINIMUM_LENGTH_OF_NE) {
            return false;
        }
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            return false;
        }
        String surfaceForm = text.getText().substring(entity.getStartPos(), entity.getEndPos()).toLowerCase();
        return !stopwordlist.contains(surfaceForm);
    }
}
