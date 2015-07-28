package org.aksw.simba.topicmodeling.utils.doc;

/**
 * Class DocumentId
 */
public class DocumentSource extends AbstractSimpleDocumentProperty<String> implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -6144552726686647479L;

    public DocumentSource() {
        super("");
    }

    public DocumentSource(String source) {
        super(source);
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
