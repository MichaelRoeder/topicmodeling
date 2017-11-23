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
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.java.CorpusObjectReader;

/**
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 * @deprecated Use the {@link GZipCorpusReaderDecorator} instead.
 */
@Deprecated
public class GZipCorpusObjectReader extends CorpusObjectReader {

    public GZipCorpusObjectReader(File file) {
        super(file);
    }

    @Override
    public void readCorpus(InputStream is) {
        GZIPInputStream gin = null;
        try {
            gin = new GZIPInputStream(is);
            super.readCorpus(gin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(gin);
        }
    }

}
