package org.dice_research.topicmodeling.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.dice_research.topicmodeling.io.xml.XmlWritingDocumentConsumer;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.DocumentFilteringSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.WikipediaDumpReader;
import org.dice_research.topicmodeling.wikipedia.WikipediaMarkupDeletingDocumentSupplierDecorator;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaArticleId;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaNamespace;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaRedirect;
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
        File outDir = outFile.getAbsoluteFile().getParentFile();
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

            // Filter documents that...
            // ... are redirects to other articles
            supplier = new DocumentFilteringSupplierDecorator(supplier,
                    d -> d.getProperty(WikipediaRedirect.class) == null);
            // ... have the namespace 0
            supplier = new DocumentFilteringSupplierDecorator(supplier,
                    d -> d.getProperty(WikipediaNamespace.class) != null
                            && d.getProperty(WikipediaNamespace.class).getNamespaceId() == 0);

            // Remove Wikipedia markup
            supplier = new WikipediaMarkupDeletingDocumentSupplierDecorator(supplier);

            // Filter documents that...
            // ... have no text
            supplier = new DocumentFilteringSupplierDecorator(supplier,
                    d -> d.getProperty(DocumentText.class) != null && !d.getProperty(DocumentText.class).isEmpty());
            // ... have no name
            supplier = new DocumentFilteringSupplierDecorator(supplier,
                    d -> d.getProperty(DocumentName.class) != null && !d.getProperty(DocumentName.class).isEmpty());

            XmlWritingDocumentConsumer.registerParseableDocumentProperty(WikipediaArticleId.class);
            XmlWritingDocumentConsumer.registerParseableDocumentProperty(WikipediaNamespace.class);

            try (XmlWritingDocumentConsumer consumer = XmlWritingDocumentConsumer
                    .createXmlWritingDocumentConsumer(outFile)) {
                LOGGER.info("Starting pipeline...");
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
        LOGGER.info("Finished.");
    }
}
