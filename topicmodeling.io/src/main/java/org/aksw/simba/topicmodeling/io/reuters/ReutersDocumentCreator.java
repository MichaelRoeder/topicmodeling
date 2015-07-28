package org.aksw.simba.topicmodeling.io.reuters;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.aksw.simba.topicmodeling.automaton.AutomatonCallback;
import org.aksw.simba.topicmodeling.automaton.BricsAutomatonManager;
import org.aksw.simba.topicmodeling.automaton.MultiPatternAutomaton;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.aksw.simba.topicmodeling.utils.doc.DocumentName;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReutersDocumentCreator implements AutomatonCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReutersDocumentCreator.class);

    private MultiPatternAutomaton automaton;
    private ReutersStringParser stringParser;
    private Deque<Document> queue;
    private String text;
    private List<String> categories = new ArrayList<String>();
    private Document currentDocument;

    /**
     * 0 - start state
     * 1 - found document
     * 2 - found topics
     * 3 - found title
     * 4 - found text (body)
     */
    private int state;

    private int dataStartPos;
    private StringBuilder currentData = new StringBuilder();

    public ReutersDocumentCreator(Deque<Document> queue) {
        this.automaton = new BricsAutomatonManager(this, new String[] { "\\<[^\\<\\>]*\\>" });
        this.queue = queue;
        this.stringParser = new ReutersStringParser();
    }

    public void createDocuments(String sgmlText) {
        text = sgmlText;
        state = 0;
        automaton.parseText(sgmlText);
    }

    @Override
    public void foundPattern(int patternId, int startPos, int length) {
        // tag found
        if (patternId == 0) {
            String tag = text.substring(startPos + 1, startPos + length - 1);
            if (tag.startsWith("REUTERS")) {
                if (state == 0) {
                    try {
                        int id = Integer.parseInt(tag.substring(tag.lastIndexOf("NEWID=\"") + 7, tag.length() - 1));
                        currentDocument = new Document(id);
                    } catch (Exception e) {
                        LOGGER.warn("Couldn't parse documentId.", e);
                        currentDocument = new Document();
                    }
                    state = 1;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("TOPICS")) {
                if (state == 1) {
                    categories.clear();
                    state = 2;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("/TOPICS")) {
                if (state == 2) {
                    currentDocument.addProperty(new DocumentMultipleCategories(categories.toArray(new String[categories
                            .size()])));
                    state = 1;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("D")) {
                if (state == 2) {
                    dataStartPos = startPos + length;
                }
            } else if (tag.equals("/D")) {
                if (state == 2) {
                    currentData.append(text.substring(dataStartPos, startPos));
                    categories.add(stringParser.parseString(currentData.toString()));
                    currentData.delete(0, currentData.length());
                }
            } else if (tag.equals("TITLE")) {
                if (state == 1) {
                    dataStartPos = startPos + length;
                    state = 3;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("/TITLE")) {
                if (state == 3) {
                    currentData.append(text.substring(dataStartPos, startPos));
                    currentDocument.addProperty(new DocumentName(stringParser.parseString(currentData.toString())));
                    currentData.delete(0, currentData.length());
                    state = 1;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("BODY")) {
                if (state == 1) {
                    dataStartPos = startPos + length;
                    state = 4;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("/BODY")) {
                if (state == 4) {
                    currentData.append(text.substring(dataStartPos, startPos));
                    currentDocument.addProperty(new DocumentText(stringParser.parseString(currentData.toString())));
                    currentData.delete(0, currentData.length());
                    state = 1;
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            } else if (tag.equals("/REUTERS")) {
                if (state == 1) {
                    state = 0;
                    queue.add(currentDocument);
                } else {
                    LOGGER.error("found \"" + tag + "\" but I'm in the wrong state (" + state + ")");
                }
            }
        }
    }
}
