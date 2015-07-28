package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.nio.charset.Charset;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCharset;
import org.aksw.simba.topicmodeling.utils.doc.DocumentRawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class CharsetDeterminingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharsetDeterminingSupplierDecorator.class);

    public CharsetDeterminingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
    }

    @Override
    protected Document prepareDocument(Document document) {
        DocumentRawData data = document.getProperty(DocumentRawData.class);
        if (data == null) {
            LOGGER.error("Got a document (#" + document.getDocumentId()
                    + ") without the needed DocumentRawData property. Ignoring it.");
        } else {
            Charset charset = detectCharset(data.getData());
            if (charset != null) {
                document.addProperty(new DocumentCharset(charset));
            }
        }
        return document;
    }

    private Charset detectCharset(byte[] data) {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(data);
        CharsetMatch match = detector.detect();
        try {
            return Charset.forName(match.getName());
        } catch (Exception e) {
            LOGGER.warn("Couldn't determine the charset of the given data. Returning null.");
        }
        return null;
    }
}
