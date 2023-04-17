package org.dice_research.topicmodeling.testframework;

import java.util.Vector;

import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.evaluate.Evaluator;
import org.dice_research.topicmodeling.reporting.TestCaseListener;

public interface TestCaseInterface extends Runnable {

	public void addListener(TestCaseListener listener);

	public Vector<TestCaseListener> getListeners();

	public void addEvaluator(Evaluator evaluator);

	public void addEvaluator(Evaluator evaluator, int evaluateEveryNthIteration);

	public Vector<Evaluator> getEvaluators();

	public int getIterationCount();

	public ModelingAlgorithm getAlgorithm();

	public void setTestCaseName(String name);

	public String getTestCaseName();
}
