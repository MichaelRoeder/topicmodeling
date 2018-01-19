package org.dice_research.topicmodeling.wordnet;

import org.apache.commons.lang3.NotImplementedException;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.lang.Dictionary;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.AbstractRelatednessCalculatorDecorator;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.InformationContentTree;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.JiangConrath;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.Lin;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.NormalizedJiangConrath;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.RelatednessCalculator;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.Resnik;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.TBK;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.WordNetManager;
import org.dice_research.topicmodeling.wordnet.ws4jwithjwi.WuPalmer;

import edu.mit.jwi.IDictionary;

public class RelatednessCalculatorFactory {

    public static RelatednessCalculator createRelatednessCalculator(WordNetRelatednessCalculationMethods method,
            ProbTopicModelingAlgorithmStateSupplier stateSupplier, Dictionary dictionary) {

        switch (method) {
        case WU_PALMER:
            return new WuPalmer(WordNetManager.getInstance().getDictionary());
        case WU_PALMER_POW_10:
            return new AbstractRelatednessCalculatorDecorator(
                    new WuPalmer(WordNetManager.getInstance().getDictionary())) {
                @Override
                protected double refineRelatedness(double relatedness) {
                    return Math.pow(relatedness, 10);
                }
            };
        case TBK:
            return new TBK(WordNetManager.getInstance().getDictionary());
        case RESNIK: {
            IDictionary wordNetDictionary = WordNetManager.getInstance().getDictionary();
            InformationContentTree.createInformationContentTree(stateSupplier, dictionary, wordNetDictionary);
            return new Resnik(wordNetDictionary);
        }
        case JIANG_CONRATH: {
            IDictionary wordNetDictionary = WordNetManager.getInstance().getDictionary();
            InformationContentTree.createInformationContentTree(stateSupplier, dictionary, wordNetDictionary);
            return new JiangConrath(wordNetDictionary);
        }
        case NORMALIZED_JIANG_CONRATH: {
            IDictionary wordNetDictionary = WordNetManager.getInstance().getDictionary();
            InformationContentTree.createInformationContentTree(stateSupplier, dictionary, wordNetDictionary);
            return new NormalizedJiangConrath(wordNetDictionary);
        }
        case LIN: {
            IDictionary wordNetDictionary = WordNetManager.getInstance().getDictionary();
            InformationContentTree.createInformationContentTree(stateSupplier, dictionary, wordNetDictionary);
            return new Lin(wordNetDictionary);
        }
        case HIRST_ST_ONGE:
            // return new HirstStOnge(lexicalDB);
        case LEACOCK_CHODOROW:
            // return new LeacockChodorow(lexicalDB);
        case LESK_BANERJEE_PEDERSEN:
            // return new Lesk(lexicalDB);
        case PATH:
            // return new Path(lexicalDB);
        default:
            // all not implemented and not defined methods should fall through to throw this exception
            throw new NotImplementedException("The requested method " + method.name()
                    + " is not implemented. Choose another one.");
        }
    }
}
