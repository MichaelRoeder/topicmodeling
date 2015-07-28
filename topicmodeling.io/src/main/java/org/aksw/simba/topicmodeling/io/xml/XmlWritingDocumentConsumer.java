package org.aksw.simba.topicmodeling.io.xml;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.aksw.simba.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlWritingDocumentConsumer extends AbstractDocumentXmlWriter implements DocumentConsumer, Closeable {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlWritingDocumentConsumer.class);

    protected FileWriter fout;

    public static XmlWritingDocumentConsumer createXmlWritingDocumentConsumer(File file) {
        FileWriter fout = null;
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            fout = new FileWriter(file);
            XmlWritingDocumentConsumer consumer = new XmlWritingDocumentConsumer(fout);
            consumer.writeHead();
            return consumer;
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file. Returning null.", e);
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException ioe) {
                }
            }
        }
        return null;
    }

    private XmlWritingDocumentConsumer(FileWriter fout) {
        this.fout = fout;
    }

    protected void writeHead() throws IOException {
        fout.write(CorpusXmlTagHelper.XML_FILE_HEAD);
        fout.write("<");
        fout.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
        fout.write(" ");
        fout.write(CorpusXmlTagHelper.NAMESPACE_DECLARATION);
        fout.write(">\n");
    }

    @Override
    public void consumeDocument(Document document) {
        try {
            writeDocument(fout, document);
            fout.flush();
        } catch (IOException e) {
            LOGGER.error("Error while trying to write document #" + document.getDocumentId() + ".", e);
        }
    }

    public void close() throws IOException {
        if (fout != null) {
            fout.write("</");
            fout.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
            fout.write(">");
            fout.close();
            fout = null;
        }
    }
}
