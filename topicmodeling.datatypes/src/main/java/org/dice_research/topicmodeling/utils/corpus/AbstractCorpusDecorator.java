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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dice_research.topicmodeling.utils.corpus.properties.CorpusProperty;
import org.dice_research.topicmodeling.utils.doc.Document;


public abstract class AbstractCorpusDecorator implements CorpusDecorator {

    private static final long serialVersionUID = 3982296337238205814L;

    protected Corpus corpus;

    public AbstractCorpusDecorator(Corpus corpus) {
        this.corpus = corpus;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    @Override
    public int getNumberOfDocuments() {
        return corpus.getNumberOfDocuments();
    }

    @Override
    public Document getDocument(int documentId) {
        return corpus.getDocument(documentId);
    }

    @Override
    public void addDocument(Document document) {
        corpus.addDocument(document);
    }

    @Override
    public void clear() {
        corpus.clear();
    }

    @Override
    public Iterator<Document> iterator() {
        return corpus.iterator();
    }

    @Override
    public List<Document> getDocuments(int startId, int endId) {
        return corpus.getDocuments(startId, endId);
    }

    public Iterator<Class<? extends CorpusProperty>> getPropertiesIterator() {
        return corpus.getPropertiesIterator();
    }

    public void removeProperty(Class<? extends CorpusProperty> propertyClass) {
        corpus.removeProperty(propertyClass);
    }

    public <T extends CorpusProperty> void addProperty(T property) {
        corpus.addProperty(property);
    }

    public <T extends CorpusProperty> T getProperty(Class<T> propertyClass) {
        return corpus.getProperty(propertyClass);
    }
    
    public HashMap<Class<? extends CorpusProperty>, CorpusProperty> getProperties() {
        return corpus.getProperties();
    }

    public void setProperties(HashMap<Class<? extends CorpusProperty>, CorpusProperty> properties) {
        corpus.setProperties(properties);
    }
}
