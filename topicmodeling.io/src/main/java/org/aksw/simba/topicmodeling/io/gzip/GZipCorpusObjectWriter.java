package org.aksw.simba.topicmodeling.io.gzip;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.aksw.simba.topicmodeling.io.CorpusObjectWriter;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.apache.commons.io.IOUtils;

public class GZipCorpusObjectWriter extends CorpusObjectWriter {

    public GZipCorpusObjectWriter(File file) {
        super(file);
    }

    @Override
    protected void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        GZIPOutputStream gout = null;
        try {
            gout = new GZIPOutputStream(out);
            super.writeCorpus(corpus, gout);
        } finally {
            IOUtils.closeQuietly(gout);
        }
    }

}
