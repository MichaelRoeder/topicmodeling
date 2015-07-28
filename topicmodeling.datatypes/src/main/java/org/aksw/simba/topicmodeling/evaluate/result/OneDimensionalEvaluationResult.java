package org.aksw.simba.topicmodeling.evaluate.result;

public interface OneDimensionalEvaluationResult<T> extends EvaluationResult {

	public EvaluationResultDimension getDimension();

	public int size();

	public void setResult(int index, T value);

	public T getResult(int index);
}
