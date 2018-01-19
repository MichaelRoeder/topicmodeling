package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;

public class DepthFinder {

	protected IDictionary dictionary;
	protected PathFinder pathFinder;

	public DepthFinder(IDictionary dictionary) {
		this.dictionary = dictionary;
		this.pathFinder = new PathFinder(dictionary);
	}

	@Deprecated
	public List<ISynset> getSynsetDepth(ISynset synset) {
		return pathFinder.getHypernymPath(synset);
	}

	public DepestCommonSynset getDepestCommonSynset(ISynset synset1,
			ISynset synset2) {
		List<List<ISynset>> synset1Tree = pathFinder.getHypernymTrees(synset1);
		List<List<ISynset>> synset2Tree = pathFinder.getHypernymTrees(synset2);
		return getDepestCommonSynset(synset1, synset1Tree, synset2, synset2Tree);
	}

	public DepestCommonSynset getDepestCommonSynset(ISynset synset1,
			List<List<ISynset>> synset1Tree, ISynset synset2,
			List<List<ISynset>> synset2Tree) {

		DepestCommonSynset dcs = new DepestCommonSynset(synset1, synset2);
		int path1Pos, path2Pos;
		for (List<ISynset> synset1HypernymPath : synset1Tree) {
			for (List<ISynset> synset2HypernymPath : synset2Tree) {
				path1Pos = 0;
				path2Pos = 0;
				// search the depest common synset of these two paths
				while ((path1Pos < synset1HypernymPath.size())
						&& (path2Pos < synset2HypernymPath.size())
						&& (synset1HypernymPath.get(path1Pos).getOffset() == synset2HypernymPath
								.get(path2Pos).getOffset())) {
					++path1Pos;
					++path2Pos;
				}
				// if the found common synset is deeper than the current dcs
				if (path1Pos >= dcs.getDepth()) {
					dcs.setOwnPath(synset1HypernymPath.subList(0, path1Pos));
					dcs.setPathSynset1(synset1HypernymPath.subList(path1Pos,
							synset1HypernymPath.size()));
					dcs.setPathSynset2(synset2HypernymPath.subList(path2Pos,
							synset2HypernymPath.size()));
				}
			}
		}
		return dcs;
	}
}
