package org.dice_research.topicmodeling.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.OneDimensionalEvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.TwoDimensionalEvaluationResult;
import org.dice_research.topicmodeling.io.CSVFileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVFileReporter implements TestCaseListener, CSVFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CSVFileReporter.class);

    // private static final String DOUBLE_FORMAT_STRING = "%,f";

    private static final String DEFAULT_SINGLE_VALUES_REPORT_FILE_NAME = "reporter.csv";

    protected CSVWriter singleResultCSVwriter;
    protected boolean isResultDescriptionWritten = false;
    protected File folder;
    protected String filePrefix;
    protected NumberFormat formatter;

    public CSVFileReporter(File folder) {
        this(folder, "");
    }

    public CSVFileReporter(File folder, String filePrefix) {
        this.folder = folder;
        this.filePrefix = filePrefix;
        try {
            singleResultCSVwriter = new CSVWriter(
                    new FileWriter(folder + File.separator + filePrefix + DEFAULT_SINGLE_VALUES_REPORT_FILE_NAME),
                    SEPARATOR, QUOTECHAR, ESCAPECHAR);
        } catch (IOException e) {
            logger.error("Error while opening CSV file.", e);
        }
        singleResultCSVwriter.writeNext(new String[] { "event", "needed time" });

        formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(1000);
    }

    @Override
    public void reportPreprocessingFinished(long neededTime) {
        singleResultCSVwriter.writeNext(new String[] { "preprocessing finished", Long.toString(neededTime) });
    }

    @Override
    public void reportInitializationFinished(long neededTime) {
        singleResultCSVwriter.writeNext(new String[] { "initialization finished", Long.toString(neededTime) });
    }

    @Override
    public void reportEvaluationResult(ManagedEvaluationResultContainer results, int iterationNumber, long neededTime) {
        EvaluationResultCollection singleEvaluationResults = results.getSingleResults();
        if (!isResultDescriptionWritten) {
            writeSingleResultsFileHead(singleEvaluationResults);
            isResultDescriptionWritten = true;
        }
        writeSingleResultsFileValues(singleEvaluationResults, iterationNumber, neededTime);
        EvaluationResultCollection resultArray[] = results.getOneDimensionalResults();
        for (int i = 0; i < resultArray.length; i++) {
            writeOneDimensionalResults(resultArray[i], iterationNumber);
        }
        resultArray = results.getTwoDimensionalResults();
        for (int i = 0; i < resultArray.length; i++) {
            writeTwoDimensionalResults(resultArray[i], iterationNumber);
        }
    }

    @Override
    public void reportAlgorithmFinished(Model model, long neededTime) {
        singleResultCSVwriter.writeNext(new String[] { "algorithm finished", Long.toString(neededTime) });
        try {
            singleResultCSVwriter.close();
        } catch (IOException e) {
            logger.error("Error while closing CSV file.", e);
        }
    }

    protected void writeSingleResultsFileHead(EvaluationResultCollection results) {
        // String head[] = new String[results.size() + 2];
        String head[] = new String[3];
        head[0] = "Iteration #";
        head[1] = "Time needed";
        head[2] = "results";
        // for (int i = 0; i < results.size(); ++i) {
        // head[i + 2] = results.get(i).getResultName();
        // }
        singleResultCSVwriter.writeNext(head);
    }

    protected void writeSingleResultsFileValues(EvaluationResultCollection results, int iterationNumber,
            long neededTime) {
        String line[] = new String[(results.size() * 2) + 2];
        line[0] = Integer.toString(iterationNumber);
        line[1] = Long.toString(neededTime);

        Object resultObj;
        for (int i = 0; i < results.size(); ++i) {
            resultObj = results.get(i).getResult();
            line[2 * i + 2] = results.get(i).getResultName();
            if (resultObj instanceof Double) {
                // line[(2 * i) + 3] = String.format(DOUBLE_FORMAT_STRING,
                // resultObj);
                line[(2 * i) + 3] = formatter.format((Double) resultObj);
            } else {
                line[(2 * i) + 3] = results.get(i).getResultAsString();
            }
        }
        singleResultCSVwriter.writeNext(line);
    }

    protected void writeOneDimensionalResults(EvaluationResultCollection results, int iterationNumber) {
        if (results.size() > 0) {
            OneDimensionalEvaluationResult<?> oneDimResults[] = new OneDimensionalEvaluationResult<?>[results.size()];
            for (int i = 0; i < results.size(); i++) {
                oneDimResults[i] = (OneDimensionalEvaluationResult<?>) results.get(i);
            }
            CSVWriter writer = null;
            try {
                writer = new CSVWriter(new FileWriter(folder + File.separator + filePrefix + "results_for_"
                        + oneDimResults[0].getDimension().toString() + "_after_iteration_" + iterationNumber + ".csv"),
                        SEPARATOR, QUOTECHAR, ESCAPECHAR);
                String line[] = new String[oneDimResults.length + 1];
                // Write the head of the file
                line[0] = oneDimResults[0].getDimension().toString();
                for (int i = 0; i < oneDimResults.length; i++) {
                    line[i + 1] = oneDimResults[i].getResultName();
                }
                writer.writeNext(line);
                // Write the values of the file
                Object resultObj;
                for (int j = 0; j < oneDimResults[0].size(); j++) {
                    line[0] = Integer.toString(j);
                    for (int i = 0; i < oneDimResults.length; i++) {
                        resultObj = oneDimResults[i].getResult(j);
                        if (resultObj instanceof Double) {
                            // line[i + 1] = String.format(DOUBLE_FORMAT_STRING,
                            // resultObj);
                            line[i + 1] = formatter.format((Double) resultObj);
                        } else {
                            line[i + 1] = resultObj.toString();
                        }
                    }
                    writer.writeNext(line);
                }
            } catch (IOException e) {
                logger.error("Error while opening CSV file.", e);
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }

    protected void writeTwoDimensionalResults(EvaluationResultCollection results, int iterationNumber) {
        if (results.size() > 0) {
            TwoDimensionalEvaluationResult<?> twoDimResults[] = new TwoDimensionalEvaluationResult<?>[results.size()];
            for (int i = 0; i < results.size(); i++) {
                twoDimResults[i] = (TwoDimensionalEvaluationResult<?>) results.get(i);
            }
            CSVWriter writer = null;
            try {
                writer = new CSVWriter(new FileWriter(folder + File.separator + filePrefix + "results_for_"
                        + twoDimResults[0].getDimension1().toString() + "_"
                        + twoDimResults[0].getDimension2().toString() + "_after_iteration_" + iterationNumber + ".csv"),
                        SEPARATOR, QUOTECHAR, ESCAPECHAR);
                String line[] = new String[(twoDimResults[0].getDimension2Size() * twoDimResults.length) + 1];
                // Write the head of the file
                line[0] = twoDimResults[0].getDimension1().toString();

                for (int i = 0; i < twoDimResults[0].getDimension2Size(); i++) {
                    for (int j = 0; j < twoDimResults.length; j++) {
                        line[(i * twoDimResults.length) + j + 1] = twoDimResults[j].getDimension2().toString() + i + "_"
                                + twoDimResults[j].getResultName();
                    }
                }
                writer.writeNext(line);
                // Write the values of the file
                Object resultObj;
                for (int k = 0; k < twoDimResults[0].getDimension1Size(); k++) {
                    line[0] = Integer.toString(k);
                    for (int i = 0; i < twoDimResults[0].getDimension2Size(); i++) {
                        for (int j = 0; j < twoDimResults.length; j++) {
                            resultObj = twoDimResults[j].getResult(k, i);
                            if (resultObj instanceof Double) {
                                // line[(i * twoDimResults.length) + j + 1] =
                                // String
                                // .format(DOUBLE_FORMAT_STRING, resultObj);
                                line[(i * twoDimResults.length) + j + 1] = formatter.format((Double) resultObj);
                            } else {
                                line[(i * twoDimResults.length) + j + 1] = resultObj.toString();
                            }
                        }
                    }
                    writer.writeNext(line);
                }
            } catch (IOException e) {
                logger.error("Error while opening CSV file.", e);
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
