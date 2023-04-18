package org.dice_research.topicmodeling.evaluate;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.dice_research.topicmodeling.algorithm.mallet.MalletLdaWrapper;
import org.dice_research.topicmodeling.algorithms.LDAModel;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;
import org.dice_research.topicmodeling.io.LodcatProbTopicModelingAlgorithmStateReader;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;

import com.carrotsearch.hppc.IntIntOpenHashMap;

import cc.mallet.types.Dirichlet;

/**
 * Implements the evaluation of the model described in Griffiths & Steyvers:
 * "Finding scientific topics". It calculates P(w|T) where w is the set of all
 * given words (i.e., all documents) and T are the topics of the model that
 * should be evaluated. This probability is intractable since all combinations
 * of topic assignments would have to be calculated. Hence, Griffiths & Steyvers
 * propose to sample the topic assignments N times to get P(Z|w,T). Based on the
 * assignments, the
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class GriffithsAndSteyversModelSelectionEvaluator extends AbstractEvaluator {

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(GriffithsAndSteyversModelSelectionEvaluator.class);

    private static final int NUMBER_OF_REPEATITIONS = 5;

    private static final int NUMBER_OF_INFERENCE_ITERATIONS = 700;

    private ProbTopicModelingAlgorithmStateSupplier ldaAlgorithm;
    private LDAModel ldaModel;
    private Corpus trainCorpus;
    private int numberOfRepeatitions = NUMBER_OF_REPEATITIONS;

    /**
     * Constructor. Called without an additional training corpus, the documents of
     * the model's state will be used.
     * 
     * @param ldaAlgorithm the LDA algorithm that comprises the model and that
     *                     should be evaluated
     */
    public GriffithsAndSteyversModelSelectionEvaluator(ProbTopicModelingAlgorithmStateSupplier ldaAlgorithm) {
        this.ldaAlgorithm = ldaAlgorithm;
        if (ldaAlgorithm instanceof MalletLdaWrapper) {
            this.ldaModel = (LDAModel) ((MalletLdaWrapper) ldaAlgorithm).getModel();
        } else {
            this.ldaModel = null;
        }
        this.trainCorpus = null;
    }

    /**
     * Constructor.
     * 
     * @param ldaAlgorithm the LDA algorithm that comprises the model and that
     *                     should be evaluated
     * @param trainCorpus  the train corpus for which the probabilities should be
     *                     calculated
     */
    public GriffithsAndSteyversModelSelectionEvaluator(MalletLdaWrapper ldaAlgorithm, Corpus trainCorpus) {
        this.ldaAlgorithm = ldaAlgorithm;
        this.ldaModel = (LDAModel) ldaAlgorithm.getModel();
        this.trainCorpus = trainCorpus;
    }

    protected int[][] classifyDocuments(int[][] tokens) {
//        int topicAssignments[][] = new int[tokens.length][];
//        for (int documentId = 0; documentId < tokens.length; ++documentId) {
//            topicAssignments[documentId] = model.inferTopicAssignmentsForDocument(tokens[documentId]);
//        }
//        return topicAssignments;
        IntStream documentIds = IntStream.range(0, tokens.length).parallel();
        Stream<int[]> documentWordTopics;
        if (ldaModel != null) {
            documentWordTopics = documentIds.mapToObj(d -> ldaModel.inferTopicAssignmentsForDocument(tokens[d]));
        } else {
            documentWordTopics = documentIds.mapToObj(ldaAlgorithm::getWordTopicAssignmentForDocument);
        }
        return documentWordTopics.toArray(int[][]::new);
    }

    @Override
    protected EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        if (ldaModel != model) {
            throw new IllegalArgumentException(
                    "Expected the alread known instance of a LDA model. But got a different object of the class "
                            + model.getClass().getCanonicalName());
        }
        if (ldaModel != null) {
            ldaModel.setInferenceIterations(NUMBER_OF_INFERENCE_ITERATIONS);
        }
        // Get tokens (i.e., words per document)
        int tokens[][] = null;
        if (trainCorpus != null) {
            tokens = extractTokens(trainCorpus);
        } else {
            tokens = extractTokens(ldaAlgorithm);
        }

        double logProbabilities[] = new double[numberOfRepeatitions];
        int numberOfTopics = ldaAlgorithm.getNumberOfTopics();
        int numberOfWords = ldaAlgorithm.getVocabulary().size();
        double beta;
        if (ldaModel != null) {
            beta = ldaModel.getBeta();
        } else {
            beta = ((LodcatProbTopicModelingAlgorithmStateReader.LodcatProbTopicModelingAlgorithmStateSupplier) ldaAlgorithm).getBeta();
        }
        double constant = numberOfTopics
                * (Dirichlet.logGamma(numberOfWords * beta) - numberOfWords * Dirichlet.logGamma(beta));
        for (int i = 0; i < numberOfRepeatitions; ++i) {
            logProbabilities[i] = constant + calculateLogProbApproximation(tokens, classifyDocuments(tokens),
                    numberOfTopics, beta, numberOfWords);
        }

        return new SingleEvaluationResult("P(w|T)", new Double(harmonicMean(logProbabilities)));
    }

    protected double calculateLogProbApproximation(int tokens[][], int topicAssignments[][], int numberOfTopics,
            double beta, int numberOfWords) {
        IntIntOpenHashMap wordsAssignedToTopics[] = new IntIntOpenHashMap[numberOfTopics];
        for (int i = 0; i < numberOfTopics; ++i) {
            wordsAssignedToTopics[i] = new IntIntOpenHashMap();
        }
        int wordsWithTopic[] = new int[numberOfTopics];
        int topicId;
        for (int i = 0; i < topicAssignments.length; ++i) {
            for (int j = 0; j < topicAssignments[i].length; ++j) {
                topicId = topicAssignments[i][j];
                ++wordsWithTopic[topicId];
                wordsAssignedToTopics[topicId].putOrAdd(tokens[i][j], 1, 1);
            }
        }

        final double NUMBER_OF_WORDS_TIMES_BETA = numberOfWords * beta;
        final double LOG_GAMMA_OF_BETA = Dirichlet.logGamma(beta);
        double sum = 0;
        double temp;
        IntIntOpenHashMap wordsAssignedToTopic;
        for (int t = 0; t < numberOfTopics; ++t) {
            temp = 0;
            wordsAssignedToTopic = wordsAssignedToTopics[t];
            for (int w = 0; w < numberOfWords; ++w) {
                if (wordsAssignedToTopic.containsKey(w)) {
                    temp += Dirichlet.logGamma(wordsAssignedToTopic.lget() + beta);
                } else {
                    temp += LOG_GAMMA_OF_BETA;
                }
            }
            sum += temp - Dirichlet.logGamma(wordsWithTopic[t] + NUMBER_OF_WORDS_TIMES_BETA);
        }
        return sum;
    }

    protected int[][] extractTokens(Corpus corpus) {
        int tokens[][] = new int[corpus.getNumberOfDocuments()][];
        int docId = 0;
        for (Document document : corpus) {
            DocumentTextWordIds ids = document.getProperty(DocumentTextWordIds.class);
            if (ids == null) {
                DocumentWordCounts wordCounts = document.getProperty(DocumentWordCounts.class);
                if (wordCounts == null) {
                    throw new IllegalArgumentException("Expected a Document with the a " + DocumentTextWordIds.class
                            + " or a " + DocumentWordCounts.class + " property.");
                } else {
                    tokens[docId] = DocumentTextWordIds.fromSummedWordCounts(wordCounts).getWordIds();
                }
            } else {
                tokens[docId] = ids.getWordIds();
            }
            ++docId;
        }
        return tokens;
    }

    protected int[][] extractTokens(ProbTopicModelingAlgorithmStateSupplier ldaAlgorithm) {
        int tokens[][] = new int[ldaAlgorithm.getNumberOfDocuments()][];
        for (int d = 0; d < tokens.length; ++d) {
            tokens[d] = ldaAlgorithm.getWordsOfDocument(d);
        }
        return tokens;
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        // nothing to do
    }

    public static double harmonicMean(double values[]) {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += 1.0 / values[i];
        }
        return values.length / sum;
    }
}
