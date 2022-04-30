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

import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;

/**
 * An abstract implementation of a class that parses {@link Document} instances
 * from several XML files. It will go through the files in a sequential order.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public abstract class AbstractMultipleXmlFilesReadingDocumentSupplier extends AbstractDocumentSupplier
        implements DocumentSupplier {

    protected static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;

    /**
     * Flag indicating whether the document IDs of the original file should be used
     * or whether the documents should receive new IDs. The default value is
     * {@value #USE_DOCUMENT_IDS_FROM_FILE_DEFAULT}.
     */
    protected boolean useDocumentIdsFromFile;
    /**
     * The current reader instance used to read from the current part file.
     */
    private StreamBasedXmlDocumentSupplier currentReader;

    public AbstractMultipleXmlFilesReadingDocumentSupplier() {
        this(USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public AbstractMultipleXmlFilesReadingDocumentSupplier(boolean useDocumentIdsFromFile) {
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

    protected Document requestNextDocument() {
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

    /**
     * Abstract method used to get the next {@link StreamBasedXmlDocumentSupplier}
     * or {@code null} if there is no more XML file that could be read. It is called
     * when the current supplier has reached its end.
     * 
     * @return the next {@link StreamBasedXmlDocumentSupplier} instance
     */
    protected abstract StreamBasedXmlDocumentSupplier getNextReader();
}
