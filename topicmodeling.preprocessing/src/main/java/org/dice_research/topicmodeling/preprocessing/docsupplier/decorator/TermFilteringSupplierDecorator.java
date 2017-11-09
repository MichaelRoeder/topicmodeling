/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;


public class TermFilteringSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private PosTaggingTermFilter filter = null;

    public TermFilteringSupplierDecorator(DocumentSupplier documentSource, PosTaggingTermFilter filter) {
        super(documentSource);
        this.filter = filter;
    }

    @Override
    public Document prepareDocument(Document document) {
        TermTokenizedText ttText = document.getProperty(TermTokenizedText.class);
        if (ttText == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        filterWords(ttText.getTermTokenizedText());
        return document;
    }

    private void filterWords(List<Term> termTokenizedText) {
        List<Term> newText = new ArrayList<Term>();
        for (Term t : termTokenizedText) {
            if (filter.isTermGood(t)) {
                newText.add(t);
            }
        }
        termTokenizedText.clear();
        termTokenizedText.addAll(newText);
    }
}
