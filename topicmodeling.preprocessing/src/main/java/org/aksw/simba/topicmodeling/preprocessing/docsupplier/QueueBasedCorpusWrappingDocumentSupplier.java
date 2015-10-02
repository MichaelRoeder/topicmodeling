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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier;

import java.util.LinkedList;
import java.util.Queue;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;


public class QueueBasedCorpusWrappingDocumentSupplier implements DocumentSupplier {

    private Queue<Document> queue;

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus) {
        this(corpus, 0, corpus.getNumberOfDocuments());
    }

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus, int documentStartId) {
        this(corpus, documentStartId, corpus.getNumberOfDocuments());
    }

    public QueueBasedCorpusWrappingDocumentSupplier(Corpus corpus, int documentStartId, int documentEndId) {
        queue = new LinkedList<Document>(corpus.getDocuments(documentStartId, documentEndId));
    }

    @Override
    public Document getNextDocument() {
        Document document = queue.poll();
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        throw new IllegalStateException("This supplier does not have a start document Id.");
    }
}