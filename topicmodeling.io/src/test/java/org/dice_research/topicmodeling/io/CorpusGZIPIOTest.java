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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dice_research.topicmodeling.io.gzip.GZipCorpusReaderDecorator;
import org.dice_research.topicmodeling.io.gzip.GZipCorpusWriterDecorator;
import org.dice_research.topicmodeling.io.java.CorpusObjectReader;
import org.dice_research.topicmodeling.io.java.CorpusObjectWriter;
import org.dice_research.topicmodeling.io.test.AbstractCorpusIOTest;
import org.dice_research.topicmodeling.io.xml.CorpusXmlReader;
import org.dice_research.topicmodeling.io.xml.CorpusXmlWriter;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CorpusGZIPIOTest extends AbstractCorpusIOTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testCases = new ArrayList<>();
        testCases.add(new Object[] { new CorpusObjectReader(), new CorpusObjectWriter(),
                CorpusObjectIOTest.createTestCorpus(), generateTempFile(".object") });
        testCases.add(new Object[] { new CorpusXmlReader(), new CorpusXmlWriter(), CorpusXMLIOTest.createTestCorpus(),
                generateTempFile(".object") });
        return testCases;
    }

    public CorpusGZIPIOTest(CorpusReader reader, CorpusWriter writer, Corpus corpus, File testFile) {
        super(new GZipCorpusReaderDecorator(reader), new GZipCorpusWriterDecorator(writer), corpus, testFile);
    }
}
