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

import java.io.Reader;

import org.dice_research.topicmodeling.io.json.adapters.DocumentAdapter;
import org.dice_research.topicmodeling.utils.doc.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public abstract class AbstractDocumentJsonReader {

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
            return gson.fromJson(reader, Document.class);
        } catch (Exception e) {
            return null;
        }
    }

}
