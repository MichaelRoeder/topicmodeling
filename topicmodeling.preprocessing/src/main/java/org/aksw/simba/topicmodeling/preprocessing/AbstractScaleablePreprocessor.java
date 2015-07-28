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
