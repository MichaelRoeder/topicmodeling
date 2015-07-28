package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;

public interface DocumentSupplierDecorator extends DocumentSupplier {

    public DocumentSupplier getDecoratedDocumentSupplier();

    public void setDecoratedDocumentSupplier(DocumentSupplier supplier);
}
