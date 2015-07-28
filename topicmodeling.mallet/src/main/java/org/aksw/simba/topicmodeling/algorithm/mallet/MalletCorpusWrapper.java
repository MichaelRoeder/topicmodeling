package org.aksw.simba.topicmodeling.algorithm.mallet;

import org.aksw.simba.topicmodeling.algorithms.VocabularyContaining;
import org.aksw.simba.topicmodeling.utils.vocabulary.Vocabulary;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.InstanceList;
import cc.mallet.types.MalletAlphabetWrapper;

public class MalletCorpusWrapper extends InstanceList implements VocabularyContaining {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4482043375784081193L;

	public MalletCorpusWrapper(Pipe pipe)
	{
		super(pipe);
	}

	public MalletCorpusWrapper(Pipe pipe, int capacity)
	{
		super(pipe, capacity);
	}
	
	public MalletCorpusWrapper(Alphabet dataAlphabet, Alphabet targetAlphabet)
	{
		super(dataAlphabet, targetAlphabet);
	}

	@Override
	public Vocabulary getVocabulary() {
		return new MalletAlphabetWrapper(this.getDataAlphabet());
	}
}
