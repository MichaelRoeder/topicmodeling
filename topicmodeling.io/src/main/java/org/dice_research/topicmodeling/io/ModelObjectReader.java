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
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.io.java.CorpusObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelObjectReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusObjectReader.class);

    public Model readModel(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            return readModel(fin);
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized Model from file", e);
            return null;
        } finally {
            IOUtils.closeQuietly(fin);
        }
    }

    public Model readModel(InputStream is) {
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(is);
            return (Model) oin.readObject();
        } catch (Exception e) {
            LOGGER.error("Error while trying to read serialized Model from file", e);
            return null;
        } finally {
            IOUtils.closeQuietly(oin);
        }
    }
}
