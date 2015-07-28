package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.PosTagger;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.PosTaggingSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NerPropagatingSupplierDecorator extends PosTaggingSupplierDecorator implements PosTaggingTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NerPropagatingSupplierDecorator.class);

    private NerPropagationPreprocessor preprocessor;
    private NerPropagationPostprocessor postprocessor;
    private PosTaggingTermFilter additionalFilter;
    private Set<String> tokens = null;

    public NerPropagatingSupplierDecorator(DocumentSupplier documentSource, PosTagger postagger) {
        this(documentSource, postagger, new SimpleNerPropagationPreprocessor());
    }

    public NerPropagatingSupplierDecorator(DocumentSupplier documentSource, PosTagger postagger,
            NerPropagationPreprocessor preprocessor) {
        this(documentSource, postagger, preprocessor, new SimpleNerPropagationPostprocessor());
    }

    public NerPropagatingSupplierDecorator(DocumentSupplier documentSource, PosTagger postagger,
            NerPropagationPreprocessor preprocessor, NerPropagationPostprocessor postprocessor) {
        super(documentSource, postagger);
        additionalFilter = postagger.getFilter();
        super.setPosTaggerFilter(this);
        this.preprocessor = preprocessor;
        this.postprocessor = postprocessor;
    }

    @Override
    public boolean isTermGood(Term term) {
        if (tokens != null) {
            return tokens.contains(term.getWordForm()) || (additionalFilter == null) || additionalFilter.isTermGood(term);
        } else {
            return (additionalFilter == null) || additionalFilter.isTermGood(term);
        }
    }

    @Override
    public void setPosTaggerFilter(PosTaggingTermFilter filter) {
        additionalFilter = filter;
    }

    @Override
    public Document getNextDocument() {
        Document document = documentSource.getNextDocument();
        if (document != null) {
            DocumentText text = document.getProperty(DocumentText.class);
            if (text == null) {
                LOGGER.error("Got a Document without the needed DocumentText property!");
                return document;
            }
            NamedEntitiesInText nes = document.getProperty(NamedEntitiesInText.class);
            if (nes == null) {
                LOGGER.error("Got a Document without a NamedEntitiesInDocument property!");
            } else if (preprocessor != null) {
                // Preprocessing
                tokens = preprocessor.preprocessNamedEntities(text, nes);
            }

            // POS-Tagging + Filtering
            document = super.prepareDocument(document);

            TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
            if (tttext == null) {
                LOGGER.error("Got a Document without the needed TermTokenizedText property!");
                return document;
            } else if (nes != null) {
                // Postprocessing
                document.addProperty(postprocessor.postprocessNamedEntities(nes, text, tttext));
            }
        }
        return document;
    }
}
