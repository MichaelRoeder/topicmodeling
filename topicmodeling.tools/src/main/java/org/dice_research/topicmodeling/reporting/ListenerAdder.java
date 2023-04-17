package org.dice_research.topicmodeling.reporting;

import java.io.File;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.io.ModelCSVWriter;
import org.dice_research.topicmodeling.io.ModelObjectWriter;
import org.dice_research.topicmodeling.io.ZipfBasedTopWordWriter;
import org.dice_research.topicmodeling.testframework.TestCaseInterface;

public class ListenerAdder {

    protected TestCaseInterface testCase;
    protected File outputFolder;

    public ListenerAdder(TestCaseInterface testCase) {
        this.testCase = testCase;
    }

    public void setOutputFolder(String outputFolderName) {
        outputFolder = new File(outputFolderName);
    }

    public void addCsvFileReporter() {
        testCase.addListener(new CSVFileReporter(outputFolder));
    }

    public void addModelCsvWriter(ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier) {
        testCase.addListener(new SimpleModelReporter(new ModelCSVWriter(probTopicStateSupplier, outputFolder)));
    }

    public void addModelObjectWriter() {
        testCase.addListener(new SimpleModelReporter(new ModelObjectWriter(outputFolder)));
    }

    public void addSimpleLogReporter() {
        testCase.addListener(new SimpleLogReporter());
    }

    public void addSimpleProbTopicModAlgStateReporter(ProbTopicModelingAlgorithmStateSupplier stateReporter) {
        testCase.addListener(new SimpleProbTopicModAlgStateReporter(stateReporter, outputFolder));
    }

    public void addZipfBasedTopWordWriter(ProbTopicModelingAlgorithmStateSupplier probTopicStateSupplier) {
        testCase.addListener(new SimpleModelReporter(new ZipfBasedTopWordWriter(probTopicStateSupplier, outputFolder)));
    }
}
