package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.ClassificationModel;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.preprocessing.docsupplier.CorpusWrappingDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.DocumentWordCountingSupplierDecorator;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.properties.ClassificationModelVersion;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentClassificationResult;
import org.dice_research.topicmodeling.utils.doc.DocumentTextWordIds;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvaluatorWithClassifiedTestCorpus extends AbstractEvaluatorWithTestCorpus<Corpus> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEvaluatorWithClassifiedTestCorpus.class);

    protected DocumentClassificationResult classifications[];

    public AbstractEvaluatorWithClassifiedTestCorpus(Corpus testCorpus) {
        super(testCorpus);
    }

    public abstract EvaluationResult evaluateModelWithClassifiedCorpus(ClassificationModel model,
            ManagedEvaluationResultContainer previousResults);

    @Override
    public EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
        if (!(model instanceof ClassificationModel)) {
            throw new IllegalArgumentException("Got a " + model.getClass().getCanonicalName()
                    + " as Model while expecting a " + ClassificationModel.class.getCanonicalName());
        }
        ClassificationModel cm = (ClassificationModel) model;
        ClassificationModelVersion corpusVersion = testCorpus.getProperty(ClassificationModelVersion.class);
        if ((corpusVersion == null) || (model.getVersion() != corpusVersion.get())) {
            classifyDocuments(cm);
        }
        return evaluateModelWithClassifiedCorpus(cm, previousResults);
    }

    protected void classifyDocuments(ClassificationModel model) {
        classifications = classifyDocuments(model, this.testCorpus);
    }

    protected DocumentClassificationResult[] classifyDocuments(ClassificationModel model, Corpus corpus) {
        prepareCorpus(corpus);
        Document document;
        DocumentClassificationResult classifications[] = new DocumentClassificationResult[corpus
                .getNumberOfDocuments()];
        for (int documentId = 0; documentId < corpus.getNumberOfDocuments(); ++documentId) {
            document = corpus.getDocument(documentId);
            // document.addProperty(model.getClassificationForDocument(document));
            classifications[documentId] = model.getClassificationForDocument(document);
        }
        return classifications;
    }

    protected void prepareCorpus(Corpus corpus) {
        // Check whether the documents contain DocumentWordCounts
        Document document = corpus.getDocument(0);
        if (document != null) {
            if (document.getProperty(DocumentWordCounts.class) == null) {
                if (document.getProperty(DocumentTextWordIds.class) != null) {
                    DocumentSupplier supplier = new CorpusWrappingDocumentSupplier(corpus);
                    supplier = new DocumentWordCountingSupplierDecorator(supplier);
                    document = supplier.getNextDocument();
                    while (document != null) {
                        document = supplier.getNextDocument();
                    }
                } else {
                    LOGGER.warn(
                            "It seems that the documents of the given corpus do not have DocumentWordCounts and DocumentTextWordIds properties. It is possible that the classification won't work.");
                }
            }
        }
    }
}
