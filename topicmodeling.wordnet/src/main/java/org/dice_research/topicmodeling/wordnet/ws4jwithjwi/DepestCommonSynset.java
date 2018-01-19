package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.List;

import edu.mit.jwi.item.ISynset;

public class DepestCommonSynset {

	protected ISynset synset1;
	protected ISynset synset2;

	protected List<ISynset> pathSynset1;
	protected List<ISynset> pathSynset2;

	protected List<ISynset> ownPath;

	public DepestCommonSynset() {
	}

	public DepestCommonSynset(ISynset synset1, ISynset synset2) {
		this.synset1 = synset1;
		this.synset2 = synset2;
	}

	public ISynset getSynset1() {
		return synset1;
	}

	public void setSynset1(ISynset synset1) {
		this.synset1 = synset1;
	}

	public ISynset getSynset2() {
		return synset2;
	}

	public void setSynset2(ISynset synset2) {
		this.synset2 = synset2;
	}

	public List<ISynset> getPathSynset1() {
		return pathSynset1;
	}

	public void setPathSynset1(List<ISynset> pathSynset1) {
		this.pathSynset1 = pathSynset1;
	}

	public List<ISynset> getPathSynset2() {
		return pathSynset2;
	}

	public void setPathSynset2(List<ISynset> pathSynset2) {
		this.pathSynset2 = pathSynset2;
	}

	public List<ISynset> getOwnPath() {
		return ownPath;
	}

	public void setOwnPath(List<ISynset> ownPath) {
		this.ownPath = ownPath;
	}

	public int getDepth() {
		if (ownPath != null) {
			return ownPath.size();
		} else {
			return 0;
		}
	}

	public int getDepthOfSynset1() {
		return pathSynset1.size() + getDepth();
	}

	public int getDepthOfSynset2() {
		return pathSynset2.size() + getDepth();
	}

	public ISynset getCommonSynset() {
		if ((ownPath == null) || (ownPath.size() == 0)) {
			return null;
		} else {
			return ownPath.get(ownPath.size() - 1);
		}
	}
}
