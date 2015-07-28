package org.aksw.simba.topicmodeling.utils.corpus;

import org.aksw.simba.topicmodeling.algorithms.VocabularyContaining;
import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;


@Deprecated
public interface BagOfWordsCorpus extends Corpus, VocabularyContaining {

	public void increaseWordCount(String word, int documentId);

	public void increaseWordCount(String word, int documentId, int count);

	public int getWordCount(int wordId, int documentId);

	public int getNumberOfDocuments();
	
	public DocumentWordCounts getWordCountsForDocument(int documentId);

	public int getNumberOfWords();
}
