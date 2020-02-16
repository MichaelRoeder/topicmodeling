package org.dice_research.topicmodeling.io.factories;

import java.io.File;

import org.dice_research.topicmodeling.utils.doc.Document;

/**
 * A factory creating a {@link Document} instance from the supplied file.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public interface FileBasedDocumentFactory {

    /**
     * Creates a {@link Document} instance from the given file and assigns the
     * default ID 0.
     * 
     * @param file the file that should be used to create the {@link Document}
     *             instance
     * @return a {@link Document} instance from the given file
     */
    public default Document createDocument(File file) {
        return createDocument(file, 0);
    }

    /**
     * Creates a {@link Document} instance from the given file and assigns the given
     * document ID.
     * 
     * @param file       the file that should be used to create the {@link Document}
     *                   instance
     * @param documentId the ID the newly created instance should have
     * @return a {@link Document} instance from the given file
     */
    public Document createDocument(File file, int documentId);
}
