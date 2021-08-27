package org.aksw.simba.topicmodeling.evaluate;

import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.evaluate.result.SingleEvaluationResult;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.SingularValueDecomposition;

public class ArunModelSelectionEvaluator extends AbstractEvaluator {

	public static final double LOG2 = Math.log(2);

	private ProbTopicModelingAlgorithmStateSupplier probAlgState;

	public ArunModelSelectionEvaluator(ProbTopicModelingAlgorithmStateSupplier probAlgState) {
		this.probAlgState = probAlgState;
	}

	@Override
	protected EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
		int numberOfWords = probAlgState.getNumberOfWords();
		int numberOfTopics = probAlgState.getNumberOfTopics();
		int numberOfDocuments = probAlgState.getNumberOfDocuments();
		DoubleMatrix2D topicWordsMatrix = DoubleFactory2D.sparse.make(numberOfTopics, numberOfWords);
		DoubleMatrix2D documentTopicsMatrix = DoubleFactory2D.sparse.make(numberOfDocuments, numberOfTopics);
		int words[], topics[];
		double documentLengths[] = new double[numberOfDocuments];
		for (int d = 0; d < numberOfDocuments; ++d) {
			words = probAlgState.getWordsOfDocument(d);
			topics = probAlgState.getWordTopicAssignmentForDocument(d);
			for (int i = 0; i < words.length; ++i) {
				topicWordsMatrix.set(topics[i], words[i], topicWordsMatrix.getQuick(topics[i], words[i]) + 1);
				documentTopicsMatrix.set(d, topics[i], documentTopicsMatrix.getQuick(d, topics[i]) + 1);
			}
			documentLengths[d] = words.length;
		}
		SingularValueDecomposition decomposition = new SingularValueDecomposition(
				topicWordsMatrix.rows() >= topicWordsMatrix.columns() ? topicWordsMatrix
						: Algebra.DEFAULT.transpose(topicWordsMatrix));
		double singularValues[] = decomposition.getSingularValues();
		topicWordsMatrix = null;
		decomposition = null;

		DoubleMatrix2D docLenghtsVector = DoubleFactory2D.dense.make(new double[][] { documentLengths });
		DoubleMatrix2D cm2Matrix = Algebra.DEFAULT.mult(docLenghtsVector, documentTopicsMatrix);
		documentTopicsMatrix = null;
		DoubleMatrix1D cm2Vector = cm2Matrix.viewRow(0);
		normalize(cm2Vector);
		double normedCm2Vector[] = cm2Vector.toArray();
		return new SingleEvaluationResult("Arun", new Double(SymmetricKLDivergence(singularValues, normedCm2Vector)));
	}

	protected double SymmetricKLDivergence(double[] v1, double[] v2) {
		return calcKLDivergence(v1, v2) + calcKLDivergence(v2, v1);
	}

	protected double calcKLDivergence(double[] v1, double[] v2) {
		double sum = 0;
		for (int i = 0; i < v1.length; ++i) {
			if ((v1[i] > 0) && (v2[i] > 0)) {
				sum += v1[i] * Math.log(v1[i] / v2[i]);
			}
		}
		return sum / LOG2;
	}
	
	protected void normalize(DoubleMatrix1D cm2Vector) {
		double length = Algebra.DEFAULT.norm2(cm2Vector);
		for (int i = 0; i < cm2Vector.size(); ++i) {
			cm2Vector.setQuick(i, cm2Vector.getQuick(i) / length);
		}
	}

	@Override
	public void setReportProvisionalResults(boolean reportProvisionalResults) {
		// nothing to do
	}

}
