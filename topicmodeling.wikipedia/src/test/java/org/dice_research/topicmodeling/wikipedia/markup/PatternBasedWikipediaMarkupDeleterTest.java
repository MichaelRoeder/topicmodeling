package org.dice_research.topicmodeling.wikipedia.markup;

import java.util.Arrays;
import java.util.List;

import org.dice_research.topicmodeling.wikipedia.markup.StackBasedMarkupDeletingMachine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PatternBasedWikipediaMarkupDeleterTest {

    @Parameters
    public static List<Object[]> getData() {
        return Arrays
                .asList(new Object[][] {
                        { "Test{{Link FA|id}}", "\nTest", "\nTest" },
                        { "Test{{Link FA|{{someOtherMarkup}}}}", "\nTest", "\nTest" },
                        { "Test [[Link|text]]", "\nTest text", "\nTest text" },
                        { "Test [[Link|style|text]]", "\nTest text", "\nTest text" },
                        { "Test [[Link|style|more style|text]]", "\nTest text", "\nTest text" },
                        { "Test [Link text]", "\nTest text", "\nTest text" },
                        { "=== Test ===", "\n Test ", "\n Test " },
                        { "====== Test ======", "\n Test ", "\n Test " },
                        { "======= Test =======", "\n= Test =", "\n= Test =" },
                        { "'''Test'''", "\n Test ", "\n Test " },
                        { "''Test''", "\n Test ", "\n Test " },
                        { "'''' Test ''''", "\n ' Test  '", "\n ' Test  '" },
                        { "''''' Test '''''", "\n   Test   ", "\n   Test   " },
                        { " &ouml;", "\n ö", "\n ö" },
                        { "*point 1\n*point 2\npoint 3\n*", "\npoint 1\npoint 2\npoint 3\n",
                                "\npoint 1\npoint 2\npoint 3\n" },
                        { "[[Square brackets|<nowiki>]</nowiki>]]", "\n]", "\n]" },
                        { "<!-- some comments --> Test <!-- another -- comment-->", "\n Test ", "\n Test " },
                        { "<<nowiki />pre>", "\n<pre>", "\n<pre>" },
                        { "<nowiki><<</nowiki>nowiki />pre>", "\n<<nowiki />pre>", "\n<<nowiki />pre>" },
                        { "{| class=\"wikitable\"\n| first cell\n| second cell\n|}",
                                "\n\n first cell\n second cell\n", "\n" },
                        { "{| class=\"wikitable\"\n| cell [text] \n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| [[cell text]] \n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| cell text <!--[extendended]-->\n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| cell text <!--|}-->\n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| cell text {{macro}}\n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| cell text \n|-\n|}", "\n\n cell text \n", "\n" },
                        { "{| class=\"wikitable\"\n| cell text || <nowiki><<</nowiki>nowiki />pre> \n|}",
                                "\n\n cell text \n <<nowiki />pre> \n", "\n" },
                        {
                                "{| class=\"wikitable\"\n| Preformatted text blocks\n| <pre> <nowiki><nowiki>Start with a space in the first column,\n(before the <nowiki>).\n\n Then your block format will be\n    maintained.</nowiki></nowiki>\n</pre>\n|}",
                                "\n\n Preformatted text blocks\n  Start with a space in the first column,\n(before the ).\n\n Then your block format will be\n    maintained.\n\n",
                                "\n"
                        },
                        {
                                "{| class=\"wikitable\"\n| Preformatted text blocks\n|\n <nowiki>Start with a space in the first column,\n(before the <nowiki>).\n\n Then your block format will be\n    maintained.</nowiki>\n|}",
                                "\n\n Preformatted text blocks\n\n Start with a space in the first column,\n(before the ).\n\n Then your block format will be\n    maintained.\n",
                                "\n"
                        },
                        {
                                "{| class=\"wikitable\"\n! some style information | text1 \n! again style information | text2 \n|}",
                                "\n\n text1 \n text2 \n", "\n" },
                        {
                                "{| class=\"wikitable mw-collapsible mw-collapsed\"\n! headline\n|-\n| some List \n{| class=\"toptextcells\"\n|\n* [[Daniel Bernard Sweeney]]: Mark Bledsoe (8)\n* [[Mare Winningham]]: Elaine Al-Zacar (8) \n|}\n|}",
                                "\n\n headline\n some List \n\n\n Daniel Bernard Sweeney: Mark Bledsoe (8)\n Mare Winningham: Elaine Al-Zacar (8) \n\n",
                                "\n"
                        },
                        {
                                "[[Algerian Cuisine | [http://recipesaroundworld.blogspot.pt/search/label/Algeria Algerian Cuisine]]]",
                                "\n Algerian Cuisine",
                                "\n Algerian Cuisine" },
                        { "<!------------Details------------------->Test", "\nTest", "\nTest" },
                        { "{| class=\"wikitable\"\n|style| 2D || [[Hyphen-minus|-]]\n|}", "\n\n 2D \n -\n", "\n" },
                        { "[[Category:Animation| ]]", "\n ", "\n " },
                        {
                                "[http://www.freemayavideotutorials.com/what-is-new/flowers-and-trees-1932-ist-oscar-award-winner-3d-animation-movie.html Flowers And Trees [1932&#93; , Ist Oscar Award Winner 3D Animation Movie | Free Maya Video Tutorials<!-- Bot generated title -->]",
                                "\nFlowers And Trees [1932] , Ist Oscar Award Winner 3D Animation Movie | Free Maya Video Tutorials",
                                "\nFlowers And Trees [1932] , Ist Oscar Award Winner 3D Animation Movie | Free Maya Video Tutorials" },
                        { "[ab a < b] <div/>", "\na < b ", "\na < b " },
                        {
                                "formula <math>\\alpha_{H_{n-i} A^{i-} }= {{[H^+ ]^{n-i} \\displaystyle \\prod_{j=0}^{i}K_j} \\over { \\displaystyle \\sum_{i=0}^n \\Big[ [H^+ ]^{n-i} \\displaystyle \\prod_{j=0}^{i}K_j} \\Big] }</math> describing Text.",
                                "\nformula  describing Text.", "\nformula  describing Text." },
                        {
                                "<gallery>\nFile:Gjergj Kastrioti.jpg|[[Skanderbeg|George Kastrioti Skanderbeg]], national hero <br />(1405–1468)\nFile:Ismail Qemali.jpg|[[Ismail Qemali]], hero of Albanian independence (1912–14)\n</gallery>",
                                "\n\nFile:Gjergj Kastrioti.jpg|George Kastrioti Skanderbeg, national hero (1405–1468)\nFile:Ismail Qemali.jpg|Ismail Qemali, hero of Albanian independence (1912–14)\n",
                                "\n\nFile:Gjergj Kastrioti.jpg|George Kastrioti Skanderbeg, national hero (1405–1468)\nFile:Ismail Qemali.jpg|Ismail Qemali, hero of Albanian independence (1912–14)\n" },
                        {
                                "some text... [[Category:Test category]]\n[[category:another category]]\n[[a third link without the category key word]]\n text ",
                                "\nsome text... \n\na third link without the category key word\n text ",
                                "\nsome text... \n\na third link without the category key word\n text " },
                        // fehlerhaftes Markup
                        {
                                "{|\n| Arabic, Bosnian, [[Kashmiri language|Kashmiri'', Kurdish, Kyrghyz, others || 500 CE || Nabataean Aramaic\n|}",
                                "\n\n Arabic, Bosnian, Kashmiri , Kurdish, Kyrghyz, others \n 500 CE \n Nabataean Aramaic\n",
                                "\n" },
                        {
                                "{|\n| [[Audi_S7#Audi_S7|{{nowrap|S7]]}}<ref>{{cite web|url=http://www.audi.co.uk/new-cars/a7/s7-sportback.html}}</ref>\n|}",
                                "\n\n \n", "\n" },
                        { "Text [versehentlich gesetzte Klammer\ntext text text text ] text",
                                "\nText [versehentlich gesetzte Klammer\ntext text text text ] text",
                                "\nText [versehentlich gesetzte Klammer\ntext text text text ] text" },
                        { "Text [[versehentlich gesetzte Klammer\ntext text text text ]] text",
                                "\nText [[versehentlich gesetzte Klammer\ntext text text text ]] text",
                                "\nText [[versehentlich gesetzte Klammer\ntext text text text ]] text" },
                        { "*Aufzählung \n*Aufzählung [[versehentlich gesetzte Klammer\n*Aufzählung",
                                "\nAufzählung \nAufzählung [[versehentlich gesetzte Klammer\nAufzählung",
                                "\nAufzählung \nAufzählung [[versehentlich gesetzte Klammer\nAufzählung" },
                        { "{|align=right\n|-\n|{{Infobox wrong closed\n| some data \n|}}\n\n Some real text",
                                "\n\n\n\n\n Some real text",
                                "\n\n\n Some real text" },
                        { "<ref <!--group=i-->>''Synchronistic History''</ref>", "\n<ref > Synchronistic History ",
                                "\n<ref > Synchronistic History " },
                        {
                                "{| class=\"wikitable\"\n|-\n|- style=\"background:#CCCCCC\"\n|'''Indicator'''<ref>Source: [Link IMF WEO Online database]</ref>\n|- align=\"right\"\n\nSome Text.",
                                "\n\n Indicator Source: IMF WEO Online database\n\n\nSome Text.", "\n\nSome Text." },
                        {
                                "{| class=\"wikitable\"\n| 18.2\n|-| COLSPAN=4 |\n{{note label|cross_chunnel|A|A}}only passengers taking Eurostar to cross the Channel\n|}",
                                "\n\n 18.2\n\nonly passengers taking Eurostar to cross the Channel", "\n" },
                        {
                                "{| class=\"wikitable\"\n|- {| class=\"wikitable\"\n| COLSPAN=4 | some text \n|-\n",
                                "\n\n\n some text \n\n", "\n\n" }
                });
    }

    private String text;

    private String expectedOutput;
    private String expectedOutputWithoutTable;

    public PatternBasedWikipediaMarkupDeleterTest(String text,
            String expectedOutput, String expectedOutputWithoutTable) {
        this.text = text;
        this.expectedOutput = expectedOutput;
        this.expectedOutputWithoutTable = expectedOutputWithoutTable;
    }

    @Test
    public void testPattern() {
        StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine(true, true);
        // deleter.setRemoveCategoryLinks(true);
        String cleanText = deleter.getCleanText(text);
        Assert.assertEquals(expectedOutput, cleanText);
    }

    @Test
    public void testPatternWithoutTable() {
        StackBasedMarkupDeletingMachine deleter = new StackBasedMarkupDeletingMachine(false, true);
        // deleter.setRemoveCategoryLinks(true);
        // deleter.setKeepTableContents(false);
        String cleanText = deleter.getCleanText(text);
        Assert.assertEquals(expectedOutputWithoutTable, cleanText);
    }
}
