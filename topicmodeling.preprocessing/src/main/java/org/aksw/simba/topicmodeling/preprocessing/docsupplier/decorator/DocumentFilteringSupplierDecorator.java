/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter.DocumentFilter;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class DocumentFilteringSupplierDecorator implements DocumentSupplierDecorator {

    protected DocumentSupplier documentSource;
    private DocumentFilter filter;

    public DocumentFilteringSupplierDecorator(DocumentSupplier documentSource, DocumentFilter filter) {
        this.documentSource = documentSource;
        this.filter = filter;
    }

    @Override
    public Document getNextDocument() {
        Document document = null;
        do {
            document = documentSource.getNextDocument();
        } while ((document != null) && (!filter.isDocumentGood(document)));
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        documentSource.setDocumentStartId(documentStartId);
    }

    @Override
    public DocumentSupplier getDecoratedDocumentSupplier() {
        return documentSource;
    }

    @Override
    public void setDecoratedDocumentSupplier(DocumentSupplier supplier) {
        this.documentSource = supplier;
    }
}
