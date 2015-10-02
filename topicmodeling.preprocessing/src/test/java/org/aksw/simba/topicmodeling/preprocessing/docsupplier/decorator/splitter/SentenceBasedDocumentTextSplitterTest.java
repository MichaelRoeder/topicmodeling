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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.splitter;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.lang.SentenceSplitterFactory;
import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggerFactory;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecoratorTest;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.PosTaggingSupplierDecorator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.splitter.SentenceBasedDocumentTextSplitter;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class SentenceBasedDocumentTextSplitterTest extends AbstractDocumentSupplierDecoratorTest {

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {
                        "This is a test text. This is the second sentence of this text. t e s t.",
                        new String[] { "This is a test text.", "This is the second sentence of this text.", "t e s t." },
                        new String[][] { { "This", "test", "text" },
                        { "This", "the", "second", "sentence", "this", "text" }, {} } },
                {
                        "     According to a ``CNN Poll'' to key reason for Clinton's low\n"
                                + " approval rating is people are angry about him not moving fast enough\n"
                                + " on gays in the military.  I just burst out laughing when I heard this;\n"
                                + " what planet do these CNN people live on anyway?\n\n"
                                + " Jason C. Austin",
                        new String[] { "According to a ``CNN Poll'' to key reason for Clinton's low\n"
                                + " approval rating is people are angry about him not moving fast enough\n"
                                + " on gays in the military.", "I just burst out laughing when I heard this;\n"
                                + " what planet do these CNN people live on anyway?", "Jason C. Austin" },
                        new String[][] {
                        { "According", "CNN", "Poll", "key", "reason", "for", "Clinton's", "low", "approval",
                                "rating", "people", "are", "angry", "about", "him", "not", "moving", "fast",
                                "enough", "gays", "the", "military" },
                        { "just", "burst", "out", "laughing", "when", "heard", "this", "what", "planet",
                                "these", "CNN", "people", "live", "anyway" }, { "Jason", "Austin" } } }
        };
        return Arrays.asList(data);
    }

    private String expectedSentences[];
    private String expectedTermWordForms[][];

    public SentenceBasedDocumentTextSplitterTest(String text, String expectedSentences[],
            String expectedTermWordForms[][]) {
        super(new DocumentText(text));
        this.expectedSentences = expectedSentences;
        this.expectedTermWordForms = expectedTermWordForms;
    }

    @Test
    public void testWithoutTerms() {
        DocumentSupplier supplier = new SentenceBasedDocumentTextSplitter(this,
                SentenceSplitterFactory.createSentenceSplitter(Language.ENG));

        Document document;
        DocumentText text;
        for (int i = 0; i < expectedSentences.length; ++i) {
            document = supplier.getNextDocument();
            Assert.assertNotNull(document);

            text = document.getProperty(DocumentText.class);
            Assert.assertNotNull(text);
            Assert.assertEquals(expectedSentences[i], text.getText());
        }
    }

    @Test
    public void testWithTerms() {
        DocumentSupplier supplier = new PosTaggingSupplierDecorator(this,
                PosTaggerFactory.getPosTaggingStep(Language.ENG, new PosTaggingTermFilter() {
                    @Override
                    public boolean isTermGood(Term term) {
                        return term.getWordForm().length() > 2;
                    }
                }));
        supplier = new SentenceBasedDocumentTextSplitter(supplier,
                SentenceSplitterFactory.createSentenceSplitter(Language.ENG));

        Document document;
        DocumentText text;
        TermTokenizedText tttext;
        for (int i = 0; i < expectedSentences.length; ++i) {
            document = supplier.getNextDocument();
            Assert.assertNotNull(document);

            text = document.getProperty(DocumentText.class);
            Assert.assertNotNull(text);
            Assert.assertEquals(expectedSentences[i], text.getText());

            tttext = document.getProperty(TermTokenizedText.class);
            Assert.assertNotNull(tttext);
            Assert.assertTrue(tttext.getTermTokenizedText().toString() + " has not the expected size of the array "
                    + Arrays.toString(expectedTermWordForms[i]), expectedTermWordForms[i].length == tttext
                    .getTermTokenizedText().size());
            for (int j = 0; j < expectedTermWordForms[i].length; ++j) {
                Assert.assertEquals(expectedTermWordForms[i][j], tttext.getTermTokenizedText().get(j).getWordForm());
            }
        }
    }
}
