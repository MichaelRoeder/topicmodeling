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
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractListBasedTermFilter implements PosTaggingTermFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractListBasedTermFilter.class);

    private Set<String> wordlist;

    protected void createWordList(File stopwordfile) throws IOException {
        wordlist = new HashSet<String>(FileUtils.readLines(stopwordfile));
    }

    protected void createWordListSafely(File stopwordfile) {
        try {
            wordlist = new HashSet<String>(FileUtils.readLines(stopwordfile));
        } catch (Exception e) {
            LOGGER.error("Couldn't read word list from file. This PosTaggingTermFilter won't work as expected!", e);
        }
    }

    protected void createWordList(InputStream in) throws IOException {
        wordlist = new HashSet<String>(IOUtils.readLines(in));
    }

    protected void createWordListSafely(InputStream in) {
        try {
            wordlist = new HashSet<String>(IOUtils.readLines(in));
        } catch (Exception e) {
            LOGGER.error("Couldn't read word list from stream. This PosTaggingTermFilter won't work as expected!", e);
        }
    }

    protected boolean isWordInWordlist(String lemma) {
        return wordlist.contains(lemma);
    }
}
