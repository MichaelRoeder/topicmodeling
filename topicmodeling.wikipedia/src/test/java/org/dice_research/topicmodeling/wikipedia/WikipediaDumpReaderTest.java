package org.dice_research.topicmodeling.wikipedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaArticleId;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaNamespace;
import org.dice_research.topicmodeling.wikipedia.doc.WikipediaRedirect;
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
        Assert.assertNotNull(document.getProperty(DocumentName.class));
        name = document.getProperty(DocumentName.class);
        Assert.assertEquals("AccessibleComputing", name.getValue());
        Assert.assertNotNull(document.getProperty(DocumentText.class));
        text = document.getProperty(DocumentText.class);
        Assert.assertEquals("#REDIRECT [[Computer accessibility]] {{R from CamelCase}}", text.getValue());
        Assert.assertNotNull(document.getProperty(WikipediaArticleId.class));
        Assert.assertEquals(10, document.getProperty(WikipediaArticleId.class).getArticleId());
        Assert.assertNotNull(document.getProperty(WikipediaNamespace.class));
        Assert.assertEquals(0, document.getProperty(WikipediaNamespace.class).getNamespaceId());
        Assert.assertNotNull(document.getProperty(WikipediaRedirect.class));
        Assert.assertEquals("Computer accessibility", document.getProperty(WikipediaRedirect.class).getArticleTitle());

        document = reader.getNextDocument();
        Assert.assertNotNull(document);
        name = document.getProperty(DocumentName.class);
        Assert.assertEquals("Test Document 2", name.getValue());
        text = document.getProperty(DocumentText.class);
        Assert.assertEquals("This is just a small \"Test Document\" for testing the reader's ability of mapping something like &amp; --> &.", text.getValue());
        Assert.assertNotNull(document.getProperty(WikipediaArticleId.class));
        Assert.assertEquals(12, document.getProperty(WikipediaArticleId.class).getArticleId());
        Assert.assertNotNull(document.getProperty(WikipediaNamespace.class));
        Assert.assertEquals(0, document.getProperty(WikipediaNamespace.class).getNamespaceId());
        Assert.assertNull(document.getProperty(WikipediaRedirect.class));
        
        document = reader.getNextDocument();
        Assert.assertNull(document);
    }
}
