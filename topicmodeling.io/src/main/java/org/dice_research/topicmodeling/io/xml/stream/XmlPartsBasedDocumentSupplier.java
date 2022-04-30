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
package org.dice_research.topicmodeling.io.xml.stream;

import java.io.File;

import org.dice_research.topicmodeling.io.xml.XmlBasedCorpusPartWriter;
import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An instance of a DocumentSupplier which can be used to read several XML-based
 * part files of a corpus consecutively as a single corpus. The reader will read
 * all consecutive part files starting from the given start ID until the given
 * end ID (exclusive). If an intermediate file is missing, the reader stops.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class XmlPartsBasedDocumentSupplier extends AbstractMultipleXmlFilesReadingDocumentSupplier implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPartsBasedDocumentSupplier.class);

    private static final int DEFAULT_START_ID = 0;
    private static final int DEFAULT_END_ID = Integer.MAX_VALUE;

    /**
     * File prefix of the single part files. Default value is
     * {@link XmlBasedCorpusPartWriter#PART_FILE_PREFIX}.
     */
    private String filePrefix;
    /**
     * File suffix of the single part files. Default value is
     * {@link XmlBasedCorpusPartWriter#PART_FILE_PREFIX}.
     */
    private String fileSuffix;
    /**
     * The input directory from which the part files are loaded.
     */
    private final File inputFolder;
    /**
     * The ID of the current part file.
     */
    private int currentPartId;
    /**
     * The ID until that part files should be read. The part with this ID is not
     * included into the reading process.
     */
    private int endPartId;

    public XmlPartsBasedDocumentSupplier(File inputFolder) {
        this(inputFolder, XmlBasedCorpusPartWriter.PART_FILE_PREFIX, XmlBasedCorpusPartWriter.PART_FILE_SUFFIX,
                DEFAULT_START_ID, DEFAULT_END_ID, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, int startId) {
        this(inputFolder, XmlBasedCorpusPartWriter.PART_FILE_PREFIX, XmlBasedCorpusPartWriter.PART_FILE_SUFFIX, startId,
                DEFAULT_END_ID, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, int startId, int endId) {
        this(inputFolder, XmlBasedCorpusPartWriter.PART_FILE_PREFIX, XmlBasedCorpusPartWriter.PART_FILE_SUFFIX, startId,
                endId, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix) {
        this(inputFolder, filePrefix, fileSuffix, DEFAULT_START_ID, DEFAULT_END_ID, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix, int startId) {
        this(inputFolder, filePrefix, fileSuffix, startId, DEFAULT_END_ID, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix, int startId,
            int endId) {
        this(inputFolder, filePrefix, fileSuffix, startId, endId, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix, int startId, int endId,
            boolean useDocumentIdsFromFile) {
        super(useDocumentIdsFromFile);
        this.inputFolder = inputFolder;
        this.filePrefix = filePrefix;
        this.fileSuffix = fileSuffix;
        this.currentPartId = startId - 1;
        this.endPartId = endId;
    }

    @Override
    protected StreamBasedXmlDocumentSupplier getNextReader() {
        ++currentPartId;
        StreamBasedXmlDocumentSupplier reader = null;
        if (currentPartId < endPartId) {
            File nextFile = new File(
                    inputFolder.getAbsolutePath() + File.separator + filePrefix + currentPartId + fileSuffix);
            if (nextFile.exists()) {
                reader = StreamBasedXmlDocumentSupplier.createReader(nextFile, useDocumentIdsFromFile);
                if (reader != null) {
                    LOGGER.info("Started reading part " + currentPartId + ".");
                }
            }
        }
        return reader;
    }
}
