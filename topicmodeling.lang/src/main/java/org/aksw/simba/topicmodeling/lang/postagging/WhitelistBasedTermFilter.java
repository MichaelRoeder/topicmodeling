package org.aksw.simba.topicmodeling.lang.postagging;

import java.io.File;

import org.aksw.simba.topicmodeling.lang.Term;


public class WhitelistBasedTermFilter extends AbstractListBasedTermFilter {

    public WhitelistBasedTermFilter(File wordfile) {
        createWordListSafely(wordfile);
    }

    @Override
    public boolean isTermGood(Term term) {
        return isWordInWordlist(term.getLemma());
    }

}
