package org.dice_research.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.ClassificationModel;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.utils.corpus.ClassifiedTestCorpus;

@Deprecated
public class ClassificationEvaluator extends AbstractEvaluatorWithClassifiedTestCorpus { //AbstractEvaluatorWithTestCorpus<ClassifiedTestCorpus> {

	public ClassificationEvaluator(ClassifiedTestCorpus testCorpus) {
		super(testCorpus);
	}

	@Override
	public EvaluationResult evaluateModelWithClassifiedCorpus(
			ClassificationModel model, ManagedEvaluationResultContainer previousResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReportProvisionalResults(boolean reportProvisionalResults) {
		// TODO Auto-generated method stub
		
	}
}
