package org.aksw.simba.topicmodeling.utils.doc;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StringReader;
import java.io.StringWriter;

import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;

import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hppc.HppcModule;

public class DocumentWordCounts extends AbstractDocumentProperty implements DocumentProperty, Externalizable {

	private static final long serialVersionUID = -1765808256696402667L;

	protected IntIntOpenHashMap wordCounts = new IntIntOpenHashMap();
	
    public DocumentWordCounts() {
        
    }

    @Deprecated
	public DocumentWordCounts(Vocabulary vocabulary) {
		this(vocabulary.size());
	}

	@Deprecated
	public DocumentWordCounts(int vocabularySize) {
	}

	@Override
	public Object getValue() {
		return wordCounts;
	}

	public void setWordCount(int wordId, int count) {
	    wordCounts.put(wordId, count);
	}

	public void increaseWordCount(int wordId, int count) {
        wordCounts.putOrAdd(wordId, count, count);
	}

	public int getCountForWord(int wordId) {
		if (wordCounts.containsKey(wordId)) {
			return wordCounts.get(wordId);
		} else {
			return 0;
		}
	}
	
	public int getNumberOfWords() {
		return wordCounts.size();
	}
    
    public int getSumOfWordCounts() {
        int sum = 0;
        for (IntIntCursor wordCount : wordCounts) {
            sum += wordCount.value;
        }
        return sum;
    }
    
    public IntIntOpenHashMap getWordCounts() {
        return wordCounts;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String input = (String) in.readObject();
        StringReader reader = new StringReader(input);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HppcModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        wordCounts = mapper.readValue(reader, IntIntOpenHashMap.class);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new HppcModule());
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, wordCounts);
        out.writeObject(writer.toString());
    }
}
