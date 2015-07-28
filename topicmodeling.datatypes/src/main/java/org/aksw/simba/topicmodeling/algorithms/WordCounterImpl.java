package org.aksw.simba.topicmodeling.algorithms;


import java.util.Arrays;
import java.util.Iterator;

import org.aksw.simba.topicmodeling.evaluate.result.EvaluationResultDimension;

import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.cursors.IntIntCursor;

/**
 * This class is used to count the word occurrences regarding the different documents and the topics with which the
 * occurrences have been marked. This class was implemented to have a single central counting instance which can be
 * offered by the {@link ProbTopicModelingAlgorithmStateSupplier} and used by different other classes.
 * 
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 * 
 */
public class WordCounterImpl implements WordCounter {

    /**
     * Used to get the corpus and the single word occurrences marked with the topics. Note that one shouldn't try to
     * call {@link ProbTopicModelingAlgorithmStateSupplier#getWordCounts()} because the method could return this
     * {@link WordCounterImpl} object.
     */
    protected ProbTopicModelingAlgorithmStateSupplier probAlgState;

    /**
     * Map to count the occurrence of a word w in a document d marked with the topic t.
     * 
     * indexed: .get(t).get(w).get(d) = N_w_d_t
     */
    protected ObjectArrayList<IntObjectOpenHashMap<IntIntOpenHashMap>> CountWordInDocumentWithTopic;

    /**
     * Map to count the occurrence of a word w in a document d.
     * 
     * indexed: .get(t).get(d) = N_d_t
     */
    protected ObjectArrayList<IntIntOpenHashMap> sumWordsWithTopicInDocument;

    /**
     * Map to count the occurrence of a word w marked with the topic t.
     * 
     * indexed: .get(t).get(w) = N_w_t
     */
    protected ObjectArrayList<IntIntOpenHashMap> sumTimesWordGotTopic;

    /**
     * Number of word occurrences marked with the topic t over all words (N_t).
     */
    protected int sumWordsWithTopic[];

    /**
     * Number of word occurrences (N_w).
     */
    protected int sumWordCounts[];

    /**
     * Sum of all word occurrences in the whole corpus (N).
     */
    protected int sumWords;

    /**
     * Flag indicating that the words have been counted
     */
    protected boolean wordsCounted = false;

    protected double documentProbabilities[];
    protected double topicProbabilities[];
    protected double wordProbabilities[];

    /**
     * Constructor.
     * 
     * @param probAlgState
     *            used to get the corpus and the single word occurrences marked with the topics.
     */
    public WordCounterImpl(ProbTopicModelingAlgorithmStateSupplier probAlgState) {
        this.probAlgState = probAlgState;
    }

    /**
     * Creates the needed arrays and maps needed for counting the words.
     */
    protected void instantiateCounter() {
        int numberOfTopics = probAlgState.getNumberOfTopics();
        // indexed: .get(t).get(w).get(d) = N_w_d_t
        CountWordInDocumentWithTopic = new ObjectArrayList<IntObjectOpenHashMap<IntIntOpenHashMap>>(numberOfTopics);
        // indexed: .get(t).get(d) = N_d_t
        sumWordsWithTopicInDocument = new ObjectArrayList<IntIntOpenHashMap>(numberOfTopics);
        // indexed: .get(t).get(w) = N_w_t
        sumTimesWordGotTopic = new ObjectArrayList<IntIntOpenHashMap>(numberOfTopics);
        sumWordsWithTopic = new int[numberOfTopics];
        sumWordCounts = new int[probAlgState.getVocabulary().size()];

        sumWords = 0;
        Arrays.fill(sumWordsWithTopic, 0);
        Arrays.fill(sumWordCounts, 0);
        for (int k = 0; k < numberOfTopics; ++k) {
            CountWordInDocumentWithTopic.insert(k, new IntObjectOpenHashMap<IntIntOpenHashMap>());
            sumWordsWithTopicInDocument.insert(k, new IntIntOpenHashMap());
            sumTimesWordGotTopic.insert(k, new IntIntOpenHashMap());
        }
    }

