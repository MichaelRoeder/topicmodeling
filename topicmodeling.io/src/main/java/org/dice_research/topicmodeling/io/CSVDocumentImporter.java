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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.ParseableDocumentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;


public class CSVDocumentImporter extends AbstractDocumentSupplier {

    private static final Logger logger = LoggerFactory.getLogger(CSVDocumentImporter.class);

    protected CSVReader reader = null;
    protected HashMap<Class<? extends ParseableDocumentProperty>, Integer> columnAssignment = new HashMap<Class<? extends ParseableDocumentProperty>, Integer>();
    // protected int documentIdColumn = -1;
    protected int maxColumnId = -1;

    public CSVDocumentImporter() {
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile) throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile));
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator) throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar) throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @param strictQuotes
     *            - sets if characters outside the quotes are ignored
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar, boolean strictQuotes)
            throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar, strictQuotes);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @param escape
     *            - the character to use for escaping a separator or quote
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar, char escape) throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar, escape);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @param escape
     *            - the character to use for escaping a separator or quote
     * @param line
     *            - the line number to skip for start reading
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar, char escape, int line)
            throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar, escape, line);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @param line
     *            - the line number to skip for start reading
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar, int line) throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar, line);
    }

    /**
     * 
     * @param csvFile
     *            - name and path of the CSV file
     * @param separator
     *            - the delimiter to use for separating entries
     * @param quotechar
     *            - the character to use for quoted elements
     * @param escape
     *            - the character to use for escaping a separator or quote
     * @param line
     *            - the line number to skip for start reading
     * @param strictQuotes
     *            - sets if characters outside the quotes are ignored
     * @throws FileNotFoundException
     */
    public void openCsvFile(File csvFile, char separator, char quotechar, char escape, int line, boolean strictQuotes)
            throws FileNotFoundException {
        reader = new CSVReader(new FileReader(csvFile), separator, quotechar, escape, line, strictQuotes);
    }

    // public void setColumnOfDocumentId(int documentIdColumn) {
    // this.documentIdColumn = documentIdColumn;
    // }

    @Override
    public Document getNextDocument() {
        if (reader == null) {
            return null;
        }

        String nextLine[];
        try {
            nextLine = reader.readNext();
        } catch (Exception e) {
            logger.error("Error while reading from CSV file.", e);
            return null;
        }

        if (nextLine == null) {
            closeCsvFile();
            return null;
        }
        if (nextLine.length <= maxColumnId) {
            logger.error("Only " + nextLine.length + " columns found in CSV file while " + maxColumnId
                    + " were expected.");
            closeCsvFile();
            return null;
        }

        int documentId;
        // if(documentIdColumn < 0)
        // {
        documentId = getNextDocumentId();
        // }
        // else
        // {
        // documentId = Integer.parseInt(nextLine[documentIdColumn]);
        // }
        Document document = new Document(documentId);

        Iterator<Class<? extends ParseableDocumentProperty>> iterator = columnAssignment.keySet().iterator();
        Class<? extends ParseableDocumentProperty> propertyClass = null;
        ParseableDocumentProperty property;
        int columnId;
        while (iterator.hasNext()) {
            try {
                propertyClass = iterator.next();
                columnId = columnAssignment.get(propertyClass);
                property = propertyClass.newInstance();
                property.parseValue(nextLine[columnId]);
                document.addProperty(property);
            } catch (Exception e) {
                logger.error("Error while creating new instance of " + propertyClass.getCanonicalName(), e);
            }
        }

        return document;
    }

    public void addPropertyToRead(Class<? extends ParseableDocumentProperty> propertyClass, int columnId) {
        columnAssignment.put(propertyClass, columnId);
        if (columnId > maxColumnId) {
            maxColumnId = columnId;
        }
    }

    public void addPropertyToRead(CSVDocumentColumnBinding binding) {
        addPropertyToRead(binding.documentProperty, binding.columnId);
    }

    public void closeCsvFile() {
        try {
            reader.close();
            reader = null;
        } catch (IOException e) {
            logger.warn("Exception while closing CSV file.", e);
        }
    }

    public static class CSVDocumentColumnBinding {
        public Class<? extends ParseableDocumentProperty> documentProperty;
        public int columnId;

        public CSVDocumentColumnBinding() {
        }

        public CSVDocumentColumnBinding(Class<? extends ParseableDocumentProperty> documentProperty, int columnId) {
            this.documentProperty = documentProperty;
            this.columnId = columnId;
        }
    }
}
