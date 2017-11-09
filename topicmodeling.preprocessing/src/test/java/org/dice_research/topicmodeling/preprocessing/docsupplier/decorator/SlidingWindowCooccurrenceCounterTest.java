/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.SlidingWindowCooccurrenceCounter;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCooccurrenceCount;
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
