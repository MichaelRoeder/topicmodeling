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

import org.dice_research.topicmodeling.io.java.CorpusObjectReader;
import org.dice_research.topicmodeling.io.java.CorpusObjectWriter;

public class CorpusObjectIOTest extends AbstractCorpusIOTest {

    private static final File CORPUS_FILE = generateTempFile(".object");

    public CorpusObjectIOTest() {
        super(new CorpusObjectReader(CORPUS_FILE), new CorpusObjectWriter(CORPUS_FILE), createTestCorpus());
    }
}
