package org.aksw.simba.topicmodeling.utils.doc;

/**
 * Class DocumentId
 */
public class DocumentName extends AbstractSimpleDocumentProperty<String> implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -6517249709629701914L;

    public DocumentName() {
        super("");
    }

    public DocumentName(String name) {
        super(name);
    }

    public String getName() {
        return get();
    }

    public void setName(String name) {
        set(name);
    }

    @Override
    public void parseValue(String value) {
        set(value);
    }

    @Override
    public String getStringValue() {
        return get();
    }
}
