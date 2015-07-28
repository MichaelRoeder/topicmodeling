package org.aksw.simba.topicmodeling.utils.doc;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;


public class TermTokenizedText extends AbstractArrayContainingDocumentProperty implements DocumentProperty {

    private static final long serialVersionUID = -4516711449280786319L;

    protected List<Term> termTokenizedText;

    public TermTokenizedText() {
        termTokenizedText = new ArrayList<Term>();
    }

    public TermTokenizedText(Term... term) {
        this();
        addTerms(term);
    }

    @Override
    public Object getValue() {
        return termTokenizedText;
    }

    public List<Term> getTermTokenizedText() {
        return termTokenizedText;
    }

    public void setTermTokenizedText(List<Term> termTokenizedText) {
        this.termTokenizedText = termTokenizedText;
    }

    public void addTerm(Term term) {
        termTokenizedText.add(term);
    }

    public void addTerms(Term... term) {
        for (int i = 0; i < term.length; ++i) {
            termTokenizedText.add(term[i]);
        }
    }

    @Override
    public Object[] getValueAsArray() {
        return termTokenizedText.toArray();
    }
}
