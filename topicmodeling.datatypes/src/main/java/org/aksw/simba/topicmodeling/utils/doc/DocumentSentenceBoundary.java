package org.aksw.simba.topicmodeling.utils.doc;

public class DocumentSentenceBoundary extends AbstractSimpleDocumentProperty<int[]> {
    
    private static final long serialVersionUID = 1L;

    public DocumentSentenceBoundary(int start, int length) {
        super(new int[] { start, length });
    }

    public int getStart() {
        return this.get()[0];
    }

    public int getLength() {
        return this.get()[1];
    }

    public int getEnd() {
        return this.get()[0] + this.get()[1];
    }
}
