package org.dice_research.topicmodeling.utils.doc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class DocumentInputStream extends AbstractSimpleDocumentProperty<InputStream> implements Closeable {

    private static final long serialVersionUID = 1L;

    private boolean closed = false;

    public DocumentInputStream(InputStream value) {
        super(value);
    }

    public boolean isClosed() {
        return closed || (this.get() != null);
    }

    @Override
    public void close() throws IOException {
        InputStream is = this.get();
        if (is != null) {
            try {
                is.close();
                closed = true;
            } catch (Exception e) {
                // nothing to do
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isClosed()) {
            close();
        }
        super.finalize();
    }

}
