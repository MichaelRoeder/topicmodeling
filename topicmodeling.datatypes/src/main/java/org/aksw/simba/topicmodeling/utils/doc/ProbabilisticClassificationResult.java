package org.aksw.simba.topicmodeling.utils.doc;

import java.util.Arrays;

import org.aksw.simba.topicmodeling.algorithms.Model;


public class ProbabilisticClassificationResult implements DocumentClassificationResult {

    private static final long serialVersionUID = -2800318151623415859L;

    protected double topicProbabilities[];
    protected int classId;
    protected Model model;
    protected int modelVersion;

    public ProbabilisticClassificationResult(double topicProbabilities[], Model model) {
        setTopicProbabilities(topicProbabilities);
        this.model = model;
        this.modelVersion = model.getVersion();
    }

    @Override
    public Object getValue() {
        return topicProbabilities;
    }

    @Override
    public int getClassId() {
        return classId;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public int getModelVersion() {
        return modelVersion;
    }

    public void setTopicProbabilities(double topicProbabilities[]) {
        this.topicProbabilities = topicProbabilities;
        double max = 0;
        classId = 0;
        for (int i = 0; i < topicProbabilities.length; ++i) {
            if (topicProbabilities[i] > max) {
                max = topicProbabilities[i];
                classId = i;
            }
        }
    }

    public double[] getTopicProbabilities() {
        return topicProbabilities;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ProbabilisticClassificationResult=\"(classId=");
        result.append(classId);
        result.append(", modelVersion=");
        result.append(modelVersion);
        result.append(", topicProbabilities=");
        result.append(Arrays.toString(topicProbabilities));
        result.append(")");
        return result.toString();
    }
}
