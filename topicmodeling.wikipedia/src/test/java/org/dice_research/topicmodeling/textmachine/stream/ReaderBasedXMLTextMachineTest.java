package org.dice_research.topicmodeling.textmachine.stream;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.dice_research.topicmodeling.io.xml.stream.ReaderBasedTextMachineObserver;
import org.dice_research.topicmodeling.io.xml.stream.ReaderBasedXMLTextMachine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ReaderBasedXMLTextMachineTest implements ReaderBasedTextMachineObserver {

    private static final String STOP_PATTERN = "<stop>";

    @Parameters
    public static List<Object[]> getData() {
        return Arrays.asList(new Object[][] {
                // Test1 simple XML Tag
                {
                        "<!-- first Test--><tag>some data, comments &amp; tags</tag>",
                        new Object[][] { { 0, "", "<!-- first Test-->" }, { 1, "", "<tag>" },
                                { 2, "some data, comments ", "&amp;" }, { 1, " tags", "</tag>" } } },
                // Test2 complex comment tags
                {
                        "<!-- Test - - -- --><!--<tag>some data, comments &amp; tags</tag>-->",
                        new Object[][] { { 0, "", "<!-- Test - - -- -->" },
                                { 0, "", "<!--<tag>some data, comments &amp; tags</tag>-->" } } },
                // Test3 stop the machine
                { "<!-- Test - - -- --><stop><tag>some data, comments &amp; tags</tag>",
                        new Object[][] { { 0, "", "<!-- Test - - -- -->" }, { 1, "", "<stop>" } } } });
    }

    private String text;
    private Object expectedPatterns[][];
    private int id;
    private ReaderBasedXMLTextMachine machine = new ReaderBasedXMLTextMachine();

    public ReaderBasedXMLTextMachineTest(String text, Object expectedPatterns[][]) {
        this.text = text;
        this.expectedPatterns = expectedPatterns;
    }

    @Test
    public void test() {
        machine.analyze(new StringReader(text), this);
        Assert.assertEquals(expectedPatterns.length, id);
    }

    @Override
    public void foundPattern(int patternId, String data, String patternMatch) {
        Assert.assertTrue(id <= expectedPatterns.length);
        Assert.assertEquals(expectedPatterns[id][0], patternId);
        Assert.assertEquals(expectedPatterns[id][1], data);
        Assert.assertEquals(expectedPatterns[id][2], patternMatch);
        ++id;

        if ((patternId == 1) && (STOP_PATTERN.equals(patternMatch))) {
            machine.stop();
        }
    }

}
