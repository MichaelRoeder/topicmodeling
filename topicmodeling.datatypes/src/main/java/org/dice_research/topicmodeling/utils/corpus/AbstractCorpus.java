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

import org.dice_research.topicmodeling.utils.corpus.properties.CorpusProperty;


public abstract class AbstractCorpus implements Corpus {

    private static final long serialVersionUID = -5837383654657177879L;

    protected HashMap<Class<? extends CorpusProperty>, CorpusProperty> properties;

    public AbstractCorpus() {
        properties = new HashMap<Class<? extends CorpusProperty>, CorpusProperty>();
    }

    public Iterator<Class<? extends CorpusProperty>> getPropertiesIterator() {
        return properties.keySet().iterator();
    }

    public void removeProperty(Class<? extends CorpusProperty> propertyClass) {
        properties.remove(propertyClass);
    }

    /**
     * @param property
     */
    public <T extends CorpusProperty> void addProperty(T property) {
        properties.put(property.getClass(), property);
    }

    /**
     * @return 
     * @param propertyClass
     */
    @SuppressWarnings("unchecked")
    public <T extends CorpusProperty> T getProperty(Class<T> propertyClass) {
        return (T) properties.get(propertyClass);
    }

    public HashMap<Class<? extends CorpusProperty>, CorpusProperty> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<Class<? extends CorpusProperty>, CorpusProperty> properties) {
        this.properties = properties;
    }
}
