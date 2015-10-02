/**
 * This file is part of topicmodeling.lang.
 *
 * topicmodeling.lang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.lang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.lang.  If not, see <http://www.gnu.org/licenses/>.
 */
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
