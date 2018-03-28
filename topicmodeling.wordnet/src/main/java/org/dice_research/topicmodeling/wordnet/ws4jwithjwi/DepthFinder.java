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

    public DepestCommonSynset getDepestCommonSynset(ISynset synset1, ISynset synset2) {
        List<List<ISynset>> synset1Tree = pathFinder.getHypernymTrees(synset1);
        List<List<ISynset>> synset2Tree = pathFinder.getHypernymTrees(synset2);
        return getDepestCommonSynset(synset1, synset1Tree, synset2, synset2Tree);
    }

    /**
     * Returns the deepest common synset of the two given synsets. If the synsets
     * have multiple paths to the node, the DCS is chosen 1) as the node with the
     * largest distance to the root and 2) with the shortest distance to the two
     * synsets (defined as the sum of the two distances (synset1,dcs) and
     * (synset2,dcs)).
     * 
     * @param synset1
     * @param synset1Tree
     * @param synset2
     * @param synset2Tree
     * @return
     */
    public DepestCommonSynset getDepestCommonSynset(ISynset synset1, List<List<ISynset>> synset1Tree, ISynset synset2,
            List<List<ISynset>> synset2Tree) {

        DepestCommonSynset dcs = new DepestCommonSynset(synset1, synset2);
        int path1Pos, path2Pos;
        for (List<ISynset> synset1HypernymPath : synset1Tree) {
            for (List<ISynset> synset2HypernymPath : synset2Tree) {
                path1Pos = 0;
                path2Pos = 0;
                // search the depest common synset of these two paths
                while ((path1Pos < synset1HypernymPath.size()) && (path2Pos < synset2HypernymPath.size())
                        && (synset1HypernymPath.get(path1Pos).getOffset() == synset2HypernymPath.get(path2Pos)
                                .getOffset())) {
                    ++path1Pos;
                    ++path2Pos;
                }
                // if 0) there is no DCS available until now, 1) the new common synset is deeper
                // than our current DCS or 2) the new common synset has the same depth but a
                // smaller distance between the two synsets
                if ((dcs.getOwnPath() == null) || (path1Pos >= dcs.getDepth())
                        || ((path1Pos == dcs.getDepth()) && (((synset1HypernymPath.size() + synset2HypernymPath.size())
                                - 2 * path1Pos) < (dcs.pathSynset1.size() + dcs.pathSynset2.size())))) {
                    // we have found a new DCS
                    dcs.setOwnPath(synset1HypernymPath.subList(0, path1Pos));
                    dcs.setPathSynset1(synset1HypernymPath.subList(path1Pos, synset1HypernymPath.size()));
                    dcs.setPathSynset2(synset2HypernymPath.subList(path2Pos, synset2HypernymPath.size()));
                }
            }
        }
        return dcs;
    }
}
