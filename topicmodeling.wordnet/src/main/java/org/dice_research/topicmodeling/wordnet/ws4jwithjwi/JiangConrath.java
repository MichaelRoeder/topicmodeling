package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.Pointer;

public class JiangConrath extends AbstractRelatednessCalculator {

    private static final Logger logger = LoggerFactory
            .getLogger(Lin.class);

    private static final double ALPHA = 0.5;
    private static final double BETA = 0.3;
    private static final double AVERAGE_DENSITY = 2.779056425772784;
    private static final int CHILDS_OF_ROOT = 22337;

    private DepthFinder depthFinder;
    private PathFinder pathFinder;

    public JiangConrath(IDictionary dictionary) {
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

        double distance = 0;
        int parentDepth;
        double icNode, icParent;
        ISynset parent = dcs.getCommonSynset();
        if (parent == null) {
            return getMinPossibleValue();
        }
        parentDepth = dcs.getDepth();
        // icParent = parent != null ? icTree.getInformationContent(parent.getID()) : 0;
        icParent = icTree.getInformationContent(parent.getID());
        for (ISynset node : dcs.getPathSynset1()) {
            icNode = icTree.getInformationContent(node.getID());
            distance += calcPathWeight(parent, parentDepth, icNode, icParent);
            parent = node;
            icParent = icNode;
            ++parentDepth;
        }

        parent = dcs.getCommonSynset();
        parentDepth = dcs.getDepthOfSynset2();
        // icParent = parent != null ? icTree.getInformationContent(parent.getID()) : 0;
        icParent = icTree.getInformationContent(parent.getID());
        for (ISynset node : dcs.getPathSynset2()) {
            icNode = icTree.getInformationContent(node.getID());
            distance += calcPathWeight(parent, parentDepth, icNode, icParent);
            parent = node;
            icParent = icNode;
            ++parentDepth;
        }
        return -distance;
    }

    private double calcPathWeight(ISynset parent, int parentDepth, double icNode, double icParent) {
//        if (parent == null) {
//            return calcPathWeightToRoot(icNode);
//        } else {
            int localDensity = parent.getRelatedSynsets(Pointer.HYPONYM).size();
            localDensity += parent.getRelatedSynsets(Pointer.HYPONYM_INSTANCE).size();
            double weight = (BETA + (1 - BETA) * (AVERAGE_DENSITY / localDensity));
            weight *= Math.pow((parentDepth + 1) / parentDepth, ALPHA);
            weight *= icNode - icParent;
            return weight;
//        }
    }

//    private double calcPathWeightToRoot(double icNode) {
//        double weight = (BETA + (BETA - 1) * (AVERAGE_DENSITY / CHILDS_OF_ROOT));
//        weight *= icNode;
//        return weight;
//    }

    @Override
    public double getMaxPossibleValue() {
        return 0;
    }

    @Override
    public double getMinPossibleValue() {
        double distance = 0;
        distance = (BETA + (1 - BETA) * (AVERAGE_DENSITY / 3));
        distance *= Math.pow(2, ALPHA);
        distance *= InformationContentTree.getInformationContentTree().getMaxInformationContent();
        distance *= 2;
        return -distance;
    }
}
