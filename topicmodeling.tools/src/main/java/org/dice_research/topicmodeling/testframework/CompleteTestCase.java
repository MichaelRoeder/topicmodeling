package org.dice_research.topicmodeling.testframework;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.lang.Language;
import org.dice_research.topicmodeling.preprocessing.Preprocessor;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.Corpus;

public class CompleteTestCase extends AbstractTestCase {
    protected Preprocessor preprocessor;
    protected DocumentSupplier documentSupplier;
    protected Corpus corpus;
    protected Language language;

    public CompleteTestCase(ModelingAlgorithm algorithm,
            DocumentSupplier documentSupplier, Language language, TerminationCondition termCondition) {
        super(algorithm, termCondition);
        this.documentSupplier = documentSupplier;
        this.language = language;
    }

    public CompleteTestCase(ModelingAlgorithm algorithm, Preprocessor preprocessor, TerminationCondition termCondition) {
        super(algorithm, termCondition);
        this.preprocessor = preprocessor;
    }

    @Deprecated
    public CompleteTestCase(ModelingAlgorithm algorithm, Preprocessor preprocessor,
            DocumentSupplier documentSupplier, TerminationCondition termCondition) {
        super(algorithm, termCondition);
        this.documentSupplier = documentSupplier;
        this.preprocessor = preprocessor;
    }

    // public void run() {
    // stopwatch.reset();
    // stopwatch.start();
    // Corpus corpus = performPreprocessing();
    // stopwatch.stop();
    // reportPreprocessingFinished();
    //
    // stopwatch.start();
    // algorithm.initialize(corpus);
    // stopwatch.stop();
    // reportInitializationFinished();
    //
    // ManagedEvaluationResultContainer results = new ManagedEvaluationResultContainer();
    // do {
    // results.clear();
    // ++iterationCount;
    // stopwatch.start();
    // algorithm.performNextStep();
    // stopwatch.stop();
    // for(int i = 0; i < evaluators.size(); ++i) {
    // if((iterationCount % iterationCountsForEvaluators.get(i)) == 0) {
    // results.addResult(evaluators.get(i).evaluateModel(algorithm.getModel(), results));
    // }
    // }
    // reportAlgorithmPerformedStep(results);
    // } while (!termCondition.hasFinished(this, results,
    // stopwatch.getTime()));
    // reportAlgorithmFinished();
    // }

    @Override
    protected void performPreprocessing() {
        if (preprocessor == null) {
            preprocessor = algorithm.createPreprocessor(documentSupplier, language);
        }
        if (documentSupplier != null) {
            preprocessor.addDocuments(documentSupplier);
        }
        corpus = preprocessor.getCorpus();
    }

    @Override
    protected void initilializeAlgorithm() {
        algorithm.initialize(corpus);
        // corpus isn't needed anymore
        corpus = null;
    }

    @Override
    protected Model performNextIterationStep() {
        algorithm.performNextStep();
        return algorithm.getModel();
    }

    public DocumentSupplier getDocumentSupplier() {
        return documentSupplier;
    }

    public void setDocumentSupplier(DocumentSupplier documentSupplier) {
        this.documentSupplier = documentSupplier;
    }
}
