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
package org.dice_research.topicmodeling.io.json;

import java.io.IOException;
import java.io.Reader;

import org.dice_research.topicmodeling.io.json.adapters.DocumentAdapter;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public abstract class AbstractDocumentJsonReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDocumentJsonReader.class);

    protected GsonBuilder builder;
    protected Gson gson;

    public AbstractDocumentJsonReader() {
        this(new GsonBuilder());
    }

    public AbstractDocumentJsonReader(GsonBuilder builder) {
        this.builder = builder;
        DocumentAdapter adapter = new DocumentAdapter();
        this.builder.registerTypeAdapter(Document.class, adapter);
        gson = this.builder.create();
        adapter.setGson(gson);
    }

    public Document readDocument(Reader reader) {
        try {
            return gson.fromJson(reader, Document.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Document readDocument(JsonReader reader) {
        try {
            // Check that an object begins
            if ((!reader.hasNext()) && (!JsonToken.BEGIN_OBJECT.equals(reader.peek()))) {
                return null;
            }
        } catch (IOException e) {
            LOGGER.warn("Got an IOException when trying to check whether a JSON object begins. Moving on.", e);
        }
        try {
            return gson.fromJson(reader, Document.class);
        } catch (Exception e) {
            LOGGER.error("Couldn't read document from JSON. Returning null.", e);
            return null;
        }
    }

}
