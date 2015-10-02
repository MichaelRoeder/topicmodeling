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

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractScaleablePreprocessor extends AbstractPreprocessor implements ScaleablePreprocessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScaleablePreprocessor.class);

    public AbstractScaleablePreprocessor(DocumentSupplier supplier) {
        super(supplier);
    }

    public AbstractScaleablePreprocessor(DocumentSupplier supplier, Corpus corpus) {
        super(supplier, corpus);
    }

    @Override
    public Corpus getCorpus(int numberOfDocuments) {
        if (!corpusCreated) {
            generateCorpus(numberOfDocuments);
        }
        return corpus;
    }

    private void generateCorpus(int numberOfDocuments) {
        if (corpus == null) {
            corpus = getNewCorpus();
        }
        DocumentSupplier supplier = this.getSupplier();
        Document document = supplier.getNextDocument();
        while ((document != null) && (corpus.getNumberOfDocuments() < numberOfDocuments)) {
            addDocumentToCorpus(corpus, document);
            if ((corpus.getNumberOfDocuments() % 1000) == 0) {
                LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
            }
            document = supplier.getNextDocument();
        }
        corpusCreated = true;
        LOGGER.info("Corpus has " + corpus.getNumberOfDocuments() + " documents.");
    }

}
