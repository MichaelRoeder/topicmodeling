package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.vocabulary.SimpleVocabulary;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.BitSet;

@RunWith(Parameterized.class)
public class VocabularyReductionMappingApplyingSupplierDecoratorTest {

    public static final int R = VocabularyReductionMappingApplyingSupplierDecorator.REMOVED_WORD;

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<Object[]>();
        // nothing changes
        data.add(new Object[] { new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 1, 2, 3, 4, 5 },
                new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 1, 1, 1, 1, 1, 1 },
                new int[] { 1, 1, 1, 1, 1, 1 } });
        // all words are deleted
        data.add(new Object[] { new int[] { 0, 1, 2, 3, 4, 5 }, new int[] {}, new int[] { R, R, R, R, R, R },
                new int[] {}, new int[] { 1, 1, 1, 1, 1, 1 }, new int[] {} });
        // nothing changes
        data.add(new Object[] { new int[] { 0, 1, 2, 3, 4, 5 }, new int[] { 1, 3, 5 }, new int[] { R, 1, R, 2, R, 0 },
                new int[] { 1, 2, 0 }, new int[] { 1, 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1 } });
        return data;
    }

    private Vocabulary vocabulary;
    private BitSet keptWords;
    private int[] expectedMapping;
    private int[] wordIdsInDoc;
    private int[] expectedWordIdsInDoc;
    private int[] wordCounts;
    private int[] expectedWordCounts;

    public VocabularyReductionMappingApplyingSupplierDecoratorTest(int[] wordIdsInDoc, int[] keptWords,
            int[] expectedMapping, int[] expectedWordIdsInDoc, int[] wordCounts, int[] expectedWordCounts) {
        this.wordIdsInDoc = wordIdsInDoc;
        this.expectedWordIdsInDoc = expectedWordIdsInDoc;
        this.wordCounts = wordCounts;
        this.expectedWordCounts = expectedWordCounts;
        this.vocabulary = new SimpleVocabulary();
        for (int i = 0; i < wordIdsInDoc.length; i++) {
            if (vocabulary.getWord(wordIdsInDoc[i]) == null) {
                vocabulary.add(Integer.toString(wordIdsInDoc[i]));
            }
        }
        this.keptWords = new BitSet(vocabulary.size());
        for (int i = 0; i < keptWords.length; i++) {
            this.keptWords.set(keptWords[i]);
        }
        this.expectedMapping = expectedMapping;
    }

    @Test
    public void testMappingCreation() {
        int[] mapping = VocabularyReductionMappingApplyingSupplierDecorator.createMapping(vocabulary, keptWords);
        Assert.assertArrayEquals(expectedMapping, mapping);
    }

    @Test
    public void testWordIdsInTextMapping() {
        VocabularyReductionMappingApplyingSupplierDecorator decorator = new VocabularyReductionMappingApplyingSupplierDecorator(
                null, expectedMapping);
        Document resultDoc = decorator
                .apply(new Document(0, new DocumentProperty[] { new DocumentTextWordIds(wordIdsInDoc) }));
        Assert.assertNotNull(resultDoc);
        Assert.assertNotNull(resultDoc.getProperty(DocumentTextWordIds.class));
        Assert.assertArrayEquals(expectedWordIdsInDoc, resultDoc.getProperty(DocumentTextWordIds.class).getWordIds());
    }

    @Test
    public void testWordCountMapping() {
        VocabularyReductionMappingApplyingSupplierDecorator decorator = new VocabularyReductionMappingApplyingSupplierDecorator(
                null, expectedMapping);
        DocumentWordCounts docWordCounts = new DocumentWordCounts();
        for (int i = 0; i < wordCounts.length; i++) {
            docWordCounts.setWordCount(i, wordCounts[i]);
        }
        Document resultDoc = decorator.apply(new Document(0, new DocumentProperty[] { docWordCounts }));
        Assert.assertNotNull(resultDoc);
        DocumentWordCounts result = resultDoc.getProperty(DocumentWordCounts.class);
        Assert.assertNotNull(result);
        for (int i = 0; i < expectedWordCounts.length; i++) {
            Assert.assertEquals(expectedWordCounts[i], result.getCountForWord(i));
        }
        Assert.assertEquals(expectedWordCounts.length, result.getNumberOfWords());
    }
}
