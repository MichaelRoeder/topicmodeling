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
package org.dice_research.topicmodeling.io.gzip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusObjectReader;

public class GZipCorpusObjectReader extends CorpusObjectReader {

    public GZipCorpusObjectReader(File file) {
        super(file);
    }

    @Override
    protected void readCorpus(InputStream is) throws IOException, ClassNotFoundException {
        GZIPInputStream gin = null;
        try {
            gin = new GZIPInputStream(is);
            super.readCorpus(gin);
        } finally {
            IOUtils.closeQuietly(gin);
        }
    }

}
