package org.dice_research.topicmodeling.io.gensim;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.dice_research.topicmodeling.utils.vocabulary.Vocabulary;
import org.junit.Assert;
import org.junit.Test;

public class VocabularyTSVReaderTest {

    @Test
    public void testFile1() throws IOException {
        Map<String, Integer> expectedMapping = new HashMap<>();
        expectedMapping.put("word1", 0);
        expectedMapping.put("word2", 1);
        expectedMapping.put("word3", 2);
        expectedMapping.put("word4", 3);

        try (Reader reader = new InputStreamReader(
                this.getClass().getClassLoader().getResourceAsStream("test_dictionary.tsv"), StandardCharsets.UTF_8);) {
            VocabularyTSVReader vReader = new VocabularyTSVReader(0, 1);
            Vocabulary vocabulary = vReader.readVocabulary(reader);
            checkVocab(vocabulary, expectedMapping);
        }
    }

    @Test
    public void testFile2() throws IOException {
        Map<String, Integer> expectedMapping = new HashMap<>();
        expectedMapping.put("zero", 0);
        expectedMapping.put("minus 14", 1);
        expectedMapping.put("[]{}\\??-strange-content", 2);
        expectedMapping.put("last", 3);
        expectedMapping.put("someWord", 4);

        try (Reader reader = new InputStreamReader(
                this.getClass().getClassLoader().getResourceAsStream("test_dictionary2.tsv"), StandardCharsets.UTF_8);) {
            VocabularyTSVReader vReader = new VocabularyTSVReader(2, 1);
            vReader.setHeaderLines(2);
            Vocabulary vocabulary = vReader.readVocabulary(reader);
            checkVocab(vocabulary, expectedMapping);
        }
    }

    protected void checkVocab(Vocabulary vocabulary, Map<String, Integer> expectedMapping) {
        String errorMessage = "The generated vocabulary " + vocabulary.toString() + " does not fit to the expected map "
                + expectedMapping.toString() + ".";
        Assert.assertEquals(errorMessage, expectedMapping.size(), vocabulary.size());
        for (String word : expectedMapping.keySet()) {
            Assert.assertEquals(errorMessage, expectedMapping.get(word), vocabulary.getId(word));
        }
    }
}
