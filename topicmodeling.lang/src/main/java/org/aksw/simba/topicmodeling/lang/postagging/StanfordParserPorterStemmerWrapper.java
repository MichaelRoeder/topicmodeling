package org.aksw.simba.topicmodeling.lang.postagging;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.apache.lucene.analysis.PorterStemmerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordParserPorterStemmerWrapper extends AbstractPosTagger {

    private static final Logger logger = LoggerFactory.getLogger(StanfordParserPorterStemmerWrapper.class);

    public static final String MAXENT_TAGGER_MODEL_FILE_PROPERTY_KEY = "com.unister.semweb.topicmodeling.lang.postagging.StanfordParserPorterStemmerWrapper.ModelFile";

    private MaxentTagger tagger;
    private PorterStemmerWrapper stemmer = new PorterStemmerWrapper();

    public static StanfordParserPorterStemmerWrapper createStanfordParserPorterStemmerWrapper() {
        String modelFile = System.getProperty(MAXENT_TAGGER_MODEL_FILE_PROPERTY_KEY);
        if (modelFile == null) {
            logger.error("Couldn't load maxent model file property (" + MAXENT_TAGGER_MODEL_FILE_PROPERTY_KEY
                    + "). Returning null.");
        }
        try {
            MaxentTagger tagger = new MaxentTagger(modelFile);
            return new StanfordParserPorterStemmerWrapper(tagger);
        } catch (Exception e) {
            logger.error("Couldn't create tagger. Returning null.", e);
            return null;
        }
    }

    private StanfordParserPorterStemmerWrapper(MaxentTagger tagger) {
        this.tagger = tagger;
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        return tokenizeText(text, null);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        TermTokenizedText termTokenizedText = new TermTokenizedText();
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(text));
        ArrayList<TaggedWord> taggedSentence;
        for (List<HasWord> sentence : sentences) {
            taggedSentence = tagger.tagSentence(sentence);
            addSentence(termTokenizedText, taggedSentence, filter);
        }
        return termTokenizedText;
    }

    private void addSentence(TermTokenizedText termTokenizedText, ArrayList<TaggedWord> taggedSentence,
            PosTaggingTermFilter filter) {
        Term term;
        for (TaggedWord taggedWord : taggedSentence) {
            term = createTerm(taggedWord);
            if ((filter == null) || (filter.isTermGood(term))) {
                termTokenizedText.addTerm(term);
            }
        }
    }

    private Term createTerm(TaggedWord taggedWord) {
        Term term = new Term(taggedWord.word(), stemWord(taggedWord.word()));
        term.setPosTag(taggedWord.tag());

        if (taggedWord.tag().contains("NN")) {
            term.properties.setNoun(true);
        } else if (taggedWord.tag().contains("VB")) {
            term.properties.setVerb(true);
        } else if (taggedWord.tag().contains("CD")) {
            term.properties.setNumber(true);
        } else if (taggedWord.tag().contains("JJ")) {
            term.properties.setAdjective(true);
        }

        return term;
    }

    private String stemWord(String word) {
        String stem = stemmer.getStemOrWordItself(word);
        return stem;
    }

}
