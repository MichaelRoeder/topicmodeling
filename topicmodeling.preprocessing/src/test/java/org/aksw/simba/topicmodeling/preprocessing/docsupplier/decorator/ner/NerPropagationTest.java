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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.SimpleWhitespaceTagger;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityPropagator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityTokenReplaceingPostprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.NerPropagatingSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.junit.Assert;
import org.junit.Test;



public class NerPropagationTest implements DocumentSupplier {

    private Document document;

    @Test
    public void testEntityPropagation() {
        document = new Document();
        document.addProperty(new DocumentText("Der neue Jaguar RX ist kein Golf VI. Golf von Mexiko"));
        document.addProperty(new NamedEntitiesInText(new NamedEntityInText(37, 15, "Golf_Geo/Mexiko"),
                new NamedEntityInText(28, 7, "Golf_Car/VI"), new NamedEntityInText(9, 9, "Jaguar_Car/RX")));
        EntityPropagator preprocessor = new EntityPropagator();

        DocumentSupplier supplier = new NerPropagatingSupplierDecorator(this, new SimpleWhitespaceTagger(),
                preprocessor, new EntityTokenReplaceingPostprocessor(preprocessor));

        supplier.getNextDocument();

        TermTokenizedText ttt = document.getProperty(TermTokenizedText.class);
        Assert.assertNotNull(ttt);
        StringBuilder newtext = new StringBuilder();
        List<Term> terms = ttt.getTermTokenizedText();
        for (int i = 0; i < terms.size(); ++i) {
            if (i > 0) {
                newtext.append(' ');
            }
            newtext.append(terms.get(i).getWordForm());
        }
        Assert.assertEquals("Der neue Jaguar_Car/RX ist kein Golf_Car/VI . Golf_Geo/Mexiko", newtext.toString());

        NamedEntitiesInTokenizedText nes = document.getProperty(NamedEntitiesInTokenizedText.class);
        Assert.assertNotNull(nes);

        Assert.assertEquals(7, nes.getNamedEntities().get(0).getStartPos());
        Assert.assertEquals(1, nes.getNamedEntities().get(0).getLength());
        Assert.assertEquals(5, nes.getNamedEntities().get(1).getStartPos());
        Assert.assertEquals(1, nes.getNamedEntities().get(1).getLength());
        Assert.assertEquals(2, nes.getNamedEntities().get(2).getStartPos());
        Assert.assertEquals(1, nes.getNamedEntities().get(2).getLength());
    }

    @Override
    public Document getNextDocument() {
        return document;
    }

    @Override
    public void setDocumentStartId(int documentStartId) {
        // nothing to do
    }
}
