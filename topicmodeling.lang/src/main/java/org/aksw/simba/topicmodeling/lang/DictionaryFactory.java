package org.aksw.simba.topicmodeling.lang;

import org.aksw.simba.topicmodeling.lang.Language;
import org.apache.commons.lang.NotImplementedException;

public class DictionaryFactory {

    public static Dictionary getDictionary(Language fromLanguage, Language toLanguage) {
        switch (fromLanguage) {
        case GER: {
            switch (toLanguage) {
            case GER:
                return new NoTranslationDictionary(fromLanguage);
            case ENG:
                return DictionaryDictCC.getInstance();
            default:
                break;
            }
        }
        case ENG: {
            switch (toLanguage) {
            case GER:
                break;
            case ENG:
                return new NoTranslationDictionary(fromLanguage);
            default:
                break;
            }
        }
        default:
            break;
        }
        throw new NotImplementedException("There is no dictionary for translations from " + fromLanguage.toString()
                + " to " + toLanguage.toString());
    }
}
