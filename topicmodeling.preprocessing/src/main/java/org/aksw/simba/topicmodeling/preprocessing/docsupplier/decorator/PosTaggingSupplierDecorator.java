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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.lang.LanguageDependentClass;
import org.aksw.simba.topicmodeling.lang.postagging.PosTagger;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


@SuppressWarnings("deprecation")
public class PosTaggingSupplierDecorator extends AbstractDocumentSupplierDecorator implements LanguageDependentClass {

    private PosTagger postagger;

    public PosTaggingSupplierDecorator(DocumentSupplier documentSource, PosTagger postagger) {
        super(documentSource);
        this.postagger = postagger;
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        TermTokenizedText ttText = postagger.tokenize(text.getText());
        if (ttText != null) {
            document.addProperty(ttText);
        }
        return document;
    }

    public void setPosTaggerFilter(PosTaggingTermFilter filter) {
        postagger.setFilter(filter);
    }

}
