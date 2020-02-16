package org.dice_research.topicmodeling.io.factories;

import java.io.File;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;

/**
 * Abstract implementation of a {@link FileBasedDocumentFactory} which creates
 * the {@link Document} instance, adds the file name as document name and calls
 * the internal abstract method {@link #addFileContent(File, Document)} to read
 * the content of the file.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public abstract class AbstractFileBasedDocumentFactory implements FileBasedDocumentFactory {

    @Override
    public Document createDocument(File file, int documentId) {
        Document document = new Document(documentId);
        document.addProperty(new DocumentName(file.getName()));
        addFileContent(file, document);
        return document;
    }

    /**
     * Adds the content of the given file to the given document. The exact
     * definition how this is done is up to the implementation.
     * 
     * @param file     the file the newly created document is based on
     * @param document the document to which the file content should be added
     */
    protected abstract void addFileContent(File file, Document document);

}
