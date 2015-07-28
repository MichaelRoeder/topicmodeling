package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityTermMapping;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityTokenReplaceingPostprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityTokenSurfaceFormMappingSupplier;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.NerPropagationPostprocessor;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.TermTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;



@RunWith(Parameterized.class)
public class EntityTokenReplaceingPostprocessorTest implements EntityTokenSurfaceFormMappingSupplier {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] {
                "Der neue JaguarRX ist kein GolfVI.",
                new NamedEntitiesInText(new NamedEntityInText(27, 6, "Golf_Car/IV"), new NamedEntityInText(9, 8,
                        "Jaguar_Car/RX")),
                new Term[][] { new Term[] { new Term("Golf_Car/IV") }, new Term[] { new Term("Jaguar_Car/RX") } },
                new NamedEntitiesInTokenizedText(new NamedEntityInText(5, 1, "Golf_Car/IV"), new NamedEntityInText(2,
                        1, "Jaguar_Car/RX")) });
        data.add(new Object[] {
                "Der neue Jaguar RX ist kein Golf VI.",
                new NamedEntitiesInText(new NamedEntityInText(28, 7, "Golf_Car/IV"), new NamedEntityInText(9, 9,
                        "Jaguar_Car/RX")),
                new Term[][] { new Term[] { new Term("Golf_Car/IV") }, new Term[] { new Term("Jaguar_Car/RX") } },
                new NamedEntitiesInTokenizedText(new NamedEntityInText(5, 1, "Golf_Car/IV"), new NamedEntityInText(2,
                        1, "Jaguar_Car/RX")) });
        return data;
    }

    private NamedEntitiesInText nes;
    private DocumentText text;
    private TermTokenizedText tttext;
    private NamedEntitiesInTokenizedText expectedNes;
    private EntityTermMapping mapping;

    public EntityTokenReplaceingPostprocessorTest(String text, NamedEntitiesInText nes, Term neTerms[][],
            NamedEntitiesInTokenizedText expectedNes) {
        super();
        this.nes = nes;
        this.expectedNes = expectedNes;
        this.text = new DocumentText(text);

        String tokens[] = text.split(" ");
        Term terms[] = new Term[tokens.length];
        for (int i = 0; i < tokens.length; ++i) {
            terms[i] = new Term(tokens[i]);
        }
        this.tttext = new TermTokenizedText(terms);

        mapping = new EntityTermMapping();
        mapping.entities = nes.getNamedEntities();
        mapping.terms = Arrays.asList(neTerms);
    }

    @Test
    public void test() {
        NerPropagationPostprocessor postprocessor = new EntityTokenReplaceingPostprocessor(this);
        NamedEntitiesInTokenizedText nesInTT = postprocessor.postprocessNamedEntities(nes, text, tttext);
        Assert.assertEquals(expectedNes.getNamedEntities(), nesInTT.getNamedEntities());
    }

    @Override
    public EntityTermMapping getLastEntityTokenSurfaceFormMapping() {
        return mapping;
    }
}
