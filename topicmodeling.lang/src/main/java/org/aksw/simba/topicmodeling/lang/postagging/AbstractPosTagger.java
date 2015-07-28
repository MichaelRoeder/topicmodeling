package org.aksw.simba.topicmodeling.lang.postagging;

import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;

public abstract class AbstractPosTagger implements PosTagger {

    private PosTaggingTermFilter filter = null;

    @Override
    public TermTokenizedText tokenize(String text) {
        return filter != null ? tokenizeText(text, filter) : tokenizeText(text);
    }

    @Override
    public void setFilter(PosTaggingTermFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public PosTaggingTermFilter getFilter() {
        return filter;
    }

    protected abstract TermTokenizedText tokenizeText(String text);

    protected abstract TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter);
}
