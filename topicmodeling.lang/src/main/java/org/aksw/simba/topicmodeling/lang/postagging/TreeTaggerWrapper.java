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
package org.aksw.simba.topicmodeling.lang.postagging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.simba.topicmodeling.automaton.AutomatonCallback;
import org.aksw.simba.topicmodeling.automaton.BricsAutomatonManager;
import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.annolab.tt4j.TokenHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TreeTaggerWrapper extends AbstractPosTagger implements TokenHandler<String>, AutomatonCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerWrapper.class);

    public static final String TREE_TAGGER_HOME_PROPERTY_KEY = "com.unister.semweb.topicmodeling.lang.postagging.TreeTaggerWrapper.TreeTaggerHome";// /data/m.roeder/programme/TreeTagger
    public static final String MODEL_FILE_PROPERTY_KEY = "com.unister.semweb.topicmodeling.lang.postagging.TreeTaggerWrapper.ModelFile";// "/data/m.roeder/programme/TreeTagger/lib/german-utf8.par:utf-8";
    public static final String ABBREVIATION_FILE_PROPERTY_KEY = "com.unister.semweb.topicmodeling.lang.postagging.TreeTaggerWrapper.AbbreviationFile";// "/data/m.roeder/programme/TreeTagger/lib/german-abbreviations-utf8";

    private static final String TOKENIZER_PATTERN[] = { "[a-zäöüßA-ZÄÖÜ0-9\\-]+[\\.]", "[a-zäöüßA-ZÄÖÜ0-9\\-]+",
            "[\\.!\\?,]" };

    private org.annolab.tt4j.TreeTaggerWrapper<String> posTagger;
    private TermTokenizedText currentTTText;
    private String currentText;
    private Set<String> abbreviations;
    private BricsAutomatonManager automaton;
    private List<String> tokens;

    public static TreeTaggerWrapper createTreeTaggerWrapper() {
        String treeTaggerHome = System.getProperty(TREE_TAGGER_HOME_PROPERTY_KEY);
        if (treeTaggerHome == null) {
            LOGGER.error("Coudln't load the tree tagger home property (" + TREE_TAGGER_HOME_PROPERTY_KEY
                    + "). Returning null.");
            return null;
        }
        System.setProperty("treetagger.home", treeTaggerHome);

        String modelFile = System.getProperty(MODEL_FILE_PROPERTY_KEY);
        if (modelFile == null) {
            LOGGER.warn("Coudln't load the model file property (" + MODEL_FILE_PROPERTY_KEY + "). Returning null.");
            return null;
        }
        org.annolab.tt4j.TreeTaggerWrapper<String> posTagger = new org.annolab.tt4j.TreeTaggerWrapper<String>();
        try {
            posTagger.setModel(modelFile);
        } catch (IOException e) {
            LOGGER.error("Couldn't load the model of the pos tagger. Returning null.", e);
        }

        String abbreviationFile = System.getProperty(ABBREVIATION_FILE_PROPERTY_KEY);
        Set<String> abbreviations;
        if (abbreviationFile == null) {
            LOGGER.error("Coudln't load the abbreviation file property (" + ABBREVIATION_FILE_PROPERTY_KEY + ").");
            abbreviations = new HashSet<String>();
        } else {
            try {
                abbreviations = new HashSet<String>(FileUtils.readLines(new File(abbreviationFile)));
            } catch (IOException e) {
                LOGGER.error("Couldn't load the abbreviations.", e);
                abbreviations = new HashSet<String>();
            }
        }

        return new TreeTaggerWrapper(posTagger, abbreviations);
    }

    private TreeTaggerWrapper(org.annolab.tt4j.TreeTaggerWrapper<String> posTagger, Set<String> abbreviations) {
        this.posTagger = posTagger;
        posTagger.setHandler(this);
        this.abbreviations = abbreviations;
        automaton = new BricsAutomatonManager(this, TOKENIZER_PATTERN);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        return tokenizeText(text, null);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        currentTTText = new TermTokenizedText();
        currentText = text;

        tokens = new ArrayList<String>();
        automaton.parseText(text);

        try {
            posTagger.process(tokens);
        } catch (Exception e) {
            LOGGER.error("Got an exception while trying to tag the given text.", e);
        }
        TermTokenizedText result = currentTTText;
        tokens = null;
        currentTTText = null;
        return result;
    }

    @Override
    public void token(String token, String pos, String lemma) {
        Term term = new Term(token, cleanLemma(token, lemma));
        term.setPosTag(pos);
        setTermPropertiesFromPosTag(term, pos);
        PosTaggingTermFilter currentFilter = getFilter();
        if ((currentFilter == null) || (currentFilter.isTermGood(term))) {
            currentTTText.addTerm(term);
        }
    }

    /**
     * Sometimes, the Treetagger returns more than one lemma. They are separated
     * by a '|'. We will use the first lemma from this list.
     */
    private String cleanLemma(String token, String lemma) {
        int pos = lemma.indexOf('|');
        if (pos < 0) {
            return lemma.toLowerCase();
        } else {
            return lemma.substring(0, pos).toLowerCase();
        }
    }

    private void setTermPropertiesFromPosTag(Term term, String pos) {
        if (pos.startsWith("N")) {
            term.properties.setNoun(true);
        } else if (pos.startsWith("V")) {
            term.properties.setVerb(true);
        } else if (pos.equals("CARD")) {
            term.properties.setNumber(true);
        } else if (pos.equals("ADJA") || pos.equals("ADJD")) {
            term.properties.setAdjective(true);
        }
    }

    @Override
    public void foundPattern(int patternId, int startPos, int length) {
        if (patternId == 0) {
            String token = currentText.substring(startPos, startPos + length);
            if (abbreviations.contains(token)) {
                tokens.add(token);
            } else {
                tokens.add(token.substring(0, length - 1));
                tokens.add(token.substring(length - 1));
            }
        } else {
            tokens.add(currentText.substring(startPos, startPos + length));
        }
    }
}
