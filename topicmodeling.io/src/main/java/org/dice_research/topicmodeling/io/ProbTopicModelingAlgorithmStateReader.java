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
package org.dice_research.topicmodeling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbTopicModelingAlgorithmStateReader {

    private static final Logger logger = LoggerFactory.getLogger(ProbTopicModelingAlgorithmStateReader.class);

    public ProbTopicModelingAlgorithmStateSupplier readProbTopicModelState(File file) {
        ProbTopicModelingAlgorithmStateSupplier result = null;
        try (InputStream in = new FileInputStream(file)) {
            result = readProbTopicModelState(in);
        } catch (FileNotFoundException e) {
            logger.error("Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file", e);
        } catch (IOException e) {
            logger.error("Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file", e);
        }
        return result;
    }

    public ProbTopicModelingAlgorithmStateSupplier readProbTopicModelState(InputStream in) throws IOException {
        Object obj = null;
        try (ObjectInputStream oin = new ObjectInputStream(in)) {
            obj = oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file", e);
        }
        return (ProbTopicModelingAlgorithmStateSupplier) obj;
    }
}
