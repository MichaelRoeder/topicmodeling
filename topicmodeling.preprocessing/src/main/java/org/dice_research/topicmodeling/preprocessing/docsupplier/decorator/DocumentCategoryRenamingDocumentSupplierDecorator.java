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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator;

import java.util.HashSet;
import java.util.Set;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.DocumentCategory;


public class DocumentCategoryRenamingDocumentSupplierDecorator extends
        AbstractPropertyEditingDocumentSupplierDecorator<DocumentCategory> {

    public static final String POSITIVE_CATEGORY_NAME = "positive";
    public static final String NEGATIVE_CATEGORY_NAME = "negative";

    private Set<String> positiveNames;

    public DocumentCategoryRenamingDocumentSupplierDecorator(DocumentSupplier documentSource,
            String positiveCategoryNames[]) {
        super(documentSource, DocumentCategory.class);
        positiveNames = new HashSet<String>(positiveCategoryNames.length);
        for (int i = 0; i < positiveCategoryNames.length; ++i) {
            positiveNames.add(positiveCategoryNames[i].toLowerCase());
        }
    }

    @Override
    protected void editDocumentProperty(DocumentCategory category) {
        if (positiveNames.contains(category.get().toLowerCase())) {
            category.set(POSITIVE_CATEGORY_NAME);
        } else {
            category.set(NEGATIVE_CATEGORY_NAME);
        }
    }
}
