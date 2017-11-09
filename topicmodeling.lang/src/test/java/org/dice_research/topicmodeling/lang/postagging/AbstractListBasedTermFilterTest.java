/**
 * This file is part of topicmodeling.lang.
 *
 * topicmodeling.lang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.lang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.lang.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.lang.postagging;

import junit.framework.Assert;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.lang.postagging.AbstractListBasedTermFilter;
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
