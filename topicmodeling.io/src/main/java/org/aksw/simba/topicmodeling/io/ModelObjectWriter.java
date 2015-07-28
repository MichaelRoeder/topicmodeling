package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.apache.commons.io.IOUtils;
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