package org.dice_research.topicmodeling.algorithm.mallet.parallel;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

import cc.mallet.topics.TopicAssignment;
import cc.mallet.topics.WorkerRunnable;
import cc.mallet.util.Randoms;

/**
 * An extension of the {@link WorkerRunnable} that is optimized for parallel
 * processing.
 * 
 * A single run of the worker comprises the following steps:
 * <ol>
 * <li>Update local counts using the {@link SynchronizedCounts} instance</li>
 * <li>Sample topic assignments for all word tokens in the documents of the range of this worker</li>
 * <li>Upload global counts using the {@link SynchronizedCounts} instance</li>
 * <li>If the {@link #updateAlphaStatistics} flag is set, update the alpha statistics.</li>
 * </ol> 
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class ExtendedWorkerRunnable extends WorkerRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedWorkerRunnable.class);

    /**
     * Takes care of the thread synchronization when updating local and global
     * counts of threads.
     */
    protected SynchronizedCounts synchronizedCounts;
    /**
     * A histogram of the document lengths that is shared among the threads.
     */
    protected IntIntOpenHashMap docLengthHistogram;
    /**
     * Flag indicating whether alpha statistics should be updated within the next
     * call of {@link #run()}.
     */
    protected boolean updateAlphaStatistics = false;

    /**
     * Constructor.
     * 
     * @param numTopics          number of topics
     * @param alpha              hyperparameters alpha
     * @param alphaSum           sum of all alpha hyperparameters
     * @param beta               hyperparameter
     * @param random             the random number generator that will be used by
     *                           this worker
     * @param data               the data (i.e., the corpus) that will be used to
     *                           generate the topic model
     * @param typeTopicCounts    word type -> topic counts
     * @param tokensPerTopic     counts of tokens per topic
     * @param startDoc           id of the start document for this worker
     * @param numDocs            number of documents that the worker will use
     * @param synchronizedCounts the {@link SynchronizedCounts} instance that will
     *                           be used to synchronize the local and global counts
     *                           across the workers
     */
    public ExtendedWorkerRunnable(int numTopics, double[] alpha, double alphaSum, double beta, Randoms random,
            ArrayList<TopicAssignment> data, int[][] typeTopicCounts, int[] tokensPerTopic, int startDoc, int numDocs,
            SynchronizedCounts synchronizedCounts) {
        super(numTopics, alpha, alphaSum, beta, random, data, typeTopicCounts, tokensPerTopic, startDoc, numDocs);
        this.synchronizedCounts = synchronizedCounts;
    }

    @Override
    public void run() {
        try {
            // Get the local counts
            synchronizedCounts.updateLocalCounts(tokensPerTopic, typeTopicCounts);
            // Do the work of a typically worker
            super.run();
            // Update global counts
            synchronizedCounts.updateGlobalCounts(tokensPerTopic, typeTopicCounts);

            if (updateAlphaStatistics) {
                synchronizedCounts.updateGlobalDocCounts(topicDocCounts);
                updateAlphaStatistics = false;
            }
        } catch (Exception e) {
            LOGGER.error("Got an exception while running.", e);
        }
    }

    @Override
    public void collectAlphaStatistics() {
        super.collectAlphaStatistics();
        updateAlphaStatistics = true;
    }

//    @Override
//    public void buildLocalTypeTopicCounts() {
//        super.buildLocalTypeTopicCounts();
//        for (int i = 0; i < tokensPerTopic.length; ++i) {
//            if(tokensPerTopic[i] < 0) {
//                System.out.println("ERROR!");
//            }
//        }
//    }

}
