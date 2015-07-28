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
