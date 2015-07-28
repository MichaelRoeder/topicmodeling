package org.aksw.simba.topicmodeling.utils.doc;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.charset.Charset;

public class DocumentCharset extends AbstractSimpleDocumentProperty<Charset> implements Externalizable {

    private static final long serialVersionUID = -4084006474209933270L;

    private static final Charset SERIALIZATION_CHARSET = Charset.forName("UTF-8");

    public DocumentCharset() {
        super(SERIALIZATION_CHARSET);
    }

    public DocumentCharset(Charset charset) {
        super(charset);
    }

    public Charset getCharset() {
        return get();
    }

    @Override
    public void readExternal(ObjectInput oIn) throws IOException, ClassNotFoundException {
        byte length = oIn.readByte();
        byte readBytes = 0;
        byte charsetNameBytes[] = new byte[length];
        while (readBytes < length) {
            readBytes += oIn.read(charsetNameBytes, readBytes, length);
        }
        String charsetName = new String(charsetNameBytes, SERIALIZATION_CHARSET);
        try {
            set(Charset.forName(charsetName));
        } catch (IllegalArgumentException e) {
            throw new IOException("Deserialized a faulty Charset name \"" + charsetName + "\".", e);
        }
    }

    @Override
    public void writeExternal(ObjectOutput oOut) throws IOException {
        byte charsetName[] = get().name().getBytes(SERIALIZATION_CHARSET);
        byte length = (byte) charsetName.length;
        oOut.write(length);
        oOut.write(charsetName);
    }
}
