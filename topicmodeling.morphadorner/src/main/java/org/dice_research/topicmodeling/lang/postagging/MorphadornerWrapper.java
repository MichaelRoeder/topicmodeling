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

import java.io.IOException;
import java.util.List;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.lang.TermProperties;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import edu.northwestern.at.utils.corpuslinguistics.adornedword.AdornedWord;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.DefaultLemmatizer;
import edu.northwestern.at.utils.corpuslinguistics.lemmatizer.Lemmatizer;
import edu.northwestern.at.utils.corpuslinguistics.namerecognizer.Names;
import edu.northwestern.at.utils.corpuslinguistics.partsofspeech.NUPOSPartOfSpeechTags;
import edu.northwestern.at.utils.corpuslinguistics.partsofspeech.PartOfSpeechTags;
import edu.northwestern.at.utils.corpuslinguistics.postagger.DefaultPartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.postagger.PartOfSpeechTagger;
import edu.northwestern.at.utils.corpuslinguistics.sentencesplitter.DefaultSentenceSplitter;
import edu.northwestern.at.utils.corpuslinguistics.sentencesplitter.SentenceSplitter;
import edu.northwestern.at.utils.corpuslinguistics.spellingstandardizer.ExtendedSearchSpellingStandardizer;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.DefaultWordTokenizer;
import edu.northwestern.at.utils.corpuslinguistics.tokenizer.WordTokenizer;

