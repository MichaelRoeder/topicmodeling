package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbTopicModelingAlgorithmStateWriter {

    private static final Logger logger = LoggerFactory.getLogger(ProbTopicModelingAlgorithmStateWriter.class);

    public void writeProbTopicModelState(ProbTopicModelingAlgorithmStateSupplier probAlgState, File file) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            writeProbTopicModelState(probAlgState, fout);
        } catch (Exception e) {
            logger.error("Error while trying to write ProbTopicModelingAlgorithmStateSupplier to file.", e);
        } finally {
            IOUtils.closeQuietly(fout);
        }
    }

    public void writeProbTopicModelState(ProbTopicModelingAlgorithmStateSupplier probAlgState, OutputStream out)
            throws IOException {
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(probAlgState);
        } finally {
            IOUtils.closeQuietly(oOut);
        }
    }
}
