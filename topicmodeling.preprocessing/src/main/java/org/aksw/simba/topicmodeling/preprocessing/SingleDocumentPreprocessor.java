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
package org.aksw.simba.topicmodeling.preprocessing;

import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.DocumentListCorpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SingleDocumentPreprocessor extends AbstractDocumentSupplier implements Preprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleDocumentPreprocessor.class);

    private Document document;
    private DocumentSupplier supplier;
    private boolean useConsecutiveNumbering;

    public SingleDocumentPreprocessor() {
        this(false);
    }

    public SingleDocumentPreprocessor(boolean useConsecutiveNumbering) {
        this.useConsecutiveNumbering = useConsecutiveNumbering;
    }

    @Override
    public Document getNextDocument() {
        if (document != null) {
            if (useConsecutiveNumbering) {
                document.setDocumentId(getNextDocumentId());
            }
            Document nextDocument = document;
            document = null;
            return nextDocument;
        }
        return null;
    }

    public Document processDocument(Document document) {
        setDocument(document);
        return getProcessedDocument();
    }

    private Document getProcessedDocument() {
        if (supplier != null) {
            return supplier.getNextDocument();
        } else {
            LOGGER.error("I wasn't configured correctly because I have no document supplier!");
            return document;
        }
    }

    private void setDocument(Document document) {
        this.document = document;
    }

    public void setDocumentSupplier(DocumentSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    @Deprecated
    public void addDocuments(DocumentSupplier documentFactory) {
        /* nothing to do */
    }

    public Corpus getCorpus(Document document) {
        DocumentListCorpus<List<Document>> corpus = new DocumentListCorpus<List<Document>>(new ArrayList<Document>(1));
        Document processedDocument;
        if (document != null) {
            processedDocument = processDocument(document);
        } else {
            processedDocument = getProcessedDocument();
        }
        if (processedDocument != null) {
            corpus.addDocument(processedDocument);
        }
        return corpus;
    }

    @Override
    public Corpus getCorpus() {
        return getCorpus(null);
    }

    @Override
    public boolean hasCorpus() {
        return false;
    }

    @Override
    public void deleteCorpus() {
        /* nothing to do */
    }

}
