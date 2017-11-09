/**
 * This file is part of topicmodeling.mallet.
 *
 * topicmodeling.mallet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.mallet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.mallet.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.algorithm.mallet;

import org.dice_research.topicmodeling.algorithms.VocabularyContaining;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;

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
