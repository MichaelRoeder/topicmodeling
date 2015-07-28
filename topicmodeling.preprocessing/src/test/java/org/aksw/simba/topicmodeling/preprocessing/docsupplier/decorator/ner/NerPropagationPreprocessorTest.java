package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.Set;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityBasedTokenizer;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityPropagator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityTermMapping;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.NerPropagationPreprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.SimpleNerPropagationPreprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.WordSensePropagator;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.junit.Assert;
import org.junit.Test;



public class NerPropagationPreprocessorTest {

    @Test
    public void testSimpleNerPropagationPreprocessor() {
        DocumentText text = new DocumentText("Der neue Jaguar RX ist kein Golf VI. Golf von Mexiko");
        NamedEntitiesInText nes = new NamedEntitiesInText(new NamedEntityInText(37, 15, "Golf_Geo/Mexiko"),
                new NamedEntityInText(28, 7, "Golf_Car/VI"),
                new NamedEntityInText(9, 9, "Jaguar_Car/RX"));
        NerPropagationPreprocessor preprocessor = new SimpleNerPropagationPreprocessor();
        Set<String> tokens = preprocessor.preprocessNamedEntities(text, nes);

        Assert.assertEquals(0, tokens.size());
        Assert.assertEquals("Der neue  Jaguar RX  ist kein  Golf VI .  Golf von Mexiko ", text.getText());
        Assert.assertEquals(42, nes.getNamedEntities().get(0).getStartPos());
        Assert.assertEquals(15, nes.getNamedEntities().get(0).getLength());
        Assert.assertEquals(31, nes.getNamedEntities().get(1).getStartPos());
        Assert.assertEquals(7, nes.getNamedEntities().get(1).getLength());
        Assert.assertEquals(10, nes.getNamedEntities().get(2).getStartPos());
        Assert.assertEquals(9, nes.getNamedEntities().get(2).getLength());
    }

    @Test
    public void testEntityBasedTokenizer() {
        String origText = "Der neue Jaguar RX ist kein Golf VI. Golf von Mexiko";
        DocumentText text = new DocumentText(origText);
        NamedEntitiesInText nes = new NamedEntitiesInText(new NamedEntityInText(37, 15, "Golf_Geo/Mexiko"),
                new NamedEntityInText(28, 7, "Golf_Car/VI"),
                new NamedEntityInText(9, 9, "Jaguar_Car/RX"));
        EntityBasedTokenizer preprocessor = new EntityBasedTokenizer();
        Set<String> tokens = preprocessor.preprocessNamedEntities(text, nes);

        Assert.assertEquals(3, tokens.size());
        Assert.assertTrue(tokens.contains("JaguarRX"));
        Assert.assertTrue(tokens.contains("GolfVI"));
        Assert.assertTrue(tokens.contains("GolfvonMexiko"));
        Assert.assertEquals("Der neue  JaguarRX  ist kein  GolfVI .  GolfvonMexiko ", text.getText());
        Assert.assertEquals(40, nes.getNamedEntities().get(0).getStartPos());
        Assert.assertEquals(13, nes.getNamedEntities().get(0).getLength());
        Assert.assertEquals(30, nes.getNamedEntities().get(1).getStartPos());
        Assert.assertEquals(6, nes.getNamedEntities().get(1).getLength());
        Assert.assertEquals(10, nes.getNamedEntities().get(2).getStartPos());
        Assert.assertEquals(8, nes.getNamedEntities().get(2).getLength());
        
        EntityTermMapping mapping = preprocessor.getLastEntityTokenSurfaceFormMapping();
        Term terms[];
        Assert.assertEquals(nes.getNamedEntities().get(0), mapping.entities.get(0));
        terms = mapping.terms.get(0);
        Assert.assertEquals(origText.substring(37,52), terms[0].getWordForm());
        Assert.assertEquals(nes.getNamedEntities().get(1), mapping.entities.get(1));
        terms = mapping.terms.get(1);
        Assert.assertEquals(origText.substring(28,35), terms[0].getWordForm());
        Assert.assertEquals(nes.getNamedEntities().get(2), mapping.entities.get(2));
        terms = mapping.terms.get(2);
        Assert.assertEquals(origText.substring(9,18), terms[0].getWordForm());
    }

