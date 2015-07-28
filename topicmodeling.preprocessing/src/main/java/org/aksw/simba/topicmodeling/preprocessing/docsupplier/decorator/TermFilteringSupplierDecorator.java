package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


public class TermFilteringSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private PosTaggingTermFilter filter = null;

    public TermFilteringSupplierDecorator(DocumentSupplier documentSource, PosTaggingTermFilter filter) {
        super(documentSource);
        this.filter = filter;
    }

    @Override
    public Document prepareDocument(Document document) {
        TermTokenizedText ttText = document.getProperty(TermTokenizedText.class);
        if (ttText == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        filterWords(ttText.getTermTokenizedText());
        return document;
    }

    private void filterWords(List<Term> termTokenizedText) {
        List<Term> newText = new ArrayList<Term>();
        for (Term t : termTokenizedText) {
            if (filter.isTermGood(t)) {
                newText.add(t);
            }
        }
        termTokenizedText.clear();
        termTokenizedText.addAll(newText);
    }
}
