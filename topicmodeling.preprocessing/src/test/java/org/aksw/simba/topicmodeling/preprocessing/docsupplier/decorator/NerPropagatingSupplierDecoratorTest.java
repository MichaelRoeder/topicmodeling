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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aksw.simba.topicmodeling.lang.Language;
import org.aksw.simba.topicmodeling.lang.Term;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggerFactory;
import org.aksw.simba.topicmodeling.lang.postagging.PosTaggingTermFilter;
import org.aksw.simba.topicmodeling.lang.postagging.TreeTaggerWrapper;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.EntityBasedTokenizer;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.NerPropagatingSupplierDecorator;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.NerPropagationPreprocessor;
import org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.ner.SimpleNerPropagationPreprocessor;
import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentProperty;
import org.aksw.simba.topicmodeling.utils.doc.DocumentText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntitiesInTokenizedText;
import org.aksw.simba.topicmodeling.utils.doc.ner.NamedEntityInText;
import org.aksw.simba.topicmodeling.utils.doc.ner.SignedNamedEntityInText;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

@RunWith(Parameterized.class)
public class NerPropagatingSupplierDecoratorTest extends AbstractDocumentSupplierDecoratorTest {

    // private static PosTaggingTermFilter FILTER = new PosTaggingTermFilter() {
    // private PosTaggingTermFilter stopWordListFilter = new
    // StopwordlistBasedTermFilter(
    // Language.GER);
    // private RunAutomaton numberChecker = new RunAutomaton((new
    // RegExp(".*[1-9].*")).toAutomaton());
    //
    // @Override
    // public boolean isTermGood(Term term) {
    // return stopWordListFilter.isTermGood(term)
    // && term.getLabel().length() >= 2 &&
    // (!numberChecker.run(term.getLemma()));
    // }
    // };

    @BeforeClass
    public static void init() {
        System.setProperty(TreeTaggerWrapper.TREE_TAGGER_HOME_PROPERTY_KEY, "/data/m.roeder/programme/TreeTagger");
        System.setProperty(TreeTaggerWrapper.MODEL_FILE_PROPERTY_KEY,
                "/data/m.roeder/programme/TreeTagger/lib/german-utf8.par:utf-8");
        System.setProperty(TreeTaggerWrapper.ABBREVIATION_FILE_PROPERTY_KEY,
                "/data/m.roeder/programme/TreeTagger/lib/german-abbreviations-utf8");
    }

    private static PosTaggingTermFilter FILTER = new PosTaggingTermFilter() {
        private RunAutomaton numberChecker = new RunAutomaton((new RegExp("[1-9\\.]+")).toAutomaton());

        @Override
        public boolean isTermGood(Term term) {
            return term.properties.isNoun() && (term.getWordForm().length() >= 2) && (!numberChecker.run(term.getLemma()));
            // && StringUtils.isAlpha(term.getLabel());
        }
    };

