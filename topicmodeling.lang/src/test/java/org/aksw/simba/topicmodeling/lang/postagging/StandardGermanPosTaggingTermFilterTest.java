package org.aksw.simba.topicmodeling.lang.postagging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StandardGermanPosTaggingTermFilterTest extends AbstractListBasedTermFilterTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testConfigs = new ArrayList<Object[]>();
        testConfigs.add(new Object[] { "sieben", true });
        testConfigs.add(new Object[] { "Test", true });
        testConfigs.add(new Object[] { "Barack", true });
        testConfigs.add(new Object[] { "Obama", true });
        testConfigs.add(new Object[] { "ist", true });
        testConfigs.add(new Object[] { "ein", false });
        testConfigs.add(new Object[] { "der", false });
        testConfigs.add(new Object[] { "die", false });
        testConfigs.add(new Object[] { "das", false });
        testConfigs.add(new Object[] { "und", false });
        return testConfigs;
    }

    public StandardGermanPosTaggingTermFilterTest(String term, boolean shouldBeAccepted) {
        super(StandardGermanPosTaggingTermFilter.getInstance(), term, shouldBeAccepted);
    }

}
