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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.TextDeletingExceptNESurfaceFormsSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



@RunWith(Parameterized.class)
public class TextDeletingExceptNESurfaceFormsSupplierDecoratorTest extends AbstractDocumentSupplierDecoratorTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {
                "Der neue Jaguar RX ist kein Golf VI.",
                new NamedEntitiesInText(new NamedEntityInText(28, 7,
                        "Golf_Car/IV"), new NamedEntityInText(9, 9, "Jaguar_Car/RX")),
                "Jaguar RX Golf VI",
                new NamedEntitiesInText(new NamedEntityInText(10, 7,
                        "Golf_Car/IV"), new NamedEntityInText(0, 9, "Jaguar_Car/RX")) });
        return data;
    }

    private String expectedText;
    private NamedEntitiesInText expectedNes;

    public TextDeletingExceptNESurfaceFormsSupplierDecoratorTest(String inputText, NamedEntitiesInText inputNes,
            String expectedText, NamedEntitiesInText expectedNes) {
        super(new DocumentText(inputText), inputNes);
        this.expectedText = expectedText;
        this.expectedNes = expectedNes;
    }

    @Test
    public void test() {
        TextDeletingExceptNESurfaceFormsSupplierDecorator supplier = new TextDeletingExceptNESurfaceFormsSupplierDecorator(
                this);
        Document document = supplier.getNextDocument();
        DocumentText text = document.getProperty(DocumentText.class);
        Assert.assertNotNull(text);
        Assert.assertEquals(expectedText, text.getText());
        NamedEntitiesInText nes = document.getProperty(NamedEntitiesInText.class);
        Assert.assertEquals(expectedNes.getNamedEntities(), nes.getNamedEntities());
    }
}
