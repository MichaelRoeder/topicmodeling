package org.aksw.simba.topicmodeling.utils.doc;

public class DocumentDescription extends AbstractSimpleDocumentProperty<String> implements
        StringContainingDocumentProperty, ParseableDocumentProperty {

    private static final long serialVersionUID = 1L;

    public DocumentDescription(String value) {
        super(value);
    }

    @Override
    public String getStringValue() {
        return get();
    }

    @Override
    public void parseValue(String value) {
        set(value);
    }

}
