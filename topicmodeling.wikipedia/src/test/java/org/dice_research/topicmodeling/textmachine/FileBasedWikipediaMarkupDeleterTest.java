package org.dice_research.topicmodeling.textmachine;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FileBasedWikipediaMarkupDeleterTest {

    private static final String WIKIPEDIA_FILES_FOLDER_NAME = "WikipediaExamples/";
    private static final int NUMBER_OF_TESTS_FOR_PERFORMANCE = 1000;

    // private static final String WIKIPEDIA_MARKUPS[] = { "{{", "}}", "[[", "]]", "==", "''", "&nbsp;" };
    private static final String WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS[] = { "{{", "}}", "[[", "]]", "==", "''", "&nbsp;",
            "align", "valign", "style" };

    @Parameters
    public static List<Object[]> getData()
    {
        return Arrays
                .asList(new Object[][] { { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample1.txt",
                        new String[] { "{{", "}}" }, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample2.txt",
                                new String[] { "align", "valign" }, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample3.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample4.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample5.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample6.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample7.txt",
                                new String[] { "{{", "}}", "[[", "]]", "==", "''", "&nbsp;",
                                        "align", "valign" }, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "WikipediaExample8.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, null },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "Redirect1.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, "Test" },
                        { WIKIPEDIA_FILES_FOLDER_NAME + "Redirect2.txt",
                                WIKIPEDIA_MARKUPS_WITH_STYLE_TERMS, "Test" },
                });
    }

    private String filePath;
    private String patternsToCheck[];
    private String expectedText;

    public FileBasedWikipediaMarkupDeleterTest(String filePath, String[] patternsToCheck, String expectedText) {
        this.filePath = filePath;
        this.patternsToCheck = patternsToCheck;
        this.expectedText = expectedText;
    }

    @Test
    public void test() throws IOException, URISyntaxException {
        String cleanText = testFileContent(filePath);
        if (expectedText != null) {
            Assert.assertTrue(cleanText.trim().equals(expectedText.trim()));
        }
    }

    @Test
    public void performanceTest() throws IOException, URISyntaxException {
        performanceTest(filePath);
    }

    public String testFileContent(String fileName) throws IOException,
            URISyntaxException {
        String text = FileUtils.readFileToString(new File(this.getClass()
                .getClassLoader().getResource(fileName).toURI()));

        Document document = new Document();
        document.addProperty(new DocumentText(text));

        StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine();
        text = deleter.getCleanText(document.getProperty(DocumentText.class)
                .getText());

        // Assert.assertTrue("Text (\"" + fileName + "\") contains {{: \n" + text, !text.contains("{{"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains }}: \n" + text, !text.contains("}}"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains [[: \n" + text, !text.contains("[["));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains ]]: \n" + text, !text.contains("]]"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains ==: \n" + text, !text.contains("=="));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains '': \n" + text, !text.contains("''"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains &nbsp;: \n" + text, !text.contains("&nbsp;"));
        // if (checkForStyleTerms) {
        // Assert.assertTrue("Text (\"" + fileName + "\") contains \"align\": \n" + text, !text.contains("align"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains \"valign\": \n" + text, !text.contains("valign"));
        // Assert.assertTrue("Text (\"" + fileName + "\") contains \"style\": \n" + text, !text.contains("style"));
        // }
        for (int i = 0; i < patternsToCheck.length; ++i) {
            Assert.assertTrue("Text (\"" + fileName + "\") contains " + patternsToCheck[i] + ": \n" + text,
                    !text.contains(patternsToCheck[i]));
        }

        return text;
    }

    public void performanceTest(String fileName) throws IOException,
            URISyntaxException {
        String text = FileUtils.readFileToString(new File(this.getClass()
                .getClassLoader().getResource(fileName).toURI()));

        Document document = new Document();
        long time = System.nanoTime();
        StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine();
        for (int i = 0; i < NUMBER_OF_TESTS_FOR_PERFORMANCE; ++i) {
            document.addProperty(new DocumentText(text));
            deleter.getCleanText(document.getProperty(DocumentText.class)
                    .getText());
        }
        time = System.nanoTime() - time;
        System.out.println(time + " nanos for " + NUMBER_OF_TESTS_FOR_PERFORMANCE
                + " documents with " + text.length() + " chars. ("
                + ((double) time / (double) (text.length() * NUMBER_OF_TESTS_FOR_PERFORMANCE))
                + " nanos per char)");
    }
}
