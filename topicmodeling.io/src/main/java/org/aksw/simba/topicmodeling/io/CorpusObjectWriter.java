package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusObjectWriter implements CorpusWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusObjectWriter.class);

    protected File file;

    public CorpusObjectWriter(File file) {
        this.file = file;
    }

    public void writeCorpus(Corpus corpus) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            writeCorpus(corpus, fOut);
        } catch (Exception e) {
            LOGGER.error("Error while trying to write serialized corpus to file", e);
        } finally {
            IOUtils.closeQuietly(fOut);
        }
    }

    protected void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(corpus);
        } finally {
            IOUtils.closeQuietly(oOut);
        }
    }
}
