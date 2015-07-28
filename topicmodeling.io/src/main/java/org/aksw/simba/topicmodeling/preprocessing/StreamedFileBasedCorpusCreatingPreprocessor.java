package org.aksw.simba.topicmodeling.preprocessing;

import java.io.File;

import org.aksw.simba.topicmodeling.io.stream.DocumentSupplierSerializer;
import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.StreamedFileBasedCorpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StreamedFileBasedCorpusCreatingPreprocessor implements Preprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamedFileBasedCorpusCreatingPreprocessor.class);

    private DocumentSupplier supplier;
    protected StreamedFileBasedCorpus corpus = null;
    protected boolean corpusCreated = false;
    protected File corpusFile;

    public StreamedFileBasedCorpusCreatingPreprocessor(DocumentSupplier supplier, File corpusFile) {
        this.supplier = supplier;
        this.corpusFile = corpusFile;
    }

    @Override
    @Deprecated
    public void addDocuments(DocumentSupplier documentFactory) {
        throw new UnsupportedOperationException("This method is not implemented!");
    }

    @Override
    public Corpus getCorpus() {
        if (!corpusCreated) {
            generateCorpus();
        }
        return corpus;
    }

    protected void generateCorpus() {
        DocumentSupplierSerializer serializer = new DocumentSupplierSerializer();
        int documentCount = serializer.serialize(supplier, corpusFile);
        corpus = new StreamedFileBasedCorpus(corpusFile, documentCount);
        corpusCreated = true;
        LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
    }

    @Override
    public boolean hasCorpus() {
        return corpusCreated;
    }

    @Override
    public void deleteCorpus() {
        if (corpus != null) {
            corpus.clear();
            corpus = null;
        }
        corpusCreated = false;
    }

}
