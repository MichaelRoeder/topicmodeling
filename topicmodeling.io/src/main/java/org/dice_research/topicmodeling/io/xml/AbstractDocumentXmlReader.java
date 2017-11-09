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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.dice_research.topicmodeling.utils.doc.ner.SignedNamedEntityInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDocumentXmlReader implements XMLParserObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocumentXmlReader.class);

    private Document currentDocument;
    private NamedEntityInText currentNamedEntity;
    private List<NamedEntityInText> namedEntities = new ArrayList<NamedEntityInText>();
    private List<String> categories = new ArrayList<String>();
    private StringBuilder textBuffer = new StringBuilder();
    private String data;

    public AbstractDocumentXmlReader() {
    }

    @Override
    public void handleOpeningTag(String tagString) {
        // SHOULDN'T THIS METHOD SET data=""? Otherwise it is possible that a
        // closing tag gets data that was set
        // before its starting tag has been seen (in most cases this is "\n").
        data = "";
        // Ok, this is done and the JUnit test is green. This comment will
        // remain here if another problem should
        // arose.

        int pos = tagString.indexOf(' ');
        String tagName;
        if (pos == -1) {
            tagName = tagString;
        } else {
            tagName = tagString.substring(0, pos);
        }

        if (tagName.equals(CorpusXmlTagHelper.DOCUMENT_TAG_NAME)) {
            currentDocument = new Document();
            pos = tagString.indexOf(" id=\"");
            if (pos > 0) {
                pos += 5;
                int tmp = tagString.indexOf('"', pos + 1);
                if (tmp > 0) {
                    try {
                        tmp = Integer.parseInt(tagString.substring(pos, tmp));
                        currentDocument.setDocumentId(tmp);
                    } catch (NumberFormatException e) {
                        LOGGER.warn("Coudln't parse the document id from the document tag.", e);
                    }
                } else {
                    LOGGER.warn("Found a document tag without a document id attribute.");
                }
            }
        } else if (tagName.equals(CorpusXmlTagHelper.NAMED_ENTITY_IN_TEXT_TAG_NAME)
                || tagName.equals(CorpusXmlTagHelper.SIGNED_NAMED_ENTITY_IN_TEXT_TAG_NAME)) {
            currentNamedEntity = parseNamedEntityInText(tagString);
            currentNamedEntity.setStartPos(textBuffer.length());
        }
    }

    @Override
    public void handleClosingTag(String tagString) {
        if (tagString.equals(CorpusXmlTagHelper.DOCUMENT_TAG_NAME)) {
            finishedDocument(currentDocument);
            currentDocument = null;
        } else
            if (tagString.equals(CorpusXmlTagHelper.TEXT_WITH_NAMED_ENTITIES_TAG_NAME) && (currentDocument != null)) {
            currentDocument.addProperty(new DocumentText(textBuffer.toString()));
            textBuffer.delete(0, textBuffer.length());
            NamedEntitiesInText nes = new NamedEntitiesInText(namedEntities);
            currentDocument.addProperty(nes);
            namedEntities.clear();
        } else if (tagString.equals(CorpusXmlTagHelper.TEXT_PART_TAG_NAME)) {
            textBuffer.append(data);
            data = "";
        } else if (tagString.equals(CorpusXmlTagHelper.NAMED_ENTITY_IN_TEXT_TAG_NAME)
                || tagString.equals(CorpusXmlTagHelper.SIGNED_NAMED_ENTITY_IN_TEXT_TAG_NAME)) {
            if (currentNamedEntity != null) {
                currentNamedEntity.setLength(data.length());
                namedEntities.add(currentNamedEntity);
                textBuffer.append(data);
                currentNamedEntity = null;
                data = "";
            }
        } else if (tagString.equals(CorpusXmlTagHelper.DOCUMENT_CATEGORIES_TAG_NAME)) {
            currentDocument
                    .addProperty(new DocumentMultipleCategories(categories.toArray(new String[categories.size()])));
            categories.clear();
        } else if (tagString.equals(CorpusXmlTagHelper.DOCUMENT_CATEGORIES_SINGLE_CATEGORY_TAG_NAME)) {
            categories.add(data);
            data = "";
        } else {
            if (currentDocument != null) {
                Class<? extends ParseableDocumentProperty> propertyClazz = CorpusXmlTagHelper
                        .getParseableDocumentPropertyClassForTagName(tagString);
                if (propertyClazz != null) {
                    try {
                        ParseableDocumentProperty property;
                        Constructor<? extends ParseableDocumentProperty> constructor;
                        try {
                            constructor = propertyClazz.getConstructor(String.class);
                            property = constructor.newInstance(data);
                        } catch (NoSuchMethodException e) {
                            // Couldn't get a constructor accepting a single
                            // String. Lets try the normal constructor.
                            constructor = propertyClazz.getConstructor();
                            property = constructor.newInstance();
                            property.parseValue(data);
                        }
                        currentDocument.addProperty(property);
                    } catch (Exception e) {
                        LOGGER.error("Couldn't parse property " + propertyClazz + " from the String \"" + data + "\".",
                                e);
                    }
                }
                data = "";
            }
        }
    }

    @Override
    public void handleData(String data) {
        this.data = data;
    }

    @Override
    public void handleEmptyTag(String tagString) {
        // nothing to do
    }

    protected NamedEntityInText parseNamedEntityInText(String tag) {
        String namedEntityUri = null;
        String namedEntitySource = null;
        int startPos = -1;
        int length = -1;
        int start = 0, end = 0;
        try {
            start = tag.indexOf(' ') + 1;
            end = tag.indexOf('=', start);
            String key, value;
            while (end > 0) {
                key = tag.substring(start, end).trim();
                end = tag.indexOf('"', end);
                start = tag.indexOf('"', end + 1);
                value = tag.substring(end + 1, start);
                if (key.equals(CorpusXmlTagHelper.URI_ATTRIBUTE_NAME)) {
                    namedEntityUri = value;
                } else if (key.equals(CorpusXmlTagHelper.SOURCE_ATTRIBUTE_NAME)) {
                    namedEntitySource = value;
                }
                /*
                 * else if (key.equals("start")) { startPos =
                 * Integer.parseInt(value); } else if (key.equals("length")) {
                 * length = Integer.parseInt(value); }
                 */
                ++start;
                end = tag.indexOf('=', start);
            }
            if (namedEntitySource != null) {
                return new SignedNamedEntityInText(startPos, length, namedEntityUri, namedEntitySource);
            } else {
                return new NamedEntityInText(startPos, length, namedEntityUri);
            }
        } catch (Exception e) {
            LOGGER.error("Couldn't parse NamedEntityInText tag (" + tag + "). Returning null.", e);
        }
        return null;
    }

    public static void registerParseableDocumentProperty(Class<? extends ParseableDocumentProperty> clazz) {
        CorpusXmlTagHelper.registerParseableDocumentProperty(clazz);
    }

    protected abstract void finishedDocument(Document document);
}
