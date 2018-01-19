package org.dice_research.topicmodeling.wordnet.ws4jwithjwi;

import edu.mit.jwi.IDictionary;

public class NormalizedJiangConrath extends AbstractRelatednessCalculatorDecorator {

    private double maxPossibleDistance = -1;

    public NormalizedJiangConrath(IDictionary dictionary) {
        super(new JiangConrath(dictionary));
    }

    @Override
    protected double refineRelatedness(double relatedness) {
        if (maxPossibleDistance < 0) {
            maxPossibleDistance = -super.calculator.getMinPossibleValue();
        }
        return 1.0 + (relatedness / maxPossibleDistance);
    }

    @Override
    public double getMinPossibleValue() {
        return 0;
    }

    @Override
    public double getMaxPossibleValue() {
        return 1;
    }
}
