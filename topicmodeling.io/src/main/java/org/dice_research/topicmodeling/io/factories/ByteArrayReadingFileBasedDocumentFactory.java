package org.dice_research.topicmodeling.io.factories;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple file-based document factory reading the file content as byte array and
 * adding it as {@link DocumentRawData} property. Mainly used for backwards
 * compatibility.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class ByteArrayReadingFileBasedDocumentFactory extends AbstractFileBasedDocumentFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayReadingFileBasedDocumentFactory.class);

    @Override
    protected void addFileContent(File file, Document document) {
        try {
            document.addProperty(new DocumentRawData(FileUtils.readFileToByteArray(file)));
        } catch (IOException e) {
            LOGGER.error(
                    "Exception while reading data from file. Won't be able to add file content to the generated document.",
                    e);
        }
    }

}
