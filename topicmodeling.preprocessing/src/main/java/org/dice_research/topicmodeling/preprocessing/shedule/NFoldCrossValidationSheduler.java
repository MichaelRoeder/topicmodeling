/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.shedule;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
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
