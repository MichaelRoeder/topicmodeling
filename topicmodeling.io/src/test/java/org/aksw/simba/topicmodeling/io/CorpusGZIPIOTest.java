package org.aksw.simba.topicmodeling.io;

import java.io.File;

import org.aksw.simba.topicmodeling.io.gzip.GZipCorpusObjectReader;
import org.aksw.simba.topicmodeling.io.gzip.GZipCorpusObjectWriter;

public class CorpusGZIPIOTest extends AbstractCorpusIOTest {

    private static final File CORPUS_FILE = generateTempFile(".object");

    public CorpusGZIPIOTest() {
        super(new GZipCorpusObjectReader(CORPUS_FILE), new GZipCorpusObjectWriter(CORPUS_FILE), createTestCorpus());
    }
}
