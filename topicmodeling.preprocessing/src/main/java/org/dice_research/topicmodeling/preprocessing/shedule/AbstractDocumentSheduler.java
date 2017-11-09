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
package org.dice_research.topicmodeling.preprocessing.shedule;

import java.util.LinkedList;
import java.util.Queue;

import org.dice_research.topicmodeling.preprocessing.docsupplier.AbstractDocumentSupplier;
import org.dice_research.topicmodeling.preprocessing.docsupplier.DocumentSupplier;
import org.dice_research.topicmodeling.utils.doc.Document;


public abstract class AbstractDocumentSheduler implements DocumentSheduler {

	protected DocumentSupplier documentSource;
	protected PartialDocumentSupplier listOfParts[];
	
	public AbstractDocumentSheduler(DocumentSupplier documentSource,
			int numberOfParts) {
		this.documentSource = documentSource;
		listOfParts = new PartialDocumentSupplier[numberOfParts];
		for (int i = 0; i < numberOfParts; ++i) {
			listOfParts[i] = new PartialDocumentSupplier(i);
		}
	}

	protected abstract Document getNextDocument(int partId);

	@Override
	public int getNumberOfParts() {
		return listOfParts.length;
	}

	@Override
	public PartialDocumentSupplier getPartialDocumentSupplier(int partId) {
		return listOfParts[partId];
	}
	
	public class PartialDocumentSupplier extends AbstractDocumentSupplier {
		protected Queue<Document> queue = new LinkedList<Document>();
		protected int id;

		public PartialDocumentSupplier(int id) {
			this.id = id;
		}

		@Override
		public Document getNextDocument() {
			Document document = null;
			if (queue.size() > 0) {
				document = queue.poll();
			} else {
				document = AbstractDocumentSheduler.this.getNextDocument(id);
			}
			if(document != null)
			{
				document.setDocumentId(this.getNextDocumentId());
			}
			return document;
		}

		public int getId() {
			return id;
		}

		public void addDocumentToQueue(Document document) {
			queue.offer(document);
		}
	}
}
