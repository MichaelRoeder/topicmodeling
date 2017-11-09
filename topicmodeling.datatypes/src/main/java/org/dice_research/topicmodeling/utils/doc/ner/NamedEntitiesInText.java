/**
 * This file is part of topicmodeling.datatypes.
 *
 * topicmodeling.datatypes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * topicmodeling.datatypes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with topicmodeling.datatypes.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dice_research.topicmodeling.utils.doc.ner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dice_research.topicmodeling.utils.doc.DocumentProperty;


public class NamedEntitiesInText implements DocumentProperty, Iterable<NamedEntityInText> {

    private static final long serialVersionUID = 3912544557154321487L;

    private List<NamedEntityInText> namedEntities = new ArrayList<NamedEntityInText>();

    public NamedEntitiesInText() {
    }

    public NamedEntitiesInText(NamedEntityInText... namedEntities) {
        this(Arrays.asList(namedEntities));
    }

    public NamedEntitiesInText(List<NamedEntityInText> namedEntities) {
        this.namedEntities.addAll(namedEntities);
    }

    @Override
    public Object getValue() {
        return namedEntities;
    }

    public List<NamedEntityInText> getNamedEntities() {
        return namedEntities;
    }

    public void setNamedEntities(List<NamedEntityInText> namedEntities) {
        this.namedEntities = namedEntities;
    }

    public void addNamedEntity(NamedEntityInText namedEntity) {
        namedEntities.add(namedEntity);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("NamedEntitiesInText=\"[");
        boolean first = true;
        for (NamedEntityInText ne : namedEntities) {
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(ne.toString());
        }
        result.append(']');
        return result.toString();
    }

    @Override
    public Iterator<NamedEntityInText> iterator() {
        return namedEntities.iterator();
    }
}