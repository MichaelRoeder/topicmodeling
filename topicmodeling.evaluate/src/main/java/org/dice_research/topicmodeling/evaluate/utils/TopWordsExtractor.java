package org.dice_research.topicmodeling.evaluate.utils;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.algorithms.WordCounter;
import org.dice_research.topicmodeling.commons.sort.AssociativeSort;

public class TopWordsExtractor {

    public int[][] extractTopWords(ProbTopicModelingAlgorithmStateSupplier probAlgState, int numberOfTopWords) {
        int numberOfWords = probAlgState.getNumberOfWords();
        int numberOfTopics = probAlgState.getNumberOfTopics();
        int topWords[][] = new int[numberOfTopics][];
        WordCounter wordCounter = probAlgState.getWordCounts();

        int wordIdArray[] = new int[numberOfWords];
        for (int w = 0; w < numberOfWords; ++w) {
            wordIdArray[w] = w;
        }

        double topicWordProbabilities[];
        int[] tempWordIds;
        for (int t = 0; t < numberOfTopics; ++t) {
            topicWordProbabilities = wordCounter.getRelativeFrequenciesOfWordsOfTopic(t);
            tempWordIds = Arrays.copyOf(wordIdArray, numberOfWords);
            AssociativeSort.quickSort(topicWordProbabilities, tempWordIds);
            topWords[t] = Arrays.copyOfRange(tempWordIds, numberOfWords - numberOfTopWords, numberOfWords);
            ArrayUtils.reverse(topWords[t]);
        }
        return topWords;
    }
}
