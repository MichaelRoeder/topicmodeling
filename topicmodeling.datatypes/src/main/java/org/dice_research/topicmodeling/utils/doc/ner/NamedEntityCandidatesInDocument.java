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
import java.util.List;

import org.dice_research.topicmodeling.utils.doc.DocumentProperty;


public class NamedEntityCandidatesInDocument implements DocumentProperty {

    private static final long serialVersionUID = -2029263266533563292L;

    private List<NamedEntityCandidate> namedEntities;

    public NamedEntityCandidatesInDocument() {
        this(new ArrayList<NamedEntityCandidate>());
    }

    public NamedEntityCandidatesInDocument(List<NamedEntityCandidate> namedEntities) {
        this.namedEntities = namedEntities;
    }

    @Override
    public Object getValue() {
        return namedEntities;
    }

    public List<NamedEntityCandidate> getNamedEntities() {
        return namedEntities;
    }

    public void setNamedEntities(List<NamedEntityCandidate> namedEntities) {
        this.namedEntities = namedEntities;
    }

    public void addNamedEntity(NamedEntityCandidate namedEntity) {
        this.namedEntities.add(namedEntity);
    }
}