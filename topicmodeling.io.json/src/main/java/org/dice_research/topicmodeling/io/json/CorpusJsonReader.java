package org.dice_research.topicmodeling.io.json;

import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusJsonReader implements CorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusJsonReader.class);

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        LOGGER.info("Got a "
                + documentFactory.getClass().getCanonicalName()
                + " object as DocumentSupplier. But I'm a corpus reader and don't need such a supplier. ");
    }

    @Override
    public Corpus getCorpus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCorpus() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void deleteCorpus() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void readCorpus() {
        // TODO Auto-generated method stub
        
    }

}
