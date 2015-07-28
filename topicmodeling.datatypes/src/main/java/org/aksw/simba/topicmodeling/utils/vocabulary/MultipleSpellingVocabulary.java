package org.aksw.simba.topicmodeling.utils.vocabulary;

import java.util.Set;


public interface MultipleSpellingVocabulary extends Vocabulary {

    public Set<String> getSpellingsForWord(int wordId);

    public void addSpelling(int wordId, String spelling);
}
