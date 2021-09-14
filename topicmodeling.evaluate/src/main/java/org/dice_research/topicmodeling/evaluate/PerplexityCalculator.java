package org.dice_research.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.ClassificationModel;
import org.dice_research.topicmodeling.algorithms.ProbabilisticWordTopicModel;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.doc.ProbabilisticClassificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerplexityCalculator extends AbstractEvaluatorWithClassifiedTestCorpus {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerplexityCalculator.class);

    public PerplexityCalculator(Corpus testCorpus) {
        super(testCorpus);
    }

    @Override
    public EvaluationResult evaluateModelWithClassifiedCorpus(ClassificationModel model,
            ManagedEvaluationResultContainer previousResults) {
        if (!(model instanceof ProbabilisticWordTopicModel)) {
            throw new IllegalArgumentException("Expected an instance of "
                    + ProbabilisticWordTopicModel.class.getCanonicalName() + " instead of "
                    + model.getClass().getCanonicalName());
        }
        ProbabilisticWordTopicModel probWTModel = (ProbabilisticWordTopicModel) model;
        DocumentWordCounts documentCounts;
        ProbabilisticClassificationResult classificationResult;
        int currentCount, numberOfWordsInCorpus = 0;
        double wordProbability, logDocumentProb, perplexity = 0;
        double topicProbForDocument[];
        // Go through the test corpus
        for (Document document : testCorpus) {
            documentCounts = document.getProperty(DocumentWordCounts.class);
            if (documentCounts == null) {
                LOGGER.error("Got a document without the needed DocumentWordCounts property. Ignoring this document.");
            } else {
                // classificationResult = document.getProperty(ProbabilisticClassificationResult.class);
                classificationResult = (ProbabilisticClassificationResult) classifications[document.getDocumentId()];
                if (classificationResult == null) {
                    LOGGER.error("Got a document without the needed ProbabilisticClassificationResult property. Ignoring this document.");
                } else {
                    topicProbForDocument = classificationResult.getTopicProbabilities();
                    logDocumentProb = 0;
                    for (int w = 0; w < documentCounts.getNumberOfWords(); ++w) {
                        currentCount = documentCounts.getCountForWord(w);
                        if (currentCount > 0) {
                            numberOfWordsInCorpus += currentCount;
                            wordProbability = 0;
                            // calculate the probability that this word has been created
                            for (int t = 0; t < probWTModel.getNumberOfTopics(); ++t) {
                                wordProbability += topicProbForDocument[t]
                                        * probWTModel.getSmoothedProbabilityOfWord(w, t);
                            }
                            logDocumentProb += currentCount * Math.log(wordProbability);
                        }
                    }
                    perplexity += logDocumentProb;
                }
            }
        }
        if (numberOfWordsInCorpus == 0) {
            LOGGER.error("Got an empty test corpus. The Perplexity will be undefined!");
            perplexity = Double.NaN;
        } else {
            // calculate how high the probability for the test documents is
            perplexity = Math.exp(-perplexity / numberOfWordsInCorpus);
        }

        return new SingleEvaluationResult("Perplexity", new Double(perplexity));
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        // nothing to do
    }
}
