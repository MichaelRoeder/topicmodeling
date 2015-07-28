package org.aksw.simba.topicmodeling.evaluate.result;

public class EvaluationResultAsDoubleMatrix extends AbstractEvaluationResult
		implements TwoDimensionalEvaluationResult<Double> {

	protected double result[][];
	protected EvaluationResultDimension dimension1;
	protected EvaluationResultDimension dimension2;

	public EvaluationResultAsDoubleMatrix(String name,
			EvaluationResultDimension dim1, EvaluationResultDimension dim2,
			double result[][]) {
		super(name);
		this.result = result;
		dimension1 = dim1;
		dimension2 = dim2;
	}

	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public EvaluationResultDimension getDimension1() {
		return dimension1;
	}

	@Override
	public EvaluationResultDimension getDimension2() {
		return dimension2;
	}

	@Override
	public int getDimension1Size() {
		return result.length;
	}

	@Override
	public int getDimension2Size() {
		if (result.length == 0) {
			return 0;
		} else {
			return result[0].length;
		}
	}

	@Override
	public void setResult(int x, int y, Double value) {
		result[x][y] = value;
	}

	@Override
	public Double getResult(int x, int y) {
		return result[x][y];
	}

	@Override
	public String getResultAsString() {
		String valueString = "Matrix[" + dimension1.toString() + "][" + dimension2.toString() + "]{";
		for (int i = 0; i < result.length; i++) {
			valueString += i == 0 ? "{" : "}, {";
			for (int j = 0; j < result[i].length; j++) {
				valueString += j == 0 ? result[i][0] : (", " + result[i][j]);
			}
		}
		return valueString + "}}";
	}

	@Override
	public String toString() {
		return "Matrix[" + dimension1.toString() + "][" + dimension2.toString() + "]";
	}
}
