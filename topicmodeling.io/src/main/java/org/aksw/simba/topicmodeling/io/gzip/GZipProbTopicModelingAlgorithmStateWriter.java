package org.aksw.simba.topicmodeling.io.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.aksw.simba.topicmodeling.io.ProbTopicModelingAlgorithmStateWriter;
import org.apache.commons.io.IOUtils;

public class GZipProbTopicModelingAlgorithmStateWriter extends ProbTopicModelingAlgorithmStateWriter {

    public void writeProbTopicModelState(ProbTopicModelingAlgorithmStateSupplier probAlgState, OutputStream out)
            throws IOException {
        GZIPOutputStream gout = null;
        try {
            gout = new GZIPOutputStream(out);
            super.writeProbTopicModelState(probAlgState, gout);
        } finally {
            IOUtils.closeQuietly(gout);
        }
    }
}
