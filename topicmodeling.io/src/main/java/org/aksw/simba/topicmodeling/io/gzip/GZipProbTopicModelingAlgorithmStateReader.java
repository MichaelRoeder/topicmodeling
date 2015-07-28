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
