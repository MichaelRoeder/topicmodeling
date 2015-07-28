package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractPreprocessor implements Preprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPreprocessor.class);

    private DocumentSupplier supplier;
    protected Corpus corpus;
    protected boolean corpusCreated = false;

    public AbstractPreprocessor(DocumentSupplier supplier) {
        this.supplier = supplier;
    }

    public AbstractPreprocessor(DocumentSupplier supplier, Corpus corpus) {
        this(supplier);
        this.corpus = corpus;
    }

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        /* nothing to do */
    }

    @Override
    public Corpus getCorpus() {
        if (!corpusCreated) {
            generateCorpus();
        }
        return corpus;
    }

    protected void generateCorpus() {
        if (corpus == null) {
            corpus = getNewCorpus();
        }
        Document document = supplier.getNextDocument();
        while (document != null) {
            addDocumentToCorpus(corpus, document);
            if ((corpus.getNumberOfDocuments() % 1000) == 0) {
                LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
            }
            document = supplier.getNextDocument();
        }
        corpusCreated = true;
        LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
    }

    @Override
    public boolean hasCorpus() {
        return corpusCreated;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
        corpusCreated = false;
    }
    
    protected DocumentSupplier getSupplier() {
        return supplier;
    }

    protected abstract Corpus getNewCorpus();

    protected abstract void addDocumentToCorpus(Corpus corpus, Document document);
}
