package org.aksw.simba.topicmodeling.io.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.aksw.simba.topicmodeling.io.CorpusWriter;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CorpusXmlWriter extends AbstractDocumentXmlWriter implements CorpusWriter {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CorpusXmlWriter.class);

    protected File file;

    public CorpusXmlWriter(File file) {
        this.file = file;
    }

    public void writeCorpus(Corpus corpus) {
        FileWriter fout = null;
        try {
            fout = new FileWriter(file);
            fout.write(CorpusXmlTagHelper.XML_FILE_HEAD);
            fout.write("<");
            fout.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
            fout.write(" ");
            fout.write(CorpusXmlTagHelper.NAMESPACE_DECLARATION);
            fout.write(">\n");
            for (Document d : corpus) {
                writeDocument(fout, d);
            }
            fout.write("</");
            fout.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
            fout.write(">");
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file", e);
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
