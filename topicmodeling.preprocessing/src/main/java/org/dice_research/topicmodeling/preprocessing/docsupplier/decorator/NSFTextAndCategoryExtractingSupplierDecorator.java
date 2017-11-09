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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;

public class NSFTextAndCategoryExtractingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(NSFTextAndCategoryExtractingSupplierDecorator.class);

    private static final boolean REMOVE_DOCUMENTS_WITHOUT_CATEGORY = true;

    private static final String CATEGORY_KEY = "Fld Applictn";
    private static final String TEXT_KEY = "Abstract";

    private static final String EMPTY_TEXT = "Not Available";

    private static final String FILTER_CATEGORY = "Other";

    private ObjectIntOpenHashMap<String> categories = new ObjectIntOpenHashMap<String>();

    public NSFTextAndCategoryExtractingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText textProperty = document.getProperty(DocumentText.class);
        if (textProperty == null) {
            throw new IllegalArgumentException("Got a Document without the needed DocumentText property.");
        }
        String text = textProperty.getText();
        String categoryName = extractCategory(text);
        if ((categoryName.isEmpty() || categoryName.contains(FILTER_CATEGORY)) && REMOVE_DOCUMENTS_WITHOUT_CATEGORY) {
            LOGGER.error("Got a document without a category. Removing it.");
            return this.getNextDocument();
        }
        document.addProperty(new DocumentCategory(categoryName));

        text = extractText(text);
        if (text.equals(EMPTY_TEXT)) {
            LOGGER.error("Got a document with an empty Text. Removing it.");
            return this.getNextDocument();
        } else {
            categories.putOrAdd(categoryName, 1, 1);
            document.addProperty(new DocumentText(text));
        }
        return document;
    }

    private String extractCategory(String text) {
        String categoryValue = getValueForKey(text, CATEGORY_KEY);
        String categoryLines[] = getTrimmedLinesOfValue(categoryValue);
        for (int i = categoryLines.length - 1; i >= 0; --i) {
            if ((categoryLines[i] != null) && (!categoryLines[i].isEmpty()) && (!categoryLines[i].equals("null"))) {
                return categoryLines[i];
            }
        }
        LOGGER.warn("Couldn't extract the category from the text. Returning empty String");
        return "";
    }

    private String extractText(String text) {
        String textValue = getValueForKey(text, TEXT_KEY);
        String textLines[] = getTrimmedLinesOfValue(textValue);

        int textLength = 0;
        for (int i = 0; i < textLines.length; i++) {
            textLength += textLines[i].length();
        }
        StringBuilder builder = new StringBuilder(textLength + textLines.length);
        for (int i = 0; i < textLines.length; i++) {
            builder.append(textLines[i]);
            builder.append(' ');
        }
        return builder.toString();
    }

    private String[] getTrimmedLinesOfValue(String value) {
        String lines[] = value.split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }
        return lines;
    }

    private String getValueForKey(String text, String key) {
        int startPos = text.indexOf(key);
        if (startPos < 0) {
            LOGGER.error("Couldn't find value for key \"" + key + "\" in the text \"" + text
                    + "\". Returning empty String");
            return "";
        }
        if (!key.contains(":")) {
            startPos = text.indexOf(':', startPos) + 1;
        } else {
            startPos += key.length();
        }

        int nextKeyPos = text.indexOf(':', startPos);
        // if the given key is the last key of the document
        if (nextKeyPos == -1) {
            return text.substring(startPos);
        }

        int endPos = 0;
        int nextLineBreakPos = text.indexOf('\n', startPos);
        while ((nextLineBreakPos < nextKeyPos) && (nextLineBreakPos > 0)) {
            endPos = nextLineBreakPos;
            nextLineBreakPos = text.indexOf('\n', nextLineBreakPos + 1);
        }
        return text.substring(startPos, endPos);
    }
}
