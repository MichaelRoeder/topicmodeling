package org.dice_research.topicmodeling.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import org.dice_research.topicmodeling.io.es.Document2JsonFunction;
import org.dice_research.topicmodeling.io.es.StreamingDocumentIndexer;
import org.dice_research.topicmodeling.io.xml.stream.StreamBasedXmlDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaArticleId;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchExporter.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            LOGGER.error("Wrong usage. Correct usage: <path-to-XML-corpus-file> <index-name> <host> <port>.");
            return;
        }

        File corpusFile = new File(args[0]);
        String indexName = args[1];
        String host = args[2];
        int port = Integer.parseInt(args[3]);

        Document2JsonFunction transformer = new Document2JsonFunction();
        transformer.registerStringContainingProperty("text", DocumentText.class);
        transformer.registerKeyValuePair("url", ElasticsearchExporter::createWikipediaUrl);

        try (StreamingDocumentIndexer indexer = StreamingDocumentIndexer.create(host, port, indexName, transformer);) {

            StreamBasedXmlDocumentSupplier.registerParseableDocumentProperty(WikipediaArticleId.class);
            StreamBasedXmlDocumentSupplier.registerParseableDocumentProperty(WikipediaNamespace.class);
            try (Reader reader = new InputStreamReader(
                    new GZIPInputStream(new BufferedInputStream(new FileInputStream(corpusFile))),
                    StandardCharsets.UTF_8)) {
                StreamBasedXmlDocumentSupplier supplier = new StreamBasedXmlDocumentSupplier(reader, true);
                Document document = supplier.getNextDocument();
                int count = 0;
                while (document != null) {
                    indexer.accept(document);
                    ++count;
                    if ((count % 1000) == 0) {
                        LOGGER.info("Saw {}th document.", count);
                    }
                    document = supplier.getNextDocument();
                }
            }
        }
    }

    public static String createWikipediaUrl(Document document) {
        DocumentName name = document.getProperty(DocumentName.class);
        if (name != null) {
            return "https://en.wikipedia.org/wiki/" + name.get().replace(' ', '_');
        } else {
            return null;
        }
    }
}
