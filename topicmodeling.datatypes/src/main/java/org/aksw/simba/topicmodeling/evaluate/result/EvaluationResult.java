package org.aksw.simba.topicmodeling.evaluate.result;

public interface EvaluationResult {

    @Override
    public String toString();

    public String getResultName();

    public Object getResult();

    public String getResultAsString();
}
