package org.dice_research.topicmodeling.io.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

public class CorpusJsonWriter extends AbstractDocumentJsonWriter implements CorpusWriter {

    public CorpusJsonWriter() {
        super();
    }
    
    public CorpusJsonWriter(GsonBuilder builder) {
        super(builder);
    }

    @Override
    public void writeCorpus(Corpus corpus) {
        throw new UnsupportedOperationException("This method is deprecated and not supported by this writer.");
    }

    @Override
    public void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out));
            writer.beginArray();
            for(Document document : corpus) {
                writeDocument(writer, document);
            }
            writer.endArray();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

}
