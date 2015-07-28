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
