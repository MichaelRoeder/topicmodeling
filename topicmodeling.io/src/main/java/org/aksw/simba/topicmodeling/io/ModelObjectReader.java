package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.aksw.simba.topicmodeling.algorithms.Model;
import org.apache.commons.io.IOUtils;
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
