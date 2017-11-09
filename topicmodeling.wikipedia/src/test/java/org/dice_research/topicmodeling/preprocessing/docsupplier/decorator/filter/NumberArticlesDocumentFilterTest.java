package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import java.util.Arrays;
import java.util.List;

import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter.NumberArticlesDocumentFilter;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NumberArticlesDocumentFilterTest {

    @Parameters
    public static List<Object[]> getData() {
        return Arrays
                .asList(new Object[][] {
                        { new Document(0, new DocumentProperty[] { new DocumentName("normaler Name") }), true },
                        { new Document(1, new DocumentProperty[] { new DocumentName("1") }), false },
                        { new Document(2, new DocumentProperty[] { new DocumentName("1945") }), false },
                        { new Document(3, new DocumentProperty[] { new DocumentName("68er") }), true }
                });
    }

    private Document document;
    private boolean expectedDecision;

    public NumberArticlesDocumentFilterTest(Document document, boolean expectedDecision) {
        this.document = document;
        this.expectedDecision = expectedDecision;
    }

    @Test
    public void test() {
        NumberArticlesDocumentFilter filter = new NumberArticlesDocumentFilter();
        Assert.assertEquals(expectedDecision, filter.isDocumentGood(document));
    }
}
