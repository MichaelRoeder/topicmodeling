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

import java.util.Arrays;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DeterministicPercentageDocumentSheduler extends
		AbstractDocumentSheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(DeterministicPercentageDocumentSheduler.class);

	protected int percentages[];
	protected int remainingPercentages[];
	protected int lastPartThatGotDocument;

	public DeterministicPercentageDocumentSheduler(
			DocumentSupplier documentSource, int numberOfParts) {
		super(documentSource, numberOfParts);
		percentages = new int[numberOfParts];
		Arrays.fill(percentages, 0);
		percentages[numberOfParts - 1] = 100;
		remainingPercentages = new int[numberOfParts];
		Arrays.fill(remainingPercentages, 0);
		lastPartThatGotDocument = numberOfParts - 1;
	}

	public void setPercentageOfPart(int partId, int percentage) {
		if ((percentage < 0) || (percentage > 100)) {
			throw new IllegalArgumentException(percentage
					+ " is an illegal percentage.");
		}
		if ((partId < percentages.length - 1) && (partId >= 0)) {
			percentages[partId] = percentage;
			percentages[percentages.length - 1] -= percentage;
			if (percentages[percentages.length - 1] < 0) {
				logger.error("The sum of all percentages is "
						+ (100 + (-percentages[percentages.length - 1]))
						+ " which is higher than 100 and not allowed. So the parts will not be as large as expected.");
			}
		} else {
			if (partId == percentages.length - 1) {
				logger.info("Someone tried to set the percentage of the "
						+ partId
						+ ". part of the corpus. This percentage can not be set, because it will be calculated using the percentages of the other parts.");
			}
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
		int chosenPart = lastPartThatGotDocument;
		do {
			chosenPart = (chosenPart == (percentages.length - 1)) ? 0
					: chosenPart + 1;
			// If there are no more remaining percentages
			if ((chosenPart == lastPartThatGotDocument)
					&& (remainingPercentages[lastPartThatGotDocument] <= 0)) {
				System.arraycopy(percentages, 0, remainingPercentages, 0,
						percentages.length);
			}
		} while (remainingPercentages[chosenPart] <= 0);
		--remainingPercentages[chosenPart];
		return chosenPart;
	}
}
