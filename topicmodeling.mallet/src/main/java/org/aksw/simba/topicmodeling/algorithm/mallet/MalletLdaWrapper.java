package org.aksw.simba.topicmodeling.algorithm.mallet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.aksw.simba.topicmodeling.algorithms.ModelingAlgorithm;
import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.aksw.simba.topicmodeling.algorithms.ProbabilisticWordTopicModel;
import org.aksw.simba.topicmodeling.algorithms.WordCounter;
import org.aksw.simba.topicmodeling.algorithms.WordCounterImpl;
import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.ListCorpusCreator;
import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.DocumentListCorpus;
import org.aksw.simba.topicmodeling.utils.corpus.properties.CorpusVocabulary;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentClassificationResult;
import org.aksw.simba.topicmodeling.utils.doc.DocumentTextWordIds;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;
import org.aksw.simba.topicmodeling.utils.doc.ProbabilisticClassificationResult;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.vocabulary.DecoratingMultipleSpellingVocabulary;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;
import org.aksw.simba.topicmodeling.utils.vocabulary.VocabularyDecorator;
import org.aksw.simba.topicmodeling.utils.vocabulary.VocabularyMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.topics.WorkerRunnable;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.MalletAlphabetWrapper;
import cc.mallet.util.Randoms;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;

public class MalletLdaWrapper implements ModelingAlgorithm, ProbTopicModelingAlgorithmStateSupplier {

