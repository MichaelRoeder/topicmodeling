package org.aksw.simba.topicmodeling.preprocessing.shedule;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NFoldCrossValidationSheduler extends AbstractDocumentSheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NFoldCrossValidationSheduler.class);

    public static final int TRAIN_DOCUMENTS_SUPPLIER_ID = 0;
    public static final int TEST_DOCUMENTS_SUPPLIER_ID = 1;

    private int numberOfFolds;
    private int currentState;

    public NFoldCrossValidationSheduler(DocumentSupplier documentSource, int numberOfFolds, int runId) {
        super(documentSource, 2);
        this.numberOfFolds = numberOfFolds;
        if (runId >= numberOfFolds) {
            currentState = runId % numberOfFolds;
            LOGGER.warn("Got the runId " + runId + " but there are only " + numberOfFolds + " folds. Using the runId "
                    + runId + " instead.");
        } else {
            currentState = runId;
        }
    }

    @Override
    protected Document getNextDocument(int partId) {
        Document document = documentSource.getNextDocument();
        if (document != null) {
            int partIdForDocument = getNextPartId();

            while ((document != null) && (partId != partIdForDocument)) {
                // If this document is not in the part of the corpus which asks
                // for a new document add the document to the queue of the part
                // it belongs to and get a new document
                listOfParts[partIdForDocument].addDocumentToQueue(document);
                document = documentSource.getNextDocument();
                partIdForDocument = getNextPartId();
            }
        }
        return document;
    }

    protected int getNextPartId() {
        ++currentState;
        if (currentState == numberOfFolds) {
            currentState = 0;
            return TEST_DOCUMENTS_SUPPLIER_ID;
        } else {
            return TRAIN_DOCUMENTS_SUPPLIER_ID;
        }
    }

}
