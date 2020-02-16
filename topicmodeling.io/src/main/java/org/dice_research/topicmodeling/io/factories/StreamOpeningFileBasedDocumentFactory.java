package org.dice_research.topicmodeling.io.factories;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamOpeningFileBasedDocumentFactory extends AbstractFileBasedDocumentFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamOpeningFileBasedDocumentFactory.class);

    @Override
    protected void addFileContent(File file, Document document) {
        try {
            document.addProperty(new DocumentInputStream(new BufferedInputStream(new FileInputStream(file))));
        } catch (FileNotFoundException e) {
            LOGGER.error(
                    "Exception while opening stream to file. Won't be able to add input stream to the generated document.",
                    e);
        }
    }

}
