package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.Pointer;

public class PathFinder {

	private static final boolean useInstanceHypernyms = true;

	private static final Logger logger = LoggerFactory
			.getLogger(PathFinder.class);

	protected IDictionary dictionary;

	public PathFinder(IDictionary dictionary) {
		this.dictionary = dictionary;
	}

	public List<List<ISynset>> getHypernymTrees(ISynset synset) {
		return getHypernymTrees(synset, new HashSet<ISynsetID>());
	}

	/**
	 * Subroutine that returns an array of hypernym trees for the given synset.
	 * In most cases there is exactly 1 hypernym for every synset.
	 * 
	 * portation of the method of the ws4j library
	 */
	@SuppressWarnings("unused")
	public List<List<ISynset>> getHypernymTrees(ISynset synset,
			Set<ISynsetID> history) {

		// get the hypernyms
		List<ISynsetID> hypernymIds = synset
				.getRelatedSynsets(Pointer.HYPERNYM);
		// get the hypernyms (if this is an instance)
		List<ISynsetID> instanceHypernymIds = useInstanceHypernyms ? synset
				.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE) : null;
		List<List<ISynset>> result = new ArrayList<List<ISynset>>();

		// If this is the highest node and has no other hypernyms
		if ((hypernymIds.size() == 0)
				&& (useInstanceHypernyms || (instanceHypernymIds.size() == 0))) {
			// return the tree containing only the current node
			List<ISynset> tree = new ArrayList<ISynset>();
			tree.add(synset);
			result.add(tree);
		} else {
			// for all (direct) hypernyms of this synset
			for (ISynsetID hypernymId : hypernymIds) {
				// if (!history.contains(hypernymId)) {
				// history.add(hypernymId);

				List<List<ISynset>> hypernymTrees = getHypernymTrees(
						dictionary.getSynset(hypernymId), history);
				// add the current Tree and
				for (List<ISynset> hypernymTree : hypernymTrees) {
					hypernymTree.add(synset);
					result.add(hypernymTree);
				}
				// } else {
				// // TODO find hypernym and add the tree to the current tree
				// }
			}
			for (ISynsetID hypernymId : instanceHypernymIds) {
				List<List<ISynset>> hypernymTrees = getHypernymTrees(
						dictionary.getSynset(hypernymId), history);
				// add the current Tree and
				for (List<ISynset> hypernymTree : hypernymTrees) {
					hypernymTree.add(synset);
					result.add(hypernymTree);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the path of hypernyms to the (virtual) root for the given synset.
	 * 
	 * @param synset
	 *            the synset for which the path to the root should be calculated
	 * @return the row of hypernyms to the (virtual) root.
	 */
	@Deprecated
	public List<ISynset> getHypernymPath(ISynset synset) {
		List<ISynset> pathToRoot = new ArrayList<ISynset>(20);
		List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
		ISynset hypernym = null;
		while (hypernyms.size() > 0) {
			if (hypernyms.size() > 1) {
				String warnMsg = "Found a Synset(";
				List<IWord> words = hypernym == null ? synset.getWords()
						: hypernym.getWords();
				for (int i = 0; i < words.size(); i++) {
					warnMsg += i == 0 ? words.get(i).getLemma() : ","
							+ words.get(i).getLemma();
				}
				warnMsg += ") with more than 1 Hypernym! The current implementation does not regard the second hypernym!";
				logger.warn(warnMsg);
			}
			hypernym = dictionary.getSynset(hypernyms.get(0));
			pathToRoot.add(hypernym);
			hypernyms = hypernym.getRelatedSynsets(Pointer.HYPERNYM);
		}
		return pathToRoot;
	}
}
