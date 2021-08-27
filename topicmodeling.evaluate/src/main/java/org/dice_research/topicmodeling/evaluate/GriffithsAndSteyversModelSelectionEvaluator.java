package org.dice_research.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithm.mallet.MalletLdaWrapper;
import org.dice_research.topicmodeling.algorithms.LDAModel;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;

import com.carrotsearch.hppc.IntIntOpenHashMap;

import cc.mallet.types.Dirichlet;

/**
 * Implements the evaluation of the model described in Griffiths & Steyvers:
 * "Finding scientific topics".
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class GriffithsAndSteyversModelSelectionEvaluator extends AbstractEvaluator {

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(GriffithsAndSteyversModelSelectionEvaluator.class);

    private static final int NUMBER_OF_REPEATITIONS = 5;

    private MalletLdaWrapper ldaAlgorithm;
    private Corpus trainCorpus;
    private int numberOfRepeatitions = NUMBER_OF_REPEATITIONS;

    public GriffithsAndSteyversModelSelectionEvaluator(MalletLdaWrapper ldaAlgorithm, Corpus trainCorpus) {
        this.ldaAlgorithm = ldaAlgorithm;
        this.trainCorpus = trainCorpus;
    }

    protected int[][] classifyDocuments(LDAModel model, int[][] tokens) {
        int topicAssignments[][] = new int[tokens.length][];
        for (int documentId = 0; documentId < tokens.length; ++documentId) {
            topicAssignments[documentId] = model.inferTopicAssignmentsForDocument(tokens[documentId]);
        }
        return topicAssignments;
    }

    @Override
    protected EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        LDAModel ldaModel = (LDAModel) ldaAlgorithm.getModel();
        if (ldaModel != model) {
            throw new IllegalArgumentException(
                    "Expected the alread known instance of a LDA model. But got a different object of the class "
                            + model.getClass().getCanonicalName());
        }
        int tokens[][] = extractTokens(trainCorpus);

        double logProbabilities[] = new double[numberOfRepeatitions];
        int numberOfTopics = ldaModel.getNumberOfTopics();
        int numberOfWords = ldaModel.getVocabulary().size();
        double beta = ldaModel.getBeta();
        double constant = numberOfTopics
                * (Dirichlet.logGamma(numberOfWords * beta) - numberOfWords * Dirichlet.logGamma(beta));
        for (int i = 0; i < numberOfRepeatitions; ++i) {
            logProbabilities[i] = constant + calculateLogProbApproximation(tokens, classifyDocuments(ldaModel, tokens),
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
