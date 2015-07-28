package org.aksw.simba.topicmodeling.utils.doc;


/**
 * Class DocumentClass
 */
public class DocumentCategory extends AbstractSimpleDocumentProperty<String> implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -799781054037155226L;

    public DocumentCategory() {
        super("");
    }

    public DocumentCategory(String category) {
        super(category);
    }

    public String getCategory() {
        return get();
    }

    public void setCategory(String category) {
        set(category);
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
