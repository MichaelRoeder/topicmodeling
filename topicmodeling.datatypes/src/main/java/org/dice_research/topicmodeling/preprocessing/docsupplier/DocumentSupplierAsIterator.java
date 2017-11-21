package org.dice_research.topicmodeling.preprocessing.docsupplier;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.dice_research.topicmodeling.utils.doc.Document;

public class DocumentSupplierAsIterator implements Iterator<Document> {

    private DocumentSupplier supplier;
    private Document next;

    public DocumentSupplierAsIterator(DocumentSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        synchronized (supplier) {
            prefetch();
            return next != null;
        }
    }

    @Override
    public Document next() {
        synchronized (supplier) {
            prefetch();
            if (next == null) {
                throw new NoSuchElementException();
            }
            Document result = next;
            next = null;
            return result;
        }
    }

    private void prefetch() {
        if (next == null) {
            next = supplier.getNextDocument();
        }
    }

}
