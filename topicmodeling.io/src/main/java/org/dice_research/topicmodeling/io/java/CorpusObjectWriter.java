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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorpusObjectWriter implements CorpusWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusObjectWriter.class);

    @Deprecated
    protected File file;

    public CorpusObjectWriter() {
    }

    @Deprecated
    public CorpusObjectWriter(File file) {
        this.file = file;
    }

    @Deprecated
    public void writeCorpus(Corpus corpus) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            writeCorpus(corpus, fOut);
        } catch (Exception e) {
            LOGGER.error("Error while trying to write serialized corpus to file", e);
        } finally {
            IOUtils.closeQuietly(fOut);
        }
    }

    public void writeCorpus(Corpus corpus, OutputStream out) throws IOException {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(corpus);
        } finally {
            IOUtils.closeQuietly(oOut);
        }
    }
}
