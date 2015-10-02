/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
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
