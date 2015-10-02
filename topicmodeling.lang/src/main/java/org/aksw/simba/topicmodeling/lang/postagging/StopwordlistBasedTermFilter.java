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
import java.io.InputStream;

import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.lang.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StopwordlistBasedTermFilter extends AbstractListBasedTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopwordlistBasedTermFilter.class);

    public StopwordlistBasedTermFilter(Language lang) {
        switch (lang) {
        case GER: {
            createWordListSafely(this.getClass().getClassLoader()
                    .getResourceAsStream(StandardGermanPosTaggingTermFilter.STOPWORD_FILE));
            break;
        }
        case ENG: {
            createWordListSafely(this.getClass().getClassLoader()
                    .getResourceAsStream(StandardEnglishPosTaggingTermFilter.STOPWORD_FILE));
            break;
        }
        default: {
            LOGGER.warn("There is no default stop word list for the language " + lang.toString()
                    + ". This filter won't work.");
        }
        }
    }

    public StopwordlistBasedTermFilter(File stopwordlistFile) {
        createWordListSafely(stopwordlistFile);
    }

    public StopwordlistBasedTermFilter(InputStream stopwordlistStream) {
        createWordListSafely(stopwordlistStream);
    }

    @Override
    public boolean isTermGood(Term term) {
        return !(isWordInWordlist(term.getLemma()));
    }

}
