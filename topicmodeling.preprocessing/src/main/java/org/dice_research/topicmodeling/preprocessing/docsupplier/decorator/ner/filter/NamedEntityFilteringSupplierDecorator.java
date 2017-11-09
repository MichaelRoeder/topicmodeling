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
package org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.ner.filter;

import java.util.ArrayList;
import java.util.List;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.decorator.AbstractDocumentSupplierDecorator;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntitiesInText;
import org.dice_research.topicmodeling.utils.doc.ner.NamedEntityInText;


public class NamedEntityFilteringSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private NamedEntitiesFilter filter;
    private boolean discardDocumentsWithoutNEs;

    public NamedEntityFilteringSupplierDecorator(DocumentSupplier documentSource, NamedEntitiesFilter filter) {
        this(documentSource, filter, false);
    }

    public NamedEntityFilteringSupplierDecorator(DocumentSupplier documentSource, NamedEntitiesFilter filter,
            boolean discardDocumentsWithoutNEs) {
        super(documentSource);
        this.filter = filter;
        this.discardDocumentsWithoutNEs = discardDocumentsWithoutNEs;
    }

    @Override
    protected Document prepareDocument(Document document) {
        NamedEntitiesInText entities = document.getProperty(NamedEntitiesInText.class);
        if (entities == null) {
            if (discardDocumentsWithoutNEs) {
                document = this.getNextDocument();
            }
        } else {
            entities = filterEntities(entities, document);
            document.addProperty(entities);
        }
        return document;
    }

    private NamedEntitiesInText filterEntities(NamedEntitiesInText entities, Document document) {
        if (entities.getNamedEntities().size() == 0) {
            return entities;
        }
        List<NamedEntityInText> filteredEntities = new ArrayList<NamedEntityInText>(entities.getNamedEntities().size());
        for (NamedEntityInText entity : entities) {
            if (filter.isNamedEntityGood(document, entity)) {
                filteredEntities.add(entity);
            }
        }
        return new NamedEntitiesInText(filteredEntities);
    }
}
