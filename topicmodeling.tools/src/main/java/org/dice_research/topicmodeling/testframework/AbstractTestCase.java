package org.dice_research.topicmodeling.testframework;

import java.util.Vector;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.evaluate.Evaluator;
import org.dice_research.topicmodeling.evaluate.Stopwatch;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.reporting.TestCaseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTestCase implements TestCaseInterface {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTestCase.class);

    protected ModelingAlgorithm algorithm;
    protected Vector<Evaluator> evaluators = new Vector<Evaluator>();
    protected Vector<Integer> iterationCountsForEvaluators = new Vector<Integer>();
    protected Vector<TestCaseListener> listeners = new Vector<TestCaseListener>();
    protected Stopwatch stopwatch = new Stopwatch();
    protected int iterationCount;
    protected TerminationCondition termCondition;
    protected String testCaseName;

    public AbstractTestCase(ModelingAlgorithm algorithm, TerminationCondition termCondition) {
        this.algorithm = algorithm;
        this.termCondition = termCondition;
    }

    @Override
    public Vector<Evaluator> getEvaluators() {
        return evaluators;
    }

    @Override
    public void addEvaluator(Evaluator evaluator) {
        addEvaluator(evaluator, 1);
    }

    @Override
    public void addEvaluator(Evaluator evaluator, int evaluateEveryNthIteration) {
        evaluators.add(evaluator);
        iterationCountsForEvaluators.add(new Integer(evaluateEveryNthIteration));
    }

    @Override
    public Vector<TestCaseListener> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(TestCaseListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        logger.info("Testcase " + testCaseName + " started");
        stopwatch.reset();
        stopwatch.start();
        performPreprocessing();
        stopwatch.stop();
        reportPreprocessingFinished();

        stopwatch.start();
        initilializeAlgorithm();
        stopwatch.stop();
        reportInitializationFinished();

        ManagedEvaluationResultContainer results = new ManagedEvaluationResultContainer();
        Model model;
        do {
            results.clear();
            ++iterationCount;
            stopwatch.start();
            model = performNextIterationStep();
            stopwatch.stop();
            performEvaluations(model, results);
            reportAlgorithmPerformedStep(results);
        } while (!termCondition.hasFinished(this, results, stopwatch.getTime()));
        reportAlgorithmFinished(model);
        logger.info("Testcase " + testCaseName + " finished");
    }

    protected abstract void performPreprocessing();

    protected abstract void initilializeAlgorithm();

    protected abstract Model performNextIterationStep();

    protected void performEvaluations(Model model, ManagedEvaluationResultContainer results) {
        for (int i = 0; i < evaluators.size(); ++i) {
            if ((iterationCount % iterationCountsForEvaluators.get(i)) == 0) {
                results.addResult(evaluators.get(i).evaluateModel(model, results));
            }
        }
    }

    protected void reportPreprocessingFinished() {
        for (TestCaseListener listener : listeners) {
            listener.reportPreprocessingFinished(stopwatch.getTime());
        }
    }

    protected void reportInitializationFinished() {
        for (TestCaseListener listener : listeners) {
            listener.reportInitializationFinished(stopwatch.getTime());
        }
    }

    protected void reportAlgorithmPerformedStep(ManagedEvaluationResultContainer results) {
        for (TestCaseListener listener : listeners) {
            listener.reportEvaluationResult(results, iterationCount, stopwatch.getTime());
        }
    }

    protected void reportAlgorithmFinished(Model model) {
        for (TestCaseListener listener : listeners) {
            listener.reportAlgorithmFinished(model, stopwatch.getTime());
        }
    }

    public TerminationCondition getTermCondition() {
        return termCondition;
    }

    public void setTermCondition(TerminationCondition termCondition) {
        this.termCondition = termCondition;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    @Override
    public int getIterationCount() {
        return iterationCount;
    }

    public ModelingAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ModelingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String getTestCaseName() {
        return testCaseName;
    }

    @Override
    public void setTestCaseName(String name) {
        testCaseName = name;
    }
}
