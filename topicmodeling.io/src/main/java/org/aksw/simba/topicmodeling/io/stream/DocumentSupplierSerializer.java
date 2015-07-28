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
