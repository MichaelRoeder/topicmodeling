package org.dice_research.topicmodeling.io.es;

import java.io.Writer;

import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;

public class Document2JsonConsumer extends Document2JsonTransformer implements DocumentConsumer {

    private Writer writer;

    public Document2JsonConsumer(Writer writer) {
        super();
        this.writer = writer;
    }

    @Override
    public void consumeDocument(Document document) {
        transform(document, writer);
    }

}
