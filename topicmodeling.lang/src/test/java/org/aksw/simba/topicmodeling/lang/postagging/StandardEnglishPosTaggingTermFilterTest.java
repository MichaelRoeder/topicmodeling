package org.aksw.simba.topicmodeling.lang.postagging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StandardEnglishPosTaggingTermFilterTest extends AbstractListBasedTermFilterTest {

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testConfigs = new ArrayList<Object[]>();
        testConfigs.add(new Object[] { "first", true });
        testConfigs.add(new Object[] { "second", true });
        testConfigs.add(new Object[] { "Barack", true });
        testConfigs.add(new Object[] { "Obama", true });
        testConfigs.add(new Object[] { "is", false });
        testConfigs.add(new Object[] { "a", false });
        return testConfigs;
    }

    public StandardEnglishPosTaggingTermFilterTest(String term, boolean shouldBeAccepted) {
        super(StandardEnglishPosTaggingTermFilter.getInstance(), term, shouldBeAccepted);
    }

}
