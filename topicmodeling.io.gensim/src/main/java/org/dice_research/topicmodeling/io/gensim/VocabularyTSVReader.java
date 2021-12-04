package org.dice_research.topicmodeling.io.gensim;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;

import org.dice_research.topicmodeling.utils.vocabulary.SimpleVocabulary;
import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Simple implementation of a class that reads a vocabulary from a TSV (or CSV)
 * file. The implementation assumes that there is one word per line with its ID.
 * The IDs of the word and word ID column can be set with the constructor, i.e.,
 * if more data (more columns) are in the given file, they will be ignored. The
 * class can be configured to skip a number of header lines using the
 * {@link #setHeaderLines(int)} method.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class VocabularyTSVReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(VocabularyTSVReader.class);

    /**
     * The ID of the column in the file in which the word can be found.
     */
    protected int wordColumnId;
    /**
     * The ID of the column in the file in which the word ID can be found.
     */
    protected int wordIdColumnId;
    /**
     * The separator character that is used to separated the columns in the file.
     */
    protected char separatorChar = '\t';
    /**
     * Number of header lines that should be skipped before starting to parse the
     * TSV file.
     */
    protected int headerLines = 0;
    /**
     * Transformation that is applied to word Ids.
     */
    protected IntUnaryOperator wordIdTransformation;

    /**
     * Constructor.
     * 
     * @param wordColumnId   The ID of the column in the file in which the word can
     *                       be found.
     * @param wordIdColumnId The ID of the column in the file in which the word ID
     *                       can be found.
     */
    public VocabularyTSVReader(int wordColumnId, int wordIdColumnId) {
        this(wordColumnId, wordIdColumnId, IntUnaryOperator.identity());
    }

    /**
     * Constructor.
     * 
     * @param wordColumnId         The ID of the column in the file in which the
     *                             word can be found.
     * @param wordIdColumnId       The ID of the column in the file in which the
     *                             word ID can be found.
     * @param wordIdTransformation Transformation operator that is applied to word
     *                             Ids.
     */
    public VocabularyTSVReader(int wordColumnId, int wordIdColumnId, IntUnaryOperator wordIdTransformation) {
        this.wordColumnId = wordColumnId;
        this.wordIdColumnId = wordIdColumnId;
        this.wordIdTransformation = wordIdTransformation;
    }

    /**
     * Reads a vocabulary from the given file.
     * 
     * @param vocabularyFile The file from which the vocabulary will be read
     * @return The newly created vocabulary or {@code null} if an error occurred.
     */
    public Vocabulary readVocabulary(File vocabularyFile) {
        try (FileReader reader = new FileReader(vocabularyFile)) {
            return readVocabulary(reader);
        } catch (IOException e) {
            LOGGER.error("Error while reading dictionary. Returning null.", e);
        }
        return null;
    }

    /**
     * Reads a vocabulary from the given reader. Note that the method may consume
     * all content and close the reader.
     * 
     * @param reader The {@link Reader} from which the vocabulary will be read
     * @return The newly created vocabulary or {@code null} if an error occurred.
     */
    public Vocabulary readVocabulary(Reader reader) {
        try (CSVReader csvReader = new CSVReader(reader, separatorChar);) {
            int highestId = Math.max(wordColumnId, wordIdColumnId);
            Map<String, Integer> wordIndexMap = new HashMap<>();
            // Start to read the file
            String[] line = csvReader.readNext();
            // First, skip header lines if there are any
            int skippedLines = 0;
            while ((line != null) && (skippedLines < headerLines)) {
                ++skippedLines;
                line = csvReader.readNext();
            }
            // Read words from file until the file ends
            int id;
            while (line != null) {
                if (line.length > highestId) {
                    try {
                        id = Integer.parseInt(line[wordIdColumnId]);
                        wordIndexMap.put(line[wordColumnId], wordIdTransformation.applyAsInt(id));
                    } catch (NumberFormatException e) {
                        LOGGER.error("Couldn't parse the word ID in column " + wordIdColumnId + " in the line "
                                + Arrays.toString(line) + ". The line will be ignored.", e);
                    }
                }
                line = csvReader.readNext();
            }
            LOGGER.info("Read {} words from the given file.", wordIndexMap.size());
            // If the reading was successful, create the vocabulary object
            return new SimpleVocabulary(wordIndexMap);
        } catch (IOException e) {
            LOGGER.error("Error while reading dictionary. Returning null.", e);
        }
        return null;
    }

    /**
     * @return the separatorChar
     */
    public char getSeparatorChar() {
        return separatorChar;
    }

    /**
     * @param separatorChar the separatorChar to set
     */
    public void setSeparatorChar(char separatorChar) {
        this.separatorChar = separatorChar;
    }

    /**
     * @return the headerLines
     */
    public int getHeaderLines() {
        return headerLines;
    }

    /**
     * @param headerLines the headerLines to set
     */
    public void setHeaderLines(int headerLines) {
        this.headerLines = headerLines;
    }

}
