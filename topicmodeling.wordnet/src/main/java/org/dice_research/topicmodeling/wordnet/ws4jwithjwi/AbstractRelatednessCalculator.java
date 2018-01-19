package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public abstract class AbstractRelatednessCalculator implements
		RelatednessCalculator {

	@SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory
			.getLogger(AbstractRelatednessCalculator.class);

	protected IDictionary dictionary;

	public AbstractRelatednessCalculator(IDictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public double calcRelatednessOfWords(String word1, POS word1PosTag,
			String word2, POS word2PosTag) {
	    if((word1 == null) || (word2 == null) || (word1.length() == 0) || (word2.length() == 0)) {
	        return getMinPossibleValue();
	    }
	    if(word1.equals(word2)) {
	        return getMaxPossibleValue();
	    }
		IIndexWord idxWord1 = dictionary.getIndexWord(word1, word1PosTag);
		IIndexWord idxWord2 = dictionary.getIndexWord(word2, word2PosTag);
		if (idxWord1 == null) {
//			logger.warn("Couldn't find \"" + word1 + "\" ("
//					+ word1PosTag.toString() + ") in the WordNet.");
			return getMinPossibleValue();
		}
		if (idxWord2 == null) {
//			logger.warn("Couldn't find \"" + word2 + "\" ("
//					+ word2PosTag.toString() + ") in the WordNet.");
			return getMinPossibleValue();
		}
		
		return calcRelatednessOfWords(idxWord1, idxWord2);
	}

	public double calcRelatednessOfWords(IIndexWord word1, IIndexWord word2) {
		List<IWordID> idsWord1 = word1.getWordIDs();
		List<IWordID> idsWord2 = word2.getWordIDs();
		Iterator<IWordID> iteratorIDsWord1 = idsWord1.iterator();
		Iterator<IWordID> iteratorIDsWord2;
		IWord currentMeaningWord1, currentMeaningWord2;
		double currentRelatedness;
		double maxRelatedness = getMinPossibleValue();
		while(iteratorIDsWord1.hasNext()) {
			currentMeaningWord1 = dictionary.getWord(iteratorIDsWord1.next());
			iteratorIDsWord2 = idsWord2.iterator();
			while(iteratorIDsWord2.hasNext()) {
				currentMeaningWord2 = dictionary.getWord(iteratorIDsWord2.next());
				currentRelatedness = calcRelatednessOfWords(currentMeaningWord1, currentMeaningWord2);
				if(currentRelatedness > maxRelatedness) {
					maxRelatedness = currentRelatedness;
				}
			}
		}
		return maxRelatedness;
	}

	@Override
	public double calcRelatednessOfWords(IWord word1, IWord word2) {
		return calcRelatednessOfSynsets(word1.getSynset(), word2.getSynset());
	}
}
