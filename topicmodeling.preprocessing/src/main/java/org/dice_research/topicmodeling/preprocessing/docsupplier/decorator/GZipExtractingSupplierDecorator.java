package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentInputStream;
import org.dice_research.topicmodeling.utils.doc.DocumentRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipExtractingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipExtractingSupplierDecorator.class);

    public GZipExtractingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        boolean extractedSomething = false;
        // Check if there is an unclosed document input stream
        DocumentInputStream dis = document.getProperty(DocumentInputStream.class);
        if ((dis != null) && (!dis.isClosed())) {
            try {
                dis.set(new GZIPInputStream(dis.get()));
                extractedSomething = true;
            } catch (IOException e) {
                LOGGER.error("Couldn't create extracting input stream for document #" + document.getDocumentId(), e);
            }
        }
        // Check if there is raw data to extract
        DocumentRawData raw = document.getProperty(DocumentRawData.class);
        if (raw != null) {
            try {
                document.addProperty(new DocumentRawData(
                        IOUtils.toByteArray(new GZIPInputStream(new ByteArrayInputStream(raw.getData())))));
                extractedSomething = true;
            } catch (IOException e) {
                LOGGER.error("Couldn't extract raw data for document #" + document.getDocumentId(), e);
            }
        }
        if (!extractedSomething) {
            LOGGER.warn("Couldn't find something to extract for document #{}", document.getDocumentId());
        }
        return document;
    }

}
