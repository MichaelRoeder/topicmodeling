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
package org.dice_research.topicmodeling.preprocessing.docsupplier;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.dice_research.topicmodeling.io.CSVDocumentImporter;
import org.dice_research.topicmodeling.io.CSVFileProcessor;
import org.dice_research.topicmodeling.io.FolderReader;
import org.dice_research.topicmodeling.io.CSVDocumentImporter.CSVDocumentColumnBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DocumentSupplierFactory {

    private static final Logger logger = LoggerFactory.getLogger(DocumentSupplierFactory.class);

    public static CSVDocumentImporter createCSVImporter(String filename, CSVDocumentColumnBinding... bindings) {
        CSVDocumentImporter csvImporter = new CSVDocumentImporter();
        for (int i = 0; i < bindings.length; ++i) {
            csvImporter.addPropertyToRead(bindings[i]);
        }

        try {
            csvImporter.openCsvFile(new File(filename), CSVFileProcessor.SEPARATOR, '"', '\'', 1);
        } catch (FileNotFoundException e) {
            logger.error("Couldn't create CSVDocumentImporter.", e);
        }
        return csvImporter;
    }

//    public static HTMLStripperWrapper createDecoratingHTMLStripper(DocumentSupplier documentSource) {
//        return new HTMLStripperWrapper(documentSource);
//    }

    public static FolderReader createFolderReader(String folderName, boolean folderNamesAreCategories) {
        FolderReader folderReader = new FolderReader(new File(folderName));
        folderReader.setUseFolderNameAsCategory(folderNamesAreCategories);
        return folderReader;
    }

    public static FolderReader createFolderReader(String folderName, String fileNameFilter,
            boolean folderNamesAreCategories) {
        FolderReader folderReader = new FolderReader(new File(folderName), new RegexFileFilter(fileNameFilter));
        folderReader.setUseFolderNameAsCategory(folderNamesAreCategories);
        return folderReader;
    }
}
