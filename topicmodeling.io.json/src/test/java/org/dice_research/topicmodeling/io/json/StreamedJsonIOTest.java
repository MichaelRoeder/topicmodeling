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

import org.dice_research.topicmodeling.io.json.stream.JsonWritingDocumentConsumer;
import org.dice_research.topicmodeling.io.json.stream.StreamBasedJsonDocumentSupplier;
import org.dice_research.topicmodeling.io.test.AbstractCorpusIOTest;

public class StreamedJsonIOTest extends AbstractCorpusIOTest {

    public StreamedJsonIOTest() {
        super((f) -> StreamBasedJsonDocumentSupplier.createReader(f),
                (f) -> JsonWritingDocumentConsumer.createJsonWritingDocumentConsumer(f),
                CorpusJsonIOTest.createTestCorpus(),  generateTempFile(".json"));
    }

}
