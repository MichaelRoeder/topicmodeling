package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.algorithms.WordCounter;
import org.dice_research.topicmodeling.io.CorpusBasedFileProcessor;
import org.dice_research.topicmodeling.lang.Dictionary;
import org.dice_research.topicmodeling.utils.vocabulary.MultipleSpellingVocabulary;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.dice_research.topicmodeling.wordnet.utils.StatisticalComputations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntDoubleOpenHashMap;
import com.carrotsearch.hppc.cursors.IntDoubleCursor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hppc.HppcModule;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class InformationContentTree implements CorpusBasedFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(InformationContentTree.class);

    private static final String INFORMATION_CONTENT_FILE_NAME = "informationContentTree_$APPENDIX$.object";

    private static InformationContentTree informationContentTree = null;

    public synchronized static final InformationContentTree createInformationContentTree(
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary translationDictionary,
            IDictionary wordNetDictionary) {
        if (informationContentTree == null) {
            informationContentTree = readFromBinaryFile();
            if (informationContentTree == null) {
                logger.info("Couldn't load information content tree. Creating new InformationContentTree object.");
                IntDoubleOpenHashMap hashMap = new IntDoubleOpenHashMap();
                double maxInformationContent = createNewInformationContentTree(stateSupplier, translationDictionary,
                        wordNetDictionary, hashMap);
                informationContentTree = new InformationContentTree(hashMap, maxInformationContent);
                saveAsBinaryFile(informationContentTree);
            }
        }
        return informationContentTree;
    }

    public synchronized static final InformationContentTree getInformationContentTree() {
        return informationContentTree;
    }

    public static void saveAsBinaryFile(InformationContentTree icTree) {
        logger.info("Saving information content tree to file.");
        FileOutputStream fout = null;
        try {
            String filename = INFORMATION_CONTENT_FILE_NAME;
            if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
            } else {
                filename = filename.replace("$APPENDIX$", "");
            }
            fout = new FileOutputStream(filename);
            // OpenHashMapDeSerializer.serializeObjectDoubleOpenHashMap(
            // bufferedRelatedness, fout);

            long doubleBits = Double.doubleToRawLongBits(icTree.getMaxInformationContent());
            byte bytes[] = new byte[8];
            bytes[0] = (byte) (doubleBits >> 56);
            bytes[1] = (byte) (doubleBits >> 48);
            bytes[2] = (byte) (doubleBits >> 40);
            bytes[3] = (byte) (doubleBits >> 32);
            bytes[4] = (byte) (doubleBits >> 24);
            bytes[5] = (byte) (doubleBits >> 16);
            bytes[6] = (byte) (doubleBits >> 8);
            bytes[7] = (byte) doubleBits;
            fout.write(bytes);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new HppcModule());
            mapper.writeValue(fout, icTree.getTree());
        } catch (IOException e) {
            logger.error("Error while writing the serialized information content tree to file.", e);
        } finally {
            try {
                fout.close();
            } catch (Exception e) {
            }
        }
    }

    private static InformationContentTree readFromBinaryFile() {
        InformationContentTree icTree = null;
        FileInputStream fin = null;
        try {
            String filename = INFORMATION_CONTENT_FILE_NAME;
            if (System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX) != null) {
                filename = filename.replace("$APPENDIX$", System.getProperty(SYSTEM_PROPERTY_NAME_FILE_APPENDIX));
            } else {
                filename = filename.replace("$APPENDIX$", "");
            }
            fin = new FileInputStream(filename);
            long bits = 0;
            for(int i = 0; i < 8; ++i) {
                bits <<= 8;
                bits = bits | (0x00000000000000FFL & ((long) fin.read()));
            }
            double maxInformationContent = Double.longBitsToDouble(bits);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            IntDoubleOpenHashMap map = mapper.readValue(fin, IntDoubleOpenHashMap.class);

            icTree = new InformationContentTree(map, maxInformationContent);
        } catch (IOException e) {
            logger.warn("Error while trying to read serialized information content tree from file", e);
        } finally {
            try {
                fin.close();
            } catch (Exception e) {
            }
        }
        return icTree;
    }

    private static double createNewInformationContentTree(
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary translationDictionary,
            IDictionary wordNetDictionary, IntDoubleOpenHashMap icTree) {
        int sumOfAllWordCounts = determineFrequencies(icTree, stateSupplier, translationDictionary, wordNetDictionary);
        calculateInformationContent(icTree, sumOfAllWordCounts);
        return -StatisticalComputations.log2(1.0 / sumOfAllWordCounts);
    }

    private static int determineFrequencies(IntDoubleOpenHashMap frequencyList,
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary translationDictionary,
            IDictionary wordNetDictionary) {
        PathFinder pathFinder = new PathFinder(wordNetDictionary);
        WordCounter wordCounter = stateSupplier.getWordCounts();
        Vocabulary vocabulary = stateSupplier.getVocabulary();
        MultipleSpellingVocabulary msVocabulary = vocabulary instanceof MultipleSpellingVocabulary ? (MultipleSpellingVocabulary) vocabulary
                : null;
        Iterator<String> iterator = vocabulary.iterator();
        String word, translation;
        int wordId, wordCount, sumOfAllWordCounts = 0;
        IIndexWord idxWord;
        Iterator<IWordID> meaningsIterator;
        List<List<ISynset>> hypernymTrees = new ArrayList<List<ISynset>>();
        ISynset synset;
        Set<String> wordSpellings = new HashSet<String>();
        while (iterator.hasNext()) {
            word = iterator.next();
            wordId = vocabulary.getId(word);

            wordSpellings.clear();
            wordSpellings.add(word);
            if (msVocabulary != null) {
                wordSpellings.addAll(msVocabulary.getSpellingsForWord(wordId));
            }
            for (String spelling : wordSpellings) {
                translation = translationDictionary.translate(spelling);
                if ((translation == null) || (translation.length() == 0)) {
                    translation = spelling;
                }
                idxWord = wordNetDictionary.getIndexWord(translation, POS.NOUN);
                if (idxWord != null) {
                    meaningsIterator = idxWord.getWordIDs().iterator();
                    wordCount = wordCounter.getCountOfWord(wordId);
                    while (meaningsIterator.hasNext()) {
                        synset = wordNetDictionary.getWord(meaningsIterator.next()).getSynset();
                        hypernymTrees.addAll(pathFinder.getHypernymTrees(synset));
                    }
                    increaseCountForSynset(frequencyList, hypernymTrees, wordCount);
                    sumOfAllWordCounts += wordCount;
                } else {
                    // logger.warn("Couldn't find \"" + translation + "\" ("
                    // + POS.NOUN.toString() + ") in the WordNet.");
                }
            }
        }
        logger.info("Counted " + sumOfAllWordCounts + " word occurrences inside the Information Content Tree.");
        return sumOfAllWordCounts;
    }

    private static void increaseCountForSynset(IntDoubleOpenHashMap icTree, List<List<ISynset>> hypernymTree,
            int count) {
        HashSet<ISynset> synsets = new HashSet<ISynset>();
        Iterator<List<ISynset>> iterator = hypernymTree.iterator();
        while (iterator.hasNext()) {
            synsets.addAll(iterator.next());
        }
        increaseCountOfSynsets(icTree, synsets, count);
    }

    private static void increaseCountOfSynsets(IntDoubleOpenHashMap icTree, Set<ISynset> synsets,
            int count) {
        Iterator<ISynset> iterator = synsets.iterator();
        while (iterator.hasNext()) {
            icTree.putOrAdd(iterator.next().getOffset(), count, count);
        }
    }

    private static void calculateInformationContent(IntDoubleOpenHashMap icTree, int sumOfAllWordCounts) {
        Iterator<IntDoubleCursor> iterator = icTree.iterator();
        IntDoubleCursor cursor;
        while (iterator.hasNext()) {
            cursor = iterator.next();
            icTree.put(cursor.key, -StatisticalComputations.log2(cursor.value / sumOfAllWordCounts));
        }
    }

    private final IntDoubleOpenHashMap icTree;
    private final double maxInformationContent;

    private InformationContentTree(IntDoubleOpenHashMap icTree, double maxInformationContent) {
        this.icTree = icTree;
        this.maxInformationContent = maxInformationContent;
    }

    private IntDoubleOpenHashMap getTree() {
        return this.icTree;
    }

    public double getMaxInformationContent() {
        return maxInformationContent;
    }

    public double getInformationContent(ISynsetID id) {
        double ic = 0;
        if (icTree.containsKey(id.getOffset())) {
            ic = icTree.get(id.getOffset());
        } else {
            // logger.warn("There is no information content in the Information Content Tree for a Synset with the offset "
            // + id.getOffset() + ". Maybe this is the wrong tree for this corpus?");
            ic = maxInformationContent;
        }
        return ic;
    }
}
