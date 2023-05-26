package org.dice_research.topicmodeling.io.es;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentPropertyUtils;
import org.dice_research.topicmodeling.utils.doc.StringContainingDocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class writes JSON files into the given directory that can be used to
 * load a corpus into Elasticsearch. Each file contains a single document. The
 * properties of the document that should be written into the JSON file have to
 * be registered with this object.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class CorpusExporter4Elasticsearch implements DocumentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusExporter4Elasticsearch.class);

    /**
     * The directory to which the files will be written.
     */
    private File outputDirectory;
    /**
     * The function that will be used to derive the file name for a given document.
     */
    private Function<Document, String> documentNameGeneration = d -> Integer.toString(d.getDocumentId());

    private Document2JsonTransformer transformer = new Document2JsonTransformer();

    /**
     * Constructor.
     * 
     * @param outputDirectory The directory to which the files will be written
     */
    public CorpusExporter4Elasticsearch(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void consumeDocument(Document document) {
        String fileName = documentNameGeneration.apply(document);
        if ((fileName == null) || fileName.isEmpty()) {
            LOGGER.error("Got an empty file name for the given document. The document will be ignored.");
            return;
        }
        String outputFile = outputDirectory.getAbsolutePath() + File.separator + fileName;
        try (FileWriter writer = new FileWriter(outputFile)) {
            transformer.transform(document, writer);
        } catch (IOException e) {
            LOGGER.error("Exception while writing file \"" + outputFile + "\".", e);
        }
    }

    /**
     * Setter for the function that is used to derive the file name for a given
     * document. By default, the document ID is used.
     * 
     * @param documentNameGeneration The function that will be used to derive the
     *                               file name for a given document
     */
    public void setDocumentNameGeneration(Function<Document, String> documentNameGeneration) {
        this.documentNameGeneration = documentNameGeneration;
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
        transformer.registerKeyValuePair(key, function);
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
        transformer.registerStringContainingProperty(key, clazz);
    }
}
