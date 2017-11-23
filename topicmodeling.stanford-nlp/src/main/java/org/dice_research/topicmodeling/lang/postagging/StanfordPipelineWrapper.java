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

import java.util.List;
import java.util.Properties;

import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class StanfordPipelineWrapper extends AbstractPosTagger {

    private static final Logger logger = LoggerFactory.getLogger(StanfordPipelineWrapper.class);

    public static final Properties DEFAULT_PROPERTIES = PropertiesUtils.asProperties("annotators",
            "tokenize,ssplit,pos,lemma");

    private StanfordCoreNLP pipeline;

    public static StanfordPipelineWrapper createDefaultStanfordPipelineWrapper() {
        return createStanfordPipelineWrapper(DEFAULT_PROPERTIES, null);
    }

    public static StanfordPipelineWrapper createDefaultStanfordPipelineWrapper(PosTaggingTermFilter filter) {
        return createStanfordPipelineWrapper(DEFAULT_PROPERTIES, filter);
    }

    public static StanfordPipelineWrapper createStanfordPipelineWrapper(Properties properties,
            PosTaggingTermFilter filter) {
        try {
            return new StanfordPipelineWrapper(properties, filter);
        } catch (Exception e) {
            logger.error("Couldn't create pipeline. Returning null.", e);
            return null;
        }
    }

    private StanfordPipelineWrapper(Properties properties, PosTaggingTermFilter filter) {
        pipeline = new StanfordCoreNLP(properties);
        setFilter(filter);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text) {
        return tokenizeText(text, null);
    }

    @Override
    protected TermTokenizedText tokenizeText(String text, PosTaggingTermFilter filter) {
        TermTokenizedText termTokenizedText = new TermTokenizedText();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            addSentence(termTokenizedText, sentence, filter);
        }
        return termTokenizedText;
    }

    private void addSentence(TermTokenizedText termTokenizedText, CoreMap sentence, PosTaggingTermFilter filter) {
        Term term;
        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
            term = createTerm(token);
            if ((filter == null) || (filter.isTermGood(term))) {
                termTokenizedText.addTerm(term);
            }
        }
    }

    private Term createTerm(CoreLabel token) {
        Term term = new Term(token.get(TextAnnotation.class), token.get(LemmaAnnotation.class));
        term.setPosTag(token.get(PartOfSpeechAnnotation.class));

        if (term.getPosTag().contains("NN")) {
            term.prop.setNoun(true);
        } else if (term.getPosTag().contains("VB")) {
            term.prop.setVerb(true);
        } else if (term.getPosTag().contains("CD")) {
            term.prop.setNumber(true);
        } else if (term.getPosTag().contains("JJ")) {
            term.prop.setAdjective(true);
        }

        return term;
    }

}
