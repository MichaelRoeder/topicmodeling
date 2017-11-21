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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusXmlWriter extends AbstractDocumentXmlWriter implements CorpusWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusXmlWriter.class);

    protected File file;

    public CorpusXmlWriter() {
    }

    @Deprecated
    public CorpusXmlWriter(File file) {
        this.file = file;
    }

    public void writeCorpus(Corpus corpus) {
        try {
            writeCorpus(corpus, file);
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file", e);
        }
    }

    @Override
    public void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out, Charsets.UTF_8);
        writer.write(CorpusXmlTagHelper.XML_FILE_HEAD);
        writer.write("<");
        writer.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
        writer.write(" ");
        writer.write(CorpusXmlTagHelper.NAMESPACE_DECLARATION);
        writer.write(">\n");
        for (Document d : corpus) {
            writeDocument(writer, d);
        }
        writer.write("</");
        writer.write(CorpusXmlTagHelper.CORPUS_TAG_NAME);
        writer.write(">");

    }
}
