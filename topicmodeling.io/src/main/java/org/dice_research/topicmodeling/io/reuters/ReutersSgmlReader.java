/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io.reuters;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
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
