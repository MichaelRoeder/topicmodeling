package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


public class StemmedTextCreatorSupplierDecorator extends AbstractDocumentSupplierDecorator {

    public StemmedTextCreatorSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
        if (tttext == null) {
            throw new IllegalArgumentException("Got a document without the needed TermTokenizedText property.");
        }
        document.addProperty(new DocumentText(createStemmedText(tttext)));
        return document;
    }

    private String createStemmedText(TermTokenizedText tttext) {
        StringBuilder result = new StringBuilder();
        for (Term term : tttext.getTermTokenizedText()) {
            result.append(term.getLemma());
            result.append(' ');
        }
        return result.toString();
    }

}
