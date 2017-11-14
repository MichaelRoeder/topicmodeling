/**
 * This file is part of topicmodeling.preprocessing.
 *
 * topicmodeling.preprocessing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.preprocessing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.preprocessing.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.topicmodeling.lang.postagging.StanfordPipelineWrapper;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



@RunWith(Parameterized.class)
public class DocumentTextWithTermInfoSupplierDecoratorTest extends AbstractDocumentSupplierDecoratorTest {

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { "This is a test text." },
                { "     According to a ``CNN Poll'' to key reason for Clinton's low\n"
                        + " approval rating is people are angry about him not moving fast enough\n"
                        + " on gays in the military.  I just burst out laughing when I heard this;\n"
                        + " what planet do these CNN people live on anyway?\n\n"
                        + " Jason C. Austin" }
        };
        return Arrays.asList(data);
    }

    public DocumentTextWithTermInfoSupplierDecoratorTest(String text) {
        super(new DocumentText(text));
    }

    @Test
    public void test() {
        DocumentSupplier supplier = new PosTaggingSupplierDecorator(this,
                StanfordPipelineWrapper.createDefaultStanfordPipelineWrapper((term -> term.getWordForm().length() > 2)));
        SimpleDocumentPropertyStorage<DocumentText> origDocTextStorage = new SimpleDocumentPropertyStorage<DocumentText>(
                supplier, DocumentText.class);
        supplier = origDocTextStorage;
        SimpleDocumentPropertyStorage<TermTokenizedText> origTermTextStorage = new SimpleDocumentPropertyStorage<TermTokenizedText>(
                supplier, TermTokenizedText.class);
        supplier = origTermTextStorage;
        supplier = new DocumentTextWithTermInfoCreatingSupplierDecorator(supplier);
        supplier = new PropertyRemovingSupplierDecorator(supplier, DocumentText.class);
        supplier = new PropertyRemovingSupplierDecorator(supplier, TermTokenizedText.class);
        supplier = new DocumentTextWithTermInfoParsingSupplierDecorator(supplier);

        Document document = supplier.getNextDocument();
        Assert.assertNotNull(document);
        DocumentText text = document.getProperty(DocumentText.class);
        Assert.assertNotNull(text);
        TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
        Assert.assertNotNull(tttext);

        DocumentText originalText = origDocTextStorage.getProperty();
        Assert.assertTrue(originalText != text);
        Assert.assertEquals(originalText, text);

        TermTokenizedText originalTTText = origTermTextStorage.getProperty();
        Assert.assertTrue(originalTTText != tttext);
        Assert.assertTrue(originalTTText.getTermTokenizedText().size() == tttext.getTermTokenizedText().size());
        for (int i = 0; i < originalTTText.getTermTokenizedText().size(); ++i) {
            Assert.assertEquals(originalTTText.getTermTokenizedText().get(i), tttext.getTermTokenizedText().get(i));
        }
    }
}
