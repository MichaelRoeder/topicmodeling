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


public class XmlPartsBasedDocumentSupplier extends AbstractDocumentSupplier implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPartsBasedDocumentSupplier.class);

    private static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;

    private String filePrefix;
    private String fileSuffix;
    private boolean useDocumentIdsFromFile;
    private final File inputFolder;
    private int currentPartId = -1;
    private StreamBasedXmlDocumentSupplier currentReader;

    public XmlPartsBasedDocumentSupplier(File inputFolder) {
        this(inputFolder, XmlBasedCorpusPartWriter.PART_FILE_PREFIX, XmlBasedCorpusPartWriter.PART_FILE_SUFFIX,
                USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix) {
        this(inputFolder, filePrefix, fileSuffix, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlPartsBasedDocumentSupplier(File inputFolder, String filePrefix, String fileSuffix,
            boolean useDocumentIdsFromFile) {
        this.inputFolder = inputFolder;
        this.filePrefix = filePrefix;
        this.fileSuffix = fileSuffix;
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
    }

    @Override
    public Document getNextDocument() {
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

    private StreamBasedXmlDocumentSupplier getNextReader() {
        ++currentPartId;
        File nextFile = new File(inputFolder.getAbsolutePath() + File.separator + filePrefix + currentPartId
                + fileSuffix);
        StreamBasedXmlDocumentSupplier reader = null;
        if (nextFile.exists()) {
            reader = StreamBasedXmlDocumentSupplier.createReader(nextFile, useDocumentIdsFromFile);
            if (reader != null) {
                LOGGER.info("Started reading part " + currentPartId + ".");
            }
        }
        return reader;
    }
}
