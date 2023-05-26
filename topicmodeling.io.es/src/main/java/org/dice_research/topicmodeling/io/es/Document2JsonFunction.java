package org.dice_research.topicmodeling.io.es;

import java.io.StringWriter;
import java.util.function.Function;

import org.dice_research.topicmodeling.utils.doc.Document;

public class Document2JsonFunction extends Document2JsonTransformer implements Function<Document, String> {

    @Override
    public String apply(Document document) {
        StringWriter writer = new StringWriter();
        transform(document, writer);
        return writer.toString();
    }

}
