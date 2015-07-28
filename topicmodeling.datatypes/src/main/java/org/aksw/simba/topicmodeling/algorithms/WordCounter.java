package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.evaluate.result.EvaluationResultDimension;

import com.carrotsearch.hppc.IntIntOpenHashMap;

/**
 * This class is used to count the word occurrences regarding the different
 * documents and the topics with which the occurrences have been marked. This
 * class was implemented to have a single central counting instance which can be
 * offered by the {@link ProbTopicModelingAlgorithmStateSupplier} and used by
 * different other classes.
 * 
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 * 
 */
public interface WordCounter {

    /**
     * The sum of all words in the corpus (N).
     * 
     * @return
     */
    public int getSumOfAllWords();

    /**
     * N_d
     * 
     * @param documentId
     * @return
     */
    public int getWordCountForDocument(int documentId);

    /**
     * N_d for all d in D.
     * 
     * @return
     */
    public int[] getWordCountsForDocumentsAsArray();

    /**
     * N_t
     * 
     * @param topicId
     * @return
     */
    public int getWordCountForTopic(int topicId);

    /**
     * N_t for all t in T.
     * 
     * @return
     */
    public int[] getWordCountsForTopics();

    /**
     * N_w
     * 
     * @param wordId
     * @return
     */
    public int getCountOfWord(int wordId);

    /**
     * N_w for all w in V.
     * 
     * @return
     */
    public int[] getCountsOfWords();

    /**
     * N_d_t
     * 
     * @param documentId
     * @param topicId
     * @return
     */
    public int getCountOfWordsInDocumentWithTopic(int documentId, int topicId);

    /**
     * N_d_t for all d in D.
     * 
     * @param topicId
     * @return
     */
    public int[] getCountsOfWordsInDocumentsWithTopicAsArray(int topicId);

    /**
     * N_d_t for all d in D.
     * 
     * @param topicId
     * @return
     */
    public IntIntOpenHashMap getCountOfWordsInDocumentWithTopicAsMap(int topicId);

    /**
     * N_w_t
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public int getCountOfWordAssignedToTopic(int wordId, int topicId);

    /**
     * N_w_t for all w in V.
     * 
     * @param topicId
     * @return
     */
    public int[] getCountsOfWordsAssignedToTopicAsArray(int topicId);

    /**
     * N_w_t for all t in T.
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public int[] getCountsWordWasAssignedToTopicsAsArray(int wordId);

    /**
     * N_w_t for all w in V.
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public IntIntOpenHashMap getCountsOfWordsAssignedToTopicAsMap(int topicId);

    /**
     * N_d_w
     * 
     * @param documentId
     * @param wordId
     * @return
     */
    public int getCountOfWordInDocument(int documentId, int wordId);

    /**
     * N_d_w_t
     * 
     * @param documentId
     * @param wordId
     * @param topicId
     * @return
     */
    public int getCountOfWordAssignedToTopicInDocument(int documentId, int wordId, int topicId);

    /**
     * N_d_w_t for all d in D.
     * 
     * @param wordId
     * @param topicId
     * @return int[] containing the number of occurrences of the given word
     *         assigned to the given topic. The length of the array is the
     *         number of documents in which the word w occurred and was assigned
     *         to the topic t.
     */
    public int[] getCountsOfWordAssignedToTopicInDocumentsAsArray(int wordId, int topicId);

    /**
     * N_d_w_t for all d in D.
     * 
     * @param wordId
     * @param topicId
     * @return IntIntOpenHashMap containing the number of occurrences of the
     *         given word assigned to the given topic. The Ids of the documents
     *         in which the given word occurred assigned to the given topic are
     *         the keys and the counts are the values of the Map.
     */
    public IntIntOpenHashMap getCountsOfWordAssignedToTopicInDocumentsAsMap(int wordId, int topicId);

    /**
     * P(w|t) = N_wt / N_t
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfWordInTopic(int wordId, int topicId);

    /**
     * P(t|d) = N_dt / N_d
     * 
     * @param documentId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopicForDocument(int documentId, int topicId);

    /**
     * P(t|w) = N_wt / N_w
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopicForWord(int wordId, int topicId);

    /**
     * Array of P(t|w) for all t in T.
     * 
     * @param wordId
     * @return
     */
    public double[] getRelativFrequenciesOfTopicsForWord(int wordId);

    /**
     * P(t) = N_t / N
     * 
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopic(int topicId);

    /**
     * P(t) = N_t / N for all t in T.
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfTopics();

    /**
     * P(d) = N_d / N
     * 
     * @return
     */
    public double getRelativFrequencyOfDocument(int documentId);

    /**
     * P(d) = N_d / N for all d in D.
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfDocuments();

    /**
     * p(w) = N_w / N
     * 
     * @param wordId
     * @return
     */
    public double getRelativFrequencyOfWord(int wordId);

    /**
     * p(w) = N_w / N for all w in V
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfWords();

    /**
     * Array of P(w|t) for all w in V.
     * 
     * @param topicId
     * @return
     */
    public double[] getRelativeFrequenciesOfWordsOfTopic(int topicId);

    /**
     * Releases all allocated objects.
     */
    public void clear();

    public int getCount(EvaluationResultDimension dimension, int id);

    public int getCount(EvaluationResultDimension dimension1, int id1, EvaluationResultDimension dimension2, int id2);

    public int getCount(EvaluationResultDimension dimension1, int id1, EvaluationResultDimension dimension2, int id2,
            EvaluationResultDimension dimension3, int id3);

    public int getDimensionSize(EvaluationResultDimension dimension);

    public double[] getProbabilitys(EvaluationResultDimension dimension);
}
