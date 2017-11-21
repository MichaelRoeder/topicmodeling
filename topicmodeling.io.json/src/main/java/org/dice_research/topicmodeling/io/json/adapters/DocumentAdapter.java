package org.dice_research.topicmodeling.io.json.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DocumentAdapter extends TypeAdapter<Document> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAdapter.class);

    private static final String ID_KEY = "id";
    private static final String PROPERTIES_KEY = "properties";
    private static final String PROPERTY_TYPE_KEY = "type";
    private static final String PROPERTY_VALUE_KEY = "value";

    protected Gson gson;

    @Override
    public void write(JsonWriter out, Document document) throws IOException {
        out.beginObject();
        out.name(ID_KEY);
        out.value(document.getDocumentId());
        out.name(PROPERTIES_KEY);
        out.beginArray();
        for (DocumentProperty property : document) {
            writeDocumentProperty(out, property);
        }
        out.endArray();
        out.endObject();
    }

    private void writeDocumentProperty(JsonWriter out, DocumentProperty property) throws IOException {
        out.beginObject();
        out.name(PROPERTY_TYPE_KEY);
        out.value(property.getClass().getCanonicalName());
        out.name(PROPERTY_VALUE_KEY);
        out.jsonValue(gson.toJson(property));
        out.endObject();
    }

    @Override
    public Document read(JsonReader in) throws IOException {
        in.beginObject();
        int id = -1;
        String key;
        List<DocumentProperty> properties = new ArrayList<DocumentProperty>();
        while (in.peek() != JsonToken.END_OBJECT) {
            key = in.nextName();
            switch (key) {
            case ID_KEY: {
                id = (int) in.nextLong();
                break;
            }
            case PROPERTIES_KEY: {
                in.beginArray();
                while (in.hasNext()) {
                    readDocumentProperty(in, properties);
                }
                in.endArray();
                break;
            }
            default: {
                LOGGER.error(
                        "Got an unknown attribute name \"{}\" while parsing an CrawleableUri object. It will be ignored.",
                        key);
            }
            }
        }
        in.endObject();
        return new Document(id, properties.toArray(new DocumentProperty[properties.size()]));
    }

    private void readDocumentProperty(JsonReader in, List<DocumentProperty> properties) throws IOException {
        in.beginObject();
        String key;
        String type = null;
        DocumentProperty value = null;
        while (in.peek() != JsonToken.END_OBJECT) {
            key = in.nextName();
            switch (key) {
            case PROPERTY_TYPE_KEY: {
                type = in.nextString();
                break;
            }
            case PROPERTY_VALUE_KEY: {
                if (type != null) {
                    try {
                        value = gson.fromJson(in, Class.forName(type));
                    } catch (ClassNotFoundException e) {
                        throw new IOException(e);
                    }
                } else {
                    LOGGER.error(
                            "Couldn't read DocumentProperty instance because the type was not defined before reading the value. It will be skipped.");
                    in.skipValue();
                }
                break;
            }
            default: {
                LOGGER.error("Got an unknown attribute name \"{}\" while parsing an object. It will be ignored.", key);
            }
            }
        }
        if (value != null) {
            properties.add(value);
        }
        in.endObject();
    }


}
