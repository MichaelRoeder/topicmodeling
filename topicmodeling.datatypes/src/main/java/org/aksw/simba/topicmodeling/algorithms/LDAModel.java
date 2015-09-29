package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;

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
