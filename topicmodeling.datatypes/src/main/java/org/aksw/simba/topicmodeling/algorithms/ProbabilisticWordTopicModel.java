/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.simba.topicmodeling.algorithms;

import org.aksw.simba.topicmodeling.utils.doc.DocumentWordCounts;

public interface ProbabilisticWordTopicModel extends VocabularyContainingClassificationModel {

	public double getSmoothedProbabilityOfWord(int wordId, int topicId);
	
	public double getProbabilityOfWord(int wordId, int topicId);
	
	public double getSmoothedProbabilityOfTopic(int topicId);
	
	public int getNumberOfTopics();
	
	public double[] getTopicProbabilitiesForDocument(DocumentWordCounts wordCounts);
//	public double[] getTopicProbabilitiesForDocument_test(Vector<Integer> wordCounts);
}
