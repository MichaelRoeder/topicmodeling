/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.aksw.simba.topicmodeling.algorithms.WordCounter;
import org.aksw.simba.topicmodeling.commons.sort.AssociativeSort;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;

public class ZipfBasedTopWordWriter extends AbstractModelFilesWriter implements CSVFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipfBasedTopWordWriter.class);

    private static final String SEPARATING_TOP_WORDS_FILE = "zipf_based_top_words.csv";
    private static final int MAX_NUMBER_OF_TOP_WORDS = 30;
    private static final double FREQUENCY_CLASS_WEIGHTING = 0.8;
    private static final double LOG2 = Math.log(2.0);

    private String fileNamePrefix;
    private ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier;

    public ZipfBasedTopWordWriter(ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier, File folder) {
        super(folder);
        this.probTopicStateSupplier = probTopicStateSupplier;
        fileNamePrefix = "/";
    }

    public ZipfBasedTopWordWriter(File folder, String fileNamePrefix) {
        super(folder);
        this.fileNamePrefix = fileNamePrefix.startsWith("/") ? fileNamePrefix : "/" + fileNamePrefix;
    }

    @Override
    public void writeModelToFiles(Model model) {
        int numberOfTopics = probTopicStateSupplier.getNumberOfTopics();
        WordCounter wordCounter = probTopicStateSupplier.getWordCounts();
        int[] backgroundWordCounts = wordCounter.getCountsOfWords();
        int[][] facetWordIds = new int[numberOfTopics][];
        for (int t = 0; t < numberOfTopics; ++t) {
            facetWordIds[t] = getFacetsForSingleTopic(wordCounter.getCountsOfWordsAssignedToTopicAsArray(t),
                    backgroundWordCounts);
        }
        writeTopWords(facetWordIds, probTopicStateSupplier.getVocabulary());
    }

    private void writeTopWords(int[][] topWords, Vocabulary vocabulary) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = topWords.length;
        int maxNumberOfTopWords = 0;
        for (int topic = 0; topic < numberOfTopics; ++topic) {
            if (topWords[topic].length > maxNumberOfTopWords) {
                maxNumberOfTopWords = topWords[topic].length;
            }
        }

        try {
            csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix
                    + SEPARATING_TOP_WORDS_FILE), SEPARATOR, QUOTECHAR, ESCAPECHAR);

            line = new String[numberOfTopics];
            for (int topic = 0; topic < numberOfTopics; ++topic) {
                line[topic] = "topic" + topic;
            }
            csvWriter.writeNext(line);

            for (int w = 0; w < maxNumberOfTopWords; ++w) {
                for (int topic = 0; topic < numberOfTopics; ++topic) {
                    if (w < topWords[topic].length) {
                        line[topic] = vocabulary.getWord(topWords[topic][w]);
                    } else {
                        line[topic] = "";
                    }
                }
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error while writing topic top words file.", e);
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing csv file.", e);
            }
        }
    }

    protected int[] getFacetsForSingleTopic(int[] foregroundWordCounts, int[] backgroundWordCounts) {
        IntDoubleOpenHashMap foregroundFrequencyClasses = new IntDoubleOpenHashMap();
        double maxForegroundClass = determineFrequencyClasses(foregroundWordCounts, foregroundFrequencyClasses);
        int[] backgroundWordCountsForTopic = getCountDifference(backgroundWordCounts, foregroundWordCounts);
        IntDoubleOpenHashMap backgroundFrequencyClasses = new IntDoubleOpenHashMap();
        double maxBackgroundClass = determineFrequencyClasses(backgroundWordCountsForTopic, backgroundFrequencyClasses);
        IntDoubleOpenHashMap differences = getClassDifferences(foregroundFrequencyClasses, backgroundFrequencyClasses,
                maxBackgroundClass);
        return determineBestWords(foregroundWordCounts, foregroundFrequencyClasses, differences, maxBackgroundClass,
                maxForegroundClass);
    }

    private double determineFrequencyClasses(int[] wordCounts, IntDoubleOpenHashMap frequencyClasses) {
        double maxCount = getMaxCount(wordCounts);
        double maxClass = 0;
        double frequencyClass;
        for (int i = 0; i < wordCounts.length; ++i) {
            if (wordCounts[i] > 0) {
                frequencyClass = (Math.log(maxCount / (double) wordCounts[i]) / LOG2) + 1;
                if (frequencyClass > maxClass) {
                    maxClass = frequencyClass;
                }
                frequencyClasses.put(i, frequencyClass);
            }
        }
        return maxClass;
    }

    private int[] getCountDifference(int[] backgroundCount, int[] foregroundCount) {
        int[] difference = new int[backgroundCount.length];
        for (int i = 0; i < backgroundCount.length; ++i) {
            difference[i] = backgroundCount[i] - foregroundCount[i];
        }
        return difference;
    }

    private double getMaxCount(int[] wordCounts) {
        double maxCount = 0;
        for (int i = 0; i < wordCounts.length; ++i) {
            if (wordCounts[i] > maxCount) {
                maxCount = wordCounts[i];
            }
        }
        return maxCount;
    }

    protected IntDoubleOpenHashMap getClassDifferences(IntDoubleOpenHashMap foregroundFrequencyClasses,
            IntDoubleOpenHashMap backgroundFrequencyClasses, double maxBackgroundClass) {
        IntDoubleOpenHashMap differences = new IntDoubleOpenHashMap(foregroundFrequencyClasses.keys.length);
        for (IntDoubleCursor foregroundFerquencyClass : foregroundFrequencyClasses) {
            if (backgroundFrequencyClasses.containsKey(foregroundFerquencyClass.key)) {
                differences.put(foregroundFerquencyClass.key,
                        backgroundFrequencyClasses.get(foregroundFerquencyClass.key) - foregroundFerquencyClass.value);
            } else {
                differences.put(foregroundFerquencyClass.key, maxBackgroundClass - foregroundFerquencyClass.value);
            }
        }
        return differences;
    }

    private int[] determineBestWords(int[] wordCounts, IntDoubleOpenHashMap frequencyClasses,
            IntDoubleOpenHashMap differences, double maxBackgroundClass, double maxForegroundClass) {
        int[] topWords = new int[differences.size()];
        double[] ratings = new double[differences.size()];
        int index = 0;
        for (IntDoubleCursor difference : differences) {
            topWords[index] = difference.key;
            // normalize the frequency class to [0,1] and weight it
            ratings[index] = FREQUENCY_CLASS_WEIGHTING
                    * (1 - (frequencyClasses.get(difference.key) / maxForegroundClass));
            // normalize the difference to [0,1] and weight it
            ratings[index] += (1 - FREQUENCY_CLASS_WEIGHTING) * (difference.value / maxBackgroundClass);
            ++index;
        }
        AssociativeSort.quickSort(ratings, topWords);
        ArrayUtils.reverse(topWords);
        return Arrays.copyOf(topWords, MAX_NUMBER_OF_TOP_WORDS);
    }
}
