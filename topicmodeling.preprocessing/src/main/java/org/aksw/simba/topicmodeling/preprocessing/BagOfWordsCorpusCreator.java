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

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.BagOfWordsCorpus;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.SimpleBagOfWordsCorpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.vocabulary.SimpleVocabulary;


@Deprecated
public class BagOfWordsCorpusCreator extends AbstractPreprocessor {

    public BagOfWordsCorpusCreator(DocumentSupplier supplier) {
        super(supplier);
    }

    public BagOfWordsCorpusCreator(DocumentSupplier supplier, BagOfWordsCorpus corpus) {
        super(supplier, corpus);
    }

    private void countWords(int documentId, TermTokenizedText tokenizedTextProperty, BagOfWordsCorpus corpus) {
        List<Term> tokenizedText = tokenizedTextProperty.getTermTokenizedText();
        for (Term term : tokenizedText)
        {
            corpus.increaseWordCount(term.getLemma(), documentId);
        }
    }

    @Override
    protected Corpus getNewCorpus() {
        return new SimpleBagOfWordsCorpus(new SimpleVocabulary());
    }

    @Override
    protected void addDocumentToCorpus(Corpus corpus, Document document) {
        ((BagOfWordsCorpus) corpus).addDocument(document);
        TermTokenizedText ttText = document.getProperty(TermTokenizedText.class);
        if (ttText == null) {
            throw new IllegalArgumentException("Got a Document object without the needed TermTokenizedText property!");
        }
        countWords(document.getDocumentId(), ttText, (BagOfWordsCorpus) corpus);
    }
}
