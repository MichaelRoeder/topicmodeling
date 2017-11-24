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
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.IOUtils;
import org.dice_research.topicmodeling.io.CorpusReader;
import org.dice_research.topicmodeling.io.CorpusWriter;
import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.preprocessing.docconsumer.DocumentConsumer;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplierAsIterator;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.DocumentListCorpus;
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
    private DocumentSupplier supplier;
    private DocumentConsumer consumer;
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

    public AbstractCorpusIOTest(DocumentSupplier supplier, DocumentConsumer consumer, Corpus corpus, File testFile) {
        this.supplier = supplier;
        this.consumer = consumer;
        this.corpus = corpus;
        this.testFile = testFile;
    }

    @Test
    public void test() {
        writeCorpus();
        Corpus readCorpus = readCorpus();
        compareCorpora(corpus, readCorpus);
    }

    public void writeCorpus() {
        OutputStream out = null;
        try {
            if (writer != null) {
                out = new BufferedOutputStream(new FileOutputStream(testFile));
                writer.writeCorpus(corpus, out);
            } else if (consumer != null) {
                StreamSupport.stream(Spliterators.spliterator(corpus.iterator(), corpus.getNumberOfDocuments(),
                        Spliterator.DISTINCT & Spliterator.NONNULL), false).forEach(consumer);
            } else {
                Assert.fail("Test is misconfigured since writer==null and consumer==null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Got an Exception: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(out);
            if ((consumer != null) && (consumer instanceof Closeable)) {
                IOUtils.closeQuietly((Closeable) consumer);
            }
        }
    }

    public Corpus readCorpus() {
        InputStream in = null;
        try {
            if (reader != null) {
                in = new BufferedInputStream(new FileInputStream(testFile));
                reader.readCorpus(in);
                return reader.getCorpus();
            } else if (supplier != null) {
                return new DocumentListCorpus<List<Document>>(
                        StreamSupport
                                .stream(Spliterators.spliteratorUnknownSize(new DocumentSupplierAsIterator(supplier),
                                        Spliterator.DISTINCT & Spliterator.NONNULL), false)
                                .collect(Collectors.toList()));
            } else {
                Assert.fail("Test is misconfigured since reader==null and supplier==null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Got an Exception: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
        return null;
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
                        Assert.assertTrue("The list of named entities does not contain " + ne.toString() + ".",
                                readNes.getNamedEntities().contains(ne));
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
