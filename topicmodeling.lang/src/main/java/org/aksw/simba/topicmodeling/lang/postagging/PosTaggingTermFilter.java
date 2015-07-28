package org.aksw.simba.topicmodeling.lang.postagging;

import org.aksw.simba.topicmodeling.lang.Term;

public interface PosTaggingTermFilter {

    public boolean isTermGood(Term term);
}
