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
package org.dice_research.topicmodeling.preprocessing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.shedule.DeterministicPercentageDocumentSheduler;
import org.dice_research.topicmodeling.preprocessing.shedule.ListBasedDocumentSheduler;
import org.dice_research.topicmodeling.preprocessing.shedule.NFoldCrossValidationSheduler;
import org.dice_research.topicmodeling.preprocessing.shedule.RandomDocumentSheduler;
import org.dice_research.topicmodeling.preprocessing.shedule.AbstractDocumentSheduler.PartialDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.AbstractDocumentProperty;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.junit.Assert;
import org.junit.Test;

import com.carrotsearch.hppc.IntOpenHashSet;

public class DocumentShedulerTest implements DocumentSupplier {

    private int documentCount;
    private int documentsAvailable;

    @Test
    public void TestRandomDocumentSheduler() {
        documentCount = 0;
        documentsAvailable = 10000;

        int parts = 5;
        double portions[] = { 0.1, 0.3, 0.2, 0.2 };
        assert(portions.length == (parts - 1));

        RandomDocumentSheduler sheduler = new RandomDocumentSheduler(this, parts);
        PartialDocumentSupplier docSuppliers[] = new PartialDocumentSupplier[parts];
        for (int i = 0; i < portions.length; ++i) {
            sheduler.setPortionOfPart(i, portions[i]);
            docSuppliers[i] = sheduler.getPartialDocumentSupplier(i);
        }
        docSuppliers[parts - 1] = sheduler.getPartialDocumentSupplier(parts - 1);

        Document tempDoc;
        int tempDocCount;
        int sum = 0;
        for (int i = 0; i < docSuppliers.length; ++i) {
            tempDocCount = -1;
            do {
                ++tempDocCount;
                tempDoc = docSuppliers[i].getNextDocument();
            } while (tempDoc != null);
            sum += tempDocCount;
        }
        Assert.assertEquals(documentsAvailable, sum);
    }

    @Test
    public void TestDeterministicPercentageDocumentShedulerExceptions() {
        int parts = 3;
        DeterministicPercentageDocumentSheduler sheduler = new DeterministicPercentageDocumentSheduler(this, parts);
        boolean gotIllegalArgumentException = false;
        try {
            sheduler.setPercentageOfPart(0, 110);
        } catch (IllegalArgumentException e) {
            gotIllegalArgumentException = true;
        }
        Assert.assertTrue(gotIllegalArgumentException);
        gotIllegalArgumentException = false;
        try {
            sheduler.setPercentageOfPart(0, -2);
        } catch (IllegalArgumentException e) {
            gotIllegalArgumentException = true;
        }
        Assert.assertTrue(gotIllegalArgumentException);

        System.out.println("An Error message should follow");
        sheduler.setPercentageOfPart(0, 60);
        sheduler.setPercentageOfPart(1, 60);
        // the sheduler should print out an error message!
    }

    @Test
    public void TestDeterministicPercentageDocumentSheduler() {
        documentCount = 0;
        documentsAvailable = 10000;

        int parts = 5;
        int percentages[] = { 9, 16, 20, 25 };
        double percentagesSum = 0;

        DeterministicPercentageDocumentSheduler sheduler = new DeterministicPercentageDocumentSheduler(this, parts);
        PartialDocumentSupplier docSuppliers[] = new PartialDocumentSupplier[parts];
        for (int i = 0; i < percentages.length; ++i) {
            sheduler.setPercentageOfPart(i, percentages[i]);
            docSuppliers[i] = sheduler.getPartialDocumentSupplier(i);
            percentagesSum += percentages[i];
        }
        docSuppliers[parts - 1] = sheduler.getPartialDocumentSupplier(parts - 1);

        Document tempDoc;
        int tempDocCount;
        int sum = 0;
        for (int i = 0; i < docSuppliers.length; ++i) {
            tempDocCount = -1;
            do {
                ++tempDocCount;
                tempDoc = docSuppliers[i].getNextDocument();
            } while (tempDoc != null);
            sum += tempDocCount;
            if (i < (parts - 1)) {
                Assert.assertEquals(percentages[i] * documentsAvailable / 100.0, (double) tempDocCount, 1.0);
            } else {
                Assert.assertEquals((100 - percentagesSum) * documentsAvailable / 100.0, (double) tempDocCount, 1.0);
            }
        }
        Assert.assertEquals(documentsAvailable, sum);
    }

