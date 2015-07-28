package org.aksw.simba.topicmodeling.lang.postagging;

import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;

public interface PosTagger {

    public TermTokenizedText tokenize(String text);
    
    public void setFilter(PosTaggingTermFilter filter);
    
    public PosTaggingTermFilter getFilter();
}
