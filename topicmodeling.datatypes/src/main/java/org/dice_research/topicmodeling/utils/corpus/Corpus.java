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
package org.dice_research.topicmodeling.utils.corpus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dice_research.topicmodeling.utils.corpus.properties.CorpusProperty;
import org.dice_research.topicmodeling.utils.doc.Document;


public interface Corpus extends Serializable, Iterable<Document> {

    public int getNumberOfDocuments();

    public Document getDocument(int documentId);

    public void addDocument(Document document);

    public void clear();

    public List<Document> getDocuments(int startId, int endId);

    public <T extends CorpusProperty> void addProperty(T property);

    public void removeProperty(Class<? extends CorpusProperty> propertyClass);

    public <T extends CorpusProperty> T getProperty(Class<T> propertyClass);

    public Iterator<Class<? extends CorpusProperty>> getPropertiesIterator();

    public HashMap<Class<? extends CorpusProperty>, CorpusProperty> getProperties();

    public void setProperties(HashMap<Class<? extends CorpusProperty>, CorpusProperty> properties);
}
