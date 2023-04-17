package org.dice_research.topicmodeling.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

public class SimpleProbTopicModAlgStateReporter implements TestCaseListener {

    // private static final String DOUBLE_FORMAT_STRING = "%,f";

    private static final boolean WRITE_GZIPPED_FILES = true;

    protected ProbTopicModelingAlgorithmStateSupplier probAlgState;

    protected File folder;

    public SimpleProbTopicModAlgStateReporter(ProbTopicModelingAlgorithmStateSupplier probAlgState, File folder) {
        this.probAlgState = probAlgState;
        this.folder = folder;
    }

    @Override
    public void reportPreprocessingFinished(long neededTime) {
    }

    @Override
    public void reportInitializationFinished(long neededTime) {
    }

    @Override
    public void reportEvaluationResult(ManagedEvaluationResultContainer results, int iterationNumber, long neededTime) {
    }

    @Override
    public void reportAlgorithmFinished(Model model, long neededTime) {
        FileOutputStream fout = null;
        GZIPOutputStream gout = null;
        ObjectOutputStream oout = null;
        try {
            if (WRITE_GZIPPED_FILES) {
                fout = new FileOutputStream(new File(folder + "/probAlgState.object.gz"));
                gout = new GZIPOutputStream(fout);
                oout = new ObjectOutputStream(gout);
            } else {
                fout = new FileOutputStream(new File(folder + "/probAlgState.object"));
                oout = new ObjectOutputStream(fout);
            }
            oout.writeObject(probAlgState);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oout);
            IOUtils.closeQuietly(gout);
            IOUtils.closeQuietly(fout);
        }
    }

}
