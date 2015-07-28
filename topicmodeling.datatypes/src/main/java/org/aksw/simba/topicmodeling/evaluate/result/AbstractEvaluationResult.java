package org.aksw.simba.topicmodeling.evaluate.result;

public abstract class AbstractEvaluationResult implements EvaluationResult{

	protected String name;
	
	public AbstractEvaluationResult(String name) {
		this.name = name;
	}
	
	@Override
	public String getResultName() {
		return name;
	}
	
	public void setResultName(String name) {
		this.name = name;
	}
}
