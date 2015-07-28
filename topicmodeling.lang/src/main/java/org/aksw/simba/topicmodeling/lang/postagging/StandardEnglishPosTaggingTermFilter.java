package org.aksw.simba.topicmodeling.lang.postagging;

public class StandardEnglishPosTaggingTermFilter extends AbstractStandardPosTaggingTermFilter implements
        PosTaggingTermFilter {

    public static final String STOPWORD_FILE = "english.stopwords";

    private static StandardEnglishPosTaggingTermFilter instance = null;

    public synchronized static StandardEnglishPosTaggingTermFilter getInstance() {
        if (instance == null) {
            instance = readStopWords(new StandardEnglishPosTaggingTermFilter(), STOPWORD_FILE);
        }
        return instance;
    }

    protected StandardEnglishPosTaggingTermFilter() {
    }
}
