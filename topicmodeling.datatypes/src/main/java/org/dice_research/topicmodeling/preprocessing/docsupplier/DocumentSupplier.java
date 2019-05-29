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
package org.dice_research.topicmodeling.preprocessing.docsupplier;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.dice_research.topicmodeling.utils.doc.Document;

public interface DocumentSupplier {

    public Document getNextDocument();

    public void setDocumentStartId(int documentStartId);

    public static Stream<Document> convertToStream(DocumentSupplier supplier) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new DocumentSupplierAsIterator(supplier),
                Spliterator.DISTINCT | Spliterator.ORDERED), false);
    }
}
