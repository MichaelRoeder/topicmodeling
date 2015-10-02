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
package org.aksw.simba.topicmodeling.io;

import java.io.File;

import org.aksw.simba.topicmodeling.io.CorpusReader;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCorpusReader implements CorpusReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCorpusReader.class);

    protected File file;
    protected Corpus corpus;

    public AbstractCorpusReader(File file) {
        this.file = file;
    }

    @Override
    public void addDocuments(DocumentSupplier documentFactory) {
        LOGGER.info("Got a " + documentFactory.getClass().getCanonicalName()
                + " object as DocumentSupplier. But I'm a corpus reader and don't need such a supplier. ");
    }

    @Override
    public Corpus getCorpus() {
        if (corpus == null) {
            readCorpus();
        }
        return corpus;
    }

    @Override
    public boolean hasCorpus() {
        return corpus != null;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
    }
}
