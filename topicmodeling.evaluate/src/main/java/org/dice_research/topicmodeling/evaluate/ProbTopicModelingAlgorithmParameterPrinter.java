package org.dice_research.topicmodeling.evaluate;

import java.util.stream.IntStream;

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
        int documents = stateSupplier.getNumberOfDocuments();
        results.addResult(new SingleEvaluationResult("numberOfDocuments", documents));
        results.addResult(new SingleEvaluationResult("usedSeed", stateSupplier.getSeed()));
        results.addResult(new SingleEvaluationResult("vocabularySize", stateSupplier.getVocabulary().size()));
        results.addResult(new SingleEvaluationResult("wordTokens",
                IntStream.range(0, documents).map(id -> stateSupplier.getWordsOfDocument(id).length).sum()));

        return results;
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        // nothing to do
    }
}
