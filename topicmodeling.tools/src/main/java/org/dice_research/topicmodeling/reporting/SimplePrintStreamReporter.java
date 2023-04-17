package org.dice_research.topicmodeling.reporting;

import java.io.PrintStream;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResultCollection;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;

/**
 * Class SimplePrintStreamReporter
 */
public class SimplePrintStreamReporter implements TestCaseListener {

    protected PrintStream printStream;

    public SimplePrintStreamReporter(PrintStream printStream) {
        this.printStream = printStream;
    };

    /**
     * Set the value of printStream
     * 
     * @param newVar the new value of printStream
     */
    protected void setPrintStream(PrintStream newVar) {
        printStream = newVar;
    }

    /**
     * Get the value of printStream
     * 
     * @return the value of printStream
     */
    protected PrintStream getPrintStream() {
        return printStream;
    }

    /**
     * @param neededTime
     */
    public void reportPreprocessingFinished(long neededTime) {
        printStream.print("Preprocessing finished after ");
        printStream.print(neededTime);
        printStream.println(" milliseconds.");
    }

    @Override
    public void reportInitializationFinished(long neededTime) {
        printStream.print("Initialization finished after ");
        printStream.print(neededTime);
        printStream.println(" milliseconds.");
    }

    /**
     * @param model
     * @param neededTime
     */
    public void reportAlgorithmFinished(Model model, long neededTime) {
        printStream.print("Algorithm finished after ");
        printStream.print(neededTime);
        printStream.println(" milliseconds.");
    }

    @Override
    public void reportEvaluationResult(ManagedEvaluationResultContainer results, int iterationNumber, long neededTime) {
        printStream.print("Got EvaluationResult #" + iterationNumber + " after ");
        printStream.print(neededTime);
        printStream.print(" milliseconds. Results are ");
        EvaluationResultCollection singleEvaluationResults = results.getSingleResults();
        if (singleEvaluationResults.size() > 0) {
            printStream.print(singleEvaluationResults.get(0).toString());
            for (int i = 1; i < singleEvaluationResults.size(); ++i) {
                printStream.print(" , " + singleEvaluationResults.get(i).toString());
            }
        }
        printStream.println();
    }
}
