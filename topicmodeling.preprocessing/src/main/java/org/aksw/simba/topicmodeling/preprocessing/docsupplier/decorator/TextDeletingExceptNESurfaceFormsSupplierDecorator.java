package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.Arrays;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TextDeletingExceptNESurfaceFormsSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextDeletingExceptNESurfaceFormsSupplierDecorator.class);

    public TextDeletingExceptNESurfaceFormsSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentText text = document.getProperty(DocumentText.class);
        if (text != null) {
            NamedEntitiesInText nes = document.getProperty(NamedEntitiesInText.class);
            if (nes != null) {
                deleteTextExceptNEs(text, nes);
            } else {
                LOGGER.error("Couldn't get needed NamedEntitiesInText property from document. Ignoring it.");
            }
        } else {
            LOGGER.error("Couldn't get needed DocumentText property from document. Ignoring it.");
        }
        return document;
    }

    private void deleteTextExceptNEs(DocumentText text, NamedEntitiesInText nes) {
        String originalText = text.getText();
        StringBuilder newTextBuilder = new StringBuilder();
        int newStartPos;
        NamedEntityInText neArray[] = nes.getNamedEntities().toArray(
                new NamedEntityInText[nes.getNamedEntities().size()]);
        // Sort it (descending)
        Arrays.sort(neArray);
        // start from the end
        for (int i = neArray.length - 1; i >= 0; --i) {
            if (i < (neArray.length - 1)) {
                newTextBuilder.append(' ');
            }
            newStartPos = newTextBuilder.length();
            newTextBuilder.append(originalText.substring(neArray[i].getStartPos(), neArray[i].getEndPos()));
            neArray[i].setStartPos(newStartPos);
        }
        text.setText(newTextBuilder.toString());
    }
}
