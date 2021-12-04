package org.dice_research.topicmodeling.io.gensim.stream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.IntUnaryOperator;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.junit.Assert;
import org.junit.Test;

import com.carrotsearch.hppc.IntIntOpenHashMap;

public class StreamBasedMMDocumentSupplierTest {

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 2; ++i) {
            try (Reader reader = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream("example_corpus.mm"),
                    StandardCharsets.UTF_8);) {
                StreamBasedMMDocumentSupplier supplier = StreamBasedMMDocumentSupplier.createReader(reader, 0, 1, 2,
                        i == 0);
                supplier.setHeaderLines(2);

                Document document;
                DocumentWordCounts wordCounts;
                IntIntOpenHashMap expectedCounts = new IntIntOpenHashMap();

                // Document 1
                document = supplier.getNextDocument();
                Assert.assertNotNull(document);
                wordCounts = document.getProperty(DocumentWordCounts.class);
                Assert.assertNotNull(wordCounts);
                Assert.assertEquals(i == 0 ? 1 : 0, document.getDocumentId());

                expectedCounts.put(0, 122);
                expectedCounts.put(1, 26);
                expectedCounts.put(2, 7);
                expectedCounts.put(3, 35);
                expectedCounts.put(4, 1);
                expectedCounts.put(5, 20);
                expectedCounts.put(6, 7);
                expectedCounts.put(7, 1);
                compareCounts(expectedCounts, wordCounts.getWordCounts());
                expectedCounts.clear();

                // Document 2
                document = supplier.getNextDocument();
                Assert.assertNotNull(document);
                wordCounts = document.getProperty(DocumentWordCounts.class);
                Assert.assertNotNull(wordCounts);
                Assert.assertEquals(i == 0 ? 6200105 : 1, document.getDocumentId());

                expectedCounts.put(38522, 1);
                expectedCounts.put(45045, 1);
                expectedCounts.put(47260, 1);
                expectedCounts.put(47872, 4);
                expectedCounts.put(49537, 1);
                expectedCounts.put(55730, 1);
                expectedCounts.put(73771, 1);
                expectedCounts.put(90856, 1);
                expectedCounts.put(91198, 2);
                expectedCounts.put(92935, 1);
                compareCounts(expectedCounts, wordCounts.getWordCounts());

                // We shouldn't get a new document
                document = supplier.getNextDocument();
                Assert.assertNull(document);

            }
        }
    }

    @Test
    public void test_transformation() throws IOException {
        for (int i = 0; i < 2; ++i) {
            try (Reader reader = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream("example_corpus.mm"),
                    StandardCharsets.UTF_8);) {
                StreamBasedMMDocumentSupplier supplier = StreamBasedMMDocumentSupplier.createReader(reader, 0, 1, 2,
                        i == 0);
                // Add some transformations
                // Increase the document ID by 2
                supplier.setDocIdTransformation(d -> d + 2);
                // Do not touch the word IDs (by default, they are reduced by 1. So the previous
                // test already covered that)
                supplier.setWordIdTransformation(IntUnaryOperator.identity());
                // Increase the word count by 10
                supplier.setWordCountTransformation(c -> c * 10);
                supplier.setHeaderLines(2);

                Document document;
                DocumentWordCounts wordCounts;
                IntIntOpenHashMap expectedCounts = new IntIntOpenHashMap();

                // Document 1
                document = supplier.getNextDocument();
                Assert.assertNotNull(document);
                wordCounts = document.getProperty(DocumentWordCounts.class);
                Assert.assertNotNull(wordCounts);
                Assert.assertEquals(i == 0 ? 3 : 0, document.getDocumentId());

                expectedCounts.put(1, 1220);
                expectedCounts.put(2, 260);
                expectedCounts.put(3, 70);
                expectedCounts.put(4, 350);
                expectedCounts.put(5, 10);
                expectedCounts.put(6, 200);
                expectedCounts.put(7, 70);
                expectedCounts.put(8, 10);
                compareCounts(expectedCounts, wordCounts.getWordCounts());
                expectedCounts.clear();

                // Document 2
                document = supplier.getNextDocument();
                Assert.assertNotNull(document);
                wordCounts = document.getProperty(DocumentWordCounts.class);
                Assert.assertNotNull(wordCounts);
                Assert.assertEquals(i == 0 ? 6200107 : 1, document.getDocumentId());

                expectedCounts.put(38523, 10);
                expectedCounts.put(45046, 10);
                expectedCounts.put(47261, 10);
                expectedCounts.put(47873, 40);
                expectedCounts.put(49538, 10);
                expectedCounts.put(55731, 10);
                expectedCounts.put(73772, 10);
                expectedCounts.put(90857, 10);
                expectedCounts.put(91199, 20);
                expectedCounts.put(92936, 10);
                compareCounts(expectedCounts, wordCounts.getWordCounts());

                // We shouldn't get a new document
                document = supplier.getNextDocument();
                Assert.assertNull(document);

            }
        }
    }

    private void compareCounts(IntIntOpenHashMap expectedCounts, IntIntOpenHashMap wordCounts) {
        Assert.assertEquals(expectedCounts.size(), wordCounts.size());
        for (int i = 0; i < expectedCounts.allocated.length; ++i) {
            if (expectedCounts.allocated[i]) {
                Assert.assertEquals(expectedCounts.values[i], wordCounts.get(expectedCounts.keys[i]));
            }
        }
    }
}
