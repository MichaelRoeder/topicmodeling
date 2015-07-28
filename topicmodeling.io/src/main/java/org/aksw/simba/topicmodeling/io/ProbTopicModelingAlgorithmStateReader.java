package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.aksw.simba.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbTopicModelingAlgorithmStateReader {

    private static final Logger logger = LoggerFactory
            .getLogger(ProbTopicModelingAlgorithmStateReader.class);

    public ProbTopicModelingAlgorithmStateSupplier readProbTopicModelState(
            File file) {
        FileInputStream fin = null;
        ProbTopicModelingAlgorithmStateSupplier result = null;
        try {
            fin = new FileInputStream(file);
            result = readProbTopicModelState(fin);
        } catch (FileNotFoundException e) {
            logger.error(
                    "Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file",
                    e);
        } catch (IOException e) {
            logger.error(
                    "Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file",
                    e);
        } finally {
            IOUtils.closeQuietly(fin);
        }
        return result;
    }

    public ProbTopicModelingAlgorithmStateSupplier readProbTopicModelState(InputStream in) throws IOException {
        Object obj = null;
        ObjectInputStream oin = null;
        try {
            oin = new ObjectInputStream(in);
            obj = oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.error(
                    "Error while trying to read serialized ProbTopicModelingAlgorithmStateSupplier from file",
                    e);
        } finally {
            IOUtils.closeQuietly(oin);
        }
        return (ProbTopicModelingAlgorithmStateSupplier) obj;
    }
}
