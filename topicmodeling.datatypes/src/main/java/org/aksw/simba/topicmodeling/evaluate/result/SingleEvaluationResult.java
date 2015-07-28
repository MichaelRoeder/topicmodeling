package org.aksw.simba.topicmodeling.evaluate.result;


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
