package org.aksw.simba.topicmodeling.io.reuters;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReutersSgmlReader extends AbstractDocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReutersSgmlReader.class);

    private static final String REUTERS_PATH = "/data/m.roeder/daten/Corpora/Reuters-21578/reut2-0";
    private static final String REUTERS_FILE_ENDING = ".sgm";
    private static final int MAX_SEGMENT_ID = 21;

    private Deque<Document> queue;
    private ReutersDocumentCreator documentCreator;
    private int nextSegmentId;

    public ReutersSgmlReader() {
        queue = new LinkedList<Document>();
        documentCreator = new ReutersDocumentCreator(queue);
    }

    @Override
    public Document getNextDocument() {
        Document document = queue.poll();
        if (document == null) {
            readNextSegment();
            document = queue.poll();
        }
        return document;
    }

    private void readNextSegment() {
        if (nextSegmentId <= MAX_SEGMENT_ID) {
            try {
                String segment = FileUtils.readFileToString(new File(REUTERS_PATH
                        + (nextSegmentId < 10 ? "0" + nextSegmentId : nextSegmentId) + REUTERS_FILE_ENDING));
                documentCreator.createDocuments(segment);
                ++nextSegmentId;
            } catch (IOException e) {
                LOGGER.error("Couldn't read segment " + nextSegmentId, e);
                e.printStackTrace();
            }
        }
    }
}