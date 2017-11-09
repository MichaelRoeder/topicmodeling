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
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.dice_research.topicmodeling.io.FolderReader;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentRawData;
import org.junit.Before;
import org.junit.Test;


public class FolderReaderTest {

    private static final File folder = new File("src/test/resources/FolderReaderTest");

    private Map<String, Document> expectedDocuments;

    @Before
    public void prepareDocuments() {
        expectedDocuments = new HashMap<String, Document>();

        Document document = new Document(1);
        document.addProperty(new DocumentName("document0"));
        document.addProperty(new DocumentRawData("This is document0.".getBytes()));
        document.addProperty(new DocumentCategory(""));
        expectedDocuments.put("document0", document);

        document = new Document(0);
        document.addProperty(new DocumentName("document1"));
        document.addProperty(new DocumentRawData("This is document1 in category1.".getBytes()));
        document.addProperty(new DocumentCategory("category1"));
        expectedDocuments.put("document1", document);

        document = new Document(2);
        document.addProperty(new DocumentName("document2"));
        document.addProperty(new DocumentRawData("This is document2 in category2.".getBytes()));
        document.addProperty(new DocumentCategory("category2"));
        expectedDocuments.put("document2", document);
    }

    @Test
    public void testFolderReader() {
        FolderReader reader = new FolderReader(folder);
        reader.setDocumentStartId(0);
        reader.setUseFolderNameAsCategory(true);

        Document readDocument = reader.getNextDocument();
        String readDocName;
        int count = 0;
        Document expectedDocument;
        while (readDocument != null) {
            // Note that we can't check the IDs of the documents since they are
            // based on the order in which the documents are read. This order
            // depends on the operating system.
            readDocName = readDocument.getProperty(DocumentName.class).get();
            Assert.assertTrue(expectedDocuments.containsKey(readDocName));
            expectedDocument = expectedDocuments.get(readDocName);
            Assert.assertEquals(expectedDocument.getProperty(DocumentRawData.class),
                    readDocument.getProperty(DocumentRawData.class));
            Assert.assertEquals(expectedDocument.getProperty(DocumentCategory.class).getValue(), readDocument
                    .getProperty(DocumentCategory.class).getValue());
            ++count;
            readDocument = reader.getNextDocument();
        }
        Assert.assertEquals(expectedDocuments.size(), count);
    }
}
