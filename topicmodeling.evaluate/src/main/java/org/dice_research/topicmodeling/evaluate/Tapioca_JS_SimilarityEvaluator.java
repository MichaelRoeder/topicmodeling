package org.dice_research.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.utils.corpus.Corpus;

/**
 * DEVELOPED FOR TOPICS4LOD. Shouldn't be used for other projects.
 * 
 * Uses Jensen Shannon divergence.
 * 
 * @author m.roeder
 * 
 */
public class Tapioca_JS_SimilarityEvaluator extends Tapioca_SimilarityEvaluator {

    public static final String SIMILARITY_OUTPUT_FILE_NAME = "dataset_JS_similarities.csv";

    private static final double LOG2 = Math.log(2.0);

    protected static final int NUMNER_OF_TOPIC_INFERENCES = 1;

    public Tapioca_JS_SimilarityEvaluator(Corpus trainCorpus, Corpus testCorpus,
            ProbTopicModelingAlgorithmStateSupplier probAlgState, String outputFolder) {
        super(trainCorpus, testCorpus, probAlgState, outputFolder);
        this.outputFileName = SIMILARITY_OUTPUT_FILE_NAME;
    }

    public Tapioca_JS_SimilarityEvaluator(Corpus trainCorpus, Corpus testCorpus,
            ProbTopicModelingAlgorithmStateSupplier probAlgState) {
        super(trainCorpus, testCorpus, probAlgState);
        this.outputFileName = SIMILARITY_OUTPUT_FILE_NAME;
    }

    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        return -getJensenShannonDivergence(vector1, vector2);
    }

    protected double getJensenShannonDivergence(double dist1[], double dist2[]) {
        double centroid[] = new double[dist1.length];
        for (int i = 0; i < dist1.length; ++i) {
            centroid[i] = (dist1[i] + dist2[i]) / 2.0;
        }
        return (getKulbackLeiblerDivergence(dist1, centroid) + getKulbackLeiblerDivergence(dist2, centroid)) / 2;
    }

    protected double getKulbackLeiblerDivergence(double dist1[], double dist2[]) {
        double sum = 0;
        for (int i = 0; i < dist1.length; ++i) {
            if (dist1[i] > 0) {
                sum += dist1[i] * (Math.log(dist1[i] / dist2[i]) / LOG2);
            }
        }
        return sum;
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        // nothing to do
    }
}