    @Test
    public void testNFoldCrossValidationSheduler() {
        documentsAvailable = 10000;
        int folds = 10;

        DocumentSupplier docSuppliers[] = new DocumentSupplier[2];
        Document tempDoc;
        int tempDocCount, sum, traceId;
        IntOpenHashSet testDocumentIds = new IntOpenHashSet(documentsAvailable);
        for (int r = 0; r < folds; ++r) {
            documentCount = 0;
            NFoldCrossValidationSheduler sheduler = new NFoldCrossValidationSheduler(this, folds, r);
            docSuppliers[NFoldCrossValidationSheduler.TRAIN_DOCUMENTS_SUPPLIER_ID] = sheduler
                    .getPartialDocumentSupplier(NFoldCrossValidationSheduler.TRAIN_DOCUMENTS_SUPPLIER_ID);
            docSuppliers[NFoldCrossValidationSheduler.TEST_DOCUMENTS_SUPPLIER_ID] = sheduler
                    .getPartialDocumentSupplier(NFoldCrossValidationSheduler.TEST_DOCUMENTS_SUPPLIER_ID);

            tempDocCount = -1;
            do {
                ++tempDocCount;
                tempDoc = docSuppliers[NFoldCrossValidationSheduler.TRAIN_DOCUMENTS_SUPPLIER_ID].getNextDocument();
            } while (tempDoc != null);
            sum = tempDocCount;
            // System.out.println("TrainDocSupplier \t" + tempDocCount);
            Assert.assertEquals(((folds - 1.0) / (double) folds) * documentsAvailable, (double) tempDocCount, 1.0);

            tempDocCount = -1;
            do {
                ++tempDocCount;
                tempDoc = docSuppliers[NFoldCrossValidationSheduler.TEST_DOCUMENTS_SUPPLIER_ID].getNextDocument();
                if (tempDoc != null) {
                    traceId = tempDoc.getProperty(DocumentTracer.class).getTraceId();
                    Assert.assertFalse("Got a test document which has already been used as test document before. (id = "
                            + tempDoc.getDocumentId() + ")", testDocumentIds.contains(traceId));
                    testDocumentIds.add(traceId);
                }
            } while (tempDoc != null);
            sum += tempDocCount;
            // System.out.println("TestDocSupplier \t" + tempDocCount);
            Assert.assertEquals((1.0 / (double) folds) * documentsAvailable, (double) tempDocCount, 1.0);
            // System.out.println("sum \t" + sum + " of " + documentsAvailable);
            Assert.assertEquals(documentsAvailable, sum);
        }
        // System.out.println("Documents used for testing\t" +
        // testDocumentIds.size() + " of " + documentsAvailable);
        Assert.assertEquals(documentsAvailable, testDocumentIds.size());
    }

    @Test
    public void TestListBasedDocumentSheduler() {
        documentCount = 0;
        documentsAvailable = 10000;

        String values[][] = new String[][] { { "2", "7", "18", "99" }, { "105", "100", "22", "1", "9" } };
        @SuppressWarnings("unchecked")
        ListBasedDocumentSheduler sheduler = new ListBasedDocumentSheduler((DocumentSupplier) this,
                DocumentTracer.class, (Set<String>[]) new Set[] { new HashSet<String>(Arrays.asList(values[0])),
                        new HashSet<String>(Arrays.asList(values[1])) });
        PartialDocumentSupplier docSuppliers[] = new PartialDocumentSupplier[values.length + 1];
        for (int i = 0; i < docSuppliers.length; ++i) {
            docSuppliers[i] = sheduler.getPartialDocumentSupplier(i);
        }

        int sum = 0;
        int expectedDocumentCounts[] = new int[values.length + 1];
        for (int i = 0; i < values.length; ++i) {
            expectedDocumentCounts[i + 1] = values[i].length;
            sum += values[i].length;
        }
        expectedDocumentCounts[0] = documentsAvailable - sum;

        Document tempDoc;
        sum = -1;
        do {
            ++sum;
            tempDoc = docSuppliers[0].getNextDocument();
        } while (tempDoc != null);
        Assert.assertEquals(expectedDocumentCounts[0], sum);

        int tempDocCount;
        for (int i = 1; i < docSuppliers.length; ++i) {
            tempDocCount = -1;
            do {
                ++tempDocCount;
                tempDoc = docSuppliers[i].getNextDocument();
                if (tempDoc != null) {
                    Assert.assertNotNull(tempDoc.getProperty(DocumentTracer.class));
                    Assert.assertTrue(ArrayUtils.indexOf(values[i - 1],
                            tempDoc.getProperty(DocumentTracer.class).getStringValue()) != ArrayUtils.INDEX_NOT_FOUND);
                }
            } while (tempDoc != null);
            Assert.assertEquals(expectedDocumentCounts[i], tempDocCount);
            sum += tempDocCount;
        }
        Assert.assertEquals(documentsAvailable, sum);
    }

    @Override
    public Document getNextDocument() {
        if (documentCount < documentsAvailable) {
            Document document = new Document(documentCount);
            document.addProperty(new DocumentTracer(documentCount));
            ++documentCount;
            return document;
        } else {
            return null;
        }
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        /* nothing to do */
    }

    private static class DocumentTracer extends AbstractDocumentProperty implements StringContainingDocumentProperty {

        private static final long serialVersionUID = 5062085480180086424L;

        private int traceId;

        public DocumentTracer(int traceId) {
            this.traceId = traceId;
        }

        @Override
        public Object getValue() {
            return traceId;
        }

        public int getTraceId() {
            return traceId;
        }

        @Override
        public String getStringValue() {
            return Integer.toString(getTraceId());
        }
    }
}
