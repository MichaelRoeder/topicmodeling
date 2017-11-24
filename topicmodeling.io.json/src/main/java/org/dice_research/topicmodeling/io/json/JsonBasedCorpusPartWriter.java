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
package org.dice_research.topicmodeling.io.json;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.dice_research.topicmodeling.io.json.stream.JsonWritingDocumentConsumer;
import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JsonBasedCorpusPartWriter implements DocumentConsumer, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonBasedCorpusPartWriter.class);

    public static final String PART_FILE_PREFIX = "part";
    public static final String PART_FILE_SUFFIX = ".json";

    private final File outputFolder;
    private final int documentPerPart;
    private int currentPartDocCount;
    private int currentPartId;
    private JsonWritingDocumentConsumer currentWriter;

    public JsonBasedCorpusPartWriter(File outputFolder, int documentPerPart) {
        this(outputFolder, documentPerPart, 0);
    }

    public JsonBasedCorpusPartWriter(File outputFolder, int documentPerPart, int firstPartId) {
        this.outputFolder = outputFolder;
        this.documentPerPart = documentPerPart;
        this.currentPartId = firstPartId;
    }

    @Override
    public synchronized void consumeDocument(Document document) {
        if (currentWriter == null) {
            currentWriter = JsonWritingDocumentConsumer.createJsonWritingDocumentConsumer(new File(outputFolder
                    .getAbsolutePath() + File.separator + PART_FILE_PREFIX + currentPartId + PART_FILE_SUFFIX));
        }
        if (currentWriter != null) {
            currentWriter.consumeDocument(document);
            ++currentPartDocCount;
            if (currentPartDocCount >= documentPerPart) {
                LOGGER.info("Finished part #" + currentPartId + ".");
                try {
                    currentWriter.close();
                } catch (IOException e) {
                    LOGGER.error("Error while closing JsonWritingDocumentConsumer.", e);
                }
                currentWriter = null;
                currentPartDocCount = 0;
                ++currentPartId;
            }
        } else {
            LOGGER.error("Couldn't create JsonWritingDocumentConsumer. Ignoring the current document.");
        }
    }

    @Override
    public void close() {
        if (currentWriter != null) {
            try {
                currentWriter.close();
            } catch (IOException e) {
                LOGGER.error("Error while closing XmlWritingDocumentConsumer.", e);
            }
        }
    }
}
