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
package org.dice_research.topicmodeling.io.stream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentSupplierDeSerializer implements DocumentSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentSupplierDeSerializer.class);

    public static DocumentSupplierDeSerializer create(String file) {
        return create(new File(file));
    }

    public static DocumentSupplierDeSerializer create(File file) {
        FileInputStream fin = null;
        DataInputStream din = null;
        try {
            fin = new FileInputStream(file);
            din = new DataInputStream(fin);
            return new DocumentSupplierDeSerializer(fin, din);
        } catch (Exception e) {
            LOGGER.error("Couldn't read the serialized document supplier. Returning null.");
            e.printStackTrace();
        }
        if (din != null) {
            try {
                din.close();
            } catch (IOException e) {
            }
        }
        if (fin != null) {
            try {
                fin.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    private FileInputStream fin;
    private DataInputStream din;

    protected DocumentSupplierDeSerializer(FileInputStream fin, DataInputStream din) {
        this.fin = fin;
        this.din = din;
    }

    @Override
    public Document getNextDocument() {
        if (din != null) {
            try {
                if (din.available() > 0) {
                    return readDocument();
                }
            } catch (Exception e) {
                LOGGER.error(
                        "Exception while trying to read a serialized document. Returning null and closing input streams.",
                        e);
                e.printStackTrace();
            }
            try {
                din.close();
            } catch (IOException e) {
            }
            din = null;
            try {
                fin.close();
            } catch (IOException e) {
            }
            fin = null;
        }
        return null;
    }

    protected Document readDocument() throws IOException, ClassNotFoundException {
        int length = din.readInt();
        byte bytes[] = new byte[length];
        int readLength = 0;
        while ((din.available() > 0) && (readLength < length)) {
            readLength += din.read(bytes, readLength, length);
        }
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream oin = new ObjectInputStream(bin);
        Document document = (Document) oin.readObject();
        oin.close();
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        // nothing to do
    }
}
