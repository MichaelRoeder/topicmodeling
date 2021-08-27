package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.AbstractEvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvaluator implements Evaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEvaluator.class);

    private String resultAppendix = null;

    @Override
    public EvaluationResult evaluateModel(Model model, ManagedEvaluationResultContainer previousResults) {
        EvaluationResult result = evaluate(model, previousResults);
        if (resultAppendix != null) {
            renameResult(result);
        }
        return result;
    }

    private void renameResult(EvaluationResult result) {
        if (result instanceof EvaluationResultCollection) {
            renameContainer((EvaluationResultCollection) result);
        } else if (result instanceof AbstractEvaluationResult) {
            ((AbstractEvaluationResult) result).setResultName(result.getResultName() + resultAppendix);
        } else {
            LOGGER.warn("Couldn't rename result because of the unknown EvaluationResult type "
                    + result.getClass().getName());
        }
    }

    private void renameContainer(EvaluationResultCollection collection) {
        for (EvaluationResult result : collection) {
            renameResult(result);
        }
    }

    protected abstract EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults);

    @Override
    public void setResultNameAppendix(String appendix) {
        resultAppendix = appendix;
    }
}
