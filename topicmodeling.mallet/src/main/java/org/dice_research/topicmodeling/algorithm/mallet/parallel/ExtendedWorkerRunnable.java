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
 * processing
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class ExtendedWorkerRunnable extends WorkerRunnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendedWorkerRunnable.class);

    protected SynchronizedCounts synchronizedCounts;

    protected IntIntOpenHashMap docLengthHistogram;

    protected boolean updateAlphaStatistics = false;

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
