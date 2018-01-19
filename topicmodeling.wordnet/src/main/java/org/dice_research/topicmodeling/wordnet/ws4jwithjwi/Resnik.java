package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;

public class Resnik extends AbstractRelatednessCalculator {

    private static final Logger logger = LoggerFactory
            .getLogger(Lin.class);

    private DepthFinder depthFinder;
    private PathFinder pathFinder;

    public Resnik(IDictionary dictionary) {
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
        InformationContentTree icTree = InformationContentTree.getInformationContentTree();
        double icDcs = 0;
        if (dcs.getCommonSynset() != null) {
            icDcs = icTree.getInformationContent(dcs.getCommonSynset().getID());
        }
        return icDcs;
    }

    @Override
    public double getMaxPossibleValue() {
        return 1;
    }

    @Override
    public double getMinPossibleValue() {
        return 0;
    }
}
