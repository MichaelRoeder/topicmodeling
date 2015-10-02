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
package org.aksw.simba.topicmodeling.preprocessing;

import org.aksw.simba.topicmodeling.preprocessing.Preprocessor;
import org.aksw.simba.topicmodeling.utils.corpus.Corpus;


public interface ScaleablePreprocessor extends Preprocessor {

    /**
     * @return com.unister.semweb.topic_modeling.utils.Corpus
     */
    public Corpus getCorpus(int numberOfDocuments);

}
