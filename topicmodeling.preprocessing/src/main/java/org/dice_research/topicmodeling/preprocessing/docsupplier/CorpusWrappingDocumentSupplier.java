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
package org.dice_research.topicmodeling.preprocessing.docsupplier;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.AbstractCorpusDecorator;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;


public class CorpusWrappingDocumentSupplier extends AbstractCorpusDecorator
        implements DocumentSupplier {

    private static final long serialVersionUID = -8846786462413271032L;

    protected int nextDocumentId;

    public CorpusWrappingDocumentSupplier(Corpus corpus) {
        super(corpus);
        nextDocumentId = 0;
    }

    public CorpusWrappingDocumentSupplier(Corpus corpus, int documentStartId) {
        super(corpus);
        nextDocumentId = documentStartId;
    }

    @Override
    public Document getNextDocument() {
        Document document = null;
        if (nextDocumentId < corpus.getNumberOfDocuments()) {
            document = corpus.getDocument(nextDocumentId);
            document.setDocumentId(nextDocumentId);
            ++nextDocumentId;
        }
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        nextDocumentId = documentStartId;
    }
}
