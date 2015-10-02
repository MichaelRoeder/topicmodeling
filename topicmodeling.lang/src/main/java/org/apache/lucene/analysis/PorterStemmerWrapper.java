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
package org.apache.lucene.analysis;

import org.tartarus.snowball.ext.EnglishStemmer;

public class PorterStemmerWrapper {
    /**
     * The English stemmer is the updated version of the original PorterStemmer.
     */
    private EnglishStemmer stemmer = new EnglishStemmer();

    public String getStem(String word) {
        String stem = null;
        stemmer.setCurrent(word);
        if (stemmer.stem()) {
            stem = new String(stemmer.getCurrentBuffer(), 0, stemmer.getCurrentBufferLength());
        }
        return stem;
    }

    public String getStemOrWordItself(String word) {
        String stem = getStem(word);
        if (stem == null) {
            return word;
        } else {
            return stem;
        }
    }
}
