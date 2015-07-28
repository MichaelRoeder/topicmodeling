package org.aksw.simba.topicmodeling.preprocessing.docsupplier;

import java.io.File;
import java.io.FileNotFoundException;

import org.aksw.simba.topicmodeling.io.CSVDocumentImporter;
import org.aksw.simba.topicmodeling.io.CSVFileProcessor;
import org.aksw.simba.topicmodeling.io.FolderReader;
import org.aksw.simba.topicmodeling.io.CSVDocumentImporter.CSVDocumentColumnBinding;
import org.apache.commons.io.filefilter.RegexFileFilter;
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
