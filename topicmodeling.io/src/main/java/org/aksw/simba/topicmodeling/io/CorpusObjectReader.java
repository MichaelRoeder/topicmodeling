package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusObjectReader extends AbstractCorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusObjectReader.class);

    public CorpusObjectReader(File file) {
        super(file);
    }

    public void readCorpus() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            readCorpus(fin);
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized corpus from file", e);
        } finally {
            IOUtils.closeQuietly(fin);
        }
    }

    protected void readCorpus(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(is);
            corpus = (Corpus) oin.readObject();
        } finally {
            IOUtils.closeQuietly(oin);
        }
    }
}
