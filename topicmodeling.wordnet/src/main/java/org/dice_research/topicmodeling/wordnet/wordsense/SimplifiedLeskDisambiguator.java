package org.dice_research.topicmodeling.wordnet.wordsense;

import java.util.HashSet;
import java.util.Set;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.lang.postagging.PosTagger;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class SimplifiedLeskDisambiguator implements WordSenseDisambiguator {

    private IDictionary dictionary;
    private PosTagger postagger;

    public SimplifiedLeskDisambiguator(IDictionary dictionary, PosTagger postagger) {
        this.dictionary = dictionary;
        this.postagger = postagger;
    }

    @Override
    public ISynset getSynsetForWord(String word, String text) {
        IIndexWord idxWord = null;
        try {
            idxWord = dictionary.getIndexWord(word, POS.NOUN);
        } catch (IllegalArgumentException e) {
            idxWord = null;
        }
        if (idxWord == null) {
            return null;
        } else {
            return getSynsetForWord(idxWord, text);
        }
    }

    public TermTokenizedText preprocessText(String text) {
        return postagger.tokenize(text);
    }

    public ISynset getSynsetForWord(IIndexWord idxWord, String text) {
        return getSynsetForWord(idxWord, preprocessText(text));
    }

    public ISynset getSynsetForWord(IIndexWord idxWord, TermTokenizedText text) {
        ISynset chosenSynset = null;
        ISynset currentSynset;
        int maxSynsetScore = 0;
        int synsetScore;
        for (IWordID wordID : idxWord.getWordIDs()) {
            currentSynset = dictionary.getWord(wordID).getSynset();
            synsetScore = calculateSynsetScore(currentSynset, text);
            if (synsetScore > maxSynsetScore) {
                chosenSynset = currentSynset;
                maxSynsetScore = synsetScore;
            }
        }
        return chosenSynset;
    }

    private int calculateSynsetScore(ISynset synset, TermTokenizedText text) {
        TermTokenizedText descriptionText = preprocessText(synset.getGloss());
        Set<Term> descriptionTerms = getDifferentTerms(descriptionText);
        Set<Term> textTerms = getDifferentTerms(text);
        int score = 0;
        for (Term term : descriptionTerms) {
            if (textTerms.contains(term)) {
                ++score;
            }
        }
        return score;
    }

    private Set<Term> getDifferentTerms(TermTokenizedText text) {
        return new HashSet<Term>(text.getTermTokenizedText());
    }
}
