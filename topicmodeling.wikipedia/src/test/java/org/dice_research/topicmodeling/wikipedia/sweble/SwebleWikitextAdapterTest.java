package org.dice_research.topicmodeling.wikipedia.sweble;

import java.util.Arrays;
import java.util.List;

import org.dice_research.topicmodeling.wikipedia.sweble.SwebleWikitextAdapter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@Deprecated
@RunWith(Parameterized.class)
public class SwebleWikitextAdapterTest {

    @Parameters
    public static List<Object[]> getData() {
        return Arrays
                .asList(new Object[][] {
                        { "Test{{Link FA|id}}", "Test" },
                        { "Test{{Link FA|{{someOtherMarkup}}}}", "Test" },
                        { "Test [[Link|text]]", "Test text" },
                        { "Test [[Link|style|text]]", "Test text" },
                        { "Test [[Link|style|more style|text]]", "Test text" },
                        { "Test [Link text]", "Test text" },
                        { "=== Test ===", " Test " },
                        { "====== Test ======", " Test " },
                        { "======= Test =======", "= Test =" },
                        { "'''Test'''", " Test " },
                        { "''Test''", " Test " },
                        { "'''' Test ''''", " ' Test  '" },
                        { "''''' Test '''''", "   Test   " },
                        { " &ouml;", " ö" },
                        // Dieser Parser soll Listenelemente löschen
                        { "*point 1\n*point 2\npoint 3\n*", "point 3" },
                        { "[[Square brackets|<nowiki>]</nowiki>]]", "]" },
                        { "<!-- some comments --> Test <!-- another -- comment-->", " Test " },
                        { "<<nowiki />pre>", "<pre>" },
                        { "<nowiki><<</nowiki>nowiki />pre>", "<<nowiki />pre>" },
                        { "{| class=\"wikitable\"\n| first cell\n| second cell\n|}", "" },
                        { "{| class=\"wikitable\"\n| cell [text] \n|}", "" },
                        { "{| class=\"wikitable\"\n| [[cell text]] \n|}", "" },
                        { "{| class=\"wikitable\"\n| cell text <!--[extendended]-->\n|}", "" },
                        { "{| class=\"wikitable\"\n| cell text <!--|}-->\n|}", "" },
                        { "{| class=\"wikitable\"\n| cell text {{macro}}\n|}", "" },
                        { "{| class=\"wikitable\"\n| cell text \n|-\n|}", "" },
                        { "{| class=\"wikitable\"\n| cell text || <nowiki><<</nowiki>nowiki />pre> \n|}", "" },
                        {
                                "{| class=\"wikitable\"\n| Preformatted text blocks\n| <pre> <nowiki><nowiki>Start with a space in the first column,\n(before the <nowiki>).\n\n Then your block format will be\n    maintained.</nowiki></nowiki>\n</pre>\n|}",
                                ""
                        },
                        {
                                "{| class=\"wikitable\"\n| Preformatted text blocks\n|\n <nowiki>Start with a space in the first column,\n(before the <nowiki>).\n\n Then your block format will be\n    maintained.</nowiki>\n|}",
                                ""
                        },
                        {
                                "{| class=\"wikitable\"\n! some style information | text1 \n! again style information | text2 \n|}",
                                "" },
                        {
                                "{| class=\"wikitable mw-collapsible mw-collapsed\"\n! headline\n|-\n| some List \n{| class=\"toptextcells\"\n|\n* [[Daniel Bernard Sweeney]]: Mark Bledsoe (8)\n* [[Mare Winningham]]: Elaine Al-Zacar (8) \n|}\n|}",
                                ""
                        },
                        {
                                "[[Algerian Cuisine | [http://recipesaroundworld.blogspot.pt/search/label/Algeria Algerian Cuisine]]]",
                                " Algerian Cuisine" },
                        { "<!------------Details------------------->Test", "Test" },
                        { "{| class=\"wikitable\"\n|style| 2D || [[Hyphen-minus|-]]\n|}", "" },
                        { "[[Category:Animation| ]]", " " },
                        {
                                "[http://www.freemayavideotutorials.com/what-is-new/flowers-and-trees-1932-ist-oscar-award-winner-3d-animation-movie.html Flowers And Trees [1932&#93; , Ist Oscar Award Winner 3D Animation Movie | Free Maya Video Tutorials<!-- Bot generated title -->]",
                                "Flowers And Trees [1932] , Ist Oscar Award Winner 3D Animation Movie | Free Maya Video Tutorials" },
                        { "[ab a < b] <div/>", "a < b " },
                        {
                                "formula <math>\\alpha_{H_{n-i} A^{i-} }= {{[H^+ ]^{n-i} \\displaystyle \\prod_{j=0}^{i}K_j} \\over { \\displaystyle \\sum_{i=0}^n \\Big[ [H^+ ]^{n-i} \\displaystyle \\prod_{j=0}^{i}K_j} \\Big] }</math> describing Text.",
                                "formula  describing Text." },
                        {
                                "<gallery>\nFile:Gjergj Kastrioti.jpg|[[Skanderbeg|George Kastrioti Skanderbeg]], national hero <br />(1405–1468)\nFile:Ismail Qemali.jpg|[[Ismail Qemali]], hero of Albanian independence (1912–14)\n</gallery>",
                                "\nFile:Gjergj Kastrioti.jpg|George Kastrioti Skanderbeg, national hero (1405–1468)\nFile:Ismail Qemali.jpg|Ismail Qemali, hero of Albanian independence (1912–14)\n" },
                        {
                                "some text... [[Category:Test category]]\n[[category:another category]]\n[[a third link without the category key word]]\n text ",
                                "some text... \n\na third link without the category key word\n text " },
                        // fehlerhaftes Markup
                        {
                                "{|\n| Arabic, Bosnian, [[Kashmiri language|Kashmiri'', Kurdish, Kyrghyz, others || 500 CE || Nabataean Aramaic\n|}",
                                "" },
                        {
                                "{|\n| [[Audi_S7#Audi_S7|{{nowrap|S7]]}}<ref>{{cite web|url=http://www.audi.co.uk/new-cars/a7/s7-sportback.html}}</ref>\n|}",
                                "" },
                        { "Text [versehentlich gesetzte Klammer\ntext text text text ] text",
                                "Text [versehentlich gesetzte Klammer\ntext text text text ] text" },
                        { "Text [[versehentlich gesetzte Klammer\ntext text text text ]] text",
                                "Text [[versehentlich gesetzte Klammer\ntext text text text ]] text" },
                        // Dieser Parser soll Listenelemente löschen
                        { "*Aufzählung \n*Aufzählung [[versehentlich gesetzte Klammer\n*Aufzählung", "" },
                        { "{|align=right\n|-\n|{{Infobox wrong closed\n| some data \n|}}\n\n Some real text",
                                "\n\n Some real text" },
                        { "<ref <!--group=i-->>''Synchronistic History''</ref>", "<ref > Synchronistic History " },
                        {
                                "{| class=\"wikitable\"\n|-\n|- style=\"background:#CCCCCC\"\n|'''Indicator'''<ref>Source: [Link IMF WEO Online database]</ref>\n|- align=\"right\"\n\nSome Text.",
                                "Some Text." },
                        {
                                "{| class=\"wikitable\"\n| 18.2\n|-| COLSPAN=4 |\n{{note label|cross_chunnel|A|A}}only passengers taking Eurostar to cross the Channel\n|}",
                                "" },
                        {
                                "{| class=\"wikitable\"\n|- {| class=\"wikitable\"\n| COLSPAN=4 | some text \n|-\n", "" }
                });
    }

    private String text;

    private String expectedOutput;

    public SwebleWikitextAdapterTest(String text,
            String expectedOutput) {
        this.text = text;
        this.expectedOutput = expectedOutput;
    }

    @Ignore
    @Test
    public void testPattern() throws Exception {
        SwebleWikitextAdapter adapter = new SwebleWikitextAdapter();
        // deleter.setRemoveCategoryLinks(true);
        String cleanText = adapter.getCleanText(text);
        Assert.assertEquals(expectedOutput.trim(), cleanText);
    }

    // @Test
    // public void testPatternWithoutTable() {
    // StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine(false, true);
    // // deleter.setRemoveCategoryLinks(true);
    // // deleter.setKeepTableContents(false);
    // String cleanText = deleter.getCleanText(text);
    // Assert.assertEquals(expectedOutputWithoutTable, cleanText);
    // }
}
