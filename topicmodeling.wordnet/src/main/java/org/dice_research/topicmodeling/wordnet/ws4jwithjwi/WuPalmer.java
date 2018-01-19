package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWordID;

public class WuPalmer extends AbstractRelatednessCalculator {

	private static final Logger logger = LoggerFactory
			.getLogger(WuPalmer.class);

	protected DepthFinder depthFinder;
	protected PathFinder pathFinder;

	public WuPalmer(IDictionary dictionary) {
		super(dictionary);
		depthFinder = new DepthFinder(dictionary);
		pathFinder = new PathFinder(dictionary);
	}

	@Override
	public double calcRelatednessOfSynsets(ISynset synset1, ISynset synset2) {
		if (synset1 == null || synset2 == null) {
			logger.warn("Can not calculate similarity of two Synsets if one of them is null.");
			return getMinPossibleValue();
		}
		if (synset1.getOffset() == synset2.getOffset()) {
			return getMaxPossibleValue();
		}
		List<List<ISynset>> synset1Tree = pathFinder.getHypernymTrees(synset1);
		List<List<ISynset>> synset2Tree = pathFinder.getHypernymTrees(synset2);
		return calcRelatednessOfSynsets(synset1, synset1Tree, synset2,
				synset2Tree);
	}

	public double calcRelatednessOfSynsets(ISynset synset1,
			List<List<ISynset>> synset1Tree, ISynset synset2,
			List<List<ISynset>> synset2Tree) {

		DepestCommonSynset dcs = depthFinder.getDepestCommonSynset(synset1,
				synset1Tree, synset2, synset2Tree);

		double score = 0;
		score = (double) (2 * dcs.getDepth())
				/ (double) (dcs.getDepthOfSynset1() + dcs.getDepthOfSynset2());
		return score;
	}

	@Override
	public double getMaxPossibleValue() {
		return 1;
	}

	@Override
	public double getMinPossibleValue() {
		return 0;
	}

	protected ObjectObjectOpenHashMap<ISynset, List<List<ISynset>>> preprocessHypernymTrees(
			IIndexWord word) {
		Iterator<IWordID> wordIDsIterator = word.getWordIDs().iterator();
		ISynset currentSynset;
		ObjectObjectOpenHashMap<ISynset, List<List<ISynset>>> hypernymTrees = new ObjectObjectOpenHashMap<ISynset, List<List<ISynset>>>();
		while (wordIDsIterator.hasNext()) {
			currentSynset = dictionary.getWord(wordIDsIterator.next())
					.getSynset();
			hypernymTrees.put(currentSynset,
					pathFinder.getHypernymTrees(currentSynset));
		}
		return hypernymTrees;
	}

	public double calcRelatednessOfWords(IIndexWord word1, IIndexWord word2) {
		List<IWordID> idsWord1 = word1.getWordIDs();
		// work with a list of preprocessed hypernym trees for the second word
		// so they haven't to be recalculated
		ObjectObjectOpenHashMap<ISynset, List<List<ISynset>>> preprocessedTrees = preprocessHypernymTrees(word2);

		Iterator<IWordID> iteratorIDsWord1 = idsWord1.iterator();
		ISynset currentSynsetWord1;
		List<List<ISynset>> currentHypernymTreeWord1;
		Iterator<ObjectObjectCursor<ISynset, List<List<ISynset>>>> treeIterator;
		ObjectObjectCursor<ISynset, List<List<ISynset>>> currentSynsetTreePair;
		double currentRelatedness;
		double maxRelatedness = getMinPossibleValue();
		while (iteratorIDsWord1.hasNext()) {
			currentSynsetWord1 = dictionary.getWord(iteratorIDsWord1.next())
					.getSynset();
			currentHypernymTreeWord1 = pathFinder
					.getHypernymTrees(currentSynsetWord1);

			treeIterator = preprocessedTrees.iterator();
			while (treeIterator.hasNext()) {
				currentSynsetTreePair = treeIterator.next();
				currentRelatedness = calcRelatednessOfSynsets(
						currentSynsetWord1, currentHypernymTreeWord1,
						currentSynsetTreePair.key, currentSynsetTreePair.value);
				if (currentRelatedness > maxRelatedness) {
					maxRelatedness = currentRelatedness;
				}
			}
		}
		return maxRelatedness;
	}
}
