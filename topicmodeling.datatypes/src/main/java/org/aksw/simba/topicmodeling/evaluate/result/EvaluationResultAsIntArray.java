package org.aksw.simba.topicmodeling.evaluate.result;

public class EvaluationResultAsIntArray extends AbstractEvaluationResult implements
		OneDimensionalEvaluationResult<Integer> {
	
	protected int result[];
	protected EvaluationResultDimension dimension;
	
	public EvaluationResultAsIntArray(String name, EvaluationResultDimension dimension, int result[]) {
		super(name);
		this.result = result;
		this.dimension = dimension;
	}
	
	public EvaluationResultAsIntArray(String name, EvaluationResultDimension dimension, int size) {
		super(name);
		this.result = new int[size];
		this.dimension = dimension;
	}

	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public EvaluationResultDimension getDimension() {
		return dimension;
	}

	@Override
	public int size() {
		return result.length;
	}

	@Override
	public void setResult(int index, Integer value) {
		result[index] = value;
	}

	@Override
	public Integer getResult(int index) {
		return result[index];
	}

	@Override
	public String getResultAsString() {
		String valueString = "Array[" + dimension.toString() + "]{";
		for (int i = 0; i < result.length; i++) {
			valueString += i == 0 ? result[0] : (", " + result[i]);
		}
		return valueString + "}";
	}
	
	@Override
	public String toString() {
		return "Array[" + dimension.toString() + "]";
	}
}
