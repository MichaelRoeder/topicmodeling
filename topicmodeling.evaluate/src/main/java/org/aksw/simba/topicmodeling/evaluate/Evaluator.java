package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

public interface Evaluator {

    /**
     * @return EvaluationResult
     * @param model
     */
    public EvaluationResult evaluateModel(Model model,
            ManagedEvaluationResultContainer previousResults);

    /**
     * Some of the evaluators need results of other evaluators. If the
     * reportProvisionalResults flag is set to true the evaluator will return
     * the results of all other called evaluators, too.
     * 
     * @param reportProvisionalResults
     */
    public void setReportProvisionalResults(boolean reportProvisionalResults);

    public void setResultNameAppendix(String appendix);
}
