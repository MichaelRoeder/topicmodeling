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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.splitter;

import java.util.LinkedList;
import java.util.Queue;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.DocumentSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;


/**
 * This is an abstract super class for {@link DocumentSupplierDecorator}s which are splitting up documents into multiple
 * documents.
 * 
 * @author m.roeder
 * 
 */
public abstract class AbstractSplittingDocumentSupplierDecorator extends AbstractDocumentSupplier implements
        DocumentSupplierDecorator {

    protected Queue<Document> queue = new LinkedList<Document>();
    protected DocumentSupplier documentSource;

    public AbstractSplittingDocumentSupplierDecorator(DocumentSupplier documentSource) {
        this.documentSource = documentSource;
    }

    @Override
    public Document getNextDocument() {
        if (queue.size() == 0) {
            createNextDocuments();
        }
        return queue.poll();
    }

    private void createNextDocuments() {
        Document document;
        do {
            document = documentSource.getNextDocument();
            splitDocument(document);
        } while ((document != null) && (queue.size() == 0));
    }

    protected void addToQueue(Document document) {
        queue.add(document);
    }

    /**
     * In this method the splitter should split up the given document and add all new documents to the {@link #queue}.
     * 
     * @param document
     */
    protected abstract void splitDocument(Document document);

    @Override
    public DocumentSupplier getDecoratedDocumentSupplier() {
        return documentSource;
    }

    @Override
    public void setDecoratedDocumentSupplier(DocumentSupplier supplier) {
        this.documentSource = supplier;
    }

}
