package org.dice_research.topicmodeling.wikipedia;

import java.util.function.Function;

import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.junit.Assert;
import org.junit.Test;

public class WikipediaMarkupDeletingDocumentSupplierDecoratorTest {

    @Test
    public void test() {
        Document document;
        DocumentText text;

        Function<Document, Document> instance = new WikipediaMarkupDeletingDocumentSupplierDecorator(null);

        document = new Document();
        document = instance.apply(document);
        Assert.assertNotNull("Document should not become null", document);
        text = document.getProperty(DocumentText.class);
        Assert.assertNull("DocumentText property should not be created", text);

        document = new Document();
        document.addProperty(new DocumentText("[[Text]]"));
        document = instance.apply(document);
        Assert.assertNotNull("Document should not become null", document);
        text = document.getProperty(DocumentText.class);
        Assert.assertNotNull("DocumentText property should not become null", text);
        Assert.assertTrue("DocumentText value should be without markup", text.getText().matches("\\s*Text\\s*"));
    }
}
