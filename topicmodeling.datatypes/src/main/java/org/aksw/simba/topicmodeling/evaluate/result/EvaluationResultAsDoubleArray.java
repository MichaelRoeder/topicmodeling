/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
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
