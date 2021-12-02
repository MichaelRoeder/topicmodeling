package org.dice_research.topicmodeling.io.gensim.stream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

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

                expectedCounts.put(1, 122);
                expectedCounts.put(2, 26);
                expectedCounts.put(3, 7);
                expectedCounts.put(4, 35);
                expectedCounts.put(5, 1);
                expectedCounts.put(6, 20);
                expectedCounts.put(7, 7);
                expectedCounts.put(8, 1);
                compareCounts(expectedCounts, wordCounts.getWordCounts());
                expectedCounts.clear();

                // Document 2
                document = supplier.getNextDocument();
                Assert.assertNotNull(document);
                wordCounts = document.getProperty(DocumentWordCounts.class);
                Assert.assertNotNull(wordCounts);
                Assert.assertEquals(i == 0 ? 6200105 : 1, document.getDocumentId());

                expectedCounts.put(38523, 1);
                expectedCounts.put(45046, 1);
                expectedCounts.put(47261, 1);
                expectedCounts.put(47873, 4);
                expectedCounts.put(49538, 1);
                expectedCounts.put(55731, 1);
                expectedCounts.put(73772, 1);
                expectedCounts.put(90857, 1);
                expectedCounts.put(91199, 2);
                expectedCounts.put(92936, 1);
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
