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
package org.aksw.simba.topicmodeling.preprocessing.docsupplier.decorator.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.aksw.simba.topicmodeling.utils.doc.Document;
import org.aksw.simba.topicmodeling.utils.doc.DocumentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CategoryBasedDocumentFilter implements DocumentFilter {

    private static final Logger logger = LoggerFactory.getLogger(CategoryBasedDocumentFilter.class);

    private Set<String> categorieNames;
    private boolean categoriesAreGood;

    public CategoryBasedDocumentFilter(Collection<String> categorieNames, boolean categoriesAreGood) {
        this.categorieNames = new HashSet<String>(categorieNames);
        this.categoriesAreGood = categoriesAreGood;
    }

    public CategoryBasedDocumentFilter(String categorieNames[], boolean categoriesAreGood) {
        this.categorieNames = new HashSet<String>(Arrays.asList(categorieNames));
        this.categoriesAreGood = categoriesAreGood;
    }

    @Override
    public boolean isDocumentGood(Document document) {
        DocumentCategory category = document.getProperty(DocumentCategory.class);
        if (category == null)
        {
            logger.error("Got a Document without a DocumentCategory property!");
            return false;
        }
        if (categorieNames.contains(category.getCategory())) {
            return categoriesAreGood;
        } else {
            return !categoriesAreGood;
        }
    }

}
