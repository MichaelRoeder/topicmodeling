package org.aksw.simba.topicmodeling.utils.doc;

public class DocumentTextWithTermInfo extends AbstractDocumentProperty implements ParseableDocumentProperty,
        StringContainingDocumentProperty {

    private static final long serialVersionUID = -3280782408725589106L;

    protected String textWithTerms;

    public DocumentTextWithTermInfo() {
    }

    public DocumentTextWithTermInfo(String textWithTerms) {
        this.textWithTerms = textWithTerms;
    }

    /**
     * Set the value of textWithTerms
     * 
     * @param textWithTerms
     *            the new value of textWithTerms
     */
    public void setTextWithTerms(String textWithTerms) {
        this.textWithTerms = textWithTerms;
    }

    /**
     * Get the value of text
     * 
     * @return the value of text
     */
    public String getTextWithTerms() {
        return textWithTerms;
    }

    @Override
    public Object getValue() {
        return getTextWithTerms();
    }

    @Override
    public void parseValue(String value) {
        this.textWithTerms = value;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        if (arg0 instanceof DocumentText) {
            if (this.textWithTerms == null) {
                return ((DocumentText) arg0).text == null;
            } else {
                return this.textWithTerms.equals(((DocumentText) arg0).text);
            }
        } else {
            return super.equals(arg0);
        }
    }

    @Override
    public String getStringValue() {
        return textWithTerms;
    }
}
