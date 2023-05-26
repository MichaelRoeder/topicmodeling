package org.dice_research.topicmodeling.io.es;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentPropertyUtils;
import org.dice_research.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonWriter;

public class Document2JsonTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Document2JsonFunction.class);

    /**
     * The registry that contains the keys and functions to derive the values based
     * on the given document.
     */
    private Map<String, Function<Document, String>> registry = new HashMap<>();

    public void transform(Document document, Writer writer) {
        String value;
        try (JsonWriter jWriter = new JsonWriter(writer)) {
            jWriter.beginObject();
            for (String key : registry.keySet()) {
                value = registry.get(key).apply(document);
                if (value != null) {
                    jWriter.name(key);
                    jWriter.value(value);
                }
            }
            jWriter.endObject();
        } catch (IOException e) {
            LOGGER.error("Exception while writing document \"" + document.getDocumentId() + "\".", e);
        }
    }

    /**
     * Registers the given function with this consumer. For each document, the
     * function will be called once to derive a value. The created key-value pair
     * will be stored in the JSON of the document.
     * 
     * @param key      the key of the value that will be derived with the given
     *                 function
     * @param function the function that will be used to derive the value from the
     *                 given document
     */
    public void registerKeyValuePair(String key, Function<Document, String> function) {
        registry.put(key, function);
    }

    /**
     * Registers the given StringContainingDocumentProperty class with this
     * consumer. For each document, the value of the given property will be derived
     * and stored with the given key in the document's JSON.
     * 
     * @param key   the key of the value that will be derived from the given
     *              StringContainingDocumentProperty class
     * @param clazz the StringContainingDocumentProperty class which will be used to
     *              get the value for the given key
     */
    public void registerStringContainingProperty(String key, Class<? extends StringContainingDocumentProperty> clazz) {
        registry.put(key, d -> DocumentPropertyUtils.getStringOrNull(d, clazz));
    }

}
