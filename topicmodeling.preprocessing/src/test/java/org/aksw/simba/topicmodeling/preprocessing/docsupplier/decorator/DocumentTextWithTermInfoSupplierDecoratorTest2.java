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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.DocumentTextWithTermInfoCreatingSupplierDecorator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.DocumentTextWithTermInfoParsingSupplierDecorator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.PropertyRemovingSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



@RunWith(Parameterized.class)
public class DocumentTextWithTermInfoSupplierDecoratorTest2 extends AbstractDocumentSupplierDecoratorTest {

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { "This|is|a|stupid|text.", new Term[] { new Term("This|is|a|stupid|text", "this|is|a|stupid|text") } },
                { "This|is|a|stupid|text.", new Term[] { new Term("This", "this"), new Term("is", "is"),
                        new Term("a", "a"), new Term("stupid", "stupid"), new Term("text", "text") } },
                {
                        "This\\ \\is\\ [a] stu[p]id t\\]e\\[xt\\\\.",
                        new Term[] { new Term("This\\", "this\\"), new Term("\\is\\", "\\is\\"),
                                new Term("[a]", "[a]"),
                                new Term("stu[p]id", "stu[p]id"), new Term("t\\]e\\[xt\\\\", "t\\]e\\[xt\\\\") } }
        };
        return Arrays.asList(data);
    }

    private String origText;
    private Term origTerms[];

    public DocumentTextWithTermInfoSupplierDecoratorTest2(String text, Term terms[]) {
        super(new DocumentText(text), new TermTokenizedText(terms));
        origText = text;
        origTerms = terms;
    }

    @Test
    public void test() {
        DocumentSupplier supplier = new DocumentTextWithTermInfoCreatingSupplierDecorator(this);
        supplier = new PropertyRemovingSupplierDecorator(supplier, DocumentText.class);
        supplier = new PropertyRemovingSupplierDecorator(supplier, TermTokenizedText.class);
        supplier = new DocumentTextWithTermInfoParsingSupplierDecorator(supplier);

        Document document = supplier.getNextDocument();
        Assert.assertNotNull(document);
        DocumentText text = document.getProperty(DocumentText.class);
        Assert.assertNotNull(text);
        TermTokenizedText tttext = document.getProperty(TermTokenizedText.class);
        Assert.assertNotNull(tttext);

        Assert.assertEquals(origText, text.getText());

        Assert.assertTrue(origTerms.length == tttext.getTermTokenizedText().size());
        for (int i = 0; i < origTerms.length; ++i) {
            Assert.assertEquals(origTerms[i], tttext.getTermTokenizedText().get(i));
        }
    }
}
