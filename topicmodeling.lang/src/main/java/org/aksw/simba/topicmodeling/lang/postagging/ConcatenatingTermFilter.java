package org.aksw.simba.topicmodeling.lang.postagging;

import org.aksw.simba.topicmodeling.lang.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConcatenatingTermFilter implements PosTaggingTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcatenatingTermFilter.class);

    public static enum ConcatenationMode {
        AND, OR
    }

    private ConcatenationMode mode;
    private PosTaggingTermFilter filters[];

    public ConcatenatingTermFilter(ConcatenationMode mode, PosTaggingTermFilter... filter) {
        this.mode = mode;
        this.filters = filter;
    }

    @Override
    public boolean isTermGood(Term term) {
        switch (mode) {
        case AND: {
            return concatenateByAnd(term);
        }
        case OR: {
            return concatenateByOr(term);
        }
        default: {
            LOGGER.error("Undefined mode \"" + mode + "\"!");
            return false;
        }
        }
    }

    private boolean concatenateByOr(Term term) {
        boolean termIsGood = false;
        int filterId = 0;
        while ((termIsGood == false) && (filterId < filters.length)) {
            termIsGood = filters[filterId].isTermGood(term);
            ++filterId;
        }
        return termIsGood;
    }

    private boolean concatenateByAnd(Term term) {
        boolean termIsGood = true;
        int filterId = 0;
        while ((termIsGood == true) && (filterId < filters.length)) {
            termIsGood = filters[filterId].isTermGood(term);
            ++filterId;
        }
        return termIsGood;
    }

}
