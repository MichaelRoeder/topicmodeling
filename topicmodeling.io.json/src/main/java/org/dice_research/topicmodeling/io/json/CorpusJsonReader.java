package org.dice_research.topicmodeling.io.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.DocumentListCorpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;

public class CorpusJsonReader extends AbstractDocumentJsonReader implements CorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusJsonReader.class);

    protected Corpus corpus;
    protected InputStream in;

    @Override
    public Corpus getCorpus() {
        if (corpus == null) {
            readCorpus();
        }
        return corpus;
    }

    @Override
    public boolean hasCorpus() {
        return corpus != null;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
    }

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        LOGGER.info("Got a " + documentFactory.getClass().getCanonicalName()
                + " object as DocumentSupplier. But I'm a corpus reader and don't need such a supplier. ");
    }

    @Override
    public void readCorpus(InputStream in) {
        this.corpus = new DocumentListCorpus<List<Document>>(new ArrayList<Document>());
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, Charsets.UTF_8));
            reader.beginArray();
            Document document;
            while (reader.hasNext()) {
                document = readDocument(reader);
                if (document != null) {
                    corpus.addDocument(document);
                }
            }
            reader.endArray();
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized corpus from file", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public void readCorpus() {
        throw new UnsupportedOperationException();
    }

}
