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

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractPreprocessor implements Preprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPreprocessor.class);

    private DocumentSupplier supplier;
    protected Corpus corpus;
    protected boolean corpusCreated = false;

    public AbstractPreprocessor(DocumentSupplier supplier) {
        this.supplier = supplier;
    }

    public AbstractPreprocessor(DocumentSupplier supplier, Corpus corpus) {
        this(supplier);
        this.corpus = corpus;
    }

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        /* nothing to do */
    }

    @Override
    public Corpus getCorpus() {
        if (!corpusCreated) {
            generateCorpus();
        }
        return corpus;
    }

    protected void generateCorpus() {
        if (corpus == null) {
            corpus = getNewCorpus();
        }
        Document document = supplier.getNextDocument();
        while (document != null) {
            addDocumentToCorpus(corpus, document);
            if ((corpus.getNumberOfDocuments() % 1000) == 0) {
                LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
            }
            document = supplier.getNextDocument();
        }
        corpusCreated = true;
        LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
    }

    @Override
    public boolean hasCorpus() {
        return corpusCreated;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
        corpusCreated = false;
    }
    
    protected DocumentSupplier getSupplier() {
        return supplier;
    }

    protected abstract Corpus getNewCorpus();

    protected abstract void addDocumentToCorpus(Corpus corpus, Document document);
}
