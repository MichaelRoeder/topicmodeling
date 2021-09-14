package org.dice_research.topicmodeling.reporting;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLogReporter implements TestCaseListener {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleLogReporter.class);

	/**
	 * @param neededTime
	 */
	public void reportPreprocessingFinished(long neededTime) {
		logger.info("Preprocessing finished after " + neededTime
				+ " milliseconds.");
	}

	@Override
	public void reportInitializationFinished(long neededTime) {
		logger.info("Initialization finished after " + neededTime
				+ " milliseconds.");
	}

	/**
	 * @param model
	 * @param neededTime
	 */
	public void reportAlgorithmFinished(Model model, long neededTime) {
		logger.info("Algorithm finished after " + neededTime + " milliseconds.");
	}

	@Override
	public void reportEvaluationResult(
			ManagedEvaluationResultContainer results, int iterationNumber,
			long neededTime) {
		String msg = "Got EvaluationResult #" + iterationNumber + " after "
				+ neededTime + " milliseconds.";
		String resultString = "";
		EvaluationResultCollection allResults[] = results.getAllResults();
		boolean isFirstValue = true;
		for (int i = 1; i < allResults.length; ++i) {
			if (allResults[i].size() > 0) {
				if (isFirstValue) {
					resultString = allResults[i].toString();
					isFirstValue = false;
				} else {
					resultString += " , " + allResults[i].toString();
				}
			}
		}
		if (resultString.length() > 0) {
			msg += " Results are " + resultString;
		}
		logger.info(msg);
	}
}
