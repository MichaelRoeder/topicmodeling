package org.dice_research.topicmodeling.testframework;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

public class EvaluationTestCase extends AbstractTestCase {

	public EvaluationTestCase(ModelingAlgorithm algorithm) {
		super(algorithm, new IterationCountCondition(1));
	}

	@Override
	protected void performPreprocessing() {
		// nothing to do
	}

	@Override
	protected void initilializeAlgorithm() {
		// nothing to do
	}

	@Override
	protected Model performNextIterationStep() {
		return algorithm.getModel();
	}

	@Override
	protected void performEvaluations(Model model,
			ManagedEvaluationResultContainer results) {
		for (int i = 0; i < evaluators.size(); ++i) {
			results.addResult(evaluators.get(i).evaluateModel(model, results));
		}
	}
}
