/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.io.stream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.apache.commons.lang3.SerializationException;


public class DocumentSupplierSerializer {

    public void serialize(DocumentSupplier supplier, String file) throws SerializationException {
        serialize(supplier, new File(file));
    }

    public int serialize(DocumentSupplier supplier, File file) throws SerializationException {
        int documentCount = 0;
        FileOutputStream fout = null;
        DataOutputStream dout = null;
        try {
            fout = new FileOutputStream(file);
            dout = new DataOutputStream(fout);
            Document document = supplier.getNextDocument();
            while (document != null) {
                writeDocument(dout, document);
                ++documentCount;
                document = supplier.getNextDocument();
            }
        } catch (Exception e) {
            throw new SerializationException("Got Exception while serializing documents.", e);
        } finally {
            if (dout != null) {
                try {
                    dout.close();
                } catch (Exception e) {
                }
            }
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
        }
        return documentCount;
    }

    protected void writeDocument(DataOutputStream dout, Document document) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(bout);
        oout.writeObject(document);
        oout.close();
        dout.writeInt(bout.size());
        bout.writeTo(dout);
    }
}
