package org.dice_research.topicmodeling.testframework;

import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

/**
 * Class IterationCountCondition
 */
public class IterationCountCondition implements TerminationCondition {

	protected int maxIterations;

	public IterationCountCondition(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	/**
	 * Set the value of maxIterations
	 * 
	 * @param newVar
	 *            the new value of maxIterations
	 */
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	/**
	 * Get the value of maxIterations
	 * 
	 * @return the value of maxIterations
	 */
	public int getMaxIterations() {
		return maxIterations;
	}

	public boolean hasFinished(TestCaseInterface testCase, ManagedEvaluationResultContainer lastResults, long neededTime ) {
		return testCase.getIterationCount() >= maxIterations;
	}

}
