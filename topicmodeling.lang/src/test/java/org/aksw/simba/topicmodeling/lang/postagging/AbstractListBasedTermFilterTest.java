package org.aksw.simba.topicmodeling.lang.postagging;

import junit.framework.Assert;

import org.aksw.simba.topicmodeling.lang.Term;
import org.junit.Test;

public abstract class AbstractListBasedTermFilterTest {

    private AbstractListBasedTermFilter filter;
    private String term;
    private boolean shouldBeAccepted;

    public AbstractListBasedTermFilterTest(AbstractListBasedTermFilter filter, String term, boolean shouldBeAccepted) {
        this.filter = filter;
        this.term = term;
        this.shouldBeAccepted = shouldBeAccepted;
    }

    @Test
    public void test() {
        Assert.assertEquals(shouldBeAccepted, filter.isTermGood(new Term(term)));
    }
}
