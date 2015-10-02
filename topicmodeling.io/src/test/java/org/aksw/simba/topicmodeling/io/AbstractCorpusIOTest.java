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
package org.aksw.simba.topicmodeling.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.aksw.simba.topicmodeling.utils.corpus.Corpus;
import org.aksw.simba.topicmodeling.utils.corpus.DocumentListCorpus;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.aksw.simba.topicmodeling.utils.doc.DocumentName;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.SignedNamedEntityInText;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractCorpusIOTest {

    private CorpusReader reader;
    private CorpusWriter writer;
    private Corpus corpus;

    public AbstractCorpusIOTest(CorpusReader reader, CorpusWriter writer, Corpus corpus) {
        this.reader = reader;
        this.writer = writer;
        this.corpus = corpus;
    }

    @Test
    public void test() {
        writer.writeCorpus(corpus);
        reader.readCorpus();
        Corpus readCorpus = reader.getCorpus();
        compareCorpora(corpus, readCorpus);
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

    public static Corpus createTestCorpus() {
        List<Document> documents = new ArrayList<Document>();

        documents.add(new Document(0, new DocumentProperty[] { new DocumentText("Dieser Text ist ein Testtext."),
                new DocumentName("Testdokument #1"), new NamedEntitiesInText() }));

        documents.add(new Document(1, new DocumentProperty[] {
                new DocumentText("Der neue Jaguar ist kein Golf."),
                new DocumentName("Bericht über den neuen Jaguar"),
                new NamedEntitiesInText(new NamedEntityInText[] { new NamedEntityInText(25, 4, "http://car/VWGolf"),
                        new SignedNamedEntityInText(9, 6, "http://animal/Jaguar", "manualAnnotation") }),
                new DocumentMultipleCategories(new String[] { "category1" }) }));

        documents.add(new Document(2, new DocumentProperty[] {
                new DocumentText("Am persischen Golf wird wieder Golf gespielt!"),
                new DocumentName("Sport aktuell"),
                new NamedEntitiesInText(new NamedEntityInText[] {
                        new SignedNamedEntityInText(31, 4, "http://sport/Golf", "someSource"),
                        new NamedEntityInText(3, 15, "http://geo/PersianGulf") }),
                new DocumentMultipleCategories(new String[] { "category1", "category2" }) }));

        return new DocumentListCorpus<List<Document>>(documents);
    }
}
