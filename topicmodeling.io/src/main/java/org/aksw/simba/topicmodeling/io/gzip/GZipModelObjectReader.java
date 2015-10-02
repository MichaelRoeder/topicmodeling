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
package org.aksw.simba.topicmodeling.io.gzip;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.aksw.simba.topicmodeling.io.ModelObjectReader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipModelObjectReader extends ModelObjectReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipModelObjectReader.class);

    @Override
    public Model readModel(InputStream is) {
        GZIPInputStream gin = null;
        try {
            gin = new GZIPInputStream(is);
            return super.readModel(gin);
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized Model from file", e);
            return null;
        } finally {
            IOUtils.closeQuietly(gin);
        }
    }
}
