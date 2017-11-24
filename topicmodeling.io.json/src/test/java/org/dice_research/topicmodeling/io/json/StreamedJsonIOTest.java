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
package org.dice_research.topicmodeling.io.json;

import java.io.File;

import org.dice_research.topicmodeling.io.json.stream.JsonWritingDocumentConsumer;
import org.dice_research.topicmodeling.io.json.stream.StreamBasedJsonDocumentSupplier;
import org.dice_research.topicmodeling.io.test.AbstractCorpusIOTest;

public class StreamedJsonIOTest extends AbstractCorpusIOTest {

    private static final File TEST_FILE = generateTempFile(".json");

    public StreamedJsonIOTest() {
        super(StreamBasedJsonDocumentSupplier.createReader(TEST_FILE),
                JsonWritingDocumentConsumer.createJsonWritingDocumentConsumer(TEST_FILE),
                CorpusJsonIOTest.createTestCorpus(), TEST_FILE);
    }

}
