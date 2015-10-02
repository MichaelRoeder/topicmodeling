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
package org.aksw.simba.topicmodeling.lang.postagging;

import java.util.List;
import java.util.StringTokenizer;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


public class SimpleWhitespaceTagger extends AbstractPosTagger {

    public SimpleWhitespaceTagger() {
    }

    public SimpleWhitespaceTagger(PosTaggingTermFilter filter) {
        setFilter(filter);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        String token;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            terms.add(new Term(token));
        }
        return ttText;
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        String token;
        Term term;
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            term = new Term(token);
            if (filter.isTermGood(term)) {
                terms.add(term);
            }
        }
        return ttText;
    }

}
