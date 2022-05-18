/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.dice_research.topicmodeling.algorithms.ProbTopicModelingAlgorithmStateSupplier;
import org.dice_research.topicmodeling.algorithms.WordCounter;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Ignore;
import org.junit.Test;

public class LodcatProbTopicModelingAlgorithmStateReaderTest {

    private static final Logger logger = LoggerFactory.getLogger(LodcatProbTopicModelingAlgorithmStateReaderTest.class);

    private static final File file = new File("src/test/resources/LodcatProbTopicModelingAlgorithmStateReaderTest/ldamodel.xml");

    @Test
    @Ignore
    public void test() {
        LodcatProbTopicModelingAlgorithmStateReader reader = new LodcatProbTopicModelingAlgorithmStateReader();
        ProbTopicModelingAlgorithmStateSupplier stateSupplier = reader.readProbTopicModelState(file);
        assert stateSupplier.getNumberOfTopics() > 0 : "number of topics";
        assert stateSupplier.getNumberOfDocuments() > 0 : "number of documents";
        assert stateSupplier.getNumberOfWords() > 0 : "number of words";
        assert stateSupplier.getWordsOfDocument(0).length > 0 : "words of some document";
        assert stateSupplier.getWordTopicAssignmentForDocument(0).length > 0 : "word topics of some document";
        assert stateSupplier.getWordsOfDocument(0).length == stateSupplier.getWordTopicAssignmentForDocument(0).length;
    }
}
