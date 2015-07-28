package org.aksw.simba.topicmodeling.evaluate.result;

public class EvaluationResultAsDoubleArray extends AbstractEvaluationResult implements
		OneDimensionalEvaluationResult<Double> {
	
	protected double result[];
	protected EvaluationResultDimension dimension;
	
	public EvaluationResultAsDoubleArray(String name, EvaluationResultDimension dimension, double result[]) {
		super(name);
		this.result = result;
		this.dimension = dimension;
	}
	
	public EvaluationResultAsDoubleArray(String name, EvaluationResultDimension dimension, int size) {
		super(name);
		this.result = new double[size];
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
	public void setResult(int index, Double value) {
		result[index] = value;
	}

	@Override
	public Double getResult(int index) {
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
