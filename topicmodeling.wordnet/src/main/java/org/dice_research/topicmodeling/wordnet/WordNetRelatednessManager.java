package org.dice_research.topicmodeling.wordnet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.io.CorpusBasedFileProcessor;
import org.dice_research.topicmodeling.lang.Dictionary;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.RelatednessCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hppc.HppcModule;

import edu.mit.jwi.item.POS;

public class WordNetRelatednessManager implements CorpusBasedFileProcessor {

    private static final boolean DESTROY_MANAGERS_IF_NOT_REQUIRED = false;

    private static final float LOAD_FACTOR = 0.1f;

    private static final String RELATEDNESS_FILE_NAME = "wordSimilarities_$APPENDIX$_$METHOD$.object";

    private static final Logger logger = LoggerFactory.getLogger(WordNetRelatednessManager.class);

    private static ObjectObjectOpenHashMap<WordNetRelatednessCalculationMethods, WordNetRelatednessManager> existingManagers;
    private static int UserCountOfSingleManager[];
    private static Semaphore ManagerMutexes[];

    protected RelatednessCalculator calculator;

    protected WordNetRelatednessCalculationMethods calculationMethod;

    protected Semaphore bufferedRelatednessMutex = new Semaphore(1);

    protected IntDoubleOpenHashMap bufferedRelatedness;

    protected boolean localRelatednessChanged = false;

    private WordNetRelatednessManager() {
    }

    private static synchronized void initWordNetRelatednessManagerClass() {
        if (ManagerMutexes == null) {
            ManagerMutexes = new Semaphore[WordNetRelatednessCalculationMethods.values().length];
            UserCountOfSingleManager = new int[WordNetRelatednessCalculationMethods.values().length];
            existingManagers = new ObjectObjectOpenHashMap<WordNetRelatednessCalculationMethods, WordNetRelatednessManager>();
            for (int i = 0; i < WordNetRelatednessCalculationMethods.values().length; ++i) {
                UserCountOfSingleManager[i] = 0;
                ManagerMutexes[i] = new Semaphore(1);
            }
        }
    }

    public static WordNetRelatednessManager getWordNetRelatednessManager(WordNetRelatednessCalculationMethods method,
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary dictionary) {
        initWordNetRelatednessManagerClass();
        // get the mutex for the requested manager
        try {
            ManagerMutexes[method.ordinal()].acquire();
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for the mutex of the requested Manager. Returning null.", e);
            return null;
        }
        if (existingManagers.containsKey(method)) {
            ++UserCountOfSingleManager[method.ordinal()];
            logger.trace("Now there are " + UserCountOfSingleManager[method.ordinal()]
                    + " users of the WordNetRelatednessManager using the " + method.toString() + " method.");
            ManagerMutexes[method.ordinal()].release();
            return existingManagers.get(method);
        }
        // ObjectDoubleOpenHashMap<String> hashMap = readFromBinaryFile(method);
        IntDoubleOpenHashMap hashMap = readFromBinaryFile(method);
        if (hashMap == null) {
            logger.warn("Couldn't load word similarities. Creating new empty WordNetRelatednessManager object.");
            // hashMap = new ObjectDoubleOpenHashMap<String>();
            hashMap = new IntDoubleOpenHashMap();
        }
        WordNetRelatednessManager manager = new WordNetRelatednessManager();
        manager.bufferedRelatedness = IntDoubleOpenHashMap.newInstance(hashMap.size(), LOAD_FACTOR);
        manager.bufferedRelatedness.allocated = hashMap.allocated;
        manager.bufferedRelatedness.assigned = hashMap.assigned;
        manager.bufferedRelatedness.keys = hashMap.keys;
        manager.bufferedRelatedness.values = hashMap.values;
        manager.setRelatednessCalculator(method, stateSupplier, dictionary);
        existingManagers.put(method, manager);
        UserCountOfSingleManager[method.ordinal()] = 1;
        logger.trace("Now there are " + UserCountOfSingleManager[method.ordinal()]
                + " users of the WordNetRelatednessManager using the " + method.toString() + " method.");
        ManagerMutexes[method.ordinal()].release();
        return manager;
    }

    public static void releaseWordNetRelatednessManager(WordNetRelatednessManager manager) {
        initWordNetRelatednessManagerClass();
        WordNetRelatednessCalculationMethods method = manager.getCalculationMethod();
        // get the mutex for the requested manager
        try {
            ManagerMutexes[method.ordinal()].acquire();
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for the mutex of the released Manager. Aborting.", e);
            return;
        }
        --UserCountOfSingleManager[method.ordinal()];
        logger.trace("Now there are " + UserCountOfSingleManager[method.ordinal()]
                + " users of the WordNetRelatednessManager using the " + method.toString() + " method.");
        // if this manager is not needed anymore
        if (UserCountOfSingleManager[method.ordinal()] == 0) {
            manager.saveAsBinaryFile();
            if (DESTROY_MANAGERS_IF_NOT_REQUIRED) {
                logger.info("Destroying WordNetRelatednessManager with the " + method.toString() + " method.");
                existingManagers.remove(method);
            }
        }
        ManagerMutexes[method.ordinal()].release();
    }

