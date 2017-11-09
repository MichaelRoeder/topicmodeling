/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io.xml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.dice_research.topicmodeling.utils.doc.ner.SignedNamedEntityInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractDocumentXmlWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocumentXmlWriter.class);

    protected void writeDocument(OutputStreamWriter fout, Document document) throws IOException {
        fout.write("<" + CorpusXmlTagHelper.DOCUMENT_TAG_NAME + " id=\"" + document.getDocumentId() + "\">\n");
        DocumentText text = null;
        NamedEntitiesInText nes = null;
        DocumentMultipleCategories categories = null;
        for (DocumentProperty property : document) {
            if (property instanceof DocumentText) {
                text = (DocumentText) property;
            } else if (property instanceof NamedEntitiesInText) {
                nes = (NamedEntitiesInText) property;
            } else if (property instanceof DocumentMultipleCategories) {
                categories = (DocumentMultipleCategories) property;
            } else if (property instanceof ParseableDocumentProperty) {
                writeDocumentProperty(fout, (ParseableDocumentProperty) property);
            }
        }
        if (categories != null) {
            fout.write("<" + CorpusXmlTagHelper.DOCUMENT_CATEGORIES_TAG_NAME + ">\n");
            writeArray(fout, categories.getCategories(),
                    CorpusXmlTagHelper.DOCUMENT_CATEGORIES_SINGLE_CATEGORY_TAG_NAME);
            fout.write("</" + CorpusXmlTagHelper.DOCUMENT_CATEGORIES_TAG_NAME + ">\n");
        }
        if (text != null) {
            if (nes != null) {
                fout.write("<" + CorpusXmlTagHelper.TEXT_WITH_NAMED_ENTITIES_TAG_NAME + ">" + prepareText(text, nes)
                        + "</" + CorpusXmlTagHelper.TEXT_WITH_NAMED_ENTITIES_TAG_NAME + ">\n");
            } else {
                writeDocumentProperty(fout, text);
            }
        }
        fout.write("</" + CorpusXmlTagHelper.DOCUMENT_TAG_NAME + ">\n");
    }

    protected void writeDocumentProperty(OutputStreamWriter fout, ParseableDocumentProperty property) throws IOException {
        String tagName = CorpusXmlTagHelper.getTagNameOfParseableDocumentProperty(property.getClass());
        if (tagName != null) {
            fout.write("<" + tagName + ">");
            if (property instanceof StringContainingDocumentProperty) {
                fout.write(StringEscapeUtils.escapeXml11(((StringContainingDocumentProperty) property).getStringValue()));
            } else {
                fout.write(StringEscapeUtils.escapeXml11(property.getValue().toString()));
            }
            fout.write("</" + tagName + ">\n");
        } else {
            LOGGER.error("There is no XML tag name defined for the ParseableDocumentProperty class "
                    + property.getClass().getCanonicalName() + ". Discarding this property.");
        }
    }

    protected void writeArray(OutputStreamWriter fout, Object[] array, String elementTagName) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            fout.write("<" + elementTagName + ">" + array[i].toString() + "</" + elementTagName + ">\n");
        }
    }

    protected String prepareText(DocumentText text, NamedEntitiesInText nes) {
        List<String> textParts = new ArrayList<String>();
        List<NamedEntityInText> entities = nes.getNamedEntities();
        Collections.sort(entities);
        String originalText = text.getText();
        // start with the last label and add the parts of the new text beginning
        // with its end to the array
        // Note that we are expecting that the labels are sorted descending by
        // there position in the text!
        boolean isSignedNamedEntity;
        int startFormerLabel = originalText.length();
        for (NamedEntityInText currentNE : entities) {
            // proof if this label undercuts the last one.
            if (startFormerLabel >= currentNE.getEndPos()) {
                isSignedNamedEntity = currentNE instanceof SignedNamedEntityInText;
                // append the text between this label and the former one
                textParts.add(">");
                textParts.add(CorpusXmlTagHelper.TEXT_PART_TAG_NAME);
                textParts.add("</");
                try {
                    textParts.add(StringEscapeUtils.escapeXml11(originalText.substring(currentNE.getEndPos(),
                            startFormerLabel)));
                } catch (StringIndexOutOfBoundsException e) {
                    LOGGER.error("Got a wrong named entity (" + currentNE.toString() + ")", e);
                    textParts.add("<AN_ERROR_OCCURED/>");
                }
                textParts.add(">");
                textParts.add(CorpusXmlTagHelper.TEXT_PART_TAG_NAME);
                textParts.add("<");
                // append the markedup label
                textParts.add(">");
                textParts.add(isSignedNamedEntity ? CorpusXmlTagHelper.SIGNED_NAMED_ENTITY_IN_TEXT_TAG_NAME
                        : CorpusXmlTagHelper.NAMED_ENTITY_IN_TEXT_TAG_NAME);
                textParts.add("</");
                try {
                    textParts.add(StringEscapeUtils.escapeXml11(originalText.substring(currentNE.getStartPos(),
                            currentNE.getEndPos())));
                } catch (StringIndexOutOfBoundsException e) {
                    LOGGER.error("Got a wrong named entity (" + currentNE.toString() + ")", e);
                    textParts.add("<AN_ERROR_OCCURED/>");
                }
                textParts.add("\">");
                // textParts.add(Integer.toString(currentNE.getLength()));
                // textParts.add("\" length=\"");
                // textParts.add(Integer.toString(currentNE.getStartPos()));
                // textParts.add("\" start=\"");
                if (isSignedNamedEntity) {
                    textParts.add(((SignedNamedEntityInText) currentNE).getSource());
                    textParts.add("\" source=\"");
                }
                textParts.add(currentNE.getNamedEntityUri());
                textParts.add(" uri=\"");
                textParts.add(isSignedNamedEntity ? CorpusXmlTagHelper.SIGNED_NAMED_ENTITY_IN_TEXT_TAG_NAME
                        : CorpusXmlTagHelper.NAMED_ENTITY_IN_TEXT_TAG_NAME);
                textParts.add("<");
                // remember the start position of this label
                startFormerLabel = currentNE.getStartPos();
            }
        }
        if (startFormerLabel > 0) {
            textParts.add("</SimpleTextPart>");
            textParts.add(StringEscapeUtils.escapeXml11(originalText.substring(0, startFormerLabel)));
            textParts.add("<SimpleTextPart>");
        }
        // Form the new text beginning with its end
        StringBuilder textWithMarkups = new StringBuilder();
        for (int i = textParts.size() - 1; i >= 0; --i) {
            textWithMarkups.append(textParts.get(i));
        }
        return textWithMarkups.toString();
    }

    public static void registerParseableDocumentProperty(Class<? extends ParseableDocumentProperty> clazz) {
        CorpusXmlTagHelper.registerParseableDocumentProperty(clazz);
    }
}
