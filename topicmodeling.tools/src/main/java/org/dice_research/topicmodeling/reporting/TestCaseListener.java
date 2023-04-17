package org.dice_research.topicmodeling.reporting;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

/**
 * Interface TestCaseListener
 */
public interface TestCaseListener {

	/**
	 * @param neededTime
	 */
	public void reportPreprocessingFinished(long neededTime);

	/**
	 * @param neededTime
	 */
	public void reportInitializationFinished(long neededTime);

	/**
	 * @param result
	 *            the result of the evaluation of the current model
	 * @param neededTime
	 *            the time that the algorithm needed to calculate this result
	 */
	public void reportEvaluationResult(ManagedEvaluationResultContainer results, int iterationNumber, long neededTime);

	/**
	 * @param model
	 * @param neededTime
	 */
	public void reportAlgorithmFinished(Model model, long neededTime);

}
