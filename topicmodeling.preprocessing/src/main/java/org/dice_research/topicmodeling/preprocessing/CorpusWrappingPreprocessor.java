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
package org.dice_research.topicmodeling.preprocessing;

import org.dice_research.topicmodeling.preprocessing.Preprocessor;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.corpus.AbstractCorpusDecorator;
import org.dice_research.topicmodeling.utils.corpus.Corpus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CorpusWrappingPreprocessor extends AbstractCorpusDecorator implements Preprocessor {
	
	private static final long serialVersionUID = 7173055356063738593L;
	
	private static final Logger logger = LoggerFactory.getLogger(CorpusWrappingPreprocessor.class);
	
	public CorpusWrappingPreprocessor(Corpus corpus) {
		super(corpus);
	}

	@Override
	public void addDocuments(DocumentSupplier documentFactory) {
		logger.info("addDocuments was called. But my corpus is fixed and I can not change it.");
	}

    @Override
    public boolean hasCorpus() {
        return corpus != null;
    }

    @Override
    public void deleteCorpus() {
        corpus = null;
    }
}
