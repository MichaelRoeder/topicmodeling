package org.aksw.simba.topicmodeling.preprocessing.shedule;

import java.util.Set;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This simple sheduler can shedule the documents based on the values of a
 * single {@link StringContainingDocumentProperty}. It takes one (or more) sets
 * of possible values. If the property value of a document matches one of the
 * property values in the i-th set of given values, the document will be part of
 * the (i + 1)th document supplier. If the value is part of multiple sets, only
 * the first match is recognized. All documents, that do have values matching
 * none of the given sets will be part of the 0th supplier. Note that documents,
 * that do not have the given property are ignored and won't be part of any
 * supplier.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class ListBasedDocumentSheduler extends AbstractDocumentSheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListBasedDocumentSheduler.class);

    private Class<? extends StringContainingDocumentProperty> propertyClass;
    private Set<String> propertyValuesTestPartition[];

    public ListBasedDocumentSheduler(DocumentSupplier documentSource,
            Class<? extends StringContainingDocumentProperty> propertyClass, Set<String> propertyValuesTestPartition[]) {
        super(documentSource, propertyValuesTestPartition.length + 1);
        this.propertyClass = propertyClass;
        this.propertyValuesTestPartition = propertyValuesTestPartition;
    }

    @Override
    protected Document getNextDocument(int partId) {
        Document document = documentSource.getNextDocument();
        if (document != null) {
            StringContainingDocumentProperty property;
            int partIdForDocument;
            String value;
            do {
                property = document.getProperty(propertyClass);
                if (property == null) {
                    LOGGER.error("Got a document without the needed " + propertyClass.getSimpleName()
                            + ". Can't shedule it. It will be ignored.");
                    partIdForDocument = -1;
                } else {
                    // find the set containing the property value
                    partIdForDocument = 0;
                    value = property.getStringValue();
                    while ((partIdForDocument < propertyValuesTestPartition.length)
                            && (!propertyValuesTestPartition[partIdForDocument].contains(value))) {
                        ++partIdForDocument;
                    }
                    // we want supplier #0 to take all documents that didn't
                    // match anything --> we have to correct the ids
                    if (partIdForDocument < propertyValuesTestPartition.length) {
                        ++partIdForDocument;
                    } else {
                        partIdForDocument = 0;
                    }
                    if (partId != partIdForDocument) {
                        // If this document is not in the part of the corpus
                        // which asks for a new document add the document to the
                        // queue of the part it belongs to and get a new
                        // document
                        listOfParts[partIdForDocument].addDocumentToQueue(document);
                        document = documentSource.getNextDocument();
                    }
                }
            } while ((document != null) && (partId != partIdForDocument));
        }
        return document;
    }

}
