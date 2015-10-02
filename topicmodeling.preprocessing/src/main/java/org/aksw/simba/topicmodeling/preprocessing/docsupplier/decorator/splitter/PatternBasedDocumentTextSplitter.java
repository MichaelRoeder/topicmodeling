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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.splitter;

import java.lang.reflect.Constructor;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.aksw.simba.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PatternBasedDocumentTextSplitter<T extends StringContainingDocumentProperty> extends
        AbstractSplittingDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatternBasedDocumentTextSplitter.class);

    // private Document lastDocument;
    // private Queue<String> paragraphs = new LinkedList<String>();
    private String splitPattern;
    private Class<T> propertyClass;
    private Constructor<T> propertyConstructor;
    private boolean trimSplittedParts;

    public PatternBasedDocumentTextSplitter(DocumentSupplier documentSource, Class<T> propertyClass, String splitPattern) {
        this(documentSource, propertyClass, splitPattern, true);
    }

    public PatternBasedDocumentTextSplitter(DocumentSupplier documentSource, Class<T> propertyClass,
            String splitPattern, boolean trimSplittedParts) {
        super(documentSource);
        this.splitPattern = splitPattern;
        this.propertyClass = propertyClass;
        this.trimSplittedParts = trimSplittedParts;
        try {
            propertyConstructor = propertyClass.getConstructor(String.class);
        } catch (Exception e) {
            LOGGER.error("The given class " + propertyClass.getCanonicalName()
                    + " doesn't have a constructor which takes a single String. This splitter won't work as expected!",
                    e);
        }
    }

    public boolean isTrimSplittedParts() {
        return trimSplittedParts;
    }

    public void setTrimSplittedParts(boolean trimSplittedParts) {
        this.trimSplittedParts = trimSplittedParts;
    }

    @Override
    protected void splitDocument(Document document) {
        if (document == null) {
            return;
        }
        T stringProperty = document.getProperty(propertyClass);
        if (stringProperty == null) {
            LOGGER.info("Got a document without the needed " + propertyClass.getSimpleName()
                    + "property. Ignoring this document.");
            return;
        }
        String value = stringProperty.getStringValue();
        String splittedValue[] = value.split(splitPattern);
        String singleSplit;
        for (int i = 0; i < splittedValue.length; ++i) {
            singleSplit = splittedValue[i];
            if (trimSplittedParts) {
                singleSplit = singleSplit.trim();
            }
            if (singleSplit.length() > 0) {
                this.queue.add(createDocument(singleSplit, document));
            }
        }
    }

    private Document createDocument(String value, Document document) {
        Document newDocument = new Document(getNextDocumentId());
        for (DocumentProperty property : document) {
            newDocument.addProperty(property);
        }
        try {
            newDocument.addProperty(propertyConstructor.newInstance(value));
        } catch (Exception e) {
            LOGGER.error("Couldn't create new instance of " + propertyClass.getSimpleName()
                    + "property. This Splitter won't work as expected.", e);
        }
        return newDocument;
    }

    // @Override
    // public Document getNextDocument() {
    // if (paragraphs.size() == 0) {
    // do {
    // lastDocument = documentSource.getNextDocument();
    // createParagraphs(lastDocument);
    // } while ((lastDocument != null) && (paragraphs.size() == 0));
    // }
    // return createNextDocument();
    // }

    // private void createParagraphs(Document document) {
    // if (document == null) {
    // return;
    // }
    // DocumentText docText = document.getProperty(DocumentText.class);
    // if (docText == null) {
    // LOGGER.info("Got a document without the needed DocumentText property. Ignoring this document.");
    // return;
    // }
    // String text = docText.getText();
    // String textParagraphs[] = text.split("\n\\s*\n");
    // String paragraph;
    // for (int i = 0; i < textParagraphs.length; ++i) {
    // paragraph = textParagraphs[i].trim();
    // if (paragraph.length() > 0) {
    // paragraphs.add(paragraph);
    // }
    // }
    // }

    // private Document createNextDocument() {
    // if (paragraphs.size() == 0) {
    // return null;
    // }
    // Document nextDocument = new Document(getNextDocumentId());
    // for (DocumentProperty property : lastDocument) {
    // nextDocument.addProperty(property);
    // }
    // nextDocument.addProperty(new DocumentText(paragraphs.poll()));
    // return nextDocument;
    // }

}
