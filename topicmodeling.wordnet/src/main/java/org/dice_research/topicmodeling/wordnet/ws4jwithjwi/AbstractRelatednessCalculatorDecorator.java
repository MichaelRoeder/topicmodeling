package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;

public abstract class AbstractRelatednessCalculatorDecorator implements RelatednessCalculator {

    protected RelatednessCalculator calculator;

    public AbstractRelatednessCalculatorDecorator(RelatednessCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public double calcRelatednessOfWords(String word1, POS word1PosTag, String word2, POS word2PosTag) {
        return refineRelatedness(calculator.calcRelatednessOfWords(word1, word1PosTag, word2, word2PosTag));
    }

    @Override
    public double calcRelatednessOfWords(IIndexWord word1, IIndexWord word2) {
        return refineRelatedness(calculator.calcRelatednessOfWords(word1, word2));
    }

    @Override
    public double calcRelatednessOfWords(IWord word1, IWord word2) {
        return refineRelatedness(calculator.calcRelatednessOfWords(word1, word2));
    }

    @Override
    public double calcRelatednessOfSynsets(ISynset synset1, ISynset synset2) {
        return refineRelatedness(calculator.calcRelatednessOfSynsets(synset1, synset2));
    }

    @Override
    public double getMaxPossibleValue() {
        return refineRelatedness(calculator.getMaxPossibleValue());
    }

    @Override
    public double getMinPossibleValue() {
        return refineRelatedness(calculator.getMinPossibleValue());
    }

    protected abstract double refineRelatedness(double relatedness);
}
