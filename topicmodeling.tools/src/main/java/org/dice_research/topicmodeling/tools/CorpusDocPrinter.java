package org.dice_research.topicmodeling.tools;

import java.io.File;
import java.io.IOException;

import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.io.gzip.GZipCorpusReaderDecorator;
import org.dice_research.topicmodeling.io.java.CorpusObjectReader;
import org.dice_research.topicmodeling.io.xml.XmlWritingDocumentConsumer;
import org.dice_research.topicmodeling.utils.corpus.Corpus;

public class CorpusDocPrinter {

    public static void main(String[] args) throws IOException {
        CorpusReader reader = new GZipCorpusReaderDecorator(new CorpusObjectReader());
        reader.readCorpus(new File(args[0]));

        XmlWritingDocumentConsumer consumer = XmlWritingDocumentConsumer
                .createXmlWritingDocumentConsumer(new File(args[1]));

        Corpus corpus = reader.getCorpus();
        reader = null;

        for (int i = 0; i < corpus.getNumberOfDocuments(); i += 100000) {
            consumer.accept(corpus.getDocument(i));
        }
        consumer.accept(corpus.getDocument(corpus.getNumberOfDocuments() - 1));

        consumer.close();
    }
}
