package org.aksw.simba.topicmodeling.lang;

import org.aksw.simba.topicmodeling.lang.Language;

public class NoTranslationDictionary implements Dictionary {

    private Language language;

    public NoTranslationDictionary(Language language) {
    }

    @Override
    public String translate(String word) {
        return word;
    }

    @Override
    public void saveAsObjectFile() {
        /* nothing to do */
    }

    @Override
    public Language getSourceLanguage() {
        return language;
    }

    @Override
    public Language getDestinationLanguage() {
        return language;
    }

}
