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
package org.dice_research.topicmodeling.algorithms;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;

/**
 * This interface extends the probabilistic word topic model by providing access
 * to the hyperparameters used in the Latent Dirichlet Allocation topic model.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public interface LDAModel extends ProbabilisticWordTopicModel {

    /**
     * Returns the value of the beta hyperparameter.
     * 
     * @return the value of the beta hyperparameter.
     */
    public double getBeta();

    /**
     * Returns the values of the alpha hyperparameter. If only a single alpha
     * has been used for all topics the array returned by this method might
     * contain only one single element. Otherwise the array contains a single
     * alpha for every single topic of the model.
     * 
     * @return
     */
    public double[] getAlphas();

    public int[] inferTopicAssignmentsForDocument(Document document);

    public int[] inferTopicAssignmentsForDocument(DocumentWordCounts wordCounts);

    public int[] inferTopicAssignmentsForDocument(int tokens[]);
}
