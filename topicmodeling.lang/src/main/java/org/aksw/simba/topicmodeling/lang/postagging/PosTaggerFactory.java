package org.aksw.simba.topicmodeling.lang.postagging;

import org.aksw.simba.topicmodeling.lang.Language;

public class PosTaggerFactory {

    public static PosTagger getPosTaggingStep(Language lang) {
        PosTagger postagger = null;
        switch (lang) {
        case ENG: {
            // postagger = new StanfordParserPorterStemmerWrapper();
            postagger = MorphadornerWrapper.getWrapper();
            break;
        }
        case GER: {
            // postagger = new TermTokenizerWrapper();
            postagger = TreeTaggerWrapper.createTreeTaggerWrapper();
            break;
        }
        default: /* nothing to do */
        }
        return postagger;
    }

    public static PosTagger getPosTaggingStep(Language lang,
            PosTaggingTermFilter filter) {
        PosTagger postagger = getPosTaggingStep(lang);
        postagger.setFilter(filter);
        return postagger;
    }

}
