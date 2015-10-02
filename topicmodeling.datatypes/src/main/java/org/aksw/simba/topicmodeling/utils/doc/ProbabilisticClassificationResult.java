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
