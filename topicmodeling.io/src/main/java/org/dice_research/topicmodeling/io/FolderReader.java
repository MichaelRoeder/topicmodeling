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
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.dice_research.topicmodeling.io.factories.FileBasedDocumentFactory;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;
import org.dice_research.topicmodeling.utils.doc.DocumentName;

public class FolderReader implements DocumentSupplier {

    protected File documentFolder;
    protected Iterator<File> iterator;
    protected SimpleDocSupplierFromFile simpleFactory;
    protected boolean useFolderNameAsCategory = false;

    public FolderReader() {
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    public FolderReader(FileBasedDocumentFactory documentFactory) {
        simpleFactory = new SimpleDocSupplierFromFile(documentFactory);
    }

    public FolderReader(File documentFolder) {
        setDocumentFolder(documentFolder);
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    public FolderReader(FileBasedDocumentFactory documentFactory, File documentFolder) {
        setDocumentFolder(documentFolder);
        simpleFactory = new SimpleDocSupplierFromFile(documentFactory);
    }

    public FolderReader(File documentFolder, IOFileFilter filter) {
        setDocumentFolder(documentFolder, filter);
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    public FolderReader(FileBasedDocumentFactory documentFactory, File documentFolder, IOFileFilter filter) {
        setDocumentFolder(documentFolder, filter);
        simpleFactory = new SimpleDocSupplierFromFile(documentFactory);
    }

    /**
     * Set the value of documentFolder
     * 
     * @param documentFolder the new value of documentFolder
     */
    public void setDocumentFolder(File documentFolder) {
        setDocumentFolder(documentFolder, TrueFileFilter.INSTANCE);
    }

    public void setDocumentFolder(File documentFolder, IOFileFilter filter) {
        this.documentFolder = documentFolder;
        iterator = FileUtils.iterateFiles(documentFolder, filter, TrueFileFilter.INSTANCE);
    }

    public boolean isFolderNameUsedAsCategory() {
        return useFolderNameAsCategory;
    }

    public void setUseFolderNameAsCategory(boolean flag) {
        useFolderNameAsCategory = flag;
    }

    /**
     * Get the value of documentFolder
     * 
     * @return the value of documentFolder
     */
    public File getDocumentFolder() {
        return documentFolder;
    }

    @Override
    public Document getNextDocument() {
        Document document = null;
        File docFile;
        if (!iterator.hasNext()) {
            return null;
        }
        docFile = iterator.next();
        simpleFactory.createAdHoc(docFile);
        document = simpleFactory.getNextDocument();
        document.addProperty(new DocumentName(docFile.getName()));

        if (useFolderNameAsCategory) {
            String category = docFile.getParent().replace(documentFolder.getPath(), "");
            if (category.startsWith(File.separator)) {
                category = category.substring(File.separator.length());
            }
            document.addProperty(new DocumentCategory(category));
        }

        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        simpleFactory.setDocumentStartId(documentStartId);
    }
}