    @Test
    public void testEntityPropagator() {
        DocumentText text = new DocumentText("Der neue Jaguar RX ist kein Golf VI. Golf von Mexiko");
        NamedEntitiesInText nes = new NamedEntitiesInText(new NamedEntityInText(37, 15, "Golf_Geo/Mexiko"),
                new NamedEntityInText(28, 7, "Golf_Car/VI"),
                new NamedEntityInText(9, 9, "Jaguar_Car/RX"));
        EntityPropagator preprocessor = new EntityPropagator();
        Set<String> tokens = preprocessor.preprocessNamedEntities(text, nes);

        Assert.assertEquals(3, tokens.size());
        Assert.assertTrue(tokens.contains("JaguarRX"));
        Assert.assertTrue(tokens.contains("GolfVI"));
        Assert.assertTrue(tokens.contains("GolfvonMexiko"));
        Assert.assertEquals("Der neue  JaguarRX  ist kein  GolfVI .  GolfvonMexiko ", text.getText());
        Assert.assertEquals(40, nes.getNamedEntities().get(0).getStartPos());
        Assert.assertEquals(13, nes.getNamedEntities().get(0).getLength());
        Assert.assertEquals(30, nes.getNamedEntities().get(1).getStartPos());
        Assert.assertEquals(6, nes.getNamedEntities().get(1).getLength());
        Assert.assertEquals(10, nes.getNamedEntities().get(2).getStartPos());
        Assert.assertEquals(8, nes.getNamedEntities().get(2).getLength());
        
        EntityTermMapping mapping = preprocessor.getLastEntityTokenSurfaceFormMapping();
        Term terms[];
        Assert.assertEquals(nes.getNamedEntities().get(0), mapping.entities.get(0));
        terms = mapping.terms.get(0);
        Assert.assertEquals(nes.getNamedEntities().get(0).getNamedEntityUri(), terms[0].getWordForm());
        Assert.assertEquals(nes.getNamedEntities().get(1), mapping.entities.get(1));
        terms = mapping.terms.get(1);
        Assert.assertEquals(nes.getNamedEntities().get(1).getNamedEntityUri(), terms[0].getWordForm());
        Assert.assertEquals(nes.getNamedEntities().get(2), mapping.entities.get(2));
        terms = mapping.terms.get(2);
        Assert.assertEquals(nes.getNamedEntities().get(2).getNamedEntityUri(), terms[0].getWordForm());
    }

    @Test
    public void testWordSensePropagator() {
        DocumentText text = new DocumentText("Der neue Jaguar RX ist kein Golf VI. Golf von Mexiko");
        NamedEntitiesInText nes = new NamedEntitiesInText(new NamedEntityInText(37, 15, "Golf_Geo/Mexiko"),
                new NamedEntityInText(28, 7, "Golf_Car/VI"),
                new NamedEntityInText(9, 9, "Jaguar_Car/RX"));
        NerPropagationPreprocessor preprocessor = new WordSensePropagator("Golf");
        Set<String> tokens = preprocessor.preprocessNamedEntities(text, nes);

        Assert.assertEquals(3, tokens.size());
        Assert.assertTrue(tokens.contains("JaguarCar"));
        Assert.assertTrue(tokens.contains("GolfCar"));
        Assert.assertTrue(tokens.contains("GolfGeo"));
        Assert.assertEquals("Der neue  Jaguar RX  ist kein  GolfCar VI .  GolfGeo von Mexiko ", text.getText());
        Assert.assertEquals(45, nes.getNamedEntities().get(0).getStartPos());
        Assert.assertEquals(18, nes.getNamedEntities().get(0).getLength());
        Assert.assertEquals(31, nes.getNamedEntities().get(1).getStartPos());
        Assert.assertEquals(10, nes.getNamedEntities().get(1).getLength());
        Assert.assertEquals(10, nes.getNamedEntities().get(2).getStartPos());
        Assert.assertEquals(9, nes.getNamedEntities().get(2).getLength());
    }
}
