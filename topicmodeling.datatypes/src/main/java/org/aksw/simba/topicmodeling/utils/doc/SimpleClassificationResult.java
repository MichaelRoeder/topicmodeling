package org.aksw.simba.topicmodeling.utils.doc;

import org.aksw.simba.topicmodeling.algorithms.Model;

public class SimpleClassificationResult implements DocumentClassificationResult {

    private static final long serialVersionUID = -5440660853166904862L;

    protected int classId;
    protected Model model;
    protected int modelVersion;

    public SimpleClassificationResult(int classId, Model model) {
        this.classId = classId;
        this.model = model;
        this.modelVersion = model.getVersion();
    }

    @Override
    public Object getValue() {
        return new Integer(classId);
    }

    @Override
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public int getModelVersion() {
        return modelVersion;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SimpleClassificationResult=\"(classId=");
        result.append(classId);
        result.append(", modelVersion=");
        result.append(modelVersion);
        result.append(")");
        return result.toString();
    }
}
