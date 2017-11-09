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

import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlBasedCorpusPartWriter extends AbstractDocumentXmlWriter implements DocumentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlBasedCorpusPartWriter.class);

    public static final String PART_FILE_PREFIX = "part";
    public static final String PART_FILE_SUFFIX = ".xml";

    private final File outputFolder;
    private final int documentPerPart;
    private int currentPartDocCount;
    private int currentPartId = 0;
    private XmlWritingDocumentConsumer currentXmlWriter;

    public XmlBasedCorpusPartWriter(File outputFolder, int documentPerPart) {
        this.outputFolder = outputFolder;
        this.documentPerPart = documentPerPart;
    }

    @Override
    public void consumeDocument(Document document) {
        if (currentXmlWriter == null) {
            currentXmlWriter = XmlWritingDocumentConsumer.createXmlWritingDocumentConsumer(new File(outputFolder
                    .getAbsolutePath() + File.separator + PART_FILE_PREFIX + currentPartId + PART_FILE_SUFFIX));
        }
        if (currentXmlWriter != null) {
            currentXmlWriter.consumeDocument(document);
            ++currentPartDocCount;
            if (currentPartDocCount >= documentPerPart) {
                LOGGER.info("Finished part #" + currentPartId + ".");
                try {
                    currentXmlWriter.close();
                } catch (IOException e) {
                    LOGGER.error("Error while closing XmlWritingDocumentConsumer.", e);
                }
                currentXmlWriter = null;
                currentPartDocCount = 0;
                ++currentPartId;
            }
        } else {
            LOGGER.error("Couldn't create XmlWritingDocumentConsumer. Ignoring the current document.");
        }
    }

    public void close() {
        if (currentXmlWriter != null) {
            try {
                currentXmlWriter.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing XmlWritingDocumentConsumer.", e);
            }
        }
    }
}
