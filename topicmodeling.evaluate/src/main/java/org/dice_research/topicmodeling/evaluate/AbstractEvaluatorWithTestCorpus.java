package org.dice_research.topicmodeling.evaluate;

import org.dice_research.topicmodeling.utils.corpus.Corpus;

public abstract class AbstractEvaluatorWithTestCorpus<T extends Corpus>  extends AbstractEvaluator {

    protected T testCorpus;

    public AbstractEvaluatorWithTestCorpus(T testCorpus) {
        this.testCorpus = testCorpus;
    }

    /**
     * Set the value of testCorpus
     * 
     * @param newVar
     *            the new value of testCorpus
     */
    public void setTestCorpus(T testCorpus) {
        this.testCorpus = testCorpus;
    }

    /**
     * Get the value of testCorpus
     * 
     * @return the value of testCorpus
     */
    public T getTestCorpus() {
        return testCorpus;
    }
}
