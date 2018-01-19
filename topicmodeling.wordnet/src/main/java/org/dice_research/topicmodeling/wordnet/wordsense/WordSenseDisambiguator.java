package org.dice_research.topicmodeling.wordnet.wordsense;

import edu.mit.jwi.item.ISynset;

public interface WordSenseDisambiguator {
    public ISynset getSynsetForWord(String word, String text);
}
