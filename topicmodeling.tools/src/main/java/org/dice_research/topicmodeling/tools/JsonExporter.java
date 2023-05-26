package org.dice_research.topicmodeling.tools;

import java.io.File;

import org.dice_research.topicmodeling.io.es.CorpusExporter4Elasticsearch;
import org.dice_research.topicmodeling.io.xml.stream.StreamBasedXmlDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaArticleId;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaNamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonExporter.class);

    public static void main(String[] args) {
        if (args.length < 2) {
            LOGGER.error("Wrong usage. Correct usage: '<path-to-XML-corpus-file> <output-directory>'.");
            return;
        }

        File corpusFile = new File(args[0]);
        File outDir = new File(args[1]);
        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                LOGGER.error("Error while creating output dir {}. Aborting.", outDir);
                return;
            }
        }
        CorpusExporter4Elasticsearch exporter = new CorpusExporter4Elasticsearch(outDir);
        exporter.registerStringContainingProperty("text", DocumentText.class);
        exporter.registerKeyValuePair("url", JsonExporter::createWikipediaUrl);

        StreamBasedXmlDocumentSupplier.registerParseableDocumentProperty(WikipediaArticleId.class);
        StreamBasedXmlDocumentSupplier.registerParseableDocumentProperty(WikipediaNamespace.class);
        StreamBasedXmlDocumentSupplier supplier = StreamBasedXmlDocumentSupplier.createReader(corpusFile);
        Document document = supplier.getNextDocument();
        while (document != null) {
            exporter.accept(document);
            document = supplier.getNextDocument();
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
