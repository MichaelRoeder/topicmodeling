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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.function.Function;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;

public abstract class AbstractDocumentSupplierDecorator
        implements DocumentSupplierDecorator, Function<Document, Document> {

    protected DocumentSupplier documentSource;
    protected boolean requestNextIfFailure = false;

    public AbstractDocumentSupplierDecorator(DocumentSupplier documentSource) {
        this(documentSource, false);
    }

    public AbstractDocumentSupplierDecorator(DocumentSupplier documentSource, boolean requestNextIfFailure) {
        this.documentSource = documentSource;
        this.requestNextIfFailure = requestNextIfFailure;
    }

    @Override
    public Document getNextDocument() {
        Document document = documentSource.getNextDocument();
        while (document != null) {
            document = prepareDocument(document);
            if (document != null) {
                // Return the prepared document
                return document;
            } else if (requestNextIfFailure) {
                // Get the next document
                document = documentSource.getNextDocument();
            } else {
                // We should simply return null instead of the document
                return null;
            }
            if ((document == null) && requestNextIfFailure) {
            }
        }
        // the document source is empty, so return null
        return null;
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

    protected abstract Document prepareDocument(Document document);

    @Override
    public Document apply(Document document) {
        return prepareDocument(document);
    }
}