public class MorphadornerWrapper extends AbstractPosTagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(MorphadornerWrapper.class);

    private static final String NOUN_POS_TAG_PREFIX = "n";
    private static final String ADVERB_NOUN_POS_TAG = "an";
    private static final String ADJECTIVE_POS_TAG_PREFIX = "j";
    private static final String VERB_POS_TAG_PREFIX = "v";
    private static final String NUMBER_POS_TAG_PREFIX = "crd";

    private static final boolean DEFAULT_TERM_CAN_GET_MULTPLE_WORD_CLASSES = true;

    private final SentenceSplitter sentenceSplitter = new DefaultSentenceSplitter();
    private final WordTokenizer wordTokenizer = new DefaultWordTokenizer();
    private Lemmatizer lemmatizer;
    // private Lexicon lexicon;
    private PartOfSpeechTags posTags;
    private PartOfSpeechTagger partOfSpeechTagger;
    private boolean termCanGetMultipeWordClasses;

    private static MorphadornerWrapper instance;

    public static MorphadornerWrapper getWrapper() {
        if (instance == null) {
            instance = create();
        }
        return instance;
    }

    private static MorphadornerWrapper create() {
        return create(DEFAULT_TERM_CAN_GET_MULTPLE_WORD_CLASSES);
    }

    private static MorphadornerWrapper create(boolean termCanGetMultipeWordClasses) {
        MorphadornerWrapper wrapper = new MorphadornerWrapper(termCanGetMultipeWordClasses);
        return loadPartsOfWrapper(wrapper);
    }

    private static MorphadornerWrapper loadPartsOfWrapper(MorphadornerWrapper wrapper) {
        try {
            wrapper.setPartOfSpeechTagger(new DefaultPartOfSpeechTagger());
        } catch (Exception e) {
            LOGGER.error("Couldn't create part of speech tagger. Returning null.", e);
            return null;
        }
        try {
            wrapper.setPosTags(new NUPOSPartOfSpeechTags());
        } catch (IOException e) {
            LOGGER.error("Couldn't load the part of speech tag set. Returning null.", e);
            return null;
        }
        Lemmatizer lemmatizer;
        try {
            lemmatizer = new DefaultLemmatizer();
        } catch (Exception e) {
            LOGGER.error("Couldn't load the part of speech tag set. Returning null.", e);
            return null;
        }
        try {
            ExtendedSearchSpellingStandardizer standardizer = new ExtendedSearchSpellingStandardizer();
            standardizer.loadStandardSpellings(
                    standardizer.getClass().getClassLoader().getResource("standardspellings.txt"), "utf-8");
            Names names = new Names();
            standardizer.addStandardSpellings(names.getFirstNames());
            standardizer.addStandardSpellings(names.getSurnames());
            standardizer.addStandardSpellings(names.getPlaceNames().keySet());
            // Load alternate/standard spelling pairs.
            standardizer.loadAlternativeSpellings(
                    standardizer.getClass().getClassLoader().getResource("ncfmergedspellingpairs.tab"), "utf-8", "\t");
            lemmatizer.setDictionary(standardizer.getStandardSpellings());
        } catch (Exception e) {
            LOGGER.error("Couldn't load the dictionary of the lemmatizer. Returning null.", e);
        }
        wrapper.setLemmatizer(lemmatizer);
        return wrapper;
    }

    public static void ResetInstance() {
        LOGGER.warn("I will reset the current instance of the MorphadornerWrapper. This is not recommended and should only be done if you are expecting large memory problems.");
        instance = loadPartsOfWrapper(instance);
        if (instance == null) {
            LOGGER.error("The reloading hasn't worked as expected. The wrapper won't work anymore.");
        }
    }

    private MorphadornerWrapper(boolean termCanGetMultipeWordClasses) {
        this.termCanGetMultipeWordClasses = termCanGetMultipeWordClasses;
        try {
            partOfSpeechTagger = new DefaultPartOfSpeechTagger();
        } catch (Exception e) {
            LOGGER.error("Couldn't create part of speech tagger. This wrapper won't work as expected.", e);
            partOfSpeechTagger = null;
        }
        try {
            posTags = new NUPOSPartOfSpeechTags();
        } catch (IOException e) {
            LOGGER.error("Couldn't load the part of speech tag set. This wrapper won't work as expected.", e);
            posTags = null;
        }
        try {
            lemmatizer = new DefaultLemmatizer();
        } catch (Exception e) {
            LOGGER.error("Couldn't load the part of speech tag set. This wrapper won't work as expected.", e);
            lemmatizer = null;
        }
        try {
            if (lemmatizer != null) {
                ExtendedSearchSpellingStandardizer standardizer = new ExtendedSearchSpellingStandardizer();
                standardizer.loadStandardSpellings(
                        standardizer.getClass().getClassLoader().getResource("standardspellings.txt"), "utf-8");
                Names names = new Names();
                standardizer.addStandardSpellings(names.getFirstNames());
                standardizer.addStandardSpellings(names.getSurnames());
                standardizer.addStandardSpellings(names.getPlaceNames().keySet());
                // Load alternate/standard spelling pairs.
                standardizer.loadAlternativeSpellings(
                        standardizer.getClass().getClassLoader().getResource("ncfmergedspellingpairs.tab"), "utf-8",
                        "\t");
                lemmatizer.setDictionary(standardizer.getStandardSpellings());
            }
        } catch (Exception e) {
            LOGGER.error("Couldn't load the dictionary of the lemmatizer. This wrapper won't work as expected.", e);
        }
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        List<List<AdornedWord>> taggedSentences = getTaggedSentences(text);
        lemmatize(taggedSentences);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        for (List<AdornedWord> sentence : taggedSentences) {
            for (AdornedWord word : sentence) {
                terms.add(transformToTerm(word));
            }
        }
        return ttText;
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        List<List<AdornedWord>> taggedSentences = getTaggedSentences(text);
        lemmatize(taggedSentences);
        TermTokenizedText ttText = new TermTokenizedText();
        List<Term> terms = ttText.getTermTokenizedText();
        Term term;
        for (List<AdornedWord> sentence : taggedSentences) {
            for (AdornedWord word : sentence) {
                term = transformToTerm(word);
                if (filter.isTermGood(term)) {
                    terms.add(term);
                }
            }
        }
        return ttText;
    }

    private synchronized List<List<AdornedWord>> getTaggedSentences(String text) {
        return partOfSpeechTagger.tagSentences(sentenceSplitter.extractSentences(text, wordTokenizer));
    }

    private void lemmatize(List<List<AdornedWord>> taggedSentences) {
        for (List<AdornedWord> sentence : taggedSentences) {
            for (AdornedWord word : sentence) {
                lemmatize(word);
            }
        }
    }

    private synchronized void lemmatize(AdornedWord word) {
        String pos = word.getPartsOfSpeech();
        String spelling = word.getSpelling();
        String lemmaClass = posTags.getLemmaWordClass(word.getPartsOfSpeech());
        if (lemmatizer.cantLemmatize(spelling) || lemmaClass.equals("none")) {
            word.setLemmata(spelling);
        } else {
            String lemma;
            if (lemmaClass.length() == 0) {
                lemma = lemmatizer.lemmatize(spelling);
            } else {
                try {
                    lemma = lemmatizer.lemmatize(spelling, posTags.getLemmaWordClass(pos));
                } catch (Exception e) {
                    LOGGER.error("Exception", e);
                    lemma = null;
                }
            }
            if (lemma.equals("*")) {
                lemma = spelling;
            }
            word.setLemmata(lemma);
        }
    }

    private Term transformToTerm(AdornedWord word) {
        Term term = new Term(word.getSpelling(), word.getLemmata());
        String posTag = word.getPartsOfSpeech();
        term.setPosTag(posTag);
        setTermProperties(posTag, term.prop);
        return term;
    }

    private void setTermProperties(String posTag, TermProperties properties) {
        if (termCanGetMultipeWordClasses) {
            String firstPart, secondPart = null;
            int pos = posTag.indexOf('-');
            if (pos > 0) {
                firstPart = posTag.substring(0, pos);
                secondPart = posTag.substring(pos + 1);
            } else {
                firstPart = posTag;
            }
            if (firstPart.startsWith(NOUN_POS_TAG_PREFIX)) {
                properties.setNoun(true);
                if (firstPart.contains(ADJECTIVE_POS_TAG_PREFIX)) {
                    properties.setAdjective(true);
                }
            } else if (firstPart.startsWith(ADJECTIVE_POS_TAG_PREFIX)) {
                properties.setAdjective(true);
                if (firstPart.contains(NOUN_POS_TAG_PREFIX)) {
                    properties.setNoun(true);
                }
            } else if (firstPart.startsWith(VERB_POS_TAG_PREFIX)) {
                properties.setVerb(true);
            } else if (firstPart.startsWith(NUMBER_POS_TAG_PREFIX)) {
                properties.setNumber(true);
            }
            if (secondPart != null) {
                if (secondPart.equals(ADVERB_NOUN_POS_TAG)) {
                    properties.setNoun(true);
                    if (secondPart.contains(ADJECTIVE_POS_TAG_PREFIX)) {
                        properties.setAdjective(true);
                    }
                } else if (secondPart.startsWith(NOUN_POS_TAG_PREFIX)) {
                    properties.setNoun(true);
                    if (secondPart.contains(ADJECTIVE_POS_TAG_PREFIX)) {
                        properties.setAdjective(true);
                    }
                } else if (secondPart.startsWith(ADJECTIVE_POS_TAG_PREFIX)) {
                    properties.setAdjective(true);
                    if (secondPart.contains(NOUN_POS_TAG_PREFIX)) {
                        properties.setNoun(true);
                    }
                } else if (secondPart.startsWith(VERB_POS_TAG_PREFIX)) {
                    properties.setVerb(true);
                } else if (secondPart.startsWith(NUMBER_POS_TAG_PREFIX)) {
                    properties.setNumber(true);
                }
            }
        } else {
            if (posTag.startsWith(NOUN_POS_TAG_PREFIX)) {
                properties.setNoun(true);
            } else if (posTag.startsWith(ADJECTIVE_POS_TAG_PREFIX)) {
                properties.setAdjective(true);
            } else if (posTag.startsWith(VERB_POS_TAG_PREFIX)) {
                properties.setVerb(true);
            } else if (posTag.startsWith(NUMBER_POS_TAG_PREFIX)) {
                properties.setNumber(true);
            }
        }
    }

    private void setLemmatizer(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    private void setPosTags(PartOfSpeechTags posTags) {
        this.posTags = posTags;
    }

    private void setPartOfSpeechTagger(PartOfSpeechTagger partOfSpeechTagger) {
        this.partOfSpeechTagger = partOfSpeechTagger;
    }

}
