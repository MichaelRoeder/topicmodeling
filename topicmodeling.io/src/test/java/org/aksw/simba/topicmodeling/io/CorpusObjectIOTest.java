package org.aksw.simba.topicmodeling.io;

import java.io.File;

public class CorpusObjectIOTest extends AbstractCorpusIOTest {

    private static final File CORPUS_FILE = generateTempFile(".object");

    public CorpusObjectIOTest() {
        super(new CorpusObjectReader(CORPUS_FILE), new CorpusObjectWriter(CORPUS_FILE), createTestCorpus());
    }
}
