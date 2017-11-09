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

import org.dice_research.topicmodeling.lang.Term;
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
