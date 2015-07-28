package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.TextDeletingExceptNESurfaceFormsSupplierDecorator;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
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
