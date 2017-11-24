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

import java.io.File;

import org.dice_research.topicmodeling.io.json.stream.StreamBasedJsonDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPartsBasedDocumentSupplier extends AbstractDocumentSupplier implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonPartsBasedDocumentSupplier.class);

    private static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;

    private String filePrefix;
    private String fileSuffix;
    private boolean useDocumentIdsFromFile;
    private final File inputFolder;
    private int currentPartId;
    /**
     * Part Id until which it will be read (excluding).
     */
    private int endPartId;
    private StreamBasedJsonDocumentSupplier currentReader;

    public JsonPartsBasedDocumentSupplier(File inputFolder) {
        this(inputFolder, JsonBasedCorpusPartWriter.PART_FILE_PREFIX, JsonBasedCorpusPartWriter.PART_FILE_SUFFIX,
                USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public JsonPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix) {
        this(inputFolder, filePrefix, fileSuffix, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public JsonPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix,
            boolean useDocumentIdsFromFile) {
        this(inputFolder, filePrefix, fileSuffix, useDocumentIdsFromFile, 0, Integer.MAX_VALUE);
    }


    public JsonPartsBasedDocumentSupplier(File inputFolder, int firstPartId, int endPartId) {
        this(inputFolder, JsonBasedCorpusPartWriter.PART_FILE_PREFIX, JsonBasedCorpusPartWriter.PART_FILE_SUFFIX,
                USE_DOCUMENT_IDS_FROM_FILE_DEFAULT, firstPartId, endPartId);
    }

    public JsonPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix,
            boolean useDocumentIdsFromFile, int firstPartId, int endPartId) {
        this.inputFolder = inputFolder;
        this.filePrefix = filePrefix;
        this.fileSuffix = fileSuffix;
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
        this.currentPartId = firstPartId - 1;
        this.endPartId = endPartId;
    }

    @Override
    public synchronized Document getNextDocument() {
        Document document = requestNextDocument();
        if (document != null) {
            if (!useDocumentIdsFromFile) {
                document.setDocumentId(getNextDocumentId());
            }
        }
        return document;
    }

    private Document requestNextDocument() {
        Document document = null;
        if (currentReader != null) {
            document = currentReader.getNextDocument();
        }
        if (document == null) {
            currentReader = getNextReader();
            if (currentReader != null) {
                document = currentReader.getNextDocument();
            }
        }
        return document;
    }

    private StreamBasedJsonDocumentSupplier getNextReader() {
        ++currentPartId;
        StreamBasedJsonDocumentSupplier reader = null;
        if (currentPartId < endPartId) {
            File nextFile = new File(
                    inputFolder.getAbsolutePath() + File.separator + filePrefix + currentPartId + fileSuffix);
            if (nextFile.exists()) {
                reader = StreamBasedJsonDocumentSupplier.createReader(nextFile, useDocumentIdsFromFile);
                if (reader != null) {
                    LOGGER.info("Started reading part " + currentPartId + ".");
                }
            }
        }
        return reader;
    }
}
