package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.lang.Dictionary;
import org.dice_research.topicmodeling.lang.Language;

public interface LanguageDependentEvaluator extends Evaluator {

    public Language getLanguageForEvaluation();
    
    public Language getLanguageOfCorpus();
    
    public Dictionary getDictionary();
}
