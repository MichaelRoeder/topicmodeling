package org.dice_research.topicmodeling.reporting;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.io.ModelWriter;

public class SimpleModelReporter implements TestCaseListener {
	
	protected ModelWriter modelWriter;
	
	public SimpleModelReporter(ModelWriter modelWriter) {
		this.modelWriter = modelWriter;
	}

	@Override
	public void reportPreprocessingFinished(long neededTime) {
		// nothing to do
	}

	@Override
	public void reportInitializationFinished(long neededTime) {
		// nothing to do
	}

	@Override
	public void reportAlgorithmFinished(Model model, long neededTime) {
		modelWriter.writeModelToFiles(model);
	}

	@Override
	public void reportEvaluationResult(ManagedEvaluationResultContainer results,
			int iterationNumber, long neededTime) {
		// nothing to do
	}
}
