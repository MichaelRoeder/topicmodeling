package org.aksw.simba.topicmodeling.io.gzip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.aksw.simba.topicmodeling.io.CorpusObjectReader;
import org.apache.commons.io.IOUtils;

public class GZipCorpusObjectReader extends CorpusObjectReader {

    public GZipCorpusObjectReader(File file) {
        super(file);
    }

    @Override
    protected void readCorpus(InputStream is) throws IOException, ClassNotFoundException {
        GZIPInputStream gin = null;
        try {
            gin = new GZIPInputStream(is);
            super.readCorpus(gin);
        } finally {
            IOUtils.closeQuietly(gin);
        }
    }

}
