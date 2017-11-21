package org.dice_research.topicmodeling.io.json;

import java.io.IOException;
import java.io.OutputStream;

import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.utils.corpus.Corpus;

import com.google.gson.Gson;

public class CorpusJsonWriter implements CorpusWriter {
    
    protected GsonBuilder builder 

    @Override
    public void writeCorpus(Corpus corpus) {
        throw new UnsupportedOperationException("This method is deprecated and not supported by this writer.");
    }

    @Override
    public void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        
    }
    
}
