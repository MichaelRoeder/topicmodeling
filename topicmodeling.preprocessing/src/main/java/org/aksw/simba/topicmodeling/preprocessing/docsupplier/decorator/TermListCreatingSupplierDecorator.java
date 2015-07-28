package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


public class TermListCreatingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private PosTaggingTermFilter filter;
    private Set<Term> terms = new HashSet<Term>();

    public TermListCreatingSupplierDecorator(DocumentSupplier documentSource, PosTaggingTermFilter filter) {
        super(documentSource);
        this.filter = filter;
    }

    @Override
    public Document prepareDocument(Document document) {
        TermTokenizedText ttText = document.getProperty(TermTokenizedText.class);
        if (ttText == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        listTerms(ttText.getTermTokenizedText());
        return document;
    }

    private void listTerms(List<Term> termTokenizedText) {
        for (Term term : termTokenizedText) {
            if (filter.isTermGood(term)) {
                terms.add(term);
            }
        }
    }

    public Set<Term> getTerms() {
        return terms;
    }
}
