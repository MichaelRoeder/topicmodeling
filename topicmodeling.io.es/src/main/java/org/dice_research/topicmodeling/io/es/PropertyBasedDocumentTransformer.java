package org.dice_research.topicmodeling.io.es;

import java.util.function.Function;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;

public class PropertyBasedDocumentTransformer implements Function<Document, Object> {
    private Class<? extends DocumentProperty> propertyClass;

    public PropertyBasedDocumentTransformer(Class<? extends DocumentProperty> propertyClass) {
        super();
        this.propertyClass = propertyClass;
    }

    @Override
    public Object apply(Document document) {
        DocumentProperty dp = document.getProperty(propertyClass);
        if (dp != null) {
            return dp.getValue();
        } else {
            return null;
        }
    }

}
