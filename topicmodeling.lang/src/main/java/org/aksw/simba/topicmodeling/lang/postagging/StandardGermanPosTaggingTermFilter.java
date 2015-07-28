package org.aksw.simba.topicmodeling.lang.postagging;

public class StandardGermanPosTaggingTermFilter extends AbstractStandardPosTaggingTermFilter implements
        PosTaggingTermFilter {

    public static final String STOPWORD_FILE = "german.stopwords";

    private static StandardGermanPosTaggingTermFilter instance = null;

    public static StandardGermanPosTaggingTermFilter getInstance() {
        if (instance == null) {
            instance = readStopWords(new StandardGermanPosTaggingTermFilter(), STOPWORD_FILE);
        }
        return instance;
    }

    protected StandardGermanPosTaggingTermFilter() {
    }
}
