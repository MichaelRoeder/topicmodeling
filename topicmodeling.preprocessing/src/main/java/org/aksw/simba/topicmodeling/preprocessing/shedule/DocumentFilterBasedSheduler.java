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
