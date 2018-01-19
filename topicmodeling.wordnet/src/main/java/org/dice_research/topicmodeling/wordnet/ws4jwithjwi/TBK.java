package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;

public class TBK extends WuPalmer {

    public TBK(IDictionary dictionary) {
        super(dictionary);
    }

    @Override
    public double calcRelatednessOfSynsets(ISynset synset1,
            List<List<ISynset>> synset1Tree, ISynset synset2,
            List<List<ISynset>> synset2Tree) {

        DepestCommonSynset dcs = depthFinder.getDepestCommonSynset(synset1,
                synset1Tree, synset2, synset2Tree);

        double score = 0;
        score = (double) (2 * dcs.getDepth())
                / (double) (dcs.getDepthOfSynset1() + dcs.getDepthOfSynset2());
        // if (synset1.equals(dcs.getCommonSynset()) || synset2.equals(dcs.getCommonSynset())) {
        // score /= (double) Math.abs(dcs.getDepthOfSynset1() - dcs.getDepthOfSynset2()) + 1.0;
        // } else {
        // score /= dcs.getDepthOfSynset1() + dcs.getDepthOfSynset2() - (2 * dcs.getDepth());
        // }

        if (!synset1.equals(dcs.getCommonSynset()) && !synset2.equals(dcs.getCommonSynset())) {
            score /= (double) Math.abs(dcs.getDepthOfSynset1() - dcs.getDepthOfSynset2()) + 1.0;
        }

        return score;
    }
}
