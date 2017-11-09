package org.dice_research.topicmodeling.wikipedia;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikipediaMarkupDeletingDocumentSupplierDecorator extends
        AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(WikipediaMarkupDeletingDocumentSupplierDecorator.class);

    // private WikipediaMarkupDeletingMachine deleter;
    private StackBasedMarkupDeletingMachine deleter;

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource) {
        super(documentSource);
        deleter = new StackBasedMarkupDeletingMachine();
    }

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource, boolean removeCategoryLinks) {
        this(documentSource);
        deleter.setRemoveCategoryLinks(removeCategoryLinks);
    }

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource, boolean removeCategoryLinks, boolean keepTableContents) {
        this(documentSource);
        deleter.setRemoveCategoryLinks(removeCategoryLinks);
        deleter.setKeepTableContents(keepTableContents);
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            LOGGER.error("Got a Document without a DocumentText property!");
            return null;
        }
        text.setText(deleter.getCleanText(text.getText()));
        return document;
    }

    public void setRemoveCategoryLinks(boolean removeCategoryLinks) {
        deleter.setRemoveCategoryLinks(removeCategoryLinks);
    }

    public void setKeepTableContents(boolean keepTableContents) {
        deleter.setKeepTableContents(keepTableContents);
    }
}
