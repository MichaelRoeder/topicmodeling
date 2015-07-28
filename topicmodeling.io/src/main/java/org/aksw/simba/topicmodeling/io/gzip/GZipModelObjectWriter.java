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
