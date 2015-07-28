package org.aksw.simba.topicmodeling.preprocessing.docsupplier;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.AbstractCorpusDecorator;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;


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
