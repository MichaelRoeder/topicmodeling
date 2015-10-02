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

import java.io.IOException;
import java.io.InputStream;

import org.aksw.simba.topicmodeling.lang.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

public class AbstractStandardPosTaggingTermFilter extends AbstractListBasedTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStandardPosTaggingTermFilter.class);

    protected static <T extends AbstractStandardPosTaggingTermFilter> T readStopWords(T filter, String stopWordFile) {
        try {
            InputStream is = StandardEnglishPosTaggingTermFilter.class.getClassLoader().getResourceAsStream(
                    stopWordFile);
            if (is == null) {
                LOGGER.error("Couldn't load stopwords (" + stopWordFile + "). Returning null.");
                return null;
            }
            filter.createWordList(is);
            return filter;
        } catch (IOException e) {
            LOGGER.error("Couldn't load stopwords. Returning null.", e);
            return null;
        }
    }

    private RunAutomaton symbolChecker = new RunAutomaton((new RegExp(".*[a-zA-Z].*")).toAutomaton());

    protected AbstractStandardPosTaggingTermFilter() {
    }

    @Override
    public boolean isTermGood(Term term) {
        String label = term.getWordForm();
        switch (label.length()) {
        case 0: // falls through
        case 1:
            return false;
        case 2: {
            // if one of the two characters is not upper case
            if (!Character.isUpperCase(label.charAt(0)) && !Character.isUpperCase(label.charAt(1))) {
                return false;
            }
            // else falls through
        }
        default: {
            return (!isWordInWordlist(term.getLemma())) && symbolChecker.run(term.getLemma());
        }
        }
    }
}