    // protected static ObjectDoubleOpenHashMap<String> readFromBinaryFile(
    protected static IntDoubleOpenHashMap readFromBinaryFile(WordNetRelatednessCalculationMethods method) {
        // ObjectDoubleOpenHashMap<String> map = null;
        IntDoubleOpenHashMap map = null;
        FileInputStream fin = null;
        try {
            String filename = RELATEDNESS_FILE_NAME;
            if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
            } else {
                filename = filename.replace("$APPENDIX$", "");
            }
            filename = filename.replace("$METHOD$", method.name());
            fin = new FileInputStream(filename);
            // map = OpenHashMapDeSerializer
            // .deserializeObjectDoubleOpenHashMap(fin);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            map = mapper.readValue(fin, IntDoubleOpenHashMap.class);
        } catch (IOException e) {
            logger.warn("Error while trying to read serialized word similarities  from file", e);
        } finally {
            try {
                fin.close();
            } catch (Exception e) {
            }
        }
        return map;
    }

    private void setRelatednessCalculator(WordNetRelatednessCalculationMethods method,
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary dictionary) {
        calculationMethod = method;
        calculator = RelatednessCalculatorFactory.createRelatednessCalculator(method, stateSupplier, dictionary);
    }

    public double getRelatednessOfWords(String word1, POS word1PosTag, String word2, POS word2PosTag) {
        // String key;
        int key;
        if (word1.compareTo(word2) < 0) {
            key = (word1 + "|" + word2).hashCode();
        } else {
            key = (word2 + "|" + word1).hashCode();
        }
        if (!bufferedRelatedness.containsKey(key)) {
            // get the mutex to write into the hash map
            try {
                bufferedRelatednessMutex.acquire();
            } catch (InterruptedException e) {
                logger.error(
                        "Interrupted while waiting for mutex to write into the buffered relatedness hash map. Returning 0 as similarity.",
                        e);
                return 0;
            }
            // check again (maybe it was written while acquiring the mutex
            if (!bufferedRelatedness.containsKey(key)) {
                bufferedRelatedness.put(key, calculator.calcRelatednessOfWords(word1, word1PosTag, word2, word2PosTag));
                localRelatednessChanged = true;
            }
            bufferedRelatednessMutex.release();
        }
        return bufferedRelatedness.get(key);
    }

    public double getRelatednessOfWords(String word1Spellings[], POS word1PosTag, String word2Spellings[],
            POS word2PosTag) {
        // String key;
        int key;
        if (word1Spellings[0].compareTo(word2Spellings[0]) < 0) {
            key = (word1Spellings[0] + "|" + word2Spellings[0]).hashCode();
        } else {
            key = (word2Spellings[0] + "|" + word1Spellings[0]).hashCode();
        }
        if (!bufferedRelatedness.containsKey(key)) {
            // get the mutex to write into the hash map
            try {
                bufferedRelatednessMutex.acquire();
            } catch (InterruptedException e) {
                logger.error(
                        "Interrupted while waiting for mutex to write into the buffered relatedness hash map. Returning 0 as similarity.",
                        e);
                return 0;
            }
            // check again (maybe it was written while acquiring the mutex
            if (!bufferedRelatedness.containsKey(key)) {
                double relatedness = calculator.getMinPossibleValue();
                for (int i = 0; i < word1Spellings.length; ++i) {
                    for (int j = 0; j < word2Spellings.length; ++j) {
                        relatedness = Math.max(relatedness, calculator.calcRelatednessOfWords(
                                word1Spellings[i], word1PosTag, word2Spellings[j], word2PosTag));
                    }
                }
                bufferedRelatedness.put(key, relatedness);
                localRelatednessChanged = true;
            }
            bufferedRelatednessMutex.release();
        }
        return bufferedRelatedness.get(key);
    }

    public void saveAsBinaryFile() {
        // get the mutex to write into the hash map
        try {
            bufferedRelatednessMutex.acquire();
        } catch (InterruptedException e) {
            logger.error(
                    "Interrupted while waiting for mutex to write the buffered relatedness hash map to a file. Aborting.",
                    e);
            return;
        }
        if (localRelatednessChanged) {
            logger.info("Saving WordNetRelatednessManager with the " + this.calculationMethod.toString()
                    + " method to file.");
            FileOutputStream fout = null;
            try {
                String filename = RELATEDNESS_FILE_NAME;
                if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                    filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
                } else {
                    filename = filename.replace("$APPENDIX$", "");
                }
                filename = filename.replace("$METHOD$", calculationMethod.name());
                fout = new FileOutputStream(filename);
                // OpenHashMapDeSerializer.serializeObjectDoubleOpenHashMap(
                // bufferedRelatedness, fout);
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new HppcModule());
                mapper.writeValue(fout, bufferedRelatedness);
                localRelatednessChanged = false;
            } catch (IOException e) {
                logger.error("Error while writing the serialized word similarities to file.", e);
            } finally {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
        }
        bufferedRelatednessMutex.release();
    }

    public WordNetRelatednessCalculationMethods getCalculationMethod() {
        return calculationMethod;
    }

    public void ckeckCorrectnessOfBufferedData() {
        // Iterator<ObjectDoubleCursor<String>> iterator = bufferedRelatedness
        // .iterator();
        // ObjectDoubleCursor<String> cursor;
        Iterator<IntDoubleCursor> iterator = bufferedRelatedness.iterator();
        IntDoubleCursor cursor;
        while (iterator.hasNext()) {
            cursor = iterator.next();
            if ((cursor.value < 0) || (cursor.value > 1))
                logger.error(cursor.key + " has a value which is outside of the values space: " + cursor.value);
        }
    }
    
    public double getMaxPossibleRelatedness() {
        return calculator.getMaxPossibleValue();
    }
    
    public double getMinPossibleRelatedness() {
        return calculator.getMinPossibleValue();
    }
}
