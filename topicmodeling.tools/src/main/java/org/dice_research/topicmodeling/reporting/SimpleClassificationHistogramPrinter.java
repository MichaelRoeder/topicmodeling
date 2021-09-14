package org.dice_research.topicmodeling.reporting;

import java.io.FileWriter;
import java.io.IOException;

import org.dice_research.topicmodeling.io.CSVFileProcessor;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectIntCursor;

public class SimpleClassificationHistogramPrinter implements CSVFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleClassificationHistogramPrinter.class);

    private static final String HISTOGRAM_FILE_NAME = "/classification_histogram.csv";

    public static void printClassificationHistogram(Corpus corpus, String folder) {
        ObjectIntOpenHashMap<String> categoryCounts = getClassificationHistogram(corpus);
        writeHistogramToFile(categoryCounts, folder);
    }

    private static ObjectIntOpenHashMap<String> getClassificationHistogram(Corpus corpus) {
        ObjectIntOpenHashMap<String> categoryCounts = new ObjectIntOpenHashMap<String>();
        DocumentCategory category;
        for (int d = 0; d < corpus.getNumberOfDocuments(); ++d) {
            category = corpus.getDocument(d).getProperty(DocumentCategory.class);
            if (category == null) {
                LOGGER.warn("Got document without DocumentCategory property.");
            } else {
                categoryCounts.putOrAdd(category.getCategory(), 1, 1);
            }
        }
        return categoryCounts;
    }

    private static void writeHistogramToFile(ObjectIntOpenHashMap<String> histogram, String folder) {
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(folder + HISTOGRAM_FILE_NAME), SEPARATOR, QUOTECHAR,
                    ESCAPECHAR);
            String line[] = new String[2];
            line[0] = "Classes";
            line[1] = Integer.toString(histogram.size());
            writer.writeNext(line);
            for (ObjectIntCursor<String> classCount : histogram) {
                line[0] = classCount.key;
                line[1] = Integer.toString(classCount.value);
                writer.writeNext(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error while opening CSV file.", e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
