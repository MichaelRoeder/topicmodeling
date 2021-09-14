package org.dice_research.topicmodeling.evaluate;

import java.util.stream.IntStream;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;

public class ArunModelSelectionEvaluator extends AbstractEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArunModelSelectionEvaluator.class);

    public static final double LOG2 = Math.log(2);

    private ProbTopicModelingAlgorithmStateSupplier probAlgState;
    private boolean parallel = false;

    public ArunModelSelectionEvaluator(ProbTopicModelingAlgorithmStateSupplier probAlgState) {
        this.probAlgState = probAlgState;
    }

    public ArunModelSelectionEvaluator(ProbTopicModelingAlgorithmStateSupplier probAlgState, boolean parallel) {
        this.probAlgState = probAlgState;
        this.parallel = parallel;
    }

    @Override
    protected EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        int numberOfWords = probAlgState.getNumberOfWords();
        int numberOfTopics = probAlgState.getNumberOfTopics();
        int numberOfDocuments = probAlgState.getNumberOfDocuments();
        DoubleMatrix2D topicWordsMatrix = DoubleFactory2D.sparse.make(numberOfTopics, numberOfWords);
        DoubleMatrix2D documentTopicsMatrix = DoubleFactory2D.sparse.make(numberOfDocuments, numberOfTopics);
        double documentLengths[] = new double[numberOfDocuments];

        long time = System.currentTimeMillis();
        if (parallel) {
            fillMatrices(topicWordsMatrix, documentTopicsMatrix, documentLengths);
        } else {
            fillMatricesInParallel(topicWordsMatrix, documentTopicsMatrix, documentLengths);
        }
        LOGGER.info("Generating matrices took {}ms. (parallelization={})", (System.currentTimeMillis() - time),
                parallel);

