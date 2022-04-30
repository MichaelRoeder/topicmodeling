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

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link DocumentSupplier} implementation that streams the given documents in
 * the given XML files. The files will be processed in the given order.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class XmlFileListBasedDocumentSupplier extends AbstractMultipleXmlFilesReadingDocumentSupplier
        implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFileListBasedDocumentSupplier.class);

    /**
     * A list of XML files that are read one after the other.
     */
    private File[] files;
    /**
     * The Id of the current file.
     */
    private int currentFileId = -1;

    /**
     * Constructor.
     * 
     * @param files the XML files that should be parsed
     */
    public XmlFileListBasedDocumentSupplier(File[] files) {
        this(files, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public XmlFileListBasedDocumentSupplier(File[] files, boolean useDocumentIdsFromFile) {
        super(useDocumentIdsFromFile);
        this.files = files;
    }

    @Override
    protected synchronized StreamBasedXmlDocumentSupplier getNextReader() {
        ++currentFileId;
        StreamBasedXmlDocumentSupplier reader = null;
        if (currentFileId < files.length) {
            if (files[currentFileId].exists()) {
                reader = StreamBasedXmlDocumentSupplier.createReader(files[currentFileId], useDocumentIdsFromFile);
                if (reader != null) {
                    LOGGER.info("Started reading \"" + files[currentFileId].toString() + "\".");
                }
            }
        }
        return reader;
    }
}
