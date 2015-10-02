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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.aksw.simba.topicmodeling.io.ProbTopicModelingAlgorithmStateReader;
import org.apache.commons.io.IOUtils;

public class GZipProbTopicModelingAlgorithmStateReader extends ProbTopicModelingAlgorithmStateReader {

    public ProbTopicModelingAlgorithmStateSupplier readProbTopicModelState(InputStream in) throws IOException {
        ProbTopicModelingAlgorithmStateSupplier result = null;
        GZIPInputStream gin = null;
        try {
            gin = new GZIPInputStream(in);
            result = super.readProbTopicModelState(gin);
        } finally {
            IOUtils.closeQuietly(gin);
        }
        return result;
    }
}
