package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;

public class ProbTopicModelingAlgorithmParameterPrinter extends AbstractEvaluator {
	
	protected ProbTopicModelingAlgorithmStateSupplier stateSupplier;
	
	public ProbTopicModelingAlgorithmParameterPrinter(ProbTopicModelingAlgorithmStateSupplier stateSupplier) {
		this.stateSupplier = stateSupplier;
	}

	@Override
	public EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
		EvaluationResultCollection results = new EvaluationResultCollection();
		results.addResult(new SingleEvaluationResult("numberOfTopics", stateSupplier.getNumberOfTopics()));
		results.addResult(new SingleEvaluationResult("numberOfDocuments", stateSupplier.getNumberOfDocuments()));
		results.addResult(new SingleEvaluationResult("usedSeed", stateSupplier.getSeed()));
		results.addResult(new SingleEvaluationResult("vocabularySize", stateSupplier.getVocabulary().size()));
		return results;
	}

	@Override
	public void setReportProvisionalResults(boolean reportProvisionalResults) {
		// nothing to do
	}
}
