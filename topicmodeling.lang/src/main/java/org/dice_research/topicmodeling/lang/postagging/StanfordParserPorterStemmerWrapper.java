/**
 * This file is part of topicmodeling.lang.
 *
 * topicmodeling.lang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.lang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.lang.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.lang.postagging;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.PorterStemmerWrapper;
import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordParserPorterStemmerWrapper extends AbstractPosTagger {

    private static final Logger logger = LoggerFactory.getLogger(StanfordParserPorterStemmerWrapper.class);

    public static final String MAXENT_TAGGER_MODEL_FILE_PROPERTY_KEY = "org.dice_research.topicmodeling.lang.postagging.StanfordParserPorterStemmerWrapper.ModelFile";

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
