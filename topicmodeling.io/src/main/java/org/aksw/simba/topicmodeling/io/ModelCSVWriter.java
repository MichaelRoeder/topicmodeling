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
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.aksw.simba.topicmodeling.algorithms.ProbabilisticWordTopicModel;
import org.aksw.simba.topicmodeling.algorithms.WordCounter;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;


public class ModelCSVWriter extends AbstractModelFilesWriter implements CSVFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ModelCSVWriter.class);

    private static final String TOPIC_PROBS_FILE_NAME = "topic_probabilities.csv";
    private static final String TOPIC_PROBS_FILE_HEAD[] = { "topics", "probability" };

    private static final String WORD_TOPIC_PROBS_FILE_NAME = "word_topic_probabilities.csv";

    private static final String WORD_TOPIC_SEPARATING_PROBS_FILE_NAME = "word_topic_separating_probabilities.csv";

    private static final String TOP_WORDS_FILE = "top_words.csv";
    private static final String SEPARATING_TOP_WORDS_FILE = "separating_top_words.csv";
    protected int numberOfTopWordsPerTopic = 10;

    private static final String TOPICS_FOR_DOCUMENTS_FILE = "topics_for_documents.csv";
    protected int numberOfTopTopicsPerDocument = 10;

    // private static final String DOUBLE_FORMAT_STRING = "%,f";

    protected String fileNamePrefix;
    protected NumberFormat formatter;
    protected ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier;
    protected boolean printWordTopicProbs = false;

    public ModelCSVWriter(ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier, File folder,
            boolean printWordTopicProbs) {
        this(probTopicStateSupplier, folder);
        this.printWordTopicProbs = printWordTopicProbs;
    }

    public ModelCSVWriter(ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier, File folder) {
        super(folder);
        this.probTopicStateSupplier = probTopicStateSupplier;
        fileNamePrefix = "/";
        formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(1000);
    }

    public ModelCSVWriter(File folder, String fileNamePrefix) {
        super(folder);
        this.fileNamePrefix = fileNamePrefix.startsWith("/") ? fileNamePrefix : "/" + fileNamePrefix;
        formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(1000);
    }

    @Override
    public void writeModelToFiles(Model model) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (model instanceof ProbabilisticWordTopicModel) {
            writeProbabilisticWordTopicModel((ProbabilisticWordTopicModel) model);
            return;
        }
        this.writeModelToCSVFile(model);
    }

    public void setNumberOfTopWordsPerTopic(int numberOfTopWordsPerTopic) {
        this.numberOfTopWordsPerTopic = numberOfTopWordsPerTopic;
    }

    protected void writeModelToCSVFile(Model model) {
        throw new IllegalArgumentException("Got an unknown Model class " + model.getClass().getCanonicalName()
                + "! Can't write this to a csv file.");
    }

    protected void writeProbabilisticWordTopicModel(ProbabilisticWordTopicModel model) {
        writeTopicFile(model);
        TopWordContainer topWords = processWordTopicProbabilities(model);
        writeTopWords(model, topWords);
        topWords = processSeparatingWordTopicProbabilities(model);
        writeSeparatingTopWords(model, topWords);
        writeTopicsForDocuments(model);
    }

    protected void writeTopicFile(ProbabilisticWordTopicModel model) {
        // write topics to file
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();

        try {
            csvWriter = new CSVWriter(
                    new FileWriter(folder.getAbsolutePath() + fileNamePrefix + TOPIC_PROBS_FILE_NAME), SEPARATOR,
                    QUOTECHAR, ESCAPECHAR);
            csvWriter.writeNext(TOPIC_PROBS_FILE_HEAD);
            line = new String[2];
            for (int topic = 0; topic < numberOfTopics; ++topic) {
                line[0] = "topic" + topic;
                // line[1] = String.format(DOUBLE_FORMAT_STRING,
                // model.getSmoothedProbabilityOfTopic(topic));
                line[1] = formatter.format(model.getSmoothedProbabilityOfTopic(topic));
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Error while writing topic probabilities file.", e);
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }
    }

    protected TopWordContainer processWordTopicProbabilities(ProbabilisticWordTopicModel model) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();
        TopWordContainer topWordsContainer = new TopWordContainer(numberOfTopics, numberOfTopWordsPerTopic);

        try {
            if (printWordTopicProbs) {
                csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix
                        + WORD_TOPIC_PROBS_FILE_NAME), SEPARATOR, QUOTECHAR, ESCAPECHAR);
            }

            line = new String[model.getNumberOfTopics() + 1];
            line[0] = "words";
            for (int topic = 0; topic < model.getNumberOfTopics(); ++topic) {
                line[topic + 1] = "topic" + topic;
            }
            if (printWordTopicProbs) {
                csvWriter.writeNext(line);
            }

            Vocabulary vocabulary = model.getVocabulary();
            Iterator<String> iterator = vocabulary.iterator();
            String word;
            int wordId;
            double wordProbability;
            while (iterator.hasNext()) {
                word = iterator.next();
                wordId = vocabulary.getId(word);
                line[0] = word;
                for (int topic = 0; topic < numberOfTopics; ++topic) {
                    wordProbability = model.getSmoothedProbabilityOfWord(wordId, topic);
                    // line[topic + 1] = String.format(DOUBLE_FORMAT_STRING,
                    // wordProbability);
                    line[topic + 1] = formatter.format(wordProbability);
                    topWordsContainer.insertIfItIsTopWord(topic, word, wordProbability);
                }
                if (printWordTopicProbs) {
                    csvWriter.writeNext(line);
                }
            }
        } catch (IOException e) {
            logger.error("Error while writing word-topic probabilities file.", e);
            return null;
        } finally {
            try {
                if (printWordTopicProbs) {
                    csvWriter.close();
                }
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }

        return topWordsContainer;
    }

    protected void writeTopWords(ProbabilisticWordTopicModel model, TopWordContainer topWords) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();

        try {
            csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix + TOP_WORDS_FILE),
                    SEPARATOR, QUOTECHAR, ESCAPECHAR);

            line = new String[model.getNumberOfTopics() * 2];
            for (int topic = 0; topic < numberOfTopics; ++topic) {
                line[2 * topic] = "topic" + topic;
                line[(2 * topic) + 1] = "";
            }
            csvWriter.writeNext(line);

            for (int wordRanking = 0; wordRanking < numberOfTopWordsPerTopic; ++wordRanking) {
                for (int topic = 0; topic < numberOfTopics; ++topic) {
                    line[2 * topic] = topWords.getTopWord(topic, wordRanking);
                    // line[(2 * topic) + 1] =
                    // String.format(DOUBLE_FORMAT_STRING,
                    // topWords.getTopWordProbability(topic, wordRanking));
                    line[(2 * topic) + 1] = formatter.format(topWords.getTopWordProbability(topic, wordRanking));
                }
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Error while writing topic top words file.", e);
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }
    }

    protected TopWordContainer processSeparatingWordTopicProbabilities(ProbabilisticWordTopicModel model) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();
        TopWordContainer topWordsContainer = new TopWordContainer(numberOfTopics, numberOfTopWordsPerTopic);

        try {
            csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix
                    + WORD_TOPIC_SEPARATING_PROBS_FILE_NAME), SEPARATOR, QUOTECHAR, ESCAPECHAR);

            line = new String[model.getNumberOfTopics() + 1];
            line[0] = "words";
            for (int topic = 0; topic < model.getNumberOfTopics(); ++topic) {
                line[topic + 1] = "topic" + topic;
            }
            csvWriter.writeNext(line);

            Vocabulary vocabulary = model.getVocabulary();
            Iterator<String> iterator = vocabulary.iterator();
            String word;
            int wordId;
            double wordProbability;
            WordCounter wordCounter = probTopicStateSupplier.getWordCounts();
            while (iterator.hasNext()) {
                word = iterator.next();
                wordId = vocabulary.getId(word);
                line[0] = word;
                for (int topic = 0; topic < numberOfTopics; ++topic) {
                    wordProbability = wordCounter.getRelativFrequencyOfWordInTopic(wordId, topic)
                            + wordCounter.getRelativFrequencyOfTopicForWord(wordId, topic);
                    // wordProbability = wordCounter.getRelativFrequencyOfWordInTopic(wordId, topic)
                    // * wordCounter.getRelativFrequencyOfTopicForWord(wordId, topic);
                    line[topic + 1] = formatter.format(wordProbability);
                    topWordsContainer.insertIfItIsTopWord(topic, word, wordProbability);
                }
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Error while writing word-topic probabilities file.", e);
            return null;
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }

        return topWordsContainer;
    }

    protected void writeSeparatingTopWords(ProbabilisticWordTopicModel model, TopWordContainer topWords) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();

        try {
            csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix
                    + SEPARATING_TOP_WORDS_FILE),
                    SEPARATOR, QUOTECHAR, ESCAPECHAR);

            line = new String[model.getNumberOfTopics() * 2];
            for (int topic = 0; topic < numberOfTopics; ++topic) {
                line[2 * topic] = "topic" + topic;
                line[(2 * topic) + 1] = "";
            }
            csvWriter.writeNext(line);

            for (int wordRanking = 0; wordRanking < numberOfTopWordsPerTopic; ++wordRanking) {
                for (int topic = 0; topic < numberOfTopics; ++topic) {
                    line[2 * topic] = topWords.getTopWord(topic, wordRanking);
                    // line[(2 * topic) + 1] =
                    // String.format(DOUBLE_FORMAT_STRING,
                    // topWords.getTopWordProbability(topic, wordRanking));
                    line[(2 * topic) + 1] = formatter.format(topWords.getTopWordProbability(topic, wordRanking));
                }
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Error while writing topic top words file.", e);
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }
    }

    protected void writeTopicsForDocuments(ProbabilisticWordTopicModel model) {
        CSVWriter csvWriter = null;
        String line[];
        int numberOfTopics = model.getNumberOfTopics();
        WordCounter wordCounter = probTopicStateSupplier.getWordCounts();
        int numberOfDocuments = probTopicStateSupplier.getNumberOfDocuments();
        int numberOfTopicsPrintedOut = Math.min(numberOfTopics, numberOfTopTopicsPerDocument);

        try {
            csvWriter = new CSVWriter(new FileWriter(folder.getAbsolutePath() + fileNamePrefix
                    + TOPICS_FOR_DOCUMENTS_FILE), SEPARATOR, QUOTECHAR, ESCAPECHAR);

            line = new String[(numberOfTopicsPrintedOut * 2) + 1];
            int wordsInDocuments[] = wordCounter.getWordCountsForDocumentsAsArray();
            TopicProbabilityForDocument topicProbabilities[] = new TopicProbabilityForDocument[numberOfTopics];
            for (int d = 0; d < numberOfDocuments; ++d) {
                line[0] = Integer.toString(d);
                if (wordsInDocuments[d] > 0) {
                    for (int t = 0; t < numberOfTopics; ++t) {
                        topicProbabilities[t] = new TopicProbabilityForDocument(t,
                                wordCounter.getCountOfWordsInDocumentWithTopic(d, t) / (double) wordsInDocuments[d]);
                    }
                } else {
                    for (int t = 0; t < numberOfTopics; ++t) {
                        topicProbabilities[t] = new TopicProbabilityForDocument(t, 0);
                    }
                }
                // sort them with descending probability
                Arrays.sort(topicProbabilities, null);
                // print out the best topics
                for (int t = 0; t < numberOfTopicsPrintedOut; ++t) {
                    line[(t * 2) + 1] = Integer.toString(topicProbabilities[t].getTopicId());
                    line[(t * 2) + 2] = formatter.format(topicProbabilities[t].getProbability());
                    ;
                }
                csvWriter.writeNext(line);
            }
        } catch (IOException e) {
            logger.error("Error while writing topic top words file.", e);
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                logger.error("Error while closing csv file.", e);
            }
        }
    }

    protected static class TopWordContainer {
        protected String topWords[][];
        protected double topWordProbabilities[][];
        protected int numberOfTopWordsPerTopic;

        public TopWordContainer(int numberOfTopics, int numberOfTopWordsPerTopic) {
            this.numberOfTopWordsPerTopic = numberOfTopWordsPerTopic;
            topWords = new String[numberOfTopics][numberOfTopWordsPerTopic];
            topWordProbabilities = new double[numberOfTopics][numberOfTopWordsPerTopic];
            for (int topic = 0; topic < numberOfTopics; ++topic) {
                Arrays.fill(topWords[topic], "");
                Arrays.fill(topWordProbabilities[topic], 0.0);
            }
        }

        public void insertIfItIsTopWord(int topic, String word, double probability) {
            if (probability > topWordProbabilities[topic][numberOfTopWordsPerTopic - 1]) {
                int index = topWordProbabilities[topic].length - 1;
                // search for the position to insert this word and move the
                // other words down
                while ((index > 0) && (probability > topWordProbabilities[topic][index - 1])) {
                    topWords[topic][index] = topWords[topic][index - 1];
                    topWordProbabilities[topic][index] = topWordProbabilities[topic][index - 1];
                    --index;
                }
                topWords[topic][index] = word;
                topWordProbabilities[topic][index] = probability;
            }
        }

        public String getTopWord(int topic, int wordRanking) {
            return topWords[topic][wordRanking];
        }

        public double getTopWordProbability(int topic, int wordRanking) {
            return topWordProbabilities[topic][wordRanking];
        }
    }

    @SuppressWarnings("unused")
    private static class TopicProbabilityForDocument implements Comparable<TopicProbabilityForDocument> {
        private int topicId;
        private double probability;

        public TopicProbabilityForDocument(int topicId, double probability) {
            this.topicId = topicId;
            this.probability = probability;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }

        @Override
        public int compareTo(TopicProbabilityForDocument otherProb) {
            // sorts them with descending probability
            if (this.probability == otherProb.getProbability()) {
                return this.topicId - otherProb.getTopicId() < 0 ? 1 : -1;
            } else {
                return this.probability - otherProb.getProbability() < 0 ? 1 : -1;
            }
        }
    }
}
