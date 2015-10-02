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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbTopicModelingAlgorithmStateWriter {

    private static final Logger logger = LoggerFactory.getLogger(ProbTopicModelingAlgorithmStateWriter.class);

    public void writeProbTopicModelState(ProbTopicModelingAlgorithmStateSupplier probAlgState, File file) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            writeProbTopicModelState(probAlgState, fout);
        } catch (Exception e) {
            logger.error("Error while trying to write ProbTopicModelingAlgorithmStateSupplier to file.", e);
        } finally {
            IOUtils.closeQuietly(fout);
        }
    }

    public void writeProbTopicModelState(ProbTopicModelingAlgorithmStateSupplier probAlgState, OutputStream out)
            throws IOException {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(probAlgState);
        } finally {
            IOUtils.closeQuietly(oOut);
        }
    }
}
