/**
 * This file is part of topicmodeling.io.
 *
 * topicmodeling.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.io.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.io.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractCorpusIOTest {

    private CorpusReader reader;
    private CorpusWriter writer;
    private Corpus corpus;
    private File testFile;

    public AbstractCorpusIOTest(CorpusReader reader, CorpusWriter writer, Corpus corpus) {
        this(reader, writer, corpus, generateTempFile(".corpus"));
    }

    public AbstractCorpusIOTest(CorpusReader reader, CorpusWriter writer, Corpus corpus, File testFile) {
        this.reader = reader;
        this.writer = writer;
        this.corpus = corpus;
        this.testFile = testFile;
    }

    @Test
    public void test() {
        OutputStream out = null;
        InputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(testFile));
            writer.writeCorpus(corpus, out);
            IOUtils.closeQuietly(out);
            in = new BufferedInputStream(new FileInputStream(testFile));
            reader.readCorpus(in);
            Corpus readCorpus = reader.getCorpus();
            compareCorpora(corpus, readCorpus);
        }catch(Exception e) {
            e.printStackTrace();
            Assert.fail("Got an Exception: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    protected void compareCorpora(Corpus corpus, Corpus readCorpus) {
        Assert.assertEquals(corpus.getNumberOfDocuments(), readCorpus.getNumberOfDocuments());
        Document origDoc, readDoc;
        Iterator<Class<? extends DocumentProperty>> iter;
        Class<? extends DocumentProperty> propClass;
        DocumentProperty origProp, readProp;
        NamedEntitiesInText origNes, readNes;
        DocumentMultipleCategories origCategories, readCategories;
        for (int d = 0; d < corpus.getNumberOfDocuments(); ++d) {
            origDoc = corpus.getDocument(d);
            readDoc = readCorpus.getDocument(d);
            iter = origDoc.getPropertiesIterator();
            while (iter.hasNext()) {
                propClass = iter.next();
                origProp = origDoc.getProperty(propClass);
                readProp = readDoc.getProperty(propClass);
                Assert.assertNotNull("Property " + propClass.getSimpleName() + " missing.", readProp);
                if (propClass == NamedEntitiesInText.class) {
                    origNes = (NamedEntitiesInText) origProp;
                    readNes = (NamedEntitiesInText) readProp;
                    for (NamedEntityInText ne : origNes.getNamedEntities()) {
                        Assert.assertTrue("The list of named entities does not contain " + ne.toString() + ".", readNes
                                .getNamedEntities().contains(ne));
                    }
                } else if (propClass == TermTokenizedText.class) {
                    List<Term> origTerms = ((TermTokenizedText) origProp).getTermTokenizedText();
                    List<Term> readTerms = ((TermTokenizedText) readProp).getTermTokenizedText();
                    for (Term t : origTerms) {
                        Assert.assertTrue("The list of named entities does not contain " + t.toString() + ".",
                                readTerms.contains(t));
                    }
                } else if (propClass == DocumentMultipleCategories.class) {
                    origCategories = (DocumentMultipleCategories) origProp;
                    readCategories = (DocumentMultipleCategories) readProp;
                    Assert.assertArrayEquals(origCategories.getCategories(), readCategories.getCategories());
                } else {
                    Assert.assertEquals(origProp.getValue(), readProp.getValue());
                }
            }
        }
    }

    public static File generateTempFile(String suffix) {
        try {
            return File.createTempFile("test_", suffix);
        } catch (IOException e) {
            return new File("tmp_test" + suffix);
        }
    }
}
