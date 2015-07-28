package org.aksw.simba.topicmodeling.utils.vocabulary;

import java.io.Serializable;
import java.util.Iterator;

public interface Vocabulary extends Serializable, Iterable<String> {

    public static final int WORD_NOT_FOUND = -1;

    public int size();

    public void add(String word);

    /**
     * 
     * @param word
     * @return id or -1
     */
    public Integer getId(String word);

    public Iterator<String> iterator();

    public String getWord(int wordId);

    public void setWord(String word, String newWord);
}
