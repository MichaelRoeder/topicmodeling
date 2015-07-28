package org.aksw.simba.topicmodeling.io;

import java.io.File;

import org.aksw.simba.topicmodeling.io.CorpusReader;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCorpusReader implements CorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCorpusReader.class);

    protected File file;
    protected Corpus corpus;

    public AbstractCorpusReader(File file) {
        this.file = file;
    }

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        LOGGER.info("Got a " + documentFactory.getClass().getCanonicalName()
                + " object as DocumentSupplier. But I'm a corpus reader and don't need such a supplier. ");
    }

    @Override
    public Corpus getCorpus() {
        if (corpus == null) {
            readCorpus();
        }
        return corpus;
    }

    @Override
    public boolean hasCorpus() {
        return corpus != null;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
    }
}
