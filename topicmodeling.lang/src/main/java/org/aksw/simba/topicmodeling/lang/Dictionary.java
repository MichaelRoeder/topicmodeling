package org.aksw.simba.topicmodeling.lang;

import org.aksw.simba.topicmodeling.lang.Language;

public interface Dictionary {

    public String translate(String word);

    public void saveAsObjectFile();

    public Language getSourceLanguage();

    public Language getDestinationLanguage();
}
