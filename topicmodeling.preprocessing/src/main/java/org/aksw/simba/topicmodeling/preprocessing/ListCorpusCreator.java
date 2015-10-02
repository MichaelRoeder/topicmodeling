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
package org.aksw.simba.topicmodeling.preprocessing;

import java.util.List;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.DocumentListCorpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class ListCorpusCreator<T extends List<Document>> extends AbstractScaleablePreprocessor {

    public ListCorpusCreator(DocumentSupplier supplier, DocumentListCorpus<T> corpus) {
        super(supplier, corpus);
    }

    public void processDocument(Document document, Corpus corpus) {
        // if (!(DecoratedCorpusHelper.isUndecoratedCorpusInstanceOf(corpus, DocumentListCorpus.class))) {
        // throw new IllegalArgumentException("Got "
        // + corpus.getClass().getCanonicalName()
        // + " instead of the expected "
        // + DocumentListCorpus.class.getCanonicalName() + "!");
        // }
    }

    @Override
    protected Corpus getNewCorpus() {
        return super.corpus;
    }

    @Override
    protected void addDocumentToCorpus(Corpus corpus, Document document) {
        // try {
        // if (!REMOVE_DOCUMENTS_WITHOUT_TEXT || (document.getProperty(DocumentText.class) != null)) {
        // ((DocumentListCorpus<T>) DecoratedCorpusHelper.getUndecoratedCorpus(corpus)).addDocument(document);
        corpus.addDocument(document);
        // }
        // } catch (ClassCastException e) {
        // throw new IllegalArgumentException("Corpus should be of the type DocumentListCorpus.", e);
        // }
    }

}
