package org.aksw.simba.topicmodeling.utils.doc;

import java.util.Arrays;

public class DocumentRawData extends AbstractDocumentProperty {

    private static final long serialVersionUID = 6808522383796850927L;

    private final byte data[];

    public DocumentRawData(byte data[]) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public Object getValue() {
        return data;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("=\"");
        builder.append(Arrays.toString(data));
        builder.append("\"");
        return builder.toString();
    };

    @Override
    public boolean equals(Object o) {
        if (o instanceof DocumentRawData) {
            if (this.data == null) {
                return ((DocumentRawData) o).data == null;
            } else {
                return Arrays.equals(this.data, ((DocumentRawData) o).data);
            }
        } else {
            return super.equals(o);
        }
    }
}
