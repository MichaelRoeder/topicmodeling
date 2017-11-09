package org.dice_research.topicmodeling.wikipedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.WikipediaDumpReader;
import org.junit.Assert;
import org.junit.Test;

public class WikipediaDumpReaderTest {

    @Test
    public void test() throws FileNotFoundException, URISyntaxException {
        URL wikipediaTestDump = this.getClass().getClassLoader().getResource("wikipedia_test_dump.xml");
        WikipediaDumpReader reader = WikipediaDumpReader.createReader(new File(wikipediaTestDump.toURI()));
        
        Document document;
        DocumentName name;
        DocumentText text;

        document = reader.getNextDocument();
        Assert.assertNotNull(document);
        name = document.getProperty(DocumentName.class);
        Assert.assertEquals("AccessibleComputing", name.getValue());
        text = document.getProperty(DocumentText.class);
        Assert.assertEquals("#REDIRECT [[Computer accessibility]] {{R from CamelCase}}", text.getValue());

        document = reader.getNextDocument();
        Assert.assertNotNull(document);
        name = document.getProperty(DocumentName.class);
        Assert.assertEquals("Test Document 2", name.getValue());
        text = document.getProperty(DocumentText.class);
        Assert.assertEquals("This is just a small \"Test Document\" for testing the reader's ability of mapping something like &amp; --> &.", text.getValue());
        
        document = reader.getNextDocument();
        Assert.assertNull(document);
    }
}
