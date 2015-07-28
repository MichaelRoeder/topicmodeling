package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import org.aksw.simba.topicmodeling.lang.LanguageDependentClass;
import org.aksw.simba.topicmodeling.lang.postagging.PosTagger;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;


@SuppressWarnings("deprecation")
public class PosTaggingSupplierDecorator extends AbstractDocumentSupplierDecorator implements LanguageDependentClass {

    private PosTagger postagger;

    public PosTaggingSupplierDecorator(DocumentSupplier documentSource, PosTagger postagger) {
        super(documentSource);
        this.postagger = postagger;
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text == null) {
            throw new IllegalArgumentException("Got a Document without a DocumentText property!");
        }
        TermTokenizedText ttText = postagger.tokenize(text.getText());
        if (ttText != null) {
            document.addProperty(ttText);
        }
        return document;
    }

    public void setPosTaggerFilter(PosTaggingTermFilter filter) {
        postagger.setFilter(filter);
    }

}
