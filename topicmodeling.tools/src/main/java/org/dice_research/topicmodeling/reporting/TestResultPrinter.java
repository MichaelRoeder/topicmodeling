package org.dice_research.topicmodeling.reporting;
import org.dice_research.topicmodeling.algorithms.Model;

public interface TestResultPrinter extends TestCaseListener {

	/**
	 * @param        model
	 */
	public void printModel(Model model);


}
