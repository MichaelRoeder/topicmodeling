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
package org.dice_research.topicmodeling.evaluate.result;


public class SingleEvaluationResult extends AbstractEvaluationResult{
	
	protected Object result;
	
	public SingleEvaluationResult(String resultName) {
		super(resultName);
	}
	
	public SingleEvaluationResult(String resultName, Object result) {
		super(resultName);
		this.result = result;
	}

	@Override
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return name + "=" + result;
	}

	@Override
	public String getResultAsString() {
		return result.toString();
	}
}
