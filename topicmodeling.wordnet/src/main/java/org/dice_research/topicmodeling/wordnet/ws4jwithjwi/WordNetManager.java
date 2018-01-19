package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;

public class WordNetManager {
	
	private static final String WORD_NET_FILE = "src/test/resources/WordNet-3.0/dict/";
	
	private static final Logger logger = LoggerFactory.getLogger(WordNetManager.class);

	private static WordNetManager instance;
	
	protected IDictionary dictionary;
	
	public static WordNetManager getInstance() {
		if(instance == null) {
			try {
				instance = new WordNetManager(loadDictionary());
			} catch (IOException e) {
				logger.error("Error while loading WordNet.", e);
				return null;
			}
		}
		return instance;
	}

	protected static IDictionary loadDictionary() throws IOException {
		IDictionary dict = new RAMDictionary(new File(WORD_NET_FILE), ILoadPolicy.IMMEDIATE_LOAD);
		dict.open();
		return dict;
	}
	
	protected WordNetManager(IDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	public IDictionary getDictionary() {
		return dictionary;
	}
}
