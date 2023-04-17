package org.dice_research.topicmodeling.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.dice_research.topicmodeling.io.xml.XmlWritingDocumentConsumer;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.wikipedia.WikipediaDumpReader;
import org.dice_research.topicmodeling.wikipedia.WikipediaMarkupDeletingDocumentSupplierDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikipediaPreprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaPreprocessor.class);

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            LOGGER.error("Wrong usage. Correct usage: '<path-to-wikipedia-dump> <output-file>'.");
            return;
        }
        File dumpFile = new File(args[0]);
        File outFile = new File(args[1]);
        File outDir = outFile.getParentFile();
        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                LOGGER.error("Error while creating output dir {}. Aborting.", outDir);
                return;
            }
        }

        LOGGER.info("Creating pipeline...");
        DocumentSupplier supplier;
        // Create Wikipedia dump reader
        try (InputStream input = new BZip2CompressorInputStream(new FileInputStream(dumpFile), true);) {
            supplier = WikipediaDumpReader.createReader(input, StandardCharsets.UTF_8);

            // Remove Wikipedia markup
            supplier = new WikipediaMarkupDeletingDocumentSupplierDecorator(supplier);

            // Filter documents that we do not want
            // TODO Add title based filter!!!

            try (XmlWritingDocumentConsumer consumer = XmlWritingDocumentConsumer
                    .createXmlWritingDocumentConsumer(outFile)) {
                Document document = supplier.getNextDocument();
                while (document != null) {
                    consumer.accept(document);
                    document = supplier.getNextDocument();
                }
            }
        } catch (IOException e) {
            LOGGER.error("IO Error. Aborting.", e);
            return;
        }
    }
}