    @Parameters
    public static Collection<Object[]> data() {
        // NerPropagationPreprocessor preprocessors[] = new
        // NerPropagationPreprocessor[] { null,
        // new EntityBasedTokenizer(), new WordSensePropagator("Golf"), new
        // EntityPropagator() };

        // Properties
        List<Object[]> startConfigs = new ArrayList<Object[]>();
        startConfigs.add(new Object[] {
                new DocumentProperty[] {
                        new DocumentText("Der neue Jaguar RX ist kein Golf VI."),
                        new NamedEntitiesInText(new NamedEntityInText(28, 7, "Golf_Car/IV"), new NamedEntityInText(9,
                                9, "Jaguar_Car/RX")) }, new int[][] { { 2, 2 }, { 0, 2 } },
                new SimpleNerPropagationPreprocessor(), Language.GER });
        startConfigs.add(new Object[] {
                new DocumentProperty[] {
                        new DocumentText("Ein Golf VI steht am Golf von Mexiko vor einem Golf Club"),
                        new NamedEntitiesInText(new NamedEntityInText(47, 4, "Golf_Sport"), new NamedEntityInText(21,
                                16, "Golf_Geo/Mexiko"), new NamedEntityInText(4, 7, "Golf_Car/IV")) },
                new int[][] { { 4, 1 }, { 2, 2 }, { 0, 2 } }, new SimpleNerPropagationPreprocessor(), Language.GER });
        startConfigs
                .add(new Object[] {
                        new DocumentProperty[] {
                                new DocumentText(
                                        "West German private banks, in the latest twist in a long-running battle over the size of their holdings in industry, have said the holdings were falling and rejected calls for tighter limits on these participations. Under West Germany's banking system, banks can hold shares in non-banking companies giving them seats on company supervisory boards, and administer the holdings of small shareholders in safe custody accounts. Holdings of over 25 pct, which give a veto right in the supervisory board of a company, must be disclosed. \"The claim that the concentration of power has intensified, as made by the Monopolies Commission, therefore turns out not to be accurate ... There is no reason to impose further legal restrictions on the acquisition of holdings by banks,\" it said. The Association's letter is the latest skirmish in a long running battle between banks on the one hand and a range of government offices and political lobbies on the left and right. The number of holdings above 10 pct in public companies with a nominal share capital of over one mln marks held by these 10 banks fell to 86 in 1986 from 129 in 1976, it said. Of these 43 reductions, 41 represented holdings of over 25 pct in 1976. The banks' holdings of nominal share capital fell to 1.70 billion marks in 1986, or 1.34 billion adjusted for capital changes in the past 10 years from 1.80 billion in 1976. Over this period total West German nominal share capital rose to 235 billion marks from 136 billion, so that the holding of banks in German companies fell to 0.7 pct from 1.3 pct. The nominal share capital of listed non-bank companies rose to an estimated 49 billion marks in 1986 from 35.58 billion in 1976, with banks' holdings falling to 1.57 billion marks or 3.2 pct from 1.61 billion marks or 4.5 pct, the Association said. It argued that bank representatives were in a minority on the supervisory boards of the 100 biggest German companies, and in most cases could be blocked by non-banking representatives. The survey covered Deutsche Bank AG DBKG.F, Dresdner Bank AG DRSD.F, Commerzbank AG CBKG.F, Bayerische Vereinsbank AG BVMG.F and Bayerische Hypotheken- und Wechsel-Bank AG BHWG.F. It also covered Berliner Handels- und Frankfurter Bank BHFG.F, Berliner Bank AG, Industriekreditbank AG, Vereins- und Westbank AG VHBG.F and Baden-Wuerttembergische Bank AG. Deutsche holds by far the largest number of such stakes, including 28.5 pct of Daimler-Benz AG DAIG.F. Criticism of the bank holdings has not only come from the Monopolies Commission and left-wing parties. An advisory commission, in a report late last year to the Economics Ministry, urged implementation of the five pct proposed by the Monopolies Commission to promote competition and avoid interest conflicts. The Economics Ministry has been controlled by the liberal Free Democrat Party, junior partner in the centre-right coalition government and increasingly in favour of deregulation. Furthermore the West German Supreme Court recently supported an action by a private Deutsche shareholder demanding more transparency in the bank's balance sheet. The shareholder argued that the bank's ability to declare industrial stakes of more than 25 pct as only temporary holdings evaded the statutory requirement to report them in full. If a bank declares stakes temporary it does not have to disclose them but can include them under securities holdings. The case is due to be heard again by the Frankfurt higher regional court. REUTER"),
                                new NamedEntitiesInText(
                                        new SignedNamedEntityInText(2959, 11,
                                                "http://dbpedia.org/resource/West_Germany",
                                                "http://ns.aksw.org/scms/tools/FOX"), new SignedNamedEntityInText(2090,
                                                25, "http://scms.eu/Bayerische_Vereinsbank_AG",
                                                "http://ns.aksw.org/scms/tools/FOX"), new SignedNamedEntityInText(1407,
                                                11, "http://dbpedia.org/resource/West_Germany",
                                                "http://ns.aksw.org/scms/tools/FOX"), new SignedNamedEntityInText(0,
                                                11, "http://dbpedia.org/resource/West_Germany",
                                                "http://ns.aksw.org/scms/tools/FOX")) },
                        new int[][] { { 171, 1 }, { 110, 1 }, { 71, 1 }, { 0, 1 } }, new EntityBasedTokenizer(),
                        Language.ENG });
        return startConfigs;
    }

    private int[][] expectedNEPositions;
    private Language lang;
    private NerPropagationPreprocessor preprocessor;

    public NerPropagatingSupplierDecoratorTest(DocumentProperty properties[], int[][] expectedNEPositions,
            NerPropagationPreprocessor preprocessor, Language lang) {
        super(properties);
        this.expectedNEPositions = expectedNEPositions;
        this.lang = lang;
        this.preprocessor = preprocessor;
    }

    @Test
    public void test() {
        NerPropagatingSupplierDecorator supplier = new NerPropagatingSupplierDecorator(this,
                PosTaggerFactory.getPosTaggingStep(lang, FILTER), preprocessor);
        Document document = supplier.getNextDocument();
        NamedEntitiesInTokenizedText nett = document.getProperty(NamedEntitiesInTokenizedText.class);
        Assert.assertNotNull(nett);
        List<NamedEntityInText> entities = nett.getNamedEntities();
        Assert.assertEquals(expectedNEPositions.length, entities.size());
        for (int i = 0; i < expectedNEPositions.length; ++i) {
            Assert.assertEquals(expectedNEPositions[i][0], entities.get(i).getStartPos());
            Assert.assertEquals(expectedNEPositions[i][1], entities.get(i).getLength());
        }
    }
}
