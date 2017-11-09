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
import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.dice_research.topicmodeling.io.CSVDocumentImporter;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.junit.Before;
import org.junit.Test;


public class CSVDocumentImporterTest {

	private static final File csvFile = new File(
			"src/test/resources/CSVDocumentImporterTest/testFile.csv");

	private Document documents[] = new Document[3];

	@Before
	public void prepareDocuments() {
		documents[0] = new Document(0);
		documents[0].addProperty(new DocumentName("document0"));
		documents[0].addProperty(new DocumentText("This is document0."));
		documents[0].addProperty(new DocumentCategory(""));
		
		documents[1] = new Document(1);
		documents[1].addProperty(new DocumentName("document1"));
		documents[1].addProperty(new DocumentText(
				"This is document1 in category1."));
		documents[1].addProperty(new DocumentCategory("category1"));
		
		documents[2] = new Document(2);
		documents[2].addProperty(new DocumentName("document2"));
		documents[2].addProperty(new DocumentText(
				"This is document2 in category2."));
		documents[2].addProperty(new DocumentCategory("category2"));
	}

	@Test
	public void testCsvImporter() {
		CSVDocumentImporter importer = new CSVDocumentImporter();
		importer.addPropertyToRead(DocumentName.class, 0);
		importer.addPropertyToRead(DocumentText.class, 2);
		importer.addPropertyToRead(DocumentCategory.class, 3);
		importer.setDocumentStartId(0);
		try {
			importer.openCsvFile(csvFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}

		Document document = importer.getNextDocument();
		int id;
		while (document != null) {
			id = document.getDocumentId();
			Assert.assertEquals(documents[id].getProperty(DocumentName.class)
					.getValue(), document.getProperty(DocumentName.class)
					.getValue());
			Assert.assertEquals(documents[id].getProperty(DocumentText.class)
					.getValue(), document.getProperty(DocumentText.class)
					.getValue());
			Assert.assertEquals(
					documents[id].getProperty(DocumentCategory.class)
							.getValue(),
					document.getProperty(DocumentCategory.class).getValue());
			document = importer.getNextDocument();
		}
	}
}
