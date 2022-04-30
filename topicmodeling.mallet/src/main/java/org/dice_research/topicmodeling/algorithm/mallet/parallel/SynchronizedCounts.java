package org.dice_research.topicmodeling.algorithm.mallet.parallel;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedCounts {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedCounts.class);

//    private SyncRunnable syncThread;

    protected int[] globalTokensPerTopic; // indexed by <topic index>
    protected int[][] globalTypeTopicCounts; // indexed by <feature index, topic index>
    protected int numTypes;
    protected int numTopics;

    protected int numThreads;
    protected Semaphore firstThreadDoneMutex = new Semaphore(1);
    protected Semaphore threadsCopiedGlobalCounts = new Semaphore(0);
    protected boolean firstThreadDone = true;
    protected boolean firstThreadTokensPerTopic = true;
    // A bitset might not be threadsafe in this situation!
    protected boolean[] firstThreadTypeTopicCounts;

    // For alpha statistics
    protected int[][] globalTopicDocCounts;
    // A bitset might not be threadsafe in this situation!
    protected boolean[] firstThreadTopicDocCounts;

    protected int[] typeSums;
    protected boolean isDebug = false;

    // These values are used to encode type/topic counts as
    // count/topic pairs in a single int.
    public int topicMask;
    public int topicBits;

    public SynchronizedCounts(int numThreads, int[] tokensPerTopic, int[][] typeTopicCounts,
            int[][] globalTopicDocCounts) {
        this.globalTokensPerTopic = tokensPerTopic;
        numTopics = tokensPerTopic.length;
        this.globalTypeTopicCounts = typeTopicCounts;
        numTypes = typeTopicCounts.length;
        this.firstThreadTypeTopicCounts = new boolean[numTypes];
        // Copied from ParallelTopicModel class
        if (Integer.bitCount(numTopics) == 1) {
            // exact power of 2
            topicMask = numTopics - 1;
            topicBits = Integer.bitCount(topicMask);
        } else {
            // otherwise add an extra bit
            topicMask = Integer.highestOneBit(numTopics) * 2 - 1;
            topicBits = Integer.bitCount(topicMask);
        }
        this.numThreads = numThreads;
        this.globalTopicDocCounts = globalTopicDocCounts;
        this.firstThreadTopicDocCounts = new boolean[numTopics];
    }

    public void enableNextStep() {
        firstThreadDone = true;
        firstThreadTokensPerTopic = true;
        Arrays.fill(firstThreadTypeTopicCounts, true);
        Arrays.fill(firstThreadTopicDocCounts, true);
        // Check the counts
        if (isDebug) {
            int overallSum = 0;
            boolean check = true;
            if (typeSums == null) {
                typeSums = new int[globalTypeTopicCounts.length];
                check = false;
            }
            int localSum;
            for (int i = 0; i < globalTypeTopicCounts.length; ++i) {
                localSum = Arrays.stream(globalTypeTopicCounts[i]).filter(n -> n != 0).map(n -> n >> topicBits).sum();
                if ((check) && (localSum != typeSums[i])) {
                    System.out.println("Check failed!");
                }
                typeSums[i] = localSum;
                overallSum += localSum;
            }
            int topicCounts = Arrays.stream(globalTokensPerTopic).sum();
            if (topicCounts != overallSum) {
                System.out.println("Check failed!");
            }
        }
    }

    public void updateGlobalCounts(int[] localTokensPerTopic, int[][] localTypeTopicCounts) {
        try {
            waitForThreads2CopyGlobalCounts();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for a synchronization mutex. Aborting update method.", e);
            return;
        }
        // Update token counts per topic
        synchronized (globalTokensPerTopic) {
            if (firstThreadTokensPerTopic) {
                // Clear the topic totals
                Arrays.fill(globalTokensPerTopic, 0);
                firstThreadTokensPerTopic = false;
            }
            for (int topic = 0; topic < numTopics; topic++) {
                globalTokensPerTopic[topic] += localTokensPerTopic[topic];
//                if ((globalTokensPerTopic[topic] < 0) || (localTokensPerTopic[topic] < 0)) {
//                    System.out.println("ERROR!");
//                }
            }
        }
        // Update type topic counts (in parallel to speed up processing)
        IntStream.range(0, numTypes).parallel()
                .forEach(i -> updateGlobalTypeTopicCount(i, globalTypeTopicCounts[i], localTypeTopicCounts[i]));
    }

    private void waitForThreads2CopyGlobalCounts() throws InterruptedException {
        try {
            firstThreadDoneMutex.acquire();
            if (firstThreadDone) {
                try {
                    threadsCopiedGlobalCounts.acquire(numThreads);
                    firstThreadDone = false;
                } catch (Exception e) {
                    LOGGER.error("Interrupted while waiting for all threads to copy global counts", e);
                }
            }
        } finally {
            firstThreadDoneMutex.release();
        }
    }

    private void updateGlobalTypeTopicCount(int typeId, int[] globalTypeTopicCounts, int[] localTypeTopicCounts) {
        synchronized (globalTypeTopicCounts) {
            if (firstThreadTypeTopicCounts[typeId]) {
                // Clear the type/topic counts, only
                // looking at the entries before the first 0 entry.
                int position = 0;
                while (position < globalTypeTopicCounts.length && globalTypeTopicCounts[position] > 0) {
                    globalTypeTopicCounts[position] = 0;
                    position++;
                }
                firstThreadTypeTopicCounts[typeId] = false;
            }

            // Here the source is the individual thread counts,
            // and the target is the global counts.
            int sourceIndex = 0;
            int topic;
            int count;
            int targetIndex;
            int currentTopic;
            int currentCount;
            while (sourceIndex < localTypeTopicCounts.length && localTypeTopicCounts[sourceIndex] > 0) {
                topic = localTypeTopicCounts[sourceIndex] & topicMask;
                count = localTypeTopicCounts[sourceIndex] >> topicBits;
                targetIndex = 0;
                currentTopic = globalTypeTopicCounts[targetIndex] & topicMask;

                while (globalTypeTopicCounts[targetIndex] > 0 && currentTopic != topic) {
                    targetIndex++;
                    if (targetIndex == globalTypeTopicCounts.length) {
                        LOGGER.info("overflow in merging on type " + typeId);
                    }
                    currentTopic = globalTypeTopicCounts[targetIndex] & topicMask;
                }
                currentCount = globalTypeTopicCounts[targetIndex] >> topicBits;

                globalTypeTopicCounts[targetIndex] = ((currentCount + count) << topicBits) + topic;

                // Now ensure that the array is still sorted by
                // bubbling this value up.
                while (targetIndex > 0 && globalTypeTopicCounts[targetIndex] > globalTypeTopicCounts[targetIndex - 1]) {
                    int temp = globalTypeTopicCounts[targetIndex];
                    globalTypeTopicCounts[targetIndex] = globalTypeTopicCounts[targetIndex - 1];
                    globalTypeTopicCounts[targetIndex - 1] = temp;
                    targetIndex--;
                }
                sourceIndex++;
            }
        }
    }

    public void updateLocalCounts(int[] localTokensPerTopic, int[][] localTypeTopicCounts) throws InterruptedException {
//        syncThread.acquireLocalCountsUpdate();
        System.arraycopy(globalTokensPerTopic, 0, localTokensPerTopic, 0, numTopics);
        int index;
        int[] targetCounts;
        int[] sourceCounts;
        for (int type = 0; type < numTypes; type++) {
            targetCounts = localTypeTopicCounts[type];
            sourceCounts = globalTypeTopicCounts[type];

            index = 0;
            while (index < sourceCounts.length) {
                if (sourceCounts[index] != 0) {
                    targetCounts[index] = sourceCounts[index];
                } else if (targetCounts[index] != 0) {
                    targetCounts[index] = 0;
                } else {
                    break;
                }
                index++;
            }
        }
        // Local counts updated -> release 1
        threadsCopiedGlobalCounts.release();
    }

    public int[] getGlobalTokensPerTopic() {
        return globalTokensPerTopic;
    }

    public void updateGlobalDocCounts(int[][] localTopicDocCounts) {
        int[] globalCounts;
        int[] localCounts;
        for (int i = 0; i < numTopics; ++i) {
            globalCounts = globalTopicDocCounts[i];
            localCounts = localTopicDocCounts[i];
            synchronized (globalCounts) {
                if (firstThreadTopicDocCounts[i]) {
                    // reset the counts
                    Arrays.fill(globalCounts, 0);
                    firstThreadTopicDocCounts[i] = false;
                }
                for (int j = 0; j < localCounts.length; ++j) {
                    if (localCounts[j] > 0) {
                        globalCounts[j] += localCounts[j];
                    }
                }
            }
            Arrays.fill(localCounts, 0);
        }
    }

    @Deprecated
    protected static class SyncRunnable implements Runnable {

        private static final Logger LOGGER = LoggerFactory.getLogger(SyncRunnable.class);

        private Semaphore globalCountsUpdated = new Semaphore(0);
        private Semaphore countsReadForLocalUpdate = new Semaphore(0);
        private int numThreads;

        public SyncRunnable(int numThreads) {
            super();
            this.numThreads = numThreads;
        }

        @Override
        public void run() {
            while (true) {
                // Wait for all threads to update the global counts
                try {
                    globalCountsUpdated.acquire(numThreads);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted while waiting for threads to update the global counts. Aborting.", e);
                    return;
                }
                // Allow the threads to update their local counts
                countsReadForLocalUpdate.release(numThreads);
            }
        }

        public void reportGlobalCountsUpdated() {
            globalCountsUpdated.release();
        }

        public void acquireLocalCountsUpdate() throws InterruptedException {
            countsReadForLocalUpdate.acquire();
        }
    }
}
