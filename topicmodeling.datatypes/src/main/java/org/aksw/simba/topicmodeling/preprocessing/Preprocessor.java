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
package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;


public interface Preprocessor {
	/**
	 * @param documentFactory
	 */
    @Deprecated
	public void addDocuments(DocumentSupplier documentFactory);

	/**
	 * @return com.unister.semweb.topic_modeling.utils.Corpus
	 */
	public Corpus getCorpus();
	
	/**
	 * Returns true if the preprocessor has an already created corpus.
	 * 
	 * @return
	 */
	public boolean hasCorpus();
	
	public void deleteCorpus();
}
