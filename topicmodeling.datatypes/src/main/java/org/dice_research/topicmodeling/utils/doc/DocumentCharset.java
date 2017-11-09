/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.utils.doc;

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
