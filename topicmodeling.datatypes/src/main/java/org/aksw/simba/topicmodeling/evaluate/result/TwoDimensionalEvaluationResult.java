package org.aksw.simba.topicmodeling.evaluate.result;

public interface TwoDimensionalEvaluationResult<T> extends EvaluationResult {

	public EvaluationResultDimension getDimension1();

	public EvaluationResultDimension getDimension2();

	public int getDimension1Size();

	public int getDimension2Size();

	public void setResult(int x, int y, T value);

	public T getResult(int x, int y);
}