    private static final long serialVersionUID = 979333314591827173L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MalletLdaWrapper.class);

    private static final String MALLET_REGEX_TOKEN = "[\\p{L}-\\.]+";

    protected MalletLDATopicModeler topicModel;
    protected MalletAlphabetWrapper alphabet = new MalletAlphabetWrapper(new Alphabet());
    protected long seed;
    protected transient WordCounter wordCounter;

    private String malletRegexToken = MALLET_REGEX_TOKEN;

    public MalletLdaWrapper(int numberOfTopics) {
        seed = System.currentTimeMillis();
        topicModel = new MalletLDATopicModeler(numberOfTopics, seed);
        wordCounter = new WordCounterImpl(this);
    }

    public MalletLdaWrapper(int numberOfTopics, long seed) {
        this.seed = seed;
        topicModel = new MalletLDATopicModeler(numberOfTopics, seed);
        wordCounter = new WordCounterImpl(this);
    }

    public MalletLdaWrapper(int numberOfTopics, double alphaSum, double beta) {
        seed = System.currentTimeMillis();
        topicModel = new MalletLDATopicModeler(numberOfTopics, alphaSum, beta, seed);
        wordCounter = new WordCounterImpl(this);
    }

    public MalletLdaWrapper(int numberOfTopics, double alphaSum, double beta, long seed) {
        this.seed = seed;
        topicModel = new MalletLDATopicModeler(numberOfTopics, alphaSum, beta, seed);
        wordCounter = new WordCounterImpl(this);
    }

    public MalletLdaWrapper(LabelAlphabet topicAlphabet, double alphaSum, double beta) {
        seed = System.currentTimeMillis();
        topicModel = new MalletLDATopicModeler(topicAlphabet, alphaSum, beta, seed);
        wordCounter = new WordCounterImpl(this);
    }

    public MalletLdaWrapper(LabelAlphabet topicAlphabet, double alphaSum, double beta, long seed) {
        this.seed = seed;
        topicModel = new MalletLDATopicModeler(topicAlphabet, alphaSum, beta, seed);
        wordCounter = new WordCounterImpl(this);
    }

    @Override
    public Preprocessor createPreprocessor(DocumentSupplier supplier, Language lang) {
        return new ListCorpusCreator<LinkedList<Document>>(supplier, new DocumentListCorpus<LinkedList<Document>>(
                new LinkedList<Document>()));
    }

    @Override
    public void initialize(Corpus corpus) {
        CorpusVocabulary cVocab = corpus.getProperty(CorpusVocabulary.class);
        if (cVocab != null) {
            directInitialization(corpus, cVocab.get());
        } else {
            LOGGER.warn("Couldn't find a vocabulary for this corpus. I will have to recreate every document to initialize Mallet (deprecated and expensive)!");
            // Begin by importing documents from text to feature sequences
            ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
            // Pipes: lowercase, tokenize, remove stopwords, map to features
            pipeList.add(new CharSequenceLowercase());
            pipeList.add(new CharSequence2TokenSequence(Pattern.compile(malletRegexToken)));
            // pipeList.add( new TokenSequenceRemoveStopwords(new
            // File("stoplists/en.txt"), "UTF-8", false, false, false) );
            pipeList.add(new TokenSequence2FeatureSequence(alphabet.getAlphabet()));

            InstanceList instances = new InstanceList(new SerialPipes(pipeList));
            // try to get the texts using TermTokenizedText
            instances.addThruPipe(new StringArrayIterator(getTokenizedTermsAsText((DocumentListCorpus<?>) corpus)));
            createMultipleSpellingVocabulary((DocumentListCorpus<?>) corpus);
            topicModel.initialize(instances);
        }
    }

    protected String[] getTokenizedTermsAsText(DocumentListCorpus<?> corpus) {
        Iterator<Document> iterator = corpus.getIterator();
        TermTokenizedText ttText;
        ArrayList<String> texts = new ArrayList<String>(corpus.size());
        StringBuilder text = new StringBuilder();
        while (iterator.hasNext()) {
            ttText = iterator.next().getProperty(TermTokenizedText.class);
            if (ttText != null) {
                for (Term term : ttText.getTermTokenizedText()) {
                    text.append(term.getLemma());
                    text.append(' ');
                }
                texts.add(text.toString());
                text.delete(0, text.length());
            } else {
                LOGGER.warn("got a Document withouth a TermTokenizedText property!");
            }
        }
        return texts.toArray(new String[0]);
    }

    private void directInitialization(Corpus corpus, Vocabulary vocabulary) {
        String words[] = new String[vocabulary.size()];
        for (String word : vocabulary) {
            words[vocabulary.getId(word)] = word;
        }
        Alphabet alphabet = new Alphabet(words);
        LOGGER.info("created Mallet Alphabet.");

        InstanceList instances = new InstanceList(alphabet, null);

        Iterator<Document> iterator = corpus.iterator();
        Document document;
        Instance instance;
        while (iterator.hasNext()) {
            document = iterator.next();
            instance = createInstanceFromDocument(document, alphabet);
            if (instance != null) {
                instances.add(instance);
                // if (instances.size() == 1000) {
                // topicModel.initialize(instances);
                // for (int i = 999; i >= 0; --i) {
                // instances.remove(i);
                // }
                // }
            }
        }
        topicModel.initialize(instances);
    }

    protected Instance createInstanceFromDocument(Document document, Alphabet alphabet) {
        URI documentUri;
        try {
            documentUri = new URI(Integer.toString(document.getDocumentId()));
        } catch (URISyntaxException e) {
            LOGGER.error("Couldn't create simple URI for document #" + document.getDocumentId() + ". Returning null.",
                    e);
            return null;
        }
        DocumentTextWordIds wordIds = document.getProperty(DocumentTextWordIds.class);
        int features[];
        if (wordIds == null) {
            DocumentWordCounts wordCounts = document.getProperty(DocumentWordCounts.class);
            if (wordCounts == null) {
                LOGGER.error("Couldn't create Mallet instance of document #" + document.getDocumentId()
                        + " because it has no DocumentTextWordIds and DocumentWordCounts properties. Returning null.");
                return null;
            } else {
                IntArrayList featuresList = new IntArrayList();
                for (IntIntCursor wordCount : wordCounts.getWordCounts()) {
                    for (int i = 0; i < wordCount.value; ++i) {
                        featuresList.add(wordCount.key);
                    }
                }
                features = featuresList.toArray();
            }
        } else {
            features = wordIds.getWordIds();
        }
        return new Instance(new FeatureSequence(alphabet, features), null, documentUri, null);
    }

    protected void createMultipleSpellingVocabulary(DocumentListCorpus<?> corpus) {
        Iterator<Document> iterator = corpus.getIterator();
        TermTokenizedText ttText;
        DecoratingMultipleSpellingVocabulary vocabulary = new DecoratingMultipleSpellingVocabulary(alphabet);
        while (iterator.hasNext()) {
            ttText = iterator.next().getProperty(TermTokenizedText.class);
            if (ttText != null) {
                for (Term term : ttText.getTermTokenizedText()) {
                    vocabulary.addSpelling(alphabet.getId(term.getLemma()), term.getWordForm());
                }
            } else {
                LOGGER.warn("got a Document withouth a TermTokenizedText property!");
            }
        }
        topicModel.setVocabularyDecorator(vocabulary);
    }

    @Override
    public void performNextStep() {
        topicModel.estimate();
        wordCounter.clear();
    }

    @Override
    public Model getModel() {
        return topicModel;
    }

    @Override
    public int[] getWordTopicAssignmentForDocument(int documentId) {
        return this.topicModel.data.get(documentId).topicSequence.toFeatureIndexSequence();
    }

    @Override
    public int[] getWordsOfDocument(int documentId) {
        return ((FeatureSequence) this.topicModel.data.get(documentId).instance.getData()).toFeatureIndexSequence();
    }

    @Override
    public int getNumberOfTopics() {
        return topicModel.numTopics;
    }

    @Override
    public int getNumberOfDocuments() {
        return topicModel.data.size();
    }

    public int getNumberOfWords() {
        if (topicModel.getVocabulary() != null) {
            return topicModel.getVocabulary().size();
        } else {
            return 0;
        }
    }

    @Override
    public Vocabulary getVocabulary() {
        Vocabulary vocabulary = topicModel.getVocabulary();
        if (vocabulary == null) {
            return alphabet;
        } else {
            return vocabulary;
        }
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public WordCounter getWordCounts() {
        // The wordCounter is null if this object has been deserialized
        if (wordCounter == null) {
            wordCounter = new WordCounterImpl(this);
        }
        return wordCounter;
    }

    public void setMalletRegexToken(String malletRegexToken) {
        this.malletRegexToken = malletRegexToken;
    }

    /**
     * Set to 0 to turn optimization off.
     * 
     * @param interval
     */
    public void setOptimizeInterval(int interval) {
        topicModel.setOptimizeInterval(interval);
    }

    protected static class MalletLDATopicModeler extends ParallelTopicModel implements ProbabilisticWordTopicModel {
        private static final long serialVersionUID = 6008315609404219023L;

        protected static final Logger logger = LoggerFactory.getLogger(MalletLDATopicModeler.class);

        protected transient WorkerRunnable[] runnables = new WorkerRunnable[1];
        protected int iteration;
        protected transient int inferencerVersion = 0;
        protected transient TopicInferencer inferencer;
        protected Vocabulary vocabulary = null;

        protected double wordTopicWeights[][];
        protected double topicWeights[];

        public MalletLDATopicModeler(int numberOfTopics, long seed) {
            super(numberOfTopics);
            randomSeed = (int) seed;
        }

        public MalletLDATopicModeler(int numberOfTopics, double alphaSum, double beta, long seed) {
            super(numberOfTopics, alphaSum, beta);
            randomSeed = (int) seed;
        }

        public MalletLDATopicModeler(LabelAlphabet topicAlphabet, double alphaSum, double beta, long seed) {
            super(topicAlphabet, alphaSum, beta);
            randomSeed = (int) seed;
        }

        public void initialize(InstanceList instances) {
            addInstances(instances);
            showTopicsInterval = 0; // turn of the logging for the topics
            saveModelInterval = 0; // turn of saving the model after an
                                   // iteration

            // If there is only one thread, copy the typeTopicCounts
            // arrays directly, rather than allocating new memory.

            Randoms random = null;
            random = new Randoms(randomSeed);

            runnables[0] = new WorkerRunnable(numTopics, alpha, alphaSum, beta, random, data, typeTopicCounts,
                    tokensPerTopic, 0, data.size());

            runnables[0].initializeAlphaStatistics(docLengthCounts.length);
            // If there is only one thread, we
            // can avoid communications overhead.
            // This switch informs the thread not to
            // gather statistics for its portion of the data.
            runnables[0].makeOnlyThread();

            iteration = 0;
            inferencerVersion = 0;
        }

        @Override
        public void estimate() {
            wordTopicWeights = null;
            topicWeights = null;
            ++iteration;
            if (iteration > burninPeriod && optimizeInterval != 0 && iteration % saveSampleInterval == 0) {
                runnables[0].collectAlphaStatistics();
            }
            runnables[0].run();
            if (iteration > burninPeriod && optimizeInterval != 0 && iteration % optimizeInterval == 0) {
                optimizeAlpha(runnables);
                optimizeBeta(runnables);
            }
        }

        @Override
        public double getSmoothedProbabilityOfWord(int wordId, int topicId) {
            calculateSmoothedWeights();
            return wordTopicWeights[wordId][topicId];
        }

        @Override
        public double getProbabilityOfWord(int wordId, int topicId) {
            int topicCounts[] = typeTopicCounts[wordId];
            int index = 0;
            while (index < topicCounts.length && topicCounts[index] > 0) {
                if ((topicCounts[index] & topicMask) == topicId) {
                    return (topicCounts[index] >> topicBits) / (double) tokensPerTopic[topicId];
                }
                index++;
            }
            return 0d;
        }

        @Override
        public double getSmoothedProbabilityOfTopic(int topicId) {
            calculateSmoothedWeights();
            return topicWeights[topicId];
        }

        @Override
        public int getNumberOfTopics() {
            return this.numTopics;
        }

        protected void calculateSmoothedWeights() {
            if (wordTopicWeights != null) {
                return;
            }

            wordTopicWeights = new double[numTypes][numTopics];
            topicWeights = new double[numTopics];
            int currentTopic, index;
            int[] topicCounts;
            for (int word = 0; word < numTypes; word++) {
                topicCounts = typeTopicCounts[word];
                index = 0;
                Arrays.fill(wordTopicWeights[word], 0);
                while (index < topicCounts.length && topicCounts[index] > 0) {
                    currentTopic = topicCounts[index] & topicMask;
                    wordTopicWeights[word][currentTopic] = beta + (topicCounts[index] >> topicBits);
                    index++;
                }
            }

            double topicWeightingSum = 0;
            for (int topic = 0; topic < numTopics; topic++) {
                topicWeights[topic] = alpha[topic] + tokensPerTopic[topic];
                topicWeightingSum += topicWeights[topic];
            }
            double weightingSum;
            for (int topic = 0; topic < numTopics; topic++) {
                topicWeights[topic] /= topicWeightingSum;
                weightingSum = (beta * numTypes) + tokensPerTopic[topic];
                for (int word = 0; word < numTypes; word++) {
                    wordTopicWeights[word][topic] = (wordTopicWeights[word][topic] == 0 ? beta
                            : wordTopicWeights[word][topic]) / weightingSum;
                }
            }
        }

        @Override
        public double[] getTopicProbabilitiesForDocument(DocumentWordCounts wordCounts) {
            if (inferencerVersion < iteration) {
                inferencer = this.getInferencer();
            }
            double inferencedTopicProbabilities[];

            // IntArrayList tokens = new IntArrayList();
            int sumOfWordCounts = wordCounts.getSumOfWordCounts();
            int tokens[] = new int[sumOfWordCounts];
            int index = 0;
            IntIntOpenHashMap counts = wordCounts.getWordCounts();
            for (int i = 0; i < counts.allocated.length; ++i) {
                if (counts.allocated[i]) {
                    for (int j = 0; j < counts.values[i]; ++j) {
                        tokens[index] = counts.keys[i];
                        ++index;
                    }
                    // tokens.add(wordCount.key);
                }
            }
            FeatureSequence fs = new FeatureSequence(this.alphabet, tokens);
            // FeatureSequence fs = new FeatureSequence(this.alphabet,
            // tokens.toArray());
            Instance instance = new Instance(fs, null, null, null);
            inferencedTopicProbabilities = inferencer.getSampledDistribution(instance, 1, 50, 50);

            return inferencedTopicProbabilities;
        }

        @Override
        public Vocabulary getVocabulary() {
            if (vocabulary != null) {
                return vocabulary;
            } else {
                if (alphabet == null) {
                    return null;
                } else {
                    return new MalletAlphabetWrapper(alphabet);
                }
            }
        }

        public void setVocabularyDecorator(VocabularyDecorator vocabulary) {
            this.vocabulary = vocabulary;
        }

        @Override
        public void setVersion(int version) {
            this.iteration = version;
        }

        @Override
        public int getVersion() {
            return iteration;
        }

        @Override
        public VocabularyMapping getVocabularyMapping(Vocabulary otherVocabulary) {
            return VocabularyMapping.createMapping(this.getVocabulary(), otherVocabulary);
        }

        @Override
        public DocumentClassificationResult getClassificationForDocument(Document document) {
            DocumentWordCounts wordCounts = document.getProperty(DocumentWordCounts.class);
            if (wordCounts == null) {
                throw new IllegalArgumentException("Expected a Document with the " + DocumentWordCounts.class
                        + " property.");
            }
            return new ProbabilisticClassificationResult(getTopicProbabilitiesForDocument(wordCounts), this);
        }
    }
}
