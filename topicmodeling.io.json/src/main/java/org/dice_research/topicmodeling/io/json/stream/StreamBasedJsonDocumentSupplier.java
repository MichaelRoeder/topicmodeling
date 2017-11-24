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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.json.AbstractDocumentJsonReader;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class StreamBasedJsonDocumentSupplier extends AbstractDocumentJsonReader implements DocumentSupplier, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamBasedJsonDocumentSupplier.class);

    private static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;

    public static StreamBasedJsonDocumentSupplier createReader(String filename) throws FileNotFoundException {
        return createReader(filename, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public static StreamBasedJsonDocumentSupplier createReader(File file) {
        return createReader(file, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public static StreamBasedJsonDocumentSupplier createReader(Reader reader) {
        return createReader(reader, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public static StreamBasedJsonDocumentSupplier createReader(String filename, boolean useDocumentIdsFromFile)
            throws FileNotFoundException {
        return createReader(new File(filename), useDocumentIdsFromFile);
    }

    public static StreamBasedJsonDocumentSupplier createReader(File file, boolean useDocumentIdsFromFile) {
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(file), Charsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Couldn't create FileReader. Returning null.", e);
            return null;
        }
        return createReader(reader, useDocumentIdsFromFile);
    }

    public static StreamBasedJsonDocumentSupplier createReader(Reader reader, boolean useDocumentIdsFromFile) {
        return new StreamBasedJsonDocumentSupplier(new JsonReader(reader), useDocumentIdsFromFile);
    }

    public static StreamBasedJsonDocumentSupplier createReader(String filename, boolean useDocumentIdsFromFile,
            GsonBuilder builder) throws FileNotFoundException {
        return createReader(new File(filename), useDocumentIdsFromFile, builder);
    }

    public static StreamBasedJsonDocumentSupplier createReader(File file, boolean useDocumentIdsFromFile,
            GsonBuilder builder) {
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(file), Charsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("Couldn't create FileReader. Returning null.", e);
            return null;
        }
        return createReader(reader, useDocumentIdsFromFile, builder);
    }

    public static StreamBasedJsonDocumentSupplier createReader(Reader reader, boolean useDocumentIdsFromFile,
            GsonBuilder builder) {
        return new StreamBasedJsonDocumentSupplier(new JsonReader(reader), useDocumentIdsFromFile);
    }

    private boolean useDocumentIdsFromFile;
    private JsonReader reader;
    private Document document;
    private int documentCount = 0;
    private int nextDocumentId;

    private StreamBasedJsonDocumentSupplier(JsonReader reader, boolean useDocumentIdsFromFile) {
        super();
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
        this.reader = reader;
    }

    private StreamBasedJsonDocumentSupplier(JsonReader reader, boolean useDocumentIdsFromFile, GsonBuilder builder) {
        super(builder);
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
        this.reader = reader;
    }

    @Override
    public Document getNextDocument() {
        if (reader != null) {
            // If this is the first document
            if (documentCount == 0) {
                try {
                    // It is possible that we have to read the beginning of the document array,
                    // first.
                    if (reader.hasNext() && JsonToken.BEGIN_ARRAY.equals(reader.peek())) {
                        reader.beginArray();
                    }
                } catch (IOException e) {
                    LOGGER.warn(
                            "Got an IOException when trying to remove the beginning of a document array. Moving on.",
                            e);
                }
            }
            document = readDocument(reader);
            if (document != null) {
                Document nextDocument = document;
                document = null;
                if (!useDocumentIdsFromFile) {
                    nextDocument.setDocumentId(getNextDocumentId());
                }
                ++documentCount;
                if (LOGGER.isInfoEnabled() && ((documentCount % 1000) == 0)) {
                    LOGGER.info("Read the " + documentCount + "th document from JSON file.");
                }
                return nextDocument;
            } else {
                // The parser has reached the end of the file
                IOUtils.closeQuietly(reader);
                reader = null;
            }
        }
        return null;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        nextDocumentId = documentStartId;
    }

    protected int getNextDocumentId() {
        int tempId = nextDocumentId;
        ++nextDocumentId;
        return tempId;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(reader);
    }

}
