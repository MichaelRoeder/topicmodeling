package org.aksw.simba.topicmodeling.preprocessing.shedule;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter.DocumentFilter;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class DocumentFilterBasedSheduler extends AbstractDocumentSheduler {

    private DocumentFilter filters[];

    public DocumentFilterBasedSheduler(DocumentSupplier documentSource, DocumentFilter filters[]) {
        super(documentSource, filters.length + 1);
        this.filters = filters;
    }

    @Override
    protected Document getNextDocument(int partId) {
        Document document = documentSource.getNextDocument();
        int partForDocument;
        while (document != null) {
            partForDocument = 0;
            // search for a part which has a filter that would take this document
            while ((partForDocument < filters.length) && (!filters[partForDocument].isDocumentGood(document))) {
                ++partForDocument;
            }
            if (partForDocument == partId) {
                return document;
            } else {
                listOfParts[partForDocument].addDocumentToQueue(document);
            }
            document = documentSource.getNextDocument();
        }
        return null;
    }

}
