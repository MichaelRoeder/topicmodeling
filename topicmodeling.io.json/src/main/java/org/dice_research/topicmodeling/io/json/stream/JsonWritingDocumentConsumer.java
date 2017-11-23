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
package org.dice_research.topicmodeling.io.json.stream;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.json.AbstractDocumentJsonWriter;
import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonWriter;

public class JsonWritingDocumentConsumer extends AbstractDocumentJsonWriter implements DocumentConsumer, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWritingDocumentConsumer.class);

    public static JsonWritingDocumentConsumer createJsonWritingDocumentConsumer(File file) {
        FileOutputStream out = null;
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            out = new FileOutputStream(file);
            return createJsonWritingDocumentConsumer(out);
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file. Returning null.", e);
            IOUtils.closeQuietly(out);
        }
        return null;
    }

    public static JsonWritingDocumentConsumer createJsonWritingDocumentConsumer(OutputStream out) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new BufferedOutputStream(out), Charsets.UTF_8);
            JsonWritingDocumentConsumer consumer = new JsonWritingDocumentConsumer(writer);
            consumer.writeHead();
            return consumer;
        } catch (Exception e) {
            LOGGER.error("Error while trying to write corpus to XML file. Returning null.", e);
            IOUtils.closeQuietly(writer);
        }
        return null;
    }

    protected JsonWriter fout;

    private JsonWritingDocumentConsumer(Writer fout) {
        this.fout = new JsonWriter(fout);
    }

    protected void writeHead() throws IOException {
        fout.beginArray();
    }

    @Override
    public void consumeDocument(Document document) {
        try {
            writeDocument(fout, document);
            fout.flush();
        } catch (IOException e) {
            LOGGER.error("Error while trying to write document #" + document.getDocumentId() + ".", e);
        }
    }

    public void close() throws IOException {
        if (fout != null) {
            fout.endArray();
            fout.close();
        }
    }
}
