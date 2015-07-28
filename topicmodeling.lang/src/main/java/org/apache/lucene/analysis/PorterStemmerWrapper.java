package org.apache.lucene.analysis;

import org.tartarus.snowball.ext.EnglishStemmer;

public class PorterStemmerWrapper {
    /**
     * The English stemmer is the updated version of the original PorterStemmer.
     */
    private EnglishStemmer stemmer = new EnglishStemmer();

    public String getStem(String word) {
        String stem = null;
        stemmer.setCurrent(word);
        if (stemmer.stem()) {
            stem = new String(stemmer.getCurrentBuffer(), 0, stemmer.getCurrentBufferLength());
        }
        return stem;
    }

    public String getStemOrWordItself(String word) {
        String stem = getStem(word);
        if (stem == null) {
            return word;
        } else {
            return stem;
        }
    }
}
