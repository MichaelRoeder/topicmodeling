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
package org.dice_research.topicmodeling.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.io.test.AbstractCorpusIOTest;
import org.dice_research.topicmodeling.lang.Term;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.dice_research.topicmodeling.utils.corpus.DocumentListCorpus;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentMultipleCategories;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.TermTokenizedText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;

public class CorpusJsonIOTest extends AbstractCorpusIOTest {

    private static final File TEST_FILE = generateTempFile(".json");

    public CorpusJsonIOTest() {
        super(new CorpusJsonReader(), new CorpusJsonWriter(), createTestCorpus(), TEST_FILE);
    }

    public static Corpus createTestCorpus() {
        List<Document> documents = new ArrayList<Document>();

        documents.add(new Document(0, new DocumentProperty[] { new DocumentText("Dieser Text ist ein Testtext."),
                new DocumentName("Testdokument #1"), new NamedEntitiesInText() }));

        // FIXME The JSON implementation can not distinguish between NamedEntitiesInText
        // and SignedNamedEntityInText
        documents.add(new Document(1,
                new DocumentProperty[] { new DocumentText("Der neue Jaguar ist kein Golf."),
                        new DocumentName("Bericht über den neuen Jaguar"),
                        // new NamedEntitiesInText(new NamedEntityInText[] { new NamedEntityInText(25,
                        // 4, "http://car/VWGolf"),
                        // new SignedNamedEntityInText(9, 6, "http://animal/Jaguar", "manualAnnotation")
                        // }),
                        new DocumentMultipleCategories(new String[] { "category1" }) }));

        documents.add(new Document(2,
                new DocumentProperty[] { new DocumentText("Am persischen Golf wird wieder Golf gespielt!"),
                        new DocumentName("Sport aktuell"),
                        // new NamedEntitiesInText(new NamedEntityInText[] {
                        // new SignedNamedEntityInText(31, 4, "http://sport/Golf", "someSource"),
                        // new NamedEntityInText(3, 15, "http://geo/PersianGulf") }),
                        new DocumentMultipleCategories(new String[] { "category1", "category2" }) }));

        Term golfTerm = new Term("Golf", "golf", "nn");
        golfTerm.prop.setNoun(true);
        documents.add(new Document(3,
                new DocumentProperty[] { new DocumentText("Am persischen Golf wird wieder Golf gespielt!"),
                        new DocumentName("Sport aktuell"), new TermTokenizedText(new Term("Am", "am", "XYZ"),
                                new Term("persichen", "persisch", "adj"), golfTerm) }));

        return new DocumentListCorpus<List<Document>>(documents);
    }
}
