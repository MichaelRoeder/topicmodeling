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

public class StandardGermanPosTaggingTermFilter extends AbstractStandardPosTaggingTermFilter implements
        PosTaggingTermFilter {

    public static final String STOPWORD_FILE = "german.stopwords";

    private static StandardGermanPosTaggingTermFilter instance = null;

    public static StandardGermanPosTaggingTermFilter getInstance() {
        if (instance == null) {
            instance = readStopWords(new StandardGermanPosTaggingTermFilter(), STOPWORD_FILE);
        }
        return instance;
    }

    protected StandardGermanPosTaggingTermFilter() {
    }
}
