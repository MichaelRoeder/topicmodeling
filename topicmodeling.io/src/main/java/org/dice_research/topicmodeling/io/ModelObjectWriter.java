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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.algorithms.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelObjectWriter extends AbstractModelFilesWriter {

    private static final Logger logger = LoggerFactory.getLogger(ModelObjectWriter.class);

    private File file;

    public ModelObjectWriter(File file) {
        super(file.getAbsoluteFile().getParentFile());
        this.file = file;
    }

    @Override
    public void writeModelToFiles(Model model) {
        if (!folder.exists()) {
            folder.mkdirs();
        }
        writeModel(model, file);
    }

    protected void writeModel(Model model, File file) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            writeModel(model, fout);
        } catch (Exception e) {
            logger.error("Couldn't write Model to file.", e);
        } finally {
            IOUtils.closeQuietly(fout);
        }
    }

    protected void writeModel(Model model, OutputStream out) throws IOException {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(model);
        } finally {
            IOUtils.closeQuietly(oOut);
        }
    }
}