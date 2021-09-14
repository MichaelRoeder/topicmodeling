package org.dice_research.topicmodeling.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.algorithm.mallet.MalletLdaWrapper;
import org.dice_research.topicmodeling.algorithms.ModelingAlgorithm;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.ArunModelSelectionEvaluator;
import org.dice_research.topicmodeling.evaluate.GriffithsAndSteyversModelSelectionEvaluator;
import org.dice_research.topicmodeling.evaluate.ProbTopicModelingAlgorithmParameterPrinter;
import org.dice_research.topicmodeling.io.ProbTopicModelingAlgorithmStateReader;
import org.dice_research.topicmodeling.reporting.CSVFileReporter;
import org.dice_research.topicmodeling.testframework.EvaluationTestCase;
import org.dice_research.topicmodeling.testframework.TestCaseInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple tool to run evaluations on a given topic model.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class ModelEvaluationTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelEvaluationTool.class);

    private static final String ARUN_CMD = "A";
    private static final String GRIFFITHS_CMD = "G";
    private static final String PARAMETER_PRINT_CMD = "P";

    private static final String OUTPUT_DIR_CMD = "o";
    private static final String GZIP_FILE_CMD = "g";
    private static final String PARALLEL_THREADS_CMD = "p";
    private static final String CSV_FILE_CMD = "c";
    private static final String MODEL_FILE_LIST_CMD = "f";

    protected static Options createCliOptions() {
        Options options = new Options();

        options.addOption(Option.builder(OUTPUT_DIR_CMD).argName("directory").hasArg()
                .desc("The directory to which result files will be written.").build());
        options.addOption(Option.builder(MODEL_FILE_LIST_CMD).argName("list-file").hasArg()
                .desc("A file that contains the paths of the model files.").build());
        options.addOption(Option.builder(PARALLEL_THREADS_CMD).argName("num-of-threads").hasArg()
                .desc("Number of threads that run in parallel (i.e. number of models that are evaluated in parallel).")
                .build());
        options.addOption(CSV_FILE_CMD, false, "Results will be reported as csv file.");
        options.addOption(GZIP_FILE_CMD, false, "Force usage of gzip algorithm.");

        options.addOption(ARUN_CMD, false, "Topic evaluation as suggested by Arun et al.");
        options.addOption(GRIFFITHS_CMD, false, "Topic evaluation as suggested by Griffiths et al.");
        options.addOption(PARAMETER_PRINT_CMD, false,
                "An evaluator that simply prints the main parameters of the topic model.");

        return options;
    }

    protected static void printHelp(Options options) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ModelEvaluationTool [options] (<model-file...> | -f <list-file>)", options);
    }

    public static void main(String[] args) throws InterruptedException {
        Options options = createCliOptions();
        // create the command line parser
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            // parse the command line arguments
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.error("Exception while parsing command:" + e.getMessage());
            return;
        }

        List<String> models = cmd.getArgList();
        if (cmd.hasOption(MODEL_FILE_LIST_CMD)) {
            try {
                models = FileUtils.readLines(new File(cmd.getOptionValue(MODEL_FILE_LIST_CMD)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOGGER.error("Exception while parsing the file that should contain the list of model files:"
                        + e.getMessage());
                return;
            }
        }

        if (models.isEmpty()) {
            LOGGER.error("Model file is missing.");
            printHelp(options);
            return;
        }

        File outputFolder = null;
        if (cmd.hasOption(OUTPUT_DIR_CMD)) {
            outputFolder = new File(cmd.getOptionValue(OUTPUT_DIR_CMD));
        } else {
            outputFolder = new File("./");
        }
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdirs()) {
                System.err.print("Couldn't create output directory \"");
                System.err.print(outputFolder.getAbsolutePath());
                System.err.print("\". Aborting.");
                return;
            }
        }

        int threads = 1;
        if (cmd.hasOption(PARALLEL_THREADS_CMD)) {
            threads = Integer.parseInt(cmd.getOptionValue(PARALLEL_THREADS_CMD));
        }

        if (threads > 1) {
            LOGGER.info("Creating thread pool with {} threads.", threads);
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            for (int i = 0; i < models.size(); ++i) {
                executor.submit(new EvaluationRun(models, i, cmd, outputFolder));
            }
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.DAYS);
        } else {
            LOGGER.info("The evaluation will run with a single thread.");
            for (int i = 0; i < models.size(); ++i) {
                (new EvaluationRun(models, i, cmd, outputFolder)).run();
            }
        }
    }

    private static InputStream openModelStream(String modelFile, CommandLine cmd) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(modelFile));
        if (modelFile.endsWith(".gz") || cmd.hasOption(GZIP_FILE_CMD)) {
            is = new GZIPInputStream(is);
        }
        return is;
    }

    public static class EvaluationRun implements Runnable {

        private List<String> models;
        private int modelId;
        private CommandLine cmd;
        private File outputFolder;

        public EvaluationRun(List<String> models, int modelId, CommandLine cmd, File outputFolder) {
            this.models = models;
            this.modelId = modelId;
            this.cmd = cmd;
            this.outputFolder = outputFolder;
        }

        @Override
        public void run() {
            String modelFile = models.get(modelId);
            LOGGER.info("Starting with model " + (modelId + 1) + "/" + models.size() + ": " + modelFile);

            ProbTopicModelingAlgorithmStateReader reader = new ProbTopicModelingAlgorithmStateReader();
            LOGGER.info("Reading model...");
            ProbTopicModelingAlgorithmStateSupplier stateSupplier = null;
            try (InputStream is = openModelStream(modelFile, cmd)) {
                stateSupplier = reader.readProbTopicModelState(is);
            } catch (IOException e) {
                LOGGER.error("Error while reading the model file:" + e.getMessage());
                return;
            }

            // Init test case
            TestCaseInterface testCase = new EvaluationTestCase((ModelingAlgorithm) stateSupplier);

            // Add evaluators
            LOGGER.info("Adding evaluators...");
            if (cmd.hasOption(PARAMETER_PRINT_CMD)) {
                testCase.addEvaluator(new ProbTopicModelingAlgorithmParameterPrinter(stateSupplier));
            }
            if (cmd.hasOption(ARUN_CMD)) {
                testCase.addEvaluator(new ArunModelSelectionEvaluator(stateSupplier));
            }
            if (cmd.hasOption(GRIFFITHS_CMD)) {
                testCase.addEvaluator(
                        new GriffithsAndSteyversModelSelectionEvaluator((MalletLdaWrapper) stateSupplier));
            }

            // Add reporters
            LOGGER.info("Adding reporters...");
            if (cmd.hasOption(CSV_FILE_CMD)) {
                testCase.addListener(
                        new CSVFileReporter(outputFolder, "result-" + Integer.toString(modelFile.hashCode())));
            }

            // Run test case
            LOGGER.info("Starting evaluation...");
            testCase.run();
            LOGGER.info("Evaluation finished for " + modelFile);
        }

    }

}
