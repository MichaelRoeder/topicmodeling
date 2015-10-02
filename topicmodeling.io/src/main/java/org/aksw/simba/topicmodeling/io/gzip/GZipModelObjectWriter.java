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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.aksw.simba.topicmodeling.io.ModelObjectWriter;
import org.apache.commons.io.IOUtils;

public class GZipModelObjectWriter extends ModelObjectWriter {

    public GZipModelObjectWriter(File file) {
        super(file);
    }

    @Override
    protected void writeModel(Model model, OutputStream out) throws IOException {
        GZIPOutputStream gout = null;
        try {
            gout = new GZIPOutputStream(out);
            super.writeModel(model, gout);
        } finally {
            IOUtils.closeQuietly(gout);
        }
    }
}
