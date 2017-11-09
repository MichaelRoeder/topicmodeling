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

import java.util.ArrayList;
import java.util.Collection;

import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;
import org.dice_research.topicmodeling.utils.doc.DocumentProperty;


public class PropertyRemovingSupplierDecorator extends AbstractDocumentSupplierDecorator {

    private ArrayList<Class<? extends DocumentProperty>> removableProperties;

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>();
    }

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource,
            Class<? extends DocumentProperty> removableDocumentProperty) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>();
        removableProperties.add(removableDocumentProperty);
    }

    public PropertyRemovingSupplierDecorator(DocumentSupplier documentSource,
            Collection<? extends Class<? extends DocumentProperty>> removableDocumentProperties) {
        super(documentSource);
        removableProperties = new ArrayList<Class<? extends DocumentProperty>>(removableDocumentProperties);
    }

    @Override
    public Document prepareDocument(Document document) {
        for (Class<? extends DocumentProperty> docPropClass : removableProperties) {
            document.removeProperty(docPropClass);
        }
        return document;
    }

    public ArrayList<Class<? extends DocumentProperty>> getRemovableProperties() {
        return removableProperties;
    }

    public void setRemovableProperties(
            ArrayList<Class<? extends DocumentProperty>> removableProperties) {
        this.removableProperties = removableProperties;
    }

    public void addRemovableProperty(
            Class<? extends DocumentProperty> documentPropertyClass) {
        removableProperties.add(documentPropertyClass);
    }

}
