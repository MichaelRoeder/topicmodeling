package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaRedirect;

public class WikipediaRedirectDetectingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    public WikipediaRedirectDetectingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text != null) {
            String rawText = text.getText().trim();
            if (rawText.startsWith("#")) {
                if (rawText.toLowerCase().startsWith("#redirect")) {
                    document.addProperty(new WikipediaRedirect(rawText.substring(9).trim()));
                } else {
                    System.out.println(rawText);
                }
            }
        }
        return document;
    }
}
