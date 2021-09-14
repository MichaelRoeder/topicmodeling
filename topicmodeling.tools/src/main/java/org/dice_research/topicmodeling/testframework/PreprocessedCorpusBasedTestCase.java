package org.dice_research.topicmodeling.testframework;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.lang.Language;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.Corpus;

public class PreprocessedCorpusBasedTestCase extends AbstractTestCase {

    protected DocumentSupplier documentSupplier;
    protected Corpus corpus;
    protected Language language;

    public PreprocessedCorpusBasedTestCase(ModelingAlgorithm algorithm, Corpus corpus,
            TerminationCondition termCondition) {
        super(algorithm, termCondition);
        this.corpus = corpus;
    }

    @Override
    protected void performPreprocessing() {
        // nothing to do
    }

    @Override
    protected void initilializeAlgorithm() {
        algorithm.initialize(corpus);
        // corpus isn't needed anymore
        // corpus = null;
    }

    @Override
    protected Model performNextIterationStep() {
        algorithm.performNextStep();
        return algorithm.getModel();
    }

    public DocumentSupplier getDocumentSupplier() {
        return documentSupplier;
    }

    public void setDocumentSupplier(DocumentSupplier documentSupplier) {
        this.documentSupplier = documentSupplier;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
