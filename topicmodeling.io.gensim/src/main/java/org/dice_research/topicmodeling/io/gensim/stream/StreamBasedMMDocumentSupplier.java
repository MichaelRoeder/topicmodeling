package org.dice_research.topicmodeling.io.gensim.stream;

import java.io.IOException;
import java.io.Reader;
import java.util.function.IntUnaryOperator;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentWordCounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

import au.com.bytecode.opencsv.CSVReader;

/**
 * A class that is able to stream a Gensim corpus. Note that by default, the
 * class will reduce the word Ids by 1 since the Gensim matrices seem to start
 * with word Id 1 instead of 0.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class StreamBasedMMDocumentSupplier implements DocumentSupplier, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamBasedMMDocumentSupplier.class);

    private static final char SEPARATOR = ' ';
    private static final boolean USE_DOCUMENT_IDS_FROM_FILE_DEFAULT = true;
    private static final int DOC_ID_INDEX = 0;
    private static final int WORD_ID_INDEX = 1;
    private static final int WORD_COUNT_INDEX = 2;

    private boolean useDocumentIdsFromFile;
    private CSVReader reader;
    private Document document;
    private int documentCount = 0;
    private int nextDocumentId;
    private int[] lastIntTriple;
    /**
     * The ID of the column in the file in which the document ID can be found.
     */
    protected int docIdColumnId;
    /**
     * The ID of the column in the file in which the word ID can be found.
     */
    protected int wordIdColumnId;
    /**
     * The ID of the column in the file in which the word count can be found.
     */
    protected int wordCountColumnId;
    protected int highestColumnId;
    /**
     * Number of header lines that should be skipped before starting to parse the
     * TSV file.
     */
    protected int headerLines = 0;
    /**
     * Transformation that is applied to document Ids.
     */
    protected IntUnaryOperator docIdTransformation = IntUnaryOperator.identity();
    /**
     * Transformation that is applied to word Ids. By default, the Ids are reduced
     * by 1 to match a dictionary that starts with 0.
     */
    protected IntUnaryOperator wordIdTransformation = (i) -> i - 1;
    /**
     * Transformation that is applied to word counts.
     */
    protected IntUnaryOperator wordCountTransformation = IntUnaryOperator.identity();

    private StreamBasedMMDocumentSupplier(CSVReader reader, int docIdColumnId, int wordIdColumnId,
            int wordCountColumnId, boolean useDocumentIdsFromFile) {
        this.useDocumentIdsFromFile = useDocumentIdsFromFile;
        this.reader = reader;
        this.docIdColumnId = docIdColumnId;
        this.wordIdColumnId = wordIdColumnId;
        this.wordCountColumnId = wordCountColumnId;
        this.highestColumnId = Math.max(Math.max(docIdColumnId, wordIdColumnId), wordCountColumnId);
    }

    @Override
    public Document getNextDocument() {
        if (reader != null) {
            // If this is the first document
            if (documentCount == 0) {
                try {
                    // Skip the first lines as configured
                    int count = 0;
                    while (count < headerLines) {
                        reader.readNext();
                        ++count;
                    }
                } catch (IOException e) {
                    LOGGER.warn("Got an IOException when trying to remove the beginning of the matrix. Moving on.", e);
                }
            }
            document = readDocument();
            if (document != null) {
                Document nextDocument = document;
                document = null;
                if (!useDocumentIdsFromFile) {
                    nextDocument.setDocumentId(getNextDocumentId());
                }
                ++documentCount;
                if (LOGGER.isInfoEnabled() && ((documentCount % 1000) == 0)) {
                    LOGGER.info("Read the " + documentCount + "th document from MM file.");
                }
                return nextDocument;
            } else {
                // The parser has reached the end of the file
                IOUtils.closeQuietly(reader);
                reader = null;
            }
        }
        return null;
    }

    /**
     * Determines the row with the triple comprising a document Id, word Id and word
     * count. It reuses the given int array as object to return the three ints. If
     * this is not provided, a new array is created.
     * 
     * @param reusedTriple an int array of size >=3 that is reused to return the
     *                     read numbers
     * @return an int array containing the 3 read values or {@code null} if the
     *         reader reached the end
     * @throws IOException if an IO error occurs
     */
    private int[] readNextIntTriple(int[] reusedTriple) throws IOException {
        int[] result = reusedTriple;
        while (true) {
            String[] line = reader.readNext();
            // If this line has an issue (i.e., it is too short) try to read the next line
            while ((line != null) && (line.length <= highestColumnId)) {
                line = reader.readNext();
            }
            // If we have reached the end of the file, return null
            if (line == null) {
                return null;
            }
            try {
                if (result == null) {
                    result = new int[3];
                }
                // Get the Id of this document
                result[DOC_ID_INDEX] = docIdTransformation.applyAsInt(Integer.parseInt(line[docIdColumnId]));
                result[WORD_ID_INDEX] = wordIdTransformation.applyAsInt(Integer.parseInt(line[wordIdColumnId]));
                result[WORD_COUNT_INDEX] = wordCountTransformation
                        .applyAsInt(Integer.parseInt(line[wordCountColumnId]));
                return result;
            } catch (NumberFormatException e) {
                LOGGER.error("Couldn't parse id in line " + line.toString() + ". It will be ignored.", e);
            }
        }
    }

    /**
     * Reads the next document from the reader.
     * 
     * @return the next document or {@code null} if either the end of the line has
     *         been reached or if an error occurs.
     */
    private Document readDocument() {
        try {
            int documentId = -1;
            DocumentWordCounts countProperty = new DocumentWordCounts();
            IntIntOpenHashMap counts = countProperty.getWordCounts();
            int[] currentLine = readNextIntTriple(new int[3]);
            // Get the document Id of the document that we would like to read next
            // Get it from the last triple if this is not null
            if (lastIntTriple != null) {
                documentId = lastIntTriple[DOC_ID_INDEX];
                // We should also add the counts for the word that this line represents
                counts.put(lastIntTriple[WORD_ID_INDEX], lastIntTriple[WORD_COUNT_INDEX]);
            } else {
                // We just started to read the file and, hence, we have to get the document ID
                // from the first line that we just read
                if (currentLine == null) {
                    // Ok, this file seems to be empty or something similar...
                    return null;
                } else {
                    documentId = currentLine[DOC_ID_INDEX];
                }
            }
            // As long as we haven't reached the end of the file and we still have the same
            // document as before
            while ((currentLine != null) && (currentLine[DOC_ID_INDEX] == documentId)) {
                // Add the current word to the counts
                counts.put(currentLine[WORD_ID_INDEX], currentLine[WORD_COUNT_INDEX]);
                // Read the next int triple (and reuse the array that we do not need anymore)
                currentLine = readNextIntTriple(currentLine);
            }
            // The next triple belongs to the next document. So we should store it
            lastIntTriple = currentLine;
            // If we were able to read counts, generate a document object and return
            if (counts.size() > 0) {
                Document document = new Document(documentId);
                document.addProperty(countProperty);
                return document;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error while reading next document. Returning null.", e);
            return null;
        }
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        nextDocumentId = documentStartId;
    }

    protected int getNextDocumentId() {
        int tempId = nextDocumentId;
        ++nextDocumentId;
        return tempId;
    }

    public void setHeaderLines(int headerLines) {
        this.headerLines = headerLines;
    }

    public int getHeaderLines() {
        return headerLines;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(reader);
    }

    /**
     * @param docIdTransformation the transformation that is applied to document Ids
     */
    public void setDocIdTransformation(IntUnaryOperator docIdTransformation) {
        this.docIdTransformation = docIdTransformation;
    }

    /**
     * @param wordIdTransformation the transformation that is applied to word Ids
     */
    public void setWordIdTransformation(IntUnaryOperator wordIdTransformation) {
        this.wordIdTransformation = wordIdTransformation;
    }

    /**
     * @param wordCountTransformation the transformation that is applied to word
     *                                counts
     */
    public void setWordCountTransformation(IntUnaryOperator wordCountTransformation) {
        this.wordCountTransformation = wordCountTransformation;
    }

    public static StreamBasedMMDocumentSupplier createReader(Reader reader, int docIdColumnId, int wordIdColumnId,
            int wordCountColumnId) {
        return new StreamBasedMMDocumentSupplier(new CSVReader(reader, SEPARATOR), docIdColumnId, wordIdColumnId,
                wordCountColumnId, USE_DOCUMENT_IDS_FROM_FILE_DEFAULT);
    }

    public static StreamBasedMMDocumentSupplier createReader(Reader reader, int docIdColumnId, int wordIdColumnId,
            int wordCountColumnId, boolean useDocumentIdsFromFile) {
        return new StreamBasedMMDocumentSupplier(new CSVReader(reader, SEPARATOR), docIdColumnId, wordIdColumnId,
                wordCountColumnId, useDocumentIdsFromFile);
    }

}
