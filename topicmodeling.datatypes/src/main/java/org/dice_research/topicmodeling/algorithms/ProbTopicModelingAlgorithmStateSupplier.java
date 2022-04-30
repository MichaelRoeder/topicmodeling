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
package org.dice_research.topicmodeling.algorithms;

import java.io.Serializable;

public interface ProbTopicModelingAlgorithmStateSupplier extends VocabularyContaining, Serializable {

    public int[] getWordTopicAssignmentForDocument(int documentId);

    public int[] getWordsOfDocument(int documentId);

    public WordCounter getWordCounts();

    public int getNumberOfTopics();

    public int getNumberOfDocuments();

    /**
     * Number of different word types (i.e., vocabulary size).
     * @return
     */
    public int getNumberOfWords();

    public long getSeed();
}
