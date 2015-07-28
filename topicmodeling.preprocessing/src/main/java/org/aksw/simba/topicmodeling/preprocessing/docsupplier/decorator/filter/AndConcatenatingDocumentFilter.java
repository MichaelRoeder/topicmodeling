package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import org.aksw.simba.topicmodeling.utils.doc.Document;

public class AndConcatenatingDocumentFilter implements DocumentFilter {

    private DocumentFilter filters[];

    public AndConcatenatingDocumentFilter(DocumentFilter[] filters) {
        this.filters = filters;
    }

    @Override
    public boolean isDocumentGood(Document document) {
        boolean isGood = true;
        int pos = 0;
        while (isGood && (pos < filters.length)) {
            isGood = filters[pos].isDocumentGood(document);
            ++pos;
        }
        return isGood;
    }

}
