package org.dice_research.topicmodeling.preprocessing.consume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.StreamSupport;

import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.DocumentListCorpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.vocabulary.SimpleVocabulary;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DocumentFrequencyDeterminerTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<Object[]>();
        // 4 simple documents
        data.add(new Object[] {
                new int[][] { new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 1, 2, 2, 2, 2 },
                        new int[] { 5, 5, 5, 4, 0 }, new int[] { 5 } },
                new int[][] { new int[] { 1, 1, 1, 1, 1, 1 }, new int[] { 1, 1, 4, 0, 0, 0 },
                        new int[] { 1, 0, 0, 0, 1, 3 }, new int[] { 0, 0, 0, 0, 0, 1 } },
                new int[] { 3, 2, 2, 1, 2, 3, 0 } });
        // 4 documents without counts
        data.add(new Object[] {
                new int[][] { new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 1, 2, 2, 2, 2 },
                        new int[] { 5, 5, 5, 4, 0 }, new int[] { 5 } },
                new int[][] { null, null, null, null }, new int[] { 3, 2, 2, 1, 2, 3, 0 } });
        // 4 documents without single word ids
        data.add(new Object[] { new int[][] { null, null, null, null },
                new int[][] { new int[] { 1, 1, 1, 1, 1, 1 }, new int[] { 1, 1, 4, 0, 0, 0 },
                        new int[] { 1, 0, 0, 0, 1, 3 }, new int[] { 0, 0, 0, 0, 0, 1 } },
                new int[] { 3, 2, 2, 1, 2, 3, 0 } });
        return data;
    }

    private Vocabulary vocabulary;
    private int[][] wordIdsInDocs;
    private int[][] wordCounts;
    private int[] expectedDFCounts;

    public DocumentFrequencyDeterminerTest(int[][] wordIdsInDocs, int[][] wordCounts, int[] expectedDFCounts) {
        this.wordIdsInDocs = wordIdsInDocs;
        this.wordCounts = wordCounts;
        this.expectedDFCounts = expectedDFCounts;
        this.vocabulary = new SimpleVocabulary();
        for (int i = 0; i < expectedDFCounts.length; i++) {
            vocabulary.add(Integer.toString(i));
        }
    }

    @Test
    public void test() {
        Corpus corpus = generateCorpus(1);
        DocumentFrequencyDeterminer dfDeterminer = new DocumentFrequencyDeterminer(vocabulary);
        StreamSupport.stream(Spliterators.spliterator(corpus.iterator(), corpus.getNumberOfDocuments(),
                Spliterator.DISTINCT & Spliterator.NONNULL), false).forEach(dfDeterminer);
        AtomicIntegerArray atomicDFCounts = dfDeterminer.getCounts();
        int dfCounts[] = new int[atomicDFCounts.length()];
        for (int i = 0; i < dfCounts.length; i++) {
            dfCounts[i] = atomicDFCounts.get(i);
        }
        Assert.assertArrayEquals(expectedDFCounts, dfCounts);
    }

    @Test
    public void testParallelism() {
        final int MULTIPLIER = 1000;
        Corpus corpus = generateCorpus(MULTIPLIER);
        DocumentFrequencyDeterminer dfDeterminer = new DocumentFrequencyDeterminer(vocabulary);
        StreamSupport.stream(Spliterators.spliterator(corpus.iterator(), corpus.getNumberOfDocuments(),
                Spliterator.DISTINCT & Spliterator.NONNULL), true).forEach(dfDeterminer);
        AtomicIntegerArray atomicDFCounts = dfDeterminer.getCounts();
        int dfCounts[] = new int[atomicDFCounts.length()];
        for (int i = 0; i < dfCounts.length; i++) {
            dfCounts[i] = atomicDFCounts.get(i);
        }
        int multiExpectedDFCounts[] = new int[expectedDFCounts.length];
        for (int i = 0; i < expectedDFCounts.length; i++) {
            multiExpectedDFCounts[i] = MULTIPLIER * expectedDFCounts[i];
        }
        Assert.assertArrayEquals(multiExpectedDFCounts, dfCounts);
    }

    private Corpus generateCorpus(int multiplier) {
        int corpusSize = multiplier * wordIdsInDocs.length;
        // create corpus
        Corpus corpus = new DocumentListCorpus<>(new ArrayList<Document>());
        Document d;
        int localId;
        for (int i = 0; i < corpusSize; i++) {
            d = new Document(i);
            localId = i % wordIdsInDocs.length;
            if (wordIdsInDocs[localId] != null) {
                d.addProperty(new DocumentTextWordIds(wordIdsInDocs[localId]));
            }
            if (wordCounts[localId] != null) {
                DocumentWordCounts counts = new DocumentWordCounts();
                for (int j = 0; j < wordCounts[localId].length; j++) {
                    if (wordCounts[localId][j] > 0) {
                        counts.setWordCount(j, wordCounts[localId][j]);
                    }
                }
                d.addProperty(counts);
            }
            corpus.addDocument(d);
        }
        return corpus;
    }
}
