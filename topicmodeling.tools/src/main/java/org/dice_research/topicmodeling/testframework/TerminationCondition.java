package org.dice_research.topicmodeling.testframework;

import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;


/**
 * Interface TerminationCondition
 */
public interface TerminationCondition {

	public boolean hasFinished(TestCaseInterface testCase, ManagedEvaluationResultContainer lastResults, long neededTime );
}
