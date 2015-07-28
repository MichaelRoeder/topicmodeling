package org.aksw.simba.topicmodeling.io;

import java.io.File;

import org.aksw.simba.topicmodeling.io.xml.CorpusXmlReader;
import org.aksw.simba.topicmodeling.io.xml.CorpusXmlWriter;

public class CorpusXMLIOTest extends AbstractCorpusIOTest {

    // private static final String CORPUS_FILE =
    // "src/test/resources/test_corpus.xml";
    private static final File CORPUS_FILE = generateTempFile(".xml");

    public CorpusXMLIOTest() {
        super(new CorpusXmlReader(CORPUS_FILE), new CorpusXmlWriter(CORPUS_FILE), createTestCorpus());
    }
}
