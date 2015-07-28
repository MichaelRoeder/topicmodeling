package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.nio.charset.Charset;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCharset;
import org.aksw.simba.topicmodeling.utils.doc.DocumentRawData;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;


public class DocumentTextCreatingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private final Charset defaultCharset;

    public DocumentTextCreatingSupplierDecorator(DocumentSupplier documentSource) {
        this(documentSource, DEFAULT_CHARSET);
    }

    public DocumentTextCreatingSupplierDecorator(DocumentSupplier documentSource, Charset defaultCharset) {
        super(documentSource);
        this.defaultCharset = defaultCharset;
    }

    @Override
    public Document prepareDocument(Document document) {
        DocumentRawData data = document.getProperty(DocumentRawData.class);
        if (data == null) {
            throw new IllegalArgumentException("Got a document without DocumentRawData property.");
        }
        DocumentCharset docCharset = document.getProperty(DocumentCharset.class);
        if (docCharset == null) {
            docCharset = new DocumentCharset(defaultCharset);
            document.addProperty(docCharset);
        }
        document.addProperty(new DocumentText(new String(data.getData(), docCharset.getCharset())));
        return document;
    }

}