    /**
     * Counts the words in the corpus regarding the documents and topics with which they occur.
     */
    protected void countWords() {
        instantiateCounter();
        int numberOfDocuments = probAlgState.getNumberOfDocuments();
        int wordTopicAssignments[];
        int wordsInDocument[];
        IntObjectOpenHashMap<IntIntOpenHashMap> tempIntObjMap;
        IntIntOpenHashMap tempIntIntMap;
        int topicId, wordId;
        for (int d = 0; d < numberOfDocuments; ++d) {
            wordTopicAssignments = probAlgState.getWordTopicAssignmentForDocument(d);
            wordsInDocument = probAlgState.getWordsOfDocument(d);

            for (int i = 0; i < wordsInDocument.length; i++) {
                wordId = wordsInDocument[i];
                topicId = wordTopicAssignments[i];
                tempIntObjMap = CountWordInDocumentWithTopic.get(topicId);
                tempIntIntMap = tempIntObjMap.get(wordId);
                if (tempIntIntMap == null) {
                    tempIntIntMap = new IntIntOpenHashMap();
                    tempIntObjMap.put(wordId, tempIntIntMap);
                }
                tempIntIntMap.putOrAdd(d, 1, 1);
                sumWordsWithTopicInDocument.get(topicId).putOrAdd(d, 1, 1);
                sumTimesWordGotTopic.get(topicId).putOrAdd(wordId, 1, 1);
                ++sumWordsWithTopic[topicId];
                ++sumWordCounts[wordId];
                ++sumWords;
            }
        }
        wordsCounted = true;
    }

    /**
     * The sum of all words in the corpus (N).
     * 
     * @return
     */
    public int getSumOfAllWords() {
        if (!wordsCounted) {
            countWords();
        }
        return sumWords;
    }

    /**
     * N_d
     * 
     * @param documentId
     * @return
     */
    public int getWordCountForDocument(int documentId) {
        if (!wordsCounted) {
            countWords();
        }
        int sum = 0;
        for (int t = 0; t < sumWordsWithTopicInDocument.size(); ++t) {
            if (sumWordsWithTopicInDocument.get(t).containsKey(documentId)) {
                sum += sumWordsWithTopicInDocument.get(t).get(documentId);
            }
        }
        return sum;
    }

