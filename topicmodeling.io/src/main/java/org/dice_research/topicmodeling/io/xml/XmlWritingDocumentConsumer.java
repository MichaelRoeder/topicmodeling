/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io.xml;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlWritingDocumentConsumer extends AbstractDocumentXmlWriter implements DocumentConsumer, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlWritingDocumentConsumer.class);

    protected Writer fout;

    public static XmlWritingDocumentConsumer createXmlWritingDocumentConsumer(File file) {
        Writer writer = null;
        // We need an absolute path, otherwise we might not be able to ask for the
        // parent file
        if (!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
                    StandardCharsets.UTF_8);
            XmlWritingDocumentConsumer consumer = new XmlWritingDocumentConsumer(writer);
            consumer.writeHead();
            return consumer;
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file. Returning null.", e);
            IOUtils.closeQuietly(writer);
        }
        return null;
    }

    private XmlWritingDocumentConsumer(Writer fout) {
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
