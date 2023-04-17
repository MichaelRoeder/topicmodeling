package org.dice_research.topicmodeling.tools;

import java.io.File;
import java.io.IOException;

import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.io.gzip.GZipCorpusReaderDecorator;
import org.dice_research.topicmodeling.io.java.CorpusObjectReader;
import org.dice_research.topicmodeling.io.xml.XmlWritingDocumentConsumer;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.properties.CorpusVocabulary;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;

public class CorpusFeaturePrinter {

    public static void main(String[] args) throws IOException {
        CorpusReader reader = new GZipCorpusReaderDecorator(new CorpusObjectReader());
        reader.readCorpus(new File(args[0]));

        Corpus corpus = reader.getCorpus();
        reader = null;

        System.out.print("#documents:  ");
        System.out.println(corpus.getNumberOfDocuments());

        System.out.print("#word types: ");
        CorpusVocabulary cv = corpus.getProperty(CorpusVocabulary.class);
        if (cv != null) {
            System.out.println(cv.get().size());
        } else {
            System.out.println("No vocabulary available");
        }

        long count = 0;
        boolean error = false;
        Document d = null;
        for (int i = 0; i < corpus.getNumberOfDocuments(); ++i) {
            d = corpus.getDocument(i);
            DocumentWordCounts dwc = d.getProperty(DocumentWordCounts.class);
            if (dwc != null) {
                count += dwc.getSumOfWordCounts();
            } else {
                TermTokenizedText ttt = d.getProperty(TermTokenizedText.class);
                if (ttt != null) {
                    count += ttt.getTermTokenizedText().size();
                } else {
                    DocumentTextWordIds dtw = d.getProperty(DocumentTextWordIds.class);
                    if (dtw != null) {
                        count += dtw.getWordIds().length;
                    } else {
                        error = true;
                    }
                }
            }
        }
        System.out.print("#word token: ");
        System.out.print(count);
        if (error) {
            System.out.println("(Couldn't get counts of all documents)");
        } else {
            System.out.println();
        }

    }
}
