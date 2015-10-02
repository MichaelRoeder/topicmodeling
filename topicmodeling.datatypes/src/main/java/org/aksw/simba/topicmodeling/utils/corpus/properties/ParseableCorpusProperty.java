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
package org.aksw.simba.topicmodeling.utils.corpus.properties;


/**
 * This interface describes {@link CorpusProperty} classes with the ability to
 * parse their value from a given String. This can be useful for reading /
 * parsing a corpus and its properties from a text without knowing the exact
 * way in which the different properties have to be parsed.
 * 
 * @author m.roeder
 * 
 */
public interface ParseableCorpusProperty extends CorpusProperty {

	/**
	 * This method is used to let the CorpusProperty parse its value from a
	 * given String. After performing this method the CorpusProperty should
	 * have the value contained inside this String.
	 * 
	 * @param value
	 */
	public void parseValue(String value);
}