//        /// TEST_START!
//        DoubleMatrix2D topicWordsMatrix2 = DoubleFactory2D.sparse.make(numberOfTopics, numberOfWords);
//        DoubleMatrix2D documentTopicsMatrix2 = DoubleFactory2D.sparse.make(numberOfDocuments, numberOfTopics);
//        double documentLengths2[] = new double[numberOfDocuments];
//        time = System.currentTimeMillis();
//        fillMatricesInParallel(topicWordsMatrix2, documentTopicsMatrix2, documentLengths2);
//        System.out.println(System.currentTimeMillis() - time);
//        for (int i = 0; i < documentLengths.length; ++i) {
//            if (documentLengths[i] != documentLengths2[i]) {
//                System.err.println("Different length for document " + i);
//            }
//        }
//        for (int d = 0; d < numberOfDocuments; ++d) {
//            for (int t = 0; t < numberOfTopics; ++t) {
//                if (documentTopicsMatrix.getQuick(d, t) != documentTopicsMatrix2.getQuick(d, t)) {
//                    System.err.println("Different document topic count for " + d + " " + t);
//                }
//
//            }
//        }
//        for (int t = 0; t < numberOfTopics; ++t) {
//            for (int w = 0; w < numberOfWords; ++w) {
//                if (topicWordsMatrix.getQuick(t, w) != topicWordsMatrix2.getQuick(t, w)) {
//                    System.err.println("Different topic word count for " + t + " " + w);
//                }
//
//            }
//        }
//        /// TEST_END!

        SingularValueDecomposition decomposition = new SingularValueDecomposition(
                topicWordsMatrix.rows() >= topicWordsMatrix.columns() ? topicWordsMatrix
                        : Algebra.DEFAULT.transpose(topicWordsMatrix));
        double singularValues[] = decomposition.getSingularValues();
        topicWordsMatrix = null;
        decomposition = null;

        DoubleMatrix2D docLenghtsVector = DoubleFactory2D.dense.make(new double[][] { documentLengths });
        DoubleMatrix2D cm2Matrix = Algebra.DEFAULT.mult(docLenghtsVector, documentTopicsMatrix);
        documentTopicsMatrix = null;
        DoubleMatrix1D cm2Vector = cm2Matrix.viewRow(0);
        normalize(cm2Vector);
        double normedCm2Vector[] = cm2Vector.toArray();
        return new SingleEvaluationResult("Arun", new Double(SymmetricKLDivergence(singularValues, normedCm2Vector)));
    }

    protected void fillMatrices(DoubleMatrix2D topicWordsMatrix, DoubleMatrix2D documentTopicsMatrix,
            double[] documentLengths) {
        int words[], topics[];
        for (int d = 0; d < documentLengths.length; ++d) {
            words = probAlgState.getWordsOfDocument(d);
            topics = probAlgState.getWordTopicAssignmentForDocument(d);
            for (int i = 0; i < words.length; ++i) {
                topicWordsMatrix.set(topics[i], words[i], topicWordsMatrix.getQuick(topics[i], words[i]) + 1);
                documentTopicsMatrix.set(d, topics[i], documentTopicsMatrix.getQuick(d, topics[i]) + 1);
            }
            documentLengths[d] = words.length;
        }
    }

    protected void fillMatricesInParallel(DoubleMatrix2D topicWordsMatrix, DoubleMatrix2D documentTopicsMatrix,
            double[] documentLengths) {
        // Fill the document topics matrix and document lengths array
        IntStream.range(0, documentLengths.length).parallel()
                .forEach(d -> handlePerDocumentData(d, documentTopicsMatrix, documentLengths));
        // Fill the topic word matrix
        IntStream.range(0, probAlgState.getNumberOfTopics()).parallel()
                .forEach(t -> handlePerTopicData(t, topicWordsMatrix));
    }

    private void handlePerDocumentData(int d, DoubleMatrix2D documentTopicsMatrix, double[] documentLengths) {
        int topics[] = probAlgState.getWordTopicAssignmentForDocument(d);
        // We can simply set the value since no other thread will access this cell
        documentLengths[d] = topics.length;
        // create topic histogram
        IntIntOpenHashMap topicCounts = new IntIntOpenHashMap(probAlgState.getNumberOfTopics());
        for (int i = 0; i < topics.length; ++i) {
            topicCounts.putOrAdd(topics[i], 1, 1);
        }
        // Ensure that no other thread works on the matrix
        synchronized (documentTopicsMatrix) {
            for (int i = 0; i < topicCounts.allocated.length; ++i) {
                if (topicCounts.allocated[i]) {
                    documentTopicsMatrix.setQuick(d, topicCounts.keys[i], topicCounts.values[i]);
                }
            }
        }
    }

    private void handlePerTopicData(int t, DoubleMatrix2D topicWordsMatrix) {
        // create a word histogram for the given topic
        IntIntOpenHashMap word2Topic = new IntIntOpenHashMap();
        IntStream.range(0, probAlgState.getNumberOfDocuments()).forEach(d -> {
            int words[] = probAlgState.getWordsOfDocument(d);
            int topics[] = probAlgState.getWordTopicAssignmentForDocument(d);
            for (int i = 0; i < topics.length; ++i) {
                if (topics[i] == t) {
                    word2Topic.putOrAdd(words[i], 1, 1);
                }
            }
        });
        // Ensure that no other thread works on the matrix while we add the histograms
        // data to the matrix
        synchronized (topicWordsMatrix) {
            for (int i = 0; i < word2Topic.allocated.length; ++i) {
                if (word2Topic.allocated[i]) {
                    topicWordsMatrix.setQuick(t, word2Topic.keys[i], word2Topic.values[i]);
                }
            }
        }
    }

    protected double SymmetricKLDivergence(double[] v1, double[] v2) {
        return calcKLDivergence(v1, v2) + calcKLDivergence(v2, v1);
    }

    protected double calcKLDivergence(double[] v1, double[] v2) {
        double sum = 0;
        for (int i = 0; i < v1.length; ++i) {
            if ((v1[i] > 0) && (v2[i] > 0)) {
                sum += v1[i] * Math.log(v1[i] / v2[i]);
            }
        }
        return sum / LOG2;
    }

    protected void normalize(DoubleMatrix1D cm2Vector) {
        double length = Algebra.DEFAULT.norm2(cm2Vector);
        for (int i = 0; i < cm2Vector.size(); ++i) {
            cm2Vector.setQuick(i, cm2Vector.getQuick(i) / length);
        }
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        // nothing to do
    }

}
