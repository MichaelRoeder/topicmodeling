package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.SlidingWindowCooccurrenceCounter;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.aksw.simba.topicmodeling.utils.doc.DocumentTextWordIds;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCooccurrenceCount;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



@RunWith(Parameterized.class)
public class SlidingWindowCooccurrenceCounterTest extends AbstractDocumentSupplierDecoratorTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> startConfigs = new ArrayList<Object[]>();
        startConfigs
                .add(new Object[] {
                        new DocumentProperty[] {
                                new DocumentTextWordIds(new int[] { 0, 1, 2, 3, 4 }) },
                        new Integer(3),
                        new int[][] { { 0, 1, 1 }, { 0, 2, 1 }, { 1, 2, 1 }, { 1, 3, 1 }, { 2, 3, 1 }, { 2, 4, 1 },
                        { 3, 4, 1 } } });
        startConfigs
                .add(new Object[] {
                        new DocumentProperty[] {
                                new DocumentTextWordIds(new int[] { 0, 1, 2, 3, 4 }) },
                        new Integer(1),
                        new int[][] {} });
        startConfigs
                .add(new Object[] {
                        new DocumentProperty[] {
                                new DocumentTextWordIds(new int[] { 0, 1, 0, 0, 2 }) },
                        new Integer(3),
                        new int[][] { { 0, 0, 2 }, { 0, 1, 3 }, { 0, 2, 2 } } });
        return startConfigs;
    }

    private Integer windowSize;
    private int expectedCounts[][];

    public SlidingWindowCooccurrenceCounterTest(DocumentProperty properties[], Integer windowSize,
            int expectedCounts[][]) {
        super(properties);
        this.windowSize = windowSize;
        this.expectedCounts = expectedCounts;
    }

    @Test
    public void test() {
        SlidingWindowCooccurrenceCounter counter = new SlidingWindowCooccurrenceCounter(this, windowSize);
        Document document = counter.getNextDocument();
        Assert.assertNotNull(document);
        DocumentWordCooccurrenceCount counts = document.getProperty(DocumentWordCooccurrenceCount.class);
        Assert.assertNotNull(counts);
        try {
            for (int[] wordCoocCount : expectedCounts) {
                Assert.assertEquals(wordCoocCount[2], counts.getCooccurrenceCount(wordCoocCount[0], wordCoocCount[1]));
            }
            Assert.assertEquals(expectedCounts.length, counts.size());
        } catch (AssertionError e) {
            System.out.println("Error. Counts: " + counts.toString());
            throw e;
        }
    }
}
