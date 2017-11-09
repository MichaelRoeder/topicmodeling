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
package org.dice_research.topicmodeling.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentCharset;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentFolderWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFolderWriter.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String DEFAULT_FILE_ENDING = ".html";

    private final String folder;
    private final String fileEnding;

    public DocumentFolderWriter(String folder) {
        fileEnding = DEFAULT_FILE_ENDING;
        if (folder.endsWith("/")) {
            this.folder = folder;
        } else {
            this.folder = folder + "/";
        }
    }

    public DocumentFolderWriter(String folder, String fileEnding) {
        this.fileEnding = fileEnding;
        if (folder.endsWith("/")) {
            this.folder = folder;
        } else {
            this.folder = folder + "/";
        }
    }

    public void writeDocuments(DocumentSupplier supplier) {
        DocumentCategory category;
        DocumentText text;
        DocumentCharset charsetProperty;
        Charset charset;
        Document document = supplier.getNextDocument();
        File file;
        while (document != null) {
            text = document.getProperty(DocumentText.class);
            if (text != null) {
                // Create the path
                category = document.getProperty(DocumentCategory.class);
                if (category != null) {
                    file = new File(folder + category.getCategory() + "/" + document.getDocumentId() + fileEnding);
                } else {
                    file = new File(folder + document.getDocumentId() + fileEnding);
                }
                file.getParentFile().mkdirs();
                // Get the correct Charset
                charsetProperty = document.getProperty(DocumentCharset.class);
                if (charsetProperty != null) {
                    charset = charsetProperty.getCharset();
                } else {
                    charset = DEFAULT_CHARSET;
                }
                try {
                    FileUtils.writeStringToFile(file, text.getText(), charset.name());
                } catch (IOException e) {
                    LOGGER.error("Error writing document text to file.", e);
                }
            } else {
                LOGGER.error("Got a Document without text property. (id = " + document.getDocumentId() + ").");
            }
            document = supplier.getNextDocument();
        }
    }
}
