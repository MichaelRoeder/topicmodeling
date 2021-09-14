package org.dice_research.topicmodeling.evaluate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.algorithms.ClassificationModel;
import org.dice_research.topicmodeling.algorithms.Model;
import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.evaluate.result.EvaluationResult;
import org.dice_research.topicmodeling.evaluate.result.ManagedEvaluationResultContainer;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentClassificationResult;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentURI;
import org.dice_research.topicmodeling.utils.doc.ProbabilisticClassificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DEVELOPED FOR TOPICS4LOD. Shouldn't be used for other projects.
 * 
 * @author m.roeder
 * 
 */
public class Tapioca_SimilarityEvaluator extends AbstractEvaluatorWithClassifiedTestCorpus {

	private static final Logger LOGGER = LoggerFactory.getLogger(Tapioca_SimilarityEvaluator.class);

	public static final String SIMILARITY_OUTPUT_FILE_NAME = "dataset_similarities.csv";

	protected ProbTopicModelingAlgorithmStateSupplier probAlgState;
	protected static final int NUMNER_OF_TOPIC_INFERENCES = 1;
	protected String outputFolder;
	protected Corpus trainCorpus;
	protected String outputFileName = SIMILARITY_OUTPUT_FILE_NAME;

	public Tapioca_SimilarityEvaluator(Corpus trainCorpus, Corpus testCorpus,
			ProbTopicModelingAlgorithmStateSupplier probAlgState, String outputFolder) {
		super(testCorpus);
		this.probAlgState = probAlgState;
		this.outputFolder = outputFolder;
		this.trainCorpus = trainCorpus;
	}

	public Tapioca_SimilarityEvaluator(Corpus trainCorpus, Corpus testCorpus,
			ProbTopicModelingAlgorithmStateSupplier probAlgState) {
		super(testCorpus);
		this.probAlgState = probAlgState;
		this.outputFolder = null;
		this.trainCorpus = trainCorpus;
	}

	@Override
	public EvaluationResult evaluate(Model model, ManagedEvaluationResultContainer previousResults) {
		if (!(model instanceof ClassificationModel)) {
			throw new IllegalArgumentException("Got a " + model.getClass().getCanonicalName()
					+ " as Model while expecting a " + ClassificationModel.class.getCanonicalName());
		}
		ClassificationModel cm = (ClassificationModel) model;
		classifyDocuments(cm);
		return evaluateModelWithClassifiedCorpus(cm, previousResults);
	}

	@Override
	public EvaluationResult evaluateModelWithClassifiedCorpus(ClassificationModel model,
			ManagedEvaluationResultContainer previousResults) {

		DocumentClassificationResult trainCorpusResults[] = classifyDocuments(model, trainCorpus);

		// print out head of similarity file
		double trainDocTopics[][] = new double[trainCorpusResults.length][];
		double docVectorLengths[] = new double[trainCorpusResults.length];
		// Document document;
		for (int i = 0; i < trainDocTopics.length; i++) {
			trainDocTopics[i] = ((ProbabilisticClassificationResult) trainCorpusResults[i]).getTopicProbabilities();
			docVectorLengths[i] = getLength(trainDocTopics[i]);
		}

		PrintStream simOut = null;
		try {
			if (outputFolder != null) {
				simOut = new PrintStream(outputFolder + File.separator + outputFileName);
			} else {
				simOut = new PrintStream(outputFileName);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Couldn't create similarity file. Aborting.", e);
			return null;
		}

		double similarity;
		Document document;
		DocumentName name;
		DocumentURI uri;
		for (int documentId = 0; documentId < trainCorpus.getNumberOfDocuments(); ++documentId) {
			document = trainCorpus.getDocument(documentId);
			simOut.print(",\"");
			uri = document.getProperty(DocumentURI.class);
			if (uri != null) {
				simOut.print(uri.get());
			} else {
				name = document.getProperty(DocumentName.class);
				if (name != null) {
					simOut.print(name.get());
				} else {
					simOut.print("Document #");
					simOut.print(documentId);
				}
			}
			simOut.print('"');
		}
		simOut.println();
		double testDocTopics[];
		double testDocVectorLength;
		for (int i = 0; i < this.testCorpus.getNumberOfDocuments(); ++i) {
			document = testCorpus.getDocument(i);
			name = document.getProperty(DocumentName.class);
			simOut.print('"');
			uri = document.getProperty(DocumentURI.class);
			if (uri != null) {
				simOut.print(uri.get());
			} else {
				name = document.getProperty(DocumentName.class);
				if (name != null) {
					simOut.print(name.get());
				} else {
					simOut.print("Document #");
					simOut.print(i);
				}
			}
			simOut.print('"');
			testDocTopics = ((ProbabilisticClassificationResult) this.classifications[i]).getTopicProbabilities();
			testDocVectorLength = getLength(testDocTopics);
			for (int j = 0; j < trainDocTopics.length; ++j) {
				similarity = calculateSimilarity(testDocTopics, testDocVectorLength, trainDocTopics[j],
						docVectorLengths[j]);
				simOut.print(',');
				simOut.print(similarity);
			}
			simOut.println();
		}
		IOUtils.closeQuietly(simOut);
		return null;
	}

	protected double calculateSimilarity(double[] vector1, double length1, double[] vector2, double length2) {
		if ((length1 == 0) || (length2 == 0)) {
			if ((length1 == 0) && (length2 == 0)) {
				return 1;
			} else {
				return 0;
			}
		} else {
			double sum = 0;
			for (int i = 0; i < vector1.length; ++i) {
				sum += vector1[i] * vector2[i];
			}
			return sum / (length1 * length2);
		}
	}

	protected double getLength(double[] vector) {
		double length = 0;
		for (int i = 0; i < vector.length; ++i) {
			length += Math.pow(vector[i], 2);
		}
		return Math.sqrt(length);
	}

	// protected DocumentClassificationResult[] classifyDocuments(
	// ClassificationModel model, Corpus corpus) {
	// prepareCorpus(testCorpus);
	// Document document;
	// DocumentClassificationResult classifications[] = new
	// DocumentClassificationResult[testCorpus
	// .getNumberOfDocuments()];
	// for (int documentId = 0; documentId < this.testCorpus
	// .getNumberOfDocuments(); ++documentId) {
	// document = this.testCorpus.getDocument(documentId);
	// // document.addProperty(model.getClassificationForDocument(document));
	// classifications[documentId] = model
	// .getClassificationForDocument(document);
	// }
	// return classifications;
	// }

	@Override
	public void setReportProvisionalResults(boolean reportProvisionalResults) {
		// nothing to do
	}
}
