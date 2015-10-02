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
package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.util.Iterator;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCategory;
import org.aksw.simba.topicmodeling.utils.doc.DocumentName;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FolderReader implements DocumentSupplier {

    protected File documentFolder;
    protected Iterator<File> iterator;
    protected SimpleDocSupplierFromFile simpleFactory;
    protected boolean useFolderNameAsCategory = false;

    public FolderReader() {
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    public FolderReader(File documentFolder) {
        setDocumentFolder(documentFolder);
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    public FolderReader(File documentFolder, IOFileFilter filter) {
        setDocumentFolder(documentFolder, filter);
        simpleFactory = new SimpleDocSupplierFromFile();
    }

    /**
     * Set the value of documentFolder
     * 
     * @param documentFolder
     *            the new value of documentFolder
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
        // simpleFactory.createDocumentAdHoc(docFile);
        simpleFactory.createRawDocumentAdHoc(docFile);
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