    /**
     * N_d for all d in D.
     * 
     * @return
     */
    public int[] getWordCountsForDocumentsAsArray() {
        if (!wordsCounted) {
            countWords();
        }
        int sums[] = new int[probAlgState.getNumberOfDocuments()];
        Arrays.fill(sums, 0);
        int keys[], values[];
        boolean allocated[];
        for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
            keys = sumWordsWithTopicInDocument.get(t).keys;
            values = sumWordsWithTopicInDocument.get(t).values;
            allocated = sumWordsWithTopicInDocument.get(t).allocated;
            for (int i = 0; i < keys.length; ++i) {
                if (allocated[i]) {
                    sums[keys[i]] += values[i];
                }
            }
        }
        return sums;
    }

    /**
     * N_t
     * 
     * @param topicId
     * @return
     */
    public int getWordCountForTopic(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordsWithTopic[topicId];
    }

    /**
     * N_t for all t in T.
     * 
     * @return
     */
    public int[] getWordCountsForTopics() {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordsWithTopic;
    }

    /**
     * N_w
     * 
     * @param wordId
     * @return
     */
    public int getCountOfWord(int wordId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordCounts[wordId];
    }

    /**
     * N_w for all w in V.
     * 
     * @return
     */
    public int[] getCountsOfWords() {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordCounts;
    }

    /**
     * N_d_t
     * 
     * @param documentId
     * @param topicId
     * @return
     */
    public int getCountOfWordsInDocumentWithTopic(int documentId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (sumWordsWithTopicInDocument.get(topicId).containsKey(documentId)) {
            return sumWordsWithTopicInDocument.get(topicId).get(documentId);
        } else {
            return 0;
        }
    }

    /**
     * N_d_t for all d in D.
     * 
     * @param topicId
     * @return
     */
    public int[] getCountsOfWordsInDocumentsWithTopicAsArray(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        int sumWordInDocumentCountsAssignedToTopic[] = new int[probAlgState.getNumberOfDocuments()];
        IntIntOpenHashMap wordCountsInDocumentWithTopic = sumWordsWithTopicInDocument.get(topicId);
        for (int d = 0; d < sumWordInDocumentCountsAssignedToTopic.length; ++d) {
            if (wordCountsInDocumentWithTopic.containsKey(d)) {
                sumWordInDocumentCountsAssignedToTopic[d] = wordCountsInDocumentWithTopic.get(d);
            } else {
                sumWordInDocumentCountsAssignedToTopic[d] = 0;
            }
        }
        return sumWordInDocumentCountsAssignedToTopic;
    }

    /**
     * N_d_t for all d in D.
     * 
     * @param topicId
     * @return
     */
    public IntIntOpenHashMap getCountOfWordsInDocumentWithTopicAsMap(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordsWithTopicInDocument.get(topicId);
    }

    /**
     * N_w_t
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public int getCountOfWordAssignedToTopic(int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (sumTimesWordGotTopic.get(topicId).containsKey(wordId)) {
            return sumTimesWordGotTopic.get(topicId).get(wordId);
        } else {
            return 0;
        }
    }

    /**
     * N_w_t for all w in V.
     * 
     * @param topicId
     * @return
     */
    public int[] getCountsOfWordsAssignedToTopicAsArray(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        int sumWordCountsAssignedToTopic[] = new int[probAlgState.getNumberOfWords()];
        IntIntOpenHashMap topicWordCounts = sumTimesWordGotTopic.get(topicId);
        for (int w = 0; w < sumWordCountsAssignedToTopic.length; ++w) {
            if (topicWordCounts.containsKey(w)) {
                sumWordCountsAssignedToTopic[w] = topicWordCounts.get(w);
            } else {
                sumWordCountsAssignedToTopic[w] = 0;
            }
        }
        return sumWordCountsAssignedToTopic;
    }

    /**
     * N_w_t for all t in T.
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public int[] getCountsWordWasAssignedToTopicsAsArray(int wordId) {
        if (!wordsCounted) {
            countWords();
        }
        int sumWordAssignmentsToTopic[] = new int[probAlgState.getNumberOfTopics()];
        for (int t = 0; t < sumWordAssignmentsToTopic.length; ++t) {
            if (sumTimesWordGotTopic.get(t).containsKey(wordId)) {
                sumWordAssignmentsToTopic[t] = sumTimesWordGotTopic.get(t).get(wordId);
            } else {
                sumWordAssignmentsToTopic[t] = 0;
            }
        }
        return sumWordAssignmentsToTopic;
    }

    /**
     * N_w_t for all w in V.
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public IntIntOpenHashMap getCountsOfWordsAssignedToTopicAsMap(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumTimesWordGotTopic.get(topicId);
    }

    /**
     * N_d_w
     * 
     * @param documentId
     * @param wordId
     * @return
     */
    public int getCountOfWordInDocument(int documentId, int wordId) {
        if (!wordsCounted) {
            countWords();
        }
        IntIntOpenHashMap tempIntIntMap;
        int sum = 0;
        for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
            if (CountWordInDocumentWithTopic.get(t).containsKey(wordId)) {
                tempIntIntMap = CountWordInDocumentWithTopic.get(t).get(wordId);
                if (tempIntIntMap.containsKey(documentId)) {
                    sum += tempIntIntMap.get(documentId);
                }
            }
        }
        return sum;
    }

    /**
     * N_d_w_t
     * 
     * @param documentId
     * @param wordId
     * @param topicId
     * @return
     */
    public int getCountOfWordAssignedToTopicInDocument(int documentId, int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (CountWordInDocumentWithTopic.get(topicId).containsKey(wordId)) {
            IntIntOpenHashMap tempIntIntMap = CountWordInDocumentWithTopic.get(topicId).get(wordId);
            if (tempIntIntMap.containsKey(documentId)) {
                return tempIntIntMap.get(documentId);
            }
        }
        return 0;
    }

    /**
     * N_d_w_t for all d in D.
     * 
     * @param wordId
     * @param topicId
     * @return int[] containing the number of occurrences of the given word assigned to the given topic. The length of
     *         the array is the number of documents in which the word w occurred and was assigned to the topic t.
     */
    public int[] getCountsOfWordAssignedToTopicInDocumentsAsArray(int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (CountWordInDocumentWithTopic.get(topicId).containsKey(wordId)) {

            int countsInDocumentOfWordWithTopicArray[] = new int[probAlgState.getNumberOfDocuments()];
            IntIntOpenHashMap countsInDocumentOfWordWithTopic = CountWordInDocumentWithTopic.get(topicId).get(wordId);
            for (int d = 0; d < countsInDocumentOfWordWithTopicArray.length; ++d) {
                if (countsInDocumentOfWordWithTopic.containsKey(d)) {
                    countsInDocumentOfWordWithTopicArray[d] = countsInDocumentOfWordWithTopic.get(d);
                } else {
                    countsInDocumentOfWordWithTopicArray[d] = 0;
                }
            }
            return countsInDocumentOfWordWithTopicArray;
        }
        return new int[0];
    }

    /**
     * N_d_w_t for all d in D.
     * 
     * @param wordId
     * @param topicId
     * @return IntIntOpenHashMap containing the number of occurrences of the given word assigned to the given topic. The
     *         Ids of the documents in which the given word occurred assigned to the given topic are the keys and the
     *         counts are the values of the Map.
     */
    public IntIntOpenHashMap getCountsOfWordAssignedToTopicInDocumentsAsMap(int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (CountWordInDocumentWithTopic.get(topicId).containsKey(wordId)) {
            return CountWordInDocumentWithTopic.get(topicId).get(wordId);
        }
        return new IntIntOpenHashMap(0);
    }

    /**
     * P(w|t) = N_wt / N_t
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfWordInTopic(int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (sumTimesWordGotTopic.get(topicId).containsKey(wordId)) {
            return (sumTimesWordGotTopic.get(topicId).get(wordId) / (double) sumWordsWithTopic[topicId]);
        } else {
            return 0;
        }
    }

    /**
     * P(t|d) = N_dt / N_d
     * 
     * @param documentId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopicForDocument(int documentId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        return getCountOfWordsInDocumentWithTopic(documentId, topicId)
                / (double) getWordCountForDocument(documentId);
    }

    /**
     * P(t|w) = N_wt / N_w
     * 
     * @param wordId
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopicForWord(int wordId, int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        if (sumTimesWordGotTopic.get(topicId).containsKey(wordId)) {
            return (sumTimesWordGotTopic.get(topicId).get(wordId) / (double) sumWordCounts[wordId]);
        } else {
            return 0;
        }
    }

    /**
     * Array of P(t|w) for all t in T.
     * 
     * @param wordId
     * @return
     */
    public double[] getRelativFrequenciesOfTopicsForWord(int wordId) {
        if (!wordsCounted) {
            countWords();
        }
        double frequencies[] = new double[probAlgState.getNumberOfTopics()];
        for (int i = 0; i < frequencies.length; ++i) {
            frequencies[i] = getRelativFrequencyOfTopicForWord(wordId, i);
        }
        return frequencies;
    }

    /**
     * P(t) = N_t / N
     * 
     * @param topicId
     * @return
     */
    public double getRelativFrequencyOfTopic(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordsWithTopic[topicId] / (double) sumWords;
    }

    /**
     * P(t) = N_t / N for all t in T.
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfTopics() {
        if (!wordsCounted) {
            countWords();
        }
        if (topicProbabilities == null) {
            topicProbabilities = new double[sumWordsWithTopic.length];
            for (int t = 0; t < topicProbabilities.length; ++t) {
                topicProbabilities[t] = getRelativFrequencyOfTopic(t);
            }
        }
        return topicProbabilities;
    }

    /**
     * P(d) = N_d / N
     * 
     * @return
     */
    public double getRelativFrequencyOfDocument(int documentId) {
        if (!wordsCounted) {
            countWords();
        }
        return getWordCountForDocument(documentId) / (double) sumWords;
    }

    /**
     * P(d) = N_d / N for all d in D.
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfDocuments() {
        if (!wordsCounted) {
            countWords();
        }
        if (documentProbabilities == null) {
            documentProbabilities = new double[probAlgState.getNumberOfDocuments()];
            Arrays.fill(documentProbabilities, 0);
            Iterator<IntIntCursor> iterator;
            IntIntCursor cursor;
            for (int t = 0; t < sumWordsWithTopicInDocument.size(); ++t) {
                iterator = sumWordsWithTopicInDocument.get(t).iterator();
                while (iterator.hasNext()) {
                    cursor = iterator.next();
                    documentProbabilities[cursor.key] += cursor.value;
                }
            }
            for (int d = 0; d < documentProbabilities.length; d++) {
                documentProbabilities[d] /= sumWords;
            }
        }

        return documentProbabilities;
    }

    /**
     * p(w) = N_w / N
     * 
     * @param wordId
     * @return
     */
    public double getRelativFrequencyOfWord(int wordId) {
        if (!wordsCounted) {
            countWords();
        }
        return sumWordCounts[wordId] / (double) sumWords;
    }

    /**
     * p(w) = N_w / N for all w in V
     * 
     * @return
     */
    public double[] getRelativFrequenciesOfWords() {
        if (!wordsCounted) {
            countWords();
        }
        if (wordProbabilities == null) {
            wordProbabilities = new double[sumWordCounts.length];
            for (int w = 0; w < sumWordCounts.length; w++) {
                wordProbabilities[w] = sumWordCounts[w] / (double) sumWords;
            }
        }
        return wordProbabilities;
    }

    /**
     * Array of P(w|t) for all w in V.
     * 
     * @param topicId
     * @return
     */
    public double[] getRelativeFrequenciesOfWordsOfTopic(int topicId) {
        if (!wordsCounted) {
            countWords();
        }
        IntIntOpenHashMap wordsOfTopic = getCountsOfWordsAssignedToTopicAsMap(topicId);
        double[] probabilities = new double[sumWordCounts.length];
        double wordsWithTopic = sumWordsWithTopic[topicId];
        for (IntIntCursor cursor : wordsOfTopic) {
            probabilities[cursor.key] = cursor.value / wordsWithTopic;
        }
        return probabilities;
    }

    /**
     * Releases all allocated objects.
     */
    public void clear() {
        CountWordInDocumentWithTopic = null;
        sumWordsWithTopic = null;
        sumWordsWithTopicInDocument = null;
        sumTimesWordGotTopic = null;
        sumWordCounts = null;
        sumWords = 0;
        wordsCounted = false;
    }

    public int getCount(EvaluationResultDimension dimension, int id) {
        switch (dimension) {
        case DOCUMENT:
            return getWordCountForDocument(id);
        case TOPIC:
            return getWordCountForTopic(id);
        case WORD:
            return getCountOfWord(id);
        }
        return 0;
    }

    public int getCount(EvaluationResultDimension dimension1, int id1, EvaluationResultDimension dimension2, int id2) {
        switch (dimension1) {
        case DOCUMENT: {
            switch (dimension2) {
            case TOPIC:
                return getCountOfWordsInDocumentWithTopic(id1, id2);
            case WORD:
                return getCountOfWordInDocument(id1, id2);
            default:
                break;
            }
            return 0;
        }
        case TOPIC: {
            switch (dimension2) {
            case DOCUMENT:
                return getCountOfWordsInDocumentWithTopic(id2, id1);
            case WORD:
                return getCountOfWordAssignedToTopic(id2, id1);
            default:
                break;
            }
            return 0;
        }
        case WORD: {
            switch (dimension2) {
            case DOCUMENT:
                return getCountOfWordInDocument(id2, id1);
            case TOPIC:
                return getCountOfWordAssignedToTopic(id1, id2);
            default:
                break;
            }
            return 0;
        }
        default:
            return 0;
        }
    }

    public int getCount(EvaluationResultDimension dimension1, int id1, EvaluationResultDimension dimension2, int id2,
            EvaluationResultDimension dimension3, int id3) {
        switch (dimension1) {
        case DOCUMENT: {
            switch (dimension2) {
            case TOPIC:
                return dimension3.equals(EvaluationResultDimension.WORD) ? getCountOfWordAssignedToTopicInDocument(id1,
                        id3, id2) : 0;
            case WORD:
                return dimension3.equals(EvaluationResultDimension.TOPIC) ? getCountOfWordAssignedToTopicInDocument(
                        id1, id2, id3) : 0;
            default:
                break;
            }
            return 0;
        }
        case TOPIC: {
            switch (dimension2) {
            case DOCUMENT:
                return dimension3.equals(EvaluationResultDimension.WORD) ? getCountOfWordAssignedToTopicInDocument(id2,
                        id3, id1) : 0;
            case WORD:
                return dimension3.equals(EvaluationResultDimension.DOCUMENT) ? getCountOfWordAssignedToTopicInDocument(
                        id3, id2, id1) : 0;
            default:
                break;
            }
            return 0;
        }
        case WORD: {
            switch (dimension2) {
            case DOCUMENT:
                return dimension3.equals(EvaluationResultDimension.TOPIC) ? getCountOfWordAssignedToTopicInDocument(
                        id2, id1, id3) : 0;
            case TOPIC:
                return dimension3.equals(EvaluationResultDimension.DOCUMENT) ? getCountOfWordAssignedToTopicInDocument(
                        id3, id1, id2) : 0;
            default:
                break;
            }
            return 0;
        }
        default:
            return 0;
        }
    }

    public int getDimensionSize(EvaluationResultDimension dimension) {
        switch (dimension) {
        case DOCUMENT:
            return probAlgState.getNumberOfDocuments();
        case TOPIC:
            return probAlgState.getNumberOfTopics();
        case WORD:
            return probAlgState.getNumberOfWords();
        default:
            return 0;
        }
    }

    public double[] getProbabilitys(EvaluationResultDimension dimension) {
        switch (dimension) {
        case DOCUMENT:
            return getRelativFrequenciesOfDocuments();
        case TOPIC:
            return getRelativFrequenciesOfTopics();
        case WORD:
            return getRelativFrequenciesOfWords();
        default:
            return new double[0];
        }
    }
}
