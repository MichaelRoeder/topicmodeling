/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io.java;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.AbstractCorpusReader;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusObjectReader extends AbstractCorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusObjectReader.class);

    public CorpusObjectReader() {
        super();
    }
    
    @Deprecated
    public CorpusObjectReader(File file) {
        super(file);
    }

    public void readCorpus(InputStream is) {
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(is);
            corpus = (Corpus) oin.readObject();
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized corpus from file", e);
        } finally {
            IOUtils.closeQuietly(oin);
        }
    }
}
