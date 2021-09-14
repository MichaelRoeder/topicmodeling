package org.dice_research.topicmodeling.evaluate;

import java.util.Arrays;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.algorithms.WordCounter;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultAsDoubleArray;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultDimension;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;

public class ClassifiedTrainingDataEvaluator extends AbstractEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(ClassifiedTrainingDataEvaluator.class);

    private ProbTopicModelingAlgorithmStateSupplier probAlgState;
    private Corpus trainCorpus;
    private boolean singleLabelClassification = true;

    public ClassifiedTrainingDataEvaluator(ProbTopicModelingAlgorithmStateSupplier probAlgState, Corpus trainCorpus) {
        this.probAlgState = probAlgState;
        this.trainCorpus = trainCorpus;
    }

    public ClassifiedTrainingDataEvaluator(ProbTopicModelingAlgorithmStateSupplier probAlgState, Corpus trainCorpus,
            boolean singleLabelClassification) {
        this.probAlgState = probAlgState;
        this.trainCorpus = trainCorpus;
        this.singleLabelClassification = singleLabelClassification;
    }

    @Override
    public EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        ObjectIntOpenHashMap<String> categoryMapping = new ObjectIntOpenHashMap<String>();
        int categoriesOfDocuments[][] = getCategoriesOfDocuments(categoryMapping);
        double distributionOfCategoriesOverTopics[][] = calculateDistributionOfCategoriesOverTopics(
                categoriesOfDocuments, categoryMapping.size());
        double singleTokenBasedDistributionOfCategoriesOverTopics[][] = calculateSingleTokenBasedDistributionOfCategoriesOverTopics(
                categoriesOfDocuments, categoryMapping.size());
        double categoryProbabilities[] = getCategoryProbabilities(categoriesOfDocuments, categoryMapping.size());
        double topicProbabilities[] = probAlgState.getWordCounts().getRelativFrequenciesOfTopics();
        double categoryTopicProbabilities[][] = getCategoryTopicProbabilities(categoriesOfDocuments,
                categoryMapping.size());

        EvaluationResultCollection results = new EvaluationResultCollection();
        results.addResult(calculatePrecisionRecallFMeasure(categoryMapping, distributionOfCategoriesOverTopics,
                topicProbabilities, categoryProbabilities, ""));
        results.addResult(calculatePrecisionRecallFMeasure(categoryMapping,
                singleTokenBasedDistributionOfCategoriesOverTopics, topicProbabilities, categoryProbabilities,
                "singleToken"));
        results.addResult(calculateMutualInformation(categoryTopicProbabilities, topicProbabilities,
                categoryProbabilities));
        results.addResult(calculatePurity(distributionOfCategoriesOverTopics, topicProbabilities, ""));
        results.addResult(calculatePurity(singleTokenBasedDistributionOfCategoriesOverTopics, topicProbabilities,
                "_singleToken"));

        return results;
    }

    /**
     * Returns the IDs of all categories a document has. The IDs are defined using the given category mapping. If a
     * category is not inside this mapping it will be added. If a document has no category its corresponding array will
     * have the length 0.
     * 
     * @param categoryMapping
     * @return
     */
    private int[][] getCategoriesOfDocuments(ObjectIntOpenHashMap<String> categoryMapping) {
        int[][] categoriesOfDocuments = new int[trainCorpus.getNumberOfDocuments()][];
        DocumentCategory category;
        DocumentMultipleCategories categories;
        int categoryId;
        for (int d = 0; d < trainCorpus.getNumberOfDocuments(); ++d) {
            category = trainCorpus.getDocument(d).getProperty(DocumentCategory.class);
            if (category == null) {
                categories = trainCorpus.getDocument(d).getProperty(DocumentMultipleCategories.class);
                if (categories == null) {
                    logger.warn("Got document without DocumentCategory and DocumentMultipleCategories property.");
                    categoriesOfDocuments[d] = new int[0];
                } else {
                    categoriesOfDocuments[d] = new int[categories.getCategories().length];
                    for (int c = 0; c < categories.getCategories().length; ++c) {
                        if (!categoryMapping.containsKey(categories.getCategories()[c])) {
                            categoryId = categoryMapping.size();
                            categoryMapping.put(categories.getCategories()[c], categoryMapping.size());
                        } else {
                            categoryId = categoryMapping.get(categories.getCategories()[c]);
                        }
                        categoriesOfDocuments[d][c] = categoryId;
                    }
                }
            } else {
                categoriesOfDocuments[d] = new int[1];
                if (!categoryMapping.containsKey(category.getCategory())) {
                    categoryId = categoryMapping.size();
                    categoryMapping.put(category.getCategory(), categoryMapping.size());
                } else {
                    categoryId = categoryMapping.get(category.getCategory());
                }
                categoriesOfDocuments[d][0] = categoryId;
            }
        }
        return categoriesOfDocuments;
    }

    private double[] getCategoryProbabilities(int categoriesOfDocuments[][], int numberOfCategories) {
        double categoryProbabilities[] = new double[numberOfCategories];
        Arrays.fill(categoryProbabilities, 0);
        for (int d = 0; d < categoriesOfDocuments.length; d++) {
            for (int c = 0; c < categoriesOfDocuments[d].length; c++) {
                ++categoryProbabilities[categoriesOfDocuments[d][c]];
            }
        }
        for (int c = 0; c < categoryProbabilities.length; c++) {
            categoryProbabilities[c] /= categoriesOfDocuments.length;
        }
        return categoryProbabilities;
    }

    /**
     * calculates the distribution of every category over all topics using the
     * sum p(t|d_c) with d_c in C. Note that because p(d) is not part of the
     * calculation every document has the same weighting.
     * 
     * @param categoriesOfDocuments
     * @param numberOfCategories
     * @return
     */
    private double[][] calculateDistributionOfCategoriesOverTopics(int categoriesOfDocuments[][], int numberOfCategories) {
        double distributionOfCategoriesOverTopics[][] = new double[numberOfCategories][probAlgState.getNumberOfTopics()];
        for (int c = 0; c < numberOfCategories; ++c) {
            Arrays.fill(distributionOfCategoriesOverTopics[c], 0);
        }

        WordCounter wordCounter = probAlgState.getWordCounts();
        double currentCategoryDistribution[];
        double maxFrequency, tempFrequency;
        int maxTopicId = 0;
        for (int d = 0; d < trainCorpus.getNumberOfDocuments(); ++d) {
            if (categoriesOfDocuments[d].length > 0) {
                if (wordCounter.getCount(EvaluationResultDimension.DOCUMENT, d) > 0) {
                    for (int c = 0; c < categoriesOfDocuments[d].length; c++) {
                        currentCategoryDistribution = distributionOfCategoriesOverTopics[categoriesOfDocuments[d][c]];
                        if (singleLabelClassification) {
                            maxFrequency = 0;
                            for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                                tempFrequency = wordCounter.getRelativFrequencyOfTopicForDocument(d, t);
                                if (tempFrequency > maxFrequency) {
                                    maxFrequency = tempFrequency;
                                    maxTopicId = t;
                                }
                            }
                            currentCategoryDistribution[maxTopicId] += 1;
                        } else {
                            // Multilabel Classification
                            for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                                currentCategoryDistribution[t] += wordCounter.getRelativFrequencyOfTopicForDocument(d,
                                        t);
                            }
                        }
                    }
                }
            }
        }
        // normalize
        double sum;
        for (int c = 0; c < numberOfCategories; ++c) {
            currentCategoryDistribution = distributionOfCategoriesOverTopics[c];
            sum = 0;
            for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                sum += currentCategoryDistribution[t];
            }
            if (sum > 0) {
                for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                    currentCategoryDistribution[t] /= sum;
                }
            }
        }

        return distributionOfCategoriesOverTopics;
    }

    /**
     * calculates the distribution of every category over all topics using the
     * sum N_{d_c,t} with d_c in C. Note that because the number of single
     * tokens is used the different documents are weighted based on the number
     * of words that they have.
     * 
     * @param categoriesOfDocuments
     * @param numberOfCategories
     * @return
     */
    private double[][] calculateSingleTokenBasedDistributionOfCategoriesOverTopics(int categoriesOfDocuments[][],
            int numberOfCategories) {
        double distributionOfCategoriesOverTopics[][] = new double[numberOfCategories][probAlgState.getNumberOfTopics()];
        for (int c = 0; c < numberOfCategories; ++c) {
            Arrays.fill(distributionOfCategoriesOverTopics[c], 0);
        }

        WordCounter wordCounter = probAlgState.getWordCounts();
        double currentCategoryDistribution[];
        for (int d = 0; d < trainCorpus.getNumberOfDocuments(); ++d) {
            if (wordCounter.getCount(EvaluationResultDimension.DOCUMENT, d) > 0) {
                for (int c = 0; c < categoriesOfDocuments[d].length; c++) {
                    currentCategoryDistribution = distributionOfCategoriesOverTopics[categoriesOfDocuments[d][c]];
                    for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                        currentCategoryDistribution[t] += wordCounter.getCountOfWordsInDocumentWithTopic(d, t);
                    }
                }
            }
        }
        // normalize
        double sum;
        for (int c = 0; c < numberOfCategories; ++c) {
            currentCategoryDistribution = distributionOfCategoriesOverTopics[c];
            sum = 0;
            for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                sum += currentCategoryDistribution[t];
            }
            if (sum > 0) {
                for (int t = 0; t < probAlgState.getNumberOfTopics(); ++t) {
                    currentCategoryDistribution[t] /= sum;
                }
            }
        }

        return distributionOfCategoriesOverTopics;
    }

    private double max(double... values) {
        double max = 0;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] > max) {
                max = values[i];
            }
        }
        return max;
    }

    private double[][] getCategoryTopicProbabilities(int categoriesOfDocuments[][], int numberOfCategories) {
        WordCounter wordCounter = probAlgState.getWordCounts();
        int numberOfTopics = probAlgState.getNumberOfTopics();
        double categoryTopicProbabilities[][] = new double[numberOfCategories][numberOfTopics];
        for (int c = 0; c < numberOfCategories; ++c) {
            Arrays.fill(categoryTopicProbabilities[c], 0);
        }

        for (int t = 0; t < numberOfTopics; ++t) {
            for (IntIntCursor topicWordCount : wordCounter.getCountsOfWordsAssignedToTopicAsMap(t)) {
                for (IntIntCursor topicWordInDocumentCount : wordCounter
                        .getCountsOfWordAssignedToTopicInDocumentsAsMap(topicWordCount.key, t)) {
                    for (int c = 0; c < categoriesOfDocuments[topicWordInDocumentCount.key].length; ++c) {
                        categoryTopicProbabilities[categoriesOfDocuments[topicWordInDocumentCount.key][c]][t] += topicWordInDocumentCount.value;
                    }
                }
            }
        }

        int sumOfAllWords = wordCounter.getSumOfAllWords();
        for (int c = 0; c < numberOfCategories; ++c) {
            for (int t = 0; t < numberOfTopics; ++t) {
                categoryTopicProbabilities[c][t] /= sumOfAllWords;
            }
        }

        return categoryTopicProbabilities;
    }

    private double fMeasure(double precision, double recall) {
        if ((precision > 0) || (recall > 0)) {
            return (2 * precision * recall) / (precision + recall);
        } else {
            return 0;
        }
    }

    private EvaluationResult calculatePrecisionRecallFMeasure(ObjectIntOpenHashMap<String> categoryMapping,
            double distributionOfCategoriesOverTopics[][], double topicProbabilities[], double categoryProbabilities[],
            String suffix) {

        EvaluationResultCollection results = new EvaluationResultCollection();
        double maxRecalls[] = new double[categoryMapping.size()];
        for (ObjectIntCursor<String> category : categoryMapping) {
            maxRecalls[category.value] = max(distributionOfCategoriesOverTopics[category.value]);
            results.addResult(new SingleEvaluationResult("maxRecall_" + suffix + "(" + category.key + ")",
                    maxRecalls[category.value]));
        }
        double expectedMaxRecall = StatisticalComputations.expectedValue(maxRecalls, categoryProbabilities);
        results.addResult(new SingleEvaluationResult("avg_" + suffix + "(Recall)", expectedMaxRecall));
        results.addResult(new SingleEvaluationResult("stdDev_" + suffix + "(Recall)", StatisticalComputations
                .standardDeviation(StatisticalComputations.variance(maxRecalls, categoryProbabilities,
                        expectedMaxRecall))));

        int numberOfTopics = probAlgState.getNumberOfTopics();
        double maxValue[] = new double[numberOfTopics];
        double sums[] = new double[numberOfTopics];
        for (ObjectIntCursor<String> category : categoryMapping) {
            for (int t = 0; t < numberOfTopics; ++t) {
                if (distributionOfCategoriesOverTopics[category.value][t] > maxValue[t]) {
                    maxValue[t] = distributionOfCategoriesOverTopics[category.value][t];
                }
                sums[t] += distributionOfCategoriesOverTopics[category.value][t];
            }
        }
        double maxPrecisions[] = new double[numberOfTopics];
        for (int t = 0; t < numberOfTopics; ++t) {
            if (sums[t] > 0) {
                maxPrecisions[t] = maxValue[t] / sums[t];
            } else {
                maxPrecisions[t] = 0;
            }
        }
        results.addResult(new EvaluationResultAsDoubleArray("maxPrecision_" + suffix, EvaluationResultDimension.TOPIC,
                maxPrecisions));
        double expectedMaxPrecision = StatisticalComputations.expectedValue(maxPrecisions, topicProbabilities);
        results.addResult(new SingleEvaluationResult("avg_" + suffix + "(Precision)", expectedMaxPrecision));
        results.addResult(new SingleEvaluationResult("stdDev_" + suffix + "(Precision)", StatisticalComputations
                .standardDeviation(StatisticalComputations.variance(maxPrecisions, topicProbabilities,
                        expectedMaxPrecision))));

        results.addResult(new SingleEvaluationResult("f-Measure_" + suffix, fMeasure(expectedMaxPrecision,
                expectedMaxRecall)));

        for (ObjectIntCursor<String> category : categoryMapping) {
            results.addResult(new EvaluationResultAsDoubleArray(category.key, EvaluationResultDimension.TOPIC,
                    distributionOfCategoriesOverTopics[category.value]));
        }
        return results;
    }

    private EvaluationResult calculateMutualInformation(double categoryTopicProbabilities[][],
            double topicProbabilities[], double categoryProbabilities[]) {
        EvaluationResultCollection results = new EvaluationResultCollection();

        double mutualInformation = 0;
        for (int c = 0; c < categoryProbabilities.length; ++c) {
            for (int t = 0; t < topicProbabilities.length; ++t) {
                if (categoryTopicProbabilities[c][t] > 0) {
                    mutualInformation += categoryTopicProbabilities[c][t]
                            * StatisticalComputations.log2(categoryTopicProbabilities[c][t]
                                    / (topicProbabilities[t] * categoryProbabilities[c]));
                }
            }
        }

        results.addResult(new SingleEvaluationResult("MI(C,T)", mutualInformation));
        results.addResult(new SingleEvaluationResult("nMI(C,T)", (2 * mutualInformation)
                / (StatisticalComputations.entropy(topicProbabilities) + StatisticalComputations
                        .entropy(categoryProbabilities))));

        return results;
    }

    private EvaluationResult calculatePurity(double distributionOfCategoriesOverTopics[][],
            double topicProbabilities[], String suffix) {
        EvaluationResultCollection results = new EvaluationResultCollection();

        int numberOfTopics = distributionOfCategoriesOverTopics[0].length;
        double purities[] = new double[numberOfTopics];
        Arrays.fill(purities, 0);
        double sum;
        for (int t = 0; t < numberOfTopics; t++) {
            sum = 0;
            for (int c = 0; c < distributionOfCategoriesOverTopics.length; c++) {
                sum += distributionOfCategoriesOverTopics[c][t];
                if (distributionOfCategoriesOverTopics[c][t] > purities[t]) {
                    purities[t] = distributionOfCategoriesOverTopics[c][t];
                }
            }
            if (sum > 0) {
                purities[t] /= sum; // normalize
            }
        }
        results.addResult(new EvaluationResultAsDoubleArray("Purity" + suffix, EvaluationResultDimension.TOPIC,
                purities));
        double expectedValue = StatisticalComputations.expectedValue(purities, topicProbabilities);
        results.addResult(new SingleEvaluationResult("E[Purity" + suffix + "]", expectedValue));
        results.addResult(new SingleEvaluationResult("stdDev(Purity" + suffix + ")", StatisticalComputations
                .standardDeviation(StatisticalComputations.variance(purities, topicProbabilities, expectedValue))));
        return results;
    }

    @Override
    public void setReportProvisionalResults(boolean reportProvisionalResults) {
        /* nothing to do */
    }
}