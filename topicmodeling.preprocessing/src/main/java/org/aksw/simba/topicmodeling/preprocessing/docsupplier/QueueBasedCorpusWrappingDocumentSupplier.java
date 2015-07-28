package org.aksw.simba.topicmodeling.preprocessing.docsupplier;

import java.util.LinkedList;
import java.util.Queue;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class QueueBasedCorpusWrappingDocumentSupplier implements DocumentSupplier {

    private Queue<Document> queue;

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus) {
        this(corpus, 0, corpus.getNumberOfDocuments());
    }

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus, int documentStartId) {
        this(corpus, documentStartId, corpus.getNumberOfDocuments());
    }

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus, int documentStartId, int documentEndId) {
        queue = new LinkedList<Document>(corpus.getDocuments(documentStartId, documentEndId));
    }

    @Override
    public Document getNextDocument() {
        Document document = queue.poll();
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        throw new IllegalStateException("This supplier does not have a start document Id.");
    }
}