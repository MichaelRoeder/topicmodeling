package org.aksw.simba.topicmodeling.utils.vocabulary;

public class SingletonVocabularyFactory<T extends Vocabulary> implements VocabularyFactory {
	
	protected T vocabulary;
	
	public SingletonVocabularyFactory(T vocabulary) {
		this.vocabulary = vocabulary;
	}

	@Override
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

}
