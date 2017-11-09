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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter;

import java.util.Arrays;
import java.util.Collection;

import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter.DocumentFilter;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter.StringContainingDocumentPropertyBasedFilter;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.filter.StringContainingDocumentPropertyBasedFilter.StringContainingDocumentPropertyBasedFilterType;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentName;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class DocumentNameBasedFilterTest {

    private static Document DOCUMENTS[] = new Document[] {
            new Document(1, new DocumentProperty[] { new DocumentName("Document1") }),
            new Document(2, new DocumentProperty[] { new DocumentName("1998") }),
            new Document(3, new DocumentProperty[] { new DocumentName("Himmel, A***h und Zwirn") }) };

    private StringContainingDocumentPropertyBasedFilterType filterType;
    private String name;
    private boolean ignoreCase;
    private boolean isGoodDocument[];

    public DocumentNameBasedFilterTest(StringContainingDocumentPropertyBasedFilterType filterType, String name,
            boolean ignoreCase,
            boolean isGoodDocument[]) {
        super();
        this.filterType = filterType;
        this.name = name;
        this.ignoreCase = ignoreCase;
        this.isGoodDocument = isGoodDocument;
    }

    @Test
    public void testFilter() {
        DocumentFilter filter = new StringContainingDocumentPropertyBasedFilter<DocumentName>(filterType,
                DocumentName.class, name, ignoreCase);
        for (int d = 0; d < DOCUMENTS.length; ++d) {
            Assert.assertTrue("The filter rated Document " + DOCUMENTS[d] + " not as expected (" + isGoodDocument[d]
                    + ").", isGoodDocument[d] == filter.isDocumentGood(DOCUMENTS[d]));
        }
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { StringContainingDocumentPropertyBasedFilterType.EQUALS, "Document1", false,
                        new boolean[] { true, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.EQUALS, "document1", false,
                        new boolean[] { false, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.EQUALS, "document1", true,
                        new boolean[] { true, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.EQUALS_NOT, "Document1", false,
                        new boolean[] { false, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.EQUALS_NOT, "document1", false,
                        new boolean[] { true, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.EQUALS_NOT, "document1", true,
                        new boolean[] { false, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.CONTAINS, "UND", false,
                        new boolean[] { false, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.CONTAINS, "UND", true,
                        new boolean[] { false, false, true } },
                { StringContainingDocumentPropertyBasedFilterType.CONTAINS_NOT, "UND", false,
                        new boolean[] { true, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.CONTAINS_NOT, "UND", true,
                        new boolean[] { true, true, false } },
                { StringContainingDocumentPropertyBasedFilterType.ENDS_WITH, "zwirn", false,
                        new boolean[] { false, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.ENDS_WITH, "zwirn", true,
                        new boolean[] { false, false, true } },
                { StringContainingDocumentPropertyBasedFilterType.ENDS_NOT_WITH, "zwirn", false,
                        new boolean[] { true, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.ENDS_NOT_WITH, "zwirn", true,
                        new boolean[] { true, true, false } },
                { StringContainingDocumentPropertyBasedFilterType.STARTS_WITH, "himmel", false,
                        new boolean[] { false, false, false } },
                { StringContainingDocumentPropertyBasedFilterType.STARTS_WITH, "himmel", true,
                        new boolean[] { false, false, true } },
                { StringContainingDocumentPropertyBasedFilterType.STARTS_NOT_WITH, "himmel", false,
                        new boolean[] { true, true, true } },
                { StringContainingDocumentPropertyBasedFilterType.STARTS_NOT_WITH, "himmel", true,
                        new boolean[] { true, true, false } },
                { StringContainingDocumentPropertyBasedFilterType.MATCHES_PATTERN, "[\\d]*", false,
                        new boolean[] { false, true, false } },
                { StringContainingDocumentPropertyBasedFilterType.MATCHES_PATTERN, "[\\d]*", true,
                        new boolean[] { false, true, false } },
                { StringContainingDocumentPropertyBasedFilterType.MATCHES_NOT_PATTERN, "[\\d]*", false,
                        new boolean[] { true, false, true } },
                { StringContainingDocumentPropertyBasedFilterType.MATCHES_NOT_PATTERN, "[\\d]*", true,
                        new boolean[] { true, false, true } } };
        return Arrays.asList(data);
    }
}
