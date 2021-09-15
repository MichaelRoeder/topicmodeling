package org.dice_research.topicmodeling.wikipedia;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.AbstractPropertyEditingDocumentSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikipediaMarkupDeletingDocumentSupplierDecorator extends
        AbstractPropertyEditingDocumentSupplierDecorator<DocumentText> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(WikipediaMarkupDeletingDocumentSupplierDecorator.class);

    private boolean removeCategoryLinks = StackBasedMarkupDeletingMachine.REMOVE_CATEGORY_LINKS_DEFAULT;
    private boolean keepTableContents = StackBasedMarkupDeletingMachine.KEEP_TABLE_CONTENTS_DEFAULT;

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource) {
        super(documentSource, DocumentText.class);
    }

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource, boolean removeCategoryLinks) {
        this(documentSource);
        this.removeCategoryLinks = removeCategoryLinks;
    }

    public WikipediaMarkupDeletingDocumentSupplierDecorator(
            DocumentSupplier documentSource, boolean removeCategoryLinks, boolean keepTableContents) {
        this(documentSource);
        this.removeCategoryLinks = removeCategoryLinks;
        this.keepTableContents = keepTableContents;
    }

    @Override
    protected void editDocumentProperty(DocumentText text) {
        text.setText(createDeleter().getCleanText(text.getText()));
    }
    
    public StackBasedMarkupDeletingMachine createDeleter() {
        StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine();
        deleter.setRemoveCategoryLinks(removeCategoryLinks);
        deleter.setKeepTableContents(keepTableContents);
        return deleter;
    }

    public void setRemoveCategoryLinks(boolean removeCategoryLinks) {
        this.removeCategoryLinks = removeCategoryLinks;
    }

    public void setKeepTableContents(boolean keepTableContents) {
        this.keepTableContents = keepTableContents;
    }
}
